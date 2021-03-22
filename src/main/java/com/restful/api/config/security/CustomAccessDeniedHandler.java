package com.restful.api.config.security;

/*
 * JWT 토큰으로 API를 호출하였으나 해당 리소스에 대한 권한이 없는 경우 해결책
 *
 * JWT 토큰은 정상이라는 가정하에 토큰이 가지지 못한 권한의 리소스를 접근할 때 발생하는 오류이다.
 * 이 경우 스프링 시큐리티에서 제공하는 AccessDeniedHandler를 상속받아 커스터마이징 해야한다.
 * 예외가 발생할 경우 handler에서 /exception/accessdenied로 포워딩되도록 처리했다.
 *
 */

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException,
            ServletException {
        response.sendRedirect("/exception/accessdenied");
    }
}
