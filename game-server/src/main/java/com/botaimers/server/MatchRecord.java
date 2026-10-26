package com.botaimers.server;

import com.botaimers.arena.TacticalBattleResult;
import java.time.Instant;
import java.util.UUID;

public record MatchRecord(UUID id, Instant createdAt, String firstName,
                          String secondName, TacticalBattleResult result) {}
