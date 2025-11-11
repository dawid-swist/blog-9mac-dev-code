package dev.nmac.blog.examples.java17.part2;

import java.util.Objects;

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
