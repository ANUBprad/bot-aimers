package com.botaimers.server;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "simulated_matches")
public class SimulatedMatchEntity {
    @Id public UUID id;
    @Column(nullable = false) public Instant createdAt;
    @Column(nullable = false, length = 40) public String firstName;
    @Column(nullable = false, length = 40) public String secondName;
    @Column(nullable = false, columnDefinition = "text") public String resultJson;

    protected SimulatedMatchEntity() {}
    public SimulatedMatchEntity(UUID id, Instant createdAt, String firstName, String secondName, String resultJson) {
        this.id = id; this.createdAt = createdAt; this.firstName = firstName;
        this.secondName = secondName; this.resultJson = resultJson;
    }
}
