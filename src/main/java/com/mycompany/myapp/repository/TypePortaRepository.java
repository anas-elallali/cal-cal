package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TypePorta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypePorta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePortaRepository extends JpaRepository<TypePorta, Long> {}
