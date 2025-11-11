package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BasicSealedExample demonstrating sealed class behavior.
 */
class BasicSealedExampleTest {

    @Test
    @DisplayName("Should create Circle with x and y coordinates")
    void shouldCalculateCircleAreaCorrectly() {
        var circle = new Circle(5.0);

        assertEquals(Math.PI * 25, circle.area(), 0.001);
        assertEquals(5.0, circle.radius());
    }

    @Test
    @DisplayName("Should calculate rectangle area correctly")
    void shouldCalculateRectangleAreaCorrectly() {
        var rectangle = new Rectangle(4.0, 6.0);

        assertEquals(24.0, rectangle.area(), 0.001);
        assertEquals(4.0, rectangle.width());
        assertEquals(6.0, rectangle.height());
    }

    @Test
    @DisplayName("Should calculate triangle area correctly")
    void shouldCalculateTriangleAreaCorrectly() {
        var triangle = new Triangle(3.0, 8.0);

        assertEquals(12.0, triangle.area(), 0.001);
        assertEquals(3.0, triangle.base());
        assertEquals(8.0, triangle.height());
    }

    @Test
    @DisplayName("Should verify Shape is sealed with correct permitted subclasses")
    void shouldVerifyShapeIsSealedWithCorrectPermittedSubclasses() {
        assertTrue(Shape.class.isSealed());

        var permitted = Shape.class.getPermittedSubclasses();
        assertEquals(3, permitted.length);

        var permittedNames = Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(Collectors.toSet());

        assertTrue(permittedNames.contains("Circle"));
        assertTrue(permittedNames.contains("Rectangle"));
        assertTrue(permittedNames.contains("Triangle"));
    }

    @Test
    @DisplayName("Should throw exception for invalid dimensions")
    void shouldThrowExceptionForInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Circle(0));
        assertThrows(IllegalArgumentException.class, () -> new Circle(-5));
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Triangle(5, -3));
    }

    @Test
    @DisplayName("Should generate correct description for Circle")
    void shouldGenerateCorrectDescriptionForCircle() {
        var circle = new Circle(5.0);
        var description = circle.describe();

        assertTrue(description.contains("Circle"));
        assertTrue(description.matches(".*5[.,]0+.*")); // Supports both . and , decimal separators
        assertTrue(description.matches(".*78[.,]5[0-9].*")); // Area approximately 78.54
    }

    @Test
    @DisplayName("Should generate correct description for Rectangle")
    void shouldGenerateCorrectDescriptionForRectangle() {
        var rectangle = new Rectangle(4.0, 6.0);
        var description = rectangle.describe();

        assertTrue(description.contains("Rectangle"));
        assertTrue(description.matches(".*4[.,]0+.*")); // Supports both . and , decimal separators
        assertTrue(description.matches(".*6[.,]0+.*")); // Supports both . and , decimal separators
        assertTrue(description.matches(".*24[.,]0+.*")); // Supports both . and , decimal separators
    }

    @Test
    @DisplayName("Should generate correct description for Triangle")
    void shouldGenerateCorrectDescriptionForTriangle() {
        var triangle = new Triangle(3.0, 8.0);
        var description = triangle.describe();

        assertTrue(description.contains("Triangle"));
        assertTrue(description.matches(".*3[.,]0+.*")); // Supports both . and , decimal separators
        assertTrue(description.matches(".*8[.,]0+.*")); // Supports both . and , decimal separators
        assertTrue(description.matches(".*12[.,]0+.*")); // Supports both . and , decimal separators
    }
}
