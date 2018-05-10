package com.cache;

import com.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/5/716:36
 * @Description:
 * @Modified By:
 */
@Component
@Slf4j
public class RedisCache<K,V> implements Cache<K,V> {

    @Resource
    private JedisUtils jedisUtils;

    private static final String CACHE_PREFIX = "premission_cache_";

    private byte[] getKey(K k){
        if(k instanceof String){
            return (CACHE_PREFIX+k).getBytes();
        }else{
            return (CACHE_PREFIX+k.toString()).getBytes();
        }
    }
    private byte[] getKey(K k,String prefix){
        if(k instanceof String){
            return (prefix+k).getBytes();
        }else{
            return (prefix+k.toString()).getBytes();
        }
    }

    @Override
    public V get(K k) throws CacheException {
        log.info("从缓存中获取授权数据");
        //在这个地方其实还可以加上本地的二级缓存，如果本地有没必要跑到redis中去取，进一步提升效率
        byte[] value = jedisUtils.get(getKey(k));
        if(value!=null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtils.set(key,value);
        jedisUtils.expire(key,600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtils.get(key);
        jedisUtils.del(key);
        if(value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    public V get(K k,String prefix) throws CacheException {
        log.info("从缓存中获取授权数据");
        //在这个地方其实还可以加上本地的二级缓存，如果本地有没必要跑到redis中去取，进一步提升效率
        byte[] value = jedisUtils.get(getKey(k,prefix));
        if(value!=null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    public V put(K k, V v,String prefix) throws CacheException {
        byte[] key = getKey(k,prefix);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtils.set(key,value);
        jedisUtils.expire(key,600);
        return v;
    }

    public V remove(K k,String prefix) throws CacheException {
        byte[] key = getKey(k,prefix);
        byte[] value = jedisUtils.get(key);
        jedisUtils.del(key);
        if(value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
