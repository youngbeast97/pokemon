package com.example.pokemon.model.battle;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleRequest {

    @NotNull(message = "attackerPlayerId is required")
    private Long attackerPlayerId;

    @NotNull(message = "defenderPlayerId is required")
    private Long defenderPlayerId;

    @NotNull(message = "attackerPokemonId is required")
    private Long attackerPokemonId;

    @NotNull(message = "defenderPokemonId is required")
    private Long defenderPokemonId;
}
