package domain.mappers;

import dao.model.CompletedMission;
import domain.model.CompletedMissionDTO;

public class CompletedMissionDTOMapper {
    public CompletedMission dtoToEntity(CompletedMissionDTO completedMissionDTO) {
        return new CompletedMission(
                completedMissionDTO.getMissionId(),
                completedMissionDTO.getCharacterId(),
                completedMissionDTO.getCompletionDate(),
                null
        );
    }

    public CompletedMissionDTO entityToDto(CompletedMission completedMission) {
        return new CompletedMissionDTO(
                completedMission.getMissionId(),
                completedMission.getCharacterId(),
                completedMission.getCompletionDate()
        );
    }
}
