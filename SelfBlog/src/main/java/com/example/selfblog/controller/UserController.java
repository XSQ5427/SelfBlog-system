package com.example.selfblog.controller;

import com.example.selfblog.entity.User;
import com.example.selfblog.pojo.UserLogin;
import com.example.selfblog.repository.SmsService;
import com.example.selfblog.repository.UserRepository;
import com.example.selfblog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    SmsService smsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @ResponseBody
    @PostMapping("chpw")
    public String setPasswordByVerifyCode(@RequestBody UserLogin userLogin){
        User user = UserUtil.getUser();
        if (smsService.checkVerifyCode(user.getPhoneNumber(),userLogin.getVerifyCode())){
            user.setPassword(passwordEncoder.encode(userLogin.getPassword()));
            userRepository.save(user);
            return "1";
        } else {
            return "0";
        }
    }
    @GetMapping
    public String userIndex(Map<String, Object> map){
        map.put("user",UserUtil.getUser());
        return "self_page";
    }
}
