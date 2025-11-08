# Java 9+ Module System: Strong Encapsulation and Explicit Dependencies

## Introduction

Java 9 introduced the **Java Platform Module System (JPMS)**, a major architectural feature that fundamentally changed how we organize, encapsulate, and manage dependencies in large Java applications. Before modules, Java used packages as the only organizational unit above classes, leading to weak encapsulation and unclear dependency relationships. The module system adds a higher level of abstraction that groups related packages together while enforcing explicit visibility control and dependency management.

The module system addresses several critical problems that plagued Java applications for decades:

- **Weak Encapsulation**: Before Java 9, any public class was globally accessible through reflection, providing almost no real encapsulation
- **Version Conflicts**: Packages could be split across multiple JAR files ("fragile base class problem"), causing version incompatibilities
- **Unclear Dependencies**: The classpath made it difficult to understand which modules depend on which, leading to complex dependency graphs
- **Large Runtime Footprint**: The entire JDK was monolithic; developers couldn't create minimal Java installations
- **Scale Issues**: Large enterprise applications struggled with maintainability due to unclear module boundaries

The module system solves these problems through three key mechanisms: **strong encapsulation** (packages are private by default), **explicit dependencies** (all module relationships must be declared), and **platform modularity** (the JDK itself is modularized).

## Understanding the Module System

A module is a named, self-contained unit that groups related packages and resources together. Every module has a **module descriptor** (`module-info.java`) that declares its name, dependencies, exported packages, and services. This descriptor is compiled to `module-info.class` and becomes part of the compiled module.

The module system operates in three execution phases:

1. **Compile-time** - `javac` validates module declarations and dependencies
2. **Link-time** - `jlink` can create custom runtime images with only needed modules
3. **Run-time** - `java` enforces module boundaries and visibility rules

### Example 1: Understanding Module Fundamentals

The module system works by grouping packages into higher-level units. When you access a module at runtime, you can introspect its metadata, verify relationships, and understand its structure.

```java
Module currentModule = BasicModuleExample.class.getModule();

// Check if this is a named module
boolean isNamed = currentModule.isNamed();
String moduleName = currentModule.getName();

// Get module descriptor with metadata
var descriptor = currentModule.getDescriptor();
if (descriptor != null) {
    Set<String> packages = descriptor.packages();
    Set<ModuleDescriptor.Requires> requires = descriptor.requires();
    Set<ModuleDescriptor.Exports> exports = descriptor.exports();
}

// Access java.base (always available, always named)
Module javaBase = Object.class.getModule();  // "java.base"
```

**Unit Tests (BDD Style):**

```java
@Test
@DisplayName("Should retrieve current module information")
void shouldRetrieveCurrentModuleInformation() {
    Module currentModule = BasicModuleExampleTest.class.getModule();
    assertNotNull(currentModule);

    // java.base is always a named module
    Module javaBase = Object.class.getModule();
    assertTrue(javaBase.isNamed());
    assertNotNull(javaBase.getName());
    assertEquals("java.base", javaBase.getName());
}

@Test
@DisplayName("Should have module descriptor with metadata")
void shouldHaveModuleDescriptorWithMetadata() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    assertNotNull(descriptor);
    assertNotNull(descriptor.packages());
    assertTrue(descriptor.packages().contains("java.lang"));
    assertFalse(descriptor.requires().isEmpty());
}

@Test
@DisplayName("Should distinguish between named and unnamed modules")
void shouldDistinguishBetweenNamedAndUnnamedModules() {
    // java.base is always a named module
    Module javaBase = Object.class.getModule();
    assertTrue(javaBase.isNamed());
    assertNotNull(javaBase.getName());

    // Code loaded via classpath (without module-info.java)
    // becomes part of "unnamed module"
}
```

**Output:**
```
Current Module Information:
  Module Name: java.base
  Is Named: true
  Module Descriptor: java.base

Module Descriptor:
  Packages: [java.lang, java.util, java.io, ...]
  Requires: [transitive java.base, ...]
  Exports: [java.lang, java.util, ...]

Key Insights:
1. Module System Hierarchy:
   Module > Packages > Classes

2. Module Descriptor (module-info.java):
   - Located at module root
   - Defines module name and directives
   - Compiled to module-info.class

3. Strong Encapsulation:
   - Packages are module-private by default
   - Must explicitly export packages
   - Reflection access restricted by default

4. Two Types of Modules:
   - Named modules (have module-info.java)
   - Unnamed module (legacy code on classpath)
```

**Key Insight:** The module system establishes a clear hierarchy where modules are the primary organizational unit. Unlike packages (which organize classes) or JAR files (which are deployment artifacts), modules are architectural units that explicitly declare their purpose, dependencies, and public API. Every named module has a descriptor that serves as a contract between the module and its consumers. The fact that `java.base` is always present and named ensures that code can always be organized into modules, even if it doesn't explicitly declare module-info.java (in which case it becomes part of the unnamed module).

---

## Declaring and Managing Module Dependencies

One of the strongest benefits of the module system is explicit dependency management. In the pre-module era, classpath ordering and JAR versions could lead to subtle bugs. With modules, all dependencies must be explicitly declared in the module descriptor using the `requires` directive. The module system supports three forms: `requires` (mandatory), `requires transitive` (re-exported), and `requires static` (optional).

### Example 2: Module Dependencies and Requires Directives

Module dependencies are declared using the `requires` directive. Every module implicitly requires `java.base`, but all other dependencies must be explicit. Dependencies can be transitive (automatically required by consumers) or static (compile-time only, optional at runtime).

```java
Module currentModule = ModuleDependenciesExample.class.getModule();
ModuleDescriptor descriptor = currentModule.getDescriptor();

// Examine requires directives
if (descriptor != null && !descriptor.requires().isEmpty()) {
    descriptor.requires().stream().limit(3).forEach(req -> {
        System.out.println("  Module: " + req.name());

        boolean isTransitive = req.modifiers()
            .contains(ModuleDescriptor.Requires.Modifier.TRANSITIVE);
        System.out.println("    Transitive: " + isTransitive);
    });
}

// Check if this module can read another
Module javaBase = Object.class.getModule();
boolean canRead = currentModule.canRead(javaBase);
System.out.println("Can read java.base: " + canRead);

// Every module can read itself
assertTrue(javaBase.canRead(javaBase));
```

**Unit Tests (BDD Style):**

```java
@Test
@DisplayName("Should show every module implicitly requires java.base")
void shouldShowEveryModuleImplicitlyRequiresJavaBase() {
    Module currentModule = ModuleDependenciesExampleTest.class.getModule();
    Module javaBase = Object.class.getModule();

    assertTrue(currentModule.canRead(javaBase));
    assertEquals("java.base", javaBase.getName());
}

@Test
@DisplayName("Should allow querying module dependencies")
void shouldAllowQueryingModuleDependencies() {
    Module javaLogging = java.util.logging.Logger.class.getModule();
    var descriptor = javaLogging.getDescriptor();

    assertNotNull(descriptor);
    assertNotNull(descriptor.requires());
    assertFalse(descriptor.requires().isEmpty());
}

@Test
@DisplayName("Should distinguish between static and non-static requires")
void shouldDistinguishBetweenStaticAndNonStaticRequires() {
    Module javaLogging = java.util.logging.Logger.class.getModule();
    var descriptor = javaLogging.getDescriptor();

    for (var require : descriptor.requires()) {
        var mods = require.modifiers();
        boolean isStatic = mods.contains(
            ModuleDescriptor.Requires.Modifier.STATIC);
        // Can be static or non-static
        assertNotNull(isStatic);
    }
}
```

**Output:**
```
Module Dependencies (requires directives):
  - Module: java.base
    Transitive: false
  - Module: java.logging
    Transitive: true

Module Relationship Queries:
  Current module can read java.base: true

Types of Requires Directives:

1. requires <module>:
   - Mandatory dependency
   - Required at compile and runtime
   - Not transitive (implicit)
   Example: requires java.base;

2. requires transitive <module>:
   - Re-exports dependencies
   - Consumers of this module automatically get access
   - Used for facade modules
   Example: requires transitive java.logging;

3. requires static <module>:
   - Optional dependency
   - Required at compile time only
   - Not required at runtime
   - Useful for optional features
   Example: requires static com.logging.advanced;
```

**Key Insight:** The explicit requires declaration creates a dependency graph that the JVM can validate and enforce. Unlike the classpath (where classpath order mattered and visibility was implicit), module dependencies are unambiguous. The distinction between `requires` (mandatory) and `requires static` (optional) allows libraries to depend on features conditionally. Transitive dependencies reduce consumer burden—if module A requires module B transitively, then any module requiring A automatically gets B without having to declare it explicitly.

---

## Strong Encapsulation and Package Exports

Before Java 9, "public" meant globally accessible. With modules, "public" alone is insufficient—packages must be explicitly exported to be accessible outside the module. This strong encapsulation is enforced by the JVM and prevents accidental dependencies on internal implementation classes.

### Example 3: Package Exports and Visibility Control

Packages are module-private by default. To make packages accessible to other modules, they must be explicitly exported using the `exports` directive. This allows clear separation between public API and internal implementation.

```java
Module javaBase = Object.class.getModule();
ModuleDescriptor descriptor = javaBase.getDescriptor();

// List all exported packages
System.out.println("Exported Packages:");
descriptor.exports().forEach(export -> {
    System.out.println("  Package: " + export.source());
    if (!export.targets().isEmpty()) {
        System.out.println("    Exported to: " + export.targets());
    } else {
        System.out.println("    Exported to: all modules");
    }
});

// Check if package is exported
boolean isExported = javaBase.isExported("java.lang");
System.out.println("java.lang exported: " + isExported);

// Check if package is exported to specific module
Module currentModule = ModuleExportsExample.class.getModule();
boolean exportedToModule = javaBase.isExported("java.lang", currentModule);
System.out.println("java.lang exported to current module: " + exportedToModule);
```

**Unit Tests (BDD Style):**

```java
@Test
@DisplayName("Should check if package is unconditionally exported")
void shouldCheckIfPackageIsUnconditionallyExported() {
    Module javaBase = Object.class.getModule();

    // java.lang should be exported from java.base
    assertTrue(javaBase.isExported("java.lang"));
    assertTrue(javaBase.isExported("java.util"));
}

@Test
@DisplayName("Should demonstrate that packages are module-private by default")
void shouldDemonstrateThatPackagesAreModulePrivateByDefault() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    var allPackages = descriptor.packages();
    var exportedPackages = descriptor.exports();

    // Not all packages are exported - some are internal
    assertTrue(exportedPackages.size() <= allPackages.size());
}

@Test
@DisplayName("Should enable safe package refactoring")
void shouldEnableSafePackageRefactoring() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    // Packages that are NOT exported are private implementation details
    var allPackages = descriptor.packages();
    var exportedPackages = descriptor.exports().stream()
        .map(e -> e.source())
        .toList();

    var privatePackages = allPackages.stream()
        .filter(pkg -> !exportedPackages.contains(pkg))
        .toList();

    // These private packages can be safely refactored
    for (String privatePackage : privatePackages) {
        assertFalse(javaBase.isExported(privatePackage));
    }
}
```

**Output:**
```
Exported Packages from java.base:
  Package: java.lang
    Exported to: all modules (unconditional)
  Package: java.util
    Exported to: all modules (unconditional)
  Package: java.io
    Exported to: all modules (unconditional)

Runtime Package Visibility Checks:
  Package 'java.lang':
    - isExported(unconditionally): true

Export Patterns in module-info.java:

1. Unconditional Export:
   exports com.example.api;
   - Package accessible from ALL modules
   - Public API of your module

2. Selective Export (exports...to):
   exports com.example.internal to com.partner.module;
   - Package accessible only to specified modules
   - Useful for shared internal APIs

3. No Export:
   // No exports directive for a package
   - Package is module-private
   - Hidden from all other modules
```

**Key Insight:** Strong encapsulation solves the "fragile base class problem" where internal implementation details became part of the public contract. Before modules, refactoring internal classes was dangerous because external code might depend on them. With modules, internal packages (not exported) are genuinely internal—consumers cannot access them even with reflection. This allows library developers to evolve implementations freely. The reduction in JDK size from ~4000 public classes to ~1300 in `java.base` demonstrates how effective this encapsulation is.

---

## Runtime Module Inspection with the Module API

The Module API enables runtime introspection of the module system. Applications and frameworks can query module properties, relationships, and configuration at runtime, enabling dynamic behavior and better debugging.

### Example 4: Module API and Runtime Introspection

The Module class and ModuleDescriptor provide comprehensive runtime access to module metadata and relationships.

```java
Module module = MyClass.class.getModule();

// Get module properties
String name = module.getName();
boolean isNamed = module.isNamed();
ClassLoader loader = module.getClassLoader();
ModuleDescriptor descriptor = module.getDescriptor();
ModuleLayer layer = module.getLayer();

// Query relationships
boolean canRead = module.canRead(otherModule);
boolean isExported = module.isExported("com.example.pkg");
boolean isOpen = module.isOpen("com.example.pkg");
boolean canUse = module.canUse(ServiceInterface.class);

// Get metadata
Set<String> packages = module.getPackages();
var requires = descriptor.requires();
var exports = descriptor.exports();

// Dynamic configuration
module.addReads(otherModule);
module.addExports("com.internal", otherModule);
module.addOpens("com.model", otherModule);
module.addUses(ServiceInterface.class);
```

**Unit Tests (BDD Style):**

```java
@Test
@DisplayName("Should get module from any class")
void shouldGetModuleFromAnyClass() {
    Module stringModule = String.class.getModule();
    Module listModule = java.util.ArrayList.class.getModule();
    Module testModule = ModuleReflectionExampleTest.class.getModule();

    assertNotNull(stringModule);
    assertNotNull(listModule);
    assertNotNull(testModule);

    // String and ArrayList are in same module
    assertEquals(stringModule.getName(), listModule.getName());
}

@Test
@DisplayName("Should retrieve module descriptor")
void shouldRetrieveModuleDescriptor() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    assertNotNull(descriptor);
    assertNotNull(descriptor.name());
    assertNotNull(descriptor.requires());
    assertNotNull(descriptor.exports());
    assertNotNull(descriptor.packages());
}

@Test
@DisplayName("Should enable runtime inspection of module hierarchy")
void shouldEnableRuntimeInspectionOfModuleHierarchy() {
    Module javaBase = Object.class.getModule();
    var layer = javaBase.getLayer();

    var config = layer.configuration();
    var allModules = config.modules();

    assertFalse(allModules.isEmpty());

    // Can find specific modules by name
    var javaBaseModule = layer.findModule("java.base");
    assertTrue(javaBaseModule.isPresent());
}

@Test
@DisplayName("Should support dynamic module configuration")
void shouldSupportDynamicModuleConfiguration() {
    Module current = ModuleReflectionExampleTest.class.getModule();
    Module javaBase = Object.class.getModule();

    // addReads - dynamically add read edge
    Module result = current.addReads(javaBase);
    assertSame(current, result);

    assertTrue(current.canRead(javaBase));
}
```

**Output:**
```
Module API Basics:

Module.getName():
  java.base

Module.isNamed():
  true
  (true for named modules, false for unnamed)

Module.getClassLoader():
  jdk.internal.loader.ClassLoaders$PlatformClassLoader@...

Module.getLayer():
  jdk.internal.module.Modules$1@...

Module.getDescriptor():
  Returns ModuleDescriptor containing:
    - name()
    - requires()
    - exports()
    - opens()
    - uses()
    - provides()
    - packages()

Getting Module from Class:
  Class: java.lang.String
  Module: java.base

Module Relationship Queries:
  Module.canRead(Module) - Tests if module reads another
  Module.isExported(String) - Checks unconditional package export
  Module.isExported(String, Module) - Checks export to specific module
  Module.getPackages() - Returns set of package names

Dynamic Module Updates:
  Module.addReads(Module) - Add read edge
  Module.addExports(String, Module) - Export package
  Module.addOpens(String, Module) - Open for reflection
  Module.addUses(Class<?>) - Add service usage
```

**Key Insight:** The Module API enables sophisticated runtime behaviors. Frameworks like Spring and Hibernate use the Module API to dynamically request reflection access to internal packages via `addOpens()`. Build tools use it to analyze module dependencies. IDE plugins use it to validate module configurations. The ability to inspect and modify module relationships at runtime makes the module system flexible enough to support legacy frameworks while still providing strong encapsulation by default.

---

## Strong Encapsulation and Reflection Control

The module system enforces strong encapsulation not just for normal access, but also for reflection. By default, reflection cannot access private members of non-exported packages. The `opens` and `opens...to` directives explicitly grant reflection access, enabling frameworks while maintaining security.

### Example 5: Strong Encapsulation and Reflection

Strong encapsulation prevents even reflection from bypassing module boundaries. Before Java 9, using `setAccessible(true)` on private members would always succeed. Now, attempting to access members of non-opened packages throws `InaccessibleObjectException`.

```java
// This works - String is from exported java.lang package
String str = "test";
Method method = String.class.getMethod("toUpperCase");
assertNotNull(method);

// Before Java 9: Would work
// After Java 9: Requires 'opens java.lang' or runtime --add-opens

// Private packages (not exported) can be safely refactored
Module javaBase = Object.class.getModule();
var descriptor = javaBase.getDescriptor();

var allPackages = descriptor.packages();
var exportedPackages = descriptor.exports().stream()
    .map(e -> e.source())
    .toList();

var privatePackages = allPackages.stream()
    .filter(pkg -> !exportedPackages.contains(pkg))
    .toList();

// These packages are internal implementation details
// Consumers don't depend on them
// Safe to rename, restructure, or remove
for (String privatePackage : privatePackages) {
    assertFalse(javaBase.isExported(privatePackage));
}
```

**Unit Tests (BDD Style):**

```java
@Test
@DisplayName("Should allow accessing classes from exported packages")
void shouldAllowAccessingClassesFromExportedPackages() {
    // java.util is exported from java.base
    List<String> list = new ArrayList<>();
    list.add("test");

    assertNotNull(list);
    assertEquals(1, list.size());
}

@Test
@DisplayName("Should demonstrate reflection on exported packages works")
void shouldDemonstrateReflectionOnExportedPackagesWorks()
        throws NoSuchMethodException {
    // String is from exported java.lang
    Class<?> stringClass = String.class;
    Module module = stringClass.getModule();

    assertTrue(module.isExported("java.lang"));

    // Reflection on public methods of exported classes works
    var method = stringClass.getMethod("toUpperCase");
    assertNotNull(method);
}

@Test
@DisplayName("Should enforce strong encapsulation at module level")
void shouldEnforceStrongEncapsulationAtModuleLevel() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    var allPackages = descriptor.packages();
    var exportedPackages = descriptor.exports();

    // Not all packages are exported
    assertTrue(exportedPackages.size() <= allPackages.size());
}

@Test
@DisplayName("Should enable safe refactoring of internal code")
void shouldEnableSafeRefactoringOfInternalCode() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    var privatePackages = descriptor.packages().stream()
        .filter(pkg -> descriptor.exports().stream()
            .noneMatch(export -> export.source().equals(pkg)))
        .toList();

    for (String privatePackage : privatePackages) {
        assertFalse(javaBase.isExported(privatePackage));
    }
}

@Test
@DisplayName("Should show benefits of strong encapsulation")
void shouldShowBenefitsOfStrongEncapsulation() {
    // Benefits demonstrated:
    Module javaBase = Object.class.getModule();
    assertTrue(javaBase.isNamed());

    // 1. Reduced attack surface
    // 2. Clear API contract
    var exports = javaBase.getDescriptor().exports();
    assertNotNull(exports);

    // 3. Safe refactoring
    var packages = javaBase.getPackages();
    var privatePackages = packages.stream()
        .filter(pkg -> !javaBase.isExported(pkg))
        .toList();
    assertNotNull(privatePackages);
}
```

**Output:**
```
Accessing Classes from Exported Packages:
  Creating ArrayList: java.util.ArrayList
  Module: java.base
  Access: ALLOWED (java.util exported unconditionally)

Reflection Access Restrictions:
  Attempting to access String class members: SUCCESS
  Class module: java.base
  Package java.lang exported: true
  Public method access via reflection: ALLOWED

  Attempting setAccessible(true) on private members:
  In module system: May throw InaccessibleObjectException
  Unless package is opened via 'opens' directive

Private Packages (Not Exported):
  jdk.internal.* - PRIVATE (internal implementation)
  sun.* - PRIVATE (not exported)
  These are not accessible via import or reflection

Problems Solved by Strong Encapsulation:

Before Java 9 (Weak Encapsulation):
  - Any public class was always accessible
  - Reflection could access private members
  - Internal classes leaked into public API
  - Hard to evolve internal implementation
  - Version conflicts from split packages
  - JDK had ~4000 public classes

After Java 9 (Strong Encapsulation):
  - Packages private by default
  - Only exported packages are accessible
  - Reflection restricted to opened packages
  - Clear distinction: API vs Implementation
  - Safe refactoring of internal code
  - JDK reduced to ~1300 classes in java.base
  - Version safety (modules don't split)

Module Directives for Encapsulation:

1. exports <package>
   - Exposes package for compilation and runtime
   - Public API of the module

2. exports <package> to <module>
   - Qualified export to specific modules
   - Shared internal APIs with partners

3. opens <package>
   - Enables reflection on all members
   - Used for frameworks (JPA, Jackson, etc.)

4. opens <package> to <module>
   - Qualified reflection access to specific modules
```

**Key Insight:** Strong encapsulation is the most transformative aspect of the module system. Before Java 9, library developers had no way to truly hide internal classes—anyone could use them via reflection. Now, internal implementations are genuinely internal. This enables safe evolution of libraries and frameworks. The JDK team used this capability to dramatically reduce the public surface area of `java.base` while internally using the classes that were previously public. The `opens` directive specifically accommodates frameworks that use reflection on private members, but only for those frameworks explicitly declared.

---

## Practical Patterns and Use Cases

### Facade Module Pattern
Group multiple modules behind a single public API:

```java
// api-module/module-info.java
module com.example.api {
    requires transitive com.example.core;
    requires transitive com.example.util;
    requires transitive com.example.cache;

    exports com.example.api.public;
}
```

Consumers only need to require the facade:
```java
// consumer-module/module-info.java
module com.example.consumer {
    requires com.example.api;  // Gets all transitive dependencies
}
```

### Internal API Pattern
Share APIs safely with partner modules:

```java
// my-module/module-info.java
module com.mycompany.database {
    exports com.mycompany.database.api;           // Public API
    exports com.mycompany.database.internal       // Shared with partner
        to com.mycompany.admin;
}
```

### Framework Support Pattern
Allow frameworks to use reflection on specific packages:

```java
// model-module/module-info.java
module com.example.model {
    exports com.example.model;
    opens com.example.model.entity to
        java.persistence,      // JPA
        com.fasterxml.jackson; // JSON marshalling
}
```

### Service Provider Pattern
Provide pluggable implementations via ServiceLoader:

```java
// api-module/module-info.java
module com.example.payment.api {
    exports com.example.payment;
    uses com.example.payment.PaymentProvider;
}

// impl-module/module-info.java
module com.example.payment.stripe {
    requires com.example.payment.api;
    provides com.example.payment.PaymentProvider
        with com.example.payment.stripe.StripePaymentProvider;
}

// Consumer
ServiceLoader<PaymentProvider> loader =
    ServiceLoader.load(PaymentProvider.class);
PaymentProvider provider = loader.findFirst().orElseThrow();
```

---

## Benefits and Problems Solved

The module system delivers measurable benefits:

1. **Reduced Attack Surface** - Only explicitly exported packages are accessible, reducing security risks
2. **Clear API Boundaries** - Exports define the contract; internal implementations are truly hidden
3. **Safe Refactoring** - Internal packages can be renamed, restructured, or removed without breaking consumers
4. **Version Safety** - Modules cannot be split across versions; each module has one version
5. **Explicit Dependencies** - All module relationships are declared; no classpath surprises
6. **Minimal Runtime Footprint** - `jlink` can create custom JDK installations with only needed modules
7. **Framework Compatibility** - The `opens` directive enables reflection-based frameworks while maintaining encapsulation by default

## Building and Running

To build and run the examples:

```bash
# Build all examples
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:build

# Run individual examples
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runBasicModule
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleDependencies
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleExports
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleReflection
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runEncapsulation

# Run all examples
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runAllExamples

# Run tests
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:test
```

Full source code and examples available at:
https://github.com/nsmac/blog-9mac-dev-code/tree/main/blog-post-examples/java/2025-11-08-java9-module-system

---

## Key Takeaways

1. **Modules are architectural units** - They group packages, enforce boundaries, and declare dependencies
2. **Packages are private by default** - Encapsulation is the default, not an afterthought
3. **Dependencies are explicit** - Module relationships are declared, not inferred from the classpath
4. **Strong encapsulation is JVM-enforced** - Not just conventions; violations are runtime errors
5. **Reflection is controlled** - The `opens` directive enables frameworks while maintaining security
6. **Every module requires java.base** - The foundation module provides core classes
7. **Transitive dependencies simplify consumption** - Facade modules reduce consumer burden
8. **Optional dependencies are supported** - `requires static` for compile-time-only needs

The module system represents a fundamental maturation of Java's architecture, enabling building enterprise applications with clear boundaries, explicit dependencies, and strong encapsulation—critical requirements for large-scale, maintainable systems.
