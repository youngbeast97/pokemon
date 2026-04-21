package com.example.pokemon.pokeapi;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PokemonApiRequest {

    private final RestTemplate restTemplate;
    private final PokemonApiMapper pokemonApiMapper;

    public PokemonApiRequest(RestTemplate restTemplate, PokemonApiMapper pokemonApiMapper) {
        this.restTemplate = restTemplate;
        this.pokemonApiMapper = pokemonApiMapper;
    }
    public PokemonApiResponse getPokemon(String name) {

        String url = "https://pokeapi.co/api/v2/pokemon/" + name.toLowerCase();

        Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

        return pokemonApiMapper.fromApiResponse(response);
    }
}