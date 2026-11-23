package domain.services;

import common.Constants;
import dao.interfaces.CompletedMissionRepository;
import dao.interfaces.PlayerCharacterRepository;
import dao.model.CompletedMission;
import dao.model.PlayerCharacter;
import domain.exceptions.DatabaseError;
import domain.exceptions.ForeignKeyError;
import domain.mappers.CharacterDTOMapper;
import domain.mappers.CompletedMissionDTOMapper;
import jakarta.inject.Inject;

import java.util.List;

public class CharacterService {
    private final PlayerCharacterRepository playerCharacterRepository;
    private final CharacterDTOMapper characterDTOMapper;
    private final CompletedMissionRepository completedMissionRepository;
    private final CompletedMissionDTOMapper completedMissionDTOMapper;

    @Inject
    public CharacterService(PlayerCharacterRepository playerCharacterRepository, CharacterDTOMapper characterDTOMapper, CompletedMissionRepository completedMissionRepository, CompletedMissionDTOMapper completedMissionDTOMapper) {
        this.playerCharacterRepository = playerCharacterRepository;
        this.characterDTOMapper = characterDTOMapper;
        this.completedMissionRepository = completedMissionRepository;
        this.completedMissionDTOMapper = completedMissionDTOMapper;
    }

    public void deleteCharacter(String characterName, boolean confirmation) {
        PlayerCharacter character = playerCharacterRepository.getByName(characterName);
        if (character == null) {
            throw new DatabaseError(Constants.NO_PLAYER_ERROR + characterName);
        }

        List<CompletedMission> missions = completedMissionRepository.getAllByCharacterName(characterName);
        if (!missions.isEmpty() && !confirmation) {
            throw new ForeignKeyError("Character " + characterName + " has completed missions. Confirmation required.");
        }

        playerCharacterRepository.deleteCharacter(character.getCharacterId(), confirmation);
    }
}

