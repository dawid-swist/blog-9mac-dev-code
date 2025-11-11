# Java 17 Features Every Senior Developer Should Know - Part 5: Text Blocks

**Part 5 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

**This is a teaser article.** A quick introduction to the feature, the problem it solves, and simple examples. Read the full article for deep dive with indentation rules, escape sequences, and 15+ practical examples with JSON, SQL, HTML, and XML.

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

## Read the Full Article

Discover more in **[Part 5: Text Blocks](part-5-text-blocks.md)**:
- Indentation rules and gotchas
- Escape sequences and special characters
- Using text blocks with formatting methods
- Text blocks in APIs (parsing JSON, executing SQL)
- 15+ practical examples with JSON, SQL, HTML, XML
- Performance considerations
- Migration from traditional string concatenation

**All code examples are in the GitHub repository:**
```
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 6 - Syntax Cheat Sheet (complete reference)
