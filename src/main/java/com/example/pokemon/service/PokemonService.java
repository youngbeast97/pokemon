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
import com.example.pokemon.pokeapi.PokemonApiRequest;
import com.example.pokemon.pokeapi.PokemonApiResponse;
import com.example.pokemon.repository.PlayerRepository;
import com.example.pokemon.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PlayerRepository playerRepository;
    private final PokemonRepository pokemonRepository;
    private final PokemonApiRequest pokemonApiRequest;
    private final PokemonMapper pokemonMapper;

    @Value("${pokemon.max-per-player:10}")
    private int maxPokemonsPerPlayer;

    @Transactional
    public void addPokemonToPlayer(Long playerId, String name) {

        if (!StringUtils.hasText(name)) {
            throw new InvalidPokemonNameException("Pokemon name must not be blank");
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new PlayerNotFoundWithProvidedIdException(
                                "Player with ID: " + playerId + " not found!"
                        ));

        name = name.toLowerCase();

        if (pokemonRepository.existsByName(name)) {
            throw new PokemonAlreadyOwnedException("Pokemon is already owned by player(s)");
        }

        assertPlayerCanHaveMorePokemons(player);

        PokemonApiResponse response = pokemonApiRequest.getPokemon(name);

        Pokemon pokemon = pokemonMapper.fromApiResponse(response, player);

        player.getPokemons().add(pokemon);

        pokemonRepository.save(pokemon);
    }

    private void assertPlayerCanHaveMorePokemons(Player player) {
        if (player.getPokemons().size() >= maxPokemonsPerPlayer) {
            throw new TooManyPokemonsForOnePlayerException("List of pokemons is full");
        }
    }

    public List<PokemonResponse> getPlayerPokemons(Long playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new PlayerNotFoundWithProvidedIdException("Player not found"));

        return player.getPokemons()
                .stream()
                .map(pokemonMapper::toResponse)
                .toList();
    }
}