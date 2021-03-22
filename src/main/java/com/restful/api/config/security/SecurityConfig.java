package com.restful.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * 리소스 접근 제한 표현식
 * hasIpAddress(ip) : 접근자의 IP주소가 매칭 하는지 확인한다.
 * hasRole(role) : 역할이 부여된 권한(Granted Authority)과 일치하는지 확인한다.
 * hasAnyRole(role) : 부여된 역할 중 일치하는 항목이 있는지 확인한다.
 * ex) access = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')"
 * permitAll : 모든 접근자를 항상 승인한다.
 * denyAll : 모든 사용자의 접근을 거부한다.
 * anonymous : 사용자가 익명 사용자인지 확인한다.
 * authenticated : 인증된 사용자인지 확인한다.
 * rememberMe : 사용자가 remember me를 사용해 인증했는지 확인한다.
 * fullyAuthenticated : 사용자가 모든 크리덴셜을 갖춘 상태에서 인증했는지 확인한다.
 *
 * 어노테이션으로 리소스 접근 권한 설정
 * configure(HttpSecurity http) 메서드 내부에서 authorizedRequest()를 통한 접근 권한 설정은 리소스의 권한을 중앙관리한다는 장점이 있다.
 * 다른 방법으로 어노테이션을 이용한 리소스 접근 권한 설정 방법도 존재한다. (@PreAuthorize, @Secured)
 * 어노테이션을 사용하기 위해서 @GlobalMethodSecurity를 활성해야한다.
 * 그리고 기존의 configure 메서드 안에서 authorizeRequest() 설정은 주석 처리한다.
 * 각각의 리소스마다 권한 설정을 해야한다면 해당 메서드 위에 어노테이션을 세팅한다.
 *
 * @PreAuthorize
 * 표현식 사용 가능
 * ex) @PreAuthorize("hasRole('ROLE_USER') and hasRole('ROLE_ADMIN')")
 *
 * @Secured
 * 표현식 사용 불가능
 * ex) @Secured({"ROLE_USER", "ROLE_ADMIN"})
 */

//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
                .and()
                    .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                        .antMatchers("/*/signin", "/*/signup").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                        .antMatchers(HttpMethod.GET, "/helloworld/**", "/exception/**").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
                    .antMatchers("/*/users").hasRole("ADMIN")
                    .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣는다

    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");

    }
}
