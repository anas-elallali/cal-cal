package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypologieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Typologie.class);
        Typologie typologie1 = new Typologie();
        typologie1.setId(1L);
        Typologie typologie2 = new Typologie();
        typologie2.setId(typologie1.getId());
        assertThat(typologie1).isEqualTo(typologie2);
        typologie2.setId(2L);
        assertThat(typologie1).isNotEqualTo(typologie2);
        typologie1.setId(null);
        assertThat(typologie1).isNotEqualTo(typologie2);
    }
}
