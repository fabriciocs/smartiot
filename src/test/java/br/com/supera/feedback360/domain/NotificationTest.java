package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.NotificationTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void recipientTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        notification.setRecipient(sysUserBack);
        assertThat(notification.getRecipient()).isEqualTo(sysUserBack);

        notification.recipient(null);
        assertThat(notification.getRecipient()).isNull();
    }
}
