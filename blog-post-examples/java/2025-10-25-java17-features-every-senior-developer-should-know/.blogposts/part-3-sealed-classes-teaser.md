# Java 17 Features Every Senior Developer Should Know - Part 3: Sealed Classes

This is **Part 3** of a comprehensive series on essential Java 17+ features that every senior developer must understand when working with modern Java. Whether you're migrating from Java 8, 11, or 13, or you're mentoring junior developers, this series covers the language features that will define your production code quality and team productivity for the next decade.

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

Imagine a payment processing system. You design an abstract `Payment` class for credit cards, debit cards, and cash. A year later, another team adds `CryptoPayment` without telling you. Now your switch statement that was supposed to handle all payment types doesn't—and you have a bug in production that the compiler never caught.

The core problem: **Java forces a false choice between `final` (too restrictive) and open (too permissive).** You can't say "extend this class, but ONLY in these specific ways."

Languages like Scala (sealed classes, 2006) and TypeScript (discriminated unions) showed how powerful controlled hierarchies are. They catch bugs at compile-time instead of runtime. They enable exhaustive pattern matching—the compiler verifies you've handled all cases.

Sealed classes (Java 17) bring this to Java: **fine-grained control over which classes can extend yours, with compiler-enforced completeness checking.**

---

## What are Sealed Classes?

A **sealed class** restricts which other classes can extend it. You explicitly list the permitted subclasses using the `permits` clause, giving you fine-grained control over class hierarchies.

## The Problem It Solves

Before sealed classes, you had only two choices:

1. **Make it `final`** - Nobody can extend it at all
2. **Leave it open** - Anybody, anywhere can extend it

Neither choice is ideal:

```java
// Problem 1: Too restrictive
public final class Payment { }
// Can't create CreditCard, DebitCard subclasses

// Problem 2: Too open
public abstract class Payment { }
// Anyone can add CryptoPayment, and you can't verify all cases in switch statements
```

## Sealed Classes Solution

```java
// Explicitly control who can extend Payment
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

## Three Subclass Modifiers

Every direct subclass of a sealed class must use one of three modifiers:

```java
public sealed abstract class Shape
    permits Circle, Rectangle, FreeformShape {
    public abstract double area();
}

// Option 1: final - no more extensions
public final class Circle extends Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}

// Option 2: sealed - further controlled extensions
public sealed class Rectangle extends Shape
    permits SizedRectangle {
    @Override
    public double area() { return width * height; }
}

// Option 3: non-sealed - breaks the seal (open for extension)
public non-sealed class FreeformShape extends Shape {
    @Override
    public double area() { /* custom logic */ }
}

// Now anyone can extend FreeformShape
public class CustomShape extends FreeformShape { }
```

## Simple Example

```java
public sealed interface Transport
    permits Car, Bicycle {
    void move();
}

public record Car(String model) implements Transport {
    @Override
    public void move() { System.out.println("Driving " + model); }
}

public record Bicycle(String type) implements Transport {
    @Override
    public void move() { System.out.println("Pedaling " + type); }
}

public class SealedExample {
    public static void main(String[] args) {
        var car = new Car("Tesla");
        var bike = new Bicycle("Mountain");

        car.move();   // Driving Tesla
        bike.move();  // Pedaling Mountain
    }
}
```

**Output:**
```
Driving Tesla
Pedaling Mountain
```

## Exhaustive Pattern Matching

Sealed classes enable exhaustive switch statements—the compiler verifies all cases are handled:

```java
public sealed interface Vehicle permits Car, Motorcycle {}

// No default case needed - compiler knows all types!
public static String describe(Vehicle v) {
    return switch (v) {
        case Car car -> "4-wheel vehicle";
        case Motorcycle moto -> "2-wheel vehicle";
        // If you forget a case, compilation fails!
    };
}
```

Add a new `Bicycle` implements Vehicle? The compiler forces you to update all switch statements.

## Reflection API

```java
Class<?> shapeClass = Shape.class;

boolean isSealed = shapeClass.isSealed();  // true
Class<?>[] permitted = shapeClass.getPermittedSubclasses();
// Returns: [Circle.class, Rectangle.class, ...]
```

## Key Characteristics

- **Compile-time verification** - Unauthorized extensions cause compiler errors
- **Exhaustive switches** - No need for `default` when all cases are sealed
- **Same package requirement** - Sealed class and subclasses in same package
- **Multi-level hierarchies** - Intermediate levels can be sealed too
- **With records** - Records are implicitly final, perfect for sealed implementations

## When to Use Sealed Classes

✅ **Great for:**
- Domain models with fixed variants (payments, shapes, states)
- APIs where you control all implementations
- Type hierarchies needing exhaustive checking
- Algebraic data types and pattern matching

❌ **Avoid for:**
- Plugin architectures (need third-party extensions)
- Frequently changing hierarchies
- Library APIs with strict backward compatibility needs

## Key Insight

Sealed classes give you the middle ground between `final` (no extensions) and open (unlimited extensions). They enable the compiler to verify completeness, preventing bugs and making refactoring safer. Combined with records and pattern matching, they bring functional programming concepts to Java.

---

## Want to Learn More?

This is just a preview of what sealed classes can do. **[Read the full Part 3 article](part-3-sealed-classes.md)** where you'll discover:

- Design philosophy behind sealed classes
- Multi-level sealed hierarchies with practical patterns
- The `non-sealed` modifier in real-world scenarios
- Sealed interfaces with complex implementations
- Advanced pattern matching with sealed types
- 5+ detailed examples: shapes, payments, JSON values, vehicles
- Reflection API for runtime introspection
- Integration with pattern matching and switch expressions
- Best practices and common pitfalls
- Migration strategies from open hierarchies

The full article is 1900+ lines with comprehensive examples, unit tests, and production-ready patterns.

**All code examples are runnable in the GitHub repository:**
```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```
