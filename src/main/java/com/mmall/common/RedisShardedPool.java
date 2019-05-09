package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;          // Jedis 连接池

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));        // 最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));         // 最大空闲实例个数
    private static Integer minidle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));         // 最小空闲实例个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));    // 在获取jedis实例时否要进行验证操作
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));    // 在归还jedis实例时否要进行验证操作

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port  = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port  = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    static {
        initPool();
    }

    /**
     * 初始化连接池
     */
    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minidle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);    // 连接耗尽是是否阻塞, true: 阻塞直到超时

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        // Hashing.MURMUR_HASH 一致性算法
        pool = new ShardedJedisPool(config, jedisShardInfoList,
                Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    /**
     * 获取 Jedis 实例
     * @return Jedis
     */
    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    /**
     * 将连接归还连接池
     * @param jedis jedis连接
     */
    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * 将坏连接归还连接池
     * @param jedis jedis连接
     */
    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();

        for (int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }

        returnResource(jedis);
//        pool.destroy();  // 销毁连接池子的所有连接

        System.out.println("success:)");
    }
}
