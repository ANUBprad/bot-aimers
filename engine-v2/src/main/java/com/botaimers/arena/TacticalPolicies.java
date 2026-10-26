package com.botaimers.arena;

/** Deterministic tactical policies suitable for repeatable simulations. */
public final class TacticalPolicies {
    private TacticalPolicies() {}

    public static TacticalPolicy aggressive() {
        return (self, position, opponent, target) ->
                adjacent(position, target) ? TacticalPolicy.Action.ATTACK : TacticalPolicy.Action.ADVANCE;
    }

    public static TacticalPolicy cautious(int guardAtOrBelowHealth) {
        if (guardAtOrBelowHealth < 1) throw new IllegalArgumentException("Health threshold must be positive");
        return (self, position, opponent, target) -> {
            if (!adjacent(position, target)) return TacticalPolicy.Action.ADVANCE;
            return self.health() <= guardAtOrBelowHealth
                    ? TacticalPolicy.Action.GUARD : TacticalPolicy.Action.ATTACK;
        };
    }

    private static boolean adjacent(GridPoint a, GridPoint b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y()) == 1;
    }
}
