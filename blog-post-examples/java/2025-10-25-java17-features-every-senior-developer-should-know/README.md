# Java 17 Features Every Senior Developer Should Know

A comprehensive series exploring essential Java 17+ features with practical examples and detailed explanations.

## Project Overview

This project demonstrates modern Java features that every senior developer should understand, including:
- **Local Variable Type Inference** (var keyword)
- **Records** - Modern data carriers
- **Sealed Classes** - Controlled inheritance hierarchies
- **Pattern Matching** and enhanced switch expressions
- **Text Blocks** - Multi-line string literals

## Project Structure

```
src/
├── main/java/dev/nmac/blog/examples/java17/
│   ├── HelloWorld.java              # Main entry point
│   ├── part1/                       # var keyword examples
│   │   ├── VarCollectionsExample.java
│   │   ├── VarAnonymousClassExample.java
│   │   ├── VarIntersectionTypesExample.java
│   │   ├── VarLimitationsExample.java
│   │   └── exercises/
│   │       └── DataProcessorSolution.java
│   ├── part2/                       # records examples
│   │   ├── BasicRecordExample.java
│   │   ├── GenericRecordExample.java
│   │   ├── RecordMethodsExample.java
│   │   ├── RecordInterfaceExample.java
│   │   ├── NestedRecordsExample.java
│   │   └── RecordPerformanceExample.java
│   ├── part3/                       # sealed classes
│   ├── part4/                       # pattern matching
│   └── part5/                       # text blocks
└── test/java/                       # Unit tests for all examples
```

## Building the Project

### Build entire project from repository root:
```bash
./gradlew build
```

### Build only this project:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:build
```

### Clean build:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:clean build
```

## Running Examples

### List all available tasks:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:tasks --all
```

### Part 1: Local Variable Type Inference (var keyword)

Run individual examples:
```bash
# Collections with var
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runVarCollections

# var with anonymous classes
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runVarAnonymousClass

# var with intersection types
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runVarIntersectionTypes

# var limitations and pitfalls
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runVarLimitations

# Exercise solution
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runDataProcessorSolution
```

Run all Part 1 examples:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runAllPart1Examples
```

### Part 2: Records

Run individual examples:
```bash
# Basic record usage
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runBasicRecordExample

# Generic records
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runGenericRecordExample

# Records with custom methods
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runRecordMethodsExample

# Records implementing interfaces
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runRecordInterfaceExample

# Nested records
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runNestedRecordsExample

# Performance comparison
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runRecordPerformanceExample
```

Run all Part 2 examples:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runAllPart2Examples
```

## Running Tests

### Run all tests for this project:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:test
```

### Run tests with detailed output:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:test --info
```

### Run specific test class:
```bash
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:test --tests "*VarCollectionsExample*"
```

## Project Configuration

- **Java Version:** 21 (compatible with Java 17+)
- **Build Tool:** Gradle 9.0.0
- **Test Framework:** JUnit 5 (Jupiter)
- **Group ID:** dev.nmac.blog.examples
- **Version:** 1.0.0

## Series Contents

### Part 1: Introduction and Local Variable Type Inference
Covers the `var` keyword, its benefits, and limitations in modern Java.

**Topics:**
- Type inference with collections
- Anonymous classes and var
- Intersection types
- Common pitfalls and best practices

### Part 2: Records - Modern Data Carriers
Explores records as a modern replacement for traditional data classes.

**Topics:**
- Basic record declaration and usage
- Generic records with type parameters
- Custom methods and constructors
- Implementing interfaces with records
- Nested records
- Performance advantages over traditional classes

### Part 3: Sealed Classes
Understanding controlled inheritance and sealed class hierarchies.

### Part 4: Pattern Matching and Switch Expressions
Modern pattern matching capabilities and enhanced switch.

### Part 5: Text Blocks
Multi-line string literals for cleaner code.

### Part 6: Syntax Cheat Sheet
Quick reference guide for all covered features.

## Learning Approach

Each example includes:
- **Well-commented source code** explaining concepts step-by-step
- **Unit tests** with `@DisplayName` annotations demonstrating real-world usage
- **Program output** showing execution results
- **Key insights** explaining the benefits and trade-offs

## Blog Posts

For comprehensive explanations and deeper insights, visit:
- **blog.9mac.dev** - Full article series with detailed walkthroughs

## Requirements

- Java 17 or higher (tested with Java 21)
- Gradle 9.0.0 (included via wrapper)

## Quick Start

```bash
# Clone and navigate to repository
cd blog-9mac-dev-code

# Run all Part 1 examples
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runAllPart1Examples

# Run all Part 2 examples
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:runAllPart2Examples

# Run all tests
./gradlew :blog-post-examples:java:2025-10-25-java17-features-every-senior-developer-should-know:test
```

## Code Quality

All examples follow:
- Java coding conventions
- Gradle best practices
- Clear naming and documentation
- Comprehensive unit test coverage

Enjoy exploring modern Java features!