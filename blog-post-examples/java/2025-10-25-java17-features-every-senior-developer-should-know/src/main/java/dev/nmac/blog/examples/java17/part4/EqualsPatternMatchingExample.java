package dev.nmac.blog.examples.java17.part4;

import java.util.Objects;

/**
 * Demonstrates pattern matching in equals() implementations.
 *
 * Pattern matching dramatically simplifies equals() methods by combining
 * null check, type check, and casting in a single operation. This reduces
 * boilerplate from 5-6 lines to a single expression.
 */
public class EqualsPatternMatchingExample {

    // Traditional equals() implementation - verbose
    public static class PointTraditional {
        private final int x, y;

        public PointTraditional(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            PointTraditional other = (PointTraditional) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Modern equals() with pattern matching - concise
    public static class PointModern {
        private final int x, y;

        public PointModern(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PointModern p &&
                   this.x == p.x && this.y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Complex object with multiple fields
    public static class Person {
        private final String firstName;
        private final String lastName;
        private final int age;

        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Person p &&
                   Objects.equals(this.firstName, p.firstName) &&
                   Objects.equals(this.lastName, p.lastName) &&
                   this.age == p.age;
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, age);
        }
    }

    // Demonstrating null safety
    public static class SafeEquals {
        private final String value;

        public SafeEquals(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // instanceof returns false for null - no explicit null check needed!
            return obj instanceof SafeEquals s && Objects.equals(this.value, s.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Modern Point ===");
        PointTraditional pt1 = new PointTraditional(10, 20);
        PointTraditional pt2 = new PointTraditional(10, 20);
        PointTraditional pt3 = new PointTraditional(30, 40);

        System.out.println("pt1.equals(pt2): " + pt1.equals(pt2));
        System.out.println("pt1.equals(pt3): " + pt1.equals(pt3));

        PointModern pm1 = new PointModern(10, 20);
        PointModern pm2 = new PointModern(10, 20);
        PointModern pm3 = new PointModern(30, 40);

        System.out.println("pm1.equals(pm2): " + pm1.equals(pm2));
        System.out.println("pm1.equals(pm3): " + pm1.equals(pm3));

        System.out.println("\n=== Person Equality ===");
        Person p1 = new Person("Alice", "Smith", 30);
        Person p2 = new Person("Alice", "Smith", 30);
        Person p3 = new Person("Bob", "Jones", 25);

        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1.equals(p3): " + p1.equals(p3));

        System.out.println("\n=== Null Safety ===");
        SafeEquals s1 = new SafeEquals("test");
        SafeEquals s2 = new SafeEquals("test");

        System.out.println("s1.equals(s2): " + s1.equals(s2));
        System.out.println("s1.equals(null): " + s1.equals(null));
        System.out.println("s1.equals(\"test\"): " + s1.equals("test"));
    }
}
