package com.example.pokemon.model.battle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BattleRequest {
    private Long attackerPlayerId;
    private Long defenderPlayerId;
    private Long attackerPokemonId;
    private Long defenderPokemonId;


}
