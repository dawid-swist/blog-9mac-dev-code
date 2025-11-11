# Java 17 Features Every Senior Developer Should Know - Part 2: Records

This is **Part 2** of a comprehensive series on essential Java 17+ features that every senior developer must understand when working with modern Java. Whether you're migrating from Java 8, 11, or 13, or you're mentoring junior developers, this series covers the language features that will define your production code quality and team productivity for the next decade.

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

For 20+ years, every Java developer has written the same ceremony over and over: a class with private final fields, a constructor, getters, `equals()`, `hashCode()`, and `toString()`. The most common data structure in Java—a simple immutable holder of data—required 30-50 lines of boilerplate per class.

Other languages solved this decades ago. Scala case classes (2003), Kotlin data classes (2016), even C# record types (2020)—all understood that **data carriers should be one line, not thirty.**

Project Lombok tried to fill the gap with annotations, but it felt like a hack: code generation invisible in source form, IDE plugins required, another build-time dependency.

Java 17 finally brought a native solution: **Records.** They're not just a convenience—they fundamentally change how you model data in Java.

---

## What are Records?

A **record** is a special class designed to be an immutable carrier for data. When you declare a record, the compiler automatically generates constructors, accessors, `equals()`, `hashCode()`, and `toString()`.

## The Problem It Solves

Before Java 16, creating a simple data class required lots of boilerplate:

```java
// Pre-Java 16: 30+ lines for a simple data holder
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() { return x; }
    public int y() { return y; }

    @Override
    public boolean equals(Object o) {
        // 10+ lines of comparison logic
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

## Records Solution

```java
// Java 16+: One line does it all
public record Point(int x, int y) {}
```

That's it. The compiler generates everything automatically.

## How Records Work

```java
// Declare a record
public record Person(String name, int age) {}

// Constructor is automatically generated
var person = new Person("Alice", 30);

// Accessors (no "get" prefix!)
System.out.println(person.name());    // Alice
System.out.println(person.age());     // 30

// equals() is auto-generated
var person2 = new Person("Alice", 30);
System.out.println(person.equals(person2));  // true

// toString() is auto-generated
System.out.println(person);  // Person[name=Alice, age=30]

// Records are immutable - no setters
// person.name = "Bob";  // Compile error!
```

## What Compiler Generates

For `record Person(String name, int age) {}`, the compiler creates:

1. **Private final fields** - `name` and `age`
2. **Canonical constructor** - `Person(String name, int age)`
3. **Accessors** - `name()` and `age()` (not getters)
4. **equals()** - Compares all fields
5. **hashCode()** - Combines all fields
6. **toString()** - Shows field values

## Simple Example

```java
public record Book(String title, String author, int year) {}

public class RecordExample {
    public static void main(String[] args) {
        var book = new Book("Clean Code", "Robert Martin", 2008);

        System.out.println(book);  // Book[title=Clean Code, author=Robert Martin, year=2008]
        System.out.println("Title: " + book.title());
        System.out.println("Author: " + book.author());

        var book2 = new Book("Clean Code", "Robert Martin", 2008);
        System.out.println("Equal: " + book.equals(book2));  // true
    }
}
```

**Output:**
```
Book[title=Clean Code, author=Robert Martin, year=2008]
Title: Clean Code
Author: Robert Martin
Equal: true
```

## Key Characteristics

- **Immutable** - Fields are `private final`, no setters
- **No inheritance** - Records are implicitly final (can't extend them)
- **Transparent** - Fields and behavior are explicit
- **Concise** - Minimal syntax, no ceremony
- **Good for data** - DTOs, value objects, domain entities

## When to Use Records

✅ **Great for:**
- API request/response objects
- Data transfer objects (DTOs)
- Immutable value objects
- Domain entities with no behavior
- Configuration objects

❌ **Not ideal for:**
- Classes with mutable state
- Complex business logic
- Hierarchies (records are final)

## Key Insight

Records eliminate boilerplate while maintaining full type safety and immutability. They're the native Java solution to the problem that Project Lombok tried to solve. With records, your code clearly expresses intent: "this class carries immutable data, nothing more."

---

## Want to Learn More?

This is just a preview of what records can do. **[Read the full Part 2 article](part-2-records.md)** where you'll discover:

- Generic records with type parameters
- Compact and custom constructors
- Record component validation
- Performance comparison with traditional classes
- Advanced patterns: sealed record hierarchies, records with methods
- 10+ detailed examples from real-world use cases
- Integration with other Java 17 features

The full article includes comprehensive examples, unit tests, performance benchmarks, and best practices from production codebases.

**All code examples are runnable in the GitHub repository:**
```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```
