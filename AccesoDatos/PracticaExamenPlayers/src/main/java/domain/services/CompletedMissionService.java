package domain.services;

import common.Constants;
import dao.interfaces.CompletedMissionRepository;
import dao.model.CompletedMission;
import dao.repositories.XML.XMLCompletedMissionsRepository;
import domain.exceptions.DatabaseError;
import jakarta.inject.Inject;

import java.util.List;

public class CompletedMissionService {
    private final CompletedMissionRepository completedMissionRepository;
    private final XMLCompletedMissionsRepository xmlRepository;

    @Inject
    public CompletedMissionService(CompletedMissionRepository completedMissionRepository, XMLCompletedMissionsRepository xmlRepository) {
        this.completedMissionRepository = completedMissionRepository;
        this.xmlRepository = xmlRepository;
    }

    public void exportCompletedMissionsToXml(int playerId) {
        List<CompletedMission> missions = completedMissionRepository.getAllByPlayerId(playerId);
        if (missions.isEmpty()) {
            throw new DatabaseError(Constants.NO_COMPLETED_MISSIONS_ERROR);
        }
        xmlRepository.save(playerId, missions);
    }


}
