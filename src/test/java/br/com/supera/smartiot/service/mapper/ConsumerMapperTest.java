package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ConsumerAsserts.*;
import static br.com.supera.smartiot.domain.ConsumerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerMapperTest {

    private ConsumerMapper consumerMapper;

    @BeforeEach
    void setUp() {
        consumerMapper = new ConsumerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConsumerSample1();
        var actual = consumerMapper.toEntity(consumerMapper.toDto(expected));
        assertConsumerAllPropertiesEquals(expected, actual);
    }
}
