package com.acme.components;

public interface IConfigService
{
    public String get(String key);
    
    public void put(String key, String value);
    
    public void remove(String key);
    
    public void iterator(String modulePath);
    
    public void closeConfigService();
}
