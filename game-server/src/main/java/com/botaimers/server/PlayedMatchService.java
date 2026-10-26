package com.botaimers.server;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class PlayedMatchService {
    private final Map<UUID, PlayedMatchRecord> matches = new ConcurrentHashMap<>();

    public PlayedMatchRecord save(PlayedMatchRequest request) {
        String player = request.playerName().trim();
        String opponent = request.opponentName().trim();
        String winner = request.winner().trim();
        if (player.equalsIgnoreCase(opponent)
                || !(winner.equals(player) || winner.equals(opponent))
                || (winner.equals(player) && (request.opponentHealth() != 0 || request.playerHealth() == 0))
                || (winner.equals(opponent) && (request.playerHealth() != 0 || request.opponentHealth() == 0))) {
            throw new IllegalArgumentException("Inconsistent local match result");
        }
        PlayedMatchRecord record = new PlayedMatchRecord(UUID.randomUUID(), Instant.now(),
                player, opponent, winner, request.turns(), request.playerHealth(),
                request.opponentHealth(), false);
        matches.put(record.id(), record);
        return record;
    }

    public List<PlayedMatchRecord> all() {
        return matches.values().stream().sorted(
                Comparator.comparing(PlayedMatchRecord::createdAt).reversed()
                        .thenComparing(PlayedMatchRecord::id)).toList();
    }
}
