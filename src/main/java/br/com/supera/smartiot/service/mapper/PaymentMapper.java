package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Payment;
import br.com.supera.smartiot.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {}
