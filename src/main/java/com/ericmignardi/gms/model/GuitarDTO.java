package com.ericmignardi.gms.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class GuitarDTO {
    private String brand;
    private String model;
    private String colour;
    private String type;
    private String serialNumber;
    private String description;
    private MultipartFile fileName;
}