package com.example.pokemon.exeption;

public class PokemonAlreadyOwnedException extends RuntimeException {
    public PokemonAlreadyOwnedException(String message) {
        super(message);
    }
}
