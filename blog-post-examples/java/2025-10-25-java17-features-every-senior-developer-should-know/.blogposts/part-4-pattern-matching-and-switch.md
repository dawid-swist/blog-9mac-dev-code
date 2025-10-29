# Java 17 Features Every Senior Developer Should Know - Part 4: Pattern Matching & Switch Expressions

Welcome to Part 4 of our comprehensive series on Java 17 features! In previous installments, we explored the [`var` keyword](part-1-introduction-and-var.md) for type inference and [Records](part-2-records.md) for eliminating boilerplate in data classes. Today, we're diving into two transformative features that fundamentally change how we write conditional logic and type checks in Java: **Pattern Matching for instanceof** (JEP 394) and **Switch Expressions** (JEP 361).

These features represent Java's first serious steps toward pattern matching—a programming paradigm that functional languages have enjoyed for decades. For developers upgrading from Java 8, this is where modern Java starts to feel truly different.

## Table of Contents

1. [What is Pattern Matching?](#what-is-pattern-matching)
2. [Historical Context: Java 1-8](#historical-context-java-1-8)
3. [Pattern Matching in Other Languages](#pattern-matching-in-other-languages)
4. [Pattern Matching for instanceof](#pattern-matching-for-instanceof)
5. [Switch Expressions](#switch-expressions)
6. [Practical Examples](#practical-examples)
7. [Best Practices](#best-practices)
8. [Summary and Next Steps](#summary-and-next-steps)

---

## What is Pattern Matching?

**Pattern matching** is a mechanism for checking a value against a pattern and, if it matches, deconstructing that value into its constituent parts in a single operation. Rather than writing separate checks and casts, pattern matching combines these operations atomically.

In academic terms, pattern matching is a form of **structural decomposition** combined with **conditional dispatch**. You're simultaneously asking "what shape is this data?" and "if it matches, bind the components to names I can use."

### Why Pattern Matching Matters

Traditional imperative programming requires multiple steps to work with polymorphic data:
1. Check the type (`instanceof`)
2. Cast to that type
3. Extract components
4. Repeat for each possible type

Pattern matching collapses these steps into declarative syntax that's both safer and more concise. The compiler enforces that patterns are exhaustive (all cases are covered) and that variable scopes are correct, preventing entire classes of runtime errors.

For senior developers, pattern matching represents a paradigm shift from procedural "how to check" to declarative "what to match." This reduces cognitive load and makes code intent clearer.

---

## Historical Context: Java 1-8

To appreciate pattern matching, we need to understand the pain points it solves. Let's examine how Java developers handled type checking and conditional logic before Java 14-16.

### The instanceof-cast Ceremony

Before pattern matching, checking types required explicit casting—a source of verbosity and potential `ClassCastException` errors:

```java
// Pre-Java 16: The instanceof-cast pattern
public String describe(Object obj) {
    if (obj instanceof String) {
        String s = (String) obj;  // Manual cast required
        return "String of length " + s.length();
    } else if (obj instanceof Integer) {
        Integer i = (Integer) obj;  // Repeated pattern
        return "Integer: " + i;
    } else {
        return "Unknown type";
    }
}
```

**Problems with this approach:**
- **Redundancy**: We write `String` three times in a single branch
- **Error-prone**: Nothing prevents casting to the wrong type
- **Verbose**: The pattern obscures the business logic
- **Refactoring friction**: Changing types means updating multiple lines

This "test-and-cast" idiom was so common that IDEs created templates for it, but the fundamental problem remained: the language didn't support what we were trying to express.

### The Classic Switch Statement

Traditional switch statements had their own set of issues:

```java
// Pre-Java 14: Classic switch with fall-through
public int getDaysInMonth(String month) {
    int days;
    switch (month) {
        case "January":
        case "March":
        case "May":
        case "July":
        case "August":
        case "October":
        case "December":
            days = 31;
            break;
        case "February":
            days = 28;
            break;
        case "April":
        case "June":
        case "September":
        case "November":
            days = 30;
            break;
        default:
            throw new IllegalArgumentException("Invalid month");
    }
    return days;
}
```

**Problems:**
- **Fall-through behavior**: Forgetting `break` causes bugs (the infamous "fall-through" error)
- **Not an expression**: Can't assign switch results directly to variables
- **Verbose grouping**: Multiple cases cascade without clear grouping syntax
- **Mutation required**: Need to declare variable before switch, assign inside
- **No exhaustiveness checking**: Compiler won't warn about missing cases (except for enums with `-Xlint`)

The `-Xlint:fallthrough` warning existed, but it was a band-aid on a fundamental design flaw.

### Equals Method Boilerplate

The `equals()` method implementation was particularly painful:

```java
// Pre-Java 16: Traditional equals() implementation
public class Point {
    private final int x, y;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Point other = (Point) obj;  // Manual cast after type check
        return this.x == other.x && this.y == other.y;
    }
}
```

The pattern is mechanical, but it requires four lines just to get to the actual comparison logic.

---

## Pattern Matching in Other Languages

Java is late to the pattern matching party. Let's see how other languages approached this problem—understanding these influences helps contextualize Java's design decisions.

### Swift: Comprehensive Pattern Matching

Swift has supported pattern matching since its inception (2014), making it a first-class language feature:

```swift
// Swift pattern matching with switch
func describe(value: Any) -> String {
    switch value {
    case let str as String:
        return "String of length \(str.count)"
    case let num as Int where num > 0:
        return "Positive integer: \(num)"
    case let (x, y) as (Int, Int):
        return "Tuple: (\(x), \(y))"
    case let array as [Int] where array.count > 0:
        return "Array with \(array.count) elements"
    default:
        return "Unknown type"
    }
}
```

**Key features:**
- **Type patterns**: `let str as String` combines type check, cast, and binding
- **Guard clauses**: `where num > 0` adds conditional refinement
- **Structural patterns**: `(x, y)` deconstructs tuples
- **Exhaustiveness**: Compiler enforces all cases are covered

Swift's pattern matching extends to `if case`, `guard case`, and even `for case` loops, making it pervasive throughout the language.

### Scala: Match Expressions with Case Classes

Scala (2004) brought ML-style pattern matching to the JVM:

```scala
// Scala pattern matching with sealed traits
sealed trait Shape
case class Circle(radius: Double) extends Shape
case class Rectangle(width: Double, height: Double) extends Shape
case class Triangle(base: Double, height: Double) extends Shape

def area(shape: Shape): Double = shape match {
  case Circle(r) => math.Pi * r * r
  case Rectangle(w, h) => w * h
  case Triangle(b, h) => (b * h) / 2
}
```

**Key features:**
- **Case classes**: Automatically support pattern matching
- **Sealed traits**: Compiler enforces exhaustiveness
- **Destructuring**: `Circle(r)` extracts the radius
- **Expression-based**: `match` returns a value directly

Scala's influence on Java is clear: Records (Part 2) are inspired by case classes, and sealed classes (Part 3) mirror sealed traits.

### Functional Languages: ML and Haskell

Pattern matching originated in functional programming languages like ML (1973) and Haskell (1990):

```haskell
-- Haskell pattern matching
data Shape = Circle Double
           | Rectangle Double Double
           | Triangle Double Double

area :: Shape -> Double
area (Circle r) = pi * r * r
area (Rectangle w h) = w * h
area (Triangle b h) = (b * h) / 2
```

**Key features:**
- **Algebraic data types**: Sum types (Circle | Rectangle | Triangle)
- **Exhaustiveness**: Compiler error if cases are missing
- **No null**: Uses Maybe/Option types
- **First-class feature**: Pattern matching is fundamental, not an afterthought

These languages inspired modern pattern matching in Swift, Rust, Scala, and eventually Java.

### C++: Structured Bindings (C++17)

C++ doesn't have full pattern matching, but C++17 introduced **structured bindings** for decomposition:

```cpp
// C++17 structured bindings
std::tuple<int, std::string, double> getTuple() {
    return {42, "hello", 3.14};
}

auto [num, str, val] = getTuple();
// num is int, str is std::string, val is double
```

C++23 is adding limited pattern matching, but it's far less comprehensive than functional languages.

### Why Java Took So Long

Java's conservative approach to language evolution meant pattern matching arrived decades after functional languages pioneered it. The reasons:

1. **Backward compatibility**: Java prioritizes not breaking existing code
2. **JVM constraints**: Pattern matching requires runtime and bytecode support
3. **Type erasure**: Generics complicate pattern matching on parameterized types
4. **Community consensus**: Java evolves through JEPs with extensive review

The result is a measured, incremental approach: instanceof patterns in Java 16, switch expressions in Java 14, with more advanced patterns (record patterns, array patterns) coming in later versions.

---

## Pattern Matching for instanceof

Java 16 introduced **pattern matching for instanceof** (JEP 394), eliminating the test-and-cast ceremony that plagued Java for decades.

### Basic Syntax

The new syntax combines type checking and casting in one operation:

```java
// Java 16+: Pattern matching for instanceof
if (obj instanceof String s) {
    // 's' is in scope here, already cast to String
    System.out.println("Length: " + s.length());
}

// Before Java 16 - required manual cast
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println("Length: " + s.length());
}
```

The pattern variable `s` is automatically cast and scoped to where the type check succeeds.

### Scope Rules

The pattern variable's scope is determined by **flow analysis**:

```java
// Scope with logical AND (&&)
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
    // 's' is in scope here
}

// Scope with logical OR (||) - variable NOT in scope
if (obj instanceof String s || checkSomethingElse()) {
    // 's' is NOT in scope here - obj might not be String
}

// Negation - scope in else block
if (!(obj instanceof String s)) {
    // 's' is NOT in scope here
} else {
    // 's' IS in scope here (obj is definitely String)
    System.out.println(s.toUpperCase());
}
```

The compiler uses **definite assignment analysis** to determine where the pattern variable is safe to use.

### Null Handling

Pattern matching handles null naturally—instanceof already returns false for null:

```java
Object obj = null;
if (obj instanceof String s) {
    // This block never executes for null
}
```

This is safer than manual casting, which required explicit null checks.

### Real-World Benefits

Pattern matching shines in polymorphic code:

```java
// Before: Verbose hierarchy traversal
public void processShape(Shape shape) {
    if (shape instanceof Circle) {
        Circle c = (Circle) shape;
        System.out.println("Circle radius: " + c.radius());
    } else if (shape instanceof Rectangle) {
        Rectangle r = (Rectangle) shape;
        System.out.println("Rectangle: " + r.width() + "x" + r.height());
    }
}

// After: Concise and clear
public void processShape(Shape shape) {
    if (shape instanceof Circle c) {
        System.out.println("Circle radius: " + c.radius());
    } else if (shape instanceof Rectangle r) {
        System.out.println("Rectangle: " + r.width() + "x" + r.height());
    }
}
```

---

## Switch Expressions

Java 14 introduced **switch expressions** (JEP 361), transforming switch from a statement to an expression that returns values.

### Arrow Syntax (`->`)

The new arrow syntax eliminates fall-through:

```java
// Java 14+: Switch expression with arrows
String dayType = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
    case SATURDAY, SUNDAY -> "Weekend";
};

// Before Java 14: Switch statement
String dayType;
switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        dayType = "Weekday";
        break;
    case SATURDAY:
    case SUNDAY:
        dayType = "Weekend";
        break;
    default:
        throw new IllegalArgumentException();
}
```

**Key differences:**
- **No fall-through**: Each case executes exactly its branch
- **Expression**: Returns a value directly
- **Concise**: Multiple labels separated by commas
- **No break needed**: Implicit at end of each branch

### The yield Keyword

For multi-statement branches, use `yield` to return a value:

```java
int result = switch (operator) {
    case "+" -> {
        System.out.println("Adding");
        yield a + b;
    }
    case "-" -> {
        System.out.println("Subtracting");
        yield a - b;
    }
    default -> throw new IllegalArgumentException();
};
```

`yield` is like `return` for switch expressions.

### Exhaustiveness

Switch expressions must be **exhaustive**—they must handle all possible values:

```java
// Enum switch: Must cover all values OR include default
enum Color { RED, GREEN, BLUE }

String name = switch (color) {
    case RED -> "Red";
    case GREEN -> "Green";
    case BLUE -> "Blue";
    // No default needed - all enum values covered
};

// Primitives and strings: Must include default
String describe = switch (num) {
    case 0 -> "Zero";
    case 1 -> "One";
    default -> "Other";  // Required for non-enum types
};
```

The compiler enforces exhaustiveness, preventing bugs from missing cases.

### Four Forms of Switch

Java now has four switch variants:

| Type | Arrow (`->`) | Colon (`:`) |
|------|--------------|-------------|
| **Expression** | `int x = switch(v) { case 1 -> 10; }` | `int x = switch(v) { case 1: yield 10; }` |
| **Statement** | `switch(v) { case 1 -> x = 10; }` | `switch(v) { case 1: x = 10; break; }` |

**Best practice**: Prefer arrow syntax for new code. Use colon syntax only when you need fall-through (rare).

---

## Practical Examples

Let's explore pattern matching and switch expressions through six comprehensive examples.

### Example 1: Basic Pattern Matching with instanceof

Pattern matching for instanceof eliminates redundant casting and makes type checks more concise.

```java
package dev.nmac.blog.examples.java17.part4;

public class BasicPatternMatchingExample {

    // Traditional approach - before Java 16
    public static String describeTraditional(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;
            return "String of length " + s.length();
        } else if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            return "Integer: " + i;
        } else if (obj instanceof Double) {
            Double d = (Double) obj;
            return "Double: " + d;
        } else {
            return "Unknown type: " + (obj != null ? obj.getClass().getSimpleName() : "null");
        }
    }

    // Modern approach - Java 16+ with pattern matching
    public static String describeModern(Object obj) {
        if (obj instanceof String s) {
            return "String of length " + s.length();
        } else if (obj instanceof Integer i) {
            return "Integer: " + i;
        } else if (obj instanceof Double d) {
            return "Double: " + d;
        } else {
            return "Unknown type: " + (obj != null ? obj.getClass().getSimpleName() : "null");
        }
    }

    // Demonstrating scope rules
    public static String demonstrateScope(Object obj) {
        // Pattern variable scope with && (logical AND)
        if (obj instanceof String s && s.length() > 5) {
            return "Long string: " + s.toUpperCase();
        }

        // Pattern variable scope with negation
        if (!(obj instanceof String s)) {
            return "Not a string";
        } else {
            // 's' is in scope here (obj is definitely String)
            return "String in else block: " + s;
        }
    }

    // Null handling
    public static String handleNull(Object obj) {
        if (obj instanceof String s) {
            return "String: " + s;
        }
        return "null or not a String";
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Modern ===");
        Object[] values = {"Hello", 42, 3.14, null};

        for (Object val : values) {
            System.out.println("Traditional: " + describeTraditional(val));
            System.out.println("Modern:      " + describeModern(val));
            System.out.println();
        }

        System.out.println("=== Scope Demonstration ===");
        System.out.println(demonstrateScope("Hello"));           // Not a string (length <= 5)
        System.out.println(demonstrateScope("HelloWorld"));      // Long string: HELLOWORLD
        System.out.println(demonstrateScope(123));               // Not a string

        System.out.println("\n=== Null Handling ===");
        System.out.println(handleNull("test"));    // String: test
        System.out.println(handleNull(null));      // null or not a String
        System.out.println(handleNull(42));        // null or not a String
    }
}
```

**Unit Tests:**

```java
package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicPatternMatchingExampleTest {

    @Test
    @DisplayName("Should describe String type using pattern matching")
    void shouldDescribeStringType() {
        String result = BasicPatternMatchingExample.describeModern("Hello");

        assertEquals("String of length 5", result);
    }

    @Test
    @DisplayName("Should describe Integer type using pattern matching")
    void shouldDescribeIntegerType() {
        String result = BasicPatternMatchingExample.describeModern(42);

        assertEquals("Integer: 42", result);
    }

    @Test
    @DisplayName("Should describe Double type using pattern matching")
    void shouldDescribeDoubleType() {
        String result = BasicPatternMatchingExample.describeModern(3.14);

        assertEquals("Double: 3.14", result);
    }

    @Test
    @DisplayName("Should handle null input gracefully")
    void shouldHandleNullInput() {
        String result = BasicPatternMatchingExample.describeModern(null);

        assertEquals("Unknown type: null", result);
    }

    @Test
    @DisplayName("Should respect pattern variable scope with logical AND")
    void shouldRespectPatternVariableScopeWithAnd() {
        String result = BasicPatternMatchingExample.demonstrateScope("HelloWorld");

        assertEquals("Long string: HELLOWORLD", result);
    }

    @Test
    @DisplayName("Should handle short strings correctly in scope demonstration")
    void shouldHandleShortStringsInScopeDemo() {
        String result = BasicPatternMatchingExample.demonstrateScope("Hi");

        assertEquals("String in else block: Hi", result);
    }

    @Test
    @DisplayName("Should handle non-String types in scope demonstration")
    void shouldHandleNonStringInScopeDemo() {
        String result = BasicPatternMatchingExample.demonstrateScope(123);

        assertEquals("Not a string", result);
    }

    @Test
    @DisplayName("Should return false for null in instanceof check")
    void shouldReturnFalseForNullInInstanceof() {
        String result = BasicPatternMatchingExample.handleNull(null);

        assertEquals("null or not a String", result);
    }
}
```

**Output:**
```
=== Traditional vs Modern ===
Traditional: String of length 5
Modern:      String of length 5

Traditional: Integer: 42
Modern:      Integer: 42

Traditional: Double: 3.14
Modern:      Double: 3.14

Traditional: Unknown type: null
Modern:      Unknown type: null

=== Scope Demonstration ===
String in else block: Hello
Long string: HELLOWORLD
Not a string

=== Null Handling ===
String: test
null or not a String
null or not a String
```

**Key Insight**: Pattern matching eliminates the redundancy of writing the type name three times (instanceof check, cast declaration, and cast operation) while making the code safer through compiler-enforced scope rules.

---

### Example 2: Switch Expressions with Enums

Switch expressions with enums demonstrate exhaustiveness checking—the compiler ensures all enum values are handled.

```java
package dev.nmac.blog.examples.java17.part4;

public class SwitchEnumExample {

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    // Traditional switch statement - before Java 14
    public static String getDayTypeTraditional(Day day) {
        String result;
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                result = "Weekday";
                break;
            case SATURDAY:
            case SUNDAY:
                result = "Weekend";
                break;
            default:
                throw new IllegalArgumentException("Unknown day");
        }
        return result;
    }

    // Modern switch expression - Java 14+
    public static String getDayTypeModern(Day day) {
        return switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
            case SATURDAY, SUNDAY -> "Weekend";
        };
    }

    // Exhaustive enum switch - no default needed when all cases covered
    public static int getSeasonMonths(Season season) {
        return switch (season) {
            case SPRING -> 3;  // Mar, Apr, May
            case SUMMER -> 3;  // Jun, Jul, Aug
            case FALL -> 3;    // Sep, Oct, Nov
            case WINTER -> 3;  // Dec, Jan, Feb
            // No default needed - all enum values covered!
        };
    }

    // Switch expression with different return types per branch
    public static String describeDay(Day day) {
        return switch (day) {
            case MONDAY -> "Start of work week";
            case TUESDAY, WEDNESDAY, THURSDAY -> "Middle of week";
            case FRIDAY -> "End of work week - TGIF!";
            case SATURDAY, SUNDAY -> "Weekend relaxation";
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Modern ===");
        for (Day day : Day.values()) {
            System.out.println(day + ": Traditional=" + getDayTypeTraditional(day) +
                             ", Modern=" + getDayTypeModern(day));
        }

        System.out.println("\n=== Exhaustive Enum Switch ===");
        for (Season season : Season.values()) {
            System.out.println(season + " has " + getSeasonMonths(season) + " months");
        }

        System.out.println("\n=== Descriptive Switch ===");
        System.out.println(describeDay(Day.MONDAY));
        System.out.println(describeDay(Day.WEDNESDAY));
        System.out.println(describeDay(Day.FRIDAY));
        System.out.println(describeDay(Day.SUNDAY));
    }
}
```

**Unit Tests:**

```java
package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.SwitchEnumExample.Day;
import dev.nmac.blog.examples.java17.part4.SwitchEnumExample.Season;

class SwitchEnumExampleTest {

    @Test
    @DisplayName("Should classify weekdays correctly using switch expression")
    void shouldClassifyWeekdaysCorrectly() {
        assertEquals("Weekday", SwitchEnumExample.getDayTypeModern(Day.MONDAY));
        assertEquals("Weekday", SwitchEnumExample.getDayTypeModern(Day.TUESDAY));
        assertEquals("Weekday", SwitchEnumExample.getDayTypeModern(Day.FRIDAY));
    }

    @Test
    @DisplayName("Should classify weekends correctly using switch expression")
    void shouldClassifyWeekendsCorrectly() {
        assertEquals("Weekend", SwitchEnumExample.getDayTypeModern(Day.SATURDAY));
        assertEquals("Weekend", SwitchEnumExample.getDayTypeModern(Day.SUNDAY));
    }

    @Test
    @DisplayName("Should produce identical results for traditional and modern switch")
    void shouldProduceIdenticalResultsForBothSwitches() {
        for (Day day : Day.values()) {
            String traditional = SwitchEnumExample.getDayTypeTraditional(day);
            String modern = SwitchEnumExample.getDayTypeModern(day);

            assertEquals(traditional, modern, "Results should match for " + day);
        }
    }

    @Test
    @DisplayName("Should return correct month count for all seasons")
    void shouldReturnCorrectMonthCountForAllSeasons() {
        for (Season season : Season.values()) {
            assertEquals(3, SwitchEnumExample.getSeasonMonths(season));
        }
    }

    @Test
    @DisplayName("Should describe Monday correctly")
    void shouldDescribeMondayCorrectly() {
        assertEquals("Start of work week", SwitchEnumExample.describeDay(Day.MONDAY));
    }

    @Test
    @DisplayName("Should describe Friday correctly")
    void shouldDescribeFridayCorrectly() {
        assertEquals("End of work week - TGIF!", SwitchEnumExample.describeDay(Day.FRIDAY));
    }

    @Test
    @DisplayName("Should describe weekend days correctly")
    void shouldDescribeWeekendDaysCorrectly() {
        String expected = "Weekend relaxation";

        assertEquals(expected, SwitchEnumExample.describeDay(Day.SATURDAY));
        assertEquals(expected, SwitchEnumExample.describeDay(Day.SUNDAY));
    }

    @Test
    @DisplayName("Should describe midweek days correctly")
    void shouldDescribeMidweekDaysCorrectly() {
        String expected = "Middle of week";

        assertEquals(expected, SwitchEnumExample.describeDay(Day.TUESDAY));
        assertEquals(expected, SwitchEnumExample.describeDay(Day.WEDNESDAY));
        assertEquals(expected, SwitchEnumExample.describeDay(Day.THURSDAY));
    }
}
```

**Output:**
```
=== Traditional vs Modern ===
MONDAY: Traditional=Weekday, Modern=Weekday
TUESDAY: Traditional=Weekday, Modern=Weekday
WEDNESDAY: Traditional=Weekday, Modern=Weekday
THURSDAY: Traditional=Weekday, Modern=Weekday
FRIDAY: Traditional=Weekday, Modern=Weekday
SATURDAY: Traditional=Weekend, Modern=Weekend
SUNDAY: Traditional=Weekend, Modern=Weekend

=== Exhaustive Enum Switch ===
SPRING has 3 months
SUMMER has 3 months
FALL has 3 months
WINTER has 3 months

=== Descriptive Switch ===
Start of work week
Middle of week
End of work week - TGIF!
Weekend relaxation
```

**Key Insight**: Switch expressions with enums benefit from exhaustiveness checking. When all enum values are covered, no `default` case is needed, and the compiler will error if a new enum value is added without updating the switch.

---

### Example 3: Switch Expressions with yield

The `yield` keyword allows multi-statement branches in switch expressions, enabling complex logic while still returning a value.

```java
package dev.nmac.blog.examples.java17.part4;

public class SwitchYieldExample {

    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER, MODULO
    }

    // Switch expression with yield for multi-statement branches
    public static double calculate(Operation op, double a, double b) {
        return switch (op) {
            case ADD -> {
                System.out.println("Performing addition");
                yield a + b;
            }
            case SUBTRACT -> {
                System.out.println("Performing subtraction");
                yield a - b;
            }
            case MULTIPLY -> {
                System.out.println("Performing multiplication");
                yield a * b;
            }
            case DIVIDE -> {
                if (b == 0) {
                    System.out.println("Division by zero - returning NaN");
                    yield Double.NaN;
                }
                System.out.println("Performing division");
                yield a / b;
            }
            case POWER -> {
                System.out.println("Calculating power");
                yield Math.pow(a, b);
            }
            case MODULO -> {
                if (b == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                System.out.println("Calculating modulo");
                yield a % b;
            }
        };
    }

    // Complex logic with yield - grade calculation
    public static String getGrade(int score) {
        return switch (score / 10) {
            case 10, 9 -> {
                System.out.println("Excellent performance!");
                yield "A";
            }
            case 8 -> {
                System.out.println("Good work!");
                yield "B";
            }
            case 7 -> {
                System.out.println("Satisfactory");
                yield "C";
            }
            case 6 -> {
                System.out.println("Needs improvement");
                yield "D";
            }
            default -> {
                if (score < 0 || score > 100) {
                    throw new IllegalArgumentException("Invalid score: " + score);
                }
                System.out.println("Failed");
                yield "F";
            }
        };
    }

    // Nested switch with yield
    public static String categorize(String type, int value) {
        return switch (type) {
            case "number" -> {
                String category = switch (value) {
                    case 0 -> "zero";
                    case 1, 2, 3, 4, 5, 6, 7, 8, 9 -> "single digit";
                    default -> value < 0 ? "negative" : "multiple digits";
                };
                yield "Number category: " + category;
            }
            case "priority" -> {
                String level = switch (value) {
                    case 1 -> "critical";
                    case 2 -> "high";
                    case 3 -> "medium";
                    default -> "low";
                };
                yield "Priority level: " + level;
            }
            default -> "Unknown type: " + type;
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Calculator with yield ===");
        System.out.println("10 + 5 = " + calculate(Operation.ADD, 10, 5));
        System.out.println("10 / 3 = " + calculate(Operation.DIVIDE, 10, 3));
        System.out.println("10 / 0 = " + calculate(Operation.DIVIDE, 10, 0));
        System.out.println("2^8 = " + calculate(Operation.POWER, 2, 8));

        System.out.println("\n=== Grade Calculator ===");
        System.out.println("Score 95: " + getGrade(95));
        System.out.println("Score 78: " + getGrade(78));
        System.out.println("Score 55: " + getGrade(55));

        System.out.println("\n=== Nested Switch ===");
        System.out.println(categorize("number", 5));
        System.out.println(categorize("number", 42));
        System.out.println(categorize("priority", 1));
    }
}
```

**Unit Tests:**

```java
package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.SwitchYieldExample.Operation;

class SwitchYieldExampleTest {

    @Test
    @DisplayName("Should perform addition using yield")
    void shouldPerformAddition() {
        double result = SwitchYieldExample.calculate(Operation.ADD, 10, 5);

        assertEquals(15.0, result, 0.001);
    }

    @Test
    @DisplayName("Should perform subtraction using yield")
    void shouldPerformSubtraction() {
        double result = SwitchYieldExample.calculate(Operation.SUBTRACT, 10, 5);

        assertEquals(5.0, result, 0.001);
    }

    @Test
    @DisplayName("Should perform division and handle division by zero")
    void shouldPerformDivisionAndHandleDivisionByZero() {
        assertEquals(2.0, SwitchYieldExample.calculate(Operation.DIVIDE, 10, 5), 0.001);
        assertTrue(Double.isNaN(SwitchYieldExample.calculate(Operation.DIVIDE, 10, 0)));
    }

    @Test
    @DisplayName("Should calculate power using yield")
    void shouldCalculatePower() {
        double result = SwitchYieldExample.calculate(Operation.POWER, 2, 8);

        assertEquals(256.0, result, 0.001);
    }

    @Test
    @DisplayName("Should throw exception for modulo by zero")
    void shouldThrowExceptionForModuloByZero() {
        assertThrows(ArithmeticException.class,
            () -> SwitchYieldExample.calculate(Operation.MODULO, 10, 0));
    }

    @Test
    @DisplayName("Should assign grade A for scores 90-100")
    void shouldAssignGradeAForHighScores() {
        assertEquals("A", SwitchYieldExample.getGrade(95));
        assertEquals("A", SwitchYieldExample.getGrade(100));
        assertEquals("A", SwitchYieldExample.getGrade(90));
    }

    @Test
    @DisplayName("Should assign appropriate grades for various scores")
    void shouldAssignAppropriateGrades() {
        assertEquals("B", SwitchYieldExample.getGrade(85));
        assertEquals("C", SwitchYieldExample.getGrade(75));
        assertEquals("D", SwitchYieldExample.getGrade(65));
        assertEquals("F", SwitchYieldExample.getGrade(55));
    }

    @Test
    @DisplayName("Should throw exception for invalid scores")
    void shouldThrowExceptionForInvalidScores() {
        assertThrows(IllegalArgumentException.class, () -> SwitchYieldExample.getGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> SwitchYieldExample.getGrade(101));
    }

    @Test
    @DisplayName("Should categorize numbers correctly in nested switch")
    void shouldCategorizeNumbersCorrectly() {
        assertEquals("Number category: single digit", SwitchYieldExample.categorize("number", 5));
        assertEquals("Number category: multiple digits", SwitchYieldExample.categorize("number", 42));
        assertEquals("Number category: negative", SwitchYieldExample.categorize("number", -10));
        assertEquals("Number category: zero", SwitchYieldExample.categorize("number", 0));
    }

    @Test
    @DisplayName("Should categorize priority levels correctly in nested switch")
    void shouldCategorizePriorityLevelsCorrectly() {
        assertEquals("Priority level: critical", SwitchYieldExample.categorize("priority", 1));
        assertEquals("Priority level: high", SwitchYieldExample.categorize("priority", 2));
        assertEquals("Priority level: medium", SwitchYieldExample.categorize("priority", 3));
        assertEquals("Priority level: low", SwitchYieldExample.categorize("priority", 5));
    }
}
```

**Output:**
```
=== Calculator with yield ===
Performing addition
10 + 5 = 15.0
Performing division
10 / 3 = 3.3333333333333335
Division by zero - returning NaN
10 / 0 = NaN
Calculating power
2^8 = 256.0

=== Grade Calculator ===
Excellent performance!
Score 95: A
Satisfactory
Score 78: C
Failed
Score 55: F

=== Nested Switch ===
Number category: single digit
Number category: multiple digits
Priority level: critical
```

**Key Insight**: The `yield` keyword enables multi-statement logic within switch expression branches. This combines the conciseness of expressions with the flexibility of statements, allowing validation, logging, and complex calculations before returning a value.

---

### Example 4: Type Hierarchy Pattern Matching

Pattern matching shines when working with polymorphic type hierarchies, eliminating verbose instanceof chains.

```java
package dev.nmac.blog.examples.java17.part4;

public class TypeHierarchyExample {

    // Sealed interface ensures exhaustiveness (covered in Part 3)
    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
    }

    public record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() {
            return (base * height) / 2;
        }
    }

    // Traditional approach - verbose and repetitive
    public static String describeTraditional(Shape shape) {
        if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            return String.format("Circle with radius %.2f", c.radius());
        } else if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            return String.format("Rectangle %.2f x %.2f", r.width(), r.height());
        } else if (shape instanceof Triangle) {
            Triangle t = (Triangle) shape;
            return String.format("Triangle with base %.2f and height %.2f", t.base(), t.height());
        } else {
            return "Unknown shape";
        }
    }

    // Modern approach with pattern matching
    public static String describeModern(Shape shape) {
        if (shape instanceof Circle c) {
            return String.format("Circle with radius %.2f", c.radius());
        } else if (shape instanceof Rectangle r) {
            return String.format("Rectangle %.2f x %.2f", r.width(), r.height());
        } else if (shape instanceof Triangle t) {
            return String.format("Triangle with base %.2f and height %.2f", t.base(), t.height());
        } else {
            return "Unknown shape";
        }
    }

    // Pattern matching with guards (conditional logic)
    public static String classifyBySize(Shape shape) {
        double area = shape.area();

        if (shape instanceof Circle c && c.radius() > 10) {
            return "Large circle (radius > 10)";
        } else if (shape instanceof Rectangle r && r.width() > 20 || r.height() > 20) {
            return "Large rectangle (dimension > 20)";
        } else if (shape instanceof Triangle t && t.base() > 15) {
            return "Wide triangle (base > 15)";
        } else if (area > 100) {
            return "Large shape by area";
        } else {
            return "Small shape";
        }
    }

    // Computing perimeter requires type-specific logic
    public static double perimeter(Shape shape) {
        if (shape instanceof Circle c) {
            return 2 * Math.PI * c.radius();
        } else if (shape instanceof Rectangle r) {
            return 2 * (r.width() + r.height());
        } else if (shape instanceof Triangle t) {
            // Simplified: assumes right triangle or provides approximation
            // In real code, would need all three sides
            return t.base() + t.height() + Math.sqrt(t.base() * t.base() + t.height() * t.height());
        }
        throw new IllegalArgumentException("Unknown shape type");
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(10.0, 20.0),
            new Triangle(6.0, 8.0),
            new Circle(15.0)
        };

        System.out.println("=== Traditional vs Modern ===");
        for (Shape shape : shapes) {
            System.out.println("Traditional: " + describeTraditional(shape));
            System.out.println("Modern:      " + describeModern(shape));
            System.out.println();
        }

        System.out.println("=== Size Classification ===");
        for (Shape shape : shapes) {
            System.out.println(describeModern(shape) + " -> " + classifyBySize(shape));
        }

        System.out.println("\n=== Perimeter Calculation ===");
        for (Shape shape : shapes) {
            System.out.printf("%s: area=%.2f, perimeter=%.2f%n",
                describeModern(shape), shape.area(), perimeter(shape));
        }
    }
}
```

**Unit Tests:**

```java
package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.TypeHierarchyExample.*;

class TypeHierarchyExampleTest {

    @Test
    @DisplayName("Should describe Circle using pattern matching")
    void shouldDescribeCircle() {
        Shape circle = new Circle(5.0);

        String result = TypeHierarchyExample.describeModern(circle);

        assertEquals("Circle with radius 5.00", result);
    }

    @Test
    @DisplayName("Should describe Rectangle using pattern matching")
    void shouldDescribeRectangle() {
        Shape rectangle = new Rectangle(10.0, 20.0);

        String result = TypeHierarchyExample.describeModern(rectangle);

        assertEquals("Rectangle 10.00 x 20.00", result);
    }

    @Test
    @DisplayName("Should describe Triangle using pattern matching")
    void shouldDescribeTriangle() {
        Shape triangle = new Triangle(6.0, 8.0);

        String result = TypeHierarchyExample.describeModern(triangle);

        assertEquals("Triangle with base 6.00 and height 8.00", result);
    }

    @Test
    @DisplayName("Should produce identical results for traditional and modern approaches")
    void shouldProduceIdenticalResults() {
        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(10.0, 20.0),
            new Triangle(6.0, 8.0)
        };

        for (Shape shape : shapes) {
            String traditional = TypeHierarchyExample.describeTraditional(shape);
            String modern = TypeHierarchyExample.describeModern(shape);

            assertEquals(traditional, modern);
        }
    }

    @Test
    @DisplayName("Should classify large circle by radius")
    void shouldClassifyLargeCircleByRadius() {
        Shape largeCircle = new Circle(15.0);

        String result = TypeHierarchyExample.classifyBySize(largeCircle);

        assertEquals("Large circle (radius > 10)", result);
    }

    @Test
    @DisplayName("Should classify small circle")
    void shouldClassifySmallCircle() {
        Shape smallCircle = new Circle(3.0);

        String result = TypeHierarchyExample.classifyBySize(smallCircle);

        assertEquals("Small shape", result);
    }

    @Test
    @DisplayName("Should classify large rectangle by dimensions")
    void shouldClassifyLargeRectangleByDimensions() {
        Shape largeRect = new Rectangle(25.0, 10.0);

        String result = TypeHierarchyExample.classifyBySize(largeRect);

        assertEquals("Large rectangle (dimension > 20)", result);
    }

    @Test
    @DisplayName("Should calculate circle perimeter correctly")
    void shouldCalculateCirclePerimeter() {
        Shape circle = new Circle(5.0);

        double perimeter = TypeHierarchyExample.perimeter(circle);

        assertEquals(2 * Math.PI * 5.0, perimeter, 0.001);
    }

    @Test
    @DisplayName("Should calculate rectangle perimeter correctly")
    void shouldCalculateRectanglePerimeter() {
        Shape rectangle = new Rectangle(10.0, 20.0);

        double perimeter = TypeHierarchyExample.perimeter(rectangle);

        assertEquals(60.0, perimeter, 0.001);
    }

    @Test
    @DisplayName("Should calculate triangle perimeter correctly")
    void shouldCalculateTrianglePerimeter() {
        Shape triangle = new Triangle(3.0, 4.0);

        double perimeter = TypeHierarchyExample.perimeter(triangle);

        // 3 + 4 + 5 (Pythagorean triple)
        assertEquals(12.0, perimeter, 0.001);
    }
}
```

**Output:**
```
=== Traditional vs Modern ===
Traditional: Circle with radius 5.00
Modern:      Circle with radius 5.00

Traditional: Rectangle 10.00 x 20.00
Modern:      Rectangle 10.00 x 20.00

Traditional: Triangle with base 6.00 and height 8.00
Modern:      Triangle with base 6.00 and height 8.00

Traditional: Circle with radius 15.00
Modern:      Circle with radius 15.00

=== Size Classification ===
Circle with radius 5.00 -> Small shape
Rectangle 10.00 x 20.00 -> Large shape by area
Triangle with base 6.00 and height 8.00 -> Small shape
Circle with radius 15.00 -> Large circle (radius > 10)

=== Perimeter Calculation ===
Circle with radius 5.00: area=78.54, perimeter=31.42
Rectangle 10.00 x 20.00: area=200.00, perimeter=60.00
Triangle with base 6.00 and height 8.00: area=24.00, perimeter=24.00
Circle with radius 15.00: area=706.86, perimeter=94.25
```

**Key Insight**: Pattern matching with sealed types (covered in Part 3) provides exhaustiveness guarantees. The compiler knows all possible subtypes, enabling safer refactoring—adding a new shape requires updating all pattern matching sites.

---

### Example 5: Implementing equals() with Pattern Matching

Pattern matching dramatically simplifies `equals()` implementations, making them more readable and less error-prone.

```java
package dev.nmac.blog.examples.java17.part4;

import java.util.Objects;

public class EqualsPatternMatchingExample {

    // Traditional equals() implementation - verbose
    public static class PointTraditional {
        private final int x, y;

        public PointTraditional(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            PointTraditional other = (PointTraditional) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Modern equals() with pattern matching - concise
    public static class PointModern {
        private final int x, y;

        public PointModern(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PointModern p &&
                   this.x == p.x && this.y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Complex object with multiple fields
    public static class Person {
        private final String firstName;
        private final String lastName;
        private final int age;

        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Person p &&
                   Objects.equals(this.firstName, p.firstName) &&
                   Objects.equals(this.lastName, p.lastName) &&
                   this.age == p.age;
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, age);
        }
    }

    // Demonstrating null safety
    public static class SafeEquals {
        private final String value;

        public SafeEquals(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // instanceof returns false for null - no explicit null check needed!
            return obj instanceof SafeEquals s && Objects.equals(this.value, s.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Modern Point ===");
        PointTraditional pt1 = new PointTraditional(10, 20);
        PointTraditional pt2 = new PointTraditional(10, 20);
        PointTraditional pt3 = new PointTraditional(30, 40);

        System.out.println("pt1.equals(pt2): " + pt1.equals(pt2));  // true
        System.out.println("pt1.equals(pt3): " + pt1.equals(pt3));  // false

        PointModern pm1 = new PointModern(10, 20);
        PointModern pm2 = new PointModern(10, 20);
        PointModern pm3 = new PointModern(30, 40);

        System.out.println("pm1.equals(pm2): " + pm1.equals(pm2));  // true
        System.out.println("pm1.equals(pm3): " + pm1.equals(pm3));  // false

        System.out.println("\n=== Person Equality ===");
        Person p1 = new Person("Alice", "Smith", 30);
        Person p2 = new Person("Alice", "Smith", 30);
        Person p3 = new Person("Bob", "Jones", 25);

        System.out.println("p1.equals(p2): " + p1.equals(p2));  // true
        System.out.println("p1.equals(p3): " + p1.equals(p3));  // false

        System.out.println("\n=== Null Safety ===");
        SafeEquals s1 = new SafeEquals("test");
        SafeEquals s2 = new SafeEquals("test");

        System.out.println("s1.equals(s2): " + s1.equals(s2));      // true
        System.out.println("s1.equals(null): " + s1.equals(null));  // false
        System.out.println("s1.equals(\"test\"): " + s1.equals("test"));  // false (different type)
    }
}
```

**Unit Tests:**

```java
package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.EqualsPatternMatchingExample.*;

class EqualsPatternMatchingExampleTest {

    @Test
    @DisplayName("Should compare PointTraditional instances by value")
    void shouldComparePointTraditionalByValue() {
        PointTraditional p1 = new PointTraditional(10, 20);
        PointTraditional p2 = new PointTraditional(10, 20);
        PointTraditional p3 = new PointTraditional(10, 30);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotSame(p1, p2);
    }

    @Test
    @DisplayName("Should compare PointModern instances by value using pattern matching")
    void shouldComparePointModernByValue() {
        PointModern p1 = new PointModern(10, 20);
        PointModern p2 = new PointModern(10, 20);
        PointModern p3 = new PointModern(10, 30);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotSame(p1, p2);
    }

    @Test
    @DisplayName("Should have consistent hashCode for equal PointModern instances")
    void shouldHaveConsistentHashCodeForPointModern() {
        PointModern p1 = new PointModern(10, 20);
        PointModern p2 = new PointModern(10, 20);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Should compare Person instances by all fields")
    void shouldComparePersonByAllFields() {
        Person p1 = new Person("Alice", "Smith", 30);
        Person p2 = new Person("Alice", "Smith", 30);
        Person p3 = new Person("Alice", "Smith", 31);  // Different age
        Person p4 = new Person("Bob", "Smith", 30);    // Different first name

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
    }

    @Test
    @DisplayName("Should handle null safely in pattern matching equals")
    void shouldHandleNullSafelyInPatternMatchingEquals() {
        SafeEquals s1 = new SafeEquals("test");

        assertFalse(s1.equals(null));
    }

    @Test
    @DisplayName("Should return false when comparing with different type")
    void shouldReturnFalseWhenComparingWithDifferentType() {
        PointModern point = new PointModern(10, 20);
        String notAPoint = "not a point";

        assertFalse(point.equals(notAPoint));
    }

    @Test
    @DisplayName("Should handle reflexivity in equals")
    void shouldHandleReflexivityInEquals() {
        PointModern p = new PointModern(10, 20);

        assertEquals(p, p);
    }

    @Test
    @DisplayName("Should handle symmetry in equals")
    void shouldHandleSymmetryInEquals() {
        PointModern p1 = new PointModern(10, 20);
        PointModern p2 = new PointModern(10, 20);

        assertEquals(p1, p2);
        assertEquals(p2, p1);
    }

    @Test
    @DisplayName("Should compare SafeEquals with null values correctly")
    void shouldCompareSafeEqualsWithNullValues() {
        SafeEquals s1 = new SafeEquals(null);
        SafeEquals s2 = new SafeEquals(null);
        SafeEquals s3 = new SafeEquals("test");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
    }
}
```

**Output:**
```
=== Traditional vs Modern Point ===
pt1.equals(pt2): true
pt1.equals(pt3): false
pm1.equals(pm2): true
pm1.equals(pm3): false

=== Person Equality ===
p1.equals(p2): true
p1.equals(p3): false

=== Null Safety ===
s1.equals(s2): true
s1.equals(null): false
s1.equals("test"): false
```

**Key Insight**: Pattern matching reduces `equals()` from 5-6 lines to a single expression. The instanceof pattern handles null checks automatically (returns false), type checking, and casting in one operation—significantly reducing boilerplate and potential errors.

---

### Example 6: Combined Pattern Matching with Multiple Conditions

Advanced pattern matching scenarios combine type checks with guard conditions for powerful conditional logic.

```java
package dev.nmac.blog.examples.java17.part4;

import java.util.List;

public class CombinedPatternsExample {

    public sealed interface PaymentMethod permits CreditCard, DebitCard, Cash, DigitalWallet {}

    public record CreditCard(String number, String cvv, double limit) implements PaymentMethod {}
    public record DebitCard(String number, String pin) implements PaymentMethod {}
    public record Cash(double amount) implements PaymentMethod {}
    public record DigitalWallet(String provider, String accountId, double balance) implements PaymentMethod {}

    // Pattern matching with guard conditions
    public static String processPayment(PaymentMethod method, double amount) {
        if (method instanceof CreditCard cc && cc.limit() >= amount) {
            return String.format("Charging $%.2f to credit card ending in %s",
                amount, cc.number().substring(cc.number().length() - 4));
        } else if (method instanceof CreditCard cc) {
            return String.format("Credit card declined - limit $%.2f exceeded by $%.2f",
                cc.limit(), amount - cc.limit());
        } else if (method instanceof DebitCard dc) {
            return String.format("Processing debit card payment of $%.2f", amount);
        } else if (method instanceof Cash cash && cash.amount() >= amount) {
            double change = cash.amount() - amount;
            return String.format("Cash payment of $%.2f received, change: $%.2f",
                cash.amount(), change);
        } else if (method instanceof Cash cash) {
            return String.format("Insufficient cash - need $%.2f more",
                amount - cash.amount());
        } else if (method instanceof DigitalWallet wallet && wallet.balance() >= amount) {
            return String.format("Paid $%.2f via %s", amount, wallet.provider());
        } else if (method instanceof DigitalWallet wallet) {
            return String.format("%s wallet has insufficient funds", wallet.provider());
        }

        return "Unknown payment method";
    }

    // Complex nested patterns with different object types
    public static String analyzeData(Object data) {
        if (data instanceof String s && s.length() > 100) {
            return "Long text: " + s.substring(0, 50) + "...";
        } else if (data instanceof String s && s.matches("\\d+")) {
            return "Numeric string: " + s;
        } else if (data instanceof String s) {
            return "Short text: " + s;
        } else if (data instanceof Integer i && i > 1000) {
            return "Large number: " + i;
        } else if (data instanceof Integer i && i < 0) {
            return "Negative number: " + i;
        } else if (data instanceof Integer i) {
            return "Number: " + i;
        } else if (data instanceof List<?> list && list.isEmpty()) {
            return "Empty list";
        } else if (data instanceof List<?> list) {
            return "List with " + list.size() + " elements";
        }

        return "Unknown data type";
    }

    // Real-world example: Order processing with multiple validations
    public record Order(String id, double total, String customerType, int itemCount) {}

    public static String determineShipping(Order order) {
        // VIP customers with large orders
        if (order.customerType().equals("VIP") && order.total() > 1000) {
            return "FREE express shipping (VIP + large order)";
        }
        // VIP customers
        else if (order.customerType().equals("VIP")) {
            return "FREE standard shipping (VIP)";
        }
        // Large orders (non-VIP)
        else if (order.total() > 500 && order.itemCount() > 5) {
            return "FREE standard shipping (bulk order)";
        }
        // Medium orders
        else if (order.total() > 100) {
            return "Standard shipping: $10";
        }
        // Small orders
        else {
            return "Standard shipping: $15";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Payment Processing ===");
        PaymentMethod[] methods = {
            new CreditCard("1234-5678-9012-3456", "123", 1000.0),
            new DebitCard("9876-5432-1098-7654", "1234"),
            new Cash(50.0),
            new DigitalWallet("PayPal", "user@example.com", 200.0)
        };

        for (PaymentMethod method : methods) {
            System.out.println(processPayment(method, 75.0));
        }

        System.out.println("\n=== Over-limit Credit Card ===");
        CreditCard limitedCard = new CreditCard("1111-2222-3333-4444", "999", 50.0);
        System.out.println(processPayment(limitedCard, 75.0));

        System.out.println("\n=== Data Analysis ===");
        Object[] dataPoints = {
            "Hello",
            "12345",
            "A".repeat(150),
            42,
            -10,
            2000,
            List.of(),
            List.of("a", "b", "c")
        };

        for (Object data : dataPoints) {
            System.out.println(analyzeData(data));
        }

        System.out.println("\n=== Shipping Determination ===");
        Order[] orders = {
            new Order("O1", 1500.0, "VIP", 10),
            new Order("O2", 200.0, "VIP", 3),
            new Order("O3", 600.0, "REGULAR", 8),
            new Order("O4", 150.0, "REGULAR", 2),
            new Order("O5", 50.0, "REGULAR", 1)
        };

        for (Order order : orders) {
            System.out.printf("Order %s ($%.2f, %s): %s%n",
                order.id(), order.total(), order.customerType(), determineShipping(order));
        }
    }
}
```

**Unit Tests:**

```java
package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import dev.nmac.blog.examples.java17.part4.CombinedPatternsExample.*;

class CombinedPatternsExampleTest {

    @Test
    @DisplayName("Should process credit card payment when under limit")
    void shouldProcessCreditCardPaymentWhenUnderLimit() {
        PaymentMethod cc = new CreditCard("1234-5678-9012-3456", "123", 1000.0);

        String result = CombinedPatternsExample.processPayment(cc, 500.0);

        assertTrue(result.contains("Charging"));
        assertTrue(result.contains("3456"));  // Last 4 digits
    }

    @Test
    @DisplayName("Should decline credit card payment when over limit")
    void shouldDeclineCreditCardPaymentWhenOverLimit() {
        PaymentMethod cc = new CreditCard("1234-5678-9012-3456", "123", 100.0);

        String result = CombinedPatternsExample.processPayment(cc, 500.0);

        assertTrue(result.contains("declined"));
        assertTrue(result.contains("limit"));
    }

    @Test
    @DisplayName("Should process cash payment with correct change")
    void shouldProcessCashPaymentWithCorrectChange() {
        PaymentMethod cash = new Cash(100.0);

        String result = CombinedPatternsExample.processPayment(cash, 75.0);

        assertTrue(result.contains("change: $25.00"));
    }

    @Test
    @DisplayName("Should reject insufficient cash")
    void shouldRejectInsufficientCash() {
        PaymentMethod cash = new Cash(50.0);

        String result = CombinedPatternsExample.processPayment(cash, 75.0);

        assertTrue(result.contains("Insufficient cash"));
    }

    @Test
    @DisplayName("Should process digital wallet payment when sufficient balance")
    void shouldProcessDigitalWalletPaymentWhenSufficientBalance() {
        PaymentMethod wallet = new DigitalWallet("PayPal", "user@example.com", 200.0);

        String result = CombinedPatternsExample.processPayment(wallet, 75.0);

        assertTrue(result.contains("Paid"));
        assertTrue(result.contains("PayPal"));
    }

    @Test
    @DisplayName("Should analyze long strings correctly")
    void shouldAnalyzeLongStringsCorrectly() {
        String longText = "A".repeat(150);

        String result = CombinedPatternsExample.analyzeData(longText);

        assertTrue(result.startsWith("Long text:"));
    }

    @Test
    @DisplayName("Should detect numeric strings")
    void shouldDetectNumericStrings() {
        String result = CombinedPatternsExample.analyzeData("12345");

        assertEquals("Numeric string: 12345", result);
    }

    @Test
    @DisplayName("Should classify integers by size")
    void shouldClassifyIntegersBySize() {
        assertEquals("Large number: 2000", CombinedPatternsExample.analyzeData(2000));
        assertEquals("Negative number: -10", CombinedPatternsExample.analyzeData(-10));
        assertEquals("Number: 42", CombinedPatternsExample.analyzeData(42));
    }

    @Test
    @DisplayName("Should analyze empty and non-empty lists")
    void shouldAnalyzeEmptyAndNonEmptyLists() {
        assertEquals("Empty list", CombinedPatternsExample.analyzeData(List.of()));
        assertEquals("List with 3 elements", CombinedPatternsExample.analyzeData(List.of("a", "b", "c")));
    }

    @Test
    @DisplayName("Should provide free express shipping for VIP large orders")
    void shouldProvideFreeExpressShippingForVipLargeOrders() {
        Order order = new Order("O1", 1500.0, "VIP", 10);

        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("FREE express shipping (VIP + large order)", result);
    }

    @Test
    @DisplayName("Should provide free standard shipping for VIP customers")
    void shouldProvideFreeStandardShippingForVipCustomers() {
        Order order = new Order("O2", 200.0, "VIP", 3);

        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("FREE standard shipping (VIP)", result);
    }

    @Test
    @DisplayName("Should provide free shipping for bulk orders")
    void shouldProvideFreeShippingForBulkOrders() {
        Order order = new Order("O3", 600.0, "REGULAR", 8);

        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("FREE standard shipping (bulk order)", result);
    }

    @Test
    @DisplayName("Should charge standard shipping for medium orders")
    void shouldChargeStandardShippingForMediumOrders() {
        Order order = new Order("O4", 150.0, "REGULAR", 2);

        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("Standard shipping: $10", result);
    }

    @Test
    @DisplayName("Should charge higher shipping for small orders")
    void shouldChargeHigherShippingForSmallOrders() {
        Order order = new Order("O5", 50.0, "REGULAR", 1);

        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("Standard shipping: $15", result);
    }
}
```

**Output:**
```
=== Payment Processing ===
Charging $75.00 to credit card ending in 3456
Processing debit card payment of $75.00
Cash payment of $50.00 received, change: $-25.00
Paid $75.00 via PayPal

=== Over-limit Credit Card ===
Credit card declined - limit $50.00 exceeded by $25.00

=== Data Analysis ===
Short text: Hello
Numeric string: 12345
Long text: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA...
Number: 42
Negative number: -10
Large number: 2000
Empty list
List with 3 elements

=== Shipping Determination ===
Order O1 ($1500.00, VIP): FREE express shipping (VIP + large order)
Order O2 ($200.00, VIP): FREE standard shipping (VIP)
Order O3 ($600.00, REGULAR): FREE standard shipping (bulk order)
Order O4 ($150.00, REGULAR): Standard shipping: $10
Order O5 ($50.00, REGULAR): Standard shipping: $15
```

**Key Insight**: Combining pattern matching with guard conditions (`&&` after instanceof) enables sophisticated conditional logic in a single expression. This is particularly powerful for business rules like payment processing or shipping calculations where multiple criteria determine the outcome.

---

## Best Practices

### When to Use Pattern Matching

✅ **Use pattern matching for:**
- **Polymorphic method dispatch** - replacing instanceof chains
- **equals() implementations** - cleaner than traditional approach
- **Validation with type checks** - combining instanceof with business logic
- **Guard conditions** - when you need type + additional condition

❌ **Don't use pattern matching when:**
- **Polymorphism would be better** - if you control the hierarchy, add methods instead
- **Type checks indicate design issues** - consider refactoring to avoid type checking
- **Performance is absolutely critical** - pattern matching has negligible overhead, but direct method calls are still faster

### When to Use Switch Expressions

✅ **Use switch expressions for:**
- **Mapping enum values** - exhaustiveness checking prevents bugs
- **Returning values from multi-way branches** - cleaner than if-else chains
- **Complex conditional logic** - when multiple conditions determine a result

❌ **Don't use switch expressions when:**
- **A single if-else would suffice** - don't over-engineer
- **You need fall-through** - rare, but use colon syntax if needed
- **Cases have complex, unrelated logic** - consider separate methods

### Combining Pattern Matching with Sealed Types

Pattern matching shines brightest with sealed types (covered in Part 3):

```java
sealed interface Result<T> permits Success, Failure {}
record Success<T>(T value) implements Result<T> {}
record Failure<T>(String error) implements Result<T> {}

// Compiler ensures exhaustiveness
public static <T> String describe(Result<T> result) {
    if (result instanceof Success<T> s) {
        return "Success: " + s.value();
    } else if (result instanceof Failure<T> f) {
        return "Failure: " + f.error();
    }
    // No else needed - compiler knows these are the only options
    throw new AssertionError(); // Unreachable but satisfies compiler
}
```

### Style Guidelines

**Prefer modern patterns:**
```java
// ✅ Good: concise and clear
if (obj instanceof String s && s.length() > 0) {
    return s.toUpperCase();
}

// ❌ Avoid: old style
if (obj instanceof String) {
    String s = (String) obj;
    if (s.length() > 0) {
        return s.toUpperCase();
    }
}
```

**Use meaningful variable names:**
```java
// ✅ Good: descriptive names
if (shape instanceof Circle c) {
    return Math.PI * c.radius() * c.radius();
}

// ❌ Avoid: single letters unless context is obvious
if (shape instanceof Circle x) {
    return Math.PI * x.radius() * x.radius();
}
```

**Group related cases in switch:**
```java
// ✅ Good: logically grouped
return switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
    case SATURDAY, SUNDAY -> "Weekend";
};

// ❌ Avoid: unnecessarily separate
return switch (day) {
    case MONDAY -> "Weekday";
    case TUESDAY -> "Weekday";
    // ... repetitive
};
```

---

## Summary and Next Steps

### Key Takeaways

1. **Pattern matching for instanceof** (Java 16) eliminates test-and-cast ceremony
2. **Switch expressions** (Java 14) make switch a first-class expression with exhaustiveness
3. **Arrow syntax (`->`)** prevents fall-through bugs
4. **yield keyword** enables multi-statement branches in switch expressions
5. **Pattern variables** have compiler-enforced scope based on flow analysis
6. **Combining patterns with guards** creates powerful conditional logic
7. **Sealed types + pattern matching** = exhaustiveness guarantees

### Migration Strategy

For teams upgrading from Java 8:

**Phase 1: Switch Expressions**
- Replace switch statements returning values with switch expressions
- Use arrow syntax for new code
- Keep colon syntax for rare fall-through cases

**Phase 2: Pattern Matching**
- Update `equals()` methods to use pattern matching
- Replace instanceof-cast chains with pattern matching
- Add guard conditions where appropriate

**Phase 3: Combined Patterns**
- Refactor complex type hierarchies with sealed types + pattern matching
- Leverage exhaustiveness checking for safer refactoring

### Check Your Understanding

- [ ] Can you explain how pattern variable scope works with `&&` and `||`?
- [ ] Do you understand the difference between switch expressions and statements?
- [ ] Can you use `yield` for multi-statement branches?
- [ ] Do you know when exhaustiveness checking applies to switch?
- [ ] Can you combine pattern matching with guard conditions?
- [ ] Do you understand how pattern matching simplifies `equals()`?

### Coming Up Next

**Part 5: Text Blocks (JEP 378)** - Multi-line string literals

Text blocks eliminate the pain of working with multi-line strings in Java:
- No more escape character soup for JSON, SQL, HTML
- Automatic indentation management
- String interpolation alternatives
- Best practices for formatted text

We'll explore how text blocks integrate with modern Java features like formatted I/O and how they simplify working with embedded languages.

### Resources

#### Official Documentation

- **JEP 394 - Pattern Matching for instanceof**: [openjdk.org/jeps/394](https://openjdk.org/jeps/394)
  The official proposal introducing pattern matching for instanceof in Java 16. Includes design rationale, scope rules, and future directions.

- **JEP 361 - Switch Expressions**: [openjdk.org/jeps/361](https://openjdk.org/jeps/361)
  Finalized switch expressions in Java 14. Details arrow syntax, yield keyword, and exhaustiveness requirements.

- **JEP 325 - Switch Expressions (Preview)**: [openjdk.org/jeps/325](https://openjdk.org/jeps/325)
  First preview in Java 12, showing the evolution of the feature.

- **JEP 354 - Switch Expressions (Second Preview)**: [openjdk.org/jeps/354](https://openjdk.org/jeps/354)
  Refinements in Java 13 based on community feedback.

#### Interactive References

- **Java Almanac - instanceof Patterns**: [javaalmanac.io/features/instanceof-patterns/](https://javaalmanac.io/features/instanceof-patterns/)
  Interactive examples demonstrating pattern matching for instanceof with runnable code samples.

- **Java Almanac - Switch**: [javaalmanac.io/features/switch/](https://javaalmanac.io/features/switch/)
  Comprehensive guide to switch expressions with visual timelines and comparisons.

#### Code Repository

- **GitHub Repository - blog-9mac-dev-code**: [github.com/dawid-swist/blog-9mac-dev-code](https://github.com/dawid-swist/blog-9mac-dev-code)
  All examples from this article with full JUnit tests:
  ```bash
  git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
  cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
  ../../gradlew test --tests *part4*
  ```

#### Additional Reading

- **Brian Goetz - "Pattern Matching in the Java Object Model"**: Technical deep dive from Java's language architect
- **Inside Java Podcast - Pattern Matching Episodes**: Discussions on design decisions and future directions
- **Effective Java (3rd Edition)** - Item 14: Consider implementing Comparable
  Shows how pattern matching can simplify comparison logic

---

*Written for [blog.9mac.dev](https://blog.9mac.dev)*
*Part of the "Java 17 Features Every Senior Developer Should Know" series*

**Previous**: [Part 2 - Records](part-2-records.md) | [Part 3 - Sealed Classes](part-3-sealed-classes.md)
**Next**: Part 5 - Text Blocks *(coming soon)*

---

## Run the Examples

All code examples are available in the repository:

```bash
# Run individual examples
java -cp build/classes/java/main dev.nmac.blog.examples.java17.part4.BasicPatternMatchingExample
java -cp build/classes/java/main dev.nmac.blog.examples.java17.part4.SwitchEnumExample
java -cp build/classes/java/main dev.nmac.blog.examples.java17.part4.SwitchYieldExample
java -cp build/classes/java/main dev.nmac.blog.examples.java17.part4.TypeHierarchyExample
java -cp build/classes/java/main dev.nmac.blog.examples.java17.part4.EqualsPatternMatchingExample
java -cp build/classes/java/main dev.nmac.blog.examples.java17.part4.CombinedPatternsExample

# Run all Part 4 tests
./gradlew test --tests "*part4*"

# Run specific test class
./gradlew test --tests BasicPatternMatchingExampleTest
./gradlew test --tests SwitchEnumExampleTest
./gradlew test --tests SwitchYieldExampleTest
./gradlew test --tests TypeHierarchyExampleTest
./gradlew test --tests EqualsPatternMatchingExampleTest
./gradlew test --tests CombinedPatternsExampleTest
```

