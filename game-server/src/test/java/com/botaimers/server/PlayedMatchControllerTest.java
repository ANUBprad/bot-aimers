package com.botaimers.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayedMatchControllerTest {
    @Autowired MockMvc mvc;
    private static final String VALID = """
            {"playerName":"Player","opponentName":"CPU","winner":"Player",
             "turns":11,"playerHealth":18,"opponentHealth":0}
            """;

    @Test void acceptsLocalMatchAsUnverified() throws Exception {
        mvc.perform(post("/api/played-matches").contentType(MediaType.APPLICATION_JSON)
                .content(VALID)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.verified").value(false))
                .andExpect(jsonPath("$.winner").value("Player"));
    }

    @Test void listsLocalMatches() throws Exception {
        mvc.perform(get("/api/played-matches")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test void rejectsImpossibleResult() throws Exception {
        mvc.perform(post("/api/played-matches").contentType(MediaType.APPLICATION_JSON)
                .content(VALID.replace("\"opponentHealth\":0", "\"opponentHealth\":10")))
                .andExpect(status().isBadRequest());
    }

    @Test void rejectsMissingPlayer() throws Exception {
        mvc.perform(post("/api/played-matches").contentType(MediaType.APPLICATION_JSON)
                .content(VALID.replace("\"Player\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }
}
