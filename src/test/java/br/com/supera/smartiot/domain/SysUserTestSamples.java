package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SysUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SysUser getSysUserSample1() {
        return new SysUser().id(1L).username("username1").email("email1").role("role1");
    }

    public static SysUser getSysUserSample2() {
        return new SysUser().id(2L).username("username2").email("email2").role("role2");
    }

    public static SysUser getSysUserRandomSampleGenerator() {
        return new SysUser()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .role(UUID.randomUUID().toString());
    }
}
