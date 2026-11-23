package ui;

import domain.exceptions.DatabaseError;
import domain.exceptions.NotEnoughTokensError;
import domain.services.MissionService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.time.LocalDate;

public class Exercise4 {
    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();

        try (SeContainer container = initializer.initialize()) {
            MissionService missionService = container.select(MissionService.class).get();

            missionService.completeMission("Anabel", "Mr White", "Dragon Hunt", LocalDate.now());
            System.out.println("Mission 'Dragon Hunt' completed by Mr White! Anabel's tokens updated.");

        } catch (NotEnoughTokensError | DatabaseError e) {
            System.err.println(e.getMessage());
        }
    }
}