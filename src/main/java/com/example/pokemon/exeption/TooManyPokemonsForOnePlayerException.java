package com.example.pokemon.exeption;

public class TooManyPokemonsForOnePlayerException extends RuntimeException {
    public TooManyPokemonsForOnePlayerException(String message) {
        super(message);
    }
}
