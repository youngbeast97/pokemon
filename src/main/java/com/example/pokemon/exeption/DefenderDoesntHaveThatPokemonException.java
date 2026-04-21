package com.example.pokemon.exeption;

public class DefenderDoesntHaveThatPokemonException extends RuntimeException {
    public DefenderDoesntHaveThatPokemonException(String message) {
        super(message);
    }
}
