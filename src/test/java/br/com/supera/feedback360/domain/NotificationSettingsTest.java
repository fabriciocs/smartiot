package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.NotificationSettingsTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationSettings.class);
        NotificationSettings notificationSettings1 = getNotificationSettingsSample1();
        NotificationSettings notificationSettings2 = new NotificationSettings();
        assertThat(notificationSettings1).isNotEqualTo(notificationSettings2);

        notificationSettings2.setId(notificationSettings1.getId());
        assertThat(notificationSettings1).isEqualTo(notificationSettings2);

        notificationSettings2 = getNotificationSettingsSample2();
        assertThat(notificationSettings1).isNotEqualTo(notificationSettings2);
    }

    @Test
    void userTest() throws Exception {
        NotificationSettings notificationSettings = getNotificationSettingsRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        notificationSettings.setUser(sysUserBack);
        assertThat(notificationSettings.getUser()).isEqualTo(sysUserBack);

        notificationSettings.user(null);
        assertThat(notificationSettings.getUser()).isNull();
    }
}
