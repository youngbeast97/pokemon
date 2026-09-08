package com.example.pokemon.exception;

public class PokemonNotFoundWithProvidedIdException extends RuntimeException {
    public PokemonNotFoundWithProvidedIdException(String message) {
        super(message);
    }
}
