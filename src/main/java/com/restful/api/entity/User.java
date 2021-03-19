package com.restful.api.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Builder // 빌더 패턴을 사용한다.
@Data // @Getter, @Setter, @ToString 등이 들어있다.
@NoArgsConstructor // 기본 생성자를 자동으로 생성한다.
@AllArgsConstructor // 인자를 모두 가진 생성자를 자동으로 생성한다.
@Table(name = "user") // 'user' 테이블과 매핑됨을 명시
@Entity // JPA Entity임을 명시한다.
public class User implements UserDetails {

    @Id // Primary Key임을 명시한다.
    /* 기본키 자동생성 전략
     * AUTO : 특정 데이터베이스에 맞게 자동으로 생성되는 방식이다.
     * IDENTITY : 기본 키 생성 방식 자체를 데이터베이스에 위임하는 방식, 데이터베이스에 의존적이다. (주로 MySQL에서 사용)
     * SEQUENCE : 데이터베이스의 시퀀스를 이용해 기본 키를 생성 (주로 오라클에서 사용)
     * TABLE : 별도의 키를 생성해주는 테이블(번호를 취할 목적으로 만들어진)을 이용하는 방식
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String uid;

    @Column(nullable = false, length = 100)
    private String username;

    /*
     * 스프링 시큐리티의 보안 적용을 위해서 User 엔티티에 UserDetails 클래스를 구현해 추가 정보를 재정의한다.
     * roles는 회원이 가지고 있는

     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
