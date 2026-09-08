package com.example.pokemon.model.pokemon;

import com.example.pokemon.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Co godzinę przywraca pokemonom część utraconego HP (regen-percent z maxHp),
 * zamiast pełnego resetu do wartości bazowej. Dzięki temu pokemon, który
 * wygrał walkę z niskim HP, stopniowo odzyskuje formę zamiast być od razu
 * w 100% gotowy do kolejnej walki.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HpResetScheduler {

    private final PokemonRepository pokemonRepository;

    @Value("${pokemon.hp-reset.regen-percent:80}")
    private int regenPercent;

    /**
     * Uruchamia się co godzinę (np. 00:00, 01:00, 02:00 …).
     * Cron: sekundy minuty godziny dzień miesiąc dzień-tygodnia
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void resetAllPokemonsHp() {
        List<Pokemon> pokemons = pokemonRepository.findAll();

        int count = 0;
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getHp() < pokemon.getMaxHp()) {
                pokemon.setHp(regenerateHp(pokemon));
                count++;
            }
        }

        pokemonRepository.saveAll(pokemons);
        log.info("[HpResetScheduler] Zregenerowano HP dla {} pokemonów ({}% maxHp).", count, regenPercent);
    }

    private int regenerateHp(Pokemon pokemon) {
        int missingHp = pokemon.getMaxHp() - pokemon.getHp();
        int regainedHp = missingHp * regenPercent / 100;
        return Math.min(pokemon.getMaxHp(), pokemon.getHp() + regainedHp);
    }
}
