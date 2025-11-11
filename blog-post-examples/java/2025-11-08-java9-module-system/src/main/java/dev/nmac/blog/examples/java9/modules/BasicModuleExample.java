package dev.nmac.blog.examples.java9.modules;

/**
 * BasicModuleExample - Demonstrates fundamental module system concepts.
 *
 * Before Java 9, Java used packages as the only organizational unit.
 * The Module System adds a higher level of abstraction that groups related packages
 * together and provides explicit visibility control and dependency management.
 *
 * Key concepts demonstrated:
 * - What a module is (collection of packages + module descriptor)
 * - Module-info.java file purpose
 * - Basic module structure
 * - Default behavior: packages are module-private by default
 */
public class BasicModuleExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Basic Module System Concepts");
        System.out.println("========================================\n");

        // Demonstrate accessing the current module
        Module currentModule = BasicModuleExample.class.getModule();
        System.out.println("Current Module Information:");
        System.out.println("  Module Name: " + currentModule.getName());
        System.out.println("  Is Named: " + currentModule.isNamed());
        System.out.println("  ClassLoader: " + currentModule.getClassLoader());
        System.out.println();

        // Show module descriptor
        System.out.println("Module Descriptor:");
        var descriptor = currentModule.getDescriptor();
        if (descriptor != null) {
            System.out.println("  Name: " + descriptor.name());
            System.out.println("  Exports: " + descriptor.exports());
            System.out.println("  Requires: " + descriptor.requires());
            System.out.println("  Opens: " + descriptor.opens());
            System.out.println("  Packages: " + descriptor.packages());
        }
        System.out.println();

        // Demonstrate java base module (always available)
        Module javaBase = Object.class.getModule();
        System.out.println("Java Base Module:");
        System.out.println("  Module Name: " + javaBase.getName());
        System.out.println("  Is Named: " + javaBase.isNamed());
        System.out.println("  Module Descriptor: " + javaBase.getDescriptor().name());
        System.out.println();

        printKeyInsights();
    }

    private static void printKeyInsights() {
        System.out.println("Key Insights:");
        System.out.println();
        System.out.println("1. Module System Hierarchy:");
        System.out.println("   Module > Packages > Classes");
        System.out.println();
        System.out.println("2. Module Descriptor (module-info.java):");
        System.out.println("   - Located at module root");
        System.out.println("   - Defines module name and directives");
        System.out.println("   - Compiled to module-info.class");
        System.out.println();
        System.out.println("3. Strong Encapsulation:");
        System.out.println("   - Packages are module-private by default");
        System.out.println("   - Must explicitly export packages");
        System.out.println("   - Reflection access restricted by default");
        System.out.println();
        System.out.println("4. Two Types of Modules:");
        System.out.println("   - Named modules (have module-info.java)");
        System.out.println("   - Unnamed module (legacy code on classpath)");
    }
}
