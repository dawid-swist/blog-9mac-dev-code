# Java 17 Features Every Senior Developer Should Know - Part 6: Quick Reference

**Part 6 of 6** - Final installment of the Java 17+ features series.

**This is a teaser article.** Quick intro to the syntax cheat sheet. Read the full article for complete reference with syntax, migration checklist, and troubleshooting guide.

---

## Why This Reference Matters

Throughout this series, we've explored six major Java 17 features. But switching between multiple long articles to remember syntax is inefficient. Senior developers need a **single, printable reference** they can keep at their desk.

Part 6 provides a **complete syntax cheat sheet** for all 6 features:
- Quick syntax for all features
- When to use each feature
- Common pitfalls and gotchas
- Migration checklist
- Combined example showing features working together

---

## What's in the Full Reference

The complete Part 6 article includes quick-reference sections for:

1. **var keyword** - Type inference syntax and rules
2. **Records** - Declaration, compact constructors, validation
3. **Sealed Classes** - Sealed syntax, permits clause, three modifiers
4. **Pattern Matching** - instanceof patterns, type patterns, guards
5. **Switch Expressions** - Arrow syntax, yield, exhaustiveness
6. **Text Blocks** - Triple quotes, indentation, escape sequences

Plus:
- Real-world combined example showing all features together
- Step-by-step migration checklist for upgrading from Java 8
- Troubleshooting guide for common compiler errors
- Performance tips and best practices

---

## Key Insight

A good cheat sheet isn't a replacement for deep understanding—it's a tool for efficient recall. After reading the full series, you'll return to Part 6 frequently: during code review, when mentoring, during migrations, or when you need to remember exact syntax.

---

## Quick Syntax Reference

### var - Type Inference

```java
// Simple types
var name = "John";              // String
var count = 42;                 // int
var salary = 50000.50;          // double

// Complex types
var list = List.of(1, 2, 3);   // List<Integer>
var map = new HashMap<String, Integer>();

// In loops
var items = List.of("a", "b", "c");
for (var item : items) { ... }
```

### Records - Data Carriers

```java
// Basic record
public record Book(String title, String author, int year) {}

// With validation
public record Person(String name, int age) {
    public Person {
        if (age < 0) throw new IllegalArgumentException("Age negative");
    }
}

// Generic
public record Pair<T>(T first, T second) {}
```

### Sealed Classes - Controlled Inheritance

```java
// Sealed class
public sealed abstract class Transport
    permits Car, Bus, Truck {}

// Final implementation
public final class Car extends Transport {}

// Non-sealed (allows further extension)
public non-sealed class Bus extends Transport {}
```

### Pattern Matching - Type Checking

```java
// Type patterns
if (obj instanceof String s) {
    System.out.println(s.length());
}

// Pattern matching in switch
String result = switch (obj) {
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    default -> "Unknown";
};

// With guards
case String s when s.length() > 10 -> ...
```

### Switch Expressions - Modern Conditionals

```java
// Expression form (returns value)
var days = switch (month) {
    case "January", "March", "May" -> 31;
    case "February" -> 28;
    case "April", "June" -> 30;
    default -> throw new IllegalArgumentException();
};

// Multiple statements with yield
var category = switch (score) {
    case 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100 -> {
        System.out.println("Excellent!");
        yield "A";
    }
    case 80, 81, 82, 83, 84, 85, 86, 87, 88, 89 -> {
        System.out.println("Good!");
        yield "B";
    }
    default -> "F";
};
```

### Text Blocks - Multi-line Strings

```java
// Simple text block
String message = """
    Hello,
    This is a multi-line
    string literal
    """;

// With interpolation
String formatted = """
    Name: %s
    Age: %d
    """.formatted(name, age);

// JSON
String json = """
    {
        "name": "%s",
        "age": %d
    }
    """.formatted(name, age);
```

## Complete Example Using All Features

```java
// Part 1: var - Type inference
var users = List.of(
    new User("Alice", 30),
    new User("Bob", 25),
    new User("Charlie", 35)
);

// Part 2: Records - Immutable data
public record User(String name, int age) {}
public record Report(String title, List<User> data) {}

// Part 3: Sealed - Controlled types
public sealed interface DataSource
    permits JsonDataSource, DatabaseDataSource {}

// Part 4 & 5: Pattern matching + Text blocks
var dataSource = getDataSource(); // Returns sealed type
var report = switch (dataSource) {
    case JsonDataSource json -> new Report("JSON Report",
        parseJson(json.getJsonData()));
    case DatabaseDataSource db -> new Report("Database Report",
        queryDatabase(db));
};

// Part 5: Text blocks for output
String htmlOutput = """
    <html>
    <body>
        <h1>%s</h1>
        <ul>
        %s
        </ul>
    </body>
    </html>
    """.formatted(report.title(),
        report.data().stream()
            .map(u -> "<li>" + u.name() + "</li>")
            .collect(Collectors.joining("\n")));
```

## Migration Checklist

- [ ] Update Java to 17+ in `pom.xml` or `build.gradle`
- [ ] Replace `new HashMap<Type, Type>()` with `var`
- [ ] Convert POJOs to Records
- [ ] Replace `instanceof + cast` with pattern matching
- [ ] Convert `switch` statements to expressions
- [ ] Replace string concatenation with text blocks
- [ ] Update unit tests
- [ ] Verify sealed hierarchies
- [ ] Run full test suite
- [ ] Code review for best practices

## Read the Full Article

Get the complete reference in **[Part 6: Syntax Cheat Sheet on blog.9mac.dev]([BLOG_LINK_HERE])**:
- Full syntax examples for all 6 features
- "When to use" and "when to avoid" sections
- Common pitfalls and compiler error messages
- Real-world combined example
- Step-by-step migration checklist
- Troubleshooting guide
- Performance tips and benchmarks

## GitHub Repository

All code examples are ready to clone and run:

```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

## Complete Series Overview

You've now explored the essential Java 17 features every senior developer should know:

1. **var keyword** - Cleaner variable declarations with type inference
2. **Records** - Immutable data carriers with zero boilerplate
3. **Sealed Classes** - Controlled inheritance hierarchies with compiler verification
4. **Pattern Matching** - Type checking and casting combined atomically
5. **Switch Expressions** - Modern conditional logic with exhaustiveness checking
6. **Text Blocks** - Readable multi-line string literals for embedded languages

Together, these features represent 20+ years of language evolution, solving real problems that affected developer productivity and code quality.

**Ready to upgrade? Start with Part 1: var Keyword and explore the full series!**
