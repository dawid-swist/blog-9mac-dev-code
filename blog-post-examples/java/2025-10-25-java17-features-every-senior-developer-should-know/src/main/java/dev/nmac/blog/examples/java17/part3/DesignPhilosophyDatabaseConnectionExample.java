package dev.nmac.blog.examples.java17.part3;

import java.util.List;

/**
 * Demonstrates the design philosophy of sealed classes:
 * Pattern 2 - Breaking the Chain (sealed → non-sealed → open)
 *
 * Shows how to control the top level but allow flexibility below,
 * using non-sealed as an escape hatch for library extensions.
 */
public class DesignPhilosophyDatabaseConnectionExample {

    // Top level: sealed, permits both sealed and non-sealed children
    public sealed abstract static class DatabaseConnection
        permits ManagedConnection, UserConnection {
        public abstract void execute(String query);
    }

    // Branch 1: sealed - tight control
    public sealed abstract static class ManagedConnection extends DatabaseConnection
        permits PooledConnection, CachedConnection {
        // Framework-controlled connections
    }

    public static final class PooledConnection extends ManagedConnection {
        private final String poolName;

        public PooledConnection(String poolName) {
            this.poolName = poolName;
        }

        public String poolName() { return poolName; }

        @Override
        public void execute(String query) {
            System.out.println("Executing on pool '" + poolName + "': " + query);
        }
    }

    public static final class CachedConnection extends ManagedConnection {
        private final String cacheName;

        public CachedConnection(String cacheName) {
            this.cacheName = cacheName;
        }

        public String cacheName() { return cacheName; }

        @Override
        public void execute(String query) {
            System.out.println("Executing with cache '" + cacheName + "': " + query);
        }
    }

    // Branch 2: non-sealed - BREAKS THE SEAL
    public non-sealed abstract static class UserConnection extends DatabaseConnection {
        // Users can extend this freely!
        protected String connectionString;

        protected UserConnection(String connectionString) {
            this.connectionString = connectionString;
        }

        public String connectionString() { return connectionString; }
    }

    // Now users can create unlimited custom connections
    public static class CustomDatabaseConnection extends UserConnection {
        public CustomDatabaseConnection(String connectionString) {
            super(connectionString);
        }

        @Override
        public void execute(String query) {
            System.out.println("Custom connection executing: " + query);
        }
    }

    public static class LoggingConnection extends UserConnection {
        public LoggingConnection(String connectionString) {
            super(connectionString);
        }

        @Override
        public void execute(String query) {
            System.out.println("[LOG] Query: " + query);
        }
    }

    public static class MetricsConnection extends UserConnection {
        public MetricsConnection(String connectionString) {
            super(connectionString);
        }

        @Override
        public void execute(String query) {
            System.out.println("[METRICS] Recording metrics for: " + query);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Framework-Controlled Connections ===");
        var pooledConn = new PooledConnection("main-pool");
        pooledConn.execute("SELECT * FROM users");

        var cachedConn = new CachedConnection("user-cache");
        cachedConn.execute("SELECT * FROM products");

        System.out.println("\n=== User-Created Custom Connections ===");
        var customConn = new CustomDatabaseConnection("jdbc:custom://localhost");
        customConn.execute("SELECT * FROM orders");

        var loggingConn = new LoggingConnection("jdbc:mysql://localhost");
        loggingConn.execute("SELECT * FROM payments");

        var metricsConn = new MetricsConnection("jdbc:postgres://localhost");
        metricsConn.execute("SELECT * FROM inventory");

        System.out.println("\n=== Connection Type Analysis ===");
        var connections = List.<DatabaseConnection>of(
            pooledConn,
            cachedConn,
            customConn,
            loggingConn,
            metricsConn
        );

        System.out.println("DatabaseConnection is sealed: " + DatabaseConnection.class.isSealed());
        System.out.println("ManagedConnection is sealed: " + ManagedConnection.class.isSealed());
        System.out.println("UserConnection is sealed: " + UserConnection.class.isSealed()); // false!

        System.out.println("\nManagedConnection permits:");
        for (var permitted : ManagedConnection.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }

        System.out.println("\nTotal connections: " + connections.size());
    }
}
