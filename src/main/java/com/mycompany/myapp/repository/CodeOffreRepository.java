package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CodeOffre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CodeOffre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeOffreRepository extends JpaRepository<CodeOffre, Long> {}
