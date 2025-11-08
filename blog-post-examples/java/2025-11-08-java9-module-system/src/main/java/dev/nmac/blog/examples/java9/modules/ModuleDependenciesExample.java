package dev.nmac.blog.examples.java9.modules;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleDescriptor.Requires.Modifier;

/**
 * ModuleDependenciesExample - Demonstrates module dependency management.
 *
 * The module system makes dependencies explicit through the 'requires' directive.
 * This example shows:
 * - How modules declare dependencies
 * - The difference between transitive and direct dependencies
 * - How to query module relationships at runtime
 * - The Module API for inspecting dependencies
 */
public class ModuleDependenciesExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Module Dependencies and Module Relationships");
        System.out.println("========================================\n");

        // Get current module
        Module currentModule = ModuleDependenciesExample.class.getModule();
        ModuleDescriptor descriptor = currentModule.getDescriptor();

        System.out.println("Current Module: " + currentModule.getName());
        System.out.println();

        // Examine all requires directives
        System.out.println("Module Dependencies (requires directives):");
        if (descriptor != null && !descriptor.requires().isEmpty()) {
            descriptor.requires().stream().limit(3).forEach(req -> {
                System.out.println("  - Module: " + req.name());
                boolean isTransitive = req.modifiers().contains(Modifier.TRANSITIVE);
                System.out.println("    Transitive: " + isTransitive);
                System.out.println();
            });
            System.out.println("  (" + descriptor.requires().size() + " total module dependencies)");
        } else {
            System.out.println("  (No explicit requires directives, uses java.base implicitly)");
            System.out.println();
        }

        // Show relationship queries
        System.out.println("Module Relationship Queries:");
        System.out.println();

        // Check if current module can read java.base
        Module javaBase = Object.class.getModule();
        boolean canReadBase = currentModule.canRead(javaBase);
        System.out.println("  Current module can read java.base: " + canReadBase);
        System.out.println();

        // Get all modules this module reads
        System.out.println("All modules that current module reads:");
        var layer = currentModule.getLayer();
        if (layer != null) {
            layer.configuration().modules().forEach(resolvedModule -> {
                try {
                    Module mod = layer.findModule(resolvedModule.name()).orElse(null);
                    if (mod != null && currentModule.canRead(mod)) {
                        System.out.println("    - " + mod.getName());
                    }
                } catch (Exception e) {
                    // Ignore
                }
            });
        } else {
            System.out.println("  (Unnamed modules don't have a layer)");
        }
        System.out.println();

        printDependencyTypes();
    }

    private static void printDependencyTypes() {
        System.out.println("Types of Requires Directives:");
        System.out.println();

        System.out.println("1. requires <module>:");
        System.out.println("   - Mandatory dependency");
        System.out.println("   - Required at compile and runtime");
        System.out.println("   - Not transitive (implicit)");
        System.out.println("   Example: requires java.base;");
        System.out.println();

        System.out.println("2. requires transitive <module>:");
        System.out.println("   - Re-exports dependencies");
        System.out.println("   - Consumers of this module automatically get access");
        System.out.println("   - Used for facade modules");
        System.out.println("   Example: requires transitive java.logging;");
        System.out.println();

        System.out.println("3. requires static <module>:");
        System.out.println("   - Optional dependency");
        System.out.println("   - Required at compile time only");
        System.out.println("   - Not required at runtime");
        System.out.println("   - Useful for optional features");
        System.out.println("   Example: requires static com.logging.advanced;");
        System.out.println();

        System.out.println("Key Insights:");
        System.out.println("- Every module implicitly requires java.base");
        System.out.println("- Dependencies must be explicitly declared");
        System.out.println("- Transitive dependencies reduce consumer burden");
        System.out.println("- Static dependencies enable optional functionality");
    }
}
