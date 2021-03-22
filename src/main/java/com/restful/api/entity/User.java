package com.restful.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String username;

    /*
     * 스프링 시큐리티의 보안 적용을 위해서 User 엔티티에 UserDetails 클래스를 구현해 추가 정보를 재정의한다.
     * roles는 회원이 가지고 있는 권한 정보이고, 가입했을 때는 기본 "ROLE_USER"로 세팅된다.
     * 권한은 회원당 여러 개가 세팅될 수 있으므로 Collection으로 선언한다.
     * getUsername()은 시큐리티에서 사용하는 회원 구분 id이다. => uid로 변경해준다.
     *
     * @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
     * 해당 데이터를 JSON 결과로 출력하지 않게한다.
     *
     * @ElementCollection
     * 컬렉션 객체이다.
     * @Entity가 아닌 기본 타입이나 임데디드 클래스로 정의된 컬렉션을 테이블로 생성하여 One To Many 관계를 다룬다.
     *
     * @Builder.Default
     * @Builder를 사용할 때 기본값으로 null이 들어가는데, 이를 통해 기본값을 설정한다.
     *
     * isAccountNonExpired() - 계정이 만료가 안되었는지
     * isAccountNonLocked() - 계정이 잠기지 안되었는지
     * isCredentialsNonExpired() - 계정 패스워드가 만료 안되었는지
     * isEnabled() - 계정이 사용 가능한지
     */

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
