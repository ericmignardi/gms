package com.ericmignardi.gms.repository;

import com.ericmignardi.gms.model.Guitar;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GuitarRepositoryTest {

    @Autowired
    private GuitarRepository guitarRepository;
    private Guitar guitar;

    @BeforeEach
    void setUp() {
        guitar = new Guitar(
                1L,
                "Fender",
                "Starcaster",
                "Olympic White",
                "Electric",
                "asdfasdfas",
                "This is a cool guitar.");
        guitarRepository.save(guitar);
    }

    @Test
    @Disabled
    void canReadAllGuitars() {
        List<Guitar> guitars = guitarRepository.findAll();
        Assertions.assertThat(guitars).size().isEqualTo(1);
    }

    @Test
    @Disabled
    void canReadGuitarById() {
        Optional<Guitar> optionalGuitar = guitarRepository.findById(guitar.getId());
        Assertions.assertThat(optionalGuitar).isPresent();
    }

    @Test
    @Disabled
    void canCreateGuitar() {
        Guitar newGuitar = new Guitar(
                2L,
                "Gibson",
                "ES-333",
                "Olive Drab",
                "Electric",
                "asdfaasdfsdfas",
                "This is a another cool guitar.");
        guitarRepository.save(newGuitar);
        Optional<Guitar> optionalGuitar = guitarRepository.findById(newGuitar.getId());
        Assertions.assertThat(optionalGuitar.isPresent()).isTrue();
    }

    @Test
    @Disabled
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
    @Disabled
    void canDeleteGuitar() {
        guitarRepository.deleteById(guitar.getId());
        Optional<Guitar> optionalGuitar = guitarRepository.findById(guitar.getId());
        Assertions.assertThat(optionalGuitar).isNotPresent();
    }

    @AfterEach
    void tearDown() {
        guitarRepository.deleteAll();
    }
}
