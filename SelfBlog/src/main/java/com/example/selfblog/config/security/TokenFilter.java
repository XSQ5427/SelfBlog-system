package com.example.selfblog.config.security;

import com.example.selfblog.entity.User;
import com.example.selfblog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;
@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {
    String token = resolveToken(request);

    if(token!=null && !"".equals(token.trim())) {
        Integer userId = tokenService.getUserIdFromToken(token);

        if (null != userId && null == SecurityContextHolder.getContext().getAuthentication()) {
            User user = null;
            try {
                user = userRepository.getById(userId);
            } catch (JpaObjectRetrievalFailureException e) {
                log.error("token中的userId无效" + userId, e);
                throw new RuntimeException("token中的userId无效" + userId);
            }
            if (user != null && tokenService.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                log.error("登录状态失效，请重新登陆。");
                throw new RuntimeException("登录状态失效，请重新登陆。");
            }
        }
    }
    filterChain.doFilter(request, response);
}
    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}