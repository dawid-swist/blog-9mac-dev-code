package dev.nmac.blog.examples.java9.modules;

import java.lang.module.ModuleDescriptor;

/**
 * ModuleExportsExample - Demonstrates package export and visibility control.
 *
 * One of the main goals of the module system is strong encapsulation.
 * By default, packages are module-private. Packages must be explicitly exported
 * to be accessible from other modules.
 *
 * This example shows:
 * - Package exports through module-info.java
 * - Selective exports (exports...to)
 * - Runtime inspection of exports
 * - The concept of public vs exported packages
 */
public class ModuleExportsExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Module Package Exports and Visibility");
        System.out.println("========================================\n");

        Module currentModule = ModuleExportsExample.class.getModule();
        ModuleDescriptor descriptor = currentModule.getDescriptor();

        System.out.println("Current Module: " + currentModule.getName());
        System.out.println();

        // Show exported packages
        System.out.println("Exported Packages from " + currentModule.getName() + ":");
        if (descriptor != null && !descriptor.exports().isEmpty()) {
            descriptor.exports().forEach(export -> {
                System.out.println("  Package: " + export.source());
                if (!export.targets().isEmpty()) {
                    System.out.println("    Exported to: " + export.targets());
                } else {
                    System.out.println("    Exported to: all modules (unconditional)");
                }
                System.out.println();
            });
        } else {
            System.out.println("  (No explicit exports in module descriptor)");
            System.out.println();
        }

        // Show packages in module
        System.out.println("All Packages in " + currentModule.getName() + ":");
        if (descriptor != null && !descriptor.packages().isEmpty()) {
            descriptor.packages().forEach(pkg -> {
                boolean isExported = isPackageExported(descriptor, pkg);
                System.out.println("  - " + pkg + " [" + (isExported ? "exported" : "private") + "]");
            });
        }
        System.out.println();

        // Runtime package visibility check
        System.out.println("Runtime Package Visibility Checks:");
        String packageName = "dev.nmac.blog.examples.java9.modules";
        System.out.println("  Package '" + packageName + "':");
        System.out.println("    - isExported(unconditionally): " + currentModule.isExported(packageName));
        System.out.println();

        printExportPatterns();
    }

    private static boolean isPackageExported(ModuleDescriptor descriptor, String packageName) {
        return descriptor.exports().stream()
                .anyMatch(export -> export.source().equals(packageName));
    }

    private static void printExportPatterns() {
        System.out.println("Export Patterns in module-info.java:");
        System.out.println();

        System.out.println("1. Unconditional Export:");
        System.out.println("   exports com.example.api;");
        System.out.println("   - Package accessible from ALL modules");
        System.out.println("   - Public API of your module");
        System.out.println();

        System.out.println("2. Selective Export (exports...to):");
        System.out.println("   exports com.example.internal to com.partner.module;");
        System.out.println("   - Package accessible only to specified modules");
        System.out.println("   - Useful for shared internal APIs");
        System.out.println("   - Maintains encapsulation with trusted partners");
        System.out.println();

        System.out.println("3. No Export:");
        System.out.println("   // No exports directive for a package");
        System.out.println("   - Package is module-private");
        System.out.println("   - Hidden from all other modules");
        System.out.println("   - Not accessible via normal import");
        System.out.println();

        System.out.println("Key Insights:");
        System.out.println();
        System.out.println("Problem Solved:");
        System.out.println("- Before Java 9: public classes were always accessible");
        System.out.println("  (weak encapsulation, fragile base class problem)");
        System.out.println("- After Java 9: only exported packages are accessible");
        System.out.println("  (strong encapsulation, clear API boundaries)");
        System.out.println();

        System.out.println("Benefits:");
        System.out.println("- Clear separation between public API and internal implementation");
        System.out.println("- Prevents accidental dependencies on internal classes");
        System.out.println("- Enables safe refactoring of internal code");
        System.out.println("- Reduces JAR file bloat by only including what's needed");
    }
}
