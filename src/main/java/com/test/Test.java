package com.test;

import com.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
        return mv;
    }

    @RequestMapping(value = "/subLogin.json",method = RequestMethod.POST)
    @ResponseBody
    public String subLogin(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token =new UsernamePasswordToken(user.getUserName(),user.getPassword());
        try{
            subject.login(token);
        }catch(AuthenticationException e){
            return e.getMessage();
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
