package com.botaimers.server;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "played_matches")
public class PlayedMatchEntity {
    @Id public UUID id;
    @Column(nullable = false) public Instant createdAt;
    @Column(nullable = false, length = 40) public String playerName;
    @Column(nullable = false, length = 40) public String opponentName;
    @Column(nullable = false, length = 40) public String winner;
    @Column(nullable = false) public int turns;
    @Column(nullable = false) public int playerHealth;
    @Column(nullable = false) public int opponentHealth;
    @Column(nullable = false) public boolean verified;

    protected PlayedMatchEntity() {}
    public PlayedMatchEntity(PlayedMatchRecord r) {
        id = r.id(); createdAt = r.createdAt(); playerName = r.playerName();
        opponentName = r.opponentName(); winner = r.winner(); turns = r.turns();
        playerHealth = r.playerHealth(); opponentHealth = r.opponentHealth();
        verified = r.verified();
    }
    public PlayedMatchRecord toRecord() {
        return new PlayedMatchRecord(id, createdAt, playerName, opponentName,
                winner, turns, playerHealth, opponentHealth, verified);
    }
}
