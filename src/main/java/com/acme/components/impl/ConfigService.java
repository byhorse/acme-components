package com.acme.components.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.components.IConfigService;
import com.acme.listener.ConfigPathChildrenCacheListener;
import com.acme.zookeeper.ZkClient;
import com.acme.zookeeper.config.ZkConfig;

public class ConfigService implements IConfigService
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);
    
    private static final String DEFAULT_ROOTPATH = "/com/acme/config/1.0.0";
    
    private final Map<String, String> cache = new ConcurrentHashMap<String, String>();
    
    private String zkRootPath;
    
    private ZkClient zkClient;
    
    public ConfigService(ZkConfig zkConfig, String zkRootPath)
    {
        this.zkClient = new ZkClient(zkConfig);
        this.zkRootPath = zkRootPath;
    }
    
    public ConfigService(ZkConfig zkConfig)
    {
        this(zkConfig, DEFAULT_ROOTPATH);
    }
    
    public String get(String key)
    {
        return cache.get(key);
    }
    
    public void put(String key, String value)
    {
        cache.put(key, value);
    }
    
    public void remove(String key)
    {
        cache.remove(key);
    }
    
    public void iterator(String modulePath)
    {
            load(zkRootPath + "/" + modulePath);
    }
    
    public void closeConfigService() {
        zkClient.destory();
        LOG.debug("ConfigService closed success.");
    }
    
    private void load(String parentPath) {
        try {
            zkClient.addPathChildListener(parentPath, new ConfigPathChildrenCacheListener(this));
            List<String> nodes = zkClient.getChildren(parentPath);
            for (String node : nodes) {
                cache.put(getFullPath(parentPath, node), zkClient.getData(getFullPath(parentPath, node)));
            }
        } catch (Exception e) {
            LOG.error("Load configer data in zookeeper encounter exception.", e);
        }
        
        traceCache();
    }
    
    private void traceCache() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("All items in cache by now----------------------------------------");
            for (Map.Entry<String, String> entry : cache.entrySet()) {
                LOG.debug("Key: {}, value: {}", entry.getKey(), entry.getValue());
            }
        }
    }
    
    private String getFullPath(String parentPath, String node)
    {
        return parentPath + "/" + node;
    }
}
