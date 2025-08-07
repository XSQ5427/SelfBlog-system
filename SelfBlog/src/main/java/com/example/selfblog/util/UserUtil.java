package com.example.selfblog.util;

import com.example.selfblog.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class UserUtil {
    public static User getUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            return null;
        }
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
    }
}
