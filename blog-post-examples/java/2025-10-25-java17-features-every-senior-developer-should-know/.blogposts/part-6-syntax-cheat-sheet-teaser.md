# Java 17 Features Every Senior Developer Should Know - Part 6: Quick Reference

This is **Part 6** (final) of a comprehensive series on essential Java 17+ features that every senior developer must understand when working with modern Java. This teaser provides a quick navigation guide to the syntax cheat sheet.

**This series covers:**
- **Part 1**: Local variable type inference with `var` keyword
- **Part 2**: Immutable data carriers with Records
- **Part 3**: Controlled inheritance hierarchies with Sealed Classes
- **Part 4**: Pattern matching and modern switch expressions
- **Part 5**: Multi-line string literals with Text Blocks
- **Part 6**: Complete syntax cheat sheet and migration guide

---

## Why This Reference Matters

Throughout this series, we've explored six major Java 17 features. But switching between five long articles to remember syntax is inefficient. Senior developers need a **single, printable reference** they can keep at their desk.

Part 6 provides:
- Quick syntax for all 6 features
- When to use each feature
- Common pitfalls and gotchas
- Migration checklist
- Combined example showing features working together

This is the **TL;DR** for the entire series—perfect for:
- Quick syntax lookup during development
- Team training and onboarding
- Migration planning
- Code review guidelines
- Printing and keeping at your desk

---

## What's in the Full Cheat Sheet

The complete Part 6 article includes quick-reference sections for:

### 1. var Keyword (Java 10)
- When to use (obvious types, generics, streams)
- When to avoid (unclear types, supertype needed)
- Quick syntax examples
- Common mistakes

### 2. Records (Java 16)
- Declaration syntax
- When to use (DTOs, value objects, configs)
- When NOT to use (mutability, inheritance)
- Compact constructors
- Custom methods

### 3. Sealed Classes (Java 17)
- Sealed class syntax with `permits`
- Three subclass modifiers: `final`, `sealed`, `non-sealed`
- Omitting `permits` when subclasses are in same file
- Reflection API: `isSealed()`, `getPermittedSubclasses()`
- When to use (domain models, APIs)

### 4. Pattern Matching (Java 16)
- Pattern matching with `instanceof`
- Type patterns and binding
- Pattern matching in method parameters
- Common pattern types

### 5. Switch Expressions (Java 14)
- Arrow syntax: `case X ->` (no fall-through)
- Multiple cases in one line
- `yield` for complex expressions
- Exhaustiveness with sealed types
- Guard conditions

### 6. Text Blocks (Java 15)
- Triple-quote syntax: `"""`
- Indentation handling
- Escape sequences inside text blocks
- String operations on text blocks

### 7. Combined Example
Real-world scenario showing all features working together:
- Records for data
- Sealed classes for type hierarchy
- Pattern matching in switch
- Text blocks for embedded languages

### 8. Migration Checklist
- Step-by-step guide for upgrading Java code
- What to update first (var keyword)
- What requires careful planning (sealed classes)
- Testing strategies after migration

---

## Example: Quick Lookup

Need to remember record syntax? From the cheat sheet:

```java
// Quick reference
record Name(Type1 field1, Type2 field2) {
    // Optional custom methods
}

// Compact constructor for validation
record Point(int x, int y) {
    public Point {
        if (x < 0 || y < 0) throw new IllegalArgumentException();
    }
}
```

Need switch expression syntax?

```java
var result = switch (value) {
    case PATTERN1 -> action1;
    case PATTERN2, PATTERN3 -> action2;
    default -> defaultAction;
};
```

Need pattern matching syntax?

```java
if (obj instanceof String s) {
    // 's' is already the String
}

// In switch with sealed types
return switch (animal) {
    case Dog dog -> "Woof!";
    case Cat cat -> "Meow!";
};
```

---

## Printing the Cheat Sheet

The full Part 6 article is designed to be:
- ✅ Single-page printable (or 2-3 pages with all examples)
- ✅ High contrast for readability
- ✅ Organized by feature with clear sections
- ✅ Examples are concise (fit on page)
- ✅ Suitable for desk reference or team wiki

---

## Key Insight

A good cheat sheet isn't a replacement for deep understanding—it's a tool for efficient recall. After reading the full series, you'll return to Part 6 frequently: during code review, when mentoring, during migrations, or when you need to remember exact syntax.

The six features in this series work together. Records store data (Part 2), sealed classes control hierarchies (Part 3), pattern matching works with sealed types (Part 4), and text blocks embed external languages (Part 5). The cheat sheet shows how they interconnect.

---

## Want the Full Reference?

**[Read the complete Part 6 cheat sheet](part-6-syntax-cheat-sheet.md)** which includes:

- Full syntax examples for all 6 features
- Detailed "when to use" and "when to avoid" sections
- Common pitfalls and compiler error messages
- Real-world combined example
- Step-by-step migration checklist
- Troubleshooting guide
- Performance tips
- Links to relevant JEPs

The cheat sheet is organized for quick lookup—flip to the feature you need and find the syntax immediately.

---

## Complete Series Overview

You've now explored the essential Java 17 features every senior developer should know:

1. **var keyword** - Cleaner variable declarations
2. **Records** - Data carriers with zero boilerplate
3. **Sealed Classes** - Controlled inheritance hierarchies
4. **Pattern Matching** - Type checking without redundant casts
5. **Switch Expressions** - Modern conditional logic
6. **Text Blocks** - Readable multi-line string literals

Together, these features represent 20+ years of language evolution, solving real problems that affected developer productivity and code quality.

**All code examples are runnable in the GitHub repository:**
```bash
git clone https://github.com/dawid-swist/blog-9mac-dev-code.git
cd blog-post-examples/java/2025-10-25-java17-features-every-senior-developer-should-know
../../gradlew test
```

---

*Thank you for reading the "Java 17 Features Every Senior Developer Should Know" series. We hope these articles help you upgrade your Java skills and improve your production code.*
