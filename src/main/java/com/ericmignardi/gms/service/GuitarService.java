package com.ericmignardi.gms.service;

import com.ericmignardi.gms.model.Guitar;
import com.ericmignardi.gms.repository.GuitarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuitarService {

    private final GuitarRepository guitarRepository;

    public Guitar readById(Long id) {
        return guitarRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public List<Guitar> readAll() {
        return guitarRepository.findAll();
    }

    public Guitar create(Guitar guitar) {
        return guitarRepository.save(guitar);
    }

    public Guitar update(Long id, Guitar guitar) {
        Guitar updatedGuitar = guitarRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        updatedGuitar.setBrand(guitar.getBrand());
        updatedGuitar.setModel(guitar.getModel());
        updatedGuitar.setColour(guitar.getColour());
        updatedGuitar.setType(guitar.getType());
        updatedGuitar.setSerialNumber(guitar.getSerialNumber());
        updatedGuitar.setDescription(guitar.getDescription());
        updatedGuitar.setFileName(guitar.getFileName());
        return guitarRepository.save(updatedGuitar);
    }

    public Guitar delete(Long id) {
        Guitar guitar = guitarRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        guitarRepository.delete(guitar);
        return guitar;
    }
}
