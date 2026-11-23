package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompletedMissionDTO {
    private int missionId;
    private int characterId;
    private LocalDate completionDate;
}
