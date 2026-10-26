package com.botaimers.server;

import jakarta.validation.constraints.*;

/** Unverified local-game report; not an authoritative multiplayer result. */
public record PlayedMatchRequest(
        @NotBlank @Size(max = 40) String playerName,
        @NotBlank @Size(max = 40) String opponentName,
        @NotBlank @Size(max = 40) String winner,
        @Min(1) @Max(100000) int turns,
        @Min(0) @Max(100000) int playerHealth,
        @Min(0) @Max(100000) int opponentHealth) {}
