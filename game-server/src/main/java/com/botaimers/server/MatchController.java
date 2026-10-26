package com.botaimers.server;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public final class MatchController {
    private final MatchService service;

    public MatchController(MatchService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MatchRecord> create(@Valid @RequestBody MatchRequest request) {
        MatchRecord record = service.create(request);
        return ResponseEntity.created(URI.create("/api/matches/" + record.id())).body(record);
    }

    @GetMapping
    public List<MatchRecord> list() {
        return service.all();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchRecord> get(@PathVariable UUID id) {
        return service.find(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> invalid(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(error.getMessage());
    }
}
