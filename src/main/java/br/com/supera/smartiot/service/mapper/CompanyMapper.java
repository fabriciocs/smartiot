package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Company;
import br.com.supera.smartiot.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
