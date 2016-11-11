package com.acme.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.components.IConfigService;
import com.acme.util.DataUtils;

public class ConfigPathChildrenCacheListener implements PathChildrenCacheListener
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigPathChildrenCacheListener.class);
    
    private IConfigService configService;
    
    public ConfigPathChildrenCacheListener(IConfigService configService)
    {
        super();
        this.configService = configService;
    }

    @Override
    public void childEvent(CuratorFramework curatorframework, PathChildrenCacheEvent event)
            throws Exception
    {
        final ChildData childData = event.getData(); 
        if (null == childData) {
            return;
        }
        final String data = DataUtils.readBytes(childData.getData());
        final String path = childData.getPath();
        final PathChildrenCacheEvent.Type type = event.getType();
        switch (type) {
            case CHILD_REMOVED: {
                configService.remove(path);
                LOG.info("Node : {} removed.", path);
                break;
            }
            case CHILD_ADDED: {
                configService.put(path, data);
                LOG.info("Node : {} created, set data : {}.", path, data);
                break;
            }
            case CHILD_UPDATED: {
                configService.put(path, data);
                LOG.info("Node : {} updated, set data : {}.", path, data);
                break;
            }
            default: {
                break;
            }
        }
    }
}
