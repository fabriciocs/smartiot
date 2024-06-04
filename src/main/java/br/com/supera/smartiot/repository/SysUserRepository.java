package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.SysUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SysUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {}
