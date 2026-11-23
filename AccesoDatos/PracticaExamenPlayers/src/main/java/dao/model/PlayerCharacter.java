package dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCharacter {
    private int characterId;
    private String name;
    private String type;
    private int level;
    private int playerId;
}
