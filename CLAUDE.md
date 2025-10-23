# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is the code repository for the blog.9mac.dev technical blog, containing practical examples and code snippets that accompany published articles. The repository demonstrates concepts in Java, Scala, Linux/Unix, macOS, Swift, and cloud technologies.

## Architecture

The repository is organized into several main directories:

- `blog-post-examples/`: Contains code examples organized by technology/language
  - `java/`: Java code examples and projects
  - `scala/`: Scala programming examples
  - `spring-framework-6/`: Spring Framework 6 demonstrations and examples
  - `dev-tools/`: Development tools, scripts, and configurations
- `subprojects/`: Reusable libraries and independent modules used across examples
- `tools/`: Auxiliary tools, scripts, and repository management utilities

## Build System

The project uses Gradle as the primary build tool with:
- Root-level `gradlew` and `gradlew.bat` wrapper scripts (Gradle 9.0.0)
- Multi-module project structure defined in `settings.gradle`
- Individual `build.gradle` files in subprojects under `blog-post-examples/`
- Java 21 as the target version for Java projects
- Spring Framework 6.2.7 for Spring examples

### Discovering Available Tasks
```bash
# List all tasks available from root
./gradlew tasks

# List tasks for a specific subproject (with all tasks including custom ones)
./gradlew :blog-post-examples:spring-framework-6:2025-05-16-CoffeeApp:tasks --all
```

## Common Commands

### Building and Running
```bash
# Build all projects
./gradlew build

# Run tests
./gradlew test

# Run specific Spring Framework 6 Coffee application examples
./gradlew :blog-post-examples:spring-framework-6:2025-05-16-CoffeeApp:runCoffeePureApp
./gradlew :blog-post-examples:spring-framework-6:2025-05-16-CoffeeApp:runCoffeeSpiApp
./gradlew :blog-post-examples:spring-framework-6:2025-05-16-CoffeeApp:runCoffeeSpringXmlApp
./gradlew :blog-post-examples:spring-framework-6:2025-05-16-CoffeeApp:runCoffeeSpringConfigApp
```

### Project Structure Navigation
Each directory typically corresponds to a specific blog post or series. Look for individual `README.md` files within subdirectories for specific instructions and context about the examples.

## Development Notes

- Examples are designed to be self-contained demonstrations of specific concepts
- Each major directory may have its own build configuration and dependencies
- Code examples use package naming convention: `dev.nmac.blog.examples.*`
- Directory naming often follows date-based convention (e.g., `2025-05-16-CoffeeApp`) corresponding to blog post dates

### Spring Framework 6 Examples

The Spring Framework 6 examples (particularly the CoffeeApp) demonstrate multiple dependency injection approaches:
- **Pure Java DI**: Manual dependency injection without frameworks (`CoffeePureApp`)
- **Java SPI**: Service Provider Interface pattern for discovery (`CoffeeSpiApp`)
- **Spring XML Configuration**: Traditional XML-based bean definitions (`CoffeeSpringXmlApp`)
- **Spring Java Configuration**: Annotation-based configuration with `@Configuration` (`CoffeeSpringConfigApp`)

Each variant uses the same core components (`CoffeeMaker`, `Water`, `CoffeeBean` interfaces) but wires them differently to illustrate various DI patterns.

## Blog Post Guidelines

### Language Policy
- **Communication with Claude Code**: Polish language only
- **ALL PRODUCTION content**: English only
  - Blog post markdown files: English
  - Source code: English
  - Code comments: English
  - JavaDoc/ScalaDoc/documentation: English
  - Variable/method/class names: English
  - Unit test names and assertions: English
  - README files: English

### Target Audience
- Senior developers updating their knowledge
- New programmers learning modern technologies
- Developers preparing for practical use

### Content Requirements
- Detailed description of each feature/concept
- Code examples with various combinations
- Professional but accessible language
- Each example: code block + unit test + explanation
- Avoid common examples from other blogs
- Include practical use cases
- Repository links for readers to clone and run

### Blog Post Format (MANDATORY)

Each example in blog posts must follow this structure:

1. **Concept description** (2-3 sentences)
2. **Code block** with `main()` method showing usage
3. **Code block** with unit tests using `@DisplayName` (BDD style)
4. **Output section** showing program output
5. **Key Insight** explaining the concept

#### Example Structure:

```java
// Concept description
public class ExampleClass {
    public static void main(String[] args) {
        // Usage demonstration
        System.out.println("result");
    }
}
```

```java
// Unit tests
@Test
@DisplayName("Should demonstrate feature behavior")
void shouldDemonstrateFeatureBehavior() {
    // Test with assertions
}
```

**Output:**
```
program output here
```

**Key Insight:** Explanation of the concept

### Strict Rules for Blog Posts

- **NO** exercises/practice sections (blog post, not tutorial)
- **NO** "Happy coding!", "Try it yourself", etc.
- Source code: detailed comments + `@DisplayName` tests
- Blog post: concise, focused on demonstrating features

### File Organization for Blog Content

- Blog posts stored in `.blogposts/` directory as markdown files (excluded from git)
- Full working code in project directory
- Concise examples
- Unit tests demonstrating API usage in single code blocks

### Reference Materials

Always verify information against official documentation and authoritative sources:
- Official language/framework documentation
- Almanac sites (e.g., javaalmanac.io for Java)
- Enhancement proposal documents (JEP, PEP, etc.)
- Official RFC/specification documents