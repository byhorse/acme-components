package com.acme.util;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

public final class DataUtils
{
    private static Charset DEFAULT_CHARSET = Charsets.UTF_8;
    
    public static String readBytes(byte[] byteDatas, Charset charset)
    {
        return new String(byteDatas, charset);
    }
    
    public static String readBytes(byte[] byteDatas)
    {
        return readBytes(byteDatas, DEFAULT_CHARSET);
    }
    
    private DataUtils()
    {
    }
}
