# Java 17 Features - Syntax Cheat Sheet

**Quick Reference Guide** | Print-friendly version for desk reference

This is Part 6 (final) of "Java 17 Features Every Senior Developer Should Know" - a condensed syntax reference for all features covered in this series. Perfect for printing and keeping at your desk.

---

## Table of Contents

1. [var Keyword (Java 10)](#var-keyword-java-10)
2. [Records (Java 16)](#records-java-16)
3. [Sealed Classes (Java 17)](#sealed-classes-java-17)
4. [Pattern Matching (Java 16)](#pattern-matching-java-16)
5. [Switch Expressions (Java 14)](#switch-expressions-java-14)
6. [Text Blocks (Java 15)](#text-blocks-java-15)
7. [Combined Example](#combined-example)
8. [Migration Checklist](#migration-checklist)

---

## var Keyword (Java 10)

**JEP 286** | Local variable type inference

### Syntax

```java
var name = expression;
```

### ✅ Use When

- Type is obvious from right side: `var user = new User()`
- Long generic types: `var map = new HashMap<String, List<Employee>>()`
- Stream chains: `var filtered = list.stream().filter(...)`
- For-loops: `for (var item : items)`

### ❌ Don't Use When

- Type isn't clear: `var data = fetchData()` (what type?)
- Need supertype: `List<String> list = new ArrayList<>()` (flexibility)
- Fields, parameters, return types (not allowed)
- Lambda/method reference alone: `var c = String::length` (error)

### Examples

```java
// ✅ Good
var message = "Hello";
var count = 42;
var users = List.of("Alice", "Bob");
var map = new HashMap<String, Integer>();

// ❌ Bad
var x = getData();           // unclear type
var y = null;                // error: cannot infer
var list = new ArrayList<>(); // error: need type args
```

### Quick Rules

- Compile-time type inference (NOT dynamic typing)
- Local variables only
- Must have initializer
- Type never changes

---

## Records (Java 16)

**JEP 395** | Immutable data carriers

### Syntax

```java
record Name(Type1 field1, Type2 field2) {
    // optional: custom methods, validation
}
```

### ✅ Use When

- Data Transfer Objects (DTOs)
- Value objects (Point, Money, Range)
- Configuration objects
- API request/response models
- Return values from methods (multiple values)

### ❌ Don't Use When

- Need mutability
- Need inheritance (records are final)
- Want to hide internal structure
- Need JavaBeans getters (use `field()` not `getField()`)
- Complex lazy initialization

### Examples

```java
// Basic record
record Point(int x, int y) {}

// With validation (compact constructor)
record Range(int start, int end) {
    public Range {
        if (start > end) throw new IllegalArgumentException();
    }
}

// With custom methods
record Person(String name, int age) {
    public boolean isAdult() { return age >= 18; }
    public Person withAge(int newAge) { return new Person(name, newAge); }
}

// Generic record
record Pair<T, U>(T first, U second) {}

// Implementing interface
record Circle(double radius) implements Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}
```

### What You Get Automatically

- Constructor: `public Point(int x, int y)`
- Accessors: `p.x()`, `p.y()` (NOT getX(), getY())
- `equals()`, `hashCode()`, `toString()`
- Fields are `private final`
- Class is `final`

### Quick Rules

- Immutable by default
- All fields final
- Cannot extend other classes
- Can implement interfaces
- Defensive copying needed for mutable components

---

## Sealed Classes (Java 17)

**JEP 409** | Controlled inheritance

### Syntax

```java
sealed interface/class Name permits SubType1, SubType2 {
}

// Subtypes must be: final, sealed, or non-sealed
```

### ✅ Use When

- Closed type hierarchies (known subtypes only)
- Domain modeling with fixed alternatives
- Exhaustive pattern matching
- API design where you control all implementations

### ❌ Don't Use When

- Open for extension (third-party implementations)
- Plugin architectures
- Need unlimited subclassing

### Examples

```java
// Sealed interface with records
sealed interface Shape permits Circle, Rectangle, Triangle {}

record Circle(double radius) implements Shape {}
record Rectangle(double width, double height) implements Shape {}
record Triangle(double base, double height) implements Shape {}

// Sealed class hierarchy
sealed class Result<T> permits Success, Failure {}

final class Success<T> extends Result<T> {
    private final T value;
    // ...
}

final class Failure<T> extends Result<T> {
    private final String error;
    // ...
}

// With non-sealed for extension point
sealed interface Payment permits CreditCard, DebitCard, ExtensiblePayment {}
final class CreditCard implements Payment {}
final class DebitCard implements Payment {}
non-sealed interface ExtensiblePayment extends Payment {} // Open for extension
```

### Permitted Subtypes Must

- Be in same module (or same package if unnamed module)
- Directly extend/implement the sealed type
- Be declared as: `final`, `sealed`, or `non-sealed`

### Quick Rules

- Compiler knows all subtypes
- Enables exhaustive pattern matching
- Subtypes must be declared in permits clause
- Better than marker interfaces for closed sets

---

## Pattern Matching (Java 16)

**JEP 394** | Pattern matching for instanceof

### Syntax

```java
if (obj instanceof Type variableName) {
    // use variableName here
}
```

### ✅ Use When

- Type checking with immediate use
- Implementing `equals()` methods
- Polymorphic dispatch
- Guard conditions: `obj instanceof String s && s.length() > 5`

### ❌ Don't Use When

- Polymorphism would be better (add methods to types)
- Type checking indicates design smell
- Performance is absolutely critical

### Examples

```java
// Basic pattern matching
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}

// With guards
if (obj instanceof String s && s.length() > 0) {
    return s.toUpperCase();
}

// Simplified equals()
@Override
public boolean equals(Object obj) {
    return obj instanceof Point p && this.x == p.x && this.y == p.y;
}

// Type hierarchy
if (shape instanceof Circle c) {
    return Math.PI * c.radius() * c.radius();
} else if (shape instanceof Rectangle r) {
    return r.width() * r.height();
}
```

### Scope Rules

```java
if (obj instanceof String s && s.length() > 5) {
    // 's' in scope here
}

if (!(obj instanceof String s)) {
    // 's' NOT in scope
} else {
    // 's' IS in scope here
}
```

### Quick Rules

- Pattern variable scoped by flow analysis
- instanceof returns false for null (safe)
- No manual cast needed
- Combines type check + cast + assignment

---

## Switch Expressions (Java 14)

**JEP 361** | Switch as expression with arrow syntax

### Syntax

```java
// Arrow syntax (no fall-through)
var result = switch (value) {
    case LABEL1 -> expression1;
    case LABEL2, LABEL3 -> expression2;
    default -> expressionN;
};

// Multi-statement with yield
var result = switch (value) {
    case LABEL1 -> {
        // statements
        yield value1;
    }
};
```

### ✅ Use When

- Mapping enum values
- Returning values from branches
- Exhaustive enum matching (no default needed)
- Multi-way conditional logic

### ❌ Don't Use When

- Simple if-else would suffice
- Need fall-through (use colon syntax)
- Cases are complex and unrelated

### Examples

```java
// Basic switch expression
String dayType = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
    case SATURDAY, SUNDAY -> "Weekend";
};

// With yield
int result = switch (op) {
    case ADD -> {
        System.out.println("Adding");
        yield a + b;
    }
    case SUBTRACT -> {
        System.out.println("Subtracting");
        yield a - b;
    }
    default -> throw new IllegalArgumentException();
};

// Exhaustive enum (no default needed)
int days = switch (season) {
    case SPRING, SUMMER, FALL, WINTER -> 3;
};
```

### Quick Rules

- Arrow `->` prevents fall-through
- Must be exhaustive (all cases or default)
- Use `yield` for multi-statement branches
- No `break` needed with arrows
- Can mix with pattern matching (future Java)

---

## Text Blocks (Java 15)

**JEP 378** | Multi-line string literals

### Syntax

```java
String text = """
    content here
    more content
    """;
```

### ✅ Use When

- Multi-line strings (JSON, SQL, HTML, XML)
- Embedded code snippets
- Long error messages
- Test fixtures

### ❌ Don't Use When

- Single-line strings
- Dynamically built strings
- Need platform-specific line endings

### Examples

```java
// JSON
String json = """
    {
      "name": "Alice",
      "age": 30,
      "email": "alice@example.com"
    }""";

// SQL
String sql = """
    SELECT u.name, u.email, o.total
    FROM users u
    JOIN orders o ON u.id = o.user_id
    WHERE o.status = 'COMPLETED'
    ORDER BY o.created_at DESC""";

// With formatted()
String json = """
    {
      "name": "%s",
      "age": %d
    }""".formatted(name, age);

// Indentation control
String text = """
    Line 1
        Indented line 2
    Line 3
    """;  // Closing delimiter determines indent stripping

// Line continuation
String long = """
    This is a long line that \
    continues on the next line \
    in source code.
    """;

// Essential space
String trailing = """
    Line with space:\s
    """;
```

### Special Escapes

- `\s` - Essential space (survives whitespace stripping)
- `\` - Line continuation (no newline inserted)
- Standard escapes work: `\n`, `\t`, `\"`, `\\`

### Quick Rules

- Opening `"""` must be followed by newline
- Closing `"""` position determines indent
- All line endings normalized to `\n`
- Trailing whitespace auto-removed
- Common indent automatically stripped

---

## Combined Example

Real-world example using all features together:

```java
package dev.nmac.blog.examples.java17.combined;

// Records for data
record User(String name, String email, int age) {
    public User {
        if (age < 0) throw new IllegalArgumentException("Age must be positive");
    }
}

record Order(String id, double total, String status) {}

// Sealed type hierarchy
sealed interface PaymentResult permits Success, Failure {}
record Success(String transactionId, double amount) implements PaymentResult {}
record Failure(String errorCode, String message) implements PaymentResult {}

public class CombinedExample {

    // Text blocks for configuration
    private static final String CONFIG_TEMPLATE = """
        {
          "environment": "%s",
          "maxConnections": %d,
          "timeout": %d
        }""";

    // Switch expression with pattern matching
    public static String processPayment(PaymentResult result) {
        return switch (result) {
            case Success(var txId, var amount) ->
                "Payment successful: $%.2f (TX: %s)".formatted(amount, txId);
            case Failure(var code, var msg) ->
                "Payment failed [%s]: %s".formatted(code, msg);
        };
    }

    // var with records
    public static void demonstrateVarWithRecords() {
        var users = List.of(
            new User("Alice", "alice@test.com", 30),
            new User("Bob", "bob@test.com", 25)
        );

        // Pattern matching + records
        for (var user : users) {
            if (user instanceof User u && u.age() >= 18) {
                System.out.println(u.name() + " is an adult");
            }
        }
    }

    // Text block query with var
    public static String getUsersQuery(String status) {
        var query = """
            SELECT u.name, u.email, COUNT(o.id) as order_count
            FROM users u
            LEFT JOIN orders o ON u.id = o.user_id
            WHERE o.status = '%s'
            GROUP BY u.name, u.email
            ORDER BY order_count DESC""".formatted(status);

        return query;
    }
}
```

---

## Migration Checklist

### From Java 8 → Java 17

**Phase 1: Low-Risk Quick Wins**
- ☐ Replace verbose local variables with `var`
- ☐ Use text blocks for JSON/SQL/HTML literals
- ☐ Replace data classes with records (DTOs, value objects)

**Phase 2: Refactoring**
- ☐ Convert switch statements returning values to switch expressions
- ☐ Replace instanceof-cast patterns with pattern matching
- ☐ Update `equals()` methods to use pattern matching

**Phase 3: Architecture**
- ☐ Model closed hierarchies with sealed types
- ☐ Combine records + sealed for domain modeling
- ☐ Leverage exhaustive pattern matching

### Compatibility Notes

- ✅ Java 17 is backward compatible with Java 8 bytecode
- ✅ Can mix old and new syntax
- ✅ No breaking changes to existing code
- ⚠️ New features require Java 17 compile target
- ⚠️ Records/sealed classes produce different bytecode

### Build Configuration

```groovy
// Gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType(JavaCompile) {
    options.release = 17
}
```

```xml
<!-- Maven -->
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

---

## Quick Comparison Table

| Feature | Java Version | Replaces | Main Benefit |
|---------|--------------|----------|--------------|
| var | 10 | Explicit types | Less boilerplate |
| Switch Expressions | 14 | Switch statements | Returns values, no fall-through |
| Text Blocks | 15 | String concatenation | No escape hell |
| Records | 16 | Data classes | Immutable DTOs in 1 line |
| Pattern Matching | 16 | instanceof + cast | Combine check and cast |
| Sealed Classes | 17 | Open hierarchies | Controlled inheritance |

---

## When to Use What

### Choose var when:
```java
var config = new ComplexConfiguration<String, List<Setting>>();  // ✅ Long type
var users = fetchUsers();  // ❌ Unclear what fetchUsers returns
```

### Choose records when:
```java
record Point(int x, int y) {}  // ✅ Immutable data
class Counter { private int value; }  // ❌ Needs mutability
```

### Choose sealed when:
```java
sealed interface Result permits Success, Failure {}  // ✅ Fixed alternatives
interface Plugin {}  // ❌ Open for extension
```

### Choose pattern matching when:
```java
if (obj instanceof String s && s.length() > 0)  // ✅ Type check + use
if (obj instanceof Comparable) shape.draw();  // ❌ Polymorphism better
```

### Choose switch expressions when:
```java
var type = switch (day) { case MON -> "Work"; ... };  // ✅ Returns value
switch (x) { case 1: doA(); break; case 2: doB(); }  // ❌ Statements
```

### Choose text blocks when:
```java
var json = """{"name": "Alice"}""";  // ✅ Multi-line
var msg = "Hello";  // ❌ Single line
```

---

## Performance Notes

| Feature | Runtime Impact | Notes |
|---------|----------------|-------|
| var | Zero | Compile-time only |
| Records | Minimal | Same as manual class |
| Sealed | Zero | Compile-time checking |
| Pattern Matching | Negligible | Slight overhead vs manual cast |
| Switch Expressions | Zero | Same bytecode as statement |
| Text Blocks | Zero | Same as concatenation |

---

## Summary

Java 17 brings these major improvements:

1. **Less Boilerplate**: var, records, text blocks eliminate repetitive code
2. **More Safety**: Pattern matching, sealed classes, exhaustive switches catch errors at compile time
3. **Better Readability**: All features make intent clearer
4. **Backward Compatible**: Existing Java 8 code still works
5. **LTS Support**: Java 17 supported until 2029

### Next Steps

1. Update to Java 17
2. Start with var and text blocks (easy wins)
3. Convert DTOs to records
4. Adopt pattern matching in new code
5. Use sealed types for domain modeling

---

**End of Series**

This concludes "Java 17 Features Every Senior Developer Should Know"

- **Part 1**: [var Keyword](part-1-introduction-and-var.md)
- **Part 2**: [Records](part-2-records.md)
- **Part 3**: [Sealed Classes](part-3-sealed-classes.md)
- **Part 4**: [Pattern Matching & Switch Expressions](part-4-pattern-matching-and-switch.md)
- **Part 5**: [Text Blocks](part-5-text-blocks.md)
- **Part 6**: Syntax Cheat Sheet (this document)

---

## Series Navigation

- **← [Part 5: Text Blocks](./part-5-text-blocks.md)** - Multi-line string literals
- **← [Series Overview](./README.md)** - Overview and series guide

[Back to Blog Posts](../)  |  [Back to Repository](../../../)
