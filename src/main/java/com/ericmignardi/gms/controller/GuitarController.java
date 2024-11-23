package com.ericmignardi.gms.controller;

import com.ericmignardi.gms.model.Guitar;
import com.ericmignardi.gms.model.GuitarDTO;
import com.ericmignardi.gms.service.GuitarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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
        if (guitarDTO.getFileName().isEmpty()) {
            result.addError(new FieldError("guitarDTO", "fileName", "Filename is required"));
        }
        if (result.hasErrors()) {
            return "pages/create-guitar";
        }
        MultipartFile image = guitarDTO.getFileName();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        try {
            String uploadDirectory = "public/images/";
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDirectory + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Guitar guitar = new Guitar();
        guitar.setBrand(guitarDTO.getBrand());
        guitar.setModel(guitarDTO.getModel());
        guitar.setColour(guitarDTO.getColour());
        guitar.setType(guitarDTO.getType());
        guitar.setSerialNumber(guitarDTO.getSerialNumber());
        guitar.setDescription(guitarDTO.getDescription());
        guitar.setFileName(storageFileName);
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
            if (!guitarDTO.getFileName().isEmpty()) {
                String uploadDirectory = "public/images/";
                Path oldImagePath = Paths.get(uploadDirectory + guitar.getFileName());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MultipartFile image = guitarDTO.getFileName();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDirectory + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }
                guitar.setFileName(storageFileName);
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
        try {
            Guitar guitar = guitarService.readById(id);
            Path imagePath = Paths.get("public/images/" + guitar.getFileName());
            try {
                Files.delete(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            guitarService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
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