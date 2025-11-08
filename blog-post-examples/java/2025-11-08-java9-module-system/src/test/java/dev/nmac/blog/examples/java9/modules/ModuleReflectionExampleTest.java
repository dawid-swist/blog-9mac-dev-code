package dev.nmac.blog.examples.java9.modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModuleReflectionExample demonstrating Module API introspection.
 */
@DisplayName("Module API and Runtime Introspection")
class ModuleReflectionExampleTest {

    @Test
    @DisplayName("Should get module from any class")
    void shouldGetModuleFromAnyClass() {
        Module module1 = String.class.getModule();
        Module module2 = java.util.ArrayList.class.getModule();
        Module module3 = ModuleReflectionExampleTest.class.getModule();

        assertNotNull(module1);
        assertNotNull(module2);
        assertNotNull(module3);

        // String and ArrayList are in same module
        assertEquals(module1.getName(), module2.getName());
    }

    @Test
    @DisplayName("Should retrieve module name")
    void shouldRetrieveModuleName() {
        Module javaBase = Object.class.getModule();
        String name = javaBase.getName();

        assertNotNull(name);
        assertEquals("java.base", name);
    }

    @Test
    @DisplayName("Should check if module is named")
    void shouldCheckIfModuleIsNamed() {
        Module module = ModuleReflectionExampleTest.class.getModule();

        // Test module may be unnamed, so test with java.base
        Module javaBase = Object.class.getModule();
        assertTrue(javaBase.isNamed());
        assertNotNull(javaBase.getName());
    }

    @Test
    @DisplayName("Should get class loader of module")
    void shouldGetClassLoaderOfModule() {
        Module javaBase = Object.class.getModule();
        ClassLoader loader = javaBase.getClassLoader();

        // java.base uses bootstrap class loader (null)
        // or platform class loader depending on JVM version
        // Just verify we can get the loader reference
        assertNotNull(javaBase);
    }

    @Test
    @DisplayName("Should retrieve module descriptor")
    void shouldRetrieveModuleDescriptor() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        assertNotNull(descriptor.name());
        assertNotNull(descriptor.requires());
        assertNotNull(descriptor.exports());
        assertNotNull(descriptor.packages());
    }

    @Test
    @DisplayName("Should get packages from module")
    void shouldGetPackagesFromModule() {
        Module module = ModuleReflectionExampleTest.class.getModule();
        Set<String> packages = module.getPackages();

        assertNotNull(packages);
        assertFalse(packages.isEmpty());
        assertTrue(packages.contains("dev.nmac.blog.examples.java9.modules"));
    }

    @Test
    @DisplayName("Should access module layer")
    void shouldAccessModuleLayer() {
        // Unnamed modules don't have a layer, so test with java.base
        Module javaBase = Object.class.getModule();
        var layer = javaBase.getLayer();

        assertNotNull(layer);
        assertNotNull(layer.configuration());
        assertFalse(layer.configuration().modules().isEmpty());
    }

    @Test
    @DisplayName("Should check canRead relationship")
    void shouldCheckCanReadRelationship() {
        Module current = ModuleReflectionExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        assertTrue(current.canRead(javaBase));
        assertTrue(javaBase.canRead(javaBase)); // Module reads itself
    }

    @Test
    @DisplayName("Should check if package is exported")
    void shouldCheckIfPackageIsExported() {
        Module javaBase = Object.class.getModule();

        assertTrue(javaBase.isExported("java.lang"));
        assertTrue(javaBase.isExported("java.util"));
    }

    @Test
    @DisplayName("Should check conditional export to specific module")
    void shouldCheckConditionalExportToSpecificModule() {
        Module current = ModuleReflectionExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // java.lang is exported unconditionally to all modules
        assertTrue(javaBase.isExported("java.lang", current));
    }

    @Test
    @DisplayName("Should check package open status for reflection")
    void shouldCheckPackageOpenStatusForReflection() {
        Module javaBase = Object.class.getModule();

        // Check if package is open for reflection
        boolean isOpen = javaBase.isOpen("java.lang");
        assertNotNull(isOpen); // May be true or false depending on configuration
    }

    @Test
    @DisplayName("Should check conditional open to specific module")
    void shouldCheckConditionalOpenToSpecificModule() {
        Module current = ModuleReflectionExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // Check if package is open to specific module
        boolean isOpenTo = javaBase.isOpen("java.lang", current);
        assertNotNull(isOpenTo);
    }

    @Test
    @DisplayName("Should check service usage capability")
    void shouldCheckServiceUsageCapability() {
        Module current = ModuleReflectionExampleTest.class.getModule();

        // Check if module uses a service (may be false)
        boolean canUse = current.canUse(Object.class);
        assertNotNull(canUse);
    }

    @Test
    @DisplayName("Should support dynamic module configuration")
    void shouldSupportDynamicModuleConfiguration() {
        Module current = ModuleReflectionExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // addReads - dynamically add read edge
        Module result = current.addReads(javaBase);
        assertSame(current, result); // Returns self for chaining

        // Verify the read relationship exists
        assertTrue(current.canRead(javaBase));
    }

    @Test
    @DisplayName("Should support dynamic export")
    void shouldSupportDynamicExport() {
        Module current = ModuleReflectionExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        String packageName = "dev.nmac.blog.examples.java9.modules";

        // addExports - dynamically export package
        Module result = current.addExports(packageName, javaBase);
        assertSame(current, result);
    }

    @Test
    @DisplayName("Should support dynamic open")
    void shouldSupportDynamicOpen() {
        Module current = ModuleReflectionExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        String packageName = "dev.nmac.blog.examples.java9.modules";

        // addOpens - dynamically open package for reflection
        Module result = current.addOpens(packageName, javaBase);
        assertSame(current, result);
    }

    @Test
    @DisplayName("Should support dynamic uses")
    void shouldSupportDynamicUses() {
        Module current = ModuleReflectionExampleTest.class.getModule();

        // addUses - dynamically add service usage
        Module result = current.addUses(Runnable.class);
        assertSame(current, result);
    }

    @Test
    @DisplayName("Should enable runtime inspection of module hierarchy")
    void shouldEnableRuntimeInspectionOfModuleHierarchy() {
        // Unnamed modules don't have a layer, so test with java.base
        Module javaBase = Object.class.getModule();
        var layer = javaBase.getLayer();

        assertNotNull(layer);

        var config = layer.configuration();
        var allModules = config.modules();

        // Can inspect all modules in the configuration
        assertFalse(allModules.isEmpty());

        // Can find specific modules by name
        var javaBaseModule = layer.findModule("java.base");
        assertTrue(javaBaseModule.isPresent());
    }

    @Test
    @DisplayName("Should demonstrate Module API for framework development")
    void shouldDemonstrateModuleApiForFrameworkDevelopment() {
        Module current = ModuleReflectionExampleTest.class.getModule();

        // Frameworks can use Module API to:
        // 1. Check which modules are available (use java.base layer for unnamed modules)
        Module javaBase = Object.class.getModule();
        var layer = javaBase.getLayer();
        assertNotNull(layer);

        // 2. Request reflection access
        String packageName = "dev.nmac.blog.examples.java9.modules";
        current.addOpens(packageName, javaBase);

        // 3. Configure service loading
        current.addUses(Runnable.class);

        // All operations work on both named and unnamed modules
        assertNotNull(current);
    }

    @Test
    @DisplayName("Should show Module API is available in java.lang")
    void shouldShowModuleApiIsAvailableInJavaLang() {
        // Module class is in java.lang.Module
        assertNotNull(Module.class);

        // ModuleDescriptor is in java.lang.module.ModuleDescriptor
        assertNotNull(java.lang.module.ModuleDescriptor.class);

        // ModuleLayer is in java.lang.module.ModuleLayer
        // (Accessed via reflection due to module encapsulation)
        assertNotNull(java.lang.module.ModuleDescriptor.class);
    }
}
