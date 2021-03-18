package com.restful.api.controller.v1;

import com.restful.api.entity.User;
import com.restful.api.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
 * @RequestMapping(value = "")
 * api를 버전별로 관리하기 위해 공통된 리소스를 묶는다.
 *
 * @Api(tags = {""})
 * UserController를 대표하는 최상단 타이틀 영역에 표시될 값을 세팅힌다.
 *
 * @ApiOperation(value = "", notes = "")
 * 각각의 리소스에 제목과 설명을 표시하기위해 세팅한다
 *
 * @ApiParam(value = "", required = "") @RequestParam ~~~
 * 파라미터에 대한 설명을 보여주기 위해 세팅한다.
 *
 */

@Api(tags = {"1. User"})
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
@RestController // 결과값을 JSON으로 출력한다.
public class UserController {
    private final UserRepository userRepository;

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public List<User> findAllUser() {
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    @ApiOperation(value = "회원 등록", notes = "회원을 등록한다.")
    @PostMapping(value = "/users")
    public User save(@ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
                     @ApiParam(value = "회원이름", required = true) @RequestParam String username) {
        User user = User.builder()
                .uid(uid)
                .username(username)
                .build();
        return userRepository.save(user);
    }
}
