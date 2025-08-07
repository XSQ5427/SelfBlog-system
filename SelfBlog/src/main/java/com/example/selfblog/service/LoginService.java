package com.example.selfblog.service;

import javax.servlet.http.HttpServletResponse;
public interface LoginService {
    String loginOrRegister(String phoneNumber, HttpServletResponse response);

    String loginByPW(String phoneNumber, String password, HttpServletResponse response);
}
