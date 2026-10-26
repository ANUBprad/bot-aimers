package com.botaimers.server;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SimulatedMatchRepository extends JpaRepository<SimulatedMatchEntity, UUID> {
    List<SimulatedMatchEntity> findAllByOrderByCreatedAtDescIdAsc();
}
