package com.example.pokemon.exception;

public class InvalidPokemonNameException extends RuntimeException {
    public InvalidPokemonNameException(String message) {
        super(message);
    }
}
