package com.botaimers.client;

import com.botaimers.arena.PlayerBattle;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/** Best-effort local match reporting; gameplay never depends on network access. */
public final class MatchReporter {
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2)).build();
    private final URI endpoint;

    public MatchReporter(String serverBaseUrl) {
        endpoint = URI.create(serverBaseUrl.replaceAll("/+$", "") + "/api/played-matches");
    }

    public CompletableFuture<String> report(PlayerBattle battle) {
        if (!battle.finished()) throw new IllegalArgumentException("Match is still active");
        String json = "{\"playerName\":\"Player\",\"opponentName\":\"CPU\","
                + "\"winner\":\"" + battle.winner() + "\","
                + "\"turns\":" + battle.turns() + ","
                + "\"playerHealth\":" + battle.playerHealth() + ","
                + "\"opponentHealth\":" + battle.enemyHealth() + "}";
        HttpRequest request = HttpRequest.newBuilder(endpoint)
                .timeout(Duration.ofSeconds(3))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        return http.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .handle((response, error) -> error != null ? "Offline - result not saved"
                        : response.statusCode() == 201 ? "Result saved to Spring Boot"
                        : "Server rejected result (" + response.statusCode() + ")");
    }
}
