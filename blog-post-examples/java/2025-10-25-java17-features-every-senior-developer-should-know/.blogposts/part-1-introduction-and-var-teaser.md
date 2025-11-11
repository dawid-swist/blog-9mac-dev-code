# Java 17 Features Every Senior Developer Should Know - Part 1: var Keyword

This is **Part 1** of a comprehensive series on essential Java 17+ features that every senior developer must understand when working with modern Java. Whether you're migrating from Java 8, 11, or 13, or you're mentoring junior developers, this series covers the language features that will define your production code quality and team productivity for the next decade.

**This series covers:**
- **Part 1**: Local variable type inference with `var` keyword
- **Part 2**: Immutable data carriers with Records
- **Part 3**: Controlled inheritance hierarchies with Sealed Classes
- **Part 4**: Pattern matching and modern switch expressions
- **Part 5**: Multi-line string literals with Text Blocks
- **Part 6**: Quick reference syntax cheat sheet

Each part is designed to be read independently, but together they provide a complete picture of modern Java development.

---

## Why This Matters

Java has been criticized for verbosity since its inception. When modern languages like C# (2007) and Kotlin got type inference, Java developers watching from the sidelines had to write types explicitly everywhere. By 2018, with the Stream API introducing increasingly complex generic types, the pain became unbearable:

```java
Map<String, List<Transaction>> transactions = new HashMap<String, List<Transaction>>();
```

That's not documentation—it's noise. Your IDE has to render the entire type signature twice on the same line. It makes refactoring painful: change `List` to `Set`? Update two places.

The `var` keyword (Java 10) was Java's answer to 20 years of criticism: **"Let the compiler figure out the type, and let developers focus on logic, not ceremony."**

---

## What is var?

The `var` keyword lets the compiler automatically infer the type of a local variable based on its initialization. Introduced in Java 10, it eliminates repetitive type declarations.

**Important:** `var` is NOT dynamic typing. The type is determined at compile-time and never changes—it's static typing with inference.

## The Problem It Solves

Before Java 10, you had to write the type twice:

```java
// Redundant type declaration
Map<String, List<Employee>> employees = new HashMap<String, List<Employee>>();
```

The compiler already knows the type from the right side—why repeat it?

## How It Works

```java
var name = "John";                    // String - inferred from literal
var count = 42;                       // int - inferred from literal
var numbers = List.of(1, 2, 3);      // List<Integer> - inferred from method return
var map = new HashMap<String, Integer>(); // HashMap<String, Integer>
```

The compiler determines the type at compile-time. These variables are as type-safe as explicitly typed variables.

## When to Use var

### ✅ Great use cases:

```java
// Long generic types
var customers = repository.findActiveCustomers();

// Stream operations
var result = employees.stream()
    .filter(e -> e.salary() > 50000)
    .map(e -> e.name())
    .collect(Collectors.toList());

// Clear from context
var now = LocalDateTime.now();
var file = new File("data.txt");
```

### ❌ Avoid when:

```java
// Type is NOT obvious
var x = calculate();  // What does calculate() return?

// Numeric literals (ambiguous)
var value = 10;      // int or long? Not immediately clear

// Method references without context
var processor = data::process;  // Type of processor?
```

## Key Rules

1. **Only for local variables** - Not for fields, parameters, or return types
2. **Must be initialized** - `var x;` is invalid
3. **Cannot change type** - `var x = 42; x = "hello";` fails at compile-time
4. **Same package rule for sealed classes** - When using var with sealed types

## Simple Example

```java
public class VarExample {
    public static void main(String[] args) {
        // var infers type from the initializer
        var greeting = "Hello, World!";
        var numbers = List.of(1, 2, 3, 4, 5);
        var total = numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();

        System.out.println(greeting);
        System.out.println("Numbers: " + numbers);
        System.out.println("Total: " + total);
    }
}
```

**Output:**
```
Hello, World!
Numbers: [1, 2, 3, 4, 5]
Total: 15
```

## Key Insight

`var` reduces boilerplate without sacrificing type safety. The compiler still performs full type checking—nothing sneaks past. It's particularly valuable with the Stream API and complex generic types, where explicit types become noise rather than documentation.

---

## Want to Learn More?

This is just a preview of what `var` can do. **[Read the full Part 1 article](part-1-introduction-and-var.md)** where you'll discover:

- Type inference with anonymous classes and intersection types
- Advanced use cases with collections and functional interfaces
- Best practices from production codebases
- Common pitfalls and how to avoid them
- Comprehensive examples covering every scenario

The full article includes 15+ detailed examples, performance considerations, and migration strategies for upgrading from Java 8.

**All code examples are runnable in the GitHub repository:**
```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```
