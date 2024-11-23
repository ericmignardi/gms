package com.ericmignardi.gms.controller;

import com.ericmignardi.gms.model.Guitar;
import com.ericmignardi.gms.model.GuitarDTO;
import com.ericmignardi.gms.service.GuitarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GuitarController {

    private final GuitarService guitarService;

    // Pages
    @GetMapping("/read-guitar")
    public String showReadGuitarPage(Model model) {
        List<Guitar> guitars = guitarService.readAll();
        model.addAttribute("guitars", guitars);
        return "pages/read-guitar";
    }

    @GetMapping("/create-guitar")
    public String showCreateGuitarPage(Model model) {
        GuitarDTO guitarDTO = new GuitarDTO();
        model.addAttribute("guitarDTO", guitarDTO);
        return "pages/create-guitar";
    }

    @GetMapping("/update-guitar")
    public String showUpdateGuitarPage(Model model, @RequestParam Long id) {
        try {
            Guitar guitar = guitarService.readById(id);
            model.addAttribute("guitar", guitar);
            GuitarDTO guitarDTO = new GuitarDTO();
            guitarDTO.setBrand(guitar.getBrand());
            guitarDTO.setModel(guitar.getModel());
            guitarDTO.setColour(guitar.getColour());
            guitarDTO.setType(guitar.getType());
            guitarDTO.setSerialNumber(guitar.getSerialNumber());
            guitarDTO.setDescription(guitar.getDescription());
            model.addAttribute("guitarDTO", guitarDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/read-guitar";
        }
        return "pages/update-guitar";
    }

    // Operations
    @PostMapping("/create-guitar")
    public String createGuitar(@ModelAttribute("guitarDTO") GuitarDTO guitarDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "pages/create-guitar";
        }
        Guitar guitar = new Guitar();
        guitar.setBrand(guitarDTO.getBrand());
        guitar.setModel(guitarDTO.getModel());
        guitar.setColour(guitarDTO.getColour());
        guitar.setType(guitarDTO.getType());
        guitar.setSerialNumber(guitarDTO.getSerialNumber());
        guitar.setDescription(guitarDTO.getDescription());
        guitarService.create(guitar);
        return "redirect:/read-guitar";
    }

    @PostMapping("/update-guitar")
    public String updateGuitar(Model model, @RequestParam Long id, @ModelAttribute GuitarDTO guitarDTO, BindingResult result) {
        try {
            Guitar guitar = guitarService.readById(id);
            model.addAttribute("guitar", guitar);
            if (result.hasErrors()) {
                return "pages/update-guitar";
            }
            guitar.setBrand(guitarDTO.getBrand());
            guitar.setModel(guitarDTO.getModel());
            guitar.setColour(guitarDTO.getColour());
            guitar.setType(guitarDTO.getType());
            guitar.setSerialNumber(guitarDTO.getSerialNumber());
            guitar.setDescription(guitarDTO.getDescription());
            guitarService.create(guitar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/read-guitar";
    }

    @GetMapping("/delete-guitar")
    public String deleteGuitar(@RequestParam Long id) {
        guitarService.delete(id);
        return "redirect:/read-guitar";
    }


    // JSON Routes
    @PostMapping("/guitars")
    public ResponseEntity<Guitar> create(@RequestBody Guitar guitar) {
        return ResponseEntity.ok(guitarService.create(guitar));
    }

    @GetMapping("/guitars/{id}")
    public ResponseEntity<Guitar> readById(@PathVariable Long id) {
        return ResponseEntity.ok(guitarService.readById(id));
    }

    @GetMapping("/guitars")
    public ResponseEntity<List<Guitar>> readAll() {
        return ResponseEntity.ok(guitarService.readAll());
    }

    @PutMapping("/guitars/{id}")
    public ResponseEntity<Guitar> update(@PathVariable Long id, @RequestBody Guitar guitar) {
        return ResponseEntity.ok(guitarService.update(id, guitar));
    }

    @DeleteMapping("/guitars/{id}")
    public ResponseEntity<Guitar> delete(@PathVariable Long id) {
        return ResponseEntity.ok(guitarService.delete(id));
    }
}