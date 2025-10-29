package dev.nmac.blog.examples.java17.part1;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstrates the use of var keyword with collections and Stream API.
 *
 * This example shows how var reduces boilerplate when working with
 * complex generic types, particularly in stream operations and collections.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
public class VarCollectionsExample {

    /**
     * Employee record for demonstration purposes.
     * Records are covered in detail in Part 2 of the series.
     */
    public record Employee(String name, int age, String department) {}

    /**
     * Filters employees by minimum age using var with Stream API.
     *
     * @param employees list of employees to filter
     * @param minAge minimum age threshold
     * @return filtered list of employees aged minAge or older
     */
    public static List<Employee> filterByAge(List<Employee> employees, int minAge) {
        // Using var eliminates the need to declare List<Employee> type
        var filtered = employees.stream()
            .filter(e -> e.age() >= minAge)
            .toList(); // Java 16+ - returns immutable list

        return filtered;
    }

    /**
     * Groups employees by department using var to avoid verbose Map declaration.
     *
     * Without var, this would require:
     * Map<String, List<Employee>> grouped = employees.stream()...
     *
     * @param employees list of employees to group
     * @return map with department as key and list of employees as value
     */
    public static Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
        // var makes this much more readable by avoiding repetition of
        // the complex Map<String, List<Employee>> type
        var grouped = employees.stream()
            .collect(Collectors.groupingBy(Employee::department));

        return grouped;
    }

    /**
     * Counts employees per department.
     *
     * @param employees list of employees
     * @return map with department as key and employee count as value
     */
    public static Map<String, Long> countByDepartment(List<Employee> employees) {
        var counts = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.counting()
            ));

        return counts;
    }

    /**
     * Main method demonstrating var usage with collections.
     */
    public static void main(String[] args) {
        System.out.println("=== var with Collections and Streams ===\n");

        // Using var with List.of() - type is clearly List<Employee>
        var employees = List.of(
            new Employee("Alice", 30, "Engineering"),
            new Employee("Bob", 25, "Marketing"),
            new Employee("Charlie", 35, "Engineering"),
            new Employee("Diana", 28, "Marketing"),
            new Employee("Eve", 32, "Engineering"),
            new Employee("Frank", 27, "Sales")
        );

        System.out.println("All employees:");
        employees.forEach(System.out::println);

        // Example 1: Filtering with var
        System.out.println("\nEmployees aged 28 or older:");
        var seniors = filterByAge(employees, 28);
        seniors.forEach(System.out::println);

        // Example 2: Grouping with var - avoids verbose Map<String, List<Employee>>
        System.out.println("\nEmployees grouped by department:");
        var byDepartment = groupByDepartment(employees);
        byDepartment.forEach((dept, emps) -> {
            System.out.println(dept + ": " + emps);
        });

        // Example 3: Counting with var
        System.out.println("\nEmployee count by department:");
        var counts = countByDepartment(employees);
        counts.forEach((dept, count) -> {
            System.out.println(dept + ": " + count);
        });

        // Example 4: var in enhanced for-loop
        System.out.println("\nEngineering department employees:");
        for (var employee : byDepartment.get("Engineering")) {
            System.out.println("  - " + employee.name() + " (age " + employee.age() + ")");
        }
    }
}
