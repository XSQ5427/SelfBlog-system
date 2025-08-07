package com.example.selfblog.controller;

import com.example.selfblog.pojo.UserLogin;
import com.example.selfblog.repository.SmsService;
import com.example.selfblog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    SmsService smsService;
    @Autowired
    LoginService loginService;
    @PutMapping("sms")
    public String sendSms(@RequestBody UserLogin userLogin) {
        String verifyCode = smsService.genVerifyCode(userLogin.getPhoneNumber());
        return "1";
    }
    @PostMapping("byvc")
    public String loginByVerifyCode(@RequestBody UserLogin userLogin, HttpServletResponse response) {
        boolean flag = smsService.checkVerifyCode(userLogin.getPhoneNumber(), userLogin.getVerifyCode());
        if (flag) {
            return loginService.loginOrRegister(userLogin.getPhoneNumber(), response);
        } else {
            return "0";
        }
    }
    @PostMapping("bypw")
    public String loginByPassword(@RequestBody UserLogin userLogin,HttpServletResponse response){
        return loginService.loginByPW(userLogin.getPhoneNumber(),userLogin.getPassword(),response);
    }
}