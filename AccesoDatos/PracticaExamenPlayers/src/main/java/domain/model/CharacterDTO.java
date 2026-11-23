package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CharacterDTO {
    private int characterId;
    private String Name;
    private String type;
    private int level;
    private int playerId;
}
