package dev.nmac.blog.examples.java17.part3;

import java.util.List;

/**
 * Example 1: Basic Sealed Classes with Shape Hierarchy
 *
 * Demonstrates:
 * - Sealed interface with permits clause
 * - Record implementations as permitted subtypes
 * - Compact constructor validation
 * - Reflection API for sealed types
 * - Records provide implicit immutability and built-in accessors
 *
 * Key concept: Sealed interfaces combined with records create elegant,
 * immutable domain models where the compiler knows all possible subtypes,
 * enabling exhaustive pattern matching and preventing unauthorized extensions.
 */

// ============================================================================
// Sealed interface - explicitly lists all permitted subtypes
// ============================================================================

/**
 * Sealed Shape interface that permits only Circle, Rectangle, and Triangle.
 * This creates a closed hierarchy where no other implementations can be added.
 */
sealed interface Shape
    permits Circle, Rectangle, Triangle {

    /**
     * Calculates the area of the shape.
     *
     * @return the area value
     */
    double area();

    /**
     * Provides a human-readable description of the shape.
     *
     * @return formatted description string
     */
    String describe();
}

// ============================================================================
// Record implementations - permitted sealed types
// ============================================================================

/**
 * Circle record implementation with radius validation.
 * Records are implicitly final, making them perfect sealed type implementations.
 */
record Circle(double radius) implements Shape {

    /**
     * Compact constructor validates that radius is positive.
     * Records automatically assign the validated value to the field.
     *
     * @throws IllegalArgumentException if radius is not positive
     */
    public Circle {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
    }

    /**
     * Calculates the area of the circle using π × r².
     *
     * @return the area of the circle
     */
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    /**
     * Provides a formatted description of the circle.
     *
     * @return string containing radius and calculated area
     */
    @Override
    public String describe() {
        return String.format("Circle[radius=%.2f, area=%.2f]", radius, area());
    }
}

/**
 * Rectangle record implementation with dimension validation.
 * Records are implicitly final, making them perfect sealed type implementations.
 */
record Rectangle(double width, double height) implements Shape {

    /**
     * Compact constructor validates that both dimensions are positive.
     * Records automatically assign the validated values to the fields.
     *
     * @throws IllegalArgumentException if either dimension is not positive
     */
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }

    /**
     * Calculates the area of the rectangle using width × height.
     *
     * @return the area of the rectangle
     */
    @Override
    public double area() {
        return width * height;
    }

    /**
     * Provides a formatted description of the rectangle.
     *
     * @return string containing width, height, and calculated area
     */
    @Override
    public String describe() {
        return String.format("Rectangle[width=%.2f, height=%.2f, area=%.2f]",
            width, height, area());
    }
}

/**
 * Triangle record implementation with dimension validation.
 * Records are implicitly final, making them perfect sealed type implementations.
 */
record Triangle(double base, double height) implements Shape {

    /**
     * Compact constructor validates that both dimensions are positive.
     * Records automatically assign the validated values to the fields.
     *
     * @throws IllegalArgumentException if either dimension is not positive
     */
    public Triangle {
        if (base <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }

    /**
     * Calculates the area of the triangle using ½ × base × height.
     *
     * @return the area of the triangle
     */
    @Override
    public double area() {
        return 0.5 * base * height;
    }

    /**
     * Provides a formatted description of the triangle.
     *
     * @return string containing base, height, and calculated area
     */
    @Override
    public String describe() {
        return String.format("Triangle[base=%.2f, height=%.2f, area=%.2f]",
            base, height, area());
    }
}

// ============================================================================
// Main example class
// ============================================================================

/**
 * Demonstrates basic sealed classes with a Shape hierarchy.
 *
 * Shows:
 * - Creating instances of permitted subtypes
 * - Polymorphic behavior through sealed parent type
 * - Type-specific accessor methods
 * - Reflection API for discovering sealed hierarchies
 */
public class BasicSealedExample {

    /**
     * Main method demonstrating sealed class usage.
     */
    public static void main(String[] args) {
        // Create instances of all permitted Shape subtypes
        var shapes = List.of(
            new Circle(5.0),
            new Rectangle(4.0, 6.0),
            new Triangle(3.0, 8.0)
        );

        // Demonstrate polymorphic behavior
        System.out.println("=== Shape Demonstrations ===");
        for (var shape : shapes) {
            System.out.println(shape.describe());
        }

        // Type-specific access - records provide automatic accessors
        System.out.println("\n=== Type-Specific Access (Record Accessors) ===");
        var circle = new Circle(10.0);
        System.out.println("Circle radius: " + circle.radius());

        var rect = new Rectangle(5.0, 3.0);
        System.out.println("Rectangle dimensions: " + rect.width() + " x " + rect.height());

        // Reflection API - discovering sealed hierarchy at runtime
        System.out.println("\n=== Reflection API ===");
        System.out.println("Shape is sealed: " + Shape.class.isSealed());
        System.out.print("Permitted subclasses: ");
        for (var permitted : Shape.class.getPermittedSubclasses()) {
            System.out.print(permitted.getSimpleName() + " ");
        }
        System.out.println();
    }
}
