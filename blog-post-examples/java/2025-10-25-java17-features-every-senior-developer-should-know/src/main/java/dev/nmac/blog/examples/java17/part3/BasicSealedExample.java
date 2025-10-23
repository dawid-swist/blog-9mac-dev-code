package dev.nmac.blog.examples.java17.part3;

import java.util.List;

/**
 * Example 1: Basic Sealed Classes with Shape Hierarchy
 *
 * Demonstrates:
 * - Sealed abstract class with permits clause
 * - Final implementations as permitted subtypes
 * - Validation in constructors
 * - Reflection API for sealed types
 * - Type-specific accessor methods
 *
 * Key concept: Sealed classes provide controlled inheritance hierarchies where
 * the compiler knows all possible subtypes, enabling exhaustive pattern matching
 * and preventing unauthorized extensions.
 */

// ============================================================================
// Sealed parent class - explicitly lists all permitted subtypes
// ============================================================================

/**
 * Sealed abstract Shape class that permits only Circle, Rectangle, and Triangle.
 * This creates a closed hierarchy where no other classes can extend Shape.
 */
sealed abstract class Shape
    permits Circle, Rectangle, Triangle {

    /**
     * Abstract method to calculate the area of the shape.
     * Must be implemented by all permitted subclasses.
     */
    public abstract double area();

    /**
     * Abstract method to provide a human-readable description of the shape.
     * Must be implemented by all permitted subclasses.
     */
    public abstract String describe();
}

// ============================================================================
// Final implementations - leaf classes in the sealed hierarchy
// ============================================================================

/**
 * Circle implementation with radius validation.
 * Final modifier prevents any further subclassing.
 */
final class Circle extends Shape {
    private final double radius;

    /**
     * Creates a Circle with the specified radius.
     *
     * @param radius the radius of the circle (must be positive)
     * @throws IllegalArgumentException if radius is not positive
     */
    public Circle(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.radius = radius;
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

    /**
     * Accessor for the radius component.
     *
     * @return the radius of the circle
     */
    public double radius() {
        return radius;
    }
}

/**
 * Rectangle implementation with width and height validation.
 * Final modifier prevents any further subclassing.
 */
final class Rectangle extends Shape {
    private final double width;
    private final double height;

    /**
     * Creates a Rectangle with the specified dimensions.
     *
     * @param width  the width of the rectangle (must be positive)
     * @param height the height of the rectangle (must be positive)
     * @throws IllegalArgumentException if either dimension is not positive
     */
    public Rectangle(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.width = width;
        this.height = height;
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

    /**
     * Accessor for the width component.
     *
     * @return the width of the rectangle
     */
    public double width() {
        return width;
    }

    /**
     * Accessor for the height component.
     *
     * @return the height of the rectangle
     */
    public double height() {
        return height;
    }
}

/**
 * Triangle implementation with base and height validation.
 * Final modifier prevents any further subclassing.
 */
final class Triangle extends Shape {
    private final double base;
    private final double height;

    /**
     * Creates a Triangle with the specified dimensions.
     *
     * @param base   the base of the triangle (must be positive)
     * @param height the height of the triangle (must be positive)
     * @throws IllegalArgumentException if either dimension is not positive
     */
    public Triangle(double base, double height) {
        if (base <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.base = base;
        this.height = height;
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

    /**
     * Accessor for the base component.
     *
     * @return the base of the triangle
     */
    public double base() {
        return base;
    }

    /**
     * Accessor for the height component.
     *
     * @return the height of the triangle
     */
    public double height() {
        return height;
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

        // Type-specific operations using accessor methods
        System.out.println("\n=== Type-Specific Access ===");
        var circle = new Circle(10.0);
        System.out.println("Circle radius: " + circle.radius());

        var rect = new Rectangle(5.0, 3.0);
        System.out.println("Rectangle dimensions: " + rect.width() + " x " + rect.height());

        // Reflection API - discovering sealed hierarchy at runtime
        System.out.println("\n=== Reflection ===");
        System.out.println("Shape is sealed: " + Shape.class.isSealed());
        System.out.print("Permitted subclasses: ");
        for (var permitted : Shape.class.getPermittedSubclasses()) {
            System.out.print(permitted.getSimpleName() + " ");
        }
        System.out.println();
    }
}
