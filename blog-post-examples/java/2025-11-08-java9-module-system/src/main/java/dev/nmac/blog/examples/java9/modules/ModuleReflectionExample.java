package dev.nmac.blog.examples.java9.modules;

import java.lang.module.ModuleDescriptor;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * ModuleReflectionExample - Demonstrates the Module API for runtime introspection.
 *
 * Java 9 introduces the Module class and related APIs for runtime inspection of modules.
 * This example shows:
 * - Using the Module API to examine modules at runtime
 * - Querying module properties and relationships
 * - Working with module layers
 * - Key methods on the Module class
 */
public class ModuleReflectionExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Module API and Runtime Introspection");
        System.out.println("========================================\n");

        demonstrateModuleAPI();
        System.out.println();

        demonstrateClassToModule();
        System.out.println();

        demonstrateModuleQueries();
        System.out.println();

        printAPIDocumentation();
    }

    private static void demonstrateModuleAPI() {
        System.out.println("Module API Basics:");
        System.out.println();

        Module module = ModuleReflectionExample.class.getModule();

        System.out.println("Module.getName():");
        System.out.println("  " + module.getName());
        System.out.println();

        System.out.println("Module.isNamed():");
        System.out.println("  " + module.isNamed());
        System.out.println("  (true for named modules, false for unnamed/legacy)");
        System.out.println();

        System.out.println("Module.getClassLoader():");
        System.out.println("  " + module.getClassLoader());
        System.out.println();

        System.out.println("Module.getLayer():");
        System.out.println("  " + module.getLayer());
        System.out.println();

        ModuleDescriptor descriptor = module.getDescriptor();
        System.out.println("Module.getDescriptor():");
        System.out.println("  Returns ModuleDescriptor containing:");
        System.out.println("    - name()");
        System.out.println("    - requires()");
        System.out.println("    - exports()");
        System.out.println("    - opens()");
        System.out.println("    - uses()");
        System.out.println("    - provides()");
        System.out.println("    - packages()");
        System.out.println();
    }

    private static void demonstrateClassToModule() {
        System.out.println("Getting Module from Class:");
        System.out.println();

        // From any class, you can get its module
        Class<?> clazz = String.class;
        Module module = clazz.getModule();

        System.out.println("Class: " + clazz.getCanonicalName());
        System.out.println("Module: " + module.getName());
        System.out.println();

        // Try with java.util classes
        Class<?> utilClass = java.util.ArrayList.class;
        Module utilModule = utilClass.getModule();

        System.out.println("Class: " + utilClass.getCanonicalName());
        System.out.println("Module: " + utilModule.getName());
        System.out.println();
    }

    private static void demonstrateModuleQueries() {
        System.out.println("Module Relationship Queries:");
        System.out.println();

        Module current = ModuleReflectionExample.class.getModule();
        Module javaBase = Object.class.getModule();

        System.out.println("Module.canRead(Module):");
        System.out.println("  current.canRead(javaBase): " + current.canRead(javaBase));
        System.out.println("  Returns true if this module reads the given module");
        System.out.println();

        System.out.println("Module.isExported(String):");
        System.out.println("  current.isExported(\"java.lang\"): "
                + javaBase.isExported("java.lang"));
        System.out.println("  Checks if package is unconditionally exported");
        System.out.println();

        System.out.println("Module.isExported(String, Module):");
        System.out.println("  current.isExported(package, targetModule)");
        System.out.println("  Checks if package is exported to specific module");
        System.out.println();

        System.out.println("Module.getPackages():");
        Set<String> packages = javaBase.getPackages();
        System.out.println("  java.base has " + packages.size() + " packages");
        System.out.println("  Sample packages: " +
                packages.stream()
                        .limit(5)
                        .toList());
        System.out.println();
    }

    private static void printAPIDocumentation() {
        System.out.println("Key Module API Methods:");
        System.out.println();

        System.out.println("Identification:");
        System.out.println("  String getName()");
        System.out.println("  boolean isNamed()");
        System.out.println("  ClassLoader getClassLoader()");
        System.out.println();

        System.out.println("Metadata:");
        System.out.println("  ModuleDescriptor getDescriptor()");
        System.out.println("  Set<String> getPackages()");
        System.out.println("  ModuleLayer getLayer()");
        System.out.println();

        System.out.println("Relationship Queries:");
        System.out.println("  boolean canRead(Module)");
        System.out.println("  boolean isExported(String)");
        System.out.println("  boolean isExported(String, Module)");
        System.out.println("  boolean isOpen(String)");
        System.out.println("  boolean isOpen(String, Module)");
        System.out.println("  boolean canUse(Class<?>)");
        System.out.println();

        System.out.println("Dynamic Module Updates:");
        System.out.println("  Module addReads(Module)");
        System.out.println("  Module addExports(String, Module)");
        System.out.println("  Module addOpens(String, Module)");
        System.out.println("  Module addUses(Class<?>)");
        System.out.println();

        System.out.println("Key Insights:");
        System.out.println("- Module API available since Java 9");
        System.out.println("- Enables runtime introspection of module structure");
        System.out.println("- Supports dynamic module configuration");
        System.out.println("- Used by frameworks for dependency injection");
    }
}
