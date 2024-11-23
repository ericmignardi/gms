package com.ericmignardi.gms.repository;

import com.ericmignardi.gms.model.Guitar;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GuitarRepositoryTestcontainersTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3");

    @Autowired
    private GuitarRepository guitarRepository;
    private Guitar guitar;

    @Test
    void connectionEstablished() {
        assertThat(mySQLContainer.isCreated()).isTrue();
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        guitar = new Guitar(
                null,
                "Fender",
                "Starcaster",
                "Olympic White",
                "Electric",
                "asdfasdfas",
                "This is a cool guitar.",
                "starcaster.jpg");
        guitarRepository.save(guitar);
    }

    @Test
    void canReadAllGuitars() {
        List<Guitar> guitars = guitarRepository.findAll();
        assertThat(guitars.size()).isEqualTo(1);
    }

    @Test
    void canReadGuitarById() {
        Optional<Guitar> guitarOptional = guitarRepository.findById(guitar.getId());
        assertThat(guitarOptional.isPresent()).isTrue();
    }

    @Test
    void canCreateGuitar() {
        Guitar newGuitar = new Guitar(
                null,
                "Gibson",
                "ES-333",
                "Olive Drab",
                "Electric",
                "asdfaasdfsdfas",
                "This is a another cool guitar.",
                "es-333.jpg");
        guitarRepository.save(newGuitar);
        Optional<Guitar> optionalGuitar = guitarRepository.findById(newGuitar.getId());
        assertThat(optionalGuitar.isPresent()).isTrue();
    }

    @Test
    void canUpdateGuitar() {
        Optional<Guitar> optionalGuitar = guitarRepository.findById(guitar.getId());
        if (optionalGuitar.isPresent()) {
            Guitar guitar = optionalGuitar.get();
            guitar.setColour("Sherwood Green");
            guitarRepository.save(guitar);
        }
        Optional<Guitar> optionalUpdatedGuitar = guitarRepository.findById(guitar.getId());
        if (optionalUpdatedGuitar.isPresent()) {
            Guitar updatedGuitar = optionalUpdatedGuitar.get();
            Assertions.assertThat(updatedGuitar.getColour()).isEqualTo("Sherwood Green");
        }
    }

    @Test
    void canDeleteGuitar() {
        guitarRepository.deleteById(guitar.getId());
        Optional<Guitar> guitarOptional = guitarRepository.findById(guitar.getId());
        assertThat(guitarOptional.isEmpty()).isTrue();
    }

    @AfterEach
    void tearDown() {
        guitarRepository.deleteAll();
    }
}