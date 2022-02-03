package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Affaire.class);
        Affaire affaire1 = new Affaire();
        affaire1.setId(1L);
        Affaire affaire2 = new Affaire();
        affaire2.setId(affaire1.getId());
        assertThat(affaire1).isEqualTo(affaire2);
        affaire2.setId(2L);
        assertThat(affaire1).isNotEqualTo(affaire2);
        affaire1.setId(null);
        assertThat(affaire1).isNotEqualTo(affaire2);
    }
}
