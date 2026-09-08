package com.example.pokemon.exception;

public class DefenderDoesntHaveThatPokemonException extends RuntimeException {
    public DefenderDoesntHaveThatPokemonException(String message) {
        super(message);
    }
}
