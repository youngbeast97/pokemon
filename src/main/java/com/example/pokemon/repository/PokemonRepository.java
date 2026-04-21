package com.example.pokemon.repository;

import com.example.pokemon.model.pokemon.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon,Long> {

    boolean existsByName(String name);
    Optional<Pokemon>findByName(String name);
}
