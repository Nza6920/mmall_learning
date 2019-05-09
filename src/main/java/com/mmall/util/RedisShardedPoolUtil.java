package com.mmall.util;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * redis Api 封装类
 */
@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 存储 redis 键值
     * @param key     键
     * @param value   值
     * @return String
     */
    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 获取 redis 中的值
     * @param key 键
     * @return String
     */
    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置有时间限制的键值
     * @param key     键
     * @param value   值
     * @param exTime  过期时间 (s)
     * @return String
     */
    public static String setEx(String key, String value, int exTime) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setEx key:{} exTime:{} value:{} error", key, exTime, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置键的过期时间
     * @param key    键
     * @param exTime 过期时间(s)
     * @return Long
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} exTime:{} error", key, exTime, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除键
     * @param key 键
     * @return Long
     */
    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            RedisShardedPoolUtil.set("key2" + i, "value2" + i);
        }

        System.out.println("success :)");
    }
}
