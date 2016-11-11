package com.acme.zookeeper.config;

public final class ZkConfig 
{
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 30000;
    private static final int DEFAULT_SESSION_TIMEOUT_MS = 15000;
    
    /**
     * zk服务地址
     */
    private String connectionString;
    
    /**
     * 连接超时时间
     */
    private int connectionTimeoutMs;
    
    /**
     * session超时时间
     */
    private int sessionTimeoutMs;

    public ZkConfig(String connectionString, int connectionTimeoutMs, int sessionTimeoutMs) 
    {
        super();
        this.connectionString = connectionString;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.sessionTimeoutMs = sessionTimeoutMs;
    }
    
    public ZkConfig(String connectionString) 
    {
        this(connectionString, DEFAULT_CONNECTION_TIMEOUT_MS, DEFAULT_SESSION_TIMEOUT_MS);
    }

    public String getConnectionString() 
    {
        return connectionString;
    }

    public void setConnectionString(String connectionString) 
    {
        this.connectionString = connectionString;
    }

    public int getConnectionTimeoutMs() 
    {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) 
    {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getSessionTimeoutMs() 
    {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) 
    {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }
}
