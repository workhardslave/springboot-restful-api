package com.restful.api.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * @ResponseBody
 * 결과를 응답에 그대로 출력(data로 출력)
 * 해당 어노테이션을 지정하지 않으면, 리턴에 지정된 이름을 가진 뷰를 프로젝트에서 찾아 화면에 출력
 *
 */

@Controller
public class HelloController {
    /*
    1. 화면에 helloworld가 출력됩니다.
    */
    @GetMapping(value = "/helloworld/string") // value는 생략 가능
    @ResponseBody
    public String helloworldString() {
        return "helloworld";
    }
    /*
    2. 화면에 {message:"helloworld"} 라고 출력됩니다.
    */
    @GetMapping(value = "/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloworld";
        return hello;
    }
    /*
    3. 화면에 helloworld.ftl의 내용이 출력됩니다.
    */
    @GetMapping(value = "/helloworld/page")
    public String helloworld() {
        return "helloworld";
    }

    @Setter
    @Getter
    public static class Hello {
        private String message;
    }
}
