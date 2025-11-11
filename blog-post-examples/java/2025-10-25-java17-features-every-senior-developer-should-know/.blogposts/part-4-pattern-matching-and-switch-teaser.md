# Java 17 Features Every Senior Developer Should Know - Part 4: Pattern Matching & Switch Expressions

This is **Part 4** of a comprehensive series on essential Java 17+ features that every senior developer must understand when working with modern Java. Whether you're migrating from Java 8, 11, or 13, or you're mentoring junior developers, this series covers the language features that will define your production code quality and team productivity for the next decade.

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

Before Java 16, checking types required redundant, mechanical code:

```java
// Pre-Java 16: The instanceof-cast ceremony
if (obj instanceof String) {
    String s = (String) obj;  // Manual cast required
    return "String of length " + s.length();
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;  // Same pattern repeated
    return "Integer: " + i;
}
```

You write `String` three times in a single branch. The pattern is mechanical and error-prone.

Switch statements were even worse—they encouraged fall-through bugs and forced variable mutation:

```java
// Pre-Java 14: Classic switch with fall-through danger
switch (month) {
    case "January":
    case "March":  // Forgot break? Silent bug.
        days = 31;
        break;
    default:
        throw new IllegalArgumentException();
}
```

Pattern matching (Java 16) and switch expressions (Java 14) solve both problems: **combine type checks with casting atomically, and make switches expressions instead of statements.**

---

## What is Pattern Matching?

**Pattern matching** combines type checking and variable binding in a single operation. Instead of checking `instanceof`, casting, and extracting—do it all at once:

```java
// Modern way with pattern matching (Java 16+)
if (obj instanceof String s) {
    return "String of length " + s.length();  // 's' is already the String
}
```

The compiler automatically:
1. Checks the type
2. Casts to that type
3. Binds the variable `s`

All in one expression. No redundancy. No extra casts.

## Basic Pattern Matching Example

```java
public String describe(Object obj) {
    // Pattern matching with instanceof
    if (obj instanceof String s) {
        return "String: " + s;
    } else if (obj instanceof Integer i) {
        return "Integer: " + i;
    } else if (obj instanceof Double d) {
        return "Double: " + d;
    } else {
        return "Unknown type";
    }
}
```

Compared to pre-Java 16, this eliminates:
- Manual casts
- Type repetition
- Variable declaration boilerplate

## Switch Expressions

Before Java 14, switch was a statement (didn't return values). Modern switch is an expression:

```java
// Old way: switch statement
int days;
switch (month) {
    case "January": days = 31; break;
    case "February": days = 28; break;
    default: throw new IllegalArgumentException();
}

// Modern way: switch expression (Java 14+)
var days = switch (month) {
    case "January" -> 31;
    case "February" -> 28;
    case "March", "April" -> 31; // Multiple cases with arrow
    default -> throw new IllegalArgumentException();
};
```

Key improvements:
- ✅ Returns a value directly
- ✅ Arrow syntax (`->`) no fall-through
- ✅ Multiple cases in one line
- ✅ No need for break statements
- ✅ Exhaustiveness checking (with sealed types)

## Pattern Matching in Switch

Combine both features with sealed classes:

```java
public sealed interface Animal permits Dog, Cat, Bird {}
public record Dog(String name) implements Animal {}
public record Cat(String name) implements Animal {}
public record Bird(String name) implements Animal {}

public String sound(Animal animal) {
    return switch (animal) {
        case Dog dog -> dog.name() + " says: Woof!";
        case Cat cat -> cat.name() + " says: Meow!";
        case Bird bird -> bird.name() + " says: Tweet!";
        // Compiler knows all cases are covered!
    };
}
```

No `default` needed—the compiler verifies exhaustiveness because `Animal` is sealed.

## Key Insight

Pattern matching represents a paradigm shift from imperative ("how do I check and cast?") to declarative ("what pattern should match?"). Combined with sealed classes, it enables exhaustive checking at compile-time. This reduces bugs and makes code intention crystal clear.

---

## Want to Learn More?

This is just a preview of pattern matching and switch expressions. **[Read the full Part 4 article](part-4-pattern-matching-and-switch.md)** where you'll discover:

- Deep history: why Java needed pattern matching
- Pattern matching in other languages (Swift, Scala, Kotlin, TypeScript)
- Guards and complex pattern conditions
- Type patterns, type tests, and deconstruction patterns
- Switch expressions vs statements: when to use each
- Integration with sealed classes for exhaustive checking
- 10+ practical examples from real-world scenarios
- Performance considerations and best practices
- Common pitfalls and how to avoid them

The full article includes comprehensive examples, performance analysis, and production-ready patterns.

**All code examples are runnable in the GitHub repository:**
```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```
