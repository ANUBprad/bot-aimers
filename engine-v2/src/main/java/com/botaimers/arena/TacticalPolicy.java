package com.botaimers.arena;

import com.botaimers.engine.Bot;

/** Chooses a spatial action based on combat state and relative positioning. */
@FunctionalInterface
public interface TacticalPolicy {
    Action choose(Bot self, GridPoint position, Bot opponent, GridPoint opponentPosition);

    enum Action { ADVANCE, ATTACK, GUARD }
}
