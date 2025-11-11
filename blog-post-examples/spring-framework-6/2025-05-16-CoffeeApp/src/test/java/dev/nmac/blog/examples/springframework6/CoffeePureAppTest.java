package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;
import dev.nmac.blog.examples.springframework6.impl.ArabicaBeans;
import dev.nmac.blog.examples.springframework6.impl.FilteredWater;
import dev.nmac.blog.examples.springframework6.impl.LibericaBeans;
import dev.nmac.blog.examples.springframework6.impl.RobustaBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoffeePureApp - Manual Dependency Injection Pattern")
class CoffeePureAppTest {

    private CoffeeMaker coffeeMaker;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Replicate what CoffeePureApp does: Manual DI
        var water = new FilteredWater();
        var beans = List.of(
                new ArabicaBeans(),
                new RobustaBeans(),
                new LibericaBeans()
        );
        coffeeMaker = new CoffeeMaker(water, beans);

        // Capture System.out for testing output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    void restoreOut() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should demonstrate pure Java manual dependency injection")
    void shouldDemonstratePureJavaManualDependencyInjection() {
        // Given: CoffeeMaker is created with manually instantiated dependencies
        assertNotNull(coffeeMaker);

        // When: We retrieve water type
        String waterType = coffeeMaker.getWaterType();

        // Then: Water is properly injected
        assertEquals("Filtered water", waterType);
    }

    @Test
    @DisplayName("Should have all three coffee bean varieties available")
    void shouldHaveAllThreeCoffeeBeanVarietiesAvailable() {
        // When
        List<String> coffees = coffeeMaker.getAvailableCoffees();

        // Then
        assertEquals(3, coffees.size());
        assertTrue(coffees.contains("Arabica (Medium)"));
        assertTrue(coffees.contains("Robusta (Dark)"));
        assertTrue(coffees.contains("Liberica (Light)"));
    }

    @Test
    @DisplayName("Should brew each coffee type successfully in manual DI mode")
    void shouldBrewEachCoffeeTypeSuccessfully() {
        // When & Then
        for (int i = 0; i < 3; i++) {
            String result = coffeeMaker.brewCoffee(i);
            assertNotNull(result);
            assertTrue(result.contains("Filtered water"));
            assertTrue(result.contains("coffee"));
        }
    }

    @Test
    @DisplayName("Should highlight the verbosity of manual DI for complex dependency graphs")
    void shouldHighlightVerbosityOfManualDI() {
        // This test demonstrates that Pure Java DI requires:
        // 1. Direct instantiation of each dependency
        // 2. Manual passing of dependencies through constructors
        // 3. No central place to manage object lifecycle

        // When: Create dependencies manually
        var water = new FilteredWater();
        List<CoffeeBean> beans = List.of(new ArabicaBeans());
        var maker = new CoffeeMaker(water, beans);

        // Then: Dependencies are correctly wired
        assertNotNull(maker.getWaterType());
        assertEquals(1, maker.getAvailableCoffees().size());
    }

    @Test
    @DisplayName("Should allow direct control over object creation and lifecycle")
    void shouldAllowDirectControlOverObjectLifecycle() {
        // Pure Java DI gives you complete control
        // You decide when to create objects, how to configure them, when to cleanup

        // When: We create a different water implementation
        List<CoffeeBean> arabicaOnly = List.of(new ArabicaBeans());
        var pureMaker = new CoffeeMaker(new FilteredWater(), arabicaOnly);

        // Then: We have full control over the configuration
        assertEquals(1, pureMaker.getAvailableCoffees().size());
    }
}
