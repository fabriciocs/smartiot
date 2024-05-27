package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SysUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SysUser getSysUserSample1() {
        return new SysUser().id(1L).name("name1").email("email1").passwordHash("passwordHash1");
    }

    public static SysUser getSysUserSample2() {
        return new SysUser().id(2L).name("name2").email("email2").passwordHash("passwordHash2");
    }

    public static SysUser getSysUserRandomSampleGenerator() {
        return new SysUser()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .passwordHash(UUID.randomUUID().toString());
    }
}
