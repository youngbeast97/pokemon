package com.example.pokemon.exeption;

public class AttackerDoesntHaveThatPokemonException extends RuntimeException {
    public AttackerDoesntHaveThatPokemonException(String message) {
        super(message);
    }
}
