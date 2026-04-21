package com.example.pokemon.pokeapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PokemonApiResponse {
    private String name;
    private int hp;
    private List<String> types;
}
