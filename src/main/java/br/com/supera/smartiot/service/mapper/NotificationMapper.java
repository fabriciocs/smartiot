package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Notification;
import br.com.supera.smartiot.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {}
