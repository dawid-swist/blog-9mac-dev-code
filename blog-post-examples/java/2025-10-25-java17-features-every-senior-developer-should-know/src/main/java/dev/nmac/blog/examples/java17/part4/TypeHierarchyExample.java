package dev.nmac.blog.examples.java17.part4;

/**
 * Demonstrates pattern matching with polymorphic type hierarchies.
 *
 * Pattern matching shines when working with sealed interfaces and records
 * (features from Part 2 and Part 3). This example shows how pattern matching
 * eliminates verbose instanceof-cast chains while maintaining type safety.
 *
 * The sealed interface ensures the compiler knows all possible subtypes,
 * enabling exhaustiveness checking in future Java versions.
 */
public class TypeHierarchyExample {

    // Sealed interface ensures exhaustiveness (covered in Part 3)
    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
    }

    public record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() {
            return (base * height) / 2;
        }
    }

    /**
     * Traditional approach - verbose and repetitive.
     * Requires explicit casting after each instanceof check.
     */
    public static String describeTraditional(Shape shape) {
        if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            return String.format("Circle with radius %.2f", c.radius());
        } else if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            return String.format("Rectangle %.2f x %.2f", r.width(), r.height());
        } else if (shape instanceof Triangle) {
            Triangle t = (Triangle) shape;
            return String.format("Triangle with base %.2f and height %.2f", t.base(), t.height());
        } else {
            return "Unknown shape";
        }
    }

    /**
     * Modern approach with pattern matching.
     * Each instanceof check automatically casts to the pattern variable.
     */
    public static String describeModern(Shape shape) {
        if (shape instanceof Circle c) {
            return String.format("Circle with radius %.2f", c.radius());
        } else if (shape instanceof Rectangle r) {
            return String.format("Rectangle %.2f x %.2f", r.width(), r.height());
        } else if (shape instanceof Triangle t) {
            return String.format("Triangle with base %.2f and height %.2f", t.base(), t.height());
        } else {
            return "Unknown shape";
        }
    }

    /**
     * Pattern matching with guards (conditional logic).
     * Combines type checks with additional conditions using &&.
     */
    public static String classifyBySize(Shape shape) {
        double area = shape.area();

        if (shape instanceof Circle c && c.radius() > 10) {
            return "Large circle (radius > 10)";
        } else if (shape instanceof Rectangle r && (r.width() > 20 || r.height() > 20)) {
            return "Large rectangle (dimension > 20)";
        } else if (shape instanceof Triangle t && t.base() > 15) {
            return "Wide triangle (base > 15)";
        } else if (area > 100) {
            return "Large shape by area";
        } else {
            return "Small shape";
        }
    }

    /**
     * Computing perimeter requires type-specific logic.
     * Demonstrates pattern matching for operations not defined on the interface.
     */
    public static double perimeter(Shape shape) {
        if (shape instanceof Circle c) {
            return 2 * Math.PI * c.radius();
        } else if (shape instanceof Rectangle r) {
            return 2 * (r.width() + r.height());
        } else if (shape instanceof Triangle t) {
            // Simplified: assumes right triangle or provides approximation
            // In real code, would need all three sides
            return t.base() + t.height() + Math.sqrt(t.base() * t.base() + t.height() * t.height());
        }
        throw new IllegalArgumentException("Unknown shape type");
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(10.0, 20.0),
            new Triangle(6.0, 8.0),
            new Circle(15.0)
        };

        System.out.println("=== Traditional vs Modern ===");
        for (Shape shape : shapes) {
            System.out.println("Traditional: " + describeTraditional(shape));
            System.out.println("Modern:      " + describeModern(shape));
            System.out.println();
        }

        System.out.println("=== Size Classification ===");
        for (Shape shape : shapes) {
            System.out.println(describeModern(shape) + " -> " + classifyBySize(shape));
        }

        System.out.println("\n=== Perimeter Calculation ===");
        for (Shape shape : shapes) {
            System.out.printf("%s: area=%.2f, perimeter=%.2f%n",
                describeModern(shape), shape.area(), perimeter(shape));
        }
    }
}
