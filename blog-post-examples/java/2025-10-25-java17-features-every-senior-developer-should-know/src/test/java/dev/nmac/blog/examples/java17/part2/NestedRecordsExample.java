package dev.nmac.blog.examples.java17.part2;

import java.util.Objects;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Nested Records Examples - Record Composition
 *
 * This example demonstrates:
 * - Records containing other records
 * - Hierarchical data modeling with records
 * - Accessing nested record components
 * - Immutability in nested structures
 * - Methods working with nested records
 *
 * Records compose naturally to model complex domain objects.
 */
public class NestedRecordsExample {

    /**
     * Address record - represents a physical address.
     */
    public record Address(String street, String city, String zipCode, String country) {

        /**
         * Compact constructor with validation and normalization.
         */
        public Address {
            Objects.requireNonNull(street, "Street cannot be null");
            Objects.requireNonNull(city, "City cannot be null");
            Objects.requireNonNull(zipCode, "Zip code cannot be null");
            Objects.requireNonNull(country, "Country cannot be null");

            if (street.isBlank()) {
                throw new IllegalArgumentException("Street cannot be blank");
            }
            if (city.isBlank()) {
                throw new IllegalArgumentException("City cannot be blank");
            }

            // Normalize zip code (remove spaces)
            zipCode = zipCode.replaceAll("\\s", "");

            // Normalize country to uppercase
            country = country.toUpperCase();
        }

        /**
         * Returns formatted address string.
         *
         * @return single-line address
         */
        public String format() {
            return street + ", " + city + " " + zipCode + ", " + country;
        }

        /**
         * Checks if address is in given country.
         *
         * @param countryCode country code to check
         * @return true if this address is in specified country
         */
        public boolean isInCountry(String countryCode) {
            return country.equalsIgnoreCase(countryCode);
        }
    }

    /**
     * Employee record - contains nested Address.
     */
    public record Employee(String name, int id, Address address) {

        /**
         * Compact constructor with validation.
         */
        public Employee {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(address, "Address cannot be null");

            if (name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive: " + id);
            }
        }

        /**
         * Returns full formatted address.
         *
         * @return formatted address string
         */
        public String getFullAddress() {
            return address.format();
        }

        /**
         * Returns employee's city.
         *
         * @return city from nested address
         */
        public String getCity() {
            return address.city();
        }

        /**
         * Creates new Employee with updated address (relocation).
         *
         * @param newAddress new address
         * @return new Employee instance with updated address
         */
        public Employee relocate(Address newAddress) {
            return new Employee(name, id, newAddress);
        }

        /**
         * Checks if employee works in given country.
         *
         * @param countryCode country code
         * @return true if employee's address is in specified country
         */
        public boolean worksInCountry(String countryCode) {
            return address.isInCountry(countryCode);
        }
    }

    /**
     * Contact record - multiple levels of nesting.
     */
    public record ContactInfo(String email, String phone) {

        public ContactInfo {
            Objects.requireNonNull(email, "Email cannot be null");
            Objects.requireNonNull(phone, "Phone cannot be null");

            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email: " + email);
            }

            // Normalize phone (remove non-digits)
            phone = phone.replaceAll("[^0-9+]", "");
        }
    }

    /**
     * Person record - contains both Address and ContactInfo.
     */
    public record Person(String name, Address address, ContactInfo contact) {

        public Person {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(address, "Address cannot be null");
            Objects.requireNonNull(contact, "Contact cannot be null");
        }

        /**
         * Returns full profile string.
         *
         * @return formatted profile
         */
        public String profile() {
            return name + "\n" +
                   "Address: " + address.format() + "\n" +
                   "Email: " + contact.email() + "\n" +
                   "Phone: " + contact.phone();
        }
    }

    /**
     * Company record - demonstrates record within record within record.
     */
    public record Company(String name, Address headquarters) {

        public Company {
            Objects.requireNonNull(name, "Company name cannot be null");
            Objects.requireNonNull(headquarters, "Headquarters address cannot be null");

            if (name.isBlank()) {
                throw new IllegalArgumentException("Company name cannot be blank");
            }
        }
    }

    /**
     * EmployeeWithCompany - three levels of nesting.
     */
    public record EmployeeWithCompany(String name, int id, Company company) {

        public EmployeeWithCompany {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(company, "Company cannot be null");

            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
        }

        /**
         * Gets company headquarters city (deeply nested).
         *
         * @return city where company headquarters is located
         */
        public String getHeadquartersCity() {
            return company.headquarters().city();
        }

        /**
         * Gets full headquarters address.
         *
         * @return formatted headquarters address
         */
        public String getHeadquartersAddress() {
            return company.headquarters().format();
        }
    }

    // ============================================================
    // JUnit Tests
    // ============================================================

    @Test
    void testAddressCreation() {
        Address addr = new Address("123 Main St", "Springfield", "12345", "USA");

        assertEquals("123 Main St", addr.street());
        assertEquals("Springfield", addr.city());
        assertEquals("12345", addr.zipCode());
        assertEquals("USA", addr.country());
    }

    @Test
    void testAddressNormalization() {
        // Zip code with spaces
        Address addr1 = new Address("123 Main", "City", "12 345", "USA");
        assertEquals("12345", addr1.zipCode());

        // Country normalized to uppercase
        Address addr2 = new Address("456 Oak", "Town", "67890", "usa");
        assertEquals("USA", addr2.country());
    }

    @Test
    void testAddressFormat() {
        Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
        String formatted = addr.format();

        assertEquals("123 Main St, Springfield 12345, USA", formatted);
    }

    @Test
    void testAddressIsInCountry() {
        Address usAddr = new Address("123 Main", "City", "12345", "USA");
        Address ukAddr = new Address("10 Downing", "London", "SW1", "UK");

        assertTrue(usAddr.isInCountry("USA"));
        assertTrue(usAddr.isInCountry("usa")); // Case insensitive
        assertFalse(usAddr.isInCountry("UK"));

        assertTrue(ukAddr.isInCountry("UK"));
        assertFalse(ukAddr.isInCountry("USA"));
    }

    @Test
    void testEmployeeNestedAccess() {
        Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
        Employee emp = new Employee("Alice Smith", 1001, addr);

        // Direct access to nested properties
        assertEquals("Alice Smith", emp.name());
        assertEquals(1001, emp.id());
        assertEquals("Springfield", emp.address().city());
        assertEquals("12345", emp.address().zipCode());
    }

    @Test
    void testEmployeeGetFullAddress() {
        Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
        Employee emp = new Employee("Bob Jones", 1002, addr);

        String expected = "123 Main St, Springfield 12345, USA";
        assertEquals(expected, emp.getFullAddress());
    }

    @Test
    void testEmployeeGetCity() {
        Address addr = new Address("456 Oak Ave", "Portland", "97201", "USA");
        Employee emp = new Employee("Charlie", 1003, addr);

        assertEquals("Portland", emp.getCity());
    }

    @Test
    void testEmployeeRelocation() {
        Address addr1 = new Address("123 Main St", "Springfield", "12345", "USA");
        Employee emp1 = new Employee("Alice", 1001, addr1);

        Address addr2 = new Address("456 Oak Ave", "Portland", "97201", "USA");
        Employee emp2 = emp1.relocate(addr2);

        // New employee has new address
        assertEquals("Portland", emp2.getCity());
        assertEquals("Portland", emp2.address().city());

        // Old employee unchanged (immutability)
        assertEquals("Springfield", emp1.getCity());
        assertEquals("Springfield", emp1.address().city());

        // Same ID and name
        assertEquals(emp1.id(), emp2.id());
        assertEquals(emp1.name(), emp2.name());
    }

    @Test
    void testEmployeeWorksInCountry() {
        Address usAddr = new Address("123 Main", "City", "12345", "USA");
        Employee usEmp = new Employee("Alice", 1, usAddr);

        assertTrue(usEmp.worksInCountry("USA"));
        assertFalse(usEmp.worksInCountry("UK"));
    }

    @Test
    void testPersonMultipleNestedRecords() {
        Address addr = new Address("123 Main", "City", "12345", "USA");
        ContactInfo contact = new ContactInfo("alice@example.com", "(123) 456-7890");
        Person person = new Person("Alice Smith", addr, contact);

        assertEquals("Alice Smith", person.name());
        assertEquals("City", person.address().city());
        assertEquals("alice@example.com", person.contact().email());
    }

    @Test
    void testContactInfoNormalization() {
        ContactInfo contact = new ContactInfo(
            "bob@example.com",
            "(123) 456-7890"
        );

        // Phone normalized to digits only
        assertEquals("1234567890", contact.phone());
    }

    @Test
    void testPersonProfile() {
        Address addr = new Address("123 Main", "Springfield", "12345", "USA");
        ContactInfo contact = new ContactInfo("alice@test.com", "1234567890");
        Person person = new Person("Alice", addr, contact);

        String profile = person.profile();

        assertTrue(profile.contains("Alice"));
        assertTrue(profile.contains("Springfield"));
        assertTrue(profile.contains("alice@test.com"));
        assertTrue(profile.contains("1234567890"));
    }

    @Test
    void testThreeLevelNesting() {
        Address hqAddr = new Address("1 Corporate Blvd", "San Francisco", "94105", "USA");
        Company company = new Company("TechCorp", hqAddr);
        EmployeeWithCompany emp = new EmployeeWithCompany("Alice", 1001, company);

        // Access deeply nested city
        assertEquals("San Francisco", emp.getHeadquartersCity());

        // Access nested through chain
        assertEquals("San Francisco", emp.company().headquarters().city());
        assertEquals("94105", emp.company().headquarters().zipCode());
    }

    @Test
    void testEmployeeWithCompanyMethods() {
        Address hqAddr = new Address("1 Corp Blvd", "NYC", "10001", "USA");
        Company company = new Company("ACME Inc", hqAddr);
        EmployeeWithCompany emp = new EmployeeWithCompany("Bob", 2001, company);

        assertEquals("NYC", emp.getHeadquartersCity());
        assertEquals("1 Corp Blvd, NYC 10001, USA", emp.getHeadquartersAddress());
    }

    @Test
    void testNestedRecordEquality() {
        Address addr1 = new Address("123 Main", "City", "12345", "USA");
        Address addr2 = new Address("123 Main", "City", "12345", "USA");

        Employee emp1 = new Employee("Alice", 1001, addr1);
        Employee emp2 = new Employee("Alice", 1001, addr2);

        // Employees are equal (nested address also equal)
        assertEquals(emp1, emp2);
    }

    @Test
    void testNestedRecordInequality() {
        Address addr1 = new Address("123 Main", "City1", "12345", "USA");
        Address addr2 = new Address("123 Main", "City2", "12345", "USA");

        Employee emp1 = new Employee("Alice", 1001, addr1);
        Employee emp2 = new Employee("Alice", 1001, addr2);

        // Different because nested address differs
        assertNotEquals(emp1, emp2);
    }

    @Test
    void testImmutability() {
        Address addr = new Address("123 Main", "City", "12345", "USA");
        Employee emp = new Employee("Alice", 1001, addr);

        // Cannot modify nested record
        // addr.city = "NewCity"; // Won't compile - records are immutable

        // Must create new instances
        Address newAddr = new Address(addr.street(), "NewCity", addr.zipCode(), addr.country());
        Employee relocated = emp.relocate(newAddr);

        assertEquals("City", emp.address().city()); // Original unchanged
        assertEquals("NewCity", relocated.address().city());
    }

    @Test
    void testValidation() {
        // Address validation
        assertThrows(IllegalArgumentException.class, () -> {
            new Address("", "City", "12345", "USA"); // Blank street
        });

        assertThrows(NullPointerException.class, () -> {
            new Address(null, "City", "12345", "USA"); // Null street
        });

        // Employee validation
        assertThrows(IllegalArgumentException.class, () -> {
            Address addr = new Address("123 Main", "City", "12345", "USA");
            new Employee("Alice", 0, addr); // Invalid ID
        });

        assertThrows(NullPointerException.class, () -> {
            new Employee("Alice", 1, null); // Null address
        });

        // ContactInfo validation
        assertThrows(IllegalArgumentException.class, () -> {
            new ContactInfo("invalidemail", "1234567890"); // No @ in email
        });
    }

    // Main method for manual testing
    public static void main(String[] args) {
        System.out.println("=== Nested Records Examples ===\n");

        // Simple nesting
        System.out.println("--- Employee with Address ---");
        Address addr = new Address("123 Main St", "Springfield", "12345", "USA");
        Employee emp = new Employee("Alice Smith", 1001, addr);

        System.out.println("Employee: " + emp.name());
        System.out.println("ID: " + emp.id());
        System.out.println("City: " + emp.getCity());
        System.out.println("Full address: " + emp.getFullAddress());
        System.out.println("Works in USA: " + emp.worksInCountry("USA"));

        System.out.println("\n--- Relocation ---");
        Address newAddr = new Address("456 Oak Ave", "Portland", "97201", "USA");
        Employee relocated = emp.relocate(newAddr);

        System.out.println("Original city: " + emp.getCity());
        System.out.println("New city: " + relocated.getCity());

        System.out.println("\n--- Person with Multiple Nested Records ---");
        ContactInfo contact = new ContactInfo("alice@example.com", "(123) 456-7890");
        Person person = new Person("Alice Smith", addr, contact);

        System.out.println("\n" + person.profile());

        System.out.println("\n--- Three-Level Nesting ---");
        Address hqAddr = new Address("1 Corporate Blvd", "San Francisco", "94105", "USA");
        Company company = new Company("TechCorp Inc", hqAddr);
        EmployeeWithCompany techEmp = new EmployeeWithCompany("Bob Jones", 2001, company);

        System.out.println("Employee: " + techEmp.name());
        System.out.println("Company: " + techEmp.company().name());
        System.out.println("HQ City: " + techEmp.getHeadquartersCity());
        System.out.println("HQ Address: " + techEmp.getHeadquartersAddress());
    }
}
