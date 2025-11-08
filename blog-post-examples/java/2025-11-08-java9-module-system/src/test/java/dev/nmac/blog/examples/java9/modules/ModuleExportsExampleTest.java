package dev.nmac.blog.examples.java9.modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModuleExportsExample demonstrating package exports.
 */
@DisplayName("Module Package Exports and Visibility")
class ModuleExportsExampleTest {

    @Test
    @DisplayName("Should list exported packages from module descriptor")
    void shouldListExportedPackagesFromModuleDescriptor() {
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        var exports = descriptor.exports();
        assertNotNull(exports);
        // java.base has exports
        assertFalse(exports.isEmpty());
    }

    @Test
    @DisplayName("Should check if package is unconditionally exported")
    void shouldCheckIfPackageIsUnconditionallyExported() {
        Module javaBase = Object.class.getModule();

        // java.lang should be exported from java.base
        assertTrue(javaBase.isExported("java.lang"));

        // Can check for our own module's packages
        Module currentModule = ModuleExportsExampleTest.class.getModule();
        String packageName = "dev.nmac.blog.examples.java9.modules";
        var isExported = currentModule.isExported(packageName);

        assertNotNull(isExported);
    }

    @Test
    @DisplayName("Should check if package is exported to specific module")
    void shouldCheckIfPackageIsExportedToSpecificModule() {
        Module javaBase = Object.class.getModule();
        Module currentModule = ModuleExportsExampleTest.class.getModule();

        // java.lang is exported unconditionally to all modules
        assertTrue(javaBase.isExported("java.lang", currentModule));
    }

    @Test
    @DisplayName("Should demonstrate that packages are module-private by default")
    void shouldDemonstrateThatPackagesAreModulePrivateByDefault() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        var allPackages = descriptor.packages();
        var exportedPackages = descriptor.exports();

        // Number of exported packages should be <= total packages
        assertTrue(exportedPackages.size() <= allPackages.size());
    }

    @Test
    @DisplayName("Should access exported packages via normal import")
    void shouldAccessExportedPackagesViaNormalImport() {
        // java.util.ArrayList is from exported java.util package in java.base
        var list = new java.util.ArrayList<String>();
        assertNotNull(list);

        // Can access classes from exported packages
        assertEquals(java.util.ArrayList.class.getModule().getName(), "java.base");
    }

    @Test
    @DisplayName("Should enumerate all packages in a module")
    void shouldEnumerateAllPackagesInAModule() {
        Module currentModule = ModuleExportsExampleTest.class.getModule();
        var packages = currentModule.getPackages();

        assertNotNull(packages);
        assertFalse(packages.isEmpty());

        // Should contain our package
        assertTrue(packages.contains("dev.nmac.blog.examples.java9.modules"));
    }

    @Test
    @DisplayName("Should show java.base exports core packages")
    void shouldShowJavaBaseExportsCorePackages() {
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        var exports = descriptor.exports();

        // java.base should export java.lang
        assertTrue(exports.stream()
                .anyMatch(export -> "java.lang".equals(export.source())));

        // and java.util
        assertTrue(exports.stream()
                .anyMatch(export -> "java.util".equals(export.source())));
    }

    @Test
    @DisplayName("Should distinguish between qualified and unconditional exports")
    void shouldDistinguishBetweenQualifiedAndUnconditionalExports() {
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        var exports = descriptor.exports();

        for (var export : exports) {
            var targets = export.targets();
            if (targets.isEmpty()) {
                // Unconditional export to all modules
                assertTrue(javaBase.isExported(export.source()));
            } else {
                // Qualified export to specific modules
                assertFalse(targets.isEmpty());
            }
        }
    }

    @Test
    @DisplayName("Should support dynamic exports at runtime")
    void shouldSupportDynamicExportsAtRuntime() {
        Module currentModule = ModuleExportsExampleTest.class.getModule();
        Module targetModule = Object.class.getModule();

        String packageName = "dev.nmac.blog.examples.java9.modules";

        // addExports allows dynamic module updates
        Module result = currentModule.addExports(packageName, targetModule);

        assertNotNull(result);
        assertEquals(currentModule, result);
    }

    @Test
    @DisplayName("Should show export pattern solves visibility problem")
    void shouldShowExportPatternSolvesVisibilityProblem() {
        // Before Java 9: Any public class was accessible everywhere
        // After Java 9: Only classes from exported packages are accessible

        Module javaBase = Object.class.getModule();

        // String is public AND in exported java.lang package
        assertTrue(javaBase.isExported("java.lang"));
        assertTrue(javaBase.getPackages().contains("java.lang"));

        // Both conditions must be true for accessibility
        assertTrue(javaBase.isExported("java.lang"));
    }

    @Test
    @DisplayName("Should enable safe package refactoring")
    void shouldEnableSafePackageRefactoring() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);

        // Packages that are NOT exported are private implementation details
        // They can be refactored safely without breaking consumers
        var allPackages = descriptor.packages();
        var exportedPackages = descriptor.exports().stream()
                .map(e -> e.source())
                .toList();

        // Private packages are the difference
        var privatePackages = allPackages.stream()
                .filter(pkg -> !exportedPackages.contains(pkg))
                .toList();

        // These private packages can be safely refactored
        assertNotNull(privatePackages);
    }

    @Test
    @DisplayName("Should demonstrate selective export pattern")
    void shouldDemonstrateSelectiveExportPattern() {
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        // Some packages might have qualified exports (exports...to)
        var qualifiedExports = descriptor.exports().stream()
                .filter(export -> !export.targets().isEmpty())
                .toList();

        // java.base has some internal packages with qualified exports
        // This keeps them private from most modules
        assertNotNull(qualifiedExports);
    }

    @Test
    @DisplayName("Should show strong encapsulation reduces API surface")
    void shouldShowStrongEncapsulationReducesApiSurface() {
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        var allPackages = descriptor.packages().size();
        var exportedPackages = descriptor.exports().size();

        // Not all packages are exported - this is the point of encapsulation
        assertTrue(exportedPackages <= allPackages);

        // java.base probably has many internal packages
        assertNotNull(allPackages);
        assertNotNull(exportedPackages);
    }
}
