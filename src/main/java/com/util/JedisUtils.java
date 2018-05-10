package com.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/5/710:42
 * @Description:
 * @Modified By:
 */
@Component
public class JedisUtils {

    @Resource
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void close(Jedis jedis) {
        jedis.close();
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getJedis();
        try{
            jedis.set(key,value);
            return value;
        }finally {
            close(jedis);
        }
    }

    public void expire(byte[]key,int time) {
        Jedis jedis = getJedis();
        try{
            jedis.expire(key,time);
        }finally {
            close(jedis);
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getJedis();
        try{
            return jedis.get(key);
        }finally {
            close(jedis);
        }
    }

    public void del(byte[] key) {
        Jedis jedis = getJedis();
        try{
            jedis.del(key);
        }finally {
            close(jedis);
        }
    }

    public Set<byte[]> keys(String shiroSessionPrefix) {
        Jedis jedis = getJedis();
        try{
            return jedis.keys((shiroSessionPrefix+"*").getBytes());
        }finally {
            close(jedis);
        }
    }

}
