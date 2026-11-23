package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerDTO {
    private int playerId;
    private String name;
    private String email;
    private int tokens;
    private List<CharacterDTO> characters;

}
