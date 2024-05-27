package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.ProfileTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = getProfileSample1();
        Profile profile2 = new Profile();
        assertThat(profile1).isNotEqualTo(profile2);

        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);

        profile2 = getProfileSample2();
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    void userTest() throws Exception {
        Profile profile = getProfileRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        profile.setUser(sysUserBack);
        assertThat(profile.getUser()).isEqualTo(sysUserBack);

        profile.user(null);
        assertThat(profile.getUser()).isNull();
    }
}
