package domain.mappers;

import dao.model.Mission;
import domain.model.MissionDTO;

public class MissionDTOMapper {
    public Mission dtoToEntity(MissionDTO missionDTO) {
        return new Mission(
                missionDTO.getMissionId(),
                missionDTO.getName(),
                missionDTO.getDescription(),
                missionDTO.getTokens()
        );
    }

    public MissionDTO entityToDto(Mission mission) {
        return new MissionDTO(
                mission.getMissionId(),
                mission.getName(),
                mission.getDescription(),
                mission.getTokens()
        );
    }
}
