# Java 17 Features Every Senior Developer Should Know - Part 4: Pattern Matching & Switch Expressions

**Part 4 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

**This is a teaser article.** Quick intro to the features, problems they solve, and examples. Read the full article for pattern matching in other languages, guards, and 10+ scenarios.

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
    default: throw new IllegalArgumentException();
}
```

Pattern matching and switch expressions solve both problems.

---

## What is Pattern Matching?

**Pattern matching** combines type checking and variable binding in a single operation:

```java
// Modern way with pattern matching (Java 16+)
if (obj instanceof String s) {
    return "String of length " + s.length();  // 's' is already String
}
```

No redundancy. No extra casts. The compiler automatically checks the type, casts, and binds the variable `s`.

---

## Switch Expressions

Before Java 14, switch was a statement (didn't return values). Modern switch is an expression:

```java
// Modern way: switch expression (Java 14+)
var days = switch (month) {
    case "January", "March", "May" -> 31;
    case "February" -> 28;
    case "April", "June" -> 30;
    default -> throw new IllegalArgumentException();
};
```

Arrow syntax (`->`) eliminates fall-through. No `break` statements needed.

---

## Key Insight

Pattern matching represents a paradigm shift from imperative ("how do I check and cast?") to declarative ("what pattern should match?"). Combined with sealed classes, it enables exhaustive checking at compile-time.

---

## Read the Full Article

Discover more in **[Part 4: Pattern Matching & Switch Expressions](part-4-pattern-matching-and-switch.md)**:
- Pattern matching in other languages (Swift, Scala, Kotlin, TypeScript)
- Guards and complex pattern conditions
- Type patterns, type tests, and deconstruction patterns
- Switch expressions vs statements: when to use each
- Integration with sealed classes for exhaustive checking
- 10+ practical examples from real-world scenarios

**All code examples are in the GitHub repository:**
```
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 5 - Text Blocks (multi-line string literals)
