package com.example.pokemon.model.pokemon;

import com.example.pokemon.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Co godzinę przywraca wszystkim pokemonom HP do wartości bazowej (maxHp).
 * Dzięki temu pokemon który wygrał walkę z niskim HP może znowu walczyć pełną parą.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HpResetScheduler {

    private final PokemonRepository pokemonRepository;

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
                pokemon.setHp(pokemon.getMaxHp());
                count++;
            }
        }

        pokemonRepository.saveAll(pokemons);
        log.info("[HpResetScheduler] Zresetowano HP dla {} pokemonów.", count);
    }
}
