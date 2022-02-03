package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePortaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePorta.class);
        TypePorta typePorta1 = new TypePorta();
        typePorta1.setId(1L);
        TypePorta typePorta2 = new TypePorta();
        typePorta2.setId(typePorta1.getId());
        assertThat(typePorta1).isEqualTo(typePorta2);
        typePorta2.setId(2L);
        assertThat(typePorta1).isNotEqualTo(typePorta2);
        typePorta1.setId(null);
        assertThat(typePorta1).isNotEqualTo(typePorta2);
    }
}
