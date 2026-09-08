package com.example.pokemon.model.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerRequest {

    @NotBlank(message = "Player name must not be blank")
    private String name;

}
