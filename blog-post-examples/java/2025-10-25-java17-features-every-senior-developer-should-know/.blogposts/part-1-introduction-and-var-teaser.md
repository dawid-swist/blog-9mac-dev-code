# Java 17 Features Every Senior Developer Should Know - Part 1: var Keyword

**Part 1 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

**This is a teaser article.** A quick introduction to the feature, the problem it solves, and a simple example. Read the full article for deep dive with 15+ detailed examples, best practices, and unit tests.

---

## Why This Matters

Java has been criticized for verbosity forever. When modern languages got type inference decades ago, Java developers had to write types explicitly everywhere. By 2018, with the Stream API introducing complex generic types, the pain became unbearable:

```java
Map<String, List<Transaction>> transactions = new HashMap<String, List<Transaction>>();
```

That's noise. The compiler already knows the type from the right side—why repeat it?

The `var` keyword (Java 10) solved this: **"Let the compiler figure out the type, and let developers focus on logic."**

---

## What is var?

The `var` keyword lets the compiler automatically infer the type of a local variable from its initialization. It's NOT dynamic typing—the type is determined at compile-time and never changes.

```java
var name = "John";                    // String
var count = 42;                       // int
var numbers = List.of(1, 2, 3);      // List<Integer>
var map = new HashMap<String, Integer>(); // HashMap<String, Integer>

// var eliminates repetition without sacrificing type safety
```

**Key rules:**
- Only for local variables (not fields, parameters, or return types)
- Must be initialized
- Cannot change type after assignment

---

## Simple Example

```java
public class VarExample {
    public static void main(String[] args) {
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

---

## Key Insight

`var` reduces boilerplate without sacrificing type safety. It's particularly valuable with the Stream API and complex generic types, where explicit types become noise rather than documentation.

---

## When to Use var

### ✅ Perfect for:

```java
// Long generic types - clarity without repetition
var customers = repository.findActiveCustomers();

// Stream operations - complex chains
var result = employees.stream()
    .filter(e -> e.salary() > 50000)
    .map(e -> e.name())
    .collect(Collectors.toList());

// Clear from context - type is obvious
var now = LocalDateTime.now();
var file = new File("data.txt");
var json = jsonParser.parse(data);
```

### ❌ Avoid when:

```java
// Type is NOT obvious to reader
var result = calculateSomething(x, y);  // What type is result?

// Public API - type should be explicit
public var getData() { ... }  // Not allowed anyway

// Too cryptic abbreviations
var x = 10;  // Better: var count = 10;
var cfg = new Configuration();  // Better: var config = ...
```

## Common Pitfalls

**Don't initialize with null:**
```java
var x = null;  // Compiler error: cannot infer type from null
```

**Can't use with multiple statements:**
```java
var name, age;  // Compile error: must initialize

// Instead use explicit types
String name;
int age;
```

**Type is determined once and fixed:**
```java
var number = 42;       // int
number = "string";     // Compile error! number is int, not String
```

## Read the Full Article

Discover much more in **[Part 1: Introduction & var Keyword on blog.9mac.dev]([BLOG_LINK_HERE])**:
- Type inference with anonymous classes and intersection types
- Advanced use cases with collections and functional interfaces
- Best practices from production codebases
- Common pitfalls and how to avoid them
- 15+ detailed examples with unit tests
- Performance implications
- Integration with other Java 17+ features

## GitHub Repository

All code examples are ready to clone and run:

```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 2 - Records (immutable data carriers)
