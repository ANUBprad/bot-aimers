package com.botaimers.engine;

import java.util.Objects;

/** Runs a bounded, deterministic duel between two strategy-driven bots. */
public final class TurnManager {
    private final CombatEngine combat = new CombatEngine();

    public BattleResult fight(Bot first, Bot second, BotStrategy firstStrategy,
                              BotStrategy secondStrategy, int maxRounds) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        Objects.requireNonNull(firstStrategy, "firstStrategy");
        Objects.requireNonNull(secondStrategy, "secondStrategy");
        if (first == second || maxRounds < 1 || !first.alive() || !second.alive()) {
            throw new IllegalArgumentException("Expected two living, distinct bots and positive rounds");
        }

        int rounds = 0;
        while (rounds < maxRounds && first.alive() && second.alive()) {
            rounds++;
            BotStrategy.Action firstAction = Objects.requireNonNull(
                    firstStrategy.choose(first, second), "first action");
            BotStrategy.Action secondAction = Objects.requireNonNull(
                    secondStrategy.choose(second, first), "second action");

            if (firstAction == BotStrategy.Action.ATTACK) {
                strike(first, second, secondAction == BotStrategy.Action.GUARD);
            }
            if (second.alive() && secondAction == BotStrategy.Action.ATTACK) {
                strike(second, first, firstAction == BotStrategy.Action.GUARD);
            }
        }

        boolean draw = first.alive() && second.alive();
        String winner = draw ? null : (first.alive() ? first.name() : second.name());
        return new BattleResult(winner, rounds, first.health(), second.health(), draw);
    }

    private void strike(Bot attacker, Bot defender, boolean guarded) {
        int normalDamage = Math.max(1, attacker.attackPower() - defender.defense());
        if (!guarded) {
            combat.attack(attacker, defender);
        } else {
            // Guard reduces incoming damage by half, rounded up; minimum one.
            int reduced = Math.max(1, (normalDamage + 1) / 2);
            defender.takeDamage(reduced);
        }
    }
}
