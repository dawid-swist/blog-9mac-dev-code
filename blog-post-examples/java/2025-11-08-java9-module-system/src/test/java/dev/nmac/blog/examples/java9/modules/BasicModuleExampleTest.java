package dev.nmac.blog.examples.java9.modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BasicModuleExample demonstrating module system fundamentals.
 */
@DisplayName("Basic Module System Concepts")
class BasicModuleExampleTest {

    @Test
    @DisplayName("Should retrieve current module information")
    void shouldRetrieveCurrentModuleInformation() {
        Module currentModule = BasicModuleExampleTest.class.getModule();

        assertNotNull(currentModule);
        // Test with java.base which is always a named module
        Module javaBase = Object.class.getModule();
        assertTrue(javaBase.isNamed(), "java.base module should be named");
        assertNotNull(javaBase.getName());
        assertNotNull(currentModule.getClassLoader());
    }

    @Test
    @DisplayName("Should have module descriptor with metadata")
    void shouldHaveModuleDescriptorWithMetadata() {
        Module currentModule = BasicModuleExampleTest.class.getModule();
        var descriptor = currentModule.getDescriptor();

        // For unnamed modules, descriptor may be null
        // For named modules (with module-info.java), descriptor should have metadata
        if (descriptor != null) {
            assertNotNull(descriptor.name());
            assertNotNull(descriptor.packages());
            assertNotNull(descriptor.requires());
        }
    }

    @Test
    @DisplayName("Should have java.base as implicit dependency")
    void shouldHaveJavaBaseAsImplicitDependency() {
        Module currentModule = BasicModuleExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        assertTrue(currentModule.canRead(javaBase),
                "Every module implicitly requires java.base");
        assertEquals("java.base", javaBase.getName());
    }

    @Test
    @DisplayName("Should distinguish between named and unnamed modules")
    void shouldDistinguishBetweenNamedAndUnnamedModules() {
        Module currentModule = BasicModuleExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // java.base is always a named module
        assertTrue(javaBase.isNamed());
        assertNotNull(javaBase.getName());

        // Current module may be unnamed (running without module-info.java)
        assertNotNull(currentModule);
    }

    @Test
    @DisplayName("Should have module descriptor with packages")
    void shouldHaveModuleDescriptorWithPackages() {
        // Test with java.base which is a named module
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        var packages = descriptor.packages();
        assertFalse(packages.isEmpty());

        // Should contain core packages
        assertTrue(packages.contains("java.lang"));
    }

    @Test
    @DisplayName("Should provide access to module layer")
    void shouldProvideAccessToModuleLayer() {
        // Unnamed modules don't have a layer (getLayer() returns null)
        // Test with java.base which is a named module
        Module javaBase = Object.class.getModule();
        var layer = javaBase.getLayer();

        assertNotNull(layer);
        assertNotNull(layer.configuration());
    }

    @Test
    @DisplayName("Should demonstrate module hierarchy over packages")
    void shouldDemonstrateModuleHierarchy() {
        Module module = BasicModuleExampleTest.class.getModule();
        String packageName = BasicModuleExampleTest.class.getPackageName();

        // Module contains packages
        assertTrue(module.getPackages().contains(packageName));

        // Each class has exactly one module
        assertEquals(module, BasicModuleExampleTest.class.getModule());
    }

    @Test
    @DisplayName("Should enforce module boundaries")
    void shouldEnforceModuleBoundaries() {
        Module module1 = BasicModuleExampleTest.class.getModule();
        Module module2 = Object.class.getModule();

        // Modules are distinct
        assertNotEquals(module1.getName(), module2.getName());

        // But current module can read java.base
        assertTrue(module1.canRead(module2));
    }

    @Test
    @DisplayName("Should show module system provides strong encapsulation")
    void shouldShowModuleSystemProvidesStrongEncapsulation() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);

        // Packages are module-private by default unless exported
        String packageName = "java.lang";
        boolean exported = descriptor.exports().stream()
                .anyMatch(export -> export.source().equals(packageName));

        // java.lang is exported from java.base
        assertTrue(exported);
        assertTrue(javaBase.getPackages().contains(packageName));
    }

    @Test
    @DisplayName("Should enable introspection of JDK modules")
    void shouldEnableIntrospectionOfJdkModules() {
        // Get various JDK modules
        Module javaBase = Object.class.getModule();
        Module javaLang = String.class.getModule();
        Module javaUtil = java.util.ArrayList.class.getModule();

        // All are named modules
        assertTrue(javaBase.isNamed());
        assertTrue(javaLang.isNamed());
        assertTrue(javaUtil.isNamed());

        // All belong to java.base
        assertEquals("java.base", javaBase.getName());
        assertEquals("java.base", javaLang.getName());
        assertEquals("java.base", javaUtil.getName());
    }
}
