package com.botaimers.server;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Names are validated before a new simulated match is created. */
public record MatchRequest(
        @NotBlank @Size(max = 40) String firstName,
        @NotBlank @Size(max = 40) String secondName) {}
