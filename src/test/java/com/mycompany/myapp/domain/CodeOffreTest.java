package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeOffreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeOffre.class);
        CodeOffre codeOffre1 = new CodeOffre();
        codeOffre1.setId(1L);
        CodeOffre codeOffre2 = new CodeOffre();
        codeOffre2.setId(codeOffre1.getId());
        assertThat(codeOffre1).isEqualTo(codeOffre2);
        codeOffre2.setId(2L);
        assertThat(codeOffre1).isNotEqualTo(codeOffre2);
        codeOffre1.setId(null);
        assertThat(codeOffre1).isNotEqualTo(codeOffre2);
    }
}
