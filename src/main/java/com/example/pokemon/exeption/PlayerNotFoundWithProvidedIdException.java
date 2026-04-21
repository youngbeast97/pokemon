package com.example.pokemon.exeption;

public class PlayerNotFoundWithProvidedIdException extends RuntimeException {
    public PlayerNotFoundWithProvidedIdException(String message) {
        super(message);
    }
}
