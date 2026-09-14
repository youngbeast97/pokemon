package com.example.pokemon.model.player;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerRequest {

    @NotBlank(message = "namePlayer is required")
    private String namePlayer;

    private String age;
}
