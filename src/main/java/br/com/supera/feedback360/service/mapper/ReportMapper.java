package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.Report;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.ReportDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "generatedBy", source = "generatedBy", qualifiedByName = "sysUserName")
    ReportDTO toDto(Report s);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
