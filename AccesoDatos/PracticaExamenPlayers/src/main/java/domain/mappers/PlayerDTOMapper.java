package domain.mappers;

import dao.model.Player;
import domain.model.PlayerDTO;

import java.util.List;

public class PlayerDTOMapper {

    public Player dtoToEntity(PlayerDTO playerDTO) {
        return new Player(
                playerDTO.getPlayerId(),
                playerDTO.getName(),
                playerDTO.getEmail(),
                playerDTO.getTokens()
        );
    }

    public PlayerDTO entityToDto(Player player) {
        return new PlayerDTO(
                player.getPlayerId(),
                player.getName(),
                player.getEmail(),
                player.getTokens(),
                List.of()
        );
    }



}
