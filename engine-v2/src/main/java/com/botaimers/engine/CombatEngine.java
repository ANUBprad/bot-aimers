package com.botaimers.engine;

/** Simple deterministic damage calculation, independent of the legacy game. */
public final class CombatEngine {
    public int attack(Bot attacker, Bot defender) {
        if (attacker == null || defender == null || attacker == defender) {
            throw new IllegalArgumentException("Distinct combatants are required");
        }
        if (!attacker.alive() || !defender.alive()) {
            throw new IllegalStateException("Both combatants must be alive");
        }
        int damage = (int) Math.max(1L, (long) attacker.attackPower() - defender.defense());
        int applied = Math.min(damage, defender.health());
        defender.takeDamage(applied);
        return applied;
    }
}
