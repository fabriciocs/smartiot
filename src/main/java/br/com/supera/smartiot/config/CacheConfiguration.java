package br.com.supera.smartiot.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, br.com.supera.smartiot.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, br.com.supera.smartiot.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, br.com.supera.smartiot.domain.User.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Authority.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.User.class.getName() + ".authorities");
            createCache(cm, br.com.supera.smartiot.domain.Cliente.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Cliente.class.getName() + ".sensores");
            createCache(cm, br.com.supera.smartiot.domain.Sensor.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Sensor.class.getName() + ".configuracaoAlertas");
            createCache(cm, br.com.supera.smartiot.domain.ConfiguracaoAlerta.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.DadoSensor.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.DadoSensor.class.getName() + ".sensors");
            createCache(cm, br.com.supera.smartiot.domain.AggregatedData.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Alert.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Company.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Consumer.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Consumer.class.getName() + ".alerts");
            createCache(cm, br.com.supera.smartiot.domain.CostCenter.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Enrollment.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Enrollment.class.getName() + ".measurements");
            createCache(cm, br.com.supera.smartiot.domain.ManualEntry.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Measurement.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Notification.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Payment.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Pricing.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Report.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.ResourceGroup.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Concentrator.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Meter.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Repeater.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.Transmitter.class.getName());
            createCache(cm, br.com.supera.smartiot.domain.SysUser.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
