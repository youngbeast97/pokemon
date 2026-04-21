package com.example.pokemon.model.pokemon;

import com.example.pokemon.model.player.Player;
import com.example.pokemon.pokeapi.PokemonApiResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonMapper {

    public Pokemon fromApiResponse(PokemonApiResponse response, Player owner) {

        Pokemon pokemon = new Pokemon();
        pokemon.setName(response.getName());
        pokemon.setHp(response.getHp());
        pokemon.setMaxHp(response.getHp());   // <-- zapisujemy bazowe HP
        pokemon.setBattleCounter(0);
        pokemon.setOwner(owner);

        List<PokemonType> types = response.getTypes()
                .stream()
                .map(t -> PokemonType.valueOf(t.toUpperCase()))
                .toList();

        pokemon.setTypes(types);

        return pokemon;
    }

    public PokemonResponse toResponse(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.getName(),
                pokemon.getHp(),
                pokemon.getTypes()
        );
    }
}
