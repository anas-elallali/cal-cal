package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ordre.class);
        Ordre ordre1 = new Ordre();
        ordre1.setId(1L);
        Ordre ordre2 = new Ordre();
        ordre2.setId(ordre1.getId());
        assertThat(ordre1).isEqualTo(ordre2);
        ordre2.setId(2L);
        assertThat(ordre1).isNotEqualTo(ordre2);
        ordre1.setId(null);
        assertThat(ordre1).isNotEqualTo(ordre2);
    }
}
