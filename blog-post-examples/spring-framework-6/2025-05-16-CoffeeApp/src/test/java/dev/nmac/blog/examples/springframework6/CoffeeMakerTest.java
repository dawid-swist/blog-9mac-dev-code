package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;
import dev.nmac.blog.examples.springframework6.api.Water;
import dev.nmac.blog.examples.springframework6.impl.ArabicaBeans;
import dev.nmac.blog.examples.springframework6.impl.FilteredWater;
import dev.nmac.blog.examples.springframework6.impl.LibericaBeans;
import dev.nmac.blog.examples.springframework6.impl.RobustaBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoffeeMaker - Core Coffee Making Logic")
class CoffeeMakerTest {

    private CoffeeMaker coffeeMaker;
    private Water water;
    private List<CoffeeBean> beans;

    @BeforeEach
    void setUp() {
        water = new FilteredWater();
        beans = List.of(
                new ArabicaBeans(),
                new RobustaBeans(),
                new LibericaBeans()
        );
        coffeeMaker = new CoffeeMaker(water, beans);
    }

    @Test
    @DisplayName("Should return water type correctly")
    void shouldReturnWaterTypeCorrectly() {
        // When
        String waterType = coffeeMaker.getWaterType();

        // Then
        assertEquals("Filtered water", waterType);
    }

    @Test
    @DisplayName("Should return all available coffee varieties")
    void shouldReturnAllAvailableCoffeeVarieties() {
        // When
        List<String> availableCoffees = coffeeMaker.getAvailableCoffees();

        // Then
        assertEquals(3, availableCoffees.size());
        assertTrue(availableCoffees.contains("Arabica (Medium)"));
        assertTrue(availableCoffees.contains("Robusta (Dark)"));
        assertTrue(availableCoffees.contains("Liberica (Light)"));
    }

    @Test
    @DisplayName("Should brew coffee with selected bean variety")
    void shouldBrewCoffeeWithSelectedBeanVariety() {
        // When
        String result = coffeeMaker.brewCoffee(0); // Arabica

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Filtered water"));
        assertTrue(result.contains("Arabica"));
        assertTrue(result.contains("Medium"));
    }

    @Test
    @DisplayName("Should brew Robusta coffee when index 1 is selected")
    void shouldBrewRobustaCoffeeWhenIndex1Selected() {
        // When
        String result = coffeeMaker.brewCoffee(1);

        // Then
        assertTrue(result.contains("Robusta"));
        assertTrue(result.contains("Dark"));
    }

    @Test
    @DisplayName("Should brew Liberica coffee when index 2 is selected")
    void shouldBrewLibericaCoffeeWhenIndex2Selected() {
        // When
        String result = coffeeMaker.brewCoffee(2);

        // Then
        assertTrue(result.contains("Liberica"));
        assertTrue(result.contains("Light"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for negative index")
    void shouldThrowExceptionForNegativeIndex() {
        // Then
        assertThrows(IllegalArgumentException.class, () -> coffeeMaker.brewCoffee(-1));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for index out of bounds")
    void shouldThrowExceptionForIndexOutOfBounds() {
        // Then
        assertThrows(IllegalArgumentException.class, () -> coffeeMaker.brewCoffee(10));
    }

    @Test
    @DisplayName("Should format brew output with water and bean details")
    void shouldFormatBrewOutputWithWaterAndBeanDetails() {
        // When
        String result = coffeeMaker.brewCoffee(0);

        // Then
        assertTrue(result.startsWith("Making coffee with"));
        assertTrue(result.contains("and"));
        assertTrue(result.contains("coffee"));
        assertTrue(result.contains("roast"));
    }
}
