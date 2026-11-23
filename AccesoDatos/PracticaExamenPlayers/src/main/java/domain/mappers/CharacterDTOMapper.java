package domain.mappers;

import dao.model.PlayerCharacter;
import domain.model.CharacterDTO;
import domain.model.CharacterDTO;

import java.util.List;

public class CharacterDTOMapper {

    public PlayerCharacter dtoToEntity(CharacterDTO characterDTO) {
        return new PlayerCharacter(
                characterDTO.getCharacterId(),
                characterDTO.getName(),
                characterDTO.getType(),
                characterDTO.getLevel(),
                characterDTO.getPlayerId()
        );
    }

    public CharacterDTO entityToDto(PlayerCharacter character) {
        return new CharacterDTO(
                character.getCharacterId(),
                character.getName(),
                character.getType(),
                character.getLevel(),
                character.getPlayerId()
        );
    }
    
}
