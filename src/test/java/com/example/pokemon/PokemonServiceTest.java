package com.example.pokemon.service;

import com.example.pokemon.exeption.InvalidPokemonNameException;
import com.example.pokemon.exeption.PlayerNotFoundWithProvidedIdException;
import com.example.pokemon.exeption.PokemonAlreadyOwnedException;
import com.example.pokemon.exeption.TooManyPokemonsForOnePlayerException;
import com.example.pokemon.exception.PlayerNotFoundWithProvidedIdException;
import com.example.pokemon.exception.PokemonAlreadyOwnedException;
import com.example.pokemon.exception.TooManyPokemonsForOnePlayerException;
import com.example.pokemon.model.player.Player;
import com.example.pokemon.model.pokemon.Pokemon;
import com.example.pokemon.model.pokemon.PokemonMapper;
import com.example.pokemon.model.pokemon.PokemonResponse;
import com.example.pokemon.model.pokemon.PokemonType;
import com.example.pokemon.pokeapi.PokemonApiRequest;
import com.example.pokemon.pokeapi.PokemonApiResponse;
import com.example.pokemon.repository.PlayerRepository;
import com.example.pokemon.repository.PokemonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock private PlayerRepository playerRepository;
    @Mock private PokemonRepository pokemonRepository;
    @Mock private PokemonApiRequest pokemonApiRequest;
    @Mock private PokemonMapper pokemonMapper;

    @InjectMocks
    private PokemonService pokemonService;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setId(1L);
        player.setName("Ash");
        player.setBattleCounter(0);
        player.setWinStreak(0);
        player.setPokemons(new ArrayList<>());

        ReflectionTestUtils.setField(pokemonService, "maxPokemonsPerPlayer", 10);
    }

    @Test
    void shouldAddPokemonRejectBlankName() {
        assertThatThrownBy(() -> pokemonService.addPokemonToPlayer(1L, "   "))
                .isInstanceOf(InvalidPokemonNameException.class);
    }


    @Test
    void shouldAddPokemon() {
        PokemonApiResponse apiResponse = new PokemonApiResponse("pikachu", 35, List.of("electric"));
        Pokemon pikachu = buildPokemon(1L, "pikachu", 35, player);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(pokemonRepository.existsByName("pikachu")).thenReturn(false);
        when(pokemonApiRequest.getPokemon("pikachu")).thenReturn(apiResponse);
        when(pokemonMapper.fromApiResponse(apiResponse, player)).thenReturn(pikachu);

        pokemonService.addPokemonToPlayer(1L, "pikachu");

        verify(pokemonRepository).save(pikachu);
        assertThat(player.getPokemons()).contains(pikachu);
    }

    @Test
    void shouldAddPokemonLowerCase() {
        PokemonApiResponse apiResponse = new PokemonApiResponse("pikachu", 35, List.of("electric"));
        Pokemon pikachu = buildPokemon(1L, "pikachu", 35, player);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(pokemonRepository.existsByName("pikachu")).thenReturn(false);
        when(pokemonApiRequest.getPokemon("pikachu")).thenReturn(apiResponse);
        when(pokemonMapper.fromApiResponse(any(), any())).thenReturn(pikachu);

        pokemonService.addPokemonToPlayer(1L, "PIKACHU");

        verify(pokemonApiRequest).getPokemon("pikachu");
        verify(pokemonRepository).existsByName("pikachu");
    }


    @Test
    void shouldAddPokemonWhenPlayerNotFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pokemonService.addPokemonToPlayer(99L, "pikachu"))
                .isInstanceOf(PlayerNotFoundWithProvidedIdException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldAddPokemonWhenPokemonOwned() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(pokemonRepository.existsByName("pikachu")).thenReturn(true);

        assertThatThrownBy(() -> pokemonService.addPokemonToPlayer(1L, "pikachu"))
                .isInstanceOf(PokemonAlreadyOwnedException.class);
    }

    @Test
    void shouldAddPokemonWhenPlayerHasIt() {
        for (int i = 0; i < 10; i++) {
            player.getPokemons().add(buildPokemon((long) i, "pokemon" + i, 50, player));
        }

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(pokemonRepository.existsByName("mewtwo")).thenReturn(false);

        assertThatThrownBy(() -> pokemonService.addPokemonToPlayer(1L, "mewtwo"))
                .isInstanceOf(TooManyPokemonsForOnePlayerException.class);
    }

    @Test
    void shouldAddPokemonWhenListIs10() {
        for (int i = 0; i < 9; i++) {
            player.getPokemons().add(buildPokemon((long) i, "pokemon" + i, 50, player));
        }

        PokemonApiResponse apiResponse = new PokemonApiResponse("mew", 100, List.of("psychic"));
        Pokemon mew = buildPokemon(10L, "mew", 100, player);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(pokemonRepository.existsByName("mew")).thenReturn(false);
        when(pokemonApiRequest.getPokemon("mew")).thenReturn(apiResponse);
        when(pokemonMapper.fromApiResponse(apiResponse, player)).thenReturn(mew);

        pokemonService.addPokemonToPlayer(1L, "mew");

        assertThat(player.getPokemons()).hasSize(10);
        verify(pokemonRepository).save(mew);
    }

    @Test
    void shouldGetPlayerPokemonsAll() {
        Pokemon p1 = buildPokemon(1L, "pikachu", 35, player);
        Pokemon p2 = buildPokemon(2L, "charmander", 39, player);
        player.getPokemons().addAll(List.of(p1, p2));

        PokemonResponse r1 = new PokemonResponse("pikachu",    35, List.of(PokemonType.ELECTRIC));
        PokemonResponse r2 = new PokemonResponse("charmander", 39, List.of(PokemonType.FIRE));

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(pokemonMapper.toResponse(p1)).thenReturn(r1);
        when(pokemonMapper.toResponse(p2)).thenReturn(r2);

        List<PokemonResponse> result = pokemonService.getPlayerPokemons(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(PokemonResponse::getName)
                .containsExactlyInAnyOrder("pikachu", "charmander");
    }

    @Test
    void shouldGetPlayerPokemonsGiveEmptyListWhenNeeded() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        List<PokemonResponse> result = pokemonService.getPlayerPokemons(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldGetPlayerPokemonsWhenPlayerNotFound() {
        when(playerRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pokemonService.getPlayerPokemons(42L))
                .isInstanceOf(PlayerNotFoundWithProvidedIdException.class);
    }

    private Pokemon buildPokemon(Long id, String name, int hp, Player owner) {
        Pokemon p = new Pokemon();
        p.setId(id);
        p.setName(name);
        p.setHp(hp);
        p.setMaxHp(hp);
        p.setBattleCounter(0);
        p.setOwner(owner);
        p.setTypes(new ArrayList<>());
        return p;
    }
}
