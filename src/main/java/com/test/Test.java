package com.test;

import com.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Author: XBlue
 * @Date: Create in 2018/5/615:07
 * @Description:
 * @Modified By:
 */
@Controller
public class Test {

    @RequestMapping("/aa.page")
    @RequiresPermissions({"user:aa"})
    public ModelAndView aa(){
        ModelAndView mv =new ModelAndView("aa");
        String result = (String) SecurityUtils.getSubject().getSession().getAttribute("cc");
        mv.addObject("name",(String)(SecurityUtils.getSubject().getPrincipal()));
        mv.addObject("result",result);
        return mv;
    }

    @RequestMapping(value = "/subLogin.json",method = RequestMethod.POST)
    @ResponseBody
    public String subLogin(User user){
        Subject subject = SecurityUtils.getSubject();
        //下面这个玩意可以用来解决验证码的问题
        SecurityUtils.getSubject().getSession().setAttribute("cc","jfd");
        //把下面的login放到业务层，返回User用户信息，然后把用户信息存到session中，这个session是shiro的session（由于我们用了redis这个session由redis管理）
        //这样才能对用户信息做及时更新
        UsernamePasswordToken token =new UsernamePasswordToken(user.getUserName(),user.getPassword());
        try{
            subject.login(token);
        }catch(IncorrectCredentialsException ice){
            // 捕获密码错误异常
            return ice.getMessage();
        }catch (UnknownAccountException uae) {
            // 捕获未知用户名异常
            return uae.getMessage();
        }catch (ExcessiveAttemptsException eae) {
            // 捕获错误登录过多的异常
            return eae.getMessage();
        }
        return "成功";
    }
    @RequestMapping(value = "bb.page")
    @RequiresPermissions({"user:bb"})
    public ModelAndView bb(){
        ModelAndView mv = new ModelAndView("bb");
        return mv;
    }

}
