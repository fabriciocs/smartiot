package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SysRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SysRole getSysRoleSample1() {
        return new SysRole().id(1L).roleName("roleName1").description("description1");
    }

    public static SysRole getSysRoleSample2() {
        return new SysRole().id(2L).roleName("roleName2").description("description2");
    }

    public static SysRole getSysRoleRandomSampleGenerator() {
        return new SysRole()
            .id(longCount.incrementAndGet())
            .roleName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
