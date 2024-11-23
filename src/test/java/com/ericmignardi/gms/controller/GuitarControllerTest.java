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
                "Cool guitar",
                "starcaster.jpg");
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
                "Cool guitar",
                "starcaster.jpg");
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



//    @Autowired
//    TestRestTemplate testRestTemplate;

//    @Test
//    @Disabled
//    void shouldFindAllGuitars() {
//        ResponseEntity<List<Guitar>> response = testRestTemplate.exchange("/guitars", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//    }
//
//    @Test
//    @Disabled
//    void shouldFindGuitarByIdWhenValid() {
//        ResponseEntity<Guitar> response = testRestTemplate.exchange("/guitars/1", HttpMethod.GET, null, Guitar.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//    }
//
//    @Test
//    @Disabled
//    void shouldCreateGuitar() {
//        Guitar guitar = new Guitar(1L, "Squier", "Starcaster", "Olympic White", "Electric", "asdfsdf23", "Cool guitar", "starcaster.jpg");
//        ResponseEntity<Guitar> response = testRestTemplate.exchange("/guitars", HttpMethod.POST, new HttpEntity<>(guitar), Guitar.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        ResponseEntity<Guitar> response1 = testRestTemplate.exchange("/guitars/1", HttpMethod.GET, null, Guitar.class);
//        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response1.getBody().getBrand()).isEqualTo("Squier");
//        assertThat(response1.getBody().getModel()).isEqualTo("Starcaster");
//        assertThat(response1.getBody().getColour()).isEqualTo("Olympic White");
//        assertThat(response1.getBody().getType()).isEqualTo("Electric");
//        assertThat(response1.getBody().getSerialNumber()).isEqualTo("asdfsdf23");
//        assertThat(response1.getBody().getDescription()).isEqualTo("Cool guitar");
//        assertThat(response1.getBody().getFileName()).isEqualTo("starcaster.jpg");
//    }
//
//    @Test
//    @Disabled
//    void shouldUpdateGuitar() {
//        Guitar guitar = new Guitar(1L, "Squier", "Starcaster", "Olympic White", "Electric", "asdfsdf23", "Cool guitar", "starcaster.jpg");
//        ResponseEntity<Guitar> response = testRestTemplate.exchange("/guitars", HttpMethod.POST, new HttpEntity<>(guitar), Guitar.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        Guitar guitarToUpdate = new Guitar(1L, "Fender", "Starcaster", "Olympic White", "Electric", "asdfsdf23", "Cool guitar", "starcaster.jpg");
//        ResponseEntity<Guitar> response2 = testRestTemplate.exchange("/guitars/1", HttpMethod.PUT, new HttpEntity<>(guitarToUpdate), Guitar.class);
//        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response2.getBody()).isNotNull();
//        ResponseEntity<Guitar> response3 = testRestTemplate.exchange("/guitars/1", HttpMethod.GET, null, Guitar.class);
//        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response3.getBody().getBrand()).isEqualTo("Fender");
//    }
//
//    @Test
//    @Disabled
//    void shouldDeleteGuitar() {
//        Guitar guitar = new Guitar(1L, "Squier", "Starcaster", "Olympic White", "Electric", "asdfsdf23", "Cool guitar", "starcaster.jpg");
//        ResponseEntity<Guitar> response = testRestTemplate.exchange("/guitars", HttpMethod.POST, new HttpEntity<>(guitar), Guitar.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        ResponseEntity<Guitar> response2 = testRestTemplate.exchange("/guitars/1", HttpMethod.GET, null, Guitar.class);
//        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
//        ResponseEntity<Guitar> response3 = testRestTemplate.exchange("/guitars/1", HttpMethod.DELETE, null, Guitar.class);
//        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
//        ResponseEntity<Guitar> response4 = testRestTemplate.exchange("/guitars/1", HttpMethod.GET, null, Guitar.class);
//        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
}