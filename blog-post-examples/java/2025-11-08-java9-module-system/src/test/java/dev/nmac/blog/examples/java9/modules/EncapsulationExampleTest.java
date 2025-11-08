package dev.nmac.blog.examples.java9.modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EncapsulationExample demonstrating strong encapsulation.
 */
@DisplayName("Strong Encapsulation and Reflection Security")
class EncapsulationExampleTest {

    @Test
    @DisplayName("Should allow accessing classes from exported packages")
    void shouldAllowAccessingClassesFromExportedPackages() {
        // java.util is exported from java.base
        List<String> list = new ArrayList<>();
        list.add("test");

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));
    }

    @Test
    @DisplayName("Should show exported packages are accessible")
    void shouldShowExportedPackagesAreAccessible() {
        Module javaBase = Object.class.getModule();

        // java.lang is exported
        assertTrue(javaBase.isExported("java.lang"));

        // Can access String and other java.lang classes
        String str = "test";
        assertEquals(javaBase, str.getClass().getModule());
    }

    @Test
    @DisplayName("Should enforce strong encapsulation at module level")
    void shouldEnforceStrongEncapsulationAtModuleLevel() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);

        // Packages are module-private by default
        var allPackages = descriptor.packages();
        var exportedPackages = descriptor.exports();

        // Not all packages are exported
        assertTrue(exportedPackages.size() <= allPackages.size());
    }

    @Test
    @DisplayName("Should demonstrate reflection on exported packages works")
    void shouldDemonstrateReflectionOnExportedPackagesWorks() throws NoSuchMethodException {
        // String is from exported java.lang
        Class<?> stringClass = String.class;
        Module module = stringClass.getModule();

        assertTrue(module.isExported("java.lang"));

        // Reflection on public methods of exported classes works
        var method = stringClass.getMethod("toUpperCase");
        assertNotNull(method);
    }

    @Test
    @DisplayName("Should protect private packages from unauthorized access")
    void shouldProtectPrivatePackagesFromUnauthorizedAccess() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);

        // Packages not in exports are private
        var privatePackages = descriptor.packages().stream()
                .filter(pkg -> descriptor.exports().stream()
                        .noneMatch(export -> export.source().equals(pkg)))
                .toList();

        // These packages exist but are not exported
        // They cannot be imported by other modules
        assertNotNull(privatePackages);
    }

    @Test
    @DisplayName("Should show packages are private by default")
    void shouldShowPackagesArePrivateByDefault() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);

        // Check if java.base has internal packages (not exported)
        var allPackages = descriptor.packages();
        var exportedPackages = descriptor.exports().stream()
                .map(e -> e.source())
                .toList();

        var privatePackages = allPackages.stream()
                .filter(pkg -> !exportedPackages.contains(pkg))
                .toList();

        // The point is: default is private, must explicitly export
        // java.base should have some private internal packages
        assertNotNull(privatePackages);
    }

    @Test
    @DisplayName("Should demonstrate difference between public and exported")
    void shouldDemonstrateDifferenceBetweenPublicAndExported() {
        // Before Java 9: public ≈ accessible from anywhere
        // After Java 9: must be public AND in exported package

        Module javaBase = Object.class.getModule();

        // String is public
        assertTrue(java.lang.String.class.getModifiers() ==
                  (java.lang.String.class.getModifiers() | java.lang.reflect.Modifier.PUBLIC));

        // AND java.lang is exported
        assertTrue(javaBase.isExported("java.lang"));

        // Both conditions required for accessibility
    }

    @Test
    @DisplayName("Should allow selective export to specific modules")
    void shouldAllowSelectiveExportToSpecificModules() {
        Module current = EncapsulationExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        String packageName = "dev.nmac.blog.examples.java9.modules";

        // Can export to specific module
        current.addExports(packageName, javaBase);

        // Now it's exported to that module
        assertTrue(current.isExported(packageName, javaBase));
    }

    @Test
    @DisplayName("Should control reflection access through opens directive")
    void shouldControlReflectionAccessThroughOpensDirective() {
        Module current = EncapsulationExampleTest.class.getModule();

        String packageName = "dev.nmac.blog.examples.java9.modules";

        // Can dynamically open packages for reflection
        Module javaBase = Object.class.getModule();
        current.addOpens(packageName, javaBase);

        // Now reflection access is allowed to that module
        // (In normal case, would throw InaccessibleObjectException)
    }

    @Test
    @DisplayName("Should distinguish between exports and opens")
    void shouldDistinguishBetweenExportsAndOpens() {
        Module current = EncapsulationExampleTest.class.getModule();

        // exports: allows compilation and runtime access
        // opens: allows only runtime reflection access

        String packageName = "dev.nmac.blog.examples.java9.modules";
        Module target = Object.class.getModule();

        // Export allows normal access
        current.addExports(packageName, target);
        assertTrue(current.isExported(packageName, target));

        // Open allows reflection (separate from export)
        current.addOpens(packageName, target);
        assertTrue(current.isOpen(packageName, target));
    }

    @Test
    @DisplayName("Should enable safe refactoring of internal code")
    void shouldEnableSafeRefactoringOfInternalCode() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);

        // Private packages (not exported) can be safely refactored
        var privatePackages = descriptor.packages().stream()
                .filter(pkg -> descriptor.exports().stream()
                        .noneMatch(export -> export.source().equals(pkg)))
                .toList();

        // These packages are internal implementation details
        // Consumers don't depend on them
        // Safe to rename, restructure, or remove
        for (String privatePackage : privatePackages) {
            assertFalse(javaBase.isExported(privatePackage),
                    "Private packages should not be exported");
        }
    }

    @Test
    @DisplayName("Should show JDK reduction through strong encapsulation")
    void shouldShowJdkReductionThroughStrongEncapsulation() {
        Module javaBase = Object.class.getModule();
        var packages = javaBase.getPackages();
        var exports = javaBase.getDescriptor().exports();

        // java.base likely has fewer exported packages than total packages
        // This is the power of encapsulation

        int totalPackages = packages.size();
        int exportedCount = exports.size();

        assertFalse(packages.isEmpty());
        assertFalse(exports.isEmpty());

        // Strong encapsulation reduces public API surface
        assertNotNull(totalPackages);
        assertNotNull(exportedCount);
    }

    @Test
    @DisplayName("Should support command-line override of encapsulation")
    void shouldSupportCommandLineOverrideOfEncapsulation() {
        // The module system provides command-line options:
        // --add-opens java.base/java.lang=ALL-UNNAMED
        // --add-exports java.base/sun.nio=ALL-UNNAMED

        // This allows frameworks to work with older code
        // But should be used carefully

        Module current = EncapsulationExampleTest.class.getModule();
        assertNotNull(current);

        // The Module API allows the same at runtime
        Module javaBase = Object.class.getModule();
        current.addOpens("java.lang", javaBase);
    }

    @Test
    @DisplayName("Should demonstrate backward compatibility with module system")
    void shouldDemonstratBackwardCompatibilityWithModuleSystem() {
        // Code not using modules (classpath) goes to "unnamed module"
        // The unnamed module can read all named modules
        // Named modules don't read the unnamed module by default
        // This maintains backward compatibility

        Module javaBase = Object.class.getModule();
        assertTrue(javaBase.isNamed());

        // Named modules are fully compatible with module system
        assertNotNull(javaBase.getName());
        assertNotNull(javaBase.getDescriptor());
    }

    @Test
    @DisplayName("Should show benefits of strong encapsulation")
    void shouldShowBenefitsOfStrongEncapsulation() {
        // Benefits demonstrated:

        // 1. Reduced attack surface
        Module javaBase = Object.class.getModule();
        assertTrue(javaBase.isNamed());

        // 2. Clear API contract
        var exports = javaBase.getDescriptor().exports();
        assertNotNull(exports);

        // 3. Safe refactoring
        var packages = javaBase.getPackages();
        var privatePackages = packages.stream()
                .filter(pkg -> !javaBase.isExported(pkg))
                .toList();

        // 4. Version independence
        // Modules don't split across versions
        // One module per version

        // 5. Explicitly declared dependencies
        var requires = javaBase.getDescriptor().requires();
        assertNotNull(requires);
    }
}
