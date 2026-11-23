package ui;

import domain.model.CharacterDTO;
import domain.model.PlayerDTO;
import domain.services.PlayerService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.List;

public class Exercise2 {
    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        PlayerService playerService = container.select(PlayerService.class).get();

        CharacterDTO mrWhite = new CharacterDTO(0, "Mr White", "Warrior", 1, 0);
        CharacterDTO mrBlack = new CharacterDTO(0, "Mr Black", "Mage", 1, 0);

        PlayerDTO anabel = new PlayerDTO(
                0,
                "Anabel",
                "anabel@gmail.com",
                100,
                List.of(mrWhite, mrBlack)

//           Forma tradicional:
//              List<CharacterDTO> characters = new ArrayList<>();
//              characters.add(mrWhite);
//              characters.add(mrBlack);
//              PlayerDTO anabel = new PlayerDTO(0, "Anabel", "anabel@gmail.com", 100, characters);
        );

        int newPlayerId = playerService.savePlayerWithCharacters(anabel);
        System.out.println("Player Anabel (ID: " + newPlayerId + ") and her characters inserted successfully.");
    }
}
