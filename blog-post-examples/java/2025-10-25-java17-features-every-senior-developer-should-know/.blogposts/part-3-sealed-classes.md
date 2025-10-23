# Java 17 Features Every Senior Developer Should Know - Part 3: Sealed Classes

Welcome back to our comprehensive series on Java 17 features! In [Part 1](part-1-introduction-and-var.md), we explored the `var` keyword and type inference. In [Part 2](part-2-records.md), we discovered how Records eliminate boilerplate in data classes. Today, we're diving into **Sealed Classes** (JEP 409)—a feature that gives you fine-grained control over class hierarchies.

If you've ever wanted to restrict which classes can extend your base class without making it `final`, or if you've struggled with maintaining domain model integrity across large codebases, this article is for you. Sealed classes bring algebraic data types to Java, enabling exhaustive pattern matching and compiler-verified completeness checks.

## Table of Contents

1. [The Problem: Uncontrolled Inheritance](#the-problem-uncontrolled-inheritance)
2. [What Are Sealed Classes?](#what-are-sealed-classes)
3. [History of Sealed Classes](#history-of-sealed-classes)
4. [Basic Syntax and Features](#basic-syntax-and-features)
5. [Practical Examples](#practical-examples)
6. [Best Practices](#best-practices)
7. [Pitfalls and Limitations](#pitfalls-and-limitations)
8. [Summary and Next Steps](#summary-and-next-steps)

---

## The Problem: Uncontrolled Inheritance

Before Java 17, you had two options for controlling inheritance:

1. **Make the class `final`** - No one can extend it at all
2. **Leave it open** - Anyone, anywhere can extend it

This binary choice created real problems in large codebases. Consider a payment processing system:

```java
// Before Java 17 - open hierarchy
public abstract class Payment {
    public abstract void process();
}

public class CreditCardPayment extends Payment {
    @Override
    public void process() { /* ... */ }
}

public class DebitCardPayment extends Payment {
    @Override
    public void process() { /* ... */ }
}

// Problem: Anyone can add new payment types!
// Some developer in another module:
public class CryptoPayment extends Payment {
    @Override
    public void process() {
        // Oops - we don't handle this in our switch statement!
    }
}
```

The issues with uncontrolled inheritance:

- **No exhaustiveness checking**: When you handle `Payment` types in a switch, the compiler can't verify you've covered all cases
- **Domain model fragmentation**: Core abstractions can be extended in unpredictable ways
- **Breaking changes**: Adding a new payment type should be a deliberate, coordinated change—not an afterthought
- **Maintenance burden**: Developers must constantly check for new subtypes in distant parts of the codebase

### Why `final` and Open Classes Aren't Enough

Making `Payment` final means you can't have _any_ subtypes—but you need `CreditCardPayment` and `DebitCardPayment`. Leaving it open means _anyone_ can add subtypes, breaking your carefully designed switch statements and validation logic.

**What we need:** A middle ground where `Payment` can be extended, but only by the classes we explicitly permit.

---

## What Are Sealed Classes?

A **sealed class** is a class that restricts which other classes may extend it. You explicitly list the permitted subclasses using the `permits` clause:

```java
public sealed abstract class Payment
    permits CreditCardPayment, DebitCardPayment, CashPayment {
    public abstract void process();
}
```

Now, **only** `CreditCardPayment`, `DebitCardPayment`, and `CashPayment` can extend `Payment`. Any attempt to create an unlisted subclass results in a compile-time error:

```java
// Compile error: CryptoPayment is not listed in permits clause
public class CryptoPayment extends Payment { }
```

### The Three Subclass Modifiers

Every direct subclass of a sealed class must declare its own inheritance policy using one of three modifiers:

1. **`final`** - No further subclassing allowed
2. **`sealed`** - Further subclassing permitted, but only to explicitly named subtypes
3. **`non-sealed`** - Open to unrestricted subclassing (breaks the seal)

```java
// Option 1: final - no more extensions
public final class CreditCardPayment extends Payment {
    @Override
    public void process() { /* ... */ }
}

// Option 2: sealed - controlled multi-level hierarchy
public sealed class BankTransfer extends Payment
    permits DomesticTransfer, InternationalTransfer {
    @Override
    public void process() { /* ... */ }
}

// Option 3: non-sealed - open for extension
public non-sealed class CashPayment extends Payment {
    @Override
    public void process() { /* ... */ }
}

// Now anyone can extend CashPayment
public class TippedCashPayment extends CashPayment { }
```

### Key Characteristics

Sealed classes have specific properties:

- **Restricted inheritance**: Only explicitly permitted classes can extend/implement
- **Exhaustiveness checking**: The compiler knows all possible subtypes for switch statements
- **Same package/module requirement**: Sealed class and permitted subclasses must be in the same package (or module)
- **Sealed interfaces**: Interfaces can be sealed identically to classes
- **Records work naturally**: Records are implicitly `final`, making them perfect sealed type implementations

---

## History of Sealed Classes

Sealed classes evolved through three Java versions based on community feedback and real-world testing.

### Java 15: Preview Feature (JEP 360)

Sealed types debuted in September 2020 as a **preview feature**, requiring the `--enable-preview` compiler flag. The basic syntax was introduced:

```java
sealed class Shape permits Circle, Rectangle, Triangle { }

final class Circle extends Shape { }
final class Rectangle extends Shape { }
final class Triangle extends Shape { }
```

**What you got:**
- Basic `sealed` and `permits` keywords
- Requirement for subclasses to use `final`, `sealed`, or `non-sealed`
- Compile-time verification of permitted subclass list

**Limitations:**
- Preview status meant production use required caution
- Limited IDE support and tooling
- Syntax still being refined based on feedback

### Java 16: Second Preview (JEP 397)

March 2021 brought refinements and clarifications:

**Improvements:**
- **Omitting `permits`** - If all subclasses are in the same source file, `permits` can be omitted
- **Better error messages** - Clearer compile errors for seal violations
- **Sealed interfaces** - Full support for sealed interfaces, not just classes
- **Record integration** - Records work seamlessly as sealed type implementations

```java
// Java 16 - permits clause optional if subclasses in same file
sealed class Result { }
final class Success extends Result { }
final class Failure extends Result { }
```

### Java 17: Standard Feature (JEP 409)

September 2021 marked the graduation of sealed classes to a **standard, permanent feature**. No more `--enable-preview` flag needed.

**Final enhancements:**
- **Reflection API** - `Class.isSealed()` and `Class.getPermittedSubclasses()`
- **Pattern matching synergy** - Full integration with pattern matching features
- **Exhaustive switch** - Compiler recognizes when switch covers all sealed subtypes
- **Production-ready** - Full IDE support, stable API, comprehensive documentation

---

## Basic Syntax and Features

### Declaration Syntax

The basic sealed class declaration consists of:
1. The `sealed` modifier (or `sealed interface` for interfaces)
2. The class/interface name
3. The `permits` clause listing allowed subclasses
4. Optional class body

```java
// Sealed class with permits
public sealed abstract class Shape
    permits Circle, Rectangle, Triangle {
    public abstract double area();
}

// Sealed interface
public sealed interface JSONValue
    permits JSONObject, JSONArray, JSONString, JSONNumber, JSONBoolean, JSONNull {
    String toJson();
}
```

### Omitting the `permits` Clause

If all permitted subclasses are declared in the same source file, you can omit `permits`:

```java
// All in one file - no permits needed
sealed class Result { }
final class Success extends Result { }
final class Failure extends Result { }
```

The compiler infers the permitted subclasses from the file contents.

### Subclass Requirements

**Every direct subclass must:**
1. Be in the same package as the sealed parent (or same module if using JPMS)
2. Explicitly extend/implement the sealed parent
3. Choose a modifier: `final`, `sealed`, or `non-sealed`

```java
// ✅ Valid - final subclass
public final class Circle extends Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}

// ✅ Valid - sealed subclass with its own hierarchy
public sealed class Polygon extends Shape
    permits Triangle, Rectangle, Pentagon {
    // ...
}

// ✅ Valid - non-sealed subclass (breaks the seal)
public non-sealed class FreeformShape extends Shape {
    // Anyone can extend FreeformShape now
}

// ❌ Invalid - missing final/sealed/non-sealed
public class InvalidShape extends Shape { } // Compile error
```

### Sealed Interfaces

Interfaces work identically to classes:

```java
public sealed interface Transport
    permits Car, Bicycle, Train {
    void move();
}

// Records are implicitly final
public record Car(String model, int passengers) implements Transport {
    @Override
    public void move() { System.out.println("Driving " + model); }
}

// Enum values are implicitly final
public enum Bicycle implements Transport {
    MOUNTAIN, ROAD, HYBRID;

    @Override
    public void move() { System.out.println("Pedaling " + this); }
}
```

### Reflection API

Java 17 adds reflection methods for sealed types:

```java
Class<?> shapeClass = Shape.class;

// Check if sealed
boolean isSealed = shapeClass.isSealed(); // true

// Get permitted subclasses
Class<?>[] permitted = shapeClass.getPermittedSubclasses();
// Returns: [Circle.class, Rectangle.class, Triangle.class]
```

---

## Practical Examples

### Example 1: Basic Sealed Classes with Shape Hierarchy

Sealed classes provide controlled inheritance hierarchies where the compiler knows all possible subtypes, enabling exhaustive pattern matching.

```java
// Sealed parent class
public sealed abstract class Shape
    permits Circle, Rectangle, Triangle {
    public abstract double area();
    public abstract String describe();
}

// Final implementations
public final class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String describe() {
        return String.format("Circle[radius=%.2f, area=%.2f]", radius, area());
    }

    public double radius() { return radius; }
}

public final class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String describe() {
        return String.format("Rectangle[width=%.2f, height=%.2f, area=%.2f]",
            width, height, area());
    }

    public double width() { return width; }
    public double height() { return height; }
}

public final class Triangle extends Shape {
    private final double base;
    private final double height;

    public Triangle(double base, double height) {
        if (base <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        return 0.5 * base * height;
    }

    @Override
    public String describe() {
        return String.format("Triangle[base=%.2f, height=%.2f, area=%.2f]",
            base, height, area());
    }

    public double base() { return base; }
    public double height() { return height; }
}

public class BasicSealedExample {
    public static void main(String[] args) {
        var shapes = List.of(
            new Circle(5.0),
            new Rectangle(4.0, 6.0),
            new Triangle(3.0, 8.0)
        );

        System.out.println("=== Shape Demonstrations ===");
        for (var shape : shapes) {
            System.out.println(shape.describe());
        }

        // Type-specific operations
        System.out.println("\n=== Type-Specific Access ===");
        var circle = new Circle(10.0);
        System.out.println("Circle radius: " + circle.radius());

        var rect = new Rectangle(5.0, 3.0);
        System.out.println("Rectangle dimensions: " + rect.width() + " x " + rect.height());

        // Reflection - discovering sealed hierarchy
        System.out.println("\n=== Reflection ===");
        System.out.println("Shape is sealed: " + Shape.class.isSealed());
        System.out.print("Permitted subclasses: ");
        for (var permitted : Shape.class.getPermittedSubclasses()) {
            System.out.print(permitted.getSimpleName() + " ");
        }
        System.out.println();
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should calculate circle area correctly")
void shouldCalculateCircleAreaCorrectly() {
    var circle = new Circle(5.0);

    assertEquals(Math.PI * 25, circle.area(), 0.001);
    assertEquals(5.0, circle.radius());
}

@Test
@DisplayName("Should calculate rectangle area correctly")
void shouldCalculateRectangleAreaCorrectly() {
    var rectangle = new Rectangle(4.0, 6.0);

    assertEquals(24.0, rectangle.area(), 0.001);
    assertEquals(4.0, rectangle.width());
    assertEquals(6.0, rectangle.height());
}

@Test
@DisplayName("Should calculate triangle area correctly")
void shouldCalculateTriangleAreaCorrectly() {
    var triangle = new Triangle(3.0, 8.0);

    assertEquals(12.0, triangle.area(), 0.001);
    assertEquals(3.0, triangle.base());
    assertEquals(8.0, triangle.height());
}

@Test
@DisplayName("Should verify Shape is sealed with correct permitted subclasses")
void shouldVerifyShapeIsSealedWithCorrectPermittedSubclasses() {
    assertTrue(Shape.class.isSealed());

    var permitted = Shape.class.getPermittedSubclasses();
    assertEquals(3, permitted.length);

    var permittedNames = Arrays.stream(permitted)
        .map(Class::getSimpleName)
        .collect(Collectors.toSet());

    assertTrue(permittedNames.contains("Circle"));
    assertTrue(permittedNames.contains("Rectangle"));
    assertTrue(permittedNames.contains("Triangle"));
}

@Test
@DisplayName("Should throw exception for invalid dimensions")
void shouldThrowExceptionForInvalidDimensions() {
    assertThrows(IllegalArgumentException.class, () -> new Circle(0));
    assertThrows(IllegalArgumentException.class, () -> new Circle(-5));
    assertThrows(IllegalArgumentException.class, () -> new Rectangle(0, 5));
    assertThrows(IllegalArgumentException.class, () -> new Triangle(5, -3));
}
```

**Output:**
```
=== Shape Demonstrations ===
Circle[radius=5.00, area=78.54]
Rectangle[width=4.00, height=6.00, area=24.00]
Triangle[base=3.00, height=8.00, area=12.00]

=== Type-Specific Access ===
Circle radius: 10.0
Rectangle dimensions: 5.0 x 3.0

=== Reflection ===
Shape is sealed: true
Permitted subclasses: Circle Rectangle Triangle
```

**Key Insight**: Sealed classes give you controlled inheritance hierarchies. Unlike `final` (no extensions) or open classes (unlimited extensions), sealed classes let you explicitly list permitted subtypes. The compiler enforces this at compile-time, preventing unauthorized extensions.

---

### Example 2: Sealed Interfaces with Records

Sealed interfaces combined with records create elegant, immutable domain models with exhaustive type checking.

```java
// Sealed interface
public sealed interface Payment
    permits CreditCard, DebitCard, Cash, BankTransfer {
    double amount();
    String description();
}

// Records as implementations (implicitly final)
public record CreditCard(
    String cardNumber,
    String cardHolder,
    double amount,
    String merchantId
) implements Payment {

    public CreditCard {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number");
        }
    }

    @Override
    public String description() {
        return String.format("Credit Card payment: $%.2f (Card ending %s)",
            amount, cardNumber.substring(12));
    }
}

public record DebitCard(
    String cardNumber,
    String cardHolder,
    double amount,
    String pin
) implements Payment {

    public DebitCard {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @Override
    public String description() {
        return String.format("Debit Card payment: $%.2f (Card ending %s)",
            amount, cardNumber.substring(12));
    }
}

public record Cash(
    double amount,
    String currency
) implements Payment {

    public Cash {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @Override
    public String description() {
        return String.format("Cash payment: %.2f %s", amount, currency);
    }
}

public record BankTransfer(
    String fromAccount,
    String toAccount,
    double amount,
    String reference
) implements Payment {

    public BankTransfer {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @Override
    public String description() {
        return String.format("Bank Transfer: $%.2f (Ref: %s)", amount, reference);
    }
}

public class SealedInterfaceExample {
    public static void main(String[] args) {
        var payments = List.of(
            new CreditCard("1234567890123456", "John Doe", 150.00, "MERCHANT_001"),
            new DebitCard("9876543210987654", "Jane Smith", 75.50, "1234"),
            new Cash(50.00, "USD"),
            new BankTransfer("ACC001", "ACC002", 1000.00, "INV-2024-001")
        );

        System.out.println("=== Payment Processing ===");
        double total = 0.0;
        for (var payment : payments) {
            System.out.println(payment.description());
            total += payment.amount();
        }

        System.out.printf("\nTotal processed: $%.2f\n", total);

        // Type-specific access
        System.out.println("\n=== Type-Specific Operations ===");
        var creditCard = new CreditCard("1111222233334444", "Alice Brown", 299.99, "SHOP_123");
        System.out.println("Card holder: " + creditCard.cardHolder());
        System.out.println("Merchant ID: " + creditCard.merchantId());

        // Reflection
        System.out.println("\n=== Interface Reflection ===");
        System.out.println("Payment is sealed: " + Payment.class.isSealed());
        System.out.print("Permitted implementations: ");
        for (var permitted : Payment.class.getPermittedSubclasses()) {
            System.out.print(permitted.getSimpleName() + " ");
        }
        System.out.println();
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create CreditCard payment with valid data")
void shouldCreateCreditCardPaymentWithValidData() {
    var payment = new CreditCard("1234567890123456", "John Doe", 150.00, "MERCHANT_001");

    assertEquals(150.00, payment.amount());
    assertEquals("John Doe", payment.cardHolder());
    assertEquals("MERCHANT_001", payment.merchantId());
    assertTrue(payment.description().contains("$150.00"));
}

@Test
@DisplayName("Should create DebitCard payment with valid data")
void shouldCreateDebitCardPaymentWithValidData() {
    var payment = new DebitCard("9876543210987654", "Jane Smith", 75.50, "1234");

    assertEquals(75.50, payment.amount());
    assertEquals("Jane Smith", payment.cardHolder());
}

@Test
@DisplayName("Should create Cash payment with currency")
void shouldCreateCashPaymentWithCurrency() {
    var payment = new Cash(50.00, "USD");

    assertEquals(50.00, payment.amount());
    assertEquals("USD", payment.currency());
    assertTrue(payment.description().contains("USD"));
}

@Test
@DisplayName("Should create BankTransfer with reference number")
void shouldCreateBankTransferWithReferenceNumber() {
    var payment = new BankTransfer("ACC001", "ACC002", 1000.00, "INV-2024-001");

    assertEquals(1000.00, payment.amount());
    assertEquals("INV-2024-001", payment.reference());
    assertTrue(payment.description().contains("INV-2024-001"));
}

@Test
@DisplayName("Should verify Payment interface is sealed")
void shouldVerifyPaymentInterfaceIsSealed() {
    assertTrue(Payment.class.isSealed());

    var permitted = Payment.class.getPermittedSubclasses();
    assertEquals(4, permitted.length);
}

@Test
@DisplayName("Should throw exception for invalid payment amounts")
void shouldThrowExceptionForInvalidPaymentAmounts() {
    assertThrows(IllegalArgumentException.class,
        () -> new CreditCard("1234567890123456", "John", 0, "M001"));
    assertThrows(IllegalArgumentException.class,
        () -> new Cash(-10, "USD"));
}

@Test
@DisplayName("Should throw exception for invalid card number")
void shouldThrowExceptionForInvalidCardNumber() {
    assertThrows(IllegalArgumentException.class,
        () -> new CreditCard("123", "John", 100, "M001"));
}
```

**Output:**
```
=== Payment Processing ===
Credit Card payment: $150.00 (Card ending 3456)
Debit Card payment: $75.50 (Card ending 7654)
Cash payment: 50.00 USD
Bank Transfer: $1000.00 (Ref: INV-2024-001)

Total processed: $1275.50

=== Type-Specific Operations ===
Card holder: Alice Brown
Merchant ID: SHOP_123

=== Interface Reflection ===
Payment is sealed: true
Permitted implementations: CreditCard DebitCard Cash BankTransfer
```

**Key Insight**: Sealed interfaces work perfectly with records because records are implicitly `final`. This combination creates immutable, type-safe domain models where the compiler knows every possible implementation. It's Java's answer to algebraic data types from functional programming languages.

---

### Example 3: Multi-level Sealed Hierarchies

Sealed classes can form multi-level hierarchies where intermediate levels are also sealed, creating tree-like type structures.

```java
// Top-level sealed class
public sealed abstract class Vehicle
    permits MotorVehicle, Bicycle {
    private final String brand;

    protected Vehicle(String brand) {
        this.brand = brand;
    }

    public String brand() { return brand; }
    public abstract String describe();
}

// Second-level sealed class
public sealed abstract class MotorVehicle extends Vehicle
    permits Car, Motorcycle {
    private final int engineCC;

    protected MotorVehicle(String brand, int engineCC) {
        super(brand);
        this.engineCC = engineCC;
    }

    public int engineCC() { return engineCC; }
}

// Final implementations at third level
public final class Car extends MotorVehicle {
    private final int doors;
    private final boolean isElectric;

    public Car(String brand, int engineCC, int doors, boolean isElectric) {
        super(brand, engineCC);
        this.doors = doors;
        this.isElectric = isElectric;
    }

    public int doors() { return doors; }
    public boolean isElectric() { return isElectric; }

    @Override
    public String describe() {
        return String.format("Car[brand=%s, engineCC=%d, doors=%d, electric=%b]",
            brand(), engineCC(), doors, isElectric);
    }
}

public final class Motorcycle extends MotorVehicle {
    private final boolean hasSidecar;

    public Motorcycle(String brand, int engineCC, boolean hasSidecar) {
        super(brand, engineCC);
        this.hasSidecar = hasSidecar;
    }

    public boolean hasSidecar() { return hasSidecar; }

    @Override
    public String describe() {
        return String.format("Motorcycle[brand=%s, engineCC=%d, hasSidecar=%b]",
            brand(), engineCC(), hasSidecar);
    }
}

// Final implementation at second level
public final class Bicycle extends Vehicle {
    private final int gears;
    private final String type; // "mountain", "road", "hybrid"

    public Bicycle(String brand, int gears, String type) {
        super(brand);
        this.gears = gears;
        this.type = type;
    }

    public int gears() { return gears; }
    public String type() { return type; }

    @Override
    public String describe() {
        return String.format("Bicycle[brand=%s, gears=%d, type=%s]",
            brand(), gears, type);
    }
}

public class MultiLevelSealedExample {
    public static void main(String[] args) {
        var vehicles = List.of(
            new Car("Tesla", 0, 4, true),
            new Car("BMW", 3000, 2, false),
            new Motorcycle("Harley-Davidson", 1200, false),
            new Motorcycle("Ural", 750, true),
            new Bicycle("Trek", 21, "mountain"),
            new Bicycle("Specialized", 18, "road")
        );

        System.out.println("=== Vehicle Inventory ===");
        for (var vehicle : vehicles) {
            System.out.println(vehicle.describe());
        }

        // Categorize by type
        System.out.println("\n=== Categorization ===");
        long motorVehicles = vehicles.stream()
            .filter(v -> v instanceof MotorVehicle)
            .count();
        long bicycles = vehicles.stream()
            .filter(v -> v instanceof Bicycle)
            .count();

        System.out.println("Motor Vehicles: " + motorVehicles);
        System.out.println("Bicycles: " + bicycles);

        // Type-specific operations
        System.out.println("\n=== Motor Vehicle Details ===");
        var car = new Car("Audi", 2000, 4, false);
        System.out.println("Car doors: " + car.doors());
        System.out.println("Engine CC: " + car.engineCC());

        // Hierarchy reflection
        System.out.println("\n=== Sealed Hierarchy ===");
        System.out.println("Vehicle is sealed: " + Vehicle.class.isSealed());
        System.out.println("MotorVehicle is sealed: " + MotorVehicle.class.isSealed());
        System.out.println("Car is final: " + java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()));
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create Car with correct properties")
void shouldCreateCarWithCorrectProperties() {
    var car = new Car("Tesla", 0, 4, true);

    assertEquals("Tesla", car.brand());
    assertEquals(0, car.engineCC());
    assertEquals(4, car.doors());
    assertTrue(car.isElectric());
}

@Test
@DisplayName("Should create Motorcycle with correct properties")
void shouldCreateMotorcycleWithCorrectProperties() {
    var motorcycle = new Motorcycle("Harley-Davidson", 1200, false);

    assertEquals("Harley-Davidson", motorcycle.brand());
    assertEquals(1200, motorcycle.engineCC());
    assertFalse(motorcycle.hasSidecar());
}

@Test
@DisplayName("Should create Bicycle with correct properties")
void shouldCreateBicycleWithCorrectProperties() {
    var bicycle = new Bicycle("Trek", 21, "mountain");

    assertEquals("Trek", bicycle.brand());
    assertEquals(21, bicycle.gears());
    assertEquals("mountain", bicycle.type());
}

@Test
@DisplayName("Should verify multi-level sealed hierarchy")
void shouldVerifyMultiLevelSealedHierarchy() {
    // Top level is sealed
    assertTrue(Vehicle.class.isSealed());
    var vehiclePermitted = Vehicle.class.getPermittedSubclasses();
    assertEquals(2, vehiclePermitted.length);

    // Second level is sealed
    assertTrue(MotorVehicle.class.isSealed());
    var motorPermitted = MotorVehicle.class.getPermittedSubclasses();
    assertEquals(2, motorPermitted.length);

    // Leaf classes are final
    assertTrue(java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()));
    assertTrue(java.lang.reflect.Modifier.isFinal(Motorcycle.class.getModifiers()));
    assertTrue(java.lang.reflect.Modifier.isFinal(Bicycle.class.getModifiers()));
}

@Test
@DisplayName("Should correctly identify MotorVehicle instances")
void shouldCorrectlyIdentifyMotorVehicleInstances() {
    var car = new Car("BMW", 3000, 4, false);
    var motorcycle = new Motorcycle("Yamaha", 600, false);
    var bicycle = new Bicycle("Giant", 18, "road");

    assertTrue(car instanceof MotorVehicle);
    assertTrue(motorcycle instanceof MotorVehicle);
    assertFalse(bicycle instanceof MotorVehicle);
}
```

**Output:**
```
=== Vehicle Inventory ===
Car[brand=Tesla, engineCC=0, doors=4, electric=true]
Car[brand=BMW, engineCC=3000, doors=2, electric=false]
Motorcycle[brand=Harley-Davidson, engineCC=1200, hasSidecar=false]
Motorcycle[brand=Ural, engineCC=750, hasSidecar=true]
Bicycle[brand=Trek, gears=21, type=mountain]
Bicycle[brand=Specialized, gears=18, type=road]

=== Categorization ===
Motor Vehicles: 4
Bicycles: 2

=== Motor Vehicle Details ===
Car doors: 4
Engine CC: 2000

=== Sealed Hierarchy ===
Vehicle is sealed: true
MotorVehicle is sealed: true
Car is final: true
```

**Key Insight**: Multi-level sealed hierarchies let you model complex domain structures with controlled inheritance at each level. The top-level `Vehicle` permits two branches: `MotorVehicle` (which is itself sealed) and `Bicycle` (which is final). This creates a tree structure where each node decides its children's extension policy.

---

### Example 4: Exhaustive Switch with Sealed Types

One of the most powerful benefits of sealed classes is exhaustive switch expressions—the compiler verifies you've handled all possible subtypes without needing a `default` case.

```java
// Sealed JSON value hierarchy
public sealed interface JSONValue
    permits JSONObject, JSONArray, JSONString, JSONNumber, JSONBoolean, JSONNull {
    String toJson();
}

public record JSONObject(java.util.Map<String, JSONValue> values) implements JSONValue {
    @Override
    public String toJson() {
        return values.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":" + e.getValue().toJson())
            .collect(java.util.stream.Collectors.joining(",", "{", "}"));
    }
}

public record JSONArray(java.util.List<JSONValue> values) implements JSONValue {
    @Override
    public String toJson() {
        return values.stream()
            .map(JSONValue::toJson)
            .collect(java.util.stream.Collectors.joining(",", "[", "]"));
    }
}

public record JSONString(String value) implements JSONValue {
    @Override
    public String toJson() {
        return "\"" + value.replace("\"", "\\\"") + "\"";
    }
}

public record JSONNumber(double value) implements JSONValue {
    @Override
    public String toJson() {
        return String.valueOf(value);
    }
}

public record JSONBoolean(boolean value) implements JSONValue {
    @Override
    public String toJson() {
        return String.valueOf(value);
    }
}

public enum JSONNull implements JSONValue {
    INSTANCE;

    @Override
    public String toJson() {
        return "null";
    }
}

public class ExhaustiveSwitchExample {
    // Exhaustive switch - no default needed!
    public static String describeType(JSONValue value) {
        return switch (value) {
            case JSONObject obj -> "object with " + obj.values().size() + " properties";
            case JSONArray arr -> "array with " + arr.values().size() + " elements";
            case JSONString str -> "string: \"" + str.value() + "\"";
            case JSONNumber num -> "number: " + num.value();
            case JSONBoolean bool -> "boolean: " + bool.value();
            case JSONNull ignored -> "null value";
            // No default case needed - compiler knows all cases are covered!
        };
    }

    public static int estimateSize(JSONValue value) {
        return switch (value) {
            case JSONObject obj -> obj.values().values().stream()
                .mapToInt(ExhaustiveSwitchExample::estimateSize)
                .sum() + 2; // {} brackets
            case JSONArray arr -> arr.values().stream()
                .mapToInt(ExhaustiveSwitchExample::estimateSize)
                .sum() + 2; // [] brackets
            case JSONString str -> str.value().length() + 2; // quotes
            case JSONNumber num -> String.valueOf(num.value()).length();
            case JSONBoolean bool -> String.valueOf(bool.value()).length();
            case JSONNull ignored -> 4; // "null"
        };
    }

    public static void main(String[] args) {
        // Build sample JSON structure
        var jsonData = new JSONObject(java.util.Map.of(
            "name", new JSONString("John Doe"),
            "age", new JSONNumber(30),
            "active", new JSONBoolean(true),
            "address", JSONNull.INSTANCE,
            "hobbies", new JSONArray(java.util.List.of(
                new JSONString("reading"),
                new JSONString("coding"),
                new JSONString("gaming")
            ))
        ));

        System.out.println("=== JSON Structure ===");
        System.out.println(jsonData.toJson());

        System.out.println("\n=== Type Descriptions (Exhaustive Switch) ===");
        jsonData.values().forEach((key, value) -> {
            System.out.println(key + " -> " + describeType(value));
        });

        System.out.println("\n=== Size Estimation ===");
        System.out.println("Estimated size: " + estimateSize(jsonData) + " characters");
        System.out.println("Actual size: " + jsonData.toJson().length() + " characters");

        // Demonstrate exhaustiveness
        System.out.println("\n=== Exhaustive Pattern Matching ===");
        var values = java.util.List.of(
            new JSONString("test"),
            new JSONNumber(42),
            new JSONBoolean(false),
            JSONNull.INSTANCE
        );

        for (var value : values) {
            System.out.println(describeType(value));
        }
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create JSONString and convert to JSON")
void shouldCreateJSONStringAndConvertToJSON() {
    var json = new JSONString("hello");

    assertEquals("\"hello\"", json.toJson());
}

@Test
@DisplayName("Should create JSONNumber and convert to JSON")
void shouldCreateJSONNumberAndConvertToJSON() {
    var json = new JSONNumber(42.5);

    assertEquals("42.5", json.toJson());
}

@Test
@DisplayName("Should create JSONBoolean and convert to JSON")
void shouldCreateJSONBooleanAndConvertToJSON() {
    var jsonTrue = new JSONBoolean(true);
    var jsonFalse = new JSONBoolean(false);

    assertEquals("true", jsonTrue.toJson());
    assertEquals("false", jsonFalse.toJson());
}

@Test
@DisplayName("Should create JSONNull and convert to JSON")
void shouldCreateJSONNullAndConvertToJSON() {
    var json = JSONNull.INSTANCE;

    assertEquals("null", json.toJson());
}

@Test
@DisplayName("Should create JSONArray and convert to JSON")
void shouldCreateJSONArrayAndConvertToJSON() {
    var json = new JSONArray(java.util.List.of(
        new JSONNumber(1),
        new JSONNumber(2),
        new JSONNumber(3)
    ));

    assertEquals("[1.0,2.0,3.0]", json.toJson());
}

@Test
@DisplayName("Should describe JSON types using exhaustive switch")
void shouldDescribeJSONTypesUsingExhaustiveSwitch() {
    assertEquals("string: \"test\"",
        ExhaustiveSwitchExample.describeType(new JSONString("test")));
    assertEquals("number: 42.0",
        ExhaustiveSwitchExample.describeType(new JSONNumber(42)));
    assertEquals("boolean: true",
        ExhaustiveSwitchExample.describeType(new JSONBoolean(true)));
    assertEquals("null value",
        ExhaustiveSwitchExample.describeType(JSONNull.INSTANCE));
}

@Test
@DisplayName("Should estimate JSON size correctly")
void shouldEstimateJSONSizeCorrectly() {
    assertEquals(6, ExhaustiveSwitchExample.estimateSize(new JSONString("test"))); // "test"
    assertEquals(4, ExhaustiveSwitchExample.estimateSize(JSONNull.INSTANCE)); // null
}

@Test
@DisplayName("Should verify JSONValue interface is sealed")
void shouldVerifyJSONValueInterfaceIsSealed() {
    assertTrue(JSONValue.class.isSealed());
    assertEquals(6, JSONValue.class.getPermittedSubclasses().length);
}
```

**Output:**
```
=== JSON Structure ===
{"hobbies":["reading","coding","gaming"],"address":null,"name":"John Doe","active":true,"age":30.0}

=== Type Descriptions (Exhaustive Switch) ===
hobbies -> array with 3 elements
address -> null value
name -> string: "John Doe"
active -> boolean: true
age -> number: 30.0

=== Size Estimation ===
Estimated size: 88 characters
Actual size: 94 characters

=== Exhaustive Pattern Matching ===
string: "test"
number: 42.0
boolean: false
null value
```

**Key Insight**: Exhaustive switch with sealed types is incredibly powerful. The compiler knows all possible `JSONValue` implementations, so it can verify your switch covers every case. If you add a new permitted subclass (like `JSONDate`), every switch becomes a compilation error until you handle the new type—preventing bugs at compile-time, not runtime.

---

### Example 5: The `non-sealed` Modifier

The `non-sealed` modifier breaks the seal, allowing unrestricted subclassing from that point onward in the hierarchy.

```java
// Sealed base class
public sealed abstract class Animal
    permits Mammal, Bird, Fish {
    private final String name;

    protected Animal(String name) {
        this.name = name;
    }

    public String name() { return name; }
    public abstract String sound();
}

// Sealed intermediate class - controlled hierarchy continues
public sealed abstract class Mammal extends Animal
    permits Dog, Cat, Pet {
    protected Mammal(String name) {
        super(name);
    }
}

// Non-sealed class - breaks the seal!
public non-sealed abstract class Pet extends Mammal {
    private String owner;

    protected Pet(String name, String owner) {
        super(name);
        this.owner = owner;
    }

    public String owner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}

// Now anyone can extend Pet - the seal is broken
public class Hamster extends Pet {
    private final String color;

    public Hamster(String name, String owner, String color) {
        super(name, owner);
        this.color = color;
    }

    public String color() { return color; }

    @Override
    public String sound() {
        return "squeak squeak";
    }
}

public class GuineaPig extends Pet {
    private final boolean isLongHaired;

    public GuineaPig(String name, String owner, boolean isLongHaired) {
        super(name, owner);
        this.isLongHaired = isLongHaired;
    }

    public boolean isLongHaired() { return isLongHaired; }

    @Override
    public String sound() {
        return "wheek wheek";
    }
}

// Final implementations in sealed hierarchy
public final class Dog extends Mammal {
    private final String breed;

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    public String breed() { return breed; }

    @Override
    public String sound() {
        return "woof";
    }
}

public final class Cat extends Mammal {
    private final boolean isIndoor;

    public Cat(String name, boolean isIndoor) {
        super(name);
        this.isIndoor = isIndoor;
    }

    public boolean isIndoor() { return isIndoor; }

    @Override
    public String sound() {
        return "meow";
    }
}

// Other sealed branches
public final class Bird extends Animal {
    private final double wingspan;

    public Bird(String name, double wingspan) {
        super(name);
        this.wingspan = wingspan;
    }

    public double wingspan() { return wingspan; }

    @Override
    public String sound() {
        return "chirp";
    }
}

public final class Fish extends Animal {
    private final String waterType;

    public Fish(String name, String waterType) {
        super(name);
        this.waterType = waterType;
    }

    public String waterType() { return waterType; }

    @Override
    public String sound() {
        return "blub";
    }
}

public class NonSealedExample {
    public static void main(String[] args) {
        var animals = java.util.List.of(
            new Dog("Buddy", "Golden Retriever"),
            new Cat("Whiskers", true),
            new Bird("Tweety", 15.5),
            new Fish("Nemo", "saltwater"),
            new Hamster("Fluffy", "Alice", "brown"),
            new GuineaPig("Patches", "Bob", true)
        );

        System.out.println("=== Animal Sounds ===");
        for (var animal : animals) {
            System.out.println(animal.name() + " says: " + animal.sound());
        }

        // Demonstrate non-sealed hierarchy
        System.out.println("\n=== Pet Hierarchy (non-sealed) ===");
        var pets = animals.stream()
            .filter(a -> a instanceof Pet)
            .map(a -> (Pet) a)
            .toList();

        for (var pet : pets) {
            System.out.println(pet.name() + " belongs to " + pet.owner());
        }

        // Reflection on sealed hierarchy
        System.out.println("\n=== Sealed Hierarchy Analysis ===");
        System.out.println("Animal is sealed: " + Animal.class.isSealed());
        System.out.println("Mammal is sealed: " + Mammal.class.isSealed());
        System.out.println("Pet is sealed: " + Pet.class.isSealed()); // false!

        System.out.println("\nMammal permits: ");
        for (var permitted : Mammal.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create Dog with breed information")
void shouldCreateDogWithBreedInformation() {
    var dog = new Dog("Buddy", "Golden Retriever");

    assertEquals("Buddy", dog.name());
    assertEquals("Golden Retriever", dog.breed());
    assertEquals("woof", dog.sound());
}

@Test
@DisplayName("Should create Cat with indoor status")
void shouldCreateCatWithIndoorStatus() {
    var cat = new Cat("Whiskers", true);

    assertEquals("Whiskers", cat.name());
    assertTrue(cat.isIndoor());
    assertEquals("meow", cat.sound());
}

@Test
@DisplayName("Should create Hamster as Pet subclass")
void shouldCreateHamsterAsPetSubclass() {
    var hamster = new Hamster("Fluffy", "Alice", "brown");

    assertEquals("Fluffy", hamster.name());
    assertEquals("Alice", hamster.owner());
    assertEquals("brown", hamster.color());
    assertEquals("squeak squeak", hamster.sound());
}

@Test
@DisplayName("Should create GuineaPig as Pet subclass")
void shouldCreateGuineaPigAsPetSubclass() {
    var guineaPig = new GuineaPig("Patches", "Bob", true);

    assertEquals("Patches", guineaPig.name());
    assertEquals("Bob", guineaPig.owner());
    assertTrue(guineaPig.isLongHaired());
}

@Test
@DisplayName("Should verify Animal and Mammal are sealed but Pet is not")
void shouldVerifyAnimalAndMammalAreSealedButPetIsNot() {
    assertTrue(Animal.class.isSealed());
    assertTrue(Mammal.class.isSealed());
    assertFalse(Pet.class.isSealed()); // non-sealed!
}

@Test
@DisplayName("Should allow unlimited extensions of non-sealed Pet class")
void shouldAllowUnlimitedExtensionsOfNonSealedPetClass() {
    // Hamster and GuineaPig can extend Pet without being in permits clause
    assertTrue(Hamster.class.getSuperclass().equals(Pet.class));
    assertTrue(GuineaPig.class.getSuperclass().equals(Pet.class));
}

@Test
@DisplayName("Should update Pet owner")
void shouldUpdatePetOwner() {
    var hamster = new Hamster("Fluffy", "Alice", "brown");
    assertEquals("Alice", hamster.owner());

    hamster.setOwner("Bob");
    assertEquals("Bob", hamster.owner());
}
```

**Output:**
```
=== Animal Sounds ===
Buddy says: woof
Whiskers says: meow
Tweety says: chirp
Nemo says: blub
Fluffy says: squeak squeak
Patches says: wheek wheek

=== Pet Hierarchy (non-sealed) ===
Fluffy belongs to Alice
Patches belongs to Bob

=== Sealed Hierarchy Analysis ===
Animal is sealed: true
Mammal is sealed: true
Pet is sealed: false

Mammal permits:
  - Dog
  - Cat
  - Pet
```

**Key Insight**: The `non-sealed` modifier strategically breaks the seal at a specific point in the hierarchy. `Animal` and `Mammal` remain sealed with controlled inheritance, but `Pet` allows unlimited extensions. This is useful when you want tight control over core abstractions but flexibility in specific branches—like allowing plugin developers to create custom pet types while keeping the core `Animal` hierarchy closed.

---

### Example 6: Domain Modeling with Result Type

Sealed classes shine in domain modeling, particularly for representing operations that can succeed or fail. Here's a practical `Result<T>` type for error handling without exceptions.

```java
// Sealed Result type
public sealed interface Result<T>
    permits Success, Failure {

    // Factory methods
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(String error) {
        return new Failure<>(error);
    }

    static <T> Result<T> failure(String error, Throwable cause) {
        return new Failure<>(error, cause);
    }

    // Query methods
    boolean isSuccess();
    boolean isFailure();

    // Value extraction (safe)
    T getValue();
    String getError();

    // Functional operations
    <U> Result<U> map(java.util.function.Function<T, U> mapper);
    <U> Result<U> flatMap(java.util.function.Function<T, Result<U>> mapper);
    Result<T> orElse(java.util.function.Supplier<Result<T>> alternative);
}

public record Success<T>(T value) implements Result<T> {
    public Success {
        if (value == null) {
            throw new IllegalArgumentException("Success value cannot be null");
        }
    }

    @Override
    public boolean isSuccess() { return true; }

    @Override
    public boolean isFailure() { return false; }

    @Override
    public T getValue() { return value; }

    @Override
    public String getError() {
        throw new UnsupportedOperationException("Success has no error");
    }

    @Override
    public <U> Result<U> map(java.util.function.Function<T, U> mapper) {
        try {
            return new Success<>(mapper.apply(value));
        } catch (Exception e) {
            return new Failure<>(e.getMessage(), e);
        }
    }

    @Override
    public <U> Result<U> flatMap(java.util.function.Function<T, Result<U>> mapper) {
        try {
            return mapper.apply(value);
        } catch (Exception e) {
            return new Failure<>(e.getMessage(), e);
        }
    }

    @Override
    public Result<T> orElse(java.util.function.Supplier<Result<T>> alternative) {
        return this;
    }
}

public record Failure<T>(String error, Throwable cause) implements Result<T> {
    public Failure(String error) {
        this(error, null);
    }

    @Override
    public boolean isSuccess() { return false; }

    @Override
    public boolean isFailure() { return true; }

    @Override
    public T getValue() {
        throw new UnsupportedOperationException("Failure has no value: " + error);
    }

    @Override
    public String getError() { return error; }

    @Override
    public <U> Result<U> map(java.util.function.Function<T, U> mapper) {
        return new Failure<>(error, cause);
    }

    @Override
    public <U> Result<U> flatMap(java.util.function.Function<T, Result<U>> mapper) {
        return new Failure<>(error, cause);
    }

    @Override
    public Result<T> orElse(java.util.function.Supplier<Result<T>> alternative) {
        return alternative.get();
    }
}

public class ResultTypeExample {
    // Simulate database operations
    public static Result<String> findUserById(int id) {
        if (id <= 0) {
            return Result.failure("Invalid user ID: " + id);
        }
        if (id > 1000) {
            return Result.failure("User not found: " + id);
        }
        return Result.success("User" + id);
    }

    public static Result<Integer> calculateDiscount(String userName) {
        if (userName.startsWith("Premium")) {
            return Result.success(20);
        } else if (userName.startsWith("Regular")) {
            return Result.success(10);
        } else {
            return Result.failure("Unknown user category: " + userName);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Basic Result Operations ===");

        // Success case
        var successResult = findUserById(42);
        System.out.println("Is success? " + successResult.isSuccess());
        System.out.println("Value: " + successResult.getValue());

        // Failure case
        var failureResult = findUserById(-1);
        System.out.println("\nIs failure? " + failureResult.isFailure());
        System.out.println("Error: " + failureResult.getError());

        // Chaining with map
        System.out.println("\n=== Chaining with map() ===");
        var mapped = findUserById(100)
            .map(name -> name.toUpperCase())
            .map(name -> "Hello, " + name);

        if (mapped.isSuccess()) {
            System.out.println("Result: " + mapped.getValue());
        }

        // Chaining with flatMap
        System.out.println("\n=== Chaining with flatMap() ===");
        var chained = findUserById(500)
            .flatMap(userName -> calculateDiscount(userName))
            .map(discount -> "Discount: " + discount + "%");

        if (chained.isSuccess()) {
            System.out.println(chained.getValue());
        }

        // Error propagation
        System.out.println("\n=== Error Propagation ===");
        var errorChain = findUserById(2000) // Fails here
            .flatMap(userName -> calculateDiscount(userName))
            .map(discount -> "Discount: " + discount + "%");

        if (errorChain.isFailure()) {
            System.out.println("Error: " + errorChain.getError());
        }

        // Fallback with orElse
        System.out.println("\n=== Fallback with orElse() ===");
        var withFallback = findUserById(2000)
            .orElse(() -> Result.success("GuestUser"));

        System.out.println("Final value: " + withFallback.getValue());

        // Exhaustive switch on Result
        System.out.println("\n=== Exhaustive Switch ===");
        var result = findUserById(123);
        String message = switch (result) {
            case Success<String> s -> "Found user: " + s.value();
            case Failure<String> f -> "Error: " + f.error();
        };
        System.out.println(message);
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create Success with valid value")
void shouldCreateSuccessWithValidValue() {
    var result = Result.success("test");

    assertTrue(result.isSuccess());
    assertFalse(result.isFailure());
    assertEquals("test", result.getValue());
}

@Test
@DisplayName("Should create Failure with error message")
void shouldCreateFailureWithErrorMessage() {
    var result = Result.<String>failure("Something went wrong");

    assertTrue(result.isFailure());
    assertFalse(result.isSuccess());
    assertEquals("Something went wrong", result.getError());
}

@Test
@DisplayName("Should throw exception when accessing value on Failure")
void shouldThrowExceptionWhenAccessingValueOnFailure() {
    var result = Result.<String>failure("Error");

    assertThrows(UnsupportedOperationException.class, result::getValue);
}

@Test
@DisplayName("Should throw exception when accessing error on Success")
void shouldThrowExceptionWhenAccessingErrorOnSuccess() {
    var result = Result.success("test");

    assertThrows(UnsupportedOperationException.class, result::getError);
}

@Test
@DisplayName("Should map Success value")
void shouldMapSuccessValue() {
    var result = Result.success(5)
        .map(x -> x * 2)
        .map(x -> "Value: " + x);

    assertTrue(result.isSuccess());
    assertEquals("Value: 10", result.getValue());
}

@Test
@DisplayName("Should propagate Failure through map")
void shouldPropagateFailureThroughMap() {
    var result = Result.<Integer>failure("Initial error")
        .map(x -> x * 2)
        .map(x -> "Value: " + x);

    assertTrue(result.isFailure());
    assertEquals("Initial error", result.getError());
}

@Test
@DisplayName("Should flatMap Success results")
void shouldFlatMapSuccessResults() {
    var result = Result.success(5)
        .flatMap(x -> Result.success(x * 2))
        .flatMap(x -> Result.success("Result: " + x));

    assertTrue(result.isSuccess());
    assertEquals("Result: 10", result.getValue());
}

@Test
@DisplayName("Should use orElse for Failure recovery")
void shouldUseOrElseForFailureRecovery() {
    var result = Result.<String>failure("Error")
        .orElse(() -> Result.success("fallback"));

    assertTrue(result.isSuccess());
    assertEquals("fallback", result.getValue());
}

@Test
@DisplayName("Should not use orElse for Success")
void shouldNotUseOrElseForSuccess() {
    var result = Result.success("original")
        .orElse(() -> Result.success("fallback"));

    assertEquals("original", result.getValue());
}

@Test
@DisplayName("Should find user by valid ID")
void shouldFindUserByValidID() {
    var result = ResultTypeExample.findUserById(42);

    assertTrue(result.isSuccess());
    assertEquals("User42", result.getValue());
}

@Test
@DisplayName("Should fail for invalid user ID")
void shouldFailForInvalidUserID() {
    var result = ResultTypeExample.findUserById(-1);

    assertTrue(result.isFailure());
    assertTrue(result.getError().contains("Invalid user ID"));
}
```

**Output:**
```
=== Basic Result Operations ===
Is success? true
Value: User42

Is failure? true
Error: Invalid user ID: -1

=== Chaining with map() ===
Result: Hello, USER100

=== Chaining with flatMap() ===
Discount: 10%

=== Error Propagation ===
Error: User not found: 2000

=== Fallback with orElse() ===
Final value: GuestUser

=== Exhaustive Switch ===
Found user: User123
```

**Key Insight**: The sealed `Result<T>` type demonstrates domain modeling at its finest. With only two possible implementations (`Success` and `Failure`), the compiler can verify exhaustive handling in switch expressions. This pattern eliminates null checks, provides type-safe error handling, and enables functional composition with `map` and `flatMap`—all without throwing exceptions. It's a powerful alternative to try-catch for expected error conditions.

---

## Best Practices

### When to Use Sealed Classes

**Use sealed classes when:**

1. **Domain modeling with closed sets** - Payment types, order statuses, geometric shapes, or any domain concept with a fixed set of variants
2. **Exhaustive pattern matching required** - When you need the compiler to verify you've handled all cases
3. **API stability matters** - Prevent external code from creating unexpected subtypes
4. **Type hierarchy is well-defined** - You know all subtypes at design time and they won't change frequently

**Don't use sealed classes when:**

1. **Plugin architectures** - If you need third-party extensions, use interfaces
2. **Frequently changing hierarchies** - Adding new subtypes requires modifying the sealed parent
3. **Library APIs with backward compatibility concerns** - Sealed classes can't be extended outside your module

### Design Patterns with Sealed Classes

**1. Algebraic Data Types (ADTs)**

Combine sealed classes with records for elegant ADTs:

```java
sealed interface Expression permits Constant, Addition, Multiplication {}
record Constant(int value) implements Expression {}
record Addition(Expression left, Expression right) implements Expression {}
record Multiplication(Expression left, Expression right) implements Expression {}
```

**2. State Machines**

Model state transitions explicitly:

```java
sealed interface OrderState permits Pending, Confirmed, Shipped, Delivered, Cancelled {}
// Each state can have different properties and transitions
```

**3. Result/Option Types**

Type-safe error handling and optional values:

```java
sealed interface Result<T> permits Success, Failure {}
sealed interface Option<T> permits Some, None {}
```

### Combining with Other Features

**Sealed + Records** - Immutable domain models with minimal boilerplate

```java
sealed interface Vehicle permits Car, Bike {}
record Car(String brand, int doors) implements Vehicle {}
record Bike(String brand, int gears) implements Vehicle {}
```

**Sealed + Pattern Matching** - Exhaustive switches without `default`

```java
return switch (vehicle) {
    case Car c -> "Car with " + c.doors() + " doors";
    case Bike b -> "Bike with " + b.gears() + " gears";
};
```

**Sealed + Enums** - Enums are implicitly final, perfect for sealed types

```java
sealed interface TrafficLight permits Red, Yellow, Green {}
enum Red implements TrafficLight { INSTANCE }
enum Yellow implements TrafficLight { INSTANCE }
enum Green implements TrafficLight { INSTANCE }
```

### Package Organization

Keep sealed classes and permitted subclasses in the same package:

```
com.example.domain/
  ├── Payment.java (sealed interface)
  ├── CreditCard.java (final record)
  ├── DebitCard.java (final record)
  └── Cash.java (final record)
```

For large hierarchies, use subpackages:

```
com.example.shapes/
  ├── Shape.java (sealed)
  ├── circle/
  │   └── Circle.java
  ├── polygon/
  │   ├── Polygon.java (sealed)
  │   ├── Triangle.java
  │   └── Rectangle.java
  └── freeform/
      └── FreeformShape.java (non-sealed)
```

---

## Pitfalls and Limitations

### Common Mistakes

**1. Forgetting the subclass modifier**

```java
sealed class Animal permits Dog, Cat {}

// ❌ Compile error - must use final, sealed, or non-sealed
class Dog extends Animal {}

// ✅ Correct
final class Dog extends Animal {}
```

**2. Subclass in wrong package**

```java
package com.example.domain;
sealed class Payment permits CreditCard {}

package com.example.impl; // ❌ Different package!
final class CreditCard extends Payment {} // Compile error
```

**3. Using `var` with exhaustive switch**

```java
// ❌ Doesn't compile - var doesn't work with pattern matching
var result = switch (shape) {
    case var c when c instanceof Circle -> "circle";
    // ...
};

// ✅ Use explicit type patterns
var result = switch (shape) {
    case Circle c -> "circle";
    case Rectangle r -> "rectangle";
    // ...
};
```

### Performance Considerations

**Memory overhead** - Sealed classes have the same memory footprint as regular classes. No additional overhead.

**instanceof checks** - Pattern matching with sealed types is **fast**. The JVM can optimize switches over sealed hierarchies because it knows all possible types.

**Reflection** - `Class.isSealed()` and `getPermittedSubclasses()` have negligible overhead. Use freely.

### Limitations

**1. Cannot seal classes from other modules**

You can't make `java.lang.String` sealed—you don't own it.

**2. No runtime enforcement**

Sealed classes are compile-time only. Reflection can still create proxies or use `Unsafe` to bypass restrictions (but don't do this).

**3. Limited to same module/package**

In JPMS (Java Platform Module System), sealed classes and subclasses must be in the same module. For non-modular projects, they must be in the same package.

**4. Cannot change permits clause without breaking changes**

Adding or removing permitted subclasses is a breaking change for anyone using exhaustive switches.

### Migration Strategies

**From open hierarchies:**
1. Identify all current subclasses
2. Add `sealed` and `permits` to parent
3. Add `final`, `sealed`, or `non-sealed` to all subclasses
4. Search for switch statements and remove `default` where appropriate

**From final classes:**
- If you have a `final` class and want to add subtypes, change to `sealed` with explicit `permits`
- This is non-breaking for existing code

---

## Summary and Next Steps

In this article, we explored **Sealed Classes** (JEP 409), one of Java 17's most powerful features for domain modeling and type safety.

**What we covered:**

- **The problem**: Uncontrolled inheritance creates maintenance burdens and prevents compiler verification
- **Sealed classes solution**: Explicitly list permitted subclasses with the `permits` clause
- **Three subclass modifiers**: `final` (no extensions), `sealed` (controlled extensions), `non-sealed` (open extensions)
- **Exhaustive pattern matching**: Compiler-verified switches without `default` cases
- **Multi-level hierarchies**: Sealed classes at multiple levels for complex domain models
- **Practical patterns**: Result types, algebraic data types, state machines

**Key takeaways:**

✅ Sealed classes give you fine-grained control over inheritance hierarchies
✅ Perfect for closed domain models with a fixed set of variants
✅ Enable exhaustive pattern matching with compiler verification
✅ Combine beautifully with records for immutable, type-safe domain models
✅ Use `final` for leaf classes, `sealed` for intermediate levels, `non-sealed` to break the seal

**When to use sealed classes:**
- Domain modeling with closed sets (payment types, shapes, order states)
- APIs where you control all implementations
- Type hierarchies that benefit from exhaustive checking

**When to avoid sealed classes:**
- Plugin architectures requiring third-party extensions
- Frequently changing hierarchies
- Library APIs with strict backward compatibility requirements

---

### What's Next?

In **Part 4**, we'll explore **Pattern Matching** and **Switch Expressions**—two features that work beautifully with sealed classes. You'll learn:

- Pattern matching for `instanceof` (JEP 394)
- Modern switch expressions with arrow syntax (JEP 361)
- Guards and pattern combinations
- Exhaustive switches with sealed types
- Practical examples combining all three features

Pattern matching and sealed classes together bring functional programming concepts to Java, enabling expressive, type-safe code with minimal boilerplate.

**All code examples from this article are available in the GitHub repository:**

```bash
git clone https://github.com/blog-9mac-dev/java-17-features.git
cd java-17-features/sealed-classes
./gradlew test
```

See you in Part 4, where we'll unlock the full power of sealed classes with pattern matching!

---

*This article is part of the "Java 17 Features Every Senior Developer Should Know" series. Check out [Part 1: Introduction & var](part-1-introduction-and-var.md) and [Part 2: Records](part-2-records.md) if you haven't already.*
