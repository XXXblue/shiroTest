package com.realm;

import com.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/5/615:26
 * @Description:
 * @Modified By:
 */
public class CustomRealm extends AuthorizingRealm{

//认证
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials());
        //      切记这里不能拿密码 ，要从数据库通过前面的用户名来获取密码
        //       如果数据库查询不到密码直接返回null,这里直接new模拟已经根据用户名找到找到
        User user =new User();
        user.setUserName("xjy");
        user.setPassword("123456");
        if(!username.equals(user.getUserName())){
            throw new UnknownAccountException("用户名错误！");
        }
        if(!password.equals(user.getPassword())){
            throw new IncorrectCredentialsException("密码错误！！！");
        }
        //返回认证信息
        return new SimpleAuthenticationInfo(user.getUserName(), // 用户名
                user.getPassword(), // 密码
                "CustomRealm" // realm name
        );
    }



//授权
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        //在这里获取的你的拦截信息，这里用缓存可以很大的提高效率，还在钻研中。。。
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String>permissionList = new ArrayList<String>();
        permissionList.add("user:aa");
        authorizationInfo.addStringPermissions(permissionList);
        return authorizationInfo;
    }
}
