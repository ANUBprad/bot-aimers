package com.botaimers.engine;

/** Guards against a stronger opponent when health is low. */
public final class BalancedStrategy implements BotStrategy {
    @Override public Action choose(Bot self, Bot opponent) {
        return self.health() <= 25 && opponent.attackPower() > self.defense()
                ? Action.GUARD : Action.ATTACK;
    }
}
