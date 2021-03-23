package com.restful.api.controller.v1;

import com.restful.api.advice.exception.CEmailSigninFailedException;
import com.restful.api.config.security.JwtTokenProvider;
import com.restful.api.entity.User;
import com.restful.api.repository.UserRepository;
import com.restful.api.response.CommonResult;
import com.restful.api.response.ResponseService;
import com.restful.api.response.SingleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
@RestController
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String uid,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password)
    {
        User user = userRepository.findByUid(uid).orElseThrow(CEmailSigninFailedException::new);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CEmailSigninFailedException();
        }

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
    }

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String uid,
                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                               @ApiParam(value = "이름", required = true) @RequestParam String uname)
    {
        User user = userRepository.save(User.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .uname(uname)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.getUid() = " + user.getUid());
        System.out.println("user.getPassword() = " + user.getPassword());
        System.out.println("user.getUsername() = " + user.getUsername());
        System.out.println("user.getRoles().get(0) = " + user.getRoles().get(0));


        return responseService.getSuccessResult();
    }
}
