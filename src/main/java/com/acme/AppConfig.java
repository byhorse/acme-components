package com.acme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.acme.components.IConfigService;
import com.acme.components.impl.ConfigService;
import com.acme.zookeeper.config.ZkConfig;

@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig
{
    private final String zkAddress = System.getProperty("ACME_CFG_ZOOKEEPER_ENV_URL"); 
    
    @Bean(destroyMethod = "closeConfigService")
    public IConfigService configService()
    {
        ZkConfig zkConfig = new ZkConfig(zkAddress);
        IConfigService service = new ConfigService(zkConfig);
        service.iterator("service");
        service.iterator("datasource");
        return service;
    }
}
