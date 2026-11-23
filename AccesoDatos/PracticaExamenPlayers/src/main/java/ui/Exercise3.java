package ui;

import domain.exceptions.DatabaseError;
import domain.services.CharacterService;
import domain.services.PlayerService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.Scanner;


public class Exercise3 {
    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        CharacterService characterService = container.select(CharacterService.class).get();

        String characterName = "Mr White";

        try {
            characterService.deleteCharacter(characterName, false);
            System.out.println("Character " + characterName + " deleted successfully.");
        } catch (Exception e) {
            System.out.print("Character " + characterName + " has completed missions. Do you want to delete it anyway? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("yes")) {
                characterService.deleteCharacter(characterName, true);
                System.out.println("Character " + characterName + " deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        }
    }
}


