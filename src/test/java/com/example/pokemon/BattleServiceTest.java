package com.example.pokemon;
import com.example.pokemon.exception.*;
import com.example.pokemon.model.battle.BattleResult;
import com.example.pokemon.model.player.Player;
import com.example.pokemon.model.pokemon.Pokemon;
import com.example.pokemon.model.pokemon.PokemonType;
import com.example.pokemon.repository.PlayerRepository;
import com.example.pokemon.repository.PokemonRepository;
import com.example.pokemon.service.BattleService;
import com.example.pokemon.service.BattleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    // BattleValidator jest prostym, bezstanowym komponentem - uzywamy
    // prawdziwej instancji zamiast mockowac jego zachowanie.
    private BattleService battleService;


    private Player attacker;
    private Player defender;
    private Pokemon attackerPokemon;
    private Pokemon defenderPokemon;

    @BeforeEach
    void setUp() {
        battleService = new BattleService(playerRepository, pokemonRepository, new BattleValidator());

        attacker = createPlayer(1L, "Ash", 0, 0);
        defender = createPlayer(2L, "Gary", 0, 0);

        attackerPokemon = createPokemon(10L, "pikachu", 90, attacker);
        defenderPokemon = createPokemon(20L, "bulbasaur", 30, defender);
    }

    private void mockRepositories() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(defender));
        when(pokemonRepository.findById(10L)).thenReturn(Optional.of(attackerPokemon));
        when(pokemonRepository.findById(20L)).thenReturn(Optional.of(defenderPokemon));
    }

    private Player createPlayer(Long id, String name, int battles, int streak) {
        Player p = new Player();
        p.setId(id);
        p.setName(name);
        p.setBattleCounter(battles);
        p.setWinStreak(streak);
        p.setPokemons(new ArrayList<>());
        return p;
    }

    private Pokemon createPokemon(Long id, String name, int hp, Player owner) {
        Pokemon p = new Pokemon();
        p.setId(id);
        p.setName(name);
        p.setHp(hp);
        p.setMaxHp(hp);
        p.setBattleCounter(0);
        p.setOwner(owner);
        p.setTypes(List.of(PokemonType.ELECTRIC));
        return p;
    }


    @Test
    void shouldfightAttackerWinsWhenMoreHp() {
        mockRepositories();

        BattleResult result = battleService.fight(1L, 2L, 10L, 20L);

        assertThat(result.getWinnerPlayerId()).isEqualTo(1L);
        assertThat(result.getLoserPlayerId()).isEqualTo(2L);
    }

    @Test
    void shouldFightDefenderWinsWhenMoreHp() {
        attackerPokemon.setHp(30);
        defenderPokemon.setHp(90);
        mockRepositories();

        BattleResult result = battleService.fight(1L, 2L, 10L, 20L);

        assertThat(result.getWinnerPlayerId()).isEqualTo(2L);
        assertThat(result.getLoserPlayerId()).isEqualTo(1L);
    }

    @Test
    void shouldFightAttackerWinsOnEqualHP() {
        attackerPokemon.setHp(50);
        defenderPokemon.setHp(50);
        mockRepositories();

        BattleResult result = battleService.fight(1L, 2L, 10L, 20L);

        assertThat(result.getWinnerPlayerId()).isEqualTo(1L);
    }

    @Test
    void shouldFightWinnerHpDecreasedByLoserHp() {
        mockRepositories();
        BattleResult result = battleService.fight(1L, 2L, 10L, 20L);

        assertThat(result.getAttackerPokemonHp()).isEqualTo(60);
    }

    @Test
    void shouldFightLoserHpBecomesZero() {
        mockRepositories();

        BattleResult result = battleService.fight(1L, 2L, 10L, 20L);

        assertThat(result.getDefenderPokemonHp()).isEqualTo(0);
    }

    @Test
    void shouldFightWinnerHpDoesNotGoUnderZero() {
        attackerPokemon.setHp(50);
        defenderPokemon.setHp(50);
        mockRepositories();

        BattleResult result = battleService.fight(1L, 2L, 10L, 20L);
        assertThat(result.getAttackerPokemonHp()).isZero();
        assertThat(result.getAttackerPokemonHp()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldApplyHpChangesSetsCorrectValues() {
        Pokemon winner = createPokemon(1L, "charizard", 100, attacker);
        Pokemon loser  = createPokemon(2L, "metapod",   40, defender);

        battleService.applyHpChanges(winner, loser);

        assertThat(winner.getHp()).isEqualTo(60);
        assertThat(loser.getHp()).isEqualTo(0);
    }

    @Test
    void shouldApplyHpChangesMoreThanZero() {
        Pokemon winner = createPokemon(1L, "pikachu", 50, attacker);
        Pokemon loser  = createPokemon(2L, "raichu",  50, defender);

        battleService.applyHpChanges(winner, loser);

        assertThat(winner.getHp()).isZero();
        assertThat(loser.getHp()).isZero();
    }


    @Test
    void shouldFightIncreaseBattleCounterForBothPlayers() {
        attacker.setBattleCounter(3);
        defender.setBattleCounter(5);
        mockRepositories();

        battleService.fight(1L, 2L, 10L, 20L);

        assertThat(attacker.getBattleCounter()).isEqualTo(4);
        assertThat(defender.getBattleCounter()).isEqualTo(6);
    }

    @Test
    void shouldUpdateWinStreak() {
        attacker.setWinStreak(2);
        defender.setWinStreak(4);
        mockRepositories();

        battleService.fight(1L, 2L, 10L, 20L);

        assertThat(attacker.getWinStreak()).isEqualTo(3);
        assertThat(defender.getWinStreak()).isEqualTo(0);
    }

    @Test
    void shouldFightIncreaseBattleCounterForBothPokemons() {
        mockRepositories();

        battleService.fight(1L, 2L, 10L, 20L);

        assertThat(attackerPokemon.getBattleCounter()).isEqualTo(1);
        assertThat(defenderPokemon.getBattleCounter()).isEqualTo(1);
    }


    @Test
    void shouldChangeOwnerOfLoserPokemon() {
        mockRepositories();

        battleService.fight(1L, 2L, 10L, 20L);
        assertThat(defenderPokemon.getOwner().getId()).isEqualTo(1L);
    }

    @Test
    void shouldChangeOwnerOfDefenderPokemon() {
        attackerPokemon.setHp(20);
        defenderPokemon.setHp(80);
        mockRepositories();

        battleService.fight(1L, 2L, 10L, 20L);
        assertThat(attackerPokemon.getOwner().getId()).isEqualTo(2L);
    }

    @Test
    void shouldFightForTheSamePlayer() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        assertThatThrownBy(() -> battleService.fight(1L, 1L, 10L, 20L))
                .isInstanceOf(PlayerTryToFightHimselfException.class);
    }

    @Test
    void shouldExceptionPlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> battleService.fight(1L, 2L, 10L, 20L))
                .isInstanceOf(PlayerNotFoundWithProvidedIdException.class);
    }

    @Test
    void shouldExceptionDefenderNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> battleService.fight(1L, 2L, 10L, 20L))
                .isInstanceOf(PlayerNotFoundWithProvidedIdException.class);
    }

    @Test
    void shouldExceptionPokemonNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(defender));
        when(pokemonRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> battleService.fight(1L, 2L, 10L, 20L))
                .isInstanceOf(PokemonNotFoundWithProvidedIdException.class);
    }

    @Test
    void shouldExceptionPokemonsDefenderNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(defender));
        when(pokemonRepository.findById(10L)).thenReturn(Optional.of(attackerPokemon));
        when(pokemonRepository.findById(20L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> battleService.fight(1L, 2L, 10L, 20L))
                .isInstanceOf(PokemonNotFoundWithProvidedIdException.class);
    }

    @Test
    void shouldAttackerDoesntHavePokemon() {
        attackerPokemon.setOwner(defender);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(defender));
        when(pokemonRepository.findById(10L)).thenReturn(Optional.of(attackerPokemon));
        when(pokemonRepository.findById(20L)).thenReturn(Optional.of(defenderPokemon));

        assertThatThrownBy(() -> battleService.fight(1L, 2L, 10L, 20L))
                .isInstanceOf(AttackerDoesntHaveThatPokemonException.class);
    }

    @Test
    void shouldExceptionDefenderDoesntHavePokemon() {
        defenderPokemon.setOwner(attacker);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(attacker));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(defender));
        when(pokemonRepository.findById(10L)).thenReturn(Optional.of(attackerPokemon));
        when(pokemonRepository.findById(20L)).thenReturn(Optional.of(defenderPokemon));

        assertThatThrownBy(() -> battleService.fight(1L, 2L, 10L, 20L))
                .isInstanceOf(DefenderDoesntHaveThatPokemonException.class);
    }




}
