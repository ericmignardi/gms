package com.ericmignardi.gms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guitars")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Guitar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String colour;
    @Column(nullable = false)
    private String type;
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "file_name", nullable = false)
    private String fileName;
}