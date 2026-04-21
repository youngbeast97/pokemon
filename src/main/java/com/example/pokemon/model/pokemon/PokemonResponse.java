package com.example.pokemon.model.pokemon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PokemonResponse {
    private String name;
    private int hp;
    private List<PokemonType> types;

}
