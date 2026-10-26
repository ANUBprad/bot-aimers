package com.botaimers.server;

import com.botaimers.arena.*;
import com.botaimers.engine.Bot;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory match storage: restart clears history; persistence comes later. */
@Service
public final class MatchService {
    private final Map<UUID, MatchRecord> matches = new ConcurrentHashMap<>();

    public MatchRecord create(MatchRequest request) {
        String firstName = request.firstName().trim();
        String secondName = request.secondName().trim();
        if (firstName.equalsIgnoreCase(secondName)) {
            throw new IllegalArgumentException("Combatants must have different names");
        }
        GridMap map = new GridMap(9, 5, Set.of(
                new GridPoint(4, 0), new GridPoint(4, 1),
                new GridPoint(4, 3), new GridPoint(4, 4)));
        TacticalBattleResult result = new TacticalArena(map).fight(
                new Bot(firstName, 60, 12, 3), new GridPoint(1, 2), TacticalPolicies.aggressive(),
                new Bot(secondName, 60, 10, 4), new GridPoint(7, 2), TacticalPolicies.cautious(15),
                40);
        MatchRecord record = new MatchRecord(UUID.randomUUID(), Instant.now(), firstName, secondName, result);
        matches.put(record.id(), record);
        return record;
    }

    public Optional<MatchRecord> find(UUID id) {
        return Optional.ofNullable(matches.get(id));
    }

    public List<MatchRecord> all() {
        return matches.values().stream()
                .sorted(Comparator.comparing(MatchRecord::createdAt).reversed()
                        .thenComparing(MatchRecord::id))
                .toList();
    }
}
