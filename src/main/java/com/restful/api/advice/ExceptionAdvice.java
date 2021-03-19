package com.restful.api.advice;

import com.restful.api.advice.exception.CUserNotFoundException;
import com.restful.api.response.CommonResult;
import com.restful.api.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


/*
 * @RestControllerAdvice
 * ControllerAdvice의 어노테이션은 @ControllerAdvice, @RestControllerAdvice 두가지가 있다.
 * 예외 발생 시, JSON 형태로 결과를 반환하려면 후자를 클래스에 선언한다.
 * @RestControllerAdvice(basePackages = "") 옵션을 주면 특정 패키지의 하위에만 로직이 적용된다.
 * 디폴트는 프로젝트 전체에 적용된다.
 *
 * @ExceptionHandler
 * 익셉션이 발생하면 해당 핸들러로 처리하겠다고 명시하는 어노테이션이다.
 * 괄호안에 어떤 익셉션이 발생할 때 핸들러를 적용할 것인지 클래스를 인자로 넣는다.
 * Exception.class는 최상위 예외처리 객체이다.
 *
 * @ResponseStatus
 * 해당 익셉션이 발생하면 response에 출력되는 Http 상태 코드를 설정할 수 있다.
 * 상태 코드는 성공이냐 아니냐 정도의 의미만 있고, 실제 사용하는 성공 실패 여부는 JSON으로 출력되는 정보를 이용한다.
 */

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.message"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.message"));
    }

    // code 정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
