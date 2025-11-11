# Java 17 Features Every Senior Developer Should Know - Part 2: Records

**Part 2 of 6** - Essential Java 17+ features for senior developers upgrading from Java 8, 11, or 13.

**This is a teaser article.** Quick intro and simple example. Read the full article for 10+ real-world examples, custom constructors, and validation.

---

## Why This Matters

For 20+ years, creating a simple data class meant writing the same ceremony over and over: private final fields, constructor, getters, `equals()`, `hashCode()`, and `toString()`. The most common data structure in Java required 30-50 lines of boilerplate.

Other languages solved this decades ago. Scala case classes (2003), Kotlin data classes (2016), even C# record types (2020)—all understood that **data carriers should be one line, not thirty.**

Java 17 finally brought a native solution: **Records.**

---

## What are Records?

A **record** is a special class designed to be an immutable carrier for data. When you declare a record, the compiler automatically generates constructors, accessors, `equals()`, `hashCode()`, and `toString()`.

**Before Java 16:**
```java
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }
    public int x() { return x; }
    public int y() { return y; }

    @Override public boolean equals(Object o) { /* 10 lines */ }
    @Override public int hashCode() { return Objects.hash(x, y); }
    @Override public String toString() { return "Point[x=" + x + ", y=" + y + "]"; }
}
```

**After Java 16:**
```java
public record Point(int x, int y) {}
```

That's it. The compiler generates everything.

---

## Simple Example

```java
public record Book(String title, String author, int year) {}

public class RecordExample {
    public static void main(String[] args) {
        var book = new Book("Clean Code", "Robert Martin", 2008);

        System.out.println(book);  // Book[title=Clean Code, author=Robert Martin, year=2008]
        System.out.println("Title: " + book.title());

        var book2 = new Book("Clean Code", "Robert Martin", 2008);
        System.out.println("Equal: " + book.equals(book2));  // true
    }
}
```

---

## Key Characteristics

- **Immutable** - Fields are `private final`, no setters
- **No inheritance** - Records are implicitly final
- **Perfect for** - DTOs, value objects, domain entities

---

## Key Insight

Records eliminate boilerplate without sacrificing type safety. They're the native Java solution to what Project Lombok tried to solve. Your code expresses intent: "immutable data carrier."

---

## Read the Full Article

**[Part 2: Records](part-2-records.md)** includes:
- Generic records with type parameters
- Custom constructors and validation
- Sealed record hierarchies
- 10+ real-world examples

**All code examples are in the GitHub repository:**
```
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

**Next in the series:** Part 3 - Sealed Classes (controlled inheritance hierarchies)
