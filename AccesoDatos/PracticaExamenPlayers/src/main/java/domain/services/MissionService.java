package domain.services;

import common.Constants;
import dao.interfaces.CompletedMissionRepository;
import dao.interfaces.MissionRepository;
import dao.interfaces.PlayerCharacterRepository;
import dao.interfaces.PlayerRepository;
import dao.model.CompletedMission;
import dao.model.Mission;
import dao.model.Player;
import dao.model.PlayerCharacter;
import domain.exceptions.DatabaseError;
import domain.exceptions.NotEnoughTokensError;
import jakarta.inject.Inject;

import java.time.LocalDate;

public class MissionService {

    private final PlayerRepository playerRepository;
    private final PlayerCharacterRepository characterRepository;
    private final MissionRepository missionRepository;
    private final CompletedMissionRepository completedMissionRepository;

    @Inject
    public MissionService(PlayerRepository playerRepository,
                          PlayerCharacterRepository characterRepository,
                          MissionRepository missionRepository,
                          CompletedMissionRepository completedMissionRepository) {
        this.playerRepository = playerRepository;
        this.characterRepository = characterRepository;
        this.missionRepository = missionRepository;
        this.completedMissionRepository = completedMissionRepository;
    }

    public void completeMission(String playerName, String characterName, String missionName, LocalDate date) {
        Player player = playerRepository.getByName(playerName);
        if (player == null) {
            throw new DatabaseError(Constants.NO_PLAYER_ERROR + playerName);
        }

        PlayerCharacter character = characterRepository.getByName(characterName);
        if (character == null || character.getPlayerId() != player.getPlayerId()) {
            throw new DatabaseError("Character " + characterName + " does not belong to player " + playerName);
        }

        Mission mission = missionRepository.getByName(missionName);
        if (mission == null) {
            throw new DatabaseError("Mission not found: " + missionName);
        }

        if (player.getTokens() < mission.getTokens()) {
            throw new NotEnoughTokensError("Player " + playerName + " does not have enough tokens ("
                    + player.getTokens() + " available, " + mission.getTokens() + " required).");
        }

        int remainingTokens = player.getTokens() - mission.getTokens();
        CompletedMission completedMission = new CompletedMission(
                mission.getMissionId(),
                character.getCharacterId(),
                date,
                character.getName()
        );

        completedMissionRepository.saveCompletedMission(completedMission);
        playerRepository.updateTokens(player.getPlayerId(), remainingTokens);
    }
}