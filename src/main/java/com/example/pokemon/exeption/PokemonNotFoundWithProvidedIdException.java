package com.example.pokemon.exeption;

public class PokemonNotFoundWithProvidedIdException extends RuntimeException {
    public PokemonNotFoundWithProvidedIdException(String message) {
        super(message);
    }
}
