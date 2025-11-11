package dev.nmac.blog.examples.java9.modules;

/**
 * HelloWorld - Entry point for the Java 9 Module System examples.
 *
 * This application demonstrates the fundamentals of Java's Platform Module System (JPMS),
 * introduced in Java 9 to provide strong encapsulation and explicit dependency management
 * at a higher level of abstraction than packages.
 */
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Java 9 Module System Examples");
        System.out.println("========================================");
        System.out.println();

        System.out.println("Welcome to the Java 9+ Module System demonstration!");
        System.out.println();
        System.out.println("This project demonstrates:");
        System.out.println("  1. Basic module definitions (module-info.java)");
        System.out.println("  2. Module dependencies and requires directives");
        System.out.println("  3. Package exports and visibility control");
        System.out.println("  4. Module API reflection and introspection");
        System.out.println("  5. Strong encapsulation and security benefits");
        System.out.println();

        System.out.println("Run individual examples using Gradle tasks:");
        System.out.println("  ./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runBasicModule");
        System.out.println("  ./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleDependencies");
        System.out.println("  ./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleExports");
        System.out.println("  ./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runModuleReflection");
        System.out.println("  ./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runEncapsulation");
        System.out.println();
        System.out.println("Or run all examples:");
        System.out.println("  ./gradlew :blog-post-examples:java:2025-11-08-java9-module-system:runAllExamples");
        System.out.println();
    }
}
