package com.example.pokemon.exception;

public class AttackerDoesntHaveThatPokemonException extends RuntimeException {
    public AttackerDoesntHaveThatPokemonException(String message) {
        super(message);
    }
}
