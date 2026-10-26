package com.botaimers.engine;

/** Always attacks when given a turn. */
public final class AggressiveStrategy implements BotStrategy {
    @Override public Action choose(Bot self, Bot opponent) {
        return Action.ATTACK;
    }
}
