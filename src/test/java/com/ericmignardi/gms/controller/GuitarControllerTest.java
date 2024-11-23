package com.ericmignardi.gms.controller;

import com.ericmignardi.gms.model.Guitar;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GuitarControllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private String guitarJson;

    @BeforeEach
    void setUp() throws Exception {
        Guitar defaultGuitar = new Guitar(
                1L,
                "Squier",
                "Starcaster",
                "Olympic White",
                "Electric",
                "asdfsdf23",
                "Cool guitar");
        guitarJson = objectMapper.writeValueAsString(defaultGuitar);
    }

    @Test
    void connectionEstablished() {
        assertThat(mySQLContainer.isCreated()).isTrue();
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @Test
    @WithMockUser
    void shouldFindAllGuitars() throws Exception {
        mockMvc.perform(post("/guitars")
                        .content(guitarJson)
                        .contentType("application/json"))
                .andExpect(status().isOk());
        MvcResult mvcResult = mockMvc.perform(get("/guitars"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("[" + guitarJson + "]");
    }

    @Test
    @WithMockUser
    void shouldFindGuitarById() throws Exception {
        mockMvc.perform(post("/guitars")
                        .content(guitarJson)
                        .contentType("application/json"))
                .andExpect(status().isOk());
        MvcResult mvcResult = mockMvc.perform(get("/guitars/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(guitarJson);
    }

    @Test
    @WithMockUser
    void shouldCreateGuitar() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/guitars")
                        .content(guitarJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(guitarJson);
        MvcResult getResult = mockMvc.perform(get("/guitars/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(getResult.getResponse().getContentAsString()).isEqualTo(guitarJson);
    }

    @Test
    @WithMockUser
    void shouldUpdateGuitar() throws Exception {
        mockMvc.perform(post("/guitars")
                        .content(guitarJson)
                        .contentType("application/json"))
                .andExpect(status().isOk());
        Guitar updatedGuitar = new Guitar(
                1L,
                "Squier",
                "Starcaster",
                "Shell Pink",
                "Electric",
                "asdfsdf23",
                "Cool guitar");
        String updatedJson = objectMapper.writeValueAsString(updatedGuitar);
        MvcResult updateResult = mockMvc.perform(put("/guitars/{id}", 1L)
                        .content(updatedJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(updateResult.getResponse().getContentAsString()).isEqualTo(updatedJson);
        MvcResult getResult = mockMvc.perform(get("/guitars/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(getResult.getResponse().getContentAsString()).isEqualTo(updatedJson);
    }

    @Test
    @WithMockUser
    void shouldDeleteGuitar() throws Exception {
        mockMvc.perform(post("/guitars")
                        .content(guitarJson)
                        .contentType("application/json"))
                .andExpect(status().isOk());
        MvcResult deleteResult = mockMvc.perform(delete("/guitars/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(deleteResult.getResponse().getContentAsString()).isEqualTo(guitarJson);
    }
}