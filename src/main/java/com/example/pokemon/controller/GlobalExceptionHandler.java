package com.example.pokemon.controller;

import com.example.pokemon.exception.AttackerDoesntHaveThatPokemonException;
import com.example.pokemon.exception.DefenderDoesntHaveThatPokemonException;
import com.example.pokemon.exception.PlayerNotFoundWithProvidedIdException;
import com.example.pokemon.exception.PlayerTryToFightHimselfException;
import com.example.pokemon.exception.PokemonAlreadyOwnedException;
import com.example.pokemon.exception.PokemonNotFoundWithProvidedIdException;
import com.example.pokemon.exception.TooManyPokemonsForOnePlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Zamienia wyjątki domenowe na spójne, czytelne odpowiedzi HTTP zamiast
 * domyślnego 500 Internal Server Error.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({PlayerNotFoundWithProvidedIdException.class, PokemonNotFoundWithProvidedIdException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({
            PlayerTryToFightHimselfException.class,
            AttackerDoesntHaveThatPokemonException.class,
            DefenderDoesntHaveThatPokemonException.class,
            PokemonAlreadyOwnedException.class,
            TooManyPokemonsForOnePlayerException.class
    })
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", message));
    }
}
