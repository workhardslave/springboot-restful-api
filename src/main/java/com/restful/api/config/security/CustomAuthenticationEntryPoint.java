package com.restful.api.config.security;

/*
 * JWT 토큰 없이 API를 호출한 경우,
 * 형식에 맞지 않거나 만료된 JWT 토큰으로 API를 호출한 경우 해결책
 *
 * 온전한 JWT가 전달이 안될 경우 토큰 인증 처리 자체가 불가능해서 토큰 검증 단에서 프로세스가 끝난다.
 * 해당 예외를 잡아내려면 스프링 시큐리티에서 제공하는 AuthenticationEntryPoint를 상속받아 재정의 해야한다.
 * 예외가 발생할 경우 /exception/entrypoint로 포워딩되도록 처리했다.
 *
 */

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/exception/entrypoint");
    }
}
