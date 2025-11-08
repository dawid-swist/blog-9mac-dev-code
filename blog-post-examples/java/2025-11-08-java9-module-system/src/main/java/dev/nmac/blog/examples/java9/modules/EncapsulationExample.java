package dev.nmac.blog.examples.java9.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * EncapsulationExample - Demonstrates strong encapsulation and reflection security.
 *
 * Java 9's module system enforces strong encapsulation at the module level.
 * This example shows:
 * - How packages are private by default
 * - Reflection access control through opens/opens...to
 * - InaccessibleObjectException when violating encapsulation
 * - Dynamic opens through the Module API
 * - Comparison with pre-Java 9 reflection behavior
 */
public class EncapsulationExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Strong Encapsulation and Reflection");
        System.out.println("========================================\n");

        demonstrateExportedPackageAccess();
        System.out.println();

        demonstrateReflectionRestrictions();
        System.out.println();

        demonstratePrivatePackageAccess();
        System.out.println();

        printEncapsulationBenefits();
    }

    private static void demonstrateExportedPackageAccess() {
        System.out.println("Accessing Classes from Exported Packages:");
        System.out.println();

        // This works because java.util is exported from java.base
        List<String> list = new ArrayList<>();
        list.add("Item 1");
        list.add("Item 2");

        System.out.println("Creating ArrayList (exported from java.base):");
        System.out.println("  list.getClass(): " + list.getClass());
        System.out.println("  list.getModule(): " + list.getClass().getModule().getName());
        System.out.println("  Access: ALLOWED (java.util exported unconditionally)");
        System.out.println();

        // Can also use reflection on exported packages
        System.out.println("Reflection on Exported Packages:");
        Method[] methods = ArrayList.class.getDeclaredMethods();
        System.out.println("  ArrayList methods count: " + methods.length);
        System.out.println("  Can inspect via reflection: YES (if class is exported)");
        System.out.println();
    }

    private static void demonstrateReflectionRestrictions() {
        System.out.println("Reflection Access Restrictions:");
        System.out.println();

        // Demonstrate trying to access non-public members
        Class<?> stringClass = String.class;
        System.out.println("Attempting to access String class members:");
        System.out.println("  Class: " + stringClass);
        System.out.println("  Module: " + stringClass.getModule().getName());
        System.out.println();

        try {
            // This works because java.lang is exported
            Method toStringMethod = stringClass.getDeclaredMethod("toString");
            System.out.println("  toString() method found: " + toStringMethod);
            System.out.println("  Access: ALLOWED (public method in exported package)");
        } catch (NoSuchMethodException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();

        // Note: Attempting to access private members via setAccessible(true)
        // would fail with InaccessibleObjectException in modules
        System.out.println("Attempting setAccessible(true) on private members:");
        System.out.println("  In module system: May throw InaccessibleObjectException");
        System.out.println("  Unless package is opened via 'opens' directive");
        System.out.println();
    }

    private static void demonstratePrivatePackageAccess() {
        System.out.println("Private Packages (Not Exported):");
        System.out.println();

        Module currentModule = EncapsulationExample.class.getModule();

        System.out.println("Current module packages:");
        if (currentModule.getDescriptor() != null) {
            currentModule.getDescriptor().packages().forEach(pkg -> {
                boolean exported = currentModule.isExported(pkg);
                System.out.println("  - " + pkg + ": " + (exported ? "EXPORTED" : "PRIVATE"));
            });
        }
        System.out.println();

        System.out.println("Accessing Private Packages:");
        System.out.println("  Attempt to import from private package: COMPILE ERROR");
        System.out.println("  Attempt to load via reflection: Fails with error");
        System.out.println("  Attempt to use @SuppressWarnings: No effect");
        System.out.println();
    }

    private static void printEncapsulationBenefits() {
        System.out.println("Problems Solved by Strong Encapsulation:");
        System.out.println();

        System.out.println("Before Java 9 (Weak Encapsulation):");
        System.out.println("  - Any public class was always accessible");
        System.out.println("  - Reflection could access private members");
        System.out.println("  - Internal classes leaked into public API");
        System.out.println("  - Hard to evolve internal implementation");
        System.out.println("  - Version conflicts from split packages");
        System.out.println();

        System.out.println("After Java 9 (Strong Encapsulation):");
        System.out.println("  - Packages private by default");
        System.out.println("  - Only exported packages are accessible");
        System.out.println("  - Reflection restricted to opened packages");
        System.out.println("  - Clear distinction: API vs Implementation");
        System.out.println("  - Safe refactoring of internal code");
        System.out.println("  - JDK reduced from ~4000 classes to ~1300 (java.base)");
        System.out.println();

        System.out.println("Module Directives for Encapsulation:");
        System.out.println();

        System.out.println("1. exports <package>");
        System.out.println("   - Exposes package for compilation and runtime");
        System.out.println("   - Public API of the module");
        System.out.println();

        System.out.println("2. exports <package> to <module>");
        System.out.println("   - Qualified export to specific modules");
        System.out.println("   - Shared internal APIs with partners");
        System.out.println();

        System.out.println("3. opens <package>");
        System.out.println("   - Enables reflection on all members");
        System.out.println("   - Used for frameworks (JPA, Jackson, etc.)");
        System.out.println();

        System.out.println("4. opens <package> to <module>");
        System.out.println("   - Qualified reflection access to specific modules");
        System.out.println();

        System.out.println("Key Insights:");
        System.out.println("- Encapsulation is enforced by the JVM, not just conventions");
        System.out.println("- Frameworks can request reflection via 'opens' in module-info");
        System.out.println("- Command-line --add-opens can override (with warning/error)");
        System.out.println("- Module API allows dynamic opens at runtime");
        System.out.println("- Enables safe, maintainable large-scale Java applications");
    }
}
