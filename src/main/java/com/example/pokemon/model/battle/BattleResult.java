package com.example.pokemon.model.battle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BattleResult {
    private Long winnerPlayerId;
    private Long loserPlayerId;
    private String log;
    private int attackerPokemonHp;
    private int defenderPokemonHp;
}