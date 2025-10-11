package main;

import config.DatabaseConfig;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("Medicine Inventory System");
        System.out.println("=================================\n");
        
        // Test database connection
        System.out.println("Testing database connection...");
        boolean connected = DatabaseConfig.testConnection();
        
        if (connected) {
            System.out.println("\n✅ System is ready to use!");
        } else {
            System.out.println("\n❌ Please check your database configuration");
        }
    }
}