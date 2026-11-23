package dao.model;

import domain.model.CharacterDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private int playerId;
    private String name;
    private String email;
    private int tokens;

}
