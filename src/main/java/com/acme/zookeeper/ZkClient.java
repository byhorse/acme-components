package com.acme.zookeeper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import com.acme.util.DataUtils;
import com.acme.zookeeper.config.ZkConfig;

public final class ZkClient
{
    private Map<String, PathChildrenCache> pathChildrenCaches = new ConcurrentHashMap<String, PathChildrenCache>();
    
    private final CuratorFramework client;
    
    private RetryPolicy retryPolicy =  new ExponentialBackoffRetry(1000, 3);
    
    public ZkClient(final ZkConfig zkConfig, Charset charset)
    {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(zkConfig.getConnectionString())
                .retryPolicy(retryPolicy).connectionTimeoutMs(zkConfig.getConnectionTimeoutMs())
                .sessionTimeoutMs(zkConfig.getSessionTimeoutMs()).build();
        try {
            curatorFramework.start();
            curatorFramework.blockUntilConnected();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
        
        this.client = curatorFramework;
    }
    
    public ZkClient(final ZkConfig zkConfig)
    {
        this(zkConfig, null);
    }
    
    /**
     * 增加对子节点的监听
     * @param path
     * @param listener
     * @throws Exception
     */
    public void addPathChildListener(String path, PathChildrenCacheListener listener) throws Exception
    {
        PathChildrenCache cacheNode = pathChildrenCaches.get(path);
        if (null == cacheNode) {
            cacheNode = new PathChildrenCache(client, path, true);
            cacheNode.getListenable().addListener(listener);
            cacheNode.start();
            pathChildrenCaches.put(path, cacheNode);
        }
    }
    
    /**
     * 获取节点数据
     * @param path
     * @return
     * @throws Exception
     */
    public String getData(String path) throws Exception
    {
            return DataUtils.readBytes(client.getData().forPath(path));
    }
    
    /**
     * 获取字节集合
     * @param parentPath
     * @return
     * @throws Exception
     */
    public List<String> getChildren(String parentPath) throws Exception
    {
        return client.getChildren().forPath(parentPath);
    }
    
    /**
     * 关闭
     */
    public void destory() {
        for (PathChildrenCache cache : pathChildrenCaches.values()) {
            CloseableUtils.closeQuietly(cache);
        }
        CloseableUtils.closeQuietly(client);
    }
}
