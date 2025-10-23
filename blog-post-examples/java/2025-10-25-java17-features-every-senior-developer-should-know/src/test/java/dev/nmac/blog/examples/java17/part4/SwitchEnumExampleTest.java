package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.SwitchEnumExample.Day;
import dev.nmac.blog.examples.java17.part4.SwitchEnumExample.Season;

/**
 * Unit tests for SwitchEnumExample demonstrating switch expressions with enums.
 *
 * These tests verify:
 * - Switch expressions correctly classify days as weekdays or weekends
 * - Exhaustiveness checking works for enum switches
 * - Traditional and modern approaches produce identical results
 * - Multiple case labels are handled correctly
 */
class SwitchEnumExampleTest {

    @Test
    @DisplayName("Should classify weekdays correctly using switch expression")
    void shouldClassifyWeekdaysCorrectly() {
        assertEquals("Weekday", SwitchEnumExample.getDayTypeModern(Day.MONDAY));
        assertEquals("Weekday", SwitchEnumExample.getDayTypeModern(Day.TUESDAY));
        assertEquals("Weekday", SwitchEnumExample.getDayTypeModern(Day.FRIDAY));
    }

    @Test
    @DisplayName("Should classify weekends correctly using switch expression")
    void shouldClassifyWeekendsCorrectly() {
        assertEquals("Weekend", SwitchEnumExample.getDayTypeModern(Day.SATURDAY));
        assertEquals("Weekend", SwitchEnumExample.getDayTypeModern(Day.SUNDAY));
    }

    @Test
    @DisplayName("Should produce identical results for traditional and modern switch")
    void shouldProduceIdenticalResultsForBothSwitches() {
        for (Day day : Day.values()) {
            String traditional = SwitchEnumExample.getDayTypeTraditional(day);
            String modern = SwitchEnumExample.getDayTypeModern(day);

            assertEquals(traditional, modern, "Results should match for " + day);
        }
    }

    @Test
    @DisplayName("Should return correct month count for all seasons")
    void shouldReturnCorrectMonthCountForAllSeasons() {
        for (Season season : Season.values()) {
            assertEquals(3, SwitchEnumExample.getSeasonMonths(season));
        }
    }

    @Test
    @DisplayName("Should describe Monday correctly")
    void shouldDescribeMondayCorrectly() {
        assertEquals("Start of work week", SwitchEnumExample.describeDay(Day.MONDAY));
    }

    @Test
    @DisplayName("Should describe Friday correctly")
    void shouldDescribeFridayCorrectly() {
        assertEquals("End of work week - TGIF!", SwitchEnumExample.describeDay(Day.FRIDAY));
    }

    @Test
    @DisplayName("Should describe weekend days correctly")
    void shouldDescribeWeekendDaysCorrectly() {
        String expected = "Weekend relaxation";

        assertEquals(expected, SwitchEnumExample.describeDay(Day.SATURDAY));
        assertEquals(expected, SwitchEnumExample.describeDay(Day.SUNDAY));
    }

    @Test
    @DisplayName("Should describe midweek days correctly")
    void shouldDescribeMidweekDaysCorrectly() {
        String expected = "Middle of week";

        assertEquals(expected, SwitchEnumExample.describeDay(Day.TUESDAY));
        assertEquals(expected, SwitchEnumExample.describeDay(Day.WEDNESDAY));
        assertEquals(expected, SwitchEnumExample.describeDay(Day.THURSDAY));
    }
}
