package com.botaimers.engine;

/** Mutable combatant with validated attributes. */
public final class Bot {
    private final String name;
    private final int attack;
    private final int defense;
    private int health;

    public Bot(String name, int health, int attack, int defense) {
        if (name == null || name.isBlank() || health <= 0 || attack < 0 || defense < 0) {
            throw new IllegalArgumentException("Invalid bot attributes");
        }
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public String name() { return name; }
    public int health() { return health; }
    public int attackPower() { return attack; }
    public int defense() { return defense; }
    public boolean alive() { return health > 0; }

    public void takeDamage(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Damage must be nonnegative");
        health = (int) Math.max(0L, (long) health - amount);
    }
}
