package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Typologie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Typologie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypologieRepository extends JpaRepository<Typologie, Long> {}
