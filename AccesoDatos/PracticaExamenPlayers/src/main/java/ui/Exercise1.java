package ui;

import dao.repositories.JDBC.DBConnectionPool;
import domain.exceptions.DatabaseError;
import domain.services.CompletedMissionService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Exercise1 {
    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            CompletedMissionService service = container.select(CompletedMissionService.class).get();
            DBConnectionPool dbPool = container.select(DBConnectionPool.class).get();

            try {
                service.exportCompletedMissionsToXml(2);
                System.out.println("Completed missions exported to XML successfully.");
            } catch (DatabaseError e) {
                System.err.println(e.getMessage());
            } finally {
                dbPool.closePool();
            }
        }
    }
}