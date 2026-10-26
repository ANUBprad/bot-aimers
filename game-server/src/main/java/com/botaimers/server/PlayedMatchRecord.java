package com.botaimers.server;

import java.time.Instant;
import java.util.UUID;

/** Local match history, stored separately from simulated AI matches. */
public record PlayedMatchRecord(UUID id, Instant createdAt, String playerName,
        String opponentName, String winner, int turns, int playerHealth,
        int opponentHealth, boolean verified) {}
