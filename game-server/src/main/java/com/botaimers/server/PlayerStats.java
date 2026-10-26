package com.botaimers.server;

/** Aggregate statistics for an unverified local player name. */
public record PlayerStats(String playerName, long matches, long wins, long losses,
                          double winRatePercent) {}
