package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool;          // Jedis 连接池

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));        // 最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));         // 最大空闲实例个数
    private static Integer minidle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));         // 最小空闲实例个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));    // 在获取jedis实例时否要进行验证操作
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));    // 在归还jedis实例时否要进行验证操作

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

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

        pool = new JedisPool(config, redisIp, redisPort, 1000*2);
    }

    /**
     * 获取 Jedis 实例
     * @return Jedis
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 将连接归还连接池
     * @param jedis jedis连接
     */
    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * 将坏连接归还连接池
     * @param jedis jedis连接
     */
    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("niu", "niuvalue");
        returnResource(jedis);
        pool.destroy();  // 销毁连接池子的所有连接

        System.out.println("success:)");
    }
}
