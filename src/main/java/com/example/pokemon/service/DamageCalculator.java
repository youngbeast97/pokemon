package com.example.pokemon.service;

import com.example.pokemon.model.pokemon.Pokemon;
import org.springframework.stereotype.Component;

/**
 * Wydzielona logika liczenia obrazen w walce, zeby {@link BattleService}
 * nie mieszal orkiestracji pojedynku z regulami wyliczania HP.
 */
@Component
public class DamageCalculator {

    public int calculateRemainingHp(Pokemon winnerPokemon, Pokemon loserPokemon) {
        int remainingHp = winnerPokemon.getHp() - loserPokemon.getHp();
        return Math.max(0, remainingHp);
    }
}
