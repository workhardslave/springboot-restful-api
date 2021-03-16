package com.restful.api.controller.v1;

import com.restful.api.entity.User;
import com.restful.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * @RequiredArgsConstructor
 * 클래스 내부에 final로 선언된 객체에 대해서 의존관계 주입을 수행한다.
 * @Autowired와 똑같은 기능이다.
 *
 * @RestController
 * 결과 데이터를 JSON으로 반환
 * @Controller + @ResponseBody
 *
 * @RequestMapping(value = "/v1")
 * api를 버전별로 관리하기 위해 공통된 리소스를 묶는다.
 *
 */

@RequiredArgsConstructor
@RequestMapping(value = "/v1")
@RestController // 결과값을 JSON으로 출력합니다.
public class UserController {
    private final UserRepository userRepository;

    @GetMapping(value = "/users")
    public List<User> findAllUser() {
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    @PostMapping(value = "/users")
    public User save() {
        User user = User.builder()
                .uid("yumi@naver.com")
                .username("유미")
                .build();
        return userRepository.save(user);
    }
}
