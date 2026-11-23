package dao.interfaces;

import dao.model.CompletedMission;

import java.util.List;

public interface CompletedMissionRepository {
    List<CompletedMission> getAllByPlayerId(int playerId);
    List<CompletedMission> getAllByCharacterName(String name);
    void saveCompletedMission(CompletedMission mission);
    void deleteAllByCharacterName(String characterName);

}
