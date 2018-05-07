package com.redis;

import com.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/5/710:40
 * @Description:
 * @Modified By:
 */
@Slf4j
@Component
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtils jedisUtils;

    private static final String SHIRO_SESSION_PREFIX = "shiro_session_";

    private byte[] getKey(String key){
        return (SHIRO_SESSION_PREFIX+key).getBytes();
    }
    @Override
    protected Serializable doCreate(Session session) {
        if(session==null)return null;
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        byte[]key = getKey(session.getId().toString());
        byte[]value = SerializationUtils.serialize(session);
        jedisUtils.set(key,value);
        jedisUtils.expire(key,600);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.info("看看跑几次");
        if(sessionId == null){
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtils.get(key);
        if(value==null){
            return null;
        }
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if(session != null && session.getId() != null){
            byte[]key = getKey(session.getId().toString());
            byte[]value = SerializationUtils.serialize(session);
            jedisUtils.set(key,value);
            jedisUtils.expire(key,600);
        }
    }

    @Override
    public void delete(Session session) {
        if(session==null||session.getId()==null){
            return;
        }
        byte[]key =getKey(session.getId().toString());
        jedisUtils.del(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]>keys= jedisUtils.keys(SHIRO_SESSION_PREFIX);
        Set<Session>sessions = new HashSet<>();
        if(CollectionUtils.isEmpty(keys)){
            return sessions;
        }
        for(byte[] key: keys){
            Session session = (Session) SerializationUtils.deserialize(jedisUtils.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
