package com.example.pokemon.controller;

import com.example.pokemon.exeption.PlayerNotFoundWithProvidedIdException;
import com.example.pokemon.model.player.Player;
import com.example.pokemon.model.player.PlayerMapper;
import com.example.pokemon.model.player.PlayerRequest;
import com.example.pokemon.model.player.PlayerResponse;
import com.example.pokemon.model.pokemon.PokemonResponse;
import com.example.pokemon.pokeapi.PokemonApiRequest;
import com.example.pokemon.repository.PlayerRepository;
import com.example.pokemon.service.PokemonService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")

public class PlayerController {

    private final PlayerRepository playerRepository;
    private final PokemonApiRequest pokemonApiRequest;
    private final PokemonService pokemonService;

    public PlayerController(PlayerRepository playerRepository, PokemonApiRequest pokemonApiRequest, PokemonService pokemonService) {
        this.playerRepository = playerRepository;
        this.pokemonApiRequest = pokemonApiRequest;
        this.pokemonService = pokemonService;
    }

    @PostMapping
    public PlayerResponse createPlayer(@Valid @RequestBody PlayerRequest request) {

        Player player = PlayerMapper.toEntity(request);
        Player saved = playerRepository.save(player);

        return PlayerMapper.toResponse(saved);
    }

    @GetMapping("/{id}")
    public PlayerResponse get(@PathVariable Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundWithProvidedIdException("Player not found with ID: " + id));

        return PlayerMapper.toResponse(player);
    }

    @GetMapping("/test-pokemon")
    public Object test() {
        return pokemonApiRequest.getPokemon("pikachu");
    }

    @GetMapping
    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    @PostMapping("/{id}/pokemon/{name}")
    public void addPokemon(@PathVariable Long id, @PathVariable String name) {
        pokemonService.addPokemonToPlayer(id, name);
    }

    @GetMapping("/{id}/pokemons")
    public List<PokemonResponse> getPokemons(@PathVariable Long id) {
        return pokemonService.getPlayerPokemons(id);
    }
}