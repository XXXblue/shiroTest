package com.ManagerExtends;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 *这个东西是修改源码，主要是来解决多次访问RedisSessionDao的问题，这是一种解决方式就是把session捆绑在request中
 * 还有一种解决方式是过滤掉静态资源
 */

public class CustomSessionManager extends DefaultWebSessionManager {
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if(sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            Session session =  (Session) request.getAttribute(sessionId.toString());
            if(session!=null){
                return session;
            }
        }
        //就是因为下面这段代码导致多次利用，所以在这段代码之前我们要进行一下处理，减少重复次数
        Session session = super.retrieveSession(sessionKey);
        if(request != null && sessionId != null){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }
}
