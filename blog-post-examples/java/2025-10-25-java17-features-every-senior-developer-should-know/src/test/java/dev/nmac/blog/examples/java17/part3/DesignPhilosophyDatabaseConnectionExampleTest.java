package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Design Philosophy: Database Connection (sealed → non-sealed → open)")
class DesignPhilosophyDatabaseConnectionExampleTest {

    @Test
    @DisplayName("Should create PooledConnection")
    void shouldCreatePooledConnection() {
        var conn = new DesignPhilosophyDatabaseConnectionExample.PooledConnection("main-pool");

        assertEquals("main-pool", conn.poolName());
        assertNotNull(conn); // Valid PooledConnection instance
    }

    @Test
    @DisplayName("Should create CachedConnection")
    void shouldCreateCachedConnection() {
        var conn = new DesignPhilosophyDatabaseConnectionExample.CachedConnection("user-cache");

        assertEquals("user-cache", conn.cacheName());
    }

    @Test
    @DisplayName("Should create CustomDatabaseConnection (user-defined)")
    void shouldCreateCustomDatabaseConnection() {
        var conn = new DesignPhilosophyDatabaseConnectionExample.CustomDatabaseConnection("jdbc:custom://localhost");

        assertEquals("jdbc:custom://localhost", conn.connectionString());
    }

    @Test
    @DisplayName("Should create LoggingConnection (user-defined)")
    void shouldCreateLoggingConnection() {
        var conn = new DesignPhilosophyDatabaseConnectionExample.LoggingConnection("jdbc:mysql://localhost");

        assertEquals("jdbc:mysql://localhost", conn.connectionString());
    }

    @Test
    @DisplayName("Should create MetricsConnection (user-defined)")
    void shouldCreateMetricsConnection() {
        var conn = new DesignPhilosophyDatabaseConnectionExample.MetricsConnection("jdbc:postgres://localhost");

        assertEquals("jdbc:postgres://localhost", conn.connectionString());
    }

    @Test
    @DisplayName("Should verify DatabaseConnection is sealed")
    void shouldVerifyDatabaseConnectionIsSealed() {
        assertTrue(DesignPhilosophyDatabaseConnectionExample.DatabaseConnection.class.isSealed());
    }

    @Test
    @DisplayName("Should verify ManagedConnection is sealed")
    void shouldVerifyManagedConnectionIsSealed() {
        assertTrue(DesignPhilosophyDatabaseConnectionExample.ManagedConnection.class.isSealed());
    }

    @Test
    @DisplayName("Should verify UserConnection is NOT sealed (breaks the seal)")
    void shouldVerifyUserConnectionIsNotSealed() {
        assertFalse(DesignPhilosophyDatabaseConnectionExample.UserConnection.class.isSealed());
    }

    @Test
    @DisplayName("Should verify DatabaseConnection permits both ManagedConnection and UserConnection")
    void shouldVerifyDatabaseConnectionPermits() {
        var permitted = DesignPhilosophyDatabaseConnectionExample.DatabaseConnection.class.getPermittedSubclasses();
        assertEquals(2, permitted.length);

        var names = java.util.Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("ManagedConnection"));
        assertTrue(names.contains("UserConnection"));
    }

    @Test
    @DisplayName("Should verify ManagedConnection permits only PooledConnection and CachedConnection")
    void shouldVerifyManagedConnectionPermits() {
        var permitted = DesignPhilosophyDatabaseConnectionExample.ManagedConnection.class.getPermittedSubclasses();
        assertEquals(2, permitted.length);

        var names = java.util.Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("PooledConnection"));
        assertTrue(names.contains("CachedConnection"));
    }

    @Test
    @DisplayName("Should demonstrate why non-sealed breaks the seal for extensibility")
    void shouldDemonstratNonSealedBreaksChain() {
        // Framework has tight control over managed connections
        assertTrue(DesignPhilosophyDatabaseConnectionExample.ManagedConnection.class.isSealed());

        // But users can freely extend UserConnection
        assertFalse(DesignPhilosophyDatabaseConnectionExample.UserConnection.class.isSealed());

        // This allows unlimited user implementations:
        // - CustomDatabaseConnection
        // - LoggingConnection
        // - MetricsConnection
        // ... and infinitely more

        // Create three different user implementations
        var custom = new DesignPhilosophyDatabaseConnectionExample.CustomDatabaseConnection("jdbc:custom://localhost");
        var logging = new DesignPhilosophyDatabaseConnectionExample.LoggingConnection("jdbc:mysql://localhost");
        var metrics = new DesignPhilosophyDatabaseConnectionExample.MetricsConnection("jdbc:postgres://localhost");

        // All are valid UserConnections
        assertTrue(custom instanceof DesignPhilosophyDatabaseConnectionExample.UserConnection);
        assertTrue(logging instanceof DesignPhilosophyDatabaseConnectionExample.UserConnection);
        assertTrue(metrics instanceof DesignPhilosophyDatabaseConnectionExample.UserConnection);

        // All are valid DatabaseConnections
        assertTrue(custom instanceof DesignPhilosophyDatabaseConnectionExample.DatabaseConnection);
        assertTrue(logging instanceof DesignPhilosophyDatabaseConnectionExample.DatabaseConnection);
        assertTrue(metrics instanceof DesignPhilosophyDatabaseConnectionExample.DatabaseConnection);
    }

    @Test
    @DisplayName("Should show top-level control while allowing bottom-level flexibility")
    void shouldShowTopLevelControlWithBottomLevelFlexibility() {
        // Top level is sealed - only two branches allowed
        var topPermitted = DesignPhilosophyDatabaseConnectionExample.DatabaseConnection.class.getPermittedSubclasses();
        assertEquals(2, topPermitted.length); // Only ManagedConnection and UserConnection

        // ManagedConnection branch - fully controlled
        var managedPermitted = DesignPhilosophyDatabaseConnectionExample.ManagedConnection.class.getPermittedSubclasses();
        assertEquals(2, managedPermitted.length); // Only PooledConnection and CachedConnection

        // UserConnection branch - open for extension
        assertFalse(DesignPhilosophyDatabaseConnectionExample.UserConnection.class.isSealed());
        // Users can add unlimited implementations

        System.out.println("✓ Top level (DatabaseConnection) sealed: controls 2 branches");
        System.out.println("✓ ManagedConnection sealed: controls 2 implementations");
        System.out.println("✓ UserConnection non-sealed: allows unlimited user implementations");
    }
}
