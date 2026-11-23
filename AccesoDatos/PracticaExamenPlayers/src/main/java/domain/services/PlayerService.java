package domain.services;

import dao.interfaces.PlayerCharacterRepository;
import dao.interfaces.PlayerRepository;
import dao.model.Player;
import dao.model.PlayerCharacter;
import domain.mappers.CharacterDTOMapper;
import domain.mappers.PlayerDTOMapper;
import domain.model.CharacterDTO;
import domain.model.PlayerDTO;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerDTOMapper playerDTOMapper;

    private final CharacterDTOMapper characterDTOMapper;

    @Inject
    public PlayerService(PlayerRepository playerRepository, PlayerDTOMapper playerDTOMapper, CharacterDTOMapper characterDTOMapper) {
        this.playerRepository = playerRepository;
        this.playerDTOMapper = playerDTOMapper;
        this.characterDTOMapper = characterDTOMapper;
    }

    public int savePlayerWithCharacters(PlayerDTO playerDTO) {
        Player player = playerDTOMapper.dtoToEntity(playerDTO);
        List<PlayerCharacter> characters = new ArrayList<>();

        if (playerDTO.getCharacters() != null) {
            for (CharacterDTO characterDTO : playerDTO.getCharacters()) {
                characters.add(characterDTOMapper.dtoToEntity(characterDTO));
            }
        }

        int result = playerRepository.savePlayerWithCharacters(player, characters);

        return result;
    }

}
