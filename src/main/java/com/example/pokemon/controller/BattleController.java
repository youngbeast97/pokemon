package com.example.pokemon.controller;

import com.example.pokemon.model.battle.BattleRequest;
import com.example.pokemon.model.battle.BattleResult;
import com.example.pokemon.service.BattleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/battle")
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping
    public BattleResult fight(@Valid @RequestBody BattleRequest request) {
        return battleService.fight(request.getAttackerPlayerId(), request.getDefenderPlayerId(), request.getAttackerPokemonId(), request.getDefenderPokemonId());
    }
}
