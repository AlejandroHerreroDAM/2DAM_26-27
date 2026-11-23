package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MissionDTO {
    private int missionId;
    private String name;
    private String description;
    private int tokens;
}
