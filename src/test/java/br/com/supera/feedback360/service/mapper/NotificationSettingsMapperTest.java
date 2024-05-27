package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.NotificationSettingsAsserts.*;
import static br.com.supera.feedback360.domain.NotificationSettingsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationSettingsMapperTest {

    private NotificationSettingsMapper notificationSettingsMapper;

    @BeforeEach
    void setUp() {
        notificationSettingsMapper = new NotificationSettingsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationSettingsSample1();
        var actual = notificationSettingsMapper.toEntity(notificationSettingsMapper.toDto(expected));
        assertNotificationSettingsAllPropertiesEquals(expected, actual);
    }
}
