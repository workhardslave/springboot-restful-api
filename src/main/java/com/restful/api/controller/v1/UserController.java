package com.restful.api.controller.v1;

import com.restful.api.advice.exception.CUserNotFoundException;
import com.restful.api.entity.User;
import com.restful.api.repository.UserRepository;
import com.restful.api.response.CommonResult;
import com.restful.api.response.ListResult;
import com.restful.api.response.ResponseService;
import com.restful.api.response.SingleResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @ApiImplicitParam(name = "", value = "", required = false, dataType = "", paramType ="")
 * 파라미터에 대한 자세한 정보를 추가한다.
 *
 */

//@Secured("ROLE_USER")
//@PreAuthorize("hasRole('ADMIN_USER')")
@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
@RestController // 결과값을 JSON으로 출력한다.
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        System.out.println(userRepository.findAll());

        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다.")
    @GetMapping(value = "/users/{id}")
    public SingleResult<User> findById(@ApiParam(value = "회원ID", required = true) @PathVariable Long id,
                                       @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        System.out.println(userRepository.findById(id));

        return responseService.getSingleResult(userRepository.findById(id).orElseThrow(CUserNotFoundException::new));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "userId로 회원정보를 수정한다.")
    @PutMapping(value = "/users/{id}")
    public SingleResult<User> update(@ApiParam(value = "회원ID", required = true) @PathVariable Long id,
                                     @ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
                                     @ApiParam(value = "회원이름", required = true) @RequestParam String username) {
        User user = User.builder()
                .id(id)
                .uid(uid)
                .username(username)
                .build();

        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원를 삭제한다.")
    @DeleteMapping(value = "/users/{id}")
    public CommonResult delete(@ApiParam(value = "회원ID", required = true) @PathVariable Long id) {
        userRepository.deleteById(id);

        return responseService.getSuccessResult();
    }

}