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
 * 회원가입
 * 1. JwtAuthenticationFilter-JwtTokenProvider.resolveToken
 * 2. 토큰이 없으므로 바로 다음 필터를 탄다.
 *
 * 로그인
 * 1. JwtAuthenticationFilter-JwtTokenProvider.resolveToken
 * 2. 토큰이 없으므로 바로 다음 필터를 탄다.
 * 3. JwtTokenProvider.createToken 토큰 생성
 * 4. 토큰을 클라이언트에 전달
 *
 * 토큰을 이용한 데이터 요청
 * 1. JwtAuthenticationFilter-JwtTokenProvider.resolveToken
 * 2. 토큰이 존재하므로, JwtAuthenticationFilter-JJwtTokenProvider.validateToken 토큰 유효성 검사
 * 3. 토큰이 유효하다면, JwtTokenProvider.getAuthentication 토큰을 통해 인증 정보 조회
 * 4. JwtTokenProvider.getUser Pk토큰에서 회원 구별 정보(여기선 PK) 정보 추출
 * 5. 가져온 인증정보를 SecurityContextHolder에 강제로 저장
 *
 */

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Value("spring.jwt.secret")
    private String secretKey;

    private Long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성 (로그인 요청)
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    // Jwt 토큰으로 인증 정보를 조회 (토큰으로 데이터를 얻어올 때)
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출 (토큰으로 데이터를 얻어올 때)
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰" (토큰을 이용한 모든 요청)
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인 (토큰으로 데이터 얻어올 때)
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}