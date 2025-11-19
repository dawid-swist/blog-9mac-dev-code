# Java 17 Features Every Senior Developer Should Know - Part 5: Text Blocks

Welcome to Part 5 of our comprehensive series on Java 17 features! In previous installments, we explored the [`var` keyword](part-1-introduction-and-var.md) for type inference, [Records](part-2-records.md) for eliminating boilerplate, [Sealed Classes](part-3-sealed-classes.md) for controlled hierarchies, and [Pattern Matching & Switch Expressions](part-4-pattern-matching-and-switch.md) for safer conditionals. Today, we're examining **Text Blocks** (JEP 378), which improves readability when embedding multi-line strings like JSON, SQL, HTML, and XML in Java code.

Traditionally, embedding multi-line strings required extensive use of escape sequences and concatenation operators. Each newline needed an explicit `\n`, quotes required escaping with `\"`, and each line demanded a `+` operator. This approach made code difficult to read and maintain, particularly for structured data formats.

Text blocks provide a cleaner solution. You can directly embed multi-line strings with proper formatting preserved. The compiler automatically handles line termination normalization, indentation stripping, and whitespace management. When combined with `formatted()`, text blocks offer a practical lightweight alternative for generating emails, invoices, configurations, and other templated content.

## Table of Contents

1. [The Problem: Multi-line Strings Before Java 15](#the-problem-multi-line-strings-before-java-15)
2. [What Are Text Blocks?](#what-are-text-blocks)
3. [Syntax and Basic Usage](#syntax-and-basic-usage)
4. [Indentation Rules](#indentation-rules)
5. [Escape Sequences](#escape-sequences)
6. [Practical Examples](#practical-examples)
7. [Template Systems with Variable Interpolation](#template-systems-with-variable-interpolation)
8. [Best Practices](#best-practices)
   - [Security Considerations](#security-considerations)
   - [When to Use Text Blocks](#when-to-use-text-blocks)
   - [Performance Considerations](#performance-considerations)
9. [Text Blocks vs Template Engines](#text-blocks-vs-template-engines)
10. [Resources](#resources)
11. [Summary](#summary)

---

## The Problem: Multi-line Strings Before Java 15

Before Java 15, creating multi-line string literals required verbose and error-prone approaches. Consider these common scenarios:

**JSON embedded in tests:**
```java
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 30,\n" +
              "  \"email\": \"alice@example.com\"\n" +
              "}";
```

**SQL queries as strings:**
```java
String query = "SELECT u.name, u.email, o.total\n" +
               "FROM users u\n" +
               "JOIN orders o ON u.id = o.user_id\n" +
               "WHERE o.status = 'COMPLETED'\n" +
               "ORDER BY o.created_at DESC";
```

**HTML templates in code:**
```java
String html = "<html>\n" +
              "  <head>\n" +
              "    <title>Welcome</title>\n" +
              "  </head>\n" +
              "  <body>\n" +
              "    <h1>Hello, World!</h1>\n" +
              "  </body>\n" +
              "</html>";
```

**Common issues with this approach:**
- Every line requires a `+` concatenation operator
- Newlines must be explicitly written as `\n`
- All quotes require escaping with `\"`
- Easy to introduce bugs: missing `+`, forgotten quotes, misplaced escapes
- Source code indentation doesn't match the actual data structure
- Copy-pasting content from external tools (Postman, database clients, design tools) requires extensive reformatting
- Structure and readability of JSON, SQL, or HTML is obscured by Java syntax noise
- Maintenance burden: any update to the content requires modifying multiple lines

---

## What Are Text Blocks?

A **text block** is a multi-line string literal that avoids most escape sequences. It was introduced in Java 15 (JEP 378) after two preview versions in Java 13 and 14.

Text blocks use **triple-double-quote** delimiters (`"""`) and automatically:
- Normalize line terminators to `\n`
- Strip common leading whitespace (indentation)
- Remove incidental trailing whitespace
- Preserve essential formatting

### Before and After

```java
// Before: Traditional string
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 30\n" +
              "}";

// After: Text block
String json = """
    {
      "name": "Alice",
      "age": 30
    }
    """;
```

The text block version is:
- ✅ More readable
- ✅ No escape sequences needed
- ✅ Natural indentation
- ✅ Easier to maintain

---

## Syntax and Basic Usage

### Delimiter Rules

Text blocks are delimited by **three double-quote marks** (`"""`):

```java
String textBlock = """
    Line 1
    Line 2
    Line 3
    """;
```

**Important rules:**
1. Opening `"""` **must** be followed by a line terminator (newline)
2. Content starts on the line **after** the opening delimiter
3. Closing `"""` can be on its own line or after content

```java
// ✅ Valid: Opening delimiter on separate line
String valid1 = """
    Hello, World!
    """;

// ✅ Valid: Closing delimiter on same line as content
String valid2 = """
    Hello, World!""";

// ❌ Invalid: Content on same line as opening delimiter
String invalid = """Hello, World!
    """;
```

### Line Terminator Normalization

All line terminators (Windows `\r\n`, Unix `\n`, Mac `\r`) are normalized to `\n`:

```java
String block = """
    Line 1
    Line 2
    """;

// Equivalent to:
String traditional = "Line 1\nLine 2\n";
```

---

## Indentation Rules

Text blocks have sophisticated indentation handling through **automatic indent stripping**. This is the most complex but powerful feature.

### Rule 1: Common Indent Stripping

The compiler finds the **minimum indentation** across all non-blank lines and removes it:

```java
String example = """
        Hello
        World
        """;

// Actual content: "Hello\nWorld\n"
// The 8 leading spaces were stripped because
// all lines shared that minimum indentation
```

### Rule 2: Closing Delimiter Position Matters

The position of the closing `"""` determines the indent level:

```java
// Closing delimiter aligned left - no stripping
String left = """
Hello
World
""";
// Content: "Hello\nWorld\n"

// Closing delimiter indented - strips that amount
String indented = """
    Hello
    World
    """;
// Content: "Hello\nWorld\n" (4 spaces stripped from each line)
```

### Rule 3: Essential Whitespace with `\s`

Use `\s` to preserve spaces that would otherwise be stripped:

```java
String preserved = """
    Hello\s\s\sWorld
    """;
// Content: "Hello   World\n" (3 spaces preserved)
```

### Rule 4: Trailing Whitespace Removal

Spaces at the end of lines are automatically removed (unless escaped with `\s`):

```java
String trailing = """
    Hello
    World
    """;
// Content: "Hello\nWorld\n" (trailing spaces removed)
```

---

## Escape Sequences

Text blocks support standard Java escape sequences plus two new ones.

### Standard Escapes

All traditional escapes still work:

```java
String escapes = """
    Newline: \n
    Tab: \t
    Backslash: \\
    Quote: \"
    """;
```

### New Escape 1: Essential Space (`\s`)

Marks a space that survives trailing whitespace removal:

```java
String essential = """
    End with space:\s
    """;
// Content: "End with space: \n" (space preserved)
```

### New Escape 2: Line Continuation (`\`)

Ends a line without adding `\n`:

```java
String continued = """
    This is a very long sentence that we want to \
    break across multiple lines in source code \
    but appear as one line in the string.
    """;
// Content: "This is a very long sentence that we want to break across multiple lines in source code but appear as one line in the string.\n"
```

### Escaping Triple Quotes

To include `"""` in a text block, escape at least one quote:

```java
String quotes = """
    He said: \"""Hello!\"""
    """;
// Content: "He said: \"\"\"Hello!\"\"\"\n"
```

---

## Practical Examples

Let's explore text blocks through eight comprehensive examples, including critical security considerations for SQL queries.

### Example 1: Basic Text Blocks

Text blocks eliminate concatenation and escape sequences for multi-line text.

```java
public class BasicTextBlocksExample {

    public static String getMultilineText() {
        return """
            Line 1
            Line 2
            Line 3
            """;
    }

    public static String getPoem() {
        return """
            Roses are "red",
            Violets are "blue",
            Java 15 is awesome,
            And so are you!""";
    }

    public static String getIndentedBlock() {
        return """
                This line has 4 spaces of indent
                    This line has 8 spaces
                Back to 4 spaces
                """;
    }

    public static void main(String[] args) {
        System.out.println("=== Multi-line Text ===");
        System.out.println(getMultilineText());

        System.out.println("=== Poem with Quotes ===");
        System.out.println(getPoem());

        System.out.println("=== Indented Block ===");
        System.out.println(getIndentedBlock());
    }
}
```

**Output:**
```
=== Multi-line Text ===
Line 1
Line 2
Line 3

=== Poem with Quotes ===
Roses are "red",
Violets are "blue",
Java 15 is awesome,
And so are you!

=== Indented Block ===
This line has 4 spaces of indent
    This line has 8 spaces
Back to 4 spaces
```

**Key Insight**: Text blocks eliminate the need for `\n` and `\"` escape sequences, making multi-line strings dramatically more readable while producing identical runtime results.

---

### Example 2: JSON with Text Blocks

Text blocks are perfect for embedded JSON - no more escape sequence hell.

```java
public class JSONTextBlocksExample {

    public static String getUserJSON() {
        return """
            {
              "name": "Alice Smith",
              "age": 30,
              "email": "alice@example.com"
            }""";
    }

    public static String getUserJSONFormatted(String name, int age, String email) {
        return """
            {
              "name": "%s",
              "age": %d,
              "email": "%s"
            }""".formatted(name, age, email);
    }

    public static void main(String[] args) {
        System.out.println("=== User JSON ===");
        System.out.println(getUserJSON());

        System.out.println("\n=== Formatted User JSON ===");
        System.out.println(getUserJSONFormatted("Charlie", 25, "charlie@example.com"));
    }
}
```

**Output:**
```
=== User JSON ===
{
  "name": "Alice Smith",
  "age": 30,
  "email": "alice@example.com"
}

=== Formatted User JSON ===
{
  "name": "Charlie",
  "age": 25,
  "email": "charlie@example.com"
}
```

**Key Insight**: Text blocks combined with `formatted()` provide a clean alternative to JSON libraries for simple cases, making test fixtures and configuration templates dramatically more maintainable.

---

### Example 3: SQL with Text Blocks

Text blocks excel at SQL queries, preserving structure and formatting without concatenation hell.

```java
public class SQLTextBlocksExample {

    public static String getActiveOrdersQuery() {
        return """
            SELECT u.id, u.name, o.order_id, o.total
            FROM users u
            JOIN orders o ON u.id = o.user_id
            WHERE o.status = 'COMPLETED'
            ORDER BY o.created_at DESC""";
    }

    // EDUCATIONAL EXAMPLE: Shows how formatted() works with SQL
    // ⚠️ WARNING: This approach is UNSAFE for production code!
    // See "SQL Queries with Text Blocks - Security First" section for secure approach
    public static String getOrdersByStatusQuery(String status, double minAmount) {
        return """
            SELECT * FROM orders
            WHERE status = '%s'
              AND total > %s
            ORDER BY created_at DESC""".formatted(status, minAmount);
    }

    public static void main(String[] args) {
        System.out.println("=== Active Orders ===" );
        System.out.println(getActiveOrdersQuery());

        System.out.println("\n=== Orders by Status ===");
        System.out.println(getOrdersByStatusQuery("COMPLETED", 100.0));
    }
}
```

**Output:**
```
=== Active Orders ===
SELECT u.id, u.name, o.order_id, o.total
FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.status = 'COMPLETED'
ORDER BY o.created_at DESC

=== Orders by Status ===
SELECT * FROM orders
WHERE status = 'COMPLETED'
  AND total > 100.0
ORDER BY created_at DESC
```

**Key Insight**: Text blocks make SQL queries readable and maintainable by preserving their natural structure, but beware of SQL injection when using `formatted()` with user input—always use PreparedStatement for production code.

---

### Example 4: HTML and SVG with Text Blocks

Text blocks simplify working with HTML and XML-like markup languages by eliminating quote escaping and concatenation noise.

```java
public class HTMLTextBlocksExample {

    public static String getDashboardHTML() {
        return """
            <html>
              <head>
                <title>User Dashboard</title>
              </head>
              <body>
                <h1>Welcome, User!</h1>
                <p>This is your dashboard.</p>
              </body>
            </html>""";
    }

    public static String getSVGCircle() {
        return """
            <svg width="100" height="100">
              <circle cx="50" cy="50" r="40" fill="blue" />
              <text x="50" y="55" text-anchor="middle" fill="white">SVG</text>
            </svg>""";
    }

    public static void main(String[] args) {
        System.out.println("=== Dashboard HTML ===");
        System.out.println(getDashboardHTML());

        System.out.println("\n=== SVG Circle ===");
        System.out.println(getSVGCircle());
    }
}
```

**Output:**
```
=== Dashboard HTML ===
<html>
  <head>
    <title>User Dashboard</title>
  </head>
  <body>
    <h1>Welcome, User!</h1>
    <p>This is your dashboard.</p>
  </body>
</html>

=== SVG Circle ===
<svg width="100" height="100">
  <circle cx="50" cy="50" r="40" fill="blue" />
  <text x="50" y="55" text-anchor="middle" fill="white">SVG</text>
</svg>
```

**Key Insight**: Text blocks eliminate the impedance mismatch between HTML/XML structure in design tools and Java code, making templates maintainable without escaping gymnastics.

---

### Example 5: Indentation Rules and Edge Cases

Text blocks handle indentation intelligently, stripping common leading whitespace while preserving relative indentation for formatting.

```java
public class IndentationExample {

    // Common indent stripping - closing delimiter determines indent level
    public static String getStripExample() {
        return """
            Line 1
            Line 2
            Line 3
            """;
    }

    // Closing delimiter aligned left - minimal stripping
    public static String getLeftAlignedClosing() {
        return """
    Indented line 1
    Indented line 2
""";
    }

    // Essential whitespace with \s
    public static String getEssentialSpace() {
        return """
            Line with trailing space:\s
            Another line
            """;
    }

    // Relative indentation preserved
    public static String getRelativeIndent() {
        return """
            Level 1
                Level 2
                    Level 3
                Level 2
            Level 1
            """;
    }

    // Empty lines handling
    public static String getEmptyLines() {
        return """
            First paragraph.

            Second paragraph after blank line.

            Third paragraph.
            """;
    }

    // Line continuation with backslash
    public static String getLineContinuation() {
        return """
            This is a very long line that we want to \
            split in source code but keep as \
            one line in the string.
            """;
    }

    // Mixed example with all features
    public static String getMixedExample() {
        return """
            Regular line
                Indented line
            Line with essential space:\s
            Split across \
            multiple source lines
            """;
    }

    public static void main(String[] args) {
        System.out.println("=== Strip Example ===");
        System.out.println("[" + getStripExample() + "]");

        System.out.println("\n=== Left Aligned Closing ===");
        System.out.println("[" + getLeftAlignedClosing() + "]");

        System.out.println("\n=== Essential Space ===");
        System.out.println("[" + getEssentialSpace() + "]");

        System.out.println("\n=== Line Continuation ===");
        System.out.println("[" + getLineContinuation() + "]");

        System.out.println("\n=== Relative Indent ===");
        System.out.println(getRelativeIndent());

        System.out.println("\n=== Empty Lines ===");
        System.out.println(getEmptyLines());

        System.out.println("\n=== Mixed Example ===");
        System.out.println("[" + getMixedExample() + "]");
    }
}
```

**Output:**
```
=== Strip Example ===
[Line 1
Line 2
Line 3
]

=== Left Aligned Closing ===
[Indented line 1
Indented line 2
]

=== Essential Space ===
[Line with trailing space:
Another line
]

=== Line Continuation ===
[This is a very long line that we want to split in source code but keep as one line in the string.
]

=== Relative Indent ===
Level 1
    Level 2
        Level 3
    Level 2
Level 1

=== Empty Lines ===
First paragraph.

Second paragraph after blank line.

Third paragraph.

=== Mixed Example ===
[Regular line
    Indented line
Line with essential space:
Split across multiple source lines
]
```

**Key Insight**: Text block indentation rules are designed to balance source code readability with content integrity—the closing delimiter position determines stripping level, preserving intentional formatting.

---

### Example 6: Escape Sequences and Special Cases

Text blocks support all standard Java escape sequences plus two new ones for advanced use cases: `\s` for essential spaces and `\` for line continuation without newlines.

```java
public class EscapeSequencesExample {

    // Standard escapes - all traditional escapes work in text blocks
    public static String getStandardEscapes() {
        return """
            Newline escape: \\n works here
            Tab escape: \\t indents like this
            Backslash: \\\\ is a single backslash
            Quote: \\" doesn't need escaping in text blocks
            """;
    }

    // Essential space (\s) - preserves trailing whitespace
    public static String getEssentialSpace() {
        return """
            Line ending with essential space:\\s
            Line without space
            Another line with space:\\s
            """;
    }

    // Line continuation (\) - joins lines without newline
    public static String getLineContinuation() {
        return """
            This is a long sentence that spans \
            multiple lines in source code but \
            appears as a single line in the string.""";
    }

    // Escaping triple quotes
    public static String getTripleQuoteEscape() {
        return """
            He said: \\"\\"\\"Hello!\\"\\"\\"
            And I replied: \\"\\"\\"Hi there!\\"\\"\\"
            """;
    }

    // Practical: SQL with special characters
    public static String getSQLWithSpecialChars() {
        return """
            SELECT * FROM table
            WHERE name = 'John\\'s Data'
            AND description LIKE '%\\\\%'
            """;
    }

    // Unicode and emoji handling - text blocks preserve all Unicode characters
    public static String getUnicodeExample() {
        return """
            Polish characters: ąćęłńóśźż ĄĆĘŁŃÓŚŹŻ
            Czech characters: čřšžď ČŘŠŽĎ
            Greek letters: α β γ δ ε ζ η θ
            Math symbols: ∑ ∫ √ ∞ ≈ ≠ ≥ ≤
            Currency: € £ ¥ ₿ ₹ ₽
            Emoji: 🚀 ✅ ❌ 🎯 🔥 💡 ⚠️ 🔒
            Arrows: → ← ↑ ↓ ⇒ ⇐ ⇑ ⇓
            """;
    }

    public static void main(String[] args) {
        System.out.println("=== Standard Escapes ===");
        System.out.println(getStandardEscapes());

        System.out.println("\n=== Essential Space ===");
        System.out.println("[" + getEssentialSpace() + "]");

        System.out.println("\n=== Line Continuation ===");
        System.out.println("[" + getLineContinuation() + "]");

        System.out.println("\n=== Triple Quote Escape ===");
        System.out.println(getTripleQuoteEscape());

        System.out.println("\n=== SQL with Special Chars ===");
        System.out.println(getSQLWithSpecialChars());

        System.out.println("\n=== Unicode and Emoji ===");
        System.out.println(getUnicodeExample());
    }
}
```

**Output:**
```
=== Standard Escapes ===
Newline escape: \n works here
Tab escape: \t indents like this
Backslash: \ is a single backslash
Quote: " doesn't need escaping in text blocks

=== Essential Space ===
[Line ending with essential space:
Line without space
Another line with space:
]

=== Line Continuation ===
[This is a long sentence that spans multiple lines in source code but appears as a single line in the string.
]

=== Triple Quote Escape ===
He said: """Hello!"""
And I replied: """Hi there!"""

=== SQL with Special Chars ===
SELECT * FROM table
WHERE name = 'John\'s Data'
AND description LIKE '%\%'

=== Unicode and Emoji ===
Polish characters: ąćęłńóśźż ĄĆĘŁŃÓŚŹŻ
Czech characters: čřšžď ČŘŠŽĎ
Greek letters: α β γ δ ε ζ η θ
Math symbols: ∑ ∫ √ ∞ ≈ ≠ ≥ ≤
Currency: € £ ¥ ₿ ₹ ₽
Emoji: 🚀 ✅ ❌ 🎯 🔥 💡 ⚠️ 🔒
Arrows: → ← ↑ ↓ ⇒ ⇐ ⇑ ⇓
```

**Key Insight**: Text blocks support both standard escape sequences (for compatibility) and new special escapes (`\s`, `\`) that provide fine-grained control over whitespace and line handling—enabling both readability and precision.

---

### Example 7: Safe Templates - Emails, Invoices, and Configuration

Text blocks combined with `formatted()` create a powerful lightweight template system without external libraries—ideal for emails, invoices, configuration files, and API responses. These use cases are safe because they don't involve SQL queries.

```java
public class SimpleTemplateSystemExample {

    // Email template with variable interpolation
    public static String renderEmailTemplate(String recipientName, String activationUrl, String expirationHours) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <h2>Welcome, %s!</h2>
                <p>Thank you for signing up. Please activate your account within %s hours.</p>
                <p><a href="%s" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Activate Account</a></p>
                <p>If you didn't sign up, please ignore this email.</p>
                <p>Best regards,<br>The Team</p>
              </body>
            </html>""".formatted(recipientName, expirationHours, activationUrl);
    }

    // Invoice template with multiple variables
    public static String renderInvoiceTemplate(String invoiceNumber, String customerName, String amount, String dueDate) {
        return """
            ╔════════════════════════════════════════╗
            ║           INVOICE                      ║
            ╚════════════════════════════════════════╝

            Invoice #: %s
            Customer:  %s

            Amount Due: $%s
            Due Date:   %s

            ────────────────────────────────────────
            Thank you for your business!
            """.formatted(invoiceNumber, customerName, amount, dueDate);
    }

    // API response template
    public static String renderAPIResponseTemplate(String status, String requestId, String message) {
        return """
            {
              "status": "%s",
              "requestId": "%s",
              "message": "%s",
              "timestamp": "%s"
            }""".formatted(status, requestId, message, java.time.Instant.now().toString());
    }

    // Config file template
    public static String renderConfigTemplate(String appName, String environment, String port) {
        return """
            # Configuration for %s
            # Environment: %s

            app.name=%s
            app.environment=%s
            server.port=%s
            logging.level=INFO
            """.formatted(appName, environment, appName, environment, port);
    }

    public static void main(String[] args) {
        System.out.println("=== Email Template ===");
        System.out.println(renderEmailTemplate("John Smith", "https://app.example.com/activate?token=xyz123", "24"));

        System.out.println("\n=== Invoice Template ===");
        System.out.println(renderInvoiceTemplate("INV-2024-001", "Acme Corp", "1,500.00", "2024-12-31"));

        System.out.println("\n=== API Response Template ===");
        System.out.println(renderAPIResponseTemplate("success", "req-12345", "User created successfully"));

        System.out.println("\n=== Config Template ===");
        System.out.println(renderConfigTemplate("MyApp", "production", "8080"));
    }
}
```

**Output:**
```
=== Email Template ===
<html>
  <body style="font-family: Arial, sans-serif;">
    <h2>Welcome, John Smith!</h2>
    <p>Thank you for signing up. Please activate your account within 24 hours.</p>
    <p><a href="https://app.example.com/activate?token=xyz123" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Activate Account</a></p>
    <p>If you didn't sign up, please ignore this email.</p>
    <p>Best regards,<br>The Team</p>
  </body>
</html>

=== Invoice Template ===
╔════════════════════════════════════════╗
║           INVOICE                      ║
╚════════════════════════════════════════╝

Invoice #: INV-2024-001
Customer:  Acme Corp

Amount Due: $1,500.00
Due Date:   2024-12-31

────────────────────────────────────────
Thank you for your business!

=== API Response Template ===
{
  "status": "success",
  "requestId": "req-12345",
  "message": "User created successfully",
  "timestamp": "2024-11-17T..."
}

=== Config Template ===
# Configuration for MyApp
# Environment: production

app.name=MyApp
app.environment=production
server.port=8080
logging.level=INFO
```

**Key Insight**: Text blocks combined with `formatted()` eliminate the need for template engines in simple scenarios—turning Java into a practical choice for generating emails, configurations, and structured text documents with clean, readable code.

---

### Example 8: Secure SQL with PreparedStatement

Text blocks work perfectly with PreparedStatement for secure, readable SQL queries. This is the SAFE way to combine text blocks with SQL—using parameterized queries instead of string formatting.

```java
public class SecureSQLExample {

    // Safe SQL: Text block with PreparedStatement placeholders (?)
    // Text blocks improve readability while PreparedStatement prevents SQL injection
    public static String getOrdersByStatusQuerySafe() {
        return """
            SELECT o.order_id, o.total, u.name, u.email
            FROM orders o
            JOIN users u ON o.user_id = u.id
            WHERE o.status = ?
              AND o.total >= ?
            ORDER BY o.created_at DESC
            LIMIT ?""";
    }

    // Complex CTE query demonstrating text block power with security
    // Common Table Expressions (CTEs) are highly readable with text blocks
    public static String getTopCustomersQuerySafe() {
        return """
            WITH customer_totals AS (
                SELECT u.id, u.name, u.email,
                       SUM(o.total) as total_spent,
                       COUNT(o.order_id) as order_count
                FROM users u
                JOIN orders o ON u.id = o.user_id
                WHERE o.status = ?
                  AND o.created_at >= ?
                GROUP BY u.id, u.name, u.email
            )
            SELECT id, name, email, total_spent, order_count
            FROM customer_totals
            WHERE total_spent >= ?
            ORDER BY total_spent DESC
            LIMIT ?""";
    }

    // Example execution method showing PreparedStatement usage
    // In production, this would execute against a real database
    public static List<String> executeOrderQuery(Connection conn, String status, double minAmount, int limit)
            throws SQLException {
        List<String> results = new ArrayList<>();
        String query = getOrdersByStatusQuerySafe();

        // PreparedStatement automatically escapes parameters - SAFE from SQL injection
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);      // Parameter 1: status
            ps.setDouble(2, minAmount);   // Parameter 2: minAmount
            ps.setInt(3, limit);          // Parameter 3: limit

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("Order %s: $%.2f - %s (%s)",
                        rs.getString("order_id"),
                        rs.getDouble("total"),
                        rs.getString("name"),
                        rs.getString("email")));
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        System.out.println("=== Safe SQL with PreparedStatement ===");
        System.out.println(getOrdersByStatusQuerySafe());

        System.out.println("\n=== Complex CTE Query ===");
        System.out.println(getTopCustomersQuerySafe());

        System.out.println("\n=== Key Points ===");
        System.out.println("1. Use ? placeholders instead of %s formatting");
        System.out.println("2. PreparedStatement.setString/setDouble/setInt for parameters");
        System.out.println("3. Text blocks still improve readability");
        System.out.println("4. Completely safe from SQL injection attacks");
    }
}
```

**Output:**
```
=== Safe SQL with PreparedStatement ===
SELECT o.order_id, o.total, u.name, u.email
FROM orders o
JOIN users u ON o.user_id = u.id
WHERE o.status = ?
  AND o.total >= ?
ORDER BY o.created_at DESC
LIMIT ?

=== Complex CTE Query ===
WITH customer_totals AS (
    SELECT u.id, u.name, u.email,
           SUM(o.total) as total_spent,
           COUNT(o.order_id) as order_count
    FROM users u
    JOIN orders o ON u.id = o.user_id
    WHERE o.status = ?
      AND o.created_at >= ?
    GROUP BY u.id, u.name, u.email
)
SELECT id, name, email, total_spent, order_count
FROM customer_totals
WHERE total_spent >= ?
ORDER BY total_spent DESC
LIMIT ?

=== Key Points ===
1. Use ? placeholders instead of %s formatting
2. PreparedStatement.setString/setDouble/setInt for parameters
3. Text blocks still improve readability
4. Completely safe from SQL injection attacks
```

**Key Insight**: Text blocks and PreparedStatement are the perfect combination—text blocks provide readability for complex SQL while PreparedStatement ensures security through parameterized queries.

---

## Template Systems with Variable Interpolation

Text blocks combined with `formatted()` create lightweight template systems without external dependencies. Example 7 demonstrates practical use cases:

**Common Use Cases for `formatted()` with Text Blocks:**

1. **Email Templates** - Dynamic email content with user data
2. **Invoice/Receipt Generation** - Formatted business documents
3. **Configuration Files** - Properties and values with variables
4. **API Response Templates** - JSON/XML data structures
5. **Log Messages** - Contextual information with parameters

For security considerations when using `formatted()` with SQL or HTML, see the [Security Considerations](#security-considerations) section in Best Practices.

---

## Best Practices

### Security Considerations

Text blocks with `formatted()` introduce critical security risks when used improperly with SQL queries or HTML content. Understanding these risks is essential for production code.

#### SQL Injection Risks

**CRITICAL**: Using `formatted()` with SQL queries creates **SQL injection vulnerabilities**—one of the most dangerous security mistakes in application development.

##### The Danger

When you embed user input directly into SQL strings using `formatted()`, attackers can manipulate the query:

```java
// ❌ DANGEROUS - DO NOT USE IN PRODUCTION
String status = userInput;  // User enters: "COMPLETED' OR '1'='1"
String sql = """
    SELECT * FROM orders
    WHERE status = '%s'
    """.formatted(status);
// Resulting SQL: SELECT * FROM orders WHERE status = 'COMPLETED' OR '1'='1'
// This returns ALL orders, bypassing the status filter!
```

More severe attacks can delete data, access sensitive information, or compromise your entire database:

```java
// ❌ If user enters: "'; DROP TABLE orders; --"
String sql = """
    SELECT * FROM orders WHERE status = '%s'
    """.formatted(maliciousInput);
// Resulting SQL executes: SELECT * FROM orders WHERE status = ''; DROP TABLE orders; --'
// Your entire orders table is DELETED!
```

##### The Safe SQL Pattern

**Always use PreparedStatement** for SQL with dynamic values:

```java
// ✅ SAFE - Use PreparedStatement with text blocks
String query = """
    SELECT * FROM orders
    WHERE status = ?
      AND total >= ?
    """;

try (PreparedStatement ps = connection.prepareStatement(query)) {
    ps.setString(1, userInput);  // Automatically escaped - safe!
    ps.setDouble(2, minAmount);
    try (ResultSet rs = ps.executeQuery()) {
        // Process results
    }
}
```

**Key Points:**
- Text blocks improve SQL readability
- `PreparedStatement` prevents SQL injection
- Use `?` placeholders, NOT `%s` formatting
- Set parameters via `setString()`, `setInt()`, `setDouble()`, etc.

**See Example 8** (SecureSQLExample) for complete, production-ready code demonstrating safe SQL with text blocks and PreparedStatement.

##### When Text Blocks ARE Safe with SQL

Text blocks are perfectly safe for SQL when:

1. **Static queries** (no variables): `String query = """SELECT * FROM users""";`
2. **PreparedStatement placeholders**: `String query = """WHERE id = ?""";` (parameters set via `setString/setInt/etc`)
3. **Non-executable purposes**: Logging, documentation, display-only queries

##### When Text Blocks Are UNSAFE with SQL

Never use `formatted()` with SQL when:

1. **Any user input** is involved (form fields, URL parameters, API requests)
2. **External data sources** (file uploads, API responses, message queues)
3. **Any untrusted data** that could be manipulated

#### HTML/XSS Injection Risks

**WARNING**: Using `formatted()` with HTML and user input creates **Cross-Site Scripting (XSS) vulnerabilities**.

##### The Danger

When you embed user input directly into HTML using `formatted()`, attackers can inject malicious scripts:

```java
// ❌ DANGEROUS - XSS Vulnerability
String username = userInput;  // User enters: "<script>alert('XSS')</script>"
String html = """
    <html>
      <body>
        <h1>Welcome, %s!</h1>
      </body>
    </html>""".formatted(username);
// Resulting HTML: <h1>Welcome, <script>alert('XSS')</script>!</h1>
// The script executes in the browser, potentially stealing cookies or performing unauthorized actions!
```

##### The Safe HTML Pattern

**Always escape HTML entities** when inserting user input into HTML:

```java
// ✅ SAFE - Escape HTML entities before formatting
public static String escapeHtml(String input) {
    return input
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#x27;");
}

String username = escapeHtml(userInput);  // Converts <script> to &lt;script&gt;
String html = """
    <html>
      <body>
        <h1>Welcome, %s!</h1>
      </body>
    </html>""".formatted(username);
// Resulting HTML: <h1>Welcome, &lt;script&gt;alert('XSS')&lt;/script&gt;!</h1>
// The script is displayed as text, not executed!
```

**Alternative**: Use dedicated HTML templating libraries (Thymeleaf, Freemarker) that automatically escape variables and provide built-in XSS protection.

**See Example 7** (SimpleTemplateSystemExample) for safe template patterns with non-executable content like emails and invoices.

#### Safe Use Cases for formatted()

`formatted()` with text blocks is **completely safe** for:

1. **Email Templates** - User input is content, not executable code
2. **Invoice/Receipt Generation** - Display-only data
3. **Configuration Files** - Properties and values, no SQL/HTML
4. **API Response Templates** - JSON/XML data structures (ensure proper JSON escaping if needed)
5. **Log Messages** - Contextual information for debugging

#### Summary

**Remember**:
- For SQL with dynamic values → **Always use PreparedStatement**
- For HTML with user input → **Always escape HTML entities**
- For safe contexts (emails, configs, logs) → `formatted()` is fine
- Text blocks are for **readability**, not security

### When to Use Text Blocks

✅ **Text blocks are ideal for:**

**1. Embedded Languages and Markup**
- JSON, XML, HTML structures
- SQL queries (especially complex ones with CTEs)
- GraphQL, YAML configurations
- SVG and other vector formats
- Any literal multi-line code in other languages

**2. Test Fixtures and Test Data**
- Mock JSON responses for API testing
- Structured test data (CSV, XML, HTML)
- Expected output validation in tests
- Configuration templates

**3. Documentation and Error Messages**
- Multi-line log messages with structure
- Formatted error messages for users
- Documentation templates
- Code examples embedded as strings

**4. Dynamic Content with `formatted()`**
- Parameterized SQL queries
- Email templates with variables
- API request bodies with dynamic fields
- Configuration files with placeholders

### When NOT to Use Text Blocks

❌ **Avoid text blocks when:**

**1. Single-Line Strings**
```java
// Don't do this
String message = """
    Hello World
    """;

// Do this instead
String message = "Hello World";
```

**2. Strings Requiring Platform-Specific Line Endings**
```java
// Don't rely on text blocks for Windows-specific line endings
// Text blocks always normalize line terminators to \n (Unix style)
// If you need Windows-style \r\n (CRLF), convert explicitly:
String textBlock = """
    Line 1
    Line 2
    """;
String crlf = textBlock.replace("\n", "\r\n");  // Converts LF to CRLF
```

**3. Complex String Processing**

If the string needs heavy regex or manipulation after creation, use regular strings for clarity:

```java
// Awkward: Text block with immediate complex processing
String processed = """
    user@example.com
    admin@example.com
    """.replaceAll("[\\r\\n]+", ",").trim();
// Result: "user@example.com,admin@example.com"

// Better: Regular string is clearer for processed data
String[] emails = {"user@example.com", "admin@example.com"};
String processed = String.join(",", emails);
```

### Performance Considerations

Text blocks have **zero runtime overhead** compared to traditional strings:
- Compiled to identical bytecode
- No performance penalty vs. concatenation
- String pooling works normally
- Use freely without performance concerns

#### Compilation Performance

Text block processing happens entirely at **compile time**, not runtime:

**Compile-Time Operations:**
- Indentation stripping is calculated when javac compiles the code
- Line terminator normalization (converting `\r\n` to `\n`) happens during compilation
- Escape sequence processing occurs at compile time
- The resulting bytecode contains a plain String constant

**Bytecode Equivalence:**
```java
// Text block source code
String block = """
    Hello
    World
    """;

// Compiles to identical bytecode as:
String traditional = "Hello\nWorld\n";
```

The JVM sees no difference between text blocks and traditional strings—they produce the exact same bytecode. This means:
- No runtime CPU overhead for indentation calculations
- No memory overhead for storing indentation metadata
- No performance difference in String creation, comparison, or manipulation
- JIT compiler optimizations apply identically to both forms

**Practical Impact**: You can use text blocks in performance-critical code (hot loops, high-throughput services) without any concerns. The readability benefit comes at zero runtime cost.

---

## Text Blocks vs Template Engines

Understanding when to use text blocks versus full template engines (Thymeleaf, Freemarker, Velocity) is crucial for architecture decisions.

### When to Use Text Blocks

**Ideal for:**
- **Simple interpolation**: Few variables, straightforward logic
- **Build-time templates**: Configuration files, code generation
- **Test fixtures**: Mock responses, expected output validation
- **Small scope**: Single-purpose, non-reusable templates
- **No external files**: Template lives alongside code

**Example use cases:**
```java
// Email notifications with 3-5 variables
String email = """
    Welcome, %s!
    Your order #%s has shipped.
    """.formatted(name, orderId);

// API response templates
String response = """
    {"status": "%s", "data": %s}
    """.formatted(status, jsonData);

// Test expected output
String expected = """
    User: Alice
    Balance: $100.00
    """;
```

### When to Use Template Engines

**Required for:**
- **Complex logic**: Conditionals, loops, nested structures
- **External templates**: Designers/non-developers maintain templates
- **Reusability**: Same template used in multiple places
- **Internationalization**: Multi-language support with resource bundles
- **Security features**: Built-in HTML escaping, auto-sanitization
- **Rich functionality**: Includes, partials, macros, filters

**Example use cases:**
```java
// Thymeleaf for complex HTML with logic
<div th:if="${user.premium}">
    <h2>Welcome, Premium Member!</h2>
    <ul>
        <li th:each="feature : ${premiumFeatures}" th:text="${feature}"></li>
    </ul>
</div>

// Freemarker for dynamic content generation
<#list products as product>
    <#if product.stock > 0>
        ${product.name}: $${product.price}
    </#if>
</#list>
```
---

## Resources

### Official Documentation

- **JEP 378 - Text Blocks**: [openjdk.org/jeps/378](https://openjdk.org/jeps/378)
  The official Java Enhancement Proposal introducing text blocks in Java 15. Includes design rationale, indentation rules, and escape sequence specifications.

- **JEP 359 - Record Classes (Preview)**: [openjdk.org/jeps/359](https://openjdk.org/jeps/359)
  First preview in Java 14 showing the evolution of text blocks across preview stages.

- **Java Language Specification - Text Blocks**: [docs.oracle.com/javase/specs/jls/se17/html/jls-3.html#jls-3.3](https://docs.oracle.com/javase/specs/jls/se17/html/jls-3.html#jls-3.3)
  Formal specification of text block syntax and semantics in the Java Language Specification.

- **Java Platform String Documentation**: [docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)
  Complete API documentation including the `formatted()` method for string interpolation.

### Interactive References

- **Java Almanac - Text Blocks**: [javaalmanac.io/features/text-blocks/](https://javaalmanac.io/features/text-blocks/)
  Interactive examples with runnable code demonstrating text block syntax and patterns.

- **Java Tutorials - Text Blocks**: [docs.oracle.com/javase/tutorial/java/data/textblocks.html](https://docs.oracle.com/javase/tutorial/java/data/textblocks.html)
  Official Oracle tutorial covering text block basics and common use cases.

### Code Repository

- **GitHub Repository - blog-9mac-dev-code**: [github.com/dawid-swist/blog-9mac-dev-code](https://github.com/dawid-swist/blog-9mac-dev-code)
  All examples from this article with complete JUnit test suite:
  ```bash
  git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
  cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
  ../../gradlew test --tests *part5*
  ```

### Additional Reading

- **Brian Goetz - State of the Specification**: Java language architect's updates on evolution
- **Inside Java Podcast - Text Blocks Episodes**: Design decisions and implementation details
- **Effective Java (3rd Edition)**: Consider using text blocks for clarity when maintaining embedded language strings

---

## Summary.
### Implementation Checklist for Your Code

When migrating to text blocks:

- ✅ Identify multi-line string literals (JSON, SQL, HTML, XML)
- ✅ Replace concatenation with triple-quote delimiters
- ✅ Verify indentation is correct (closing `"""` determines strip level)
- ✅ Remove unnecessary escape sequences (`\"` becomes `"`)
- ✅ Use `formatted()` for parameterized strings instead of multiple calls
- ✅ Test edge cases (empty lines, trailing whitespace, special characters)
- ✅ Update test fixtures to use text blocks for readability

### Common Mistakes to Avoid

1. **Forgetting newline after opening `"""`**
   ```java
   // Wrong - content on same line as opening delimiter
   String block = """Hello""";

   // Correct - newline after opening delimiter
   String block = """
       Hello""";
   ```

2. **Misunderstanding indent stripping**
   ```java
   // This strips 4 spaces (closing """ is indented at 4 spaces)
   String block = """
       Content""";  // Result: "Content\n"

   // This strips 0 spaces (closing """ at left margin, content not indented)
   String block = """
   Content""";  // Result: "Content\n"

   // This doesn't strip anything (closing """ at left margin, content indented)
   String block = """
       Content
   """;  // Result: "    Content\n"
   ```

3. **Forgetting that trailing whitespace is removed**
   ```java
   // Trailing spaces after "text" are removed
   String block = """
       text   \
       more
       """;  // Use \s if you need trailing space
   ```

4. **Using `formatted()` with SQL (SQL Injection Risk)**
   ```java
   // DANGEROUS: This is vulnerable to SQL injection!
   String sql = """
       SELECT * FROM table WHERE name = '%s'
       """.formatted(name);

   // SAFE: Always use PreparedStatement with parameterized queries
   String query = """
       SELECT * FROM table WHERE name = ?
       """;
   PreparedStatement ps = connection.prepareStatement(query);
   ps.setString(1, name);
   ```
---

*Written for [blog.9mac.dev](https://blog.9mac.dev)*
*Part of the "Java 17 Features Every Senior Developer Should Know" series*

**Previous**: [Part 4 - Pattern Matching & Switch Expressions](part-4-pattern-matching-and-switch.md)
**Next**: [Part 6 - Syntax Cheat Sheet & Reference Guide](part-6-syntax-cheat-sheet.md)
