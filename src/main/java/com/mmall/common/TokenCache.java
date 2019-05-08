package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenCache {
    private static LoadingCache<String, String> localCache = CacheBuilder
            .newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)   // 超过 10000 使用lRU(最小使用算法)算法自动清除
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                // 默认的加载实现,当调用get取值时, 如果没有key对应的值, 就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    // 设置缓存
    public static void setKey(String key, String value)
    {
        localCache.put(key, value);
    }

    // 获取缓存值
    public static String getKey(String key)
    {
        String value = null;

        try {
            value = localCache.get(key);

            if ("null".equals(value)) {
                return null;
            }

            return value;
        } catch (Exception e) {
            log.error("localCache get error", e);
        }

        return null;
    }
}
