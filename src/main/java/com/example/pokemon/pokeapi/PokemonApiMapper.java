package com.example.pokemon.pokeapi;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PokemonApiMapper {

    public PokemonApiResponse fromApiResponse(Map<String, Object> json) {
        String name = (String) json.get("name");

        //sie wjebalem z tym bo sie okazalo ze Pokemon ma statystyk jak odrzutowiec F16 i trzeba to jakos uprosic


        List<Map<String, Object>> stats =
                (List<Map<String, Object>>) json.get("stats");

        int hp = stats.stream()
                .filter(s -> {
                    Map<String, Object> stat = (Map<String, Object>) s.get("stat");
                    return "hp".equals(stat.get("name"));
                })
                .map(s -> (int) s.get("base_stat"))
                .findFirst()
                .orElse(0);

        List<Map<String, Object>> types =
                (List<Map<String, Object>>) json.get("types");

        List<String> typeNames = types.stream()
                .map(t -> (Map<String, Object>) t.get("type"))
                .map(t -> (String) t.get("name"))
                .toList();

        return new PokemonApiResponse(name, hp, typeNames);
    }
}
