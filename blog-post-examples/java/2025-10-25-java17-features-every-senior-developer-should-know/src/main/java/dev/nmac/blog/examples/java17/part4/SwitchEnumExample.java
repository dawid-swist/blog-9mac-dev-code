package dev.nmac.blog.examples.java17.part4;

/**
 * Demonstrates switch expressions with enums (JEP 361 - Java 14).
 *
 * Switch expressions introduce arrow syntax (`->`) that eliminates fall-through bugs
 * and enables using switch as an expression that returns values. When combined with
 * enums, the compiler enforces exhaustiveness - all enum values must be handled.
 *
 * This example compares traditional switch statements with modern switch expressions
 * and demonstrates exhaustiveness checking.
 */
public class SwitchEnumExample {

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    /**
     * Traditional switch statement - before Java 14.
     * Requires explicit break statements and separate variable declaration.
     */
    public static String getDayTypeTraditional(Day day) {
        String result;
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                result = "Weekday";
                break;
            case SATURDAY:
            case SUNDAY:
                result = "Weekend";
                break;
            default:
                throw new IllegalArgumentException("Unknown day");
        }
        return result;
    }

    /**
     * Modern switch expression - Java 14+.
     * Uses arrow syntax, no break needed, returns value directly.
     * Multiple case labels can be grouped with commas.
     */
    public static String getDayTypeModern(Day day) {
        return switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
            case SATURDAY, SUNDAY -> "Weekend";
        };
    }

    /**
     * Exhaustive enum switch - no default needed when all cases covered.
     * The compiler enforces that all enum values are handled. If a new enum
     * value is added, this code will fail to compile until updated.
     */
    public static int getSeasonMonths(Season season) {
        return switch (season) {
            case SPRING -> 3;  // Mar, Apr, May
            case SUMMER -> 3;  // Jun, Jul, Aug
            case FALL -> 3;    // Sep, Oct, Nov
            case WINTER -> 3;  // Dec, Jan, Feb
            // No default needed - all enum values covered!
            // If we add a new Season, compiler will error here
        };
    }

    /**
     * Switch expression with different descriptions per case.
     * Demonstrates grouping related cases and providing distinct values.
     */
    public static String describeDay(Day day) {
        return switch (day) {
            case MONDAY -> "Start of work week";
            case TUESDAY, WEDNESDAY, THURSDAY -> "Middle of week";
            case FRIDAY -> "End of work week - TGIF!";
            case SATURDAY, SUNDAY -> "Weekend relaxation";
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Modern ===");
        for (Day day : Day.values()) {
            System.out.println(day + ": Traditional=" + getDayTypeTraditional(day) +
                             ", Modern=" + getDayTypeModern(day));
        }

        System.out.println("\n=== Exhaustive Enum Switch ===");
        for (Season season : Season.values()) {
            System.out.println(season + " has " + getSeasonMonths(season) + " months");
        }

        System.out.println("\n=== Descriptive Switch ===");
        System.out.println(describeDay(Day.MONDAY));
        System.out.println(describeDay(Day.WEDNESDAY));
        System.out.println(describeDay(Day.FRIDAY));
        System.out.println(describeDay(Day.SUNDAY));
    }
}
