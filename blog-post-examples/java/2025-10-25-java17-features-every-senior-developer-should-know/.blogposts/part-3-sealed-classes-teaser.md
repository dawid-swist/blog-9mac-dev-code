# Java 17 Features Every Senior Developer Should Know - Part 3: Sealed Classes

**Part 3 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

**This is a teaser article.** A quick introduction to the feature, the problem it solves, and a simple example. Read the full article for deep dive with multi-level hierarchies, non-sealed modifiers, and 5+ comprehensive real-world examples.

---

## Why This Matters

Imagine a payment processing system. You design an abstract `Payment` class for credit cards, debit cards, and cash. A year later, another team adds `CryptoPayment` without telling you. Now your switch statement that handles all payment types doesn't—and you have a bug in production that the compiler never caught.

The problem: **Java forces a false choice between `final` (too restrictive) and open (too permissive).** You can't say "extend this class, but ONLY in these specific ways."

Sealed classes (Java 17) solve this: **fine-grained control over which classes can extend yours, with compiler-enforced completeness checking.**

---

## What are Sealed Classes?

A **sealed class** restricts which other classes can extend it. You explicitly list the permitted subclasses using the `permits` clause:

```java
public sealed abstract class Payment
    permits CreditCard, DebitCard, Cash {
    public abstract void process();
}

public final class CreditCard extends Payment { }
public final class DebitCard extends Payment { }
public final class Cash extends Payment { }

// This won't compile - CryptoPayment not in permits list
// public class CryptoPayment extends Payment { }
```

---

## Exhaustive Pattern Matching

Sealed classes with pattern matching eliminate the need for `default` cases:

```java
return switch (payment) {
    case CreditCard cc -> "Card: " + cc.cardNumber();
    case DebitCard dc -> "Debit: " + dc.accountNumber();
    case Cash cash -> "Cash: " + cash.amount();
    // Compiler verifies all cases covered!
};
```

Add a new implementation? The compiler forces you to update all switch statements.

---

## Key Insight

Sealed classes give you the middle ground between `final` (no extensions) and open (unlimited extensions). They enable the compiler to verify completeness, preventing bugs and making refactoring safer.

---

## Read the Full Article

Discover more in **[Part 3: Sealed Classes](part-3-sealed-classes.md)**:
- Design philosophy behind sealed classes
- Multi-level sealed hierarchies with practical patterns
- The `non-sealed` modifier in real-world scenarios
- Sealed interfaces with complex implementations
- 5+ comprehensive examples: payments, shapes, vehicles, JSON
- Reflection API for runtime introspection
- Best practices and common pitfalls

**All code examples are in the GitHub repository:**
```
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 4 - Pattern Matching & Switch Expressions
