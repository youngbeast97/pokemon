package com.example.pokemon.exception;

public class PlayerNotFoundWithProvidedIdException extends RuntimeException {
    public PlayerNotFoundWithProvidedIdException(String message) {
        super(message);
    }
}
