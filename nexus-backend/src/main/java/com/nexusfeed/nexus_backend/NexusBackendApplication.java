package com.nexusfeed.nexus_backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NexusBackendApplication {

	public static void main(String[] args) {
        System.out.println("🚀 Java is running from: " + System.getProperty("user.dir"));
        SpringApplication.run(NexusBackendApplication.class, args);
	}

    @Bean
    public CommandLineRunner diagnosticTest() {
        return args -> {
            System.out.println("\n--- 🔍 DATABASE DIAGNOSTIC ---");
            String url = "jdbc:sqlite:nexus-backend/jobs.db"; // Match your properties path
            
            try (Connection conn = DriverManager.getConnection(url)) {
                ResultSet rs = conn.getMetaData().getTables(null, null, "jobs", null);
                if (rs.next()) {
                    System.out.println("✅ SUCCESS: Table 'jobs' found!");
                } else {
                    System.out.println("❌ ERROR: Connected to file, but table 'jobs' is MISSING.");
                    System.out.println("Check if your Python script used a different table name.");
                }
            } catch (Exception e) {
                System.out.println("❌ CONNECTION FAILED: " + e.getMessage());
            }
            System.out.println("------------------------------\n");
        };
    }
}
