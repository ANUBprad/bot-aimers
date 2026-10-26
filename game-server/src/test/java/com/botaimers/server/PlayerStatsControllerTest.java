package com.botaimers.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerStatsControllerTest {
    @Autowired MockMvc mvc;

    @Test void returnsStatisticsShape() throws Exception {
        mvc.perform(get("/api/players/UnknownPlayer/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").isNumber())
                .andExpect(jsonPath("$.wins").isNumber())
                .andExpect(jsonPath("$.losses").isNumber());
    }
}
