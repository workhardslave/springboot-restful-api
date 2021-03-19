package com.restful.api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/*
 * JWT 토큰 생성 및 유효성 검사를 하는 컴포넌트이다.
 * JWT는 여러가지 암호화 알고리즘을 제공하며, 알고리즘과 비밀 키를 가지고 토큰을 생성한다.
 * claim 정보에는 토큰에 부가적으로 실어보낼 정보를 세팅할 수 있다.
 * 보통 claim 정보에 회원을 구분할 수 있는 값을 세팅하고, 토큰이 들어오면 해당 값으로 회원을 구분하여 리소르를 제공한다.
 * 그리고 JWT는 토큰 만료 시간을 세팅하여 발급 후 일정 시간에 만료시킬 수 있다.
 *

 */

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Value("spring.jwt.secret")
    private String secretKey;

    private Long tokenValidMillisecond = 1000L * 60 * 60; // 1시간동안 토큰 유효

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        System.out.println("초기화");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond)) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리, 비밀 키
                .compact();
    }

    // JWT 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // JWT 토큰에서 회원 구별 정보를 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request 헤더에서 토큰 파싱 : "X-AUTH-TOKEN: JWT 토큰"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // JWT 토큰의 유효성 + 만료 시간 확인인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
