package com.realm;

import com.pojo.User;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        if(!username.equals(user.getUserName())){
            throw new UnknownAccountException("用户名错误！");
        }
//        一般密码是从数据库查询出来
//        if(!password.equals(user.getPassword())){
//            throw new IncorrectCredentialsException("密码错误！！！");
//        }
        //返回认证信息，在返回的里面进行进行密码的比对,你只要传数据库的密文就行了，用户输入的在token中它自己会找，只有通过这样才能使内置加密算法生效，上面的不行

        return new SimpleAuthenticationInfo(user, // 用户名
                user.getPassword(), // 密码
                "CustomRealm" // realm name
        );
    }



//授权
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        log.info("从数据库中获取授权数据");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String>permissionList = new ArrayList<String>();
        permissionList.add("user:aa");
        authorizationInfo.addStringPermissions(permissionList);
        return authorizationInfo;
    }
}
