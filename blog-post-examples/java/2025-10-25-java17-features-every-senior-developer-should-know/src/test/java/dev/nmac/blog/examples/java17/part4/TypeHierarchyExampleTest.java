package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.TypeHierarchyExample.*;

class TypeHierarchyExampleTest {

    @Test
    @DisplayName("Should describe Circle using pattern matching")
    void shouldDescribeCircle() {
        Shape circle = new Circle(5.0);
        String result = TypeHierarchyExample.describeModern(circle);
        assertEquals("Circle with radius 5.00", result);
    }

    @Test
    @DisplayName("Should describe Rectangle using pattern matching")
    void shouldDescribeRectangle() {
        Shape rectangle = new Rectangle(10.0, 20.0);
        String result = TypeHierarchyExample.describeModern(rectangle);
        assertEquals("Rectangle 10.00 x 20.00", result);
    }

    @Test
    @DisplayName("Should describe Triangle using pattern matching")
    void shouldDescribeTriangle() {
        Shape triangle = new Triangle(6.0, 8.0);
        String result = TypeHierarchyExample.describeModern(triangle);
        assertEquals("Triangle with base 6.00 and height 8.00", result);
    }

    @Test
    @DisplayName("Should produce identical results for traditional and modern approaches")
    void shouldProduceIdenticalResults() {
        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(10.0, 20.0),
            new Triangle(6.0, 8.0)
        };

        for (Shape shape : shapes) {
            String traditional = TypeHierarchyExample.describeTraditional(shape);
            String modern = TypeHierarchyExample.describeModern(shape);
            assertEquals(traditional, modern);
        }
    }

    @Test
    @DisplayName("Should classify large circle by radius")
    void shouldClassifyLargeCircleByRadius() {
        Shape largeCircle = new Circle(15.0);
        String result = TypeHierarchyExample.classifyBySize(largeCircle);
        assertEquals("Large circle (radius > 10)", result);
    }

    @Test
    @DisplayName("Should classify small circle")
    void shouldClassifySmallCircle() {
        Shape smallCircle = new Circle(3.0);
        String result = TypeHierarchyExample.classifyBySize(smallCircle);
        assertEquals("Small shape", result);
    }

    @Test
    @DisplayName("Should classify large rectangle by dimensions")
    void shouldClassifyLargeRectangleByDimensions() {
        Shape largeRect = new Rectangle(25.0, 10.0);
        String result = TypeHierarchyExample.classifyBySize(largeRect);
        assertEquals("Large rectangle (dimension > 20)", result);
    }

    @Test
    @DisplayName("Should calculate circle perimeter correctly")
    void shouldCalculateCirclePerimeter() {
        Shape circle = new Circle(5.0);
        double perimeter = TypeHierarchyExample.perimeter(circle);
        assertEquals(2 * Math.PI * 5.0, perimeter, 0.001);
    }

    @Test
    @DisplayName("Should calculate rectangle perimeter correctly")
    void shouldCalculateRectanglePerimeter() {
        Shape rectangle = new Rectangle(10.0, 20.0);
        double perimeter = TypeHierarchyExample.perimeter(rectangle);
        assertEquals(60.0, perimeter, 0.001);
    }

    @Test
    @DisplayName("Should calculate triangle perimeter correctly")
    void shouldCalculateTrianglePerimeter() {
        Shape triangle = new Triangle(3.0, 4.0);
        double perimeter = TypeHierarchyExample.perimeter(triangle);
        assertEquals(12.0, perimeter, 0.001); // 3 + 4 + 5 (Pythagorean triple)
    }
}
