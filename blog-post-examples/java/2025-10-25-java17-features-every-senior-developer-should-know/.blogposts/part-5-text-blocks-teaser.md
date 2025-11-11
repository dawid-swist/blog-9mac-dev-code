# Java 17 Features Every Senior Developer Should Know - Part 5: Text Blocks

**Part 5 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

Text blocks solve the embedded language problem: write JSON, SQL, HTML, and XML directly without escape sequences or concatenation. This article covers indentation rules, string interpolation, and practical examples for real-world APIs and configurations.

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

Every line needs the `+` operator, manual `\n` escapes, and quote escaping with `\"`. Copy-paste a JSON object from a tool? Good luck reformatting it.

Text Blocks (Java 15) solve this: **write multi-line strings the way you'd write them in any other language.**

---

## What are Text Blocks?

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

No escape sequences. No concatenation. No ceremony. The compiler automatically handles line breaks and indentation.

---

## Before and After

```java
// Before: Noisy and error-prone
String sql = "SELECT u.name, u.email\n" +
             "FROM users u\n" +
             "WHERE u.active = true\n" +
             "ORDER BY u.created_at DESC";

// After: Readable as actual SQL
String sql = """
    SELECT u.name, u.email
    FROM users u
    WHERE u.active = true
    ORDER BY u.created_at DESC
    """;
```

---

## Key Insight

Text blocks eliminate one of Java's longest-standing pain points: working with embedded languages (JSON, SQL, HTML, XML). They make strings readable by letting you write data the way you'd write it outside of Java code.

---

## Practical Examples

### HTML Templates

```java
String htmlTemplate = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>User Profile</title>
    </head>
    <body>
        <h1>Welcome, %s</h1>
        <p>Account created: %s</p>
    </body>
    </html>
    """;

String html = htmlTemplate.formatted(userName, dateCreated);
```

### SQL Queries

```java
String selectQuery = """
    SELECT u.id, u.name, u.email, COUNT(o.id) as order_count
    FROM users u
    LEFT JOIN orders o ON u.id = o.user_id
    WHERE u.active = true
    GROUP BY u.id, u.name, u.email
    ORDER BY order_count DESC
    LIMIT 10
    """;
```

### JSON Configuration

```java
String config = """
    {
        "database": {
            "host": "localhost",
            "port": 5432,
            "name": "myapp"
        },
        "logging": {
            "level": "INFO",
            "format": "json"
        }
    }
    """;

JsonObject json = JsonParser.parse(config);
```

### XML Document

```java
String xmlRequest = """
    <?xml version="1.0" encoding="UTF-8"?>
    <order>
        <customer id="12345">
            <name>John Doe</name>
            <email>john@example.com</email>
        </customer>
        <items>
            <item>
                <product>Widget</product>
                <quantity>5</quantity>
            </item>
        </items>
    </order>
    """;
```

## Indentation and Whitespace

Text blocks handle indentation automatically:

```java
// Recommended: Closing """ on separate line
String text = """
    Line 1
    Line 2
    Line 3
    """;

// Also valid: No trailing newline
String text = """
    Line 1
    Line 2
    Line 3""";

// Escapes still work
String escaped = """
    Name: \s
    Value: \t
    """;
```

## String Interpolation (Template Strings - Java 21+)

```java
String name = "Alice";
int age = 30;

// Using formatted()
String message = """
    Name: %s
    Age: %d
    """.formatted(name, age);

// Using String.format()
String message = String.format("""
    Name: %s
    Age: %d
    """, name, age);
```

## Processing embedded data

```java
public class JsonHandler {
    public static void parseConfig(String jsonFile) {
        String jsonData = readFile(jsonFile);

        String query = """
            SELECT * FROM config
            WHERE data = '%s'
            """.formatted(jsonData);

        return database.execute(query);
    }
}
```

## Read the Full Article

Discover much more in **[Part 5: Text Blocks on blog.9mac.dev]([BLOG_LINK_HERE])**:
- Indentation rules and gotchas
- Escape sequences and special characters
- Using text blocks with formatting methods
- Text blocks in APIs (parsing JSON, executing SQL)
- 15+ practical examples with JSON, SQL, HTML, XML
- Performance considerations
- Migration from traditional string concatenation
- Integration with templating libraries

## GitHub Repository

All code examples are ready to clone and run:

```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 6 - Syntax Cheat Sheet (complete reference)
