package com.example.pokemon.controller;

import com.example.pokemon.pokeapi.PokemonApiRequest;
import com.example.pokemon.pokeapi.PokemonApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon-api")
public class PokemonApiController {

    private final PokemonApiRequest pokemonApiRequest;

    public PokemonApiController( PokemonApiRequest pokemonApiRequest) {
        this.pokemonApiRequest = pokemonApiRequest;
    }

    @GetMapping("/{name}")
    public PokemonApiResponse getPokemon(@PathVariable String name) {
        return pokemonApiRequest.getPokemon(name);
    }
}