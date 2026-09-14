# MERGE LAB — Git + PR + Merge Conflicts + IntelliJ 3-way Merge

Laboratorium ćwiczące rozwiązywanie realnych konfliktów mergea na prawdziwym
kodzie projektu `pokemon`. Pracujemy bezpośrednio na `master` — bez osobnego
brancha roboczego. Każdy ukończony TASK trwale zmienia lokalny `master`.

## Zasady

1. **Jeden TASK na raz.** Kolejny TASK jest projektowany dopiero po
   zmergowaniu i zweryfikowaniu poprzedniego — na podstawie faktycznego stanu
   `master` po Twoim rozwiązaniu, nie z góry.
2. **Model konfliktu:** każdy TASK to `COMMON BASE` → (`MASTER CHANGE` na
   `master`) + (`TASK CHANGE` na `training/task-XX`), obie konkurencyjne
   zmiany dotyczą tego samego obszaru kodu, więc `git merge training/task-XX`
   na `master` musi dać prawdziwy `CONFLICT (content)`.
3. **Punkt bezpieczeństwa:** tag `merge-lab-start` wskazuje commit master
   sprzed rozpoczęcia laboratorium (`84ae52f`). Nigdy nie jest nadpisywany
   ani używany do resetu — to wyłącznie punkt odniesienia.
4. **IntelliJ 3-way Merge:** po `git merge training/task-XX` na `master`
   otwierasz *Resolve Conflicts*. Jedna strona to `master` (Twoje HEAD),
   druga to `training/task-XX`, środkowy panel `Result` to wersja finalna,
   którą budujesz ręcznie. Dokładne rozmieszczenie LEFT/RIGHT zależy od
   wersji IntelliJ — nie zakładaj sztywno strony.
5. **Weryfikacja ≠ samo `BUILD SUCCESS`.** Każdy TASK ma dedykowany
   `TaskXXVerificationTest` w `src/test/java/com/example/pokemon/mergelab/`,
   który celowo wymaga funkcjonalności **obu** stron konfliktu jednocześnie
   (A=FAIL, B=FAIL, A+B=PASS). Test ten dodawany jest wyłącznie na branchu
   `training/task-XX` (nie na `master`), żeby `master` zawsze startował do
   ćwiczenia w stanie zielonym/buildowalnym — po mergu ląduje w wyniku jako
   czyste dodanie pliku (bez konfliktu), a jego treść ocenia jakość Twojego
   rozwiązania konfliktów w plikach produkcyjnych.
6. **Regresja.** Każdy kolejny TASK musi zachować zachowanie wymagane przez
   poprzednie taski (ich `TaskXXVerificationTest` muszą nadal przechodzić).

## Plan (9 tasków, budowane pojedynczo)

**GROUP 1 — FUNDAMENTALS**
- TASK 01 🟢 — DTO validation / exception consistency / response contract (3 konflikty, 4 pliki)
- TASK 02 🟡 — do zaprojektowania po TASK 01
- TASK 03 🟡 — konflikt wielowarstwowy (Controller → DTO → Service)

**GROUP 2 — REFACTOR VS FEATURE**
- TASK 04 🟡/🟠 — feature vs refactor tego samego kodu
- TASK 05 🟠 — konflikt architektoniczny (wydzielenie Service)
- TASK 06 🟠 — konflikt semantyczny (compile PASS, verification FAIL)

**GROUP 3 — ADVANCED**
- TASK 07 🟠 — min. 3 pliki, 2 warstwy, 4+ obszary konfliktowe
- TASK 08 🔴 — obie strony sensowne osobno, błędne razem
- TASK 09 🔴 FINAL BOSS — LEFT+RIGHT+MANUAL EDIT w kilku miejscach

---

## TASK 01

### Level
🟢 EASY/MEDIUM

### Requires
Nic (pierwszy task, baza: `master` po fixie kompilacji, commit `bc7d962`).

### Context
`PlayerRequest` (`namePlayer`, `age: String`) nie miał żadnej walidacji.
`PlayerController.get()` rzucał surowy `new RuntimeException("Player not
found")` zamiast spójnego wyjątku domenowego używanego w reszcie warstwy
serwisów (`PlayerNotFoundWithProvidedIdException`). `PlayerMapper.toResponse`
zwracał tylko podstawowe pola gracza, bez żadnych wartości wyliczanych.

### Master change
Na `master` (commit `2833c0f`) wprowadzono trzy niezależne usprawnienia
skupione wokół `Player*`:
- walidację jednego z pól `PlayerRequest`,
- ujednolicenie obsługi błędu „gracz nie istnieje” w `PlayerController.get()`,
- nowe, wyliczane pole w odpowiedzi `PlayerResponse`, ustawiane w
  `PlayerMapper`.

### Developer change
Na `training/task-01` (commit `79277b5`) wprowadzono **inne, równoległe**
usprawnienia w dokładnie tych samych miejscach:
- walidację **drugiego** pola `PlayerRequest`,
- **inny** guard + inny komunikat w tym samym `orElseThrow(...)` w
  `PlayerController.get()`,
- **inne** wyliczane pole w `PlayerResponse` / `PlayerMapper`, wstawiane w
  tym samym miejscu co zmiana na masterze.

### Pull Request
```
BASE:    master
COMPARE: training/task-01
```

### Acceptance Criteria
- [ ] `PlayerRequest` waliduje **oba** pola (z mastera i z task brancha) —
      obie reguły muszą przetrwać merge.
- [ ] `@Valid` faktycznie aktywuje walidację w `PlayerController.createPlayer`.
- [ ] `PlayerController.get()` w dalszym ciągu chroni się przed nieprawidłowym
      `id` **oraz** rzuca spójny, domenowy wyjątek gdy gracz nie istnieje —
      obie zmiany muszą współistnieć w jednej metodzie.
- [ ] `PlayerResponse`/`PlayerMapper` zawiera **oba** nowe pola wyliczane,
      każde poprawnie wyliczone z danych gracza.
- [ ] Wszystkie dotychczasowe testy (`BattleServiceTest`,
      `PokemonServiceTest`) nadal przechodzą bez zmian.
- [ ] `Task01VerificationTest` (dodany przez `training/task-01`, ląduje w
      mergu automatycznie) przechodzi w całości.

### Expected conflicts
3 konfliktowe obszary w 4 plikach:
1. `src/main/java/com/example/pokemon/model/player/PlayerRequest.java`
   (import + adnotacje na polach — 2 hunki).
2. `src/main/java/com/example/pokemon/controller/PlayerController.java`
   (linia `orElseThrow(...)` w `get()`).
3. `src/main/java/com/example/pokemon/model/player/PlayerMapper.java` oraz
   `src/main/java/com/example/pokemon/model/player/PlayerResponse.java`
   (wstawienie nowego pola w tym samym miejscu po obu stronach).

### Verification
```
mvn -o test -Dtest=Task01VerificationTest
```

### Manual checklist
- [ ] Czy `PlayerRequest` waliduje obie reguły (z mastera i z task brancha)?
- [ ] Czy `@Valid` jest obecne w kontrolerze, więc walidacja realnie działa?
- [ ] Czy w `PlayerController.get()` zachowałeś zarówno guard na złe `id`,
      jak i spójny wyjątek domenowy — a nie tylko jedno z nich?
- [ ] Czy `PlayerResponse` ma oba nowe pola, a nie tylko jedno?
- [ ] Czy nie zostawiłeś martwego kodu (np. nieużywanego importu wyjątku)?
- [ ] Czy `mvn -o test` (cały pakiet) przechodzi bez błędów?

### Hints
- To nie jest konflikt "wybierz jedną stronę" — obie strony wnoszą coś
  wartościowego i wynik musi zawierać **sumę**, nie wybór.
- Zwróć uwagę, że w `PlayerController.get()` konflikt dotyczy tylko jednej
  linii (`orElseThrow`) — resztę (guard clause) IntelliJ powinien wmergować
  automatycznie bez pytania.
- `Task01VerificationTest` nie skompiluje się/nie przejdzie na żadnym z
  branchy osobno — to zamierzone. Dopiero poprawny `Result` sprawia, że
  test przechodzi.

### Score
- 1 pkt — kompilacja (`mvn -o compile`)
- 2 pkt — istniejące testy (`BattleServiceTest`, `PokemonServiceTest`)
- 3 pkt — `Task01VerificationTest` (wszystkie 8 metod)
- 2 pkt — brak regresji (cały `mvn -o test` zielony)
- 2 pkt — manual checklist (spójność, brak martwego kodu, brak duplikacji)
