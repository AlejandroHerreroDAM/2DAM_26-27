package dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedMission {
    private int missionId;
    private int characterId;
    private LocalDate completionDate;
    private String characterName;
}
