package com.example.selfblog.service.impl;
import com.example.selfblog.config.security.TokenService;

import com.example.selfblog.entity.User;
import com.example.selfblog.repository.UserRepository;
import com.example.selfblog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    public String loginOrRegister(String phoneNumber, HttpServletResponse response) {
        User user = userRepository.findOneByPhoneNumber(phoneNumber);
        if (null == user) {
            user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setPassword("");
            long currentTimeStamp = System.currentTimeMillis();
            user.setUpdateTime(new Timestamp(currentTimeStamp));
            userRepository.save(user);
        }
        return updateSecurityUserInfo(user, response);
    }

    public String updateSecurityUserInfo(User user, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        String token = tokenService.generateToken(user);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }
    @Override
    public String loginByPW(String phoneNumber, String password, HttpServletResponse response){
        User user = userRepository.findOneByPhoneNumber(phoneNumber);
        if (null == user || !passwordEncoder.matches(password, user.getPassword())){
            return "0";
        }
        return updateSecurityUserInfo(user,response);
    }
}


