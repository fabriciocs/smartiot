package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.CompliancePolicy;
import br.com.supera.feedback360.service.dto.CompliancePolicyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompliancePolicy} and its DTO {@link CompliancePolicyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompliancePolicyMapper extends EntityMapper<CompliancePolicyDTO, CompliancePolicy> {}
