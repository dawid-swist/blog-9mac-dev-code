package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;
import dev.nmac.blog.examples.springframework6.api.Water;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoffeeSpiApp - Java SPI Pattern for Dependency Discovery")
class CoffeeSpiAppTest {

    @Test
    @DisplayName("Should discover Water implementation via ServiceLoader")
    void shouldDiscoverWaterImplementationViaServiceLoader() {
        // When: Using ServiceLoader to discover Water implementations
        Water water = ServiceLoader.load(Water.class)
                .findFirst()
                .orElse(null);

        // Then: Water implementation is discovered
        assertNotNull(water);
        assertNotNull(water.getType());
    }

    @Test
    @DisplayName("Should discover all CoffeeBean implementations via ServiceLoader")
    void shouldDiscoverAllCoffeeBeanImplementationsViaServiceLoader() {
        // When: Using ServiceLoader to discover all CoffeeBean implementations
        var beans = ServiceLoader.load(CoffeeBean.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());

        // Then: All coffee bean implementations are discovered
        assertFalse(beans.isEmpty());
        assertTrue(beans.size() >= 3, "Should find at least 3 coffee bean implementations");

        // Verify variety names are present
        var varieties = beans.stream()
                .map(CoffeeBean::getVariety)
                .collect(Collectors.toSet());
        assertTrue(varieties.contains("Arabica"));
        assertTrue(varieties.contains("Robusta"));
        assertTrue(varieties.contains("Liberica"));
    }

    @Test
    @DisplayName("Should enable loose coupling through service discovery mechanism")
    void shouldEnableLooseCouplingThroughServiceDiscovery() {
        // SPI Pattern benefit: CoffeeSpiApp doesn't know about concrete implementations
        // It just loads them via ServiceLoader

        // When: Load services
        Water water = ServiceLoader.load(Water.class).findFirst().orElse(null);
        var beans = ServiceLoader.load(CoffeeBean.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());

        // Then: Services are properly loaded without explicit references
        assertNotNull(water);
        assertFalse(beans.isEmpty());
    }

    @Test
    @DisplayName("Should support runtime discovery of implementations")
    void shouldSupportRuntimeDiscoveryOfImplementations() {
        // SPI discovers implementations at runtime based on META-INF/services files
        // This allows adding new implementations without changing the loader code

        // When
        var waterProviders = ServiceLoader.load(Water.class).stream().toList();
        var beanProviders = ServiceLoader.load(CoffeeBean.class).stream().toList();

        // Then
        assertTrue(waterProviders.size() > 0);
        assertTrue(beanProviders.size() > 0);
    }

    @Test
    @DisplayName("Should allow each bean to have independent roast levels via SPI")
    void shouldAllowIndependentRoastLevels() {
        // When: Load CoffeeBean implementations
        var beans = ServiceLoader.load(CoffeeBean.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());

        // Then: Each implementation can define its own roast level independently
        beans.forEach(bean -> assertNotNull(bean.getRoastLevel()));

        // Verify roast levels are different
        var roastLevels = beans.stream()
                .map(CoffeeBean::getRoastLevel)
                .collect(Collectors.toSet());
        assertTrue(roastLevels.size() > 1, "Beans should have different roast levels");
    }
}
