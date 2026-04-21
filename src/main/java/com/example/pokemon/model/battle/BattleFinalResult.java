package com.example.pokemon.model.battle;

import com.example.pokemon.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BattleFinalResult {
    private final Player winner;
    private final Player loser;


}