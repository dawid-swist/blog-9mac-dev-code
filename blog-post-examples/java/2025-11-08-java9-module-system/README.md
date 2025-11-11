# Java 9+ Module System: A Comprehensive Guide

A comprehensive exploration of Java's Platform Module System (JPMS) introduced in Java 9, with practical examples demonstrating modularity, encapsulation, and dependency management.

## Project Overview

This project demonstrates the Java 9 Module System through five progressive examples, each focusing on a specific aspect of modularity:

- **Basic Module System** - Understanding modules, module-info.java, and module descriptors
- **Module Dependencies** - How modules declare and manage dependencies
- **Package Exports** - Strong encapsulation and visibility control
- **Module Reflection API** - Runtime introspection and configuration
- **Strong Encapsulation** - Security benefits and practical implications

## What Problem Does the Module System Solve?

Before Java 9, the only organizational unit above classes was packages. This led to several problems:

1. **Weak Encapsulation** - Any public class was globally accessible through reflection
2. **Version Conflicts** - Packages could be split across JAR files (fragile base class problem)
3. **Unclear Dependencies** - Classpath made it hard to understand module relationships
4. **Large Runtime** - No way to create minimal JDK installations
5. **Scale Issues** - Large applications difficult to maintain and evolve

## Key Concepts

### Module Hierarchy
```
Module > Packages > Classes
```

### Module Descriptor (module-info.java)
Located at the module root, defines:
- Module name
- Required dependencies (`requires`)
- Exported packages (`exports`)
- Services offered/used (`provides`, `uses`)
- Packages open for reflection (`opens`)

### Three Execution Phases
1. **Compile-time** - `javac` with module-source-path
2. **Link-time** - `jlink` for creating custom JDK images
3. **Run-time** - `java` with module-path

### Module Types
- **Named modules** - Have module-info.java (explicit declaration)
- **Automatic modules** - JAR files added to module-path (inferred names)
- **Unnamed module** - Legacy code on classpath (backward compatibility)
- **System modules** - JDK modules (java.base, java.logging, etc.)

## Project Structure

```
src/
├── main/java/dev/nmac/blog/examples/java9/modules/
│   ├── HelloWorld.java
│   ├── BasicModuleExample.java
│   ├── ModuleDependenciesExample.java
│   ├── ModuleExportsExample.java
│   ├── ModuleReflectionExample.java
│   └── EncapsulationExample.java
└── test/java/dev/nmac/blog/examples/java9/modules/
    ├── BasicModuleExampleTest.java
    ├── ModuleDependenciesExampleTest.java
    ├── ModuleExportsExampleTest.java
    ├── ModuleReflectionExampleTest.java
    └── EncapsulationExampleTest.java
```

## Building the Project

### Build entire project from repository root:
```bash
./gradlew build
```

### Build only this project:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:build
```

### Clean build:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:clean build
```

## Running Examples

### List all available tasks:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:tasks --all
```

### Run individual examples:

#### 1. Basic Module System Concepts
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runBasicModule
```
Demonstrates:
- What a module is
- Module-info.java file structure
- Module descriptors
- Default module-private behavior
- Implicit java.base dependency

#### 2. Module Dependencies
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleDependencies
```
Demonstrates:
- `requires` directive
- `requires transitive` for re-exporting
- `requires static` for optional dependencies
- Runtime module relationship queries
- Module layer configuration

#### 3. Package Exports
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleExports
```
Demonstrates:
- `exports` directive (unconditional)
- `exports ... to` (selective/qualified)
- Strong encapsulation benefits
- Private packages vs exported packages
- API boundaries

#### 4. Module API and Reflection
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleReflection
```
Demonstrates:
- Module class API
- Runtime introspection
- Module relationships (canRead, isExported, isOpen)
- Dynamic module configuration (addReads, addExports, addOpens)
- Module layers and configuration

#### 5. Strong Encapsulation
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runEncapsulation
```
Demonstrates:
- Reflection access control
- `opens` directive for framework access
- InaccessibleObjectException prevention
- Comparing pre/post Java 9 encapsulation
- Safe refactoring of internal code

### Run all examples:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runAllExamples
```

## Running Tests

### Run all tests for this project:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:test
```

### Run tests with detailed output:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:test --info
```

### Run specific test class:
```bash
./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:test --tests "*BasicModuleExampleTest*"
```

## Module System Directives Reference

### Dependency Directives

| Directive | Purpose | Example |
|-----------|---------|---------|
| `requires` | Mandatory dependency | `requires java.base;` |
| `requires transitive` | Re-export dependency | `requires transitive java.logging;` |
| `requires static` | Optional (compile-time only) | `requires static com.optional;` |

### Visibility Directives

| Directive | Purpose | Example |
|-----------|---------|---------|
| `exports` | Unconditional package export | `exports com.example.api;` |
| `exports ... to` | Qualified export | `exports com.internal to com.trusted;` |
| `opens` | Unconditional reflection access | `opens com.reflect;` |
| `opens ... to` | Qualified reflection access | `opens com.internal to java.base;` |

### Service Directives

| Directive | Purpose | Example |
|-----------|---------|---------|
| `uses` | Service consumption | `uses java.nio.file.spi.FileSystemProvider;` |
| `provides ... with` | Service implementation | `provides com.api.Service with com.impl.ServiceImpl;` |

## Key Module System Directives Explained

### requires
```java
module my.module {
    requires java.base;         // Mandatory dependency
}
```
- Compile-time AND runtime dependency
- Default: not transitive
- Every module implicitly requires java.base

### requires transitive
```java
module my.api {
    requires transitive java.logging;  // Re-exports dependency
}
```
- Consumers of my.api automatically get access to java.logging
- Used for API modules/facades
- Reduces consumer burden

### requires static
```java
module my.module {
    requires static com.optional;      // Optional feature
}
```
- Compile-time only dependency
- Not required at runtime
- Enables optional feature support

### exports
```java
module my.module {
    exports com.example.api;           // Public API
}
```
- Package accessible from all modules
- Default: packages are module-private
- Classes must still be public

### exports ... to
```java
module my.module {
    exports com.example.internal to com.trusted;  // Selective export
}
```
- Package accessible only to named modules
- Maintains encapsulation
- Useful for shared internal APIs

### opens (for frameworks)
```java
module my.module {
    opens com.example.model;           // Reflection access
}
```
- Enables reflection on private members
- Runtime-only (affects reflection, not compilation)
- Used by JPA, Jackson, Mockito, etc.

## Module System Benefits

### 1. Strong Encapsulation
- Packages private by default
- Only exported packages accessible
- Prevents accidental API leakage

### 2. Explicit Dependencies
- Dependencies declared in module-info.java
- Clear module graph
- Prevents circular dependencies

### 3. Platform Modularity
- JDK modularized with ~100 modules
- Can create minimal Java installations with jlink
- Reduced runtime footprint

### 4. Reliable Configuration
- Transitive dependencies included
- Prevents version conflicts
- No split packages across modules

### 5. Security
- Reflection access controlled
- Encapsulation enforced by JVM
- Stronger security boundaries

## Example Patterns

### Facade Module Pattern
```java
module my.facade {
    requires transitive java.base;
    requires transitive com.library1;
    requires transitive com.library2;

    exports com.facade.api;
}
```
Simplifies consumer dependencies.

### Internal API Pattern
```java
module my.module {
    exports com.my.public.api;
    exports com.my.internal to com.trusted.partner;
}
```
Share internal APIs safely with partners.

### Framework Module Pattern
```java
module my.module {
    exports com.my.model;
    opens com.my.model to java.persistence;
}
```
Allow frameworks to use reflection on specific packages.

## Project Configuration

- **Java Version:** 21 (compatible with Java 9+)
- **Build Tool:** Gradle 9.0.0
- **Test Framework:** JUnit 5 (Jupiter)
- **Group ID:** dev.nmac.blog.examples
- **Version:** 1.0.0

## Learning Path

1. **Start with BasicModuleExample** - Understand module fundamentals
2. **Study ModuleDependenciesExample** - Learn dependency management
3. **Explore ModuleExportsExample** - Understand encapsulation
4. **Review ModuleReflectionExample** - Master the Module API
5. **Examine EncapsulationExample** - See security benefits
6. **Run all tests** - Verify understanding

## Reference Materials

For comprehensive understanding, consult:
- **JEP 261** - Module System (openjdk.org/jeps/261)
- **Java 21 Module API** - docs.oracle.com/javase/21/docs/api/java.base/java/lang/Module.html
- **Java Platform SE Specification** - Module System chapter

## Key Insights

1. **Packages are module-private by default** - Must explicitly export
2. **Every module implicitly requires java.base** - No need to declare
3. **public ≠ accessible** - Must be public AND exported
4. **Reflection requires opens** - Cannot setAccessible(true) without permission
5. **Module layer enables dynamic configuration** - Frameworks can add edges at runtime
6. **JDK modularization enables jlink** - Create minimal runtime images
7. **Strong encapsulation is JVM-enforced** - Not just conventions
8. **Backward compatibility maintained** - Classpath still works (unnamed module)

## Real-World Use Cases

### 1. Microservices Architecture
- Each service as separate module
- Clear service boundaries
- Explicit dependencies

### 2. Plugin Systems
- Base framework module
- Plugin modules with ServiceLoader
- Dynamic module loading

### 3. Large Enterprises
- Multiple teams owning modules
- Reduced coupling
- Safe refactoring
- Clear API contracts

### 4. Library Development
- Clear public API (exports)
- Hidden implementation details
- Stable versioning
- SemVer-friendly design

### 5. JDK-like Distributions
- Create custom Java images with jlink
- Include only needed modules
- Reduced container/deployment size

## Code Quality

All examples follow:
- Java coding conventions
- Gradle best practices
- Clear naming and documentation
- Comprehensive unit test coverage (BDD style with @DisplayName)
- Practical, runnable code

## Enjoy exploring the Java Module System!

This project provides hands-on experience with one of Java's most important architectural features. The Module System enables building large-scale, maintainable applications with clear boundaries and strong encapsulation.

Run the examples, study the tests, and experiment with the Module API to master this essential Java skill!
