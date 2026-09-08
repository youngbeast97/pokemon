package com.example.pokemon.service;

import com.example.pokemon.exception.*;
import com.example.pokemon.model.battle.BattleFinalResult;
import com.example.pokemon.model.battle.BattleResult;
import com.example.pokemon.model.player.Player;
import com.example.pokemon.model.pokemon.Pokemon;
import com.example.pokemon.repository.PlayerRepository;
import com.example.pokemon.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final PlayerRepository playerRepository;
    private final PokemonRepository pokemonRepository;

    @Transactional
    public BattleResult fight(Long attackerId, Long defenderId,
                              Long attackerPokemonId, Long defenderPokemonId) {

        Player attacker = getPlayer(attackerId);
        Player defender = getPlayer(defenderId);

        validateNotSamePlayer(attackerId, defenderId);

        Pokemon attackerPokemon = getPokemon(attackerPokemonId);
        Pokemon defenderPokemon = getPokemon(defenderPokemonId);

        validateOwnership(attackerId, defenderId, attackerPokemon, defenderPokemon);

        BattleFinalResult result = resolveBattle(attacker, defender,
                attackerPokemon, defenderPokemon);

        boolean attackerWon = result.getWinner().getId().equals(attackerId);
        Pokemon winnerPokemon = attackerWon ? attackerPokemon : defenderPokemon;
        Pokemon loserPokemon  = attackerWon ? defenderPokemon : attackerPokemon;

        applyHpChanges(winnerPokemon, loserPokemon);

        updateStats(result, attackerPokemon, defenderPokemon);

        transferPokemonOwnership(result, loserPokemon);

        String log = buildLog(result, winnerPokemon, loserPokemon);

        return new BattleResult(
                result.getWinner().getId(),
                result.getLoser().getId(),
                log,
                attackerPokemon.getHp(),
                defenderPokemon.getHp()
        );
    }


    private BattleFinalResult resolveBattle(Player attacker, Player defender,
                                            Pokemon attackerPokemon, Pokemon defenderPokemon) {

        if (attackerPokemon.getHp() >= defenderPokemon.getHp()) {
            return new BattleFinalResult(attacker, defender);
        } else {
            return new BattleFinalResult(defender, attacker);
        }
    }

    public void applyHpChanges(Pokemon winnerPokemon, Pokemon loserPokemon) {
        int newWinnerHp = winnerPokemon.getHp() - loserPokemon.getHp();
        winnerPokemon.setHp(Math.max(0, newWinnerHp));
        loserPokemon.setHp(0);
    }

    private void updateStats(BattleFinalResult result,
                             Pokemon attackerPokemon, Pokemon defenderPokemon) {

        Player winner = result.getWinner();
        Player loser  = result.getLoser();

        winner.setWinStreak(winner.getWinStreak() + 1);
        loser.setWinStreak(0);

        winner.setBattleCounter(winner.getBattleCounter() + 1);
        loser.setBattleCounter(loser.getBattleCounter() + 1);

        attackerPokemon.setBattleCounter(attackerPokemon.getBattleCounter() + 1);
        defenderPokemon.setBattleCounter(defenderPokemon.getBattleCounter() + 1);
    }

    private void transferPokemonOwnership(BattleFinalResult result, Pokemon loserPokemon) {
        loserPokemon.setOwner(result.getWinner());
    }

    private String buildLog(BattleFinalResult result,
                            Pokemon winnerPokemon, Pokemon loserPokemon) {

        return String.format(
                "Winner is Player:  %d (%s, HP after battle battle: %d) vs Player %d (%s, HP after battle: %d)", //tu se musialem pomoc zeby to jako tako wygladalo
                result.getWinner().getId(), winnerPokemon.getName(), winnerPokemon.getHp(),
                result.getLoser().getId(), loserPokemon.getName(), loserPokemon.getHp()
        );
    }

    private void validateNotSamePlayer(Long attackerId, Long defenderId) {
        if (attackerId.equals(defenderId)) {
            throw new PlayerTryToFightHimselfException("Player cannot fight against himself");
        }
    }

    private void validateOwnership(Long attackerId, Long defenderId,
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

    private Player getPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() ->
                        new PlayerNotFoundWithProvidedIdException("Player not found with ID:  " + id));
    }

    private Pokemon getPokemon(Long id) {
        return pokemonRepository.findById(id)
                .orElseThrow(() ->
                        new PokemonNotFoundWithProvidedIdException("Pokemon not found with ID:  " + id));
    }
}