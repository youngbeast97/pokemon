package com.example.pokemon.exception;

public class PlayerTryToFightHimselfException extends RuntimeException {
    public PlayerTryToFightHimselfException(String message) {
        super(message);
    }
}
