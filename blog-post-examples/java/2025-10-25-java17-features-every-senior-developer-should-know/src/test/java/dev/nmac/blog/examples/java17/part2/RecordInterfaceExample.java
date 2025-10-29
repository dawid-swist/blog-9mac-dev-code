package dev.nmac.blog.examples.java17.part2;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    // ============================================================
    // JUnit Tests
    // ============================================================

    @Test
    void testCircleDrawable() {
        Circle circle = new Circle(10, 20, 5);

        // Implements Drawable methods
        double area = circle.area();
        assertTrue(area > 78 && area < 79); // π * 5²

        // Can call draw
        circle.draw(); // Prints to console
    }

    @Test
    void testRectangleMultipleInterfaces() {
        Rectangle rect = new Rectangle(0, 0, 10, 20);

        // Drawable methods
        assertEquals(200.0, rect.area());

        // Measurable methods
        assertEquals(60.0, rect.perimeter());

        assertFalse(rect.isSquare());
    }

    @Test
    void testSquareDetection() {
        Rectangle square = new Rectangle(0, 0, 10, 10);
        assertTrue(square.isSquare());
    }

    @Test
    void testPolymorphism() {
        // Records can be used through interface types
        List<Drawable> shapes = List.of(
            new Circle(10, 20, 5),
            new Rectangle(30, 40, 100, 50),
            new Triangle(60, 70, 30, 40)
        );

        assertEquals(3, shapes.size());

        // Polymorphic behavior
        for (Drawable shape : shapes) {
            shape.draw(); // Calls correct implementation
            assertTrue(shape.area() > 0);
        }

        // Type checks still work
        assertTrue(shapes.get(0) instanceof Circle);
        assertTrue(shapes.get(1) instanceof Rectangle);
        assertTrue(shapes.get(2) instanceof Triangle);
    }

    @Test
    void testDefaultMethod() {
        Circle circle = new Circle(0, 0, 5);

        // Default method from interface
        String desc = circle.description();
        assertTrue(desc.contains("area"));
        assertTrue(desc.contains("78") || desc.contains("79"));
    }

    @Test
    void testAreaCalculations() {
        Circle c = new Circle(0, 0, 5);
        Rectangle r = new Rectangle(0, 0, 10, 20);
        Triangle t = new Triangle(0, 0, 10, 20);

        // Circle: π * 5²
        assertTrue(c.area() > 78 && c.area() < 79);

        // Rectangle: 10 * 20
        assertEquals(200.0, r.area());

        // Triangle: 0.5 * 10 * 20
        assertEquals(100.0, t.area());
    }

    @Test
    void testPerimeterCalculations() {
        Rectangle r = new Rectangle(0, 0, 10, 20);
        assertEquals(60.0, r.perimeter());

        Triangle t = new Triangle(0, 0, 6, 4);
        double perimeter = t.perimeter();
        assertTrue(perimeter > 14 && perimeter < 17); // 6 + 2*5 (approx)
    }

    @Test
    void testComparablePoint() {
        Point p1 = new Point(3, 4);  // distance = 5
        Point p2 = new Point(1, 1);  // distance ≈ 1.4
        Point p3 = new Point(3, 4);  // distance = 5

        // p2 is closer to origin
        assertTrue(p2.compareTo(p1) < 0);
        assertTrue(p1.compareTo(p2) > 0);

        // Equal distance
        assertEquals(0, p1.compareTo(p3));
    }

    @Test
    void testPointSorting() {
        List<Point> points = new java.util.ArrayList<>(List.of(
            new Point(5, 0),
            new Point(1, 1),
            new Point(3, 4),
            new Point(0, 1)
        ));

        // Sort by distance from origin
        points.sort(Point::compareTo);

        // Closest point should be first
        assertEquals(new Point(0, 1), points.get(0));
        assertEquals(new Point(1, 1), points.get(1));
    }

    @Test
    void testDistanceFromOrigin() {
        Point p1 = new Point(3, 4);
        assertEquals(5.0, p1.distanceFromOrigin(), 0.001);

        Point p2 = new Point(0, 0);
        assertEquals(0.0, p2.distanceFromOrigin(), 0.001);
    }

    @Test
    void testSerializableUser() throws Exception {
        User user = new User("alice", 1001);

        // Serialize
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos)) {
            oos.writeObject(user);
        }

        // Deserialize
        byte[] bytes = baos.toByteArray();
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(bytes);
        User deserialized;
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais)) {
            deserialized = (User) ois.readObject();
        }

        // Should be equal
        assertEquals(user, deserialized);
        assertEquals("alice", deserialized.username());
        assertEquals(1001, deserialized.id());
    }

    @Test
    void testValidation() {
        // Circle validation
        assertThrows(IllegalArgumentException.class, () -> {
            new Circle(0, 0, 0); // Zero radius
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Circle(0, 0, -5); // Negative radius
        });

        // Rectangle validation
        assertThrows(IllegalArgumentException.class, () -> {
            new Rectangle(0, 0, 0, 10); // Zero width
        });

        // User validation
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", 1); // Blank username
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new User("alice", 0); // Zero ID
        });
    }

    @Test
    void testRecordEquality() {
        Circle c1 = new Circle(10, 20, 5);
        Circle c2 = new Circle(10, 20, 5);
        Circle c3 = new Circle(10, 20, 6);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);

        Rectangle r1 = new Rectangle(0, 0, 10, 20);
        Rectangle r2 = new Rectangle(0, 0, 10, 20);
        assertEquals(r1, r2);
    }

    @Test
    void testRecordsInCollections() {
        // Records work well in sets
        java.util.Set<Point> pointSet = new java.util.HashSet<>();
        pointSet.add(new Point(1, 1));
        pointSet.add(new Point(1, 1)); // Duplicate
        pointSet.add(new Point(2, 2));

        assertEquals(2, pointSet.size()); // Duplicate removed

        // Records work well as map keys
        java.util.Map<Circle, String> circleMap = new java.util.HashMap<>();
        Circle key1 = new Circle(0, 0, 5);
        circleMap.put(key1, "small");

        // Lookup by value equality
        Circle key2 = new Circle(0, 0, 5);
        assertEquals("small", circleMap.get(key2));
    }

    @Test
    void testInterfaceTypeAssignment() {
        // Can assign to interface type
        Drawable drawable = new Circle(0, 0, 10);
        assertEquals(Math.PI * 100, drawable.area(), 0.001);

        Measurable measurable = new Rectangle(0, 0, 10, 20);
        assertEquals(60.0, measurable.perimeter());

        // Multiple interfaces
        Rectangle rect = new Rectangle(0, 0, 5, 5);
        Drawable d = rect;
        Measurable m = rect;

        assertEquals(25.0, d.area());
        assertEquals(20.0, m.perimeter());
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
