# Java 17 Features Every Senior Developer Should Know - Part 4: Pattern Matching & Switch Expressions

Welcome to Part 4 of our comprehensive series on Java 17 features! In previous installments, we explored the [`var` keyword](part-1-introduction-and-var.md) for type inference and [Records](part-2-records.md) for eliminating boilerplate in data classes. Today, we're diving into two transformative features that fundamentally change how we write conditional logic and type checks in Java: **Pattern Matching for instanceof** (JEP 394) and **Switch Expressions** (JEP 361).

These features represent Java's first serious steps toward pattern matching—a programming paradigm that functional languages have enjoyed for decades. For developers upgrading from Java 8, this is where modern Java starts to feel truly different.

## Table of Contents

1. [What is Pattern Matching?](#what-is-pattern-matching)
2. [Historical Context: Java 1-8](#historical-context-java-1-8)
3. [Changes to instanceof (Java 16+)](#changes-to-instanceof-java-16)
4. [Switch Expressions (Java 14+)](#switch-expressions-java-14)
5. [Pattern Matching in Switch (Java 17+)](#pattern-matching-in-switch-java-17)
6. [Practical Examples](#practical-examples)
7. [Best Practices](#best-practices)
8. [Summary and Next Steps](#summary-and-next-steps)

---

## What is Pattern Matching?

**Pattern matching** is a general programming concept—not specific to Java—for checking a value against a pattern and, if it matches, deconstructing that value into its constituent parts. Java introduces pattern matching incrementally across multiple language features.

In academic terms, pattern matching is a form of **structural decomposition** combined with **conditional dispatch**. You're simultaneously asking "what shape is this data?" and "if it matches, bind the components to names I can use."

### Pattern Matching in Java: Multiple Forms

Java's approach to pattern matching is incremental—different patterns are added in different versions:

1. **Type patterns** (Java 16+): `instanceof Circle c` - match and cast types
2. **Guarded patterns** (Java 16+): Add conditions with `when` clause: `instanceof String s when s.length() > 0`
3. **Record patterns** (Java 17+): Destructure records - `case Point(int x, int y) ->`
4. **Array patterns** (Preview, future): Match array contents - coming in future versions

Each form can appear in different contexts:
- **instanceof expressions** (Java 16+): Type checking with automatic casting
- **switch statements/expressions** (Java 14+ with patterns in Java 17+)

### The Problem Pattern Matching Solves

Traditional imperative programming requires multiple steps to work with polymorphic data:
1. Check the type using `instanceof`
2. Cast to that type manually
3. Extract components (for records)
4. Repeat for each possible type

Pattern matching collapses these steps into declarative syntax that's both safer and more concise. Instead of procedural "how to check," you write declarative "what to match." This represents a **paradigm shift** that:

- **Reduces boilerplate**: Eliminates manual type casting and null checks
- **Improves safety**: Compiler enforces exhaustiveness and correct variable scopes
- **Reduces errors**: Prevents entire classes of type-casting and null-pointer bugs
- **Clarifies intent**: Makes code more readable by expressing "what we're matching for" rather than "how to check"

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

Java 16+ introduced **pattern matching** to eliminate the test-and-cast ceremony, while Java 14+ made **switch expressions** return values and prevent fall-through bugs. Java 17 brings these together: pattern matching in switch statements creates powerful, concise conditional logic.

---

## Changes to instanceof (Java 16+)

Java 16 introduced major changes to the `instanceof` operator via **Pattern Matching for instanceof** (JEP 394). In the Java Language Specification, this feature is referred to as **"pattern variables"** or **"type patterns in instanceof"**.

These changes finally bring to Java a feature that other languages have had for decades. Swift, Scala, and functional languages like Haskell pioneered pattern matching as a way to combine type checking, casting, and value extraction into a single expression.

Java's conservative approach meant pattern matching arrived later, but it's now transforming how we write conditional logic, eliminating the test-and-cast ceremony that plagued Java for decades.

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

The pattern variable is only in scope where the compiler can **prove it was assigned**.

#### How Pattern Variables Are Scoped

**Rule 1: After `instanceof` in same if-block**
```java
if (obj instanceof String s) {
    // ✓ 's' is a String here
    System.out.println(s.length());
}
```

**Rule 2: With `&&` (AND) on the right side**
```java
if (obj instanceof String s && s.length() > 5) {
    // ✓ 's' is a String AND length > 5 here
    System.out.println(s.toUpperCase());
}
```

**Rule 3: NOT with `||` (OR) on the right side**
```java
if (obj instanceof String s || isTrusted(obj)) {
    // ✗ DOES NOT COMPILE
    // 's' might not exist if isTrusted() was true instead
    System.out.println(s.length());
}

// ✓ CORRECT: Use separate if statements
if (obj instanceof String s) {
    System.out.println(s.length());
}
if (isTrusted(obj)) {
    System.out.println("Trusted");
}
```

**Rule 4: In else block with negation**
```java
if (!(obj instanceof String s)) {
    // 's' is NOT in scope here (obj is not a String)
} else {
    // ✓ 's' IS in scope here (obj is definitely a String)
    System.out.println(s.length());
}
```

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

## Switch Expressions (Java 14+)

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

## Pattern Matching in Switch (Java 17+)

Java 17 brought **pattern matching in switch expressions** (JEP 406), combining everything you've learned so far into one powerful feature. Instead of using `instanceof` checks for pattern matching, you can now directly match patterns inside switch expressions.

### Type Patterns in Switch

Match different types directly without `instanceof`:

```java
String result = switch (obj) {
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    case Double d -> "Double: " + d;
    case null -> "Null value";
    default -> "Unknown type";
};
```

### Guarded Patterns

Add conditions to patterns using the `when` clause:

```java
String classify = switch (obj) {
    case String s when s.isEmpty() -> "Empty string";
    case String s when s.length() < 5 -> "Short string: " + s;
    case String s -> "Long string: " + s;
    default -> "Not a string";
};
```

### Sealed Types with Pattern Matching

Sealed types + pattern matching = exhaustiveness guaranteed by the compiler:

```java
sealed interface Shape permits Circle, Rectangle, Triangle {}
record Circle(double radius) implements Shape {}
record Rectangle(int width, int height) implements Shape {}
record Triangle(int base, int height) implements Shape {}

int area = switch (shape) {
    case Circle c -> (int) (Math.PI * c.radius() * c.radius());
    case Rectangle r -> r.width() * r.height();
    case Triangle t -> (t.base() * t.height()) / 2;
    // No default needed - compiler enforces all cases
};
```

### Record Patterns

Destructure records directly in switch patterns:

```java
record Point(int x, int y) {}

String location = switch (obj) {
    case Point(int x, int y) when x > 0 && y > 0 -> "Quadrant 1";
    case Point(int x, int y) when x < 0 && y > 0 -> "Quadrant 2";
    case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
    default -> "Other";
};
```

---

## Practical Examples

Let's explore pattern matching and switch expressions through seven comprehensive examples.

### Example 1: Basic Pattern Matching with instanceof

Pattern matching for instanceof eliminates redundant casting and makes type checks more concise.

**Before (Java 8-15):**
```java
if (obj instanceof String) {
    String s = (String) obj;
    return "String of length " + s.length();
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    return "Integer: " + i;
}
```

**After (Java 16+):**
```java
if (obj instanceof String s) {
    return "String of length " + s.length();
} else if (obj instanceof Integer i) {
    return "Integer: " + i;
}
```

**Full Example:**
```java
public class BasicPatternMatchingExample {

    // Java 16+ pattern matching
    public static String describeModern(Object obj) {
        if (obj instanceof String s) {
            return "String of length " + s.length();
        } else if (obj instanceof Integer i) {
            return "Integer: " + i;
        } else if (obj instanceof Double d) {
            return "Double: " + d;
        }
        return "Unknown type";
    }

    // Demonstrating scope rules with && (AND) and negation
    public static String demonstrateScope(Object obj) {
        if (obj instanceof String s && s.length() > 5) {
            return "Long string: " + s.toUpperCase();
        }
        if (!(obj instanceof String s)) {
            return "Not a string";
        }
        return "String in else: " + s;
    }
}
```

**Test:**
```java
@Test
void shouldUsePatternMatching() {
    assertEquals("String of length 5",
        BasicPatternMatchingExample.describeModern("Hello"));
}
```

**Key Insight**: Pattern matching combines type checking, casting, and variable binding in a single operation. The scope rules ensure pattern variables only exist where the type check succeeds—in the if-block, with `&&` operators on the right side, or in negation blocks.

---

### Example 2: Switch Expressions with Enums

Switch expressions with enums demonstrate exhaustiveness checking—the compiler ensures all enum values are handled.

**Before (Java 8-13):**
```java
String result;
switch (day) {
    case MONDAY:
    case TUESDAY:
        result = "Weekday";
        break;
    case SATURDAY:
    case SUNDAY:
        result = "Weekend";
        break;
    default:
        throw new IllegalArgumentException();
}
```

**After (Java 14+):**
```java
String result = switch (day) {
    case MONDAY, TUESDAY -> "Weekday";
    case SATURDAY, SUNDAY -> "Weekend";
};
```

**Full Example:**
```java
public class SwitchEnumExample {
    public enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

    // Modern switch expression with grouped cases
    public static String getDayType(Day day) {
        return switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
            case SATURDAY, SUNDAY -> "Weekend";
        };
    }

    // No default needed - all enum values covered by compiler
    public static int getSeasonMonths(String season) {
        return switch (season) {
            case "SPRING" -> 3;
            case "SUMMER" -> 3;
            case "FALL" -> 3;
            case "WINTER" -> 3;
        };
    }
}
```

**Test:**
```java
@Test
void shouldClassifyDaysWithSwitch() {
    assertEquals("Weekday", SwitchEnumExample.getDayType(Day.MONDAY));
    assertEquals("Weekend", SwitchEnumExample.getDayType(Day.SATURDAY));
}
```

**Key Insight**: Switch expressions with enums benefit from exhaustiveness checking. When all enum values are covered, no `default` case is needed. The compiler will error if a new enum value is added without updating the switch.

---

### Example 3: Switch Expressions with yield

The `yield` keyword allows multi-statement branches in switch expressions, enabling complex logic while still returning a value.

```java
public class SwitchYieldExample {

    public enum Operation { ADD, SUBTRACT, MULTIPLY, DIVIDE }

    // Simple yield: single statement in braces
    public static double calculate(Operation op, double a, double b) {
        return switch (op) {
            case ADD -> {
                System.out.println("Adding...");
                yield a + b;
            }
            case SUBTRACT -> {
                System.out.println("Subtracting...");
                yield a - b;
            }
            case MULTIPLY -> a * b;
            case DIVIDE -> (b == 0) ? Double.NaN : a / b;
        };
    }

    // Complex logic: validation with yield
    public static String getGrade(int score) {
        return switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            default -> {
                if (score < 0 || score > 100) {
                    throw new IllegalArgumentException("Invalid: " + score);
                }
                yield "F";
            }
        };
    }
}
```

**Test:**
```java
@Test
void shouldUseYieldForMultiStatementBranches() {
    assertEquals(15.0, calculate(Operation.ADD, 10, 5), 0.001);
    assertEquals("A", getGrade(95));
    assertEquals("F", getGrade(45));
}
```

**Key Insight**: The `yield` keyword enables multi-statement logic within switch branches. Unlike the arrow syntax (`->`), which executes a single expression, `yield` allows statements with side effects (like logging) before returning a value. This combines conciseness with flexibility.

---

### Example 4: Pattern Matching in Switch (Java 17+)

Pattern matching in switch expressions (JEP 406) combines type matching, guards, and record destructuring into a single powerful feature.

```java
public class PatternSwitchExample {

    // Type patterns - match and extract different types
    public static String processObject(Object obj) {
        return switch (obj) {
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            case Double d -> "Double: " + d;
            case null -> "Null value";
            default -> "Unknown";
        };
    }

    // Record patterns with guarded patterns
    public record Point(int x, int y) {}

    public static String describePoint(Object obj) {
        return switch (obj) {
            case Point(int x, int y) when x > 0 && y > 0 ->
                "Quadrant 1: (" + x + ", " + y + ")";
            case Point(int x, int y) when x < 0 && y > 0 ->
                "Quadrant 2: (" + x + ", " + y + ")";
            case Point(int x, int y) when x == 0 && y == 0 ->
                "Origin";
            case Point(int x, int y) ->
                "Other: (" + x + ", " + y + ")";
            default -> "Not a point";
        };
    }
}
```

**Test:**
```java
@Test
void shouldUsePatternMatchingInSwitch() {
    assertEquals("String: Hello",
        PatternSwitchExample.processObject("Hello"));
    assertEquals("Null value",
        PatternSwitchExample.processObject(null));
    assertEquals("Quadrant 1: (3, 4)",
        PatternSwitchExample.describePoint(new Point(3, 4)));
}
```

**Key Insight**: Pattern matching in switch combines type patterns (like `case String s`), guarded patterns (like `when x > 0`), and record destructuring (like `case Point(int x, int y)`). This eliminates the need for instanceof checks and manual casting, resulting in cleaner, more type-safe code.

---

### Example 5: Pattern Matching with Type Hierarchies

When handling polymorphic types, pattern matching eliminates the redundancy of explicit casting. Instead of writing type names three times (instanceof, cast declaration, cast operation), do it once with pattern matching.

**Before Java 16 - Explicit casting required:**
```java
if (shape instanceof Circle) {
    Circle c = (Circle) shape;  // ← Type name appears 3 times!
    return "Radius: " + c.radius();
} else if (shape instanceof Rectangle) {
    Rectangle r = (Rectangle) shape;  // ← Redundant
    return "Width: " + r.width();
}
```

**Java 16+ - Pattern matching eliminates redundancy:**
```java
if (shape instanceof Circle c) {  // ← All three operations: check, cast, bind
    return "Radius: " + c.radius();
} else if (shape instanceof Rectangle r) {
    return "Width: " + r.width();
}
```

**Full Example:**

```java
public class TypeHierarchyExample {

    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    // Modern approach with pattern matching
    public static String describe(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle: radius=" + c.radius();
            case Rectangle r -> "Rectangle: " + r.width() + "x" + r.height();
            case Triangle t -> "Triangle: base=" + t.base();
        };
    }
}
```

**Test:**
```java
@Test
void shouldEliminateTypeCastingWithPatternMatching() {
    Shape circle = new Circle(5.0);
    Shape rect = new Rectangle(10.0, 20.0);

    assertEquals("Circle: radius=5.0", describe(circle));
    assertTrue(describe(rect).contains("Rectangle"));
}
```

**Key Insight**: Sealed types + pattern matching guarantee exhaustiveness. The compiler knows all Shape subtypes. When you use sealed types with switch pattern matching, no `default` case is needed—the compiler will error if you forget to handle a subtype. This prevents entire classes of runtime type errors.

---

### Example 6: Implementing equals() with Pattern Matching

Pattern matching eliminates boilerplate from `equals()` methods. Instead of explicit null checks, type checks, and casting, pattern matching does it all in one line.

**Before Java 16 - 6 lines of ceremony:**
```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Point other = (Point) obj;              // ← Manual cast after type check
    return this.x == other.x && this.y == other.y;
}
```

**Java 16+ - Pattern matching eliminates all boilerplate:**
```java
@Override
public boolean equals(Object obj) {
    return obj instanceof Point p &&        // ← Type check + cast + bind in ONE
           this.x == p.x && this.y == p.y;
}
```

**Full Example:**

```java
public class EqualsPatternMatchingExample {

    public record Point(int x, int y) {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Point p &&
                   this.x == p.x && this.y == p.y;
        }
    }

    public record Person(String name, int age) {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Person p &&
                   Objects.equals(this.name, p.name) &&
                   this.age == p.age;
        }
    }
}
```

**Test:**
```java
@Test
void shouldUsePatternMatchingInEquals() {
    Point p1 = new Point(10, 20);
    Point p2 = new Point(10, 20);

    assertTrue(p1.equals(p2));
    assertFalse(p1.equals(null));
}
```

**Key Insight**: Pattern matching replaces 6 lines of `equals()` boilerplate with 1 line. The `instanceof` pattern automatically handles null (returns false), type checking, and casting—all atomically. This eliminates an entire class of bugs where type checks and casts could be misaligned.

---

### Example 7: Sealed Types with Pattern Matching (Result<T>)

Pattern matching reaches its full potential with sealed types. A Result<T> can only be Success or Failure—the compiler enforces exhaustiveness. Add a new type, and all pattern matching code fails to compile until updated.

```java
public class ResultPatternExample {

    public sealed interface Result<T> permits Success, Failure {}
    public record Success<T>(T value) implements Result<T> {}
    public record Failure<T>(String error) implements Result<T> {}

    // Pattern matching with sealed types
    public static <T> String describe(Result<T> result) {
        return switch (result) {
            case Success<T> s -> "Success: " + s.value();
            case Failure<T> f -> "Failure: " + f.error();
            // No default needed - compiler knows these are the only two cases!
        };
    }

    // Functional transformation preserving sealed type contracts
    public static <T, U> Result<U> map(Result<T> result, Function<T, U> fn) {
        return switch (result) {
            case Success<T> s -> new Success<>(fn.apply(s.value()));
            case Failure<T> f -> new Failure<>(f.error());
        };
    }
}
```

**Test:**
```java
@Test
void shouldEnforceSealedTypeExhaustiveness() {
    Result<String> success = new Success<>("Value");
    Result<String> failure = new Failure<>("Error");

    assertEquals("Success: Value", describe(success));
    assertEquals("Failure: Error", describe(failure));
    assertEquals("Success: 10",
        describe(map(new Success<>(5), x -> x * 2)));
}
```

**Key Insight**: Sealed types + pattern matching = compiler enforced exhaustiveness. The compiler knows Success and Failure are the ONLY two cases. If you add a new Result type, all pattern matching code fails to compile until updated. This catches bugs at compile-time instead of runtime, and no default case is needed.

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

**Previous**: [Part 3 - Sealed Classes](part-3-sealed-classes.md)
**Next**: [Part 5 - Text Blocks](part-5-text-blocks.md)

---

## Series Navigation

- **← [Part 3: Sealed Classes](./part-3-sealed-classes.md)** - Controlled inheritance
- **← [Series Overview](./README.md)** - Overview and series guide
- **→ [Part 5: Text Blocks](./part-5-text-blocks.md)** - Next: Multi-line string handling

[Back to Blog Posts](../)  |  [Back to Repository](../../../)
