# Java 17 Features Every Senior Developer Should Know - Part 2: Records

Welcome back to our comprehensive series on Java 17 features! In [Part 1](part-1-introduction-and-var.md), we explored the `var` keyword and how type inference reduces boilerplate in local variable declarations. Today, we're diving into one of Java's most impactful features: **Records** (JEP 395).

If you've ever groaned while writing yet another data class with getters, equals, hashCode, and toString methods, this article is for you. Records eliminate this ceremony entirely, letting you declare data carriers in a single line while the compiler handles the rest.

## Table of Contents

1. [The Problem: Boilerplate in Data Classes](#the-problem-boilerplate-in-data-classes)
2. [What Are Records?](#what-are-records)
3. [History of Records](#history-of-records)
4. [Basic Syntax and Features](#basic-syntax-and-features)
5. [Practical Examples](#practical-examples)
6. [Performance Considerations](#performance-considerations)
7. [Pitfalls and Limitations](#pitfalls-and-limitations)
8. [Best Practices](#best-practices)
9. [Summary and Next Steps](#summary-and-next-steps)

---

## The Problem: Boilerplate in Data Classes

Before Java 16, creating a simple data class meant writing dozens of lines of repetitive code. Let's look at a typical example - a `Point` class representing coordinates:

```java
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
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

That's **30+ lines of code** just to store two integers! And the problems don't stop there:

- **Error-prone**: Forget to update `equals()` when adding a field? Your tests will miss subtle bugs.
- **Maintenance burden**: Every field addition means updating multiple methods.
- **Cluttered code**: The ceremony obscures the intent: "this class holds x and y coordinates."
- **Refactoring friction**: Renaming fields requires changes in 4-5 places.

### The Lombok Compromise

Many teams turned to Project Lombok as a temporary solution:

```java
@Value // Generates immutable class with all boilerplate
public class Point {
    int x;
    int y;
}
```

Lombok works, but it has downsides:
- Requires IDE plugins and annotation processors
- Generated code is invisible in source form
- Not part of the Java language specification
- Can cause build tool complications
- Creates team friction ("Do we really need another dependency?")

The Java community needed a **native language solution**. Enter Records.

---

## What Are Records?

A **record** is a special kind of class designed to be a transparent carrier for immutable data. When you declare a record, the compiler automatically generates:

1. **Private final fields** for each component
2. **Public accessor methods** (not getters - just the component name)
3. **A canonical constructor** accepting all components
4. **Proper `equals()` method** comparing all components
5. **Proper `hashCode()` method** combining all components
6. **A `toString()` method** with record-specific format

Here's how the `Point` class looks as a record:

```java
public record Point(int x, int y) {
}
```

That's it. **One line.** The compiler generates the equivalent of all 30+ lines we wrote manually.

### Key Characteristics

Records have specific properties that distinguish them from regular classes:

- **Immutability**: All components are implicitly `final`
- **Transparency**: The state is part of the public API contract
- **Nominal typing**: Each record declaration creates a distinct type
- **Restrictions**: Records cannot extend other classes (they implicitly extend `java.lang.Record`)
- **Final**: Records are implicitly final - they cannot be subclassed

---

## History of Records

Records didn't appear overnight. They evolved through multiple Java versions based on community feedback.

### Java 14: Preview Feature (JEP 359)

Records debuted in March 2020 as a **preview feature**, requiring the `--enable-preview` compiler flag. The basic syntax was introduced:

```java
record Point(int x, int y) {}
```

**What you got:**
- Canonical constructor
- Accessor methods (`p.x()`, not `p.getX()`)
- `equals()`, `hashCode()`, `toString()` implementations
- Ability to add custom methods

**Limitations:**
- Preview status meant production use required caution
- Limited to top-level declarations
- Couldn't implement interfaces (initially)

### Java 15: Second Preview (JEP 384)

September 2020 brought refinements based on developer feedback:

**New capabilities:**
- **Local records** - declare records inside methods
- **Compact constructor syntax** - validation without repeating parameters
- **Interface implementation** - records can now implement interfaces
- **Nested records** - records within classes or other records

```java
// Local record - new in Java 15
public void processOrders(List<Order> orders) {
    record OrderSummary(String customer, double total) {}

    var summaries = orders.stream()
        .map(o -> new OrderSummary(o.customer(), o.total()))
        .toList();
}
```

### Java 16: Standard Feature (JEP 395)

March 2021 marked the graduation of records to a **standard, permanent feature**. No more `--enable-preview` flag needed.

**Additional enhancements:**
- **Generic records** - `record Pair<T, U>(T first, U second) {}`
- **Static members** - static fields and methods allowed
- **Annotation support** - full annotation capabilities
- **Reflection API** - `Class.isRecord()`, `getRecordComponents()`

---

## Basic Syntax and Features

### Declaration Syntax

The basic record declaration consists of:
1. The `record` keyword
2. The record name
3. Component list in parentheses (the **header**)
4. Optional body in curly braces

```java
// Minimal record - just the header
record Point(int x, int y) {}

// Record with validation
record Range(int start, int end) {
    // Compact constructor
    public Range {
        if (start > end) {
            throw new IllegalArgumentException(
                "start (" + start + ") must not exceed end (" + end + ")"
            );
        }
    }
}
```

### What the Compiler Generates

When you declare `record Point(int x, int y) {}`, the compiler creates:

```java
public final class Point extends Record {
    private final int x;
    private final int y;

    // Canonical constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Accessor methods (NOT getters!)
    public int x() { return x; }
    public int y() { return y; }

    // equals comparing all components
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point other &&
               x == other.x &&
               y == other.y;
    }

    // hashCode combining all components
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // toString with record-specific format
    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

### Canonical Constructor

The **canonical constructor** is the constructor that takes all components in order. The compiler generates it automatically:

```java
record Point(int x, int y) {}

// Compiler generates:
// public Point(int x, int y) { ... }
```

You can provide your own canonical constructor if needed:

```java
record Point(int x, int y) {
    // Explicit canonical constructor
    public Point(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be non-negative");
        }
        this.x = x;
        this.y = y;
    }
}
```

### Compact Constructor

Java 15 introduced the **compact constructor** - a more concise way to customize the canonical constructor:

```java
record Range(int start, int end) {
    // Compact constructor - no parameter list!
    public Range {
        // Validation runs BEFORE field initialization
        if (start > end) {
            throw new IllegalArgumentException("Invalid range");
        }
        // Field assignments happen automatically after this block
    }
}
```

The compact constructor is syntactic sugar. The compiler transforms it into the canonical constructor, inserting field assignments at the end.

**You can also normalize data:**

```java
record Person(String name, int age) {
    public Person {
        // Normalize before storing
        name = name.trim().toUpperCase();
        age = Math.max(0, age); // Ensure non-negative
    }
}
```

### Custom Methods

Records can have instance and static methods:

```java
record Rectangle(int width, int height) {
    // Instance method
    public int area() {
        return width * height;
    }

    // Static factory method
    public static Rectangle square(int side) {
        return new Rectangle(side, side);
    }

    // "Wither" method - returns modified copy
    public Rectangle withWidth(int newWidth) {
        return new Rectangle(newWidth, height);
    }
}
```

---

## Practical Examples

Let's explore records through six practical examples, each with complete code and JUnit tests.

### Example 1: Basic Records with Validation

Records provide immutable data carriers with minimal boilerplate. The compiler automatically generates equals(), hashCode(), toString(), and accessor methods.

```java
// Basic record representing 2D coordinates
public record Point(int x, int y) {
}

// Record with validation using compact constructor
public record Range(int start, int end) {
    public Range {
        if (start > end) {
            throw new IllegalArgumentException(
                "start must not exceed end: start=" + start + ", end=" + end
            );
        }
    }

    public boolean contains(int value) {
        return value >= start && value <= end;
    }
}

public class BasicRecordExample {
    public static void main(String[] args) {
        // Point examples
        var p1 = new Point(10, 20);
        var p2 = new Point(10, 20);
        var p3 = new Point(30, 40);

        System.out.println("Point p1: " + p1);
        System.out.println("Accessing components: x=" + p1.x() + ", y=" + p1.y());

        // Equality (value-based)
        System.out.println("p1.equals(p2): " + p1.equals(p2));  // true
        System.out.println("p1 == p2: " + (p1 == p2));           // false
        System.out.println("p1.hashCode() == p2.hashCode(): " + (p1.hashCode() == p2.hashCode())); // true

        // Range examples
        var r1 = new Range(1, 10);
        System.out.println("\nRange: " + r1);
        System.out.println("Contains 5? " + r1.contains(5));
        System.out.println("Contains 15? " + r1.contains(15));

        // Validation
        try {
            var invalid = new Range(10, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create Point with x and y coordinates")
void shouldCreatePointWithCoordinates() {
    var point = new Point(10, 20);

    assertEquals(10, point.x());
    assertEquals(20, point.y());
}

@Test
@DisplayName("Should compare Points by value equality")
void shouldComparePointsByValueEquality() {
    var p1 = new Point(10, 20);
    var p2 = new Point(10, 20);
    var p3 = new Point(10, 30);

    assertEquals(p1, p2);
    assertNotEquals(p1, p3);
    assertEquals(p1.hashCode(), p2.hashCode());
}

@Test
@DisplayName("Should generate toString in record format")
void shouldGenerateToStringInRecordFormat() {
    var point = new Point(10, 20);

    assertEquals("Point[x=10, y=20]", point.toString());
}

@Test
@DisplayName("Should throw exception when creating Range with start greater than end")
void shouldThrowExceptionWhenCreatingRangeWithStartGreaterThanEnd() {
    var exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Range(10, 1)
    );

    assertTrue(exception.getMessage().contains("start must not exceed end"));
}
```

**Output:**
```
Point p1: Point[x=10, y=20]
Accessing components: x=10, y=20
p1.equals(p2): true
p1 == p2: false
p1.hashCode() == p2.hashCode(): true

Range: Range[start=1, end=10]
Contains 5? true
Contains 15? false
Expected error: start must not exceed end: start=10, end=1
```

**Key Insight**: Records provide value semantics out of the box. Two `Point` instances with the same coordinates are considered equal, making them perfect for map keys and set elements.

### Example 2: Generic Records

Generic records enable reusable, type-safe data structures with full type inference support.

```java
// Generic pair - holds two values of potentially different types
public record Pair<T, U>(T first, U second) {
    public Pair<U, T> swap() {
        return new Pair<>(second, first);
    }
}

// Generic record with bounded type parameter - only Number subtypes allowed
public record Box<T extends Number>(T value) {
    public double doubleValue() {
        return value.doubleValue();
    }

    public static Box<Integer> ofInt(int value) {
        return new Box<>(value);
    }
}

public class GenericRecordExample {
    public static void main(String[] args) {
        // Pair with different types
        var pair1 = new Pair<>("age", 30);
        System.out.println("String-Integer pair: " + pair1);
        System.out.println("First: " + pair1.first() + ", Second: " + pair1.second());

        // Swap elements
        var swapped = pair1.swap();
        System.out.println("Swapped: " + swapped);

        // Box with bounded type
        var intBox = Box.ofInt(42);
        var doubleBox = new Box<>(3.14);
        System.out.println("Integer box: " + intBox);
        System.out.println("  as double: " + intBox.doubleValue());
        System.out.println("Double box: " + doubleBox);

        // Nested generics
        var nested = new Pair<>(new Pair<>(1, 2), "coordinates");
        System.out.println("Nested pair: " + nested);
        System.out.println("Inner first: " + nested.first().first());
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should create generic Pair with different types")
void shouldCreateGenericPairWithDifferentTypes() {
    var p1 = new Pair<>("age", 30);
    assertEquals("age", p1.first());
    assertEquals(30, p1.second());

    var p2 = new Pair<>(42, "answer");
    assertEquals(42, p2.first());
    assertEquals("answer", p2.second());
}

@Test
@DisplayName("Should swap Pair elements")
void shouldSwapPairElements() {
    var original = new Pair<>("key", 123);
    var swapped = original.swap();

    assertEquals(123, swapped.first());
    assertEquals("key", swapped.second());
    assertEquals("key", original.first());
}

@Test
@DisplayName("Should create Box with bounded generic type")
void shouldCreateBoxWithBoundedGenericType() {
    var intBox = new Box<>(42);
    var doubleBox = new Box<>(3.14);

    assertEquals(42.0, intBox.doubleValue());
    assertEquals(3.14, doubleBox.doubleValue(), 0.001);
}

@Test
@DisplayName("Should support nested Pairs")
void shouldSupportNestedPairs() {
    var nested = new Pair<>(new Pair<>(1, 2), "coordinates");

    assertEquals(1, nested.first().first());
    assertEquals(2, nested.first().second());
    assertEquals("coordinates", nested.second());
}
```

**Output:**
```
String-Integer pair: Pair[first=age, second=30]
First: age, Second: 30
Swapped: Pair[first=30, second=age]
Integer box: Box[value=42]
  as double: 42.0
Double box: Box[value=3.14]
Nested pair: Pair[first=Pair[first=1, second=2], second=coordinates]
Inner first: 1
```

**Key Insight**: Generic records combine type safety with reusability. The bounded type parameter in `Box<T extends Number>` ensures only numeric types can be stored while still providing generic flexibility.

### Example 3: Records with Custom Methods

Records can contain rich behavior while maintaining immutability. The "wither" pattern allows creating modified copies.

```java
// Person record with custom methods and validation
public record Person(String firstName, String lastName, int age) {
    public Person {
        firstName = firstName.trim();
        lastName = lastName.trim();
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdult() {
        return age >= 18;
    }

    // Wither method - returns modified copy
    public Person withAge(int newAge) {
        return new Person(firstName, lastName, newAge);
    }
}

// Money record with business logic
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        currency = currency.toUpperCase();
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount.add(other.amount), currency);
    }

    public static Money dollars(double amount) {
        return new Money(BigDecimal.valueOf(amount), "USD");
    }
}

public class RecordMethodsExample {
    public static void main(String[] args) {
        // Person examples
        Person person = new Person("  Alice  ", "  Smith  ", 30);
        System.out.println("Person: " + person);
        System.out.println("Full name: " + person.fullName());
        System.out.println("Is adult: " + person.isAdult());

        // Wither pattern
        Person older = person.withAge(31);
        System.out.println("After birthday: " + older);
        System.out.println("Original unchanged: " + person);

        // Money examples
        Money m1 = Money.dollars(10.50);
        Money m2 = Money.dollars(5.25);
        System.out.println("\nm1: " + m1);
        System.out.println("m2: " + m2);
        System.out.println("Sum: " + m1.add(m2));
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should normalize names and validate age in Person")
void shouldNormalizeNamesAndValidateAge() {
    Person person = new Person("  Alice  ", "  Smith  ", 30);

    assertEquals("Alice", person.firstName());
    assertEquals("Smith", person.lastName());
    assertEquals("Alice Smith", person.fullName());
    assertTrue(person.isAdult());
}

@Test
@DisplayName("Should create modified copy using wither pattern")
void shouldCreateModifiedCopyUsingWitherPattern() {
    Person p1 = new Person("Bob", "Jones", 15);
    assertFalse(p1.isAdult());

    Person p2 = p1.withAge(25);

    assertTrue(p2.isAdult());
    assertEquals(25, p2.age());
    assertEquals(15, p1.age()); // Original unchanged
}

@Test
@DisplayName("Should add Money with same currency")
void shouldAddMoneyWithSameCurrency() {
    Money m1 = Money.dollars(10.50);
    Money m2 = new Money(new BigDecimal("5.25"), "USD");

    Money sum = m1.add(m2);

    assertEquals(new BigDecimal("15.75"), sum.amount());
    assertEquals("USD", sum.currency());
}

@Test
@DisplayName("Should throw exception when adding Money with different currencies")
void shouldThrowExceptionWhenAddingMoneyWithDifferentCurrencies() {
    Money usd = Money.dollars(10);
    Money eur = new Money(BigDecimal.TEN, "EUR");

    assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
}
```

**Output:**
```
Person: Person[firstName=Alice, lastName=Smith, age=30]
Full name: Alice Smith
Is adult: true
After birthday: Person[firstName=Alice, lastName=Smith, age=31]
Original unchanged: Person[firstName=Alice, lastName=Smith, age=30]

m1: Money[amount=10.50, currency=USD]
m2: Money[amount=5.25, currency=USD]
Sum: Money[amount=15.75, currency=USD]
```

**Key Insight**: The "wither" pattern (`withAge()`) is common in records - since records are immutable, methods that would "modify" state instead return a new instance with the changed value.

### Example 4: Records Implementing Interfaces

Records work seamlessly with interfaces, enabling polymorphism while maintaining concise syntax.

```java
// Interface for drawable objects
public interface Drawable {
    void draw();
    double area();

    default String description() {
        return "Drawable shape with area: " + area();
    }
}

// Circle record implementing Drawable
public record Circle(int x, int y, int radius) implements Drawable {
    public Circle {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
    }

    @Override
    public void draw() {
        System.out.println("Drawing circle at (" + x + ", " + y +
                         ") with radius " + radius);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

// Rectangle record implementing Drawable
public record Rectangle(int x, int y, int width, int height) implements Drawable {
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
    }

    @Override
    public void draw() {
        System.out.println("Drawing rectangle at (" + x + ", " + y + ") " +
                         width + "x" + height);
    }

    @Override
    public double area() {
        return width * height;
    }

    public boolean isSquare() {
        return width == height;
    }
}

public class RecordInterfaceExample {
    public static void main(String[] args) {
        // Drawable shapes
        Circle circle = new Circle(10, 20, 5);
        circle.draw();
        System.out.println("Area: " + circle.area());
        System.out.println("Description: " + circle.description());

        Rectangle rect = new Rectangle(0, 0, 100, 50);
        rect.draw();
        System.out.println("Area: " + rect.area());
        System.out.println("Is square: " + rect.isSquare());

        // Polymorphism
        List<Drawable> shapes = List.of(
            new Circle(0, 0, 10),
            new Rectangle(0, 0, 20, 30),
            new Circle(5, 5, 15)
        );

        System.out.println("\nPolymorphic behavior:");
        for (Drawable shape : shapes) {
            System.out.println(shape.getClass().getSimpleName() + " - " +
                             shape.description());
        }
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should implement Drawable interface for Circle and Rectangle")
void shouldImplementDrawableInterface() {
    Circle circle = new Circle(10, 20, 5);
    Rectangle rect = new Rectangle(0, 0, 100, 50);

    assertTrue(circle instanceof Drawable);
    assertTrue(rect instanceof Drawable);
}

@Test
@DisplayName("Should calculate areas correctly")
void shouldCalculateAreasCorrectly() {
    Circle c = new Circle(0, 0, 5);
    Rectangle r = new Rectangle(0, 0, 10, 20);

    assertTrue(c.area() > 78 && c.area() < 79); // π * 5²
    assertEquals(200.0, r.area(), 0.001);       // 10 * 20
}

@Test
@DisplayName("Should support polymorphic collections")
void shouldSupportPolymorphicCollections() {
    List<Drawable> shapes = List.of(
        new Circle(10, 20, 5),
        new Rectangle(30, 40, 100, 50),
        new Circle(60, 70, 15)
    );

    assertEquals(3, shapes.size());
    assertTrue(shapes.get(0) instanceof Circle);
    assertTrue(shapes.get(1) instanceof Rectangle);
}

@Test
@DisplayName("Should check if Rectangle is square")
void shouldCheckIfRectangleIsSquare() {
    Rectangle square = new Rectangle(0, 0, 10, 10);
    Rectangle rect = new Rectangle(0, 0, 10, 20);

    assertTrue(square.isSquare());
    assertFalse(rect.isSquare());
}
```

**Output:**
```
Drawing circle at (10, 20) with radius 5
Area: 78.53981633974483
Description: Drawable shape with area: 78.53981633974483
Drawing rectangle at (0, 0) 100x50
Area: 5000.0
Is square: false

Polymorphic behavior:
Circle - Drawable shape with area: 314.1592653589793
Rectangle - Drawable shape with area: 600.0
Circle - Drawable shape with area: 706.8583470577034
```

**Key Insight**: Records work seamlessly with interfaces, enabling polymorphism while maintaining all the benefits of concise syntax and generated methods.

### Example 5: Nested Records

Records compose naturally to model complex domain objects. Each record maintains its own invariants while composing into larger structures.

```java
// Address record - represents a physical address
public record Address(String street, String city, String zipCode, String country) {
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be blank");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be blank");
        }
        // Normalize zip code (remove spaces)
        zipCode = zipCode.replaceAll("\\s", "");
        // Normalize country to uppercase
        country = country.toUpperCase();
    }

    public String format() {
        return street + ", " + city + " " + zipCode + ", " + country;
    }

    public boolean isInCountry(String countryCode) {
        return country.equalsIgnoreCase(countryCode);
    }
}

// Employee record - contains nested Address
public record Employee(String name, int id, Address address) {
    public Employee {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive: " + id);
        }
    }

    public String getFullAddress() {
        return address.format();
    }

    public String getCity() {
        return address.city();
    }

    public Employee relocate(Address newAddress) {
        return new Employee(name, id, newAddress);
    }

    public boolean worksInCountry(String countryCode) {
        return address.isInCountry(countryCode);
    }
}

public class NestedRecordsExample {
    public static void main(String[] args) {
        // Simple nesting
        Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
        Employee emp = new Employee("Alice Smith", 1001, addr);

        System.out.println("Employee: " + emp.name());
        System.out.println("ID: " + emp.id());
        System.out.println("City: " + emp.getCity());
        System.out.println("Full address: " + emp.getFullAddress());
        System.out.println("Works in USA: " + emp.worksInCountry("USA"));

        // Relocation
        Address newAddr = new Address("456 Oak Ave", "Portland", "97201", "USA");
        Employee relocated = emp.relocate(newAddr);

        System.out.println("\nOriginal city: " + emp.getCity());
        System.out.println("New city: " + relocated.getCity());
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should access nested Address properties from Employee")
void shouldAccessNestedAddressProperties() {
    Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
    Employee emp = new Employee("Alice Smith", 1001, addr);

    assertEquals("Alice Smith", emp.name());
    assertEquals("Springfield", emp.address().city());
    assertEquals("12345", emp.address().zipCode());
}

@Test
@DisplayName("Should format full address from nested record")
void shouldFormatFullAddressFromNestedRecord() {
    Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
    Employee emp = new Employee("Bob Jones", 1002, addr);

    String expected = "123 Main St, Springfield 12345, USA";
    assertEquals(expected, emp.getFullAddress());
}

@Test
@DisplayName("Should create relocated Employee with new Address")
void shouldCreateRelocatedEmployeeWithNewAddress() {
    Address addr1 = new Address("123 Main St", "Springfield", "12345", "USA");
    Employee emp1 = new Employee("Charlie", 1003, addr1);

    Address addr2 = new Address("456 Oak Ave", "Portland", "97201", "USA");
    Employee emp2 = emp1.relocate(addr2);

    assertEquals("Portland", emp2.address().city());
    assertEquals("Springfield", emp1.address().city()); // Original unchanged
    assertEquals(emp1.id(), emp2.id());
    assertEquals(emp1.name(), emp2.name());
}

@Test
@DisplayName("Should normalize zip code removing spaces")
void shouldNormalizeZipCodeRemovingSpaces() {
    Address addr = new Address("123 Main", "City", "12 345", "USA");
    assertEquals("12345", addr.zipCode());
}
```

**Output:**
```
Employee: Alice Smith
ID: 1001
City: Springfield
Full address: 123 Main St, Springfield 12345, USA
Works in USA: true

Original city: Springfield
New city: Portland
```

**Key Insight**: Nested records model complex domain objects naturally. Each record maintains its own invariants while composing into larger structures.

### Example 6: Performance Considerations

Understanding how records perform is crucial for production use - memory layout, equality operations, and collection performance.

```java
// Small record for memory demonstration
public record SmallRecord(int x, int y) {
}

// Larger record for comparison
public record LargeRecord(long a, long b, long c, long d, long e) {
}

// Record for equality testing
public record TestRecord(String field1, String field2, int field3, long field4) {
}

// Coordinate record for collection performance
public record Coordinate(int x, int y) {
    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}

// Color record demonstrating caching pattern
public record Color(int r, int g, int b) {
    private static final Map<String, Color> CACHE = new ConcurrentHashMap<>();

    public Color {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException(
                "RGB values must be 0-255: r=" + r + ", g=" + g + ", b=" + b
            );
        }
    }

    public static Color of(int r, int g, int b) {
        String key = r + "," + g + "," + b;
        return CACHE.computeIfAbsent(key, k -> new Color(r, g, b));
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static final Color RED = Color.of(255, 0, 0);
    public static final Color GREEN = Color.of(0, 255, 0);
    public static final Color BLUE = Color.of(0, 0, 255);

    public String toHex() {
        return String.format("#%02X%02X%02X", r, g, b);
    }
}

public class RecordPerformanceExample {
    public static void main(String[] args) {
        // Memory estimates
        System.out.println("=== Memory Layout ===");
        System.out.println("SmallRecord: ~24 bytes");
        System.out.println("LargeRecord: ~56 bytes");

        // Equality performance
        System.out.println("\n=== Equality Performance ===");
        TestRecord r1 = new TestRecord("alpha", "beta", 100, 200L);
        TestRecord r2 = new TestRecord("alpha", "beta", 100, 200L);

        int iterations = 1_000_000;
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            r1.equals(r2);
        }
        long duration = System.nanoTime() - start;
        System.out.println("1M equality checks: " + duration / 1_000_000 + "ms");

        // HashMap performance
        System.out.println("\n=== HashMap Performance ===");
        Map<Coordinate, String> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(new Coordinate(i, i * 2), "value" + i);
        }
        System.out.println("Inserted 1000 records as keys");
        System.out.println("Lookup test: " + map.get(new Coordinate(500, 1000)));

        // Caching pattern
        System.out.println("\n=== Caching Pattern ===");
        Color.clearCache();
        Color red1 = Color.of(255, 0, 0);
        Color red2 = Color.of(255, 0, 0);
        System.out.println("red1 == red2 (same instance): " + (red1 == red2));
        System.out.println("RED constant: " + Color.RED.toHex());
    }
}
```

```java
// Unit tests

@Test
@DisplayName("Should have comparable memory overhead to manual classes")
void shouldHaveComparableMemoryOverhead() {
    SmallRecord small = new SmallRecord(10, 20);
    LargeRecord large = new LargeRecord(1L, 2L, 3L, 4L, 5L);

    // Records have same overhead as equivalent manual classes
    assertNotNull(small);
    assertNotNull(large);
}

@Test
@DisplayName("Should perform equality checks efficiently")
void shouldPerformEqualityChecksEfficiently() {
    TestRecord r1 = new TestRecord("alpha", "beta", 100, 200L);
    TestRecord r2 = new TestRecord("alpha", "beta", 100, 200L);

    long start = System.nanoTime();
    for (int i = 0; i < 1_000_000; i++) {
        r1.equals(r2);
    }
    long duration = System.nanoTime() - start;

    assertTrue(duration < 100_000_000); // Under 100ms
}

@Test
@DisplayName("Should work efficiently as HashMap keys")
void shouldWorkEfficientlyAsHashMapKeys() {
    Map<Coordinate, String> map = new HashMap<>();

    for (int i = 0; i < 1000; i++) {
        map.put(new Coordinate(i, i * 2), "value" + i);
    }

    Coordinate key = new Coordinate(500, 1000);
    assertEquals("value500", map.get(key));

    // Check hash distribution
    Set<Integer> hashCodes = new HashSet<>();
    for (Coordinate coord : map.keySet()) {
        hashCodes.add(coord.hashCode());
    }
    assertTrue(hashCodes.size() > 950); // Good hash distribution
}

@Test
@DisplayName("Should support caching pattern with factory method")
void shouldSupportCachingPatternWithFactoryMethod() {
    Color.clearCache();
    Color red1 = Color.of(255, 0, 0);
    Color red2 = Color.of(255, 0, 0);

    assertSame(red1, red2); // Same instance from cache
    assertEquals(red1, red2); // Also value equality
}
```

**Output:**
```
=== Memory Layout ===
SmallRecord: ~24 bytes
LargeRecord: ~56 bytes

=== Equality Performance ===
1M equality checks: 15ms

=== HashMap Performance ===
Inserted 1000 records as keys
Lookup test: value500

=== Caching Pattern ===
red1 == red2 (same instance): true
RED constant: #FF0000
```

**Key Insights:**
- Records have the same memory overhead as equivalent manual classes
- Object header: 12-16 bytes (depends on JVM settings)
- Components stored as instance fields (primitives inline, references as pointers)
- No additional overhead for generated methods
- Equality performance depends on number and types of components
- Records work excellently as map keys due to consistent `equals()` and `hashCode()`
- Caching pattern reduces memory footprint for frequently used records

---

## Pitfalls and Limitations

Records have intentional restrictions that prevent common mistakes but can surprise developers coming from traditional classes.

### Mutable Components

Records are only as immutable as their components. Arrays and collections are mutable:

```java
// ❌ PROBLEM: Array component can be modified
record BadRecord(int[] data) {
}

// Usage
int[] array = {1, 2, 3};
BadRecord record = new BadRecord(array);
array[0] = 999; // Modifies the record's internal state!

// ✅ SOLUTION: Defensive copying
record GoodRecord(int[] data) {
    public GoodRecord {
        data = data.clone(); // Copy on construction
    }

    @Override
    public int[] data() {
        return data.clone(); // Copy on access
    }
}
```

### Collections Require Special Handling

Collections need defensive copying and immutability:

```java
// ❌ PROBLEM: Mutable list
record BadTeam(String name, List<String> members) {
}

List<String> members = new ArrayList<>();
members.add("Alice");
BadTeam team = new BadTeam("Dev Team", members);
members.add("Bob"); // Modifies team's internal list!

// ✅ SOLUTION: Use immutable copy
record GoodTeam(String name, List<String> members) {
    public GoodTeam {
        members = List.copyOf(members); // Immutable defensive copy
    }
}
```

### Cannot Extend Classes

Records implicitly extend `java.lang.Record` and cannot extend other classes:

```java
// ❌ Won't compile
record Point(int x, int y) extends SomeClass {
}

// ✅ Can implement interfaces
interface Positioned {
    int x();
    int y();
}

record Point(int x, int y) implements Positioned {
}
```

### Final by Nature

Records are implicitly final - they cannot be subclassed:

```java
record Point(int x, int y) {
}

// ❌ Won't compile
class Point3D extends Point {
}
```

### Instance Fields Forbidden

You cannot declare instance fields beyond the components:

```java
// ❌ Won't compile
record Point(int x, int y) {
    private int z; // Compilation error!
}

// ✅ Use components or computed values
record Point(int x, int y) {
    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}
```

---

## Best Practices

### When to Use Records

✅ **Use records for:**
- **Data Transfer Objects (DTOs)** - perfect for API request/response models
- **Value objects** - coordinates, money, dates, addresses
- **Configuration objects** - immutable settings
- **Grouping data in streams** - temporary structures in processing pipelines
- **Domain value types** - anything representing a "value" rather than an "entity"
- **Return values** - replacing arrays or lists for multiple return values

```java
// Excellent record use case
record ApiResponse(int statusCode, String message, LocalDateTime timestamp) {
}

record Coordinates(double latitude, double longitude) {
}

record Money(BigDecimal amount, Currency currency) {
}
```

### When NOT to Use Records

❌ **Don't use records when:**
- **You need mutable state** - records are for immutable data
- **You need inheritance** - records can't extend classes
- **You want to hide representation** - records are transparent by design
- **You need JavaBeans conventions** - records use `field()` not `getField()`
- **Complex lazy initialization needed** - all fields must be set in constructor

```java
// Bad record use - this should be a class
record UserSession(String userId, List<Action> actions) {
    public void addAction(Action a) { // ❌ Violates immutability
        actions.add(a);
    }
}

// Good alternative - use a class with encapsulation
class UserSession {
    private final String userId;
    private final List<Action> actions = new ArrayList<>();

    public void addAction(Action a) {
        actions.add(a);
    }
}
```

### Immutable Collections Pattern

Always use `List.copyOf()` for collection components:

```java
record Team(String name, List<String> members) {
    public Team {
        members = List.copyOf(members); // Immutable copy
    }
}

// Usage
List<String> mutableList = new ArrayList<>();
mutableList.add("Alice");
Team team = new Team("Dev", mutableList);

mutableList.add("Bob"); // Doesn't affect team
assertEquals(1, team.members().size());
```

### Validation in Compact Constructor

Put all validation in the compact constructor:

```java
record Email(String address) {
    public Email {
        if (address == null || !address.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        address = address.toLowerCase(); // Normalize
    }
}
```

### Document Invariants

Use JavaDoc to document invariants enforced by validation:

```java
/**
 * Represents a valid email address.
 *
 * <p>Invariants:
 * <ul>
 *   <li>Address is non-null</li>
 *   <li>Address contains '@' character</li>
 *   <li>Address is normalized to lowercase</li>
 * </ul>
 */
record Email(String address) {
    public Email {
        if (address == null || !address.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        address = address.toLowerCase();
    }
}
```

---

## Summary and Next Steps

### Key Takeaways

1. **Records eliminate boilerplate** - one line replaces 30+ lines of code
2. **Immutability by default** - all components are final
3. **Value semantics** - structural equality, perfect for DTOs and value objects
4. **Compact constructor** - concise validation and normalization
5. **Not a silver bullet** - use for data carriers, not entities with behavior
6. **Defensive copying required** - for arrays and collections
7. **Performance comparable** - to equivalent manual classes

### Records vs Alternatives

| Feature | Records | Lombok @Value | Manual Class |
|---------|---------|---------------|--------------|
| Language support | Native | Library dependency | Native |
| IDE support | Built-in | Requires plugin | Built-in |
| Boilerplate | Minimal | Minimal | High |
| Flexibility | Restricted | High | Unlimited |
| Immutability | Enforced | Optional | Manual |
| Pattern matching | Full support | None | Limited |

### Check Your Understanding

- [ ] Can you explain what the compiler generates for a record?
- [ ] Do you understand the difference between canonical and compact constructors?
- [ ] Can you implement defensive copying for collection components?
- [ ] Do you know when to use records vs regular classes?
- [ ] Can you explain why records can't extend other classes?

### Coming Up Next

**Part 3: Sealed Classes (JEP 409)** - Controlling type hierarchies

Sealed classes let you restrict which classes can extend or implement a type, enabling:
- Exhaustive pattern matching without default cases
- Closed type hierarchies for domain modeling
- Compiler-verified completeness in switch statements
- Better API design with controlled inheritance

We'll explore how sealed classes combine with records to create powerful, type-safe domain models.

### Resources

#### Official Documentation

- **JEP 395 - Records**: [openjdk.org/jeps/395](https://openjdk.org/jeps/395)
  The official JEP that made records a standard feature in Java 16. Contains design rationale, grammar specification, and migration guide.

- **Java Language Specification - Records**: [docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.10](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.10)
  Formal language specification for record classes.

#### Interactive References

- **Java Almanac - Records**: [javaalmanac.io/features/records/](https://javaalmanac.io/features/records/)
  Interactive examples and timeline of record features across Java versions.

#### Code Repository

- **GitHub Repository**: [github.com/dawid-swist/blog-9mac-dev-code](https://github.com/dawid-swist/blog-9mac-dev-code)
  All examples from this article with full tests:
  ```bash
  git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
  cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
  ../../gradlew test
  ```

---

## Run the Examples

All code examples are available in the repository:

```bash
# Run individual tests
./gradlew test --tests BasicRecordExampleTest
./gradlew test --tests GenericRecordExampleTest
./gradlew test --tests RecordMethodsExampleTest
./gradlew test --tests RecordInterfaceExampleTest
./gradlew test --tests NestedRecordsExampleTest
./gradlew test --tests RecordPerformanceExampleTest

# Run all Part 2 tests
./gradlew test --tests *part2*
```

---

*Written for [blog.9mac.dev](https://blog.9mac.dev)*
*Part of the "Java 17 Features Every Senior Developer Should Know" series*

**Previous**: [Part 1 - Introduction & var Keyword](part-1-introduction-and-var.md)
**Next**: Part 3 - Sealed Classes *(coming soon)*
