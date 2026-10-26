package com.botaimers.server;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/played-matches")
public final class PlayedMatchController {
    private final PlayedMatchService service;
    public PlayedMatchController(PlayedMatchService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<PlayedMatchRecord> create(@Valid @RequestBody PlayedMatchRequest request) {
        PlayedMatchRecord record = service.save(request);
        return ResponseEntity.created(URI.create("/api/played-matches/" + record.id())).body(record);
    }

    @GetMapping
    public List<PlayedMatchRecord> list() { return service.all(); }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> invalid(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
