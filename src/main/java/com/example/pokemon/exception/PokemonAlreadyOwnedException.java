package com.example.pokemon.exception;

public class PokemonAlreadyOwnedException extends RuntimeException {
    public PokemonAlreadyOwnedException(String message) {
        super(message);
    }
}
