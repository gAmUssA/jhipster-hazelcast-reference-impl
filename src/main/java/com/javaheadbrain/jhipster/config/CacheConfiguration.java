package com.javaheadbrain.jhipster.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Hazelcast;
import com.javaheadbrain.jhipster.config.hazelcast.HazelcastConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

@SuppressWarnings("unused")
@Configuration
@EnableCaching
@AutoConfigureAfter(value = {HazelcastConfiguration.class, MetricsConfiguration.class})
@AutoConfigureBefore(value = {DatabaseConfiguration.class, WebConfigurer.class})
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    @Inject
    private Environment env;

    @Inject
    private HazelcastInstance hazelcastInstance;

    private CacheManager cacheManager;

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        log.debug("Starting HazelcastCacheManager");
        cacheManager = new com.hazelcast.spring.cache.HazelcastCacheManager(hazelcastInstance);
        return cacheManager;
    }

    /**
     * Use by Spring Security, to get events from Hazelcast.
     *
     * @return the session registry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
