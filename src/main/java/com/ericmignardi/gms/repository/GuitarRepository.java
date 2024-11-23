package com.ericmignardi.gms.repository;

import com.ericmignardi.gms.model.Guitar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuitarRepository extends JpaRepository<Guitar, Long> {
}