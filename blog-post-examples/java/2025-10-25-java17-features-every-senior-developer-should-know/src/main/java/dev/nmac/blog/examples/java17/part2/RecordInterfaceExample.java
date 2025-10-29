package dev.nmac.blog.examples.java17.part2;

import java.util.List;

/**
 * Record Interface Examples - Records Implementing Interfaces
 *
 * This example demonstrates:
 * - Records implementing interfaces
 * - Polymorphic behavior with records
 * - Multiple interface implementation
 * - Interface default methods with records
 * - Collections of records via interface types
 *
 * Records can implement interfaces for polymorphic behavior while
 * maintaining all benefits of concise syntax and generated methods.
 */
public class RecordInterfaceExample {

    /**
     * Drawable interface - represents objects that can be drawn.
     */
    public interface Drawable {
        void draw();
        double area();

        /**
         * Default method - available to all implementers.
         */
        default String description() {
            return "Drawable shape with area: " + area();
        }
    }

    /**
     * Measurable interface - represents objects with dimensions.
     */
    public interface Measurable {
        double perimeter();
    }

    /**
     * Circle record implementing Drawable.
     */
    public record Circle(int x, int y, int radius) implements Drawable {

        public Circle {
            if (radius <= 0) {
                throw new IllegalArgumentException("Radius must be positive");
            }
        }

        @Override
        public void draw() {
            System.out.println("Drawing circle at (" + x + ", " + y +
                             ") with radius " + radius);
        }

        @Override
        public double area() {
            return Math.PI * radius * radius;
        }

        public double diameter() {
            return 2.0 * radius;
        }
    }

    /**
     * Rectangle record implementing both Drawable and Measurable.
     */
    public record Rectangle(int x, int y, int width, int height)
            implements Drawable, Measurable {

        public Rectangle {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
        }

        @Override
        public void draw() {
            System.out.println("Drawing rectangle at (" + x + ", " + y + ") " +
                             width + "x" + height);
        }

        @Override
        public double area() {
            return width * height;
        }

        @Override
        public double perimeter() {
            return 2.0 * (width + height);
        }

        public boolean isSquare() {
            return width == height;
        }
    }

    /**
     * Triangle record implementing both interfaces.
     */
    public record Triangle(int x, int y, int base, int height)
            implements Drawable, Measurable {

        public Triangle {
            if (base <= 0 || height <= 0) {
                throw new IllegalArgumentException("Base and height must be positive");
            }
        }

        @Override
        public void draw() {
            System.out.println("Drawing triangle at (" + x + ", " + y + ") " +
                             "base=" + base + ", height=" + height);
        }

        @Override
        public double area() {
            return 0.5 * base * height;
        }

        @Override
        public double perimeter() {
            // Simplified - assumes isosceles triangle
            double side = Math.sqrt((base / 2.0) * (base / 2.0) + height * height);
            return base + 2 * side;
        }
    }

    /**
     * Comparable interface example - records can be compared.
     */
    public record Point(int x, int y) implements Comparable<Point> {

        @Override
        public int compareTo(Point other) {
            // Compare by distance from origin
            int thisDist = x * x + y * y;
            int otherDist = other.x * other.x + other.y * other.y;
            return Integer.compare(thisDist, otherDist);
        }

        public double distanceFromOrigin() {
            return Math.sqrt(x * x + y * y);
        }
    }

    /**
     * Serializable record example.
     */
    public record User(String username, int id) implements java.io.Serializable {

        public User {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username required");
            }
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
        }
    }

    // Main method for manual testing
    public static void main(String[] args) {
        System.out.println("=== Record Interface Examples ===\n");

        // Drawable shapes
        System.out.println("--- Drawable Shapes ---");
        Circle circle = new Circle(10, 20, 5);
        circle.draw();
        System.out.println("Area: " + circle.area());
        System.out.println("Description: " + circle.description());

        System.out.println();

        Rectangle rect = new Rectangle(0, 0, 100, 50);
        rect.draw();
        System.out.println("Area: " + rect.area());
        System.out.println("Perimeter: " + rect.perimeter());
        System.out.println("Is square: " + rect.isSquare());

        System.out.println();

        Triangle tri = new Triangle(0, 0, 30, 40);
        tri.draw();
        System.out.println("Area: " + tri.area());

        System.out.println("\n--- Polymorphism ---");
        List<Drawable> shapes = List.of(
            new Circle(0, 0, 10),
            new Rectangle(0, 0, 20, 30),
            new Triangle(0, 0, 15, 20)
        );

        for (Drawable shape : shapes) {
            System.out.println(shape.getClass().getSimpleName() + " - " + shape.description());
        }

        System.out.println("\n--- Comparable Points ---");
        List<Point> points = List.of(
            new Point(5, 0),
            new Point(1, 1),
            new Point(3, 4)
        );

        System.out.println("Unsorted:");
        points.forEach(p -> System.out.println("  " + p + " - distance: " +
                                               String.format("%.2f", p.distanceFromOrigin())));

        List<Point> sorted = new java.util.ArrayList<>(points);
        sorted.sort(Point::compareTo);

        System.out.println("\nSorted by distance:");
        sorted.forEach(p -> System.out.println("  " + p + " - distance: " +
                                               String.format("%.2f", p.distanceFromOrigin())));
    }
}
