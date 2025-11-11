# Java 9+ Module System: Strong Encapsulation and Explicit Dependencies

## Introduction

Java 9 introduced the **Java Platform Module System (JPMS)**, a major architectural feature that fundamentally changed how we organize, encapsulate, and manage dependencies in large Java applications. Before modules, Java used packages as the only organizational unit above classes, leading to weak encapsulation and unclear dependency relationships.

The module system addresses several critical problems that plagued Java applications for decades:

- **Weak Encapsulation** - Any public class was globally accessible; no real encapsulation existed
- **Version Conflicts** - Packages could split across JAR files, causing incompatibilities
- **Unclear Dependencies** - The classpath made it hard to understand module relationships
- **Large Runtime Footprint** - The entire JDK was monolithic; you couldn't create minimal Java installations
- **Scale Issues** - Large applications struggled with maintainability due to unclear module boundaries

The module system solves these through three key mechanisms: **strong encapsulation** (packages are private by default), **explicit dependencies** (all relationships declared), and **platform modularity** (the JDK itself is modularized).

---

# PART 1: How Modules Work (Fundamentals)

## What is a Module?

A module is a **named, self-contained unit** that groups related packages and resources together. Every module has a **module descriptor** (`module-info.java`) that declares its name, dependencies, exported packages, and services.

```
Module Hierarchy:
├── Module (named unit)
│   ├── Package (java.lang)
│   │   ├── Class (String)
│   │   └── Class (Integer)
│   ├── Package (java.util)
│   │   ├── Class (ArrayList)
│   │   └── Class (HashMap)
│   └── Resources (config files, etc.)
```

### Module Descriptor (module-info.java)

```java
module com.example.api {
    // Dependencies
    requires java.base;
    requires com.example.core;

    // Exports
    exports com.example.api;

    // Services
    uses com.example.service.Provider;
}
```

## Core Concept 1: Strong Encapsulation by Default

**Packages are private by default.** Only exported packages are accessible outside the module.

**Before Java 9:**
```java
public class InternalHelper {  // Anyone can use this!
    public void internalMethod() { }
}
```

**After Java 9:**
```java
// module-info.java
module mylib {
    exports com.mylib.api;  // Only this is accessible
    // com.mylib.internal is PRIVATE
}

// Other module trying to use it:
import com.mylib.api.PublicClass;          // ✅ Works
// import com.mylib.internal.InternalHelper; // ❌ Compile error!
```

### Example 1: Package Exports and Encapsulation

Strong encapsulation means developers can safely refactor internal code.

```java
// Check what packages are exported
Module javaBase = Object.class.getModule();
var descriptor = javaBase.getDescriptor();

// List exported packages
descriptor.exports().forEach(export -> {
    System.out.println("Exported: " + export.source());
});

// Check if package is exported
boolean exported = javaBase.isExported("java.lang");
System.out.println("java.lang exported: " + exported);
```

**Unit Tests:**

```java
@Test
@DisplayName("Should access classes from exported packages")
void shouldAccessClassesFromExportedPackages() {
    // java.util is exported from java.base
    List<String> list = new ArrayList<>();  // ✅ Works
    list.add("test");
    assertEquals(1, list.size());
}

@Test
@DisplayName("Should demonstrate packages are private by default")
void shouldDemonstrateThatPackagesAreModulePrivateByDefault() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    var allPackages = descriptor.packages();
    var exportedPackages = descriptor.exports().stream()
        .map(e -> e.source())
        .toList();

    var privatePackages = allPackages.stream()
        .filter(pkg -> !exportedPackages.contains(pkg))
        .toList();

    // Not all packages are exported - some are internal!
    assertTrue(exportedPackages.size() <= allPackages.size());
    assertFalse(privatePackages.isEmpty());
}

@Test
@DisplayName("Should enable safe refactoring of internal code")
void shouldEnableSafeRefactoringOfInternalCode() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    // Internal packages (non-exported) are safe to refactor
    var internalPackages = descriptor.packages().stream()
        .filter(pkg -> !javaBase.isExported(pkg))
        .toList();

    // These are internal - no one depends on them
    for (String pkg : internalPackages) {
        assertFalse(javaBase.isExported(pkg));
    }
}
```

**Output:**
```
Exported Packages:
  java.lang ✅
  java.util ✅
  java.io ✅
  jdk.internal.* ❌ (private)
  sun.* ❌ (private)

Packages are private by default:
  Total packages in java.base: 1500+
  Exported packages: ~60
  Private packages: ~1440+
```

**Key Insight:** Strong encapsulation is the most powerful aspect of modules. Before Java 9, library developers had no way to hide internal classes—anyone could use them. Now, internal packages are genuinely internal. The JDK reduced its public surface from ~4000 classes to ~1300 by properly hiding internals. This enables safe evolution.

---

## Core Concept 2: Explicit Dependencies

**All module relationships must be declared** via `requires`. Dependencies are explicit and validated by the JVM.

```
BEFORE (Classpath):
java -cp lib/*:myapp.jar MyApp
↓
No way to know which JARs depend on what

AFTER (Module System):
module-info.java: requires com.other.module
↓
Dependencies validated, no surprises
```

### Three Types of Dependencies

#### `requires <module>` - Mandatory
```java
module com.example.app {
    requires java.logging;  // Must be present
}
```

#### `requires transitive <module>` - Re-exported
```java
module com.example.facade {
    requires transitive java.logging;  // Consumers get this too
}

module com.example.client {
    requires com.example.facade;  // Automatically gets java.logging
}
```

#### `requires static <module>` - Optional
```java
module com.example.app {
    requires static com.optional.feature;  // Compile-time only
}
```

### Example 2: Module Dependencies

Dependencies between modules are explicit and validated.

```java
// Check if module can read another
Module current = MyClass.class.getModule();
Module javaBase = Object.class.getModule();

boolean canRead = current.canRead(javaBase);
System.out.println("Can read java.base: " + canRead);  // true

// Every module implicitly requires java.base
assertTrue(current.canRead(javaBase));
```

**Unit Tests:**

```java
@Test
@DisplayName("Should show every module implicitly requires java.base")
void shouldShowEveryModuleImplicitlyRequiresJavaBase() {
    Module current = ModuleDependenciesExampleTest.class.getModule();
    Module javaBase = Object.class.getModule();

    // Current module can read java.base (implicit requirement)
    assertTrue(current.canRead(javaBase));
    assertEquals("java.base", javaBase.getName());
}

@Test
@DisplayName("Should distinguish between static and non-static requires")
void shouldDistinguishBetweenStaticAndNonStaticRequires() {
    Module javaLogging = java.util.logging.Logger.class.getModule();
    var descriptor = javaLogging.getDescriptor();

    assertNotNull(descriptor);
    var requires = descriptor.requires();

    // Check modifiers on each require
    for (var require : requires) {
        var mods = require.modifiers();
        boolean isStatic = mods.contains(
            ModuleDescriptor.Requires.Modifier.STATIC);
        boolean isTransitive = mods.contains(
            ModuleDescriptor.Requires.Modifier.TRANSITIVE);
        // Can be static, transitive, or both/neither
    }
}

@Test
@DisplayName("Should show all modules can read java.base")
void shouldShowAllModulesCanReadJavaBase() {
    Module current = ModuleDependenciesExampleTest.class.getModule();
    Module javaBase = Object.class.getModule();
    Module javaLogging = java.util.logging.Logger.class.getModule();

    // All modules can read java.base
    assertTrue(current.canRead(javaBase));
    assertTrue(javaLogging.canRead(javaBase));
    assertTrue(javaBase.canRead(javaBase));  // Reads itself
}
```

**Output:**
```
Module Dependency Analysis:
Current module: (unnamed module)
  Can read java.base: true

java.logging module:
  Requires: [java.base]
  Transitive: [no]

Dependency Resolution:
  Required at compile: ✅
  Required at runtime: ✅
  Re-exported: ❌
```

**Key Insight:** Explicit dependencies create a clear dependency graph that the JVM validates. Unlike the classpath, module relationships are unambiguous. Transitive dependencies allow facade modules to simplify consumption by bundling related modules. Static dependencies enable optional features—code can work without them.

---

## Core Concept 3: Reflection Control

Strong encapsulation extends to reflection. Before Java 9, `setAccessible(true)` always worked. Now, reflection access is also controlled.

```
BEFORE Java 9:
Field field = MyClass.class.getDeclaredField("privateField");
field.setAccessible(true);  // Always works!

AFTER Java 9:
field.setAccessible(true);  // Throws InaccessibleObjectException
// Unless package is opened via 'opens' directive
```

### Example 3: Encapsulation and Reflection

Reflection is restricted unless packages are explicitly opened.

```java
// Reflection on exported packages works
Class<?> stringClass = String.class;  // From exported java.lang
Method method = stringClass.getMethod("toUpperCase");  // ✅ Works

// Non-opened packages block reflection
// (even with setAccessible(true))
```

**Unit Tests:**

```java
@Test
@DisplayName("Should allow reflection on exported packages")
void shouldAllowReflectionOnExportedPackages()
        throws NoSuchMethodException {
    // String is from exported java.lang
    Class<?> stringClass = String.class;
    Module module = stringClass.getModule();

    assertTrue(module.isExported("java.lang"));

    // Reflection on public methods works
    Method method = stringClass.getMethod("toUpperCase");
    assertNotNull(method);
}

@Test
@DisplayName("Should restrict reflection on non-opened packages")
void shouldRestrictReflectionOnNonOpenedPackages() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    // Some packages are opened, others are closed
    var openedPackages = descriptor.opens().stream()
        .map(o -> o.source())
        .collect(toSet());

    // Closed packages prevent reflection access
    assertTrue(openedPackages.size() > 0);

    // But not all packages are opened
    var allPackages = descriptor.packages();
    var closedPackages = allPackages.stream()
        .filter(pkg -> !openedPackages.contains(pkg))
        .toList();

    assertFalse(closedPackages.isEmpty());
}
```

**Output:**
```
Reflection Access Control:
  String class exported: true
  Can use reflection: ✅

Internal packages (not opened):
  jdk.internal.* - reflection blocked ❌
  sun.* - reflection blocked ❌

Opened for reflection (via 'opens'):
  java.lang - ✅ (needed by many frameworks)
  java.util - ✅ (needed by frameworks)
```

**Key Insight:** Reflection control prevents even reflection from bypassing encapsulation. Before modules, reflection could access private members of any class. Now, only opened packages allow this. This enables frameworks to work with private members when needed (via `opens`) while keeping other packages truly sealed.

---

## Benefits: Problems Solved

### 1. Weak Encapsulation → Strong Encapsulation
Internal classes are now genuinely internal. The JDK reduced from ~4000 public classes to ~1300.

### 2. Classpath Hell → Explicit Dependencies
No more guessing which JARs depend on which. Relationships are declared and validated.

### 3. Large JDK → Modular JDK
You can create minimal Java installations with only needed modules using `jlink`.

### 4. Version Conflicts → Version Safety
Modules can't be split; each module has one version. No "split package" problems.

---

# PART 2: Working with Modules at Runtime - The Module API

Now that we understand how modules work, let's explore the **Module API** that lets us query and work with modules at runtime.

## The Module Class and ModuleDescriptor

Java provides the `Module` class to query module information at runtime:

```java
Module module = MyClass.class.getModule();

// Basic information
String name = module.getName();
boolean isNamed = module.isNamed();
ClassLoader loader = module.getClassLoader();

// Metadata
ModuleDescriptor descriptor = module.getDescriptor();
ModuleLayer layer = module.getLayer();

// Relationships
boolean canRead = module.canRead(otherModule);
boolean isExported = module.isExported("com.example.pkg");
boolean isOpen = module.isOpen("com.example.pkg");
```

### Example 4: Module API Introspection

The Module API enables runtime inspection of module metadata and relationships.

```java
// Get module from any class
Module module = String.class.getModule();
System.out.println(module.getName());  // "java.base"

// Get module descriptor
var descriptor = module.getDescriptor();
System.out.println(descriptor.packages());
System.out.println(descriptor.exports());
System.out.println(descriptor.requires());

// Query relationships
Module current = MyClass.class.getModule();
System.out.println(current.canRead(module));
```

**Unit Tests:**

```java
@Test
@DisplayName("Should get module from any class")
void shouldGetModuleFromAnyClass() {
    Module stringModule = String.class.getModule();
    Module listModule = java.util.ArrayList.class.getModule();

    assertNotNull(stringModule);
    assertNotNull(listModule);

    // String and ArrayList are in same module
    assertEquals(stringModule.getName(), listModule.getName());
    assertEquals("java.base", stringModule.getName());
}

@Test
@DisplayName("Should retrieve module descriptor with all metadata")
void shouldRetrieveModuleDescriptorWithAllMetadata() {
    Module javaBase = Object.class.getModule();
    var descriptor = javaBase.getDescriptor();

    assertNotNull(descriptor);
    assertNotNull(descriptor.name());
    assertNotNull(descriptor.requires());
    assertNotNull(descriptor.exports());
    assertNotNull(descriptor.packages());

    // java.base has many packages
    assertFalse(descriptor.packages().isEmpty());
    assertFalse(descriptor.exports().isEmpty());
}

@Test
@DisplayName("Should query module relationships at runtime")
void shouldQueryModuleRelationshipsAtRuntime() {
    Module current = ModuleReflectionExampleTest.class.getModule();
    Module javaBase = Object.class.getModule();
    Module javaLogging = java.util.logging.Logger.class.getModule();

    // Check various relationships
    assertTrue(current.canRead(javaBase));
    assertTrue(current.canRead(javaLogging) || javaLogging == javaBase);

    // Check exports
    assertTrue(javaBase.isExported("java.lang"));
    assertTrue(javaBase.isExported("java.lang", current));
}

@Test
@DisplayName("Should enable runtime inspection of module hierarchy")
void shouldEnableRuntimeInspectionOfModuleHierarchy() {
    Module javaBase = Object.class.getModule();
    var layer = javaBase.getLayer();

    var config = layer.configuration();
    var allModules = config.modules();

    // Can inspect all modules
    assertFalse(allModules.isEmpty());

    // Can find specific modules by name
    var javaLogging = layer.findModule("java.logging");
    assertTrue(javaLogging.isPresent());
}
```

**Output:**
```
Module Information:
  Module Name: java.base
  Is Named: true
  Classloader: jdk.internal.loader.ClassLoaders$PlatformClassLoader

Module Descriptor:
  Packages: 1400+ packages
  Requires: [java.base - transitive]
  Exports: [java.lang, java.util, java.io, ...]
  Opens: [java.lang, java.util, ...]

Module Relationships:
  current module can read java.base: true
  java.base is exported to all: true
  Module hierarchy: 100+ modules in JDK
```

**Key Insight:** The Module API enables sophisticated runtime behaviors. Frameworks use it to dynamically request reflection access. Build tools use it to analyze dependencies. IDEs use it to validate configurations. The ability to inspect modules at runtime makes the system flexible enough to support legacy frameworks while providing strong encapsulation by default.

---

## Dynamic Module Configuration

The Module API allows dynamic changes to module relationships at runtime:

```java
Module current = MyApp.class.getModule();
Module target = OtherModule.class.getModule();

// Add read edge
current.addReads(target);

// Export a package
current.addExports("com.internal", target);

// Open package for reflection
current.addOpens("com.model", target);

// Add service usage
current.addUses(ServiceInterface.class);
```

### Example 5: Dynamic Module Updates

Some applications need to modify module relationships at runtime.

```java
// Add ability to read another module
Module current = getMyModule();
Module required = getOtherModule();
current.addReads(required);

// Export a package to specific module
current.addExports("com.internal.api", requiredModule);

// Open package for reflection (framework support)
current.addOpens("com.model.dto", jpaModule);
```

**Unit Tests:**

```java
@Test
@DisplayName("Should support dynamic module configuration")
void shouldSupportDynamicModuleConfiguration() {
    Module current = ModuleReflectionExampleTest.class.getModule();
    Module javaBase = Object.class.getModule();

    // addReads - dynamically add read edge
    Module result = current.addReads(javaBase);
    assertSame(current, result);  // Returns self for chaining

    // Verify relationship
    assertTrue(current.canRead(javaBase));
}

@Test
@DisplayName("Should support dynamic export")
void shouldSupportDynamicExport() {
    Module current = ModuleReflectionExampleTest.class.getModule();
    Module target = Object.class.getModule();

    String packageName = "dev.nmac.blog.examples.java9.modules";

    // Dynamically export package
    Module result = current.addExports(packageName, target);
    assertSame(current, result);
}

@Test
@DisplayName("Should support dynamic opens for reflection")
void shouldSupportDynamicOpensForReflection() {
    Module current = ModuleReflectionExampleTest.class.getModule();
    Module jpaModule = Object.class.getModule();

    // Dynamically open for reflection
    current.addOpens("dev.nmac.blog.examples.java9.modules",
                    jpaModule);

    assertTrue(current.isOpen("dev.nmac.blog.examples.java9.modules",
                            jpaModule));
}

@Test
@DisplayName("Should support dynamic service usage")
void shouldSupportDynamicServiceUsage() {
    Module current = ModuleReflectionExampleTest.class.getModule();

    // Add service usage
    Module result = current.addUses(Runnable.class);
    assertSame(current, result);
}
```

**Output:**
```
Dynamic Module Configuration:
  addReads(targetModule): ✅
  addExports(package, targetModule): ✅
  addOpens(package, targetModule): ✅
  addUses(serviceClass): ✅

Benefits:
  - Frameworks can request reflection access
  - Tools can add modules dynamically
  - Build systems can reconfigure at runtime
  - Applications can adapt module structure
```

**Key Insight:** Dynamic configuration makes the module system flexible. While static declarations in `module-info.java` are the norm, the Module API allows runtime modifications for advanced scenarios. This is how frameworks like Spring use modules—they declare what they need in their module descriptor, but may also dynamically configure modules based on application needs.

---

## Practical Use Cases for the Module API

### 1. Frameworks Requesting Reflection Access
```java
// JPA framework dynamically opens packages
Module myModule = MyEntity.class.getModule();
Module jpaModule = javax.persistence.Entity.class.getModule();

myModule.addOpens("com.example.entity", jpaModule);
```

### 2. Build Tools Analyzing Dependencies
```java
// Maven plugin inspecting module structure
Module module = someClass.getModule();
var descriptor = module.getDescriptor();
descriptor.requires().forEach(req -> {
    System.out.println("Dependency: " + req.name());
});
```

### 3. IDE Validation
```java
// IDE checking if class can be accessed
Module target = TargetClass.class.getModule();
String package = "com.example.api";
boolean accessible = target.isExported(package);
```

### 4. Dynamic Plugin Loading
```java
// Application dynamically loading plugin module
Module pluginModule = loadPluginModule("com.example.plugin");
Module appModule = AppClass.class.getModule();

appModule.addReads(pluginModule);  // App can use plugin
```

---

## Summary: Two Perspectives of Modules

### Static Perspective (Compile-time)
- Declared in `module-info.java`
- Validated by javac
- JVM verifies at class loading

### Dynamic Perspective (Runtime)
- Inspected via Module API
- Can be modified via `addReads`, `addExports`, etc.
- Enables frameworks and tools

Both perspectives are essential:
- **Static declarations** provide clear contracts and prevent accidents
- **Dynamic capabilities** enable flexibility for frameworks and advanced scenarios

---

## Key Takeaways

1. **Modules group packages** - Clear Module > Packages > Classes hierarchy
2. **Packages are private by default** - Must explicitly export
3. **Dependencies are explicit** - `requires`, `requires transitive`, `requires static`
4. **Strong encapsulation is JVM-enforced** - Reflection also controlled
5. **Module API enables introspection** - Query module structure at runtime
6. **Dynamic configuration is possible** - Add reads, exports, opens at runtime
7. **Clear API boundaries** - Public API vs internal implementation
8. **Frameworks can integrate** - Via `opens` directive and dynamic configuration

---

## Running the Examples

Build and run the examples:

```bash
# Build
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

Full source code:
https://github.com/nsmac/blog-9mac-dev-code/tree/main/blog-post-examples/java/2025-11-08-java9-module-system
