package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Affaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Affaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffaireRepository extends JpaRepository<Affaire, Long> {}
