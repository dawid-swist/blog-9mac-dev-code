# Java 17 Features Every Senior Developer Should Know - Part 5: Text Blocks

This is **Part 5** of a comprehensive series on essential Java 17+ features that every senior developer must understand when working with modern Java. Whether you're migrating from Java 8, 11, or 13, or you're mentoring junior developers, this series covers the language features that will define your production code quality and team productivity for the next decade.

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

If you've ever embedded JSON, SQL, HTML, or XML in Java, you know the pain:

```java
// Before Java 15: String concatenation hell
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 30,\n" +
              "  \"email\": \"alice@example.com\"\n" +
              "}";
```

Every line needs:
- The `+` operator
- Manual `\n` escape sequences
- Quote escaping with `\"`
- Careful indentation that doesn't reflect the actual data structure

Copy-paste a JSON object from a tool? Good luck reformatting it.

Write a SQL query? The syntax is buried in escape characters.

Maintain HTML templates? It's unreadable.

Text Blocks (Java 15) solve this: **write multi-line strings the way you'd write them in any other language.**

---

## What Are Text Blocks?

A **text block** is a multi-line string literal delimited by triple-double-quotes (`"""`):

```java
// Java 15+: Text blocks - clean and readable
String json = """
    {
        "name": "Alice",
        "age": 30,
        "email": "alice@example.com"
    }
    """;
```

The compiler automatically:
- Normalizes line breaks to `\n`
- Strips common leading whitespace
- Removes trailing whitespace
- Preserves essential formatting

No escape sequences. No concatenation. No ceremony.

## Before and After

### JSON Example

```java
// Before: Noisy and error-prone
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 30\n" +
              "}";

// After: Clean and readable
String json = """
    {
        "name": "Alice",
        "age": 30
    }
    """;
```

### SQL Example

```java
// Before: Query logic obscured by escape characters
String query = "SELECT u.name, u.email\n" +
               "FROM users u\n" +
               "WHERE u.active = true\n" +
               "ORDER BY u.created_at DESC";

// After: Readable as actual SQL
String query = """
    SELECT u.name, u.email
    FROM users u
    WHERE u.active = true
    ORDER BY u.created_at DESC
    """;
```

### HTML Example

```java
// Before: Can't easily copy-paste HTML
String html = "<html>\n" +
              "  <body>\n" +
              "    <h1>Welcome</h1>\n" +
              "  </body>\n" +
              "</html>";

// After: Paste HTML directly
String html = """
    <html>
        <body>
            <h1>Welcome</h1>
        </body>
    </html>
    """;
```

## Key Features

### No Escape Sequences (Mostly)

Regular strings inside text blocks don't need escaping:

```java
String html = """
    <p>The file is at: C:\Users\alice\file.txt</p>
    """;
// No need to escape backslashes!
```

Exception: You still need `\"` for quotes inside the text block.

### Indentation Handling

The compiler strips common leading whitespace:

```java
String code = """
    public void hello() {
        System.out.println("World");
    }
    """;
// Common 4-space indentation is removed - the string has no leading spaces
```

### Works with String Methods

Text blocks produce regular strings—they work with all String methods:

```java
String json = """
    {
        "name": "Alice"
    }
    """;

// Regular string operations work
System.out.println(json.length());
System.out.println(json.contains("Alice"));
String formatted = json.formatted("value");  // Old-style formatting
```

## Simple Example

```java
public class TextBlockExample {
    public static void main(String[] args) {
        // Multi-line JSON literal
        String json = """
            {
                "title": "Java 17 Features",
                "author": "Alice",
                "year": 2024
            }
            """;

        System.out.println(json);

        // SQL query
        String sql = """
            SELECT id, name, email
            FROM users
            WHERE created_at > NOW() - INTERVAL '30 days'
            ORDER BY created_at DESC
            """;

        System.out.println("SQL:\n" + sql);
    }
}
```

**Output:**
```
{
    "title": "Java 17 Features",
    "author": "Alice",
    "year": 2024
}
SQL:
SELECT id, name, email
FROM users
WHERE created_at > NOW() - INTERVAL '30 days'
ORDER BY created_at DESC
```

## Key Insight

Text blocks eliminate one of Java's longest-standing pain points: working with embedded languages (JSON, SQL, HTML, XML). They make strings readable and maintainable by letting you write data the way you'd write it outside of Java code. This is especially valuable for configurations, templates, and DSLs embedded in applications.

---

## Want to Learn More?

This is just a preview of what text blocks can do. **[Read the full Part 5 article](part-5-text-blocks.md)** where you'll discover:

- Detailed indentation rules and gotchas
- Escape sequences and special characters
- Using text blocks with formatting methods
- Text blocks in APIs (parsing JSON, executing SQL)
- Common patterns and anti-patterns
- Integration with other Java 17 features
- 15+ practical examples with real-world use cases
- Performance considerations
- Migration from traditional string concatenation

The full article includes comprehensive examples covering JSON, SQL, HTML, XML, and custom formats.

**All code examples are runnable in the GitHub repository:**
```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```
