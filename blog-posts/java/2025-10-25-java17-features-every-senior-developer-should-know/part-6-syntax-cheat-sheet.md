# Java 17 Features Every Senior Developer Should Know - Part 6: Syntax Cheat Sheet & Reference Guide

**Complete Reference for Desktop** | Print-friendly version for desk reference

This is Part 6 (final) of "Java 17 Features Every Senior Developer Should Know" - a comprehensive reference covering syntax, usage patterns, feature interactions, and migration strategies for all 6 features covered in Parts 1-5. Perfect for printing and keeping at your desk as you work with modern Java code.

## How to Use This Series

**First time learning Java 17 features?**
- Read [Part 1: var Keyword](part-1-introduction-and-var.md) → [Part 2: Records](part-2-records.md) → [Part 3: Sealed Classes](part-3-sealed-classes.md) → [Part 4: Pattern Matching](part-4-pattern-matching-and-switch.md) → [Part 5: Text Blocks](part-5-text-blocks.md)
- Each part includes practical examples and best practices
- Estimated reading time: 1-2 hours total

**Need a quick reference?**
- Use the Quick Reference Cards below (this page)
- Each section provides syntax, examples, and use cases at a glance
- Links to full parts for detailed explanations

**Looking for specific feature?**
- Use the Table of Contents below to jump to any feature
- Each Quick Reference Card links back to its full part

**Want to understand feature interactions?**
- See [Feature Interaction Patterns](#feature-interaction-patterns) section
- Shows real-world scenarios combining multiple features

**Planning a migration?**
- See [Migration Strategy](#migration-strategy) section
- Includes phased approach and compatibility notes

---

## Table of Contents

1. [Series Overview & Timeline](#series-overview--timeline)
2. [Feature Comparison Matrix](#feature-comparison-matrix)
3. [Quick Reference Cards](#quick-reference-cards)
   - [var Keyword (Java 10)](#var-keyword-java-10)
   - [Records (Java 16)](#records-java-16)
   - [Sealed Classes (Java 17)](#sealed-classes-java-17)
   - [Pattern Matching (Java 16)](#pattern-matching-java-16)
   - [Switch Expressions (Java 14)](#switch-expressions-java-14)
   - [Text Blocks (Java 15)](#text-blocks-java-15)
4. [Feature Interaction Patterns](#feature-interaction-patterns)
5. [Decision Trees](#decision-trees)
6. [Migration Strategy](#migration-strategy)
7. [Pitfalls & Gotchas Quick Reference](#pitfalls--gotchas-quick-reference)
8. [JEP Reference Index](#jep-reference-index)
9. [FAQ & Troubleshooting](#faq--troubleshooting)
10. [Series Navigation](#series-navigation)

---

## Series Overview & Timeline

This series covers **6 major Java features spanning Java 10-17**, representing over 20 years of language evolution:

| Part | Feature | Release | JEP | Key Benefit |
|------|---------|---------|-----|------------|
| 1 | **var** - Local variable type inference | Java 10 | 286 | Less boilerplate with type inference |
| 2 | **Records** - Immutable data carriers | Java 16 | 395 | One-line immutable data classes |
| 3 | **Sealed Classes** - Controlled inheritance | Java 17 | 409 | Compiler enforces closed hierarchies |
| 4 | **Pattern Matching** - Type check + cast | Java 16 | 394 | Atomic type checking and variable binding |
| 5 | **Switch Expressions** - Returns values | Java 14 | 361 | Cleaner conditional logic with exhaustiveness |
| 6 | **Text Blocks** - Multi-line strings | Java 15 | 378 | No escape sequence hell for JSON/SQL/HTML |

**Why These Features Matter:**
- **Part 1-2**: Reduce boilerplate (less code to write)
- **Part 3-4**: Increase safety (compiler catches more errors)
- **Part 5-6**: Improve readability (code matches intent)

---

## Feature Comparison Matrix

### Decision At-a-Glance

| Challenge | Solution | When | Avoid |
|-----------|----------|------|-------|
| Long generic types | `var` | Type obvious from right side | Type unclear from context |
| Immutable data classes | `Records` | DTOs, value objects | Mutable data, need inheritance |
| Controlled inheritance | `Sealed classes` | Fixed type hierarchies | Open-for-extension APIs |
| Type checking + casting | `Pattern matching` | Checking types with immediate use | Polymorphism is better choice |
| Returning from switch | `Switch expressions` | Enum mapping, value computation | Simple if-else, need fall-through |
| Multi-line strings | `Text blocks` | JSON, SQL, HTML, XML | Single line strings |

### Feature Dependencies

```
var (Java 10)
    ↓ (enables clean declaration of)
Records (Java 16)
    ↓ (work well with)
Sealed Classes (Java 17)
    ↓ (enables exhaustive checking with)
Pattern Matching (Java 16)
    ↓ (used in)
Switch Expressions (Java 14)
    ↓ (format data with)
Text Blocks (Java 15)
```

---

## Quick Reference Cards

### var Keyword (Java 10)

**JEP 286** | Local variable type inference

📖 **See also**: [Part 1: Introduction & var Keyword](part-1-introduction-and-var.md) - Full feature deep dive with examples and pitfalls

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

📖 **See also**: [Part 2: Records](part-2-records.md) - Full feature deep dive with examples and best practices

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

📖 **See also**: [Part 3: Sealed Classes](part-3-sealed-classes.md) - Design patterns and hierarchy control explained

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

📖 **See also**: [Part 4: Pattern Matching & Switch Expressions](part-4-pattern-matching-and-switch.md) - Type patterns and exhaustiveness checking

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

📖 **See also**: [Part 4: Pattern Matching & Switch Expressions](part-4-pattern-matching-and-switch.md) - Clean conditional logic with yield

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

📖 **See also**: [Part 5: Text Blocks](part-5-text-blocks.md) - Multi-line strings with security considerations

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

## Feature Interaction Patterns

### Pattern 1: var + Records for Clean Data Structures

```java
// Instead of verbose declarations:
User user = new User("Alice", "alice@test.com", 30);

// Use var with records (inferred from new User(...)):
var user = new User("Alice", "alice@test.com", 30);
var users = List.of(
    new User("Alice", "alice@test.com", 30),
    new User("Bob", "bob@test.com", 25)
);

// Iterate with var:
for (var u : users) {
    System.out.println(u.name());  // u is inferred as User
}
```

**Benefits:**
- Less visual noise with type inference
- Records provide immutability automatically
- Clear and concise declarations

### Pattern 2: Sealed Classes + Pattern Matching for Exhaustiveness

```java
// Define sealed hierarchy:
sealed interface Result<T> permits Success, Failure {}
record Success<T>(T value) implements Result<T> {}
record Failure(String error) implements Result<T> {}

// Exhaustive pattern matching guaranteed by compiler:
String msg = switch (result) {
    case Success(var val) -> "Got: " + val;
    case Failure(var err) -> "Error: " + err;
    // No default needed - compiler verifies all cases covered!
};
```

**Benefits:**
- Compiler prevents missing cases
- New subtypes require code updates
- Sealed + Records + Pattern Matching together = type safety

### Pattern 3: Text Blocks + formatted() for Templating

```java
// Configuration template with var:
var config = """
    {
      "name": "%s",
      "port": %d,
      "debug": %s
    }""".formatted(appName, port, debugMode);

// SQL query building with var:
var query = """
    SELECT * FROM users
    WHERE status = '%s'
    AND created_at > '%s'
    ORDER BY created_at DESC
    LIMIT %d
    """.formatted(status, dateStart, limit);

// Email template:
var email = """
    <h2>Hello, %s!</h2>
    <p>Your order #%s is ready.</p>
    <a href="%s">Track Order</a>
    """.formatted(userName, orderId, trackingUrl);
```

**Benefits:**
- No external template libraries needed
- Multi-line strings with proper formatting
- Type-safe parameter interpolation

### Pattern 4: All Features Combined - Order Processing

```java
// Define domain with records and sealed classes:
sealed interface Payment permits CreditCard, PayPal, BankTransfer {}
record CreditCard(String number, String expiry) implements Payment {}
record PayPal(String email) implements Payment {}
record BankTransfer(String accountNumber) implements Payment {}

record Order(String id, List<OrderItem> items, Payment payment) {}
record OrderItem(String product, int quantity, double price) {}

// Process with all features:
public static String processOrder(Order order) {
    var totalPrice = order.items().stream()
        .mapToDouble(item -> item.price() * item.quantity())
        .sum();

    var result = switch (order.payment()) {
        case CreditCard cc ->
            "Charged $%.2f to card ending in %s".formatted(totalPrice, cc.number().substring(12));
        case PayPal pp ->
            "Charged $%.2f via PayPal account %s".formatted(totalPrice, pp.email());
        case BankTransfer bt ->
            "Charged $%.2f via bank transfer to %s".formatted(totalPrice, bt.accountNumber());
    };

    var confirmation = """
        Order Confirmation
        ==================
        Order ID: %s
        Total: $%.2f
        Status: %s

        Thank you for your purchase!
        """.formatted(order.id(), totalPrice, result);

    return confirmation;
}
```

---

## Decision Trees

### Should You Use var?

```
Type inference needed?
├─ YES: Is type obvious from right side?
│   ├─ YES → Use var ✅
│   └─ NO → Use explicit type ❌
└─ NO: Use explicit type ❌

Examples:
✅ var config = new HashMap<String, List<Setting>>();  // Obvious from new
✅ var filtered = list.stream().filter(...);  // Stream chain type
❌ var result = fetchData();  // What is the return type?
❌ var cache = new HashMap<>();  // Can't infer without type args
```

### Should You Use Records?

```
Defining immutable data?
├─ YES: Need mutable state?
│   ├─ YES → Use class ❌
│   └─ NO: Need to extend class?
│       ├─ YES → Use class ❌
│       └─ NO → Use record ✅
└─ NO: Use class ❌

Examples:
✅ record Point(int x, int y) {}  // Immutable value object
✅ record User(String name, String email) {}  // Data transfer
✅ record Config(String key, String value) {}  // Configuration
❌ record Mutable { private int value; }  // Records are final
❌ class MyRecord extends Parent {}  // Records don't extend
```

### Should You Use Sealed Classes?

```
Controlling inheritance?
├─ YES: Fixed set of subtypes?
│   ├─ YES → Use sealed ✅
│   └─ NO: Open for extension?
│       └─ YES → Use regular class ❌
└─ NO: Use regular class ❌

Examples:
✅ sealed interface Result permits Success, Failure {}  // Fixed types
✅ sealed class Shape permits Circle, Rectangle {}  // Domain model
✅ sealed interface Node permits Leaf, Branch {}  // Tree structure
❌ interface Plugin {}  // Third-party implementations
❌ class OpenAPI {}  // Extensible API
```

### Should You Use Pattern Matching?

```
Type checking with variable use?
├─ YES: Immediate use of casted value?
│   ├─ YES → Use pattern matching ✅
│   └─ NO: Polymorphism better ❌
└─ NO: Use normal control flow ❌

Examples:
✅ if (obj instanceof String s) return s.toUpperCase();
✅ if (shape instanceof Circle c) return c.radius() * 2;
❌ if (obj instanceof Comparable) { ... }  // Use polymorphism
❌ if (result instanceof Success) success(); // No variable use
```

### Should You Use Switch Expressions?

```
Need to return value from switch?
├─ YES: All cases covered?
│   ├─ YES: Exhaustive enum or sealed type?
│   │   ├─ YES → Use switch expression without default ✅
│   │   └─ NO: Has default case?
│   │       └─ YES → Use switch expression with default ✅
│   └─ NO → Use switch statement ❌
└─ NO: Use switch statement ❌

Examples:
✅ var day = switch (date) { case MON -> "Monday"; ... };
✅ int result = switch (op) { case ADD -> a + b; ... };
❌ switch (x) { case 1: doA(); break; }  // No return value
❌ switch (action) { case 1: case 2: doIt(); break; }  // Need fall-through
```

### Should You Use Text Blocks?

```
Multi-line string needed?
├─ YES: Static content or template?
│   ├─ YES → Use text blocks ✅
│   └─ NO: Dynamically built at runtime?
│       └─ YES → Use regular string ❌
└─ NO: Single line?
    └─ YES → Use regular string ❌

Examples:
✅ var json = """{"name": "%s", "age": %d}""".formatted(name, age);
✅ var sql = """SELECT * FROM users WHERE id = ?""";
✅ var html = """<h1>Hello</h1>""";
❌ var msg = "Hello";  // Single line
❌ var dynamic = buildStringAtRuntime();  // Computed
```

---

## Migration Strategy

### Recommended Approach: Phase 1-3 Over Time

**Phase 1: Immediate Wins (Week 1-2)**
- Minimal risk, quick implementation
- No architectural changes needed
- Can be done gradually

Steps:
1. Replace `new HashMap<String, Integer>()` with `var`
2. Replace `new ArrayList<String>()` with `var`
3. Convert string concatenations with multi-line JSON/SQL to text blocks
4. Start new DTOs as records instead of classes

Timeline: 1-2 weeks

**Phase 2: Refactoring (Week 3-6)**
- Moderate effort, good ROI
- Improves code quality
- Some review/testing needed

Steps:
1. Convert switch statements that return values to switch expressions
2. Replace instanceof + cast patterns with pattern matching
3. Update `equals()` methods to use pattern matching
4. Convert old value classes to records

Timeline: 3-4 weeks

**Phase 3: Architecture (Week 7+)**
- Larger effort, long-term value
- Requires design thinking
- Improves maintainability

Steps:
1. Identify closed type hierarchies for sealing
2. Convert to sealed classes/interfaces
3. Leverage exhaustive pattern matching
4. Redesign error handling with sealed Result types
5. Model domain with sealed + records combinations

Timeline: Ongoing

### Compatibility Notes

✅ **Fully Backward Compatible:**
- Java 17 bytecode compatible with Java 8
- Old Java 8 code still works
- Can mix old and new syntax gradually

⚠️ **Important Caveats:**
- All new features require Java 17+ compiler
- Records and sealed classes produce different bytecode
- Runtime target can be Java 8+ but compile target must be 17+
- Some IDE features (code completion, refactoring) depend on IDE version

### Build Configuration Updates

Already shown above in original content - Gradle and Maven configurations remain the same.

---

## Pitfalls & Gotchas Quick Reference

| Pitfall | Feature | Problem | Solution |
|---------|---------|---------|----------|
| Type inference too broad | var | `var list = new ArrayList<>()` - type error | Provide type args: `new ArrayList<Integer>()` |
| Unclear variable type | var | `var data = fetchData()` - unclear | Use explicit type if unclear |
| Mutating records | Records | Records are immutable - can't modify field | Create new instance instead |
| Records extending classes | Records | `record R extends Parent {}` - error | Records are final, extend interfaces only |
| Sealed without permits | Sealed | Sealed must declare all subtypes | Use `permits` clause explicitly |
| Non-sealed chain break | Sealed | `sealed -> non-sealed -> ???` - loses safety | Document intentional break points |
| Pattern var scope | Pattern Match | `if (!(obj instanceof String s)) use s;` - scope issue | Use positive condition or else block |
| Fall-through in switch expr | Switch Expr | Arrow syntax prevents fall-through (good) | Use comma syntax if fall-through needed |
| Text block line ending | Text Blocks | Platform-specific line endings | All normalized to `\n` automatically |
| Indentation stripping | Text Blocks | Unexpected whitespace changes | Closing `"""` determines common indent |
| Text block in middle of line | Text Blocks | `var x = """text"""` - syntax error | Opening `"""` must be followed by newline |

---

## JEP Reference Index

| Feature | JEP | Status | Java Version | Enhancement | Link |
|---------|-----|--------|--------------|-------------|------|
| Local Variable Type Inference | 286 | Final | 10 | Reduces boilerplate with compile-time type inference | [JEP 286](https://openjdk.org/jeps/286) |
| Switch Expressions (Preview) | 325 | Preview | 12 | Expression form of switch statement | [JEP 325](https://openjdk.org/jeps/325) |
| Switch Expressions (Preview) | 354 | Preview | 13 | Refined with `yield` keyword | [JEP 354](https://openjdk.org/jeps/354) |
| Switch Expressions (Final) | 361 | Final | 14 | Stable switch expressions feature | [JEP 361](https://openjdk.org/jeps/361) |
| Text Blocks (Preview) | 355 | Preview | 13 | Initial multi-line string literals | [JEP 355](https://openjdk.org/jeps/355) |
| Text Blocks (Preview) | 368 | Preview | 14 | Refined escapes and indentation | [JEP 368](https://openjdk.org/jeps/368) |
| Records (Preview) | 359 | Preview | 14 | Immutable data carriers | [JEP 359](https://openjdk.org/jeps/359) |
| Records (Preview) | 384 | Preview | 15 | Refined record features | [JEP 384](https://openjdk.org/jeps/384) |
| Pattern Matching for instanceof (Preview) | 394 | Preview | 16 | Type testing with pattern variables | [JEP 394](https://openjdk.org/jeps/394) |
| Records (Final) | 395 | Final | 16 | Stable records feature | [JEP 395](https://openjdk.org/jeps/395) |
| Sealed Classes (Preview) | 360 | Preview | 15 | Restricted class hierarchies | [JEP 360](https://openjdk.org/jeps/360) |
| Sealed Classes (Preview) | 397 | Preview | 16 | Refined sealed feature | [JEP 397](https://openjdk.org/jeps/397) |
| Sealed Classes (Final) | 409 | Final | 17 | Stable sealed classes feature | [JEP 409](https://openjdk.org/jeps/409) |
| Text Blocks (Final) | 378 | Final | 15 | Stable text blocks feature | [JEP 378](https://openjdk.org/jeps/378) |

**Key Insights:**
- Most features have 1-2 preview versions before becoming final
- Pattern matching continues to evolve (future: record patterns, array patterns)
- Records, Sealed Classes, and Pattern Matching are related (work together)

---

## FAQ & Troubleshooting

### var Keyword

**Q: Why can't I use var for fields?**
A: var is for local variable type inference only. Fields need explicit types for API clarity and to avoid issues with reflection/serialization.

**Q: Can I use var with null?**
A: No, `var x = null;` is a compile error. The compiler can't infer the type.

**Q: How does var work with generics?**
A: The right side must provide full type info. `var map = new HashMap<String, Integer>();` works. `var map = new HashMap<>();` doesn't.

### Records

**Q: Can I inherit from another record?**
A: No, records are final. But you can implement interfaces and nest records.

**Q: Do records support defensive copying?**
A: Yes, use the compact constructor to create defensive copies of mutable fields:
```java
record Container(List<String> items) {
    public Container {
        items = new ArrayList<>(items);  // Defensive copy
    }
}
```

**Q: How do I override toString() in records?**
A: You can override auto-generated methods:
```java
record Point(int x, int y) {
    @Override
    public String toString() { return "[" + x + ", " + y + "]"; }
}
```

### Sealed Classes

**Q: What's the difference between sealed and final?**
A: `final` prevents all inheritance. `sealed` allows controlled inheritance from specific permitted subtypes.

**Q: Can I have sealed interface implementing another sealed interface?**
A: Yes, sealed interfaces can extend other sealed interfaces.

**Q: What happens if I use non-sealed?**
A: It opens that branch for further extension, breaking the sealed chain intentionally.

### Pattern Matching

**Q: Can I use pattern matching with null?**
A: `instanceof` is null-safe (returns false). Use explicit null check before instanceof.

**Q: What about pattern variables in nested conditions?**
A: Scope determined by flow analysis. Use `else` blocks and nested ifs carefully.

**Q: Can I pattern match on switch (not just instanceof)?**
A: Yes (Java 17+), see Part 4 for record patterns in switch.

### Switch Expressions

**Q: Why must switch expressions be exhaustive?**
A: Because they return values - every path must produce a result. Catch errors early.

**Q: Can I use switch expressions with sealed types?**
A: Yes, and compiler ensures all permitted types are covered (when sealed).

**Q: What's the difference between -> and : syntax?**
A: Arrow `->` prevents fall-through. Colon `:` allows fall-through (traditional switch).

### Text Blocks

**Q: How do I handle line endings with text blocks?**
A: All line endings are normalized to `\n` regardless of platform.

**Q: Can I use text blocks in string concatenation?**
A: Yes, they're just strings: `"prefix" + """content""" + "suffix"`

**Q: What's the difference between \s and regular space?**
A: `\s` (essential space) survives the trailing whitespace stripping that happens with indentation.

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
