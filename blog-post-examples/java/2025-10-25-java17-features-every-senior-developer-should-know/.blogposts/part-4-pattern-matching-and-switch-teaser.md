# Java 17 Features Every Senior Developer Should Know - Part 4: Pattern Matching & Switch Expressions

**Part 4 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

Pattern matching replaces mechanical instanceof-cast chains with declarative patterns, while switch expressions eliminate fall-through bugs. This article shows how modern pattern matching works with guards, records, and sealed types for safer, more readable code.

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

## More Pattern Matching Examples

### Type Patterns with Guards

```java
public String analyze(Object obj) {
    return switch (obj) {
        case String s when s.length() > 10 -> "Long string: " + s;
        case String s -> "Short string: " + s;
        case Integer i when i > 0 -> "Positive: " + i;
        case Integer i -> "Non-positive: " + i;
        default -> "Unknown type";
    };
}
```

### Deconstruction Patterns (Records)

```java
public record Point(int x, int y) {}
public record Circle(Point center, int radius) {}

public String describeCircle(Object obj) {
    return switch (obj) {
        case Circle(var center, var r) when r > 100
            -> "Large circle at " + center + ", radius: " + r;
        case Circle(Point(var x, var y), var r)
            -> "Circle at (" + x + "," + y + "), radius: " + r;
        default -> "Not a circle";
    };
}
```

### Combining with if-else

```java
public void processPayment(Object obj) {
    if (obj instanceof CreditCard card) {
        chargeCard(card.cardNumber(), card.amount());
    } else if (obj instanceof BankTransfer transfer) {
        transferFunds(transfer.accountNumber(), transfer.amount());
    } else if (obj instanceof CryptoCurrency crypto) {
        processCrypto(crypto.wallet(), crypto.amount());
    } else {
        throw new IllegalArgumentException("Unknown payment type");
    }
}
```

## Switch Expressions vs Statements

| Feature | Statement | Expression |
|---------|-----------|-----------|
| Returns value | No | Yes |
| Syntax | `case:` with `break` | Arrow `->` |
| Fall-through | Possible (bug-prone) | Not possible |
| Default | Optional | Required |
| Readability | Lower | Higher |

```java
// OLD: Switch statement
int days;
switch (month) {
    case "January":
    case "March":
    case "May":
        days = 31;
        break;
    default:
        throw new Exception("Unknown month");
}

// NEW: Switch expression
int days = switch (month) {
    case "January", "March", "May" -> 31;
    case "April", "June", "September", "November" -> 30;
    case "February" -> 28;
    default -> throw new IllegalArgumentException("Unknown month");
};
```

## Practical Example: JSON Parser

```java
public Object parseJsonValue(String json) {
    return switch (json.charAt(0)) {
        case '{' -> parseObject(json);
        case '[' -> parseArray(json);
        case '"' -> parseString(json);
        case 't', 'f' -> parseBoolean(json);
        case 'n' -> null;
        case '-', '0','1','2','3','4','5','6','7','8','9'
            -> parseNumber(json);
        default -> throw new IllegalArgumentException("Invalid JSON: " + json);
    };
}
```

## Common Patterns

1. **Type Checking** - Replace instanceof chains
2. **Sealed Hierarchy Exhaustiveness** - Compiler ensures all cases
3. **Guard Conditions** - Filter patterns with `when`
4. **Deconstruction** - Extract values from records
5. **Multi-case Labels** - Comma-separated cases

## Read the Full Article

Discover much more in **[Part 4: Pattern Matching & Switch Expressions on blog.9mac.dev]([BLOG_LINK_HERE])**:
- Pattern matching in other languages (Swift, Scala, Kotlin, TypeScript)
- Guards and complex pattern conditions
- Type patterns, type tests, and deconstruction patterns
- Switch expressions vs statements: when to use each
- Integration with sealed classes for exhaustive checking
- 10+ practical examples from real-world scenarios
- Performance implications

## GitHub Repository

All code examples are ready to clone and run:

```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 5 - Text Blocks (multi-line string literals)
