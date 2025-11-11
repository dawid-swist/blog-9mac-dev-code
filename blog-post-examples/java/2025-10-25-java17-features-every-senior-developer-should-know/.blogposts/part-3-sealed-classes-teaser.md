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

## The Three Modifiers

### 1. `sealed` - Parent classes/interfaces that restrict extensions

```java
public sealed abstract class Shape
    permits Circle, Rectangle, Triangle {
    abstract double area();
}
```

### 2. `final` - Permitted subclasses that can't be extended further

```java
public final class Circle extends Shape {
    private double radius;

    @Override
    double area() { return Math.PI * radius * radius; }
}
```

### 3. `non-sealed` - Permitted subclass that can be extended by anyone

```java
public non-sealed class SpecialShape extends Shape {
    // Any class can now extend SpecialShape
    @Override
    double area() { return 0; }
}
```

## Real-World Example: Expression Evaluator

```java
public sealed interface Expr
    permits Expr.Num, Expr.Add, Expr.Sub {

    int evaluate();

    record Num(int value) implements Expr {
        public int evaluate() { return value; }
    }

    record Add(Expr left, Expr right) implements Expr {
        public int evaluate() { return left.evaluate() + right.evaluate(); }
    }

    record Sub(Expr left, Expr right) implements Expr {
        public int evaluate() { return left.evaluate() - right.evaluate(); }
    }
}

// Usage with exhaustive pattern matching:
public class EvalExample {
    public static int eval(Expr expr) {
        return switch (expr) {
            case Expr.Num num -> num.value();
            case Expr.Add add -> add.left().evaluate() + add.right().evaluate();
            case Expr.Sub sub -> sub.left().evaluate() - sub.right().evaluate();
            // No default needed - compiler verifies all cases!
        };
    }
}
```

## Common Patterns

### Single Level Hierarchy
```java
public sealed abstract class Transport
    permits Car, Bus, Train {}
```

### Multi-Level Hierarchy
```java
public sealed class Vehicle permits Car, Truck {}
public final class Car extends Vehicle {}
public non-sealed class Truck extends Vehicle {} // Others can extend Truck
```

### Sealed Interfaces
```java
public sealed interface Repository<T>
    permits JdbcRepository, MongoRepository {}
```

## Benefits

- **Compiler Verification**: Exhaustive switch statements
- **Intentional Design**: Makes class hierarchies explicit
- **Refactoring Safety**: Adding implementations forces updates
- **Performance**: Enables optimizations (narrower type sets)
- **Documentation**: Code clearly shows permitted extensions

## Read the Full Article

Discover much more in **[Part 3: Sealed Classes on blog.9mac.dev]([BLOG_LINK_HERE])**:
- Design philosophy behind sealed classes
- Multi-level sealed hierarchies with practical patterns
- The `non-sealed` modifier in real-world scenarios
- Sealed interfaces with complex implementations
- 5+ comprehensive examples: payments, shapes, vehicles, JSON
- Reflection API for runtime introspection
- Best practices and common pitfalls

## GitHub Repository

All code examples are ready to clone and run:

```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 4 - Pattern Matching & Switch Expressions
