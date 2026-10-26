package com.botaimers.server;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/players")
public final class PlayerStatsController {
    private final PlayedMatchRepository repository;

    public PlayerStatsController(PlayedMatchRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{name}/stats")
    public PlayerStats stats(@PathVariable String name) {
        List<PlayedMatchEntity> games = repository.findAll().stream()
                .filter(m -> m.playerName.equalsIgnoreCase(name)).toList();
        long wins = games.stream().filter(m -> m.winner.equalsIgnoreCase(m.playerName)).count();
        long losses = games.size() - wins;
        double rate = games.isEmpty() ? 0 : (100.0 * wins / games.size());
        return new PlayerStats(name, games.size(), wins, losses, rate);
    }
}
