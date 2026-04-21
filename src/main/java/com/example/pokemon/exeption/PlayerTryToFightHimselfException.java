package com.example.pokemon.exeption;

public class PlayerTryToFightHimselfException extends RuntimeException {
    public PlayerTryToFightHimselfException(String message) {
        super(message);
    }
}
