package dev.nmac.blog.examples.java17.part1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nmac.blog.examples.java17.part1.VarCollectionsExample.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VarCollectionsExample demonstrating var usage with collections.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
@DisplayName("var with Collections and Streams")
class VarCollectionsExampleTest {

    @Test
    @DisplayName("Should filter employees by minimum age using var with Stream API")
    void shouldFilterEmployeesByAge() {
        var employees = List.of(
            new Employee("Alice", 30, "Engineering"),
            new Employee("Bob", 25, "Marketing"),
            new Employee("Charlie", 35, "Engineering")
        );

        var result = filterByAge(employees, 28);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(e -> e.age() >= 28));
        assertEquals("Alice", result.get(0).name());
        assertEquals("Charlie", result.get(1).name());
    }

    @Test
    @DisplayName("Should return empty list when no employees meet age requirement")
    void shouldReturnEmptyListWhenNoEmployeesMeetAgeRequirement() {
        var employees = List.of(
            new Employee("Alice", 25, "Engineering"),
            new Employee("Bob", 23, "Marketing")
        );

        var result = filterByAge(employees, 30);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should group employees by department using var to avoid verbose Map declaration")
    void shouldGroupEmployeesByDepartment() {
        var employees = List.of(
            new Employee("Alice", 30, "Engineering"),
            new Employee("Bob", 25, "Marketing"),
            new Employee("Charlie", 35, "Engineering"),
            new Employee("Diana", 28, "Marketing")
        );

        var result = groupByDepartment(employees);

        assertEquals(2, result.size());
        assertEquals(2, result.get("Engineering").size());
        assertEquals(2, result.get("Marketing").size());

        var engineeringEmployees = result.get("Engineering");
        assertTrue(engineeringEmployees.stream()
            .anyMatch(e -> e.name().equals("Alice")));
        assertTrue(engineeringEmployees.stream()
            .anyMatch(e -> e.name().equals("Charlie")));
    }

    @Test
    @DisplayName("Should handle empty employee list gracefully")
    void shouldHandleEmptyEmployeeList() {
        var emptyList = List.<Employee>of();

        var filteredResult = filterByAge(emptyList, 30);
        var groupedResult = groupByDepartment(emptyList);

        assertTrue(filteredResult.isEmpty());
        assertTrue(groupedResult.isEmpty());
    }

    @Test
    @DisplayName("Should count employees by department using var with complex collectors")
    void shouldCountEmployeesByDepartment() {
        var employees = List.of(
            new Employee("Alice", 30, "Engineering"),
            new Employee("Bob", 25, "Marketing"),
            new Employee("Charlie", 35, "Engineering"),
            new Employee("Diana", 28, "Sales")
        );

        var counts = countByDepartment(employees);

        assertEquals(3, counts.size());
        assertEquals(2L, counts.get("Engineering"));
        assertEquals(1L, counts.get("Marketing"));
        assertEquals(1L, counts.get("Sales"));
    }

    @Test
    @DisplayName("Should demonstrate var simplifies working with complex generic types")
    void shouldDemonstrateVarWithComplexTypes() {
        var employees = List.of(
            new Employee("Alice", 30, "Engineering")
        );

        var grouped = groupByDepartment(employees);

        assertNotNull(grouped);
        assertTrue(grouped instanceof java.util.Map);
    }
}
