package com.example.pokemon.exeption;

public class InvalidPokemonNameException extends RuntimeException {
    public InvalidPokemonNameException(String message) {
        super(message);
    }
}
