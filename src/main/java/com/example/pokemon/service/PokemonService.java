package com.example.pokemon.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {

    /*
    zmniejszanie HP o tyle o ile wygralem i zeby bylo >0 a schedule wraca 100% po jakims czasie
    TESTOWANIE 80% okolo


    i nastepny projekt znalezc API rejestru pedofili

     */
    private final PlayerRepository playerRepository;
    private final PokemonRepository pokemonRepository;
    private final PokemonApiRequest pokemonApiRequest;
    private final PokemonMapper pokemonMapper;

    @Transactional
    public void addPokemonToPlayer(Long playerId, String name) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new PlayerNotFoundWithProvidedIdException(
                                "Player with ID: " + playerId + " not found!"
                        ));

        name = name.toLowerCase();

        if (pokemonRepository.existsByName(name)) {
            throw new PokemonAlreadyOwnedException("Pokemon is already owned by player(s)");
        }

        if (player.getPokemons().size() >= 10) {
            throw new TooManyPokemonsForOnePlayerException("List of pokemons is full");
        }

        PokemonApiResponse response = pokemonApiRequest.getPokemon(name);

        Pokemon pokemon = pokemonMapper.fromApiResponse(response, player);

        player.getPokemons().add(pokemon);

        pokemonRepository.save(pokemon);
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