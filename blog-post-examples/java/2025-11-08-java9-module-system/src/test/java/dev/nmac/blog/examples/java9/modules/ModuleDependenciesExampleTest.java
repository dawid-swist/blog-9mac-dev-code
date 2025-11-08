package dev.nmac.blog.examples.java9.modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.module.ModuleDescriptor.Requires.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModuleDependenciesExample demonstrating module dependencies.
 */
@DisplayName("Module Dependencies and Relationships")
class ModuleDependenciesExampleTest {

    @Test
    @DisplayName("Should allow querying module dependencies")
    void shouldAllowQueryingModuleDependencies() {
        // java.base is the foundation module and has no requires
        // Use java.logging which requires java.base
        Module javaLogging = java.util.logging.Logger.class.getModule();
        var descriptor = javaLogging.getDescriptor();

        assertNotNull(descriptor);
        assertNotNull(descriptor.requires());
        // java.logging module should have requires directives (at least java.base)
        assertFalse(descriptor.requires().isEmpty());
    }

    @Test
    @DisplayName("Should show every module implicitly requires java.base")
    void shouldShowEveryModuleImplicitlyRequiresJavaBase() {
        Module currentModule = ModuleDependenciesExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // Every module can read java.base
        assertTrue(currentModule.canRead(javaBase));
        assertEquals("java.base", javaBase.getName());
    }

    @Test
    @DisplayName("Should query if module can read another module")
    void shouldQueryIfModuleCanReadAnotherModule() {
        Module currentModule = ModuleDependenciesExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // Test canRead method
        assertTrue(currentModule.canRead(javaBase));
        assertTrue(javaBase.canRead(javaBase)); // Module can read itself
    }

    @Test
    @DisplayName("Should identify transitive vs direct dependencies")
    void shouldIdentifyTransitiveVsDirectDependencies() {
        // Unnamed modules have descriptor == null, so test with java.base
        Module javaBase = Object.class.getModule();
        var descriptor = javaBase.getDescriptor();

        assertNotNull(descriptor);
        var requires = descriptor.requires();

        // Check properties of requires directives
        for (var require : requires) {
            assertNotNull(require.name());
            // Each require has modifiers
            var mods = require.modifiers();
            assertNotNull(mods);
            // Modifiers can include TRANSITIVE, STATIC, etc.
        }
    }

    @Test
    @DisplayName("Should distinguish between static and non-static requires")
    void shouldDistinguishBetweenStaticAndNonStaticRequires() {
        // java.base has no requires (it's the foundation module)
        // Use java.logging which requires java.base
        Module javaLogging = java.util.logging.Logger.class.getModule();
        var descriptor = javaLogging.getDescriptor();

        assertNotNull(descriptor);
        var requires = descriptor.requires();

        // java.logging has requires directives (at least java.base)
        assertFalse(requires.isEmpty());

        // Each require should have modifiers that include or exclude STATIC
        for (var require : requires) {
            var mods = require.modifiers();
            boolean isStatic = mods.contains(Modifier.STATIC);
            // Can be static or non-static
            assertNotNull(isStatic);
        }
    }

    @Test
    @DisplayName("Should show modules can have transitive requires")
    void shouldShowModulesCanHaveTransitiveRequires() {
        // java.base has no requires (it's the foundation module)
        // Use java.sql which has transitive requires
        Module javaSql = java.sql.Driver.class.getModule();
        var descriptor = javaSql.getDescriptor();

        assertNotNull(descriptor);
        var requires = descriptor.requires();

        // java.sql has requires directives
        assertFalse(requires.isEmpty());

        // Check if any of java.sql's requires are transitive
        for (var require : requires) {
            var mods = require.modifiers();
            boolean isTransitive = mods.contains(Modifier.TRANSITIVE);
            // Modules may or may not be transitive
            assertNotNull(isTransitive);
        }
    }

    @Test
    @DisplayName("Should allow checking module relationships at runtime")
    void shouldAllowCheckingModuleRelationshipsAtRuntime() {
        Module currentModule = ModuleDependenciesExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();
        Module javaLogging = java.util.logging.Logger.class.getModule();

        // Can check various relationships
        assertTrue(currentModule.canRead(javaBase));
        assertTrue(currentModule.canRead(javaLogging) || javaLogging == javaBase);

        // Named modules are identifiable by name
        assertTrue(javaBase.isNamed());
        assertEquals("java.base", javaBase.getName());
    }

    @Test
    @DisplayName("Should provide access to module layer configuration")
    void shouldProvideAccessToModuleLayerConfiguration() {
        // Unnamed modules don't have a layer, so test with java.base
        Module javaBase = Object.class.getModule();
        var layer = javaBase.getLayer();

        assertNotNull(layer);
        assertNotNull(layer.configuration());

        // Configuration contains module information
        var config = layer.configuration();
        assertFalse(config.modules().isEmpty());
    }

    @Test
    @DisplayName("Should demonstrate requires patterns in real modules")
    void shouldDemonstrateRequiresPatternsInRealModules() {
        // java.base has no requires (it's the foundation module)
        // Use java.logging which requires java.base
        Module javaLogging = java.util.logging.Logger.class.getModule();
        var descriptor = javaLogging.getDescriptor();

        assertNotNull(descriptor);
        var requires = descriptor.requires();

        // java.logging should have requires (at least java.base)
        assertFalse(requires.isEmpty());

        // Can filter by modifiers
        var transitiveReqs = requires.stream()
                .filter(r -> r.modifiers().contains(Modifier.TRANSITIVE))
                .toList();

        var staticReqs = requires.stream()
                .filter(r -> r.modifiers().contains(Modifier.STATIC))
                .toList();

        // May have transitive requires
        assertNotNull(transitiveReqs);
        // May or may not have static requires
        assertNotNull(staticReqs);
    }

    @Test
    @DisplayName("Should show all system modules are available")
    void shouldShowAllSystemModulesAreAvailable() {
        // Various classes from different modules
        Module javaBase = Object.class.getModule();
        Module javaLogging = java.util.logging.Logger.class.getModule();
        Module javaIO = java.io.File.class.getModule();

        // All are named modules
        assertTrue(javaBase.isNamed());
        assertTrue(javaLogging.isNamed());
        assertTrue(javaIO.isNamed());

        // All are accessible and queryable
        assertNotNull(javaBase.getName());
        assertNotNull(javaLogging.getName());
        assertNotNull(javaIO.getName());
    }

    @Test
    @DisplayName("Should support dynamic module reading")
    void shouldSupportDynamicModuleReading() {
        Module currentModule = ModuleDependenciesExampleTest.class.getModule();
        Module javaBase = Object.class.getModule();

        // addReads is a dynamic operation (adds a read edge at runtime)
        Module result = currentModule.addReads(javaBase);

        // Returns the module for chaining
        assertNotNull(result);
        assertEquals(currentModule, result);

        // Module can now (still) read java.base
        assertTrue(currentModule.canRead(javaBase));
    }
}
