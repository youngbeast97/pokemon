package com.example.pokemon.exception;

public class TooManyPokemonsForOnePlayerException extends RuntimeException {
    public TooManyPokemonsForOnePlayerException(String message) {
        super(message);
    }
}
