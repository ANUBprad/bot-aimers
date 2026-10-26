package com.botaimers.engine;

/** Strategy decides whether to attack or guard for a turn. */
public interface BotStrategy {
    Action choose(Bot self, Bot opponent);
    enum Action { ATTACK, GUARD }
}
