package com.example.pokemon.model.pokemon;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.pokemon.model.player.Player;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pokemons")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int hp;

    /** Bazowe HP – używane przy resecie schedulera */
    private int maxHp;

    private int battleCounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player owner;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<PokemonType> types = new ArrayList<>();
}
