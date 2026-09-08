package com.example.pokemon.service;

import com.example.pokemon.exception.AttackerDoesntHaveThatPokemonException;
import com.example.pokemon.exception.DefenderDoesntHaveThatPokemonException;
import com.example.pokemon.exception.PlayerTryToFightHimselfException;
import com.example.pokemon.model.pokemon.Pokemon;
import org.springframework.stereotype.Component;

/**
 * Wydzielona walidacja reguł biznesowych walki, żeby {@link BattleService}
 * mógł skupić się wyłącznie na przebiegu i wyniku pojedynku.
 */
@Component
public class BattleValidator {

    public void validateNotSamePlayer(Long attackerId, Long defenderId) {
        if (attackerId.equals(defenderId)) {
            throw new PlayerTryToFightHimselfException("Player cannot fight against himself");
        }
    }

    public void validateOwnership(Long attackerId, Long defenderId,
                                   Pokemon attackerPokemon, Pokemon defenderPokemon) {

        if (!attackerPokemon.getOwner().getId().equals(attackerId)) {
            throw new AttackerDoesntHaveThatPokemonException(
                    "Attacker doesn't have that pokemon ");
        }
        if (!defenderPokemon.getOwner().getId().equals(defenderId)) {
            throw new DefenderDoesntHaveThatPokemonException(
                    "Defender doesn't have that THE pokemon");
        }
    }
}
