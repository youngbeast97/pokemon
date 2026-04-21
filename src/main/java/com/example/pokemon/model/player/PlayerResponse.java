package com.example.pokemon.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {
    private Long id;
    private String name;
    private int battleCounter;
    private int winStreak;
    private List<String> pokemons;

}
