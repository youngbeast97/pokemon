package com.example.pokemon.model.player;


import com.example.pokemon.model.pokemon.Pokemon;

public final class PlayerMapper {

    public static Player toEntity(PlayerRequest request) {
        Player player = new Player();
        player.setName(request.getNamePlayer());
        player.setBattleCounter(0);
        player.setWinStreak(0);
        return player;
    }

    public static PlayerResponse toResponse(Player player) {
        PlayerResponse response = new PlayerResponse();
        response.setId(player.getId());
        response.setName(player.getName());
        response.setBattleCounter(player.getBattleCounter());
        response.setWinStreak(player.getWinStreak());
        response.setPokemonCount(player.getPokemons().size());

        response.setPokemons(player.getPokemons()
                .stream()
                .map(Pokemon::getName)
                .toList());
        return response;

    }

}
