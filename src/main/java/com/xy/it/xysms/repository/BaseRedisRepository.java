package com.xy.it.xysms.repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis仓储抽象类
 * Created by liamjung on 2018/1/22.
 */
public abstract class BaseRedisRepository {

    private final JedisPool JEDIS_POOL;

    protected BaseRedisRepository(String redisHost, String redisPass, int redisDb) {

        String[] strArr = redisHost.split(":");
        String ip = strArr[0];
        int port = Integer.valueOf(strArr[1]);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //The maximum number of active connections
        //that can be allocated from this pool at the same time, or negative for no limit.
        // 最大总jedis连接
        jedisPoolConfig.setMaxTotal(1000);
        //The maximum number of connections
        //that can remain idle in the pool, without extra ones being released, or negative for no limit.
        // 最大空闲jedis连接
        jedisPoolConfig.setMaxIdle(20);
        //The minimum number of connections
        //that can remain idle in the pool, without extra ones being created, or zero to create none.
        // 最小空闲jedis连接
        jedisPoolConfig.setMinIdle(1);
        //The maximum number of milliseconds
        //that the pool will wait (when there are no available connections)
        //for a connection to be returned before throwing an exception, or -1 to wait indefinitely.
        // 阻塞等待jedis可用连接的最大等待时间
        jedisPoolConfig.setMaxWaitMillis(5000L);
        //向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取
        jedisPoolConfig.setTestOnBorrow(true);
        //向连接池“归还”链接时，是否检测“链接”对象的有效性
        jedisPoolConfig.setTestOnReturn(false);
        //for JMX
        jedisPoolConfig.setJmxEnabled(true);

        JEDIS_POOL = new JedisPool(
                jedisPoolConfig,
                ip,
                port,
                4000,
                redisPass,
                redisDb
//                env.getProperty("redis.index", int.class, 1)
        );

        //应用关闭时，执行
        //关闭连接池
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                //为兼容java8之前的版本，采用非lambdas
                super.run();

                JEDIS_POOL.destroy();
            }
        });
    }

    /**
     * 获取连接
     *
     * @return
     */
    protected Jedis getConnection() {

        return JEDIS_POOL.getResource();
    }

    /**
     * 关闭连接
     *
     * @param jedis
     */
    protected void closeConnection(Jedis jedis) {

        if (jedis != null)
            JEDIS_POOL.returnResourceObject(jedis);
    }
}
