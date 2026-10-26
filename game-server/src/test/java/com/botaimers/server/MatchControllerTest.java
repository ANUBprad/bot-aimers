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
class MatchControllerTest {
    @Autowired MockMvc mvc;

    @Test void createsAndRetrievesMatch() throws Exception {
        String location = mvc.perform(post("/api/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Alpha\",\"secondName\":\"Beta\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.winner").value("Alpha"))
                .andExpect(jsonPath("$.result.rounds").isNumber())
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alpha"));
    }

    @Test void rejectsInvalidNames() throws Exception {
        mvc.perform(post("/api/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\" \",\"secondName\":\"Beta\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Alpha\",\"secondName\":\"alpha\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void returns404ForUnknownMatch() throws Exception {
        mvc.perform(get("/api/matches/00000000-0000-0000-0000-000000000001"))
                .andExpect(status().isNotFound());
    }

    @Test void listsMatchHistory() throws Exception {
        mvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
