# MERGE LAB — Progress

Rzeczywisty postęp laboratorium. Przed przygotowaniem kolejnego TASK zawsze
weryfikuj ten plik względem faktycznego `git log` / `git status` / wyników
testów — wpis "COMPLETED" nie jest ślepo ufany, tylko potwierdzany na nowo.

## Safety tag
```
merge-lab-start -> 84ae52f651169476c612c8cd61944fed567d8581
(Merge pull request #3 from youngbeast97/refactor/battle-engine-and-config)
```

## Pre-lab fix (nie jest taskiem)
`master` w punkcie `merge-lab-start` nie kompilował się (literówka
`attackerWon` zamiast `attackerWonGame` w `BattleService.fight`, pozostałość
po mergu PR #3). Naprawione w commicie `bc7d962` — to jest prawdziwy punkt
startowy (`COMMON BASE`) dla TASK 01.

## TASK 01 — READY
- Base master commit (common base): `bc7d962` (fix kompilacji)
- Master change commit: `2833c0f` (`feat(master-change): waliduj namePlayer, ujednolic wyjatek 404 gracza i dodaj pokemonCount`)
- Task branch: `training/task-01`
  - `79277b5` — `feat(task-01): waliduj age, dodaj guard na nieprawidlowe id i pole active`
  - `64d76ab` — `test(mergelab): dodaj Task01VerificationTest`
- Trial merge: potwierdzony ręcznie (`git merge training/task-01 --no-commit --no-ff`
  → 4x `CONFLICT (content)` w `PlayerController.java`, `PlayerMapper.java`,
  `PlayerRequest.java`, `PlayerResponse.java`), następnie `git merge --abort`.
  Repo pozostawione czyste na `master` @ `2833c0f`.
- Baseline testy na `master` (`2833c0f`): `mvn -o test` → 30/30 PASS.
- Baseline testy na `training/task-01` (`64d76ab`): kompilacja test-classes
  **celowo** się nie udaje (`Task01VerificationTest` wymaga `pokemonCount`,
  które istnieje tylko na masterze) — zgodne z projektem A=FAIL/B=FAIL/A+B=PASS.
- Remote: **NIE wypchnięte**. `origin/master` nadal wskazuje `84ae52f` (bez
  fixu kompilacji i bez master change). `training/task-01` istnieje tylko
  lokalnie. Zobacz sekcję "Stan na GitHub" niżej — wymaga Twojej decyzji.
- Score: —  (task jeszcze nierozwiązany)

### Stan na GitHub potrzebny do sensownego PR
Żeby PR `base: master compare: training/task-01` pokazywał dokładnie
zamierzoną sytuację (tylko zmiany TASK 01, bez szumu), na `origin` musi
znaleźć się:
1. `origin/master` zaktualizowany do lokalnego `master` (`2833c0f`) — czyli
   commit z fixem kompilacji (`bc7d962`) i commit "master change" (`2833c0f`).
2. Dopiero wtedy `training/task-01` wypchnięty do `origin/training/task-01`.

Bez kroku 1 GitHub policzyłby merge-base PR-a względem starego
`origin/master` (`84ae52f`) i pokazałby w diffie również fix kompilacji oraz
"master change" jako część "compare" — myląco. **Nie wykonałem tego push
automatycznie** (zmiana współdzielonego `master` na GitHub) — to wymaga
Twojej wyraźnej zgody.

## TASK 02 — NOT STARTED
Do zaprojektowania dopiero po zmergowaniu i zweryfikowaniu TASK 01.
