package com.restful.api.response;

// enum으로 API 요청 결과에 대한 code, message를 정의한다.
public enum CommonResponse {
    SUCCESS(0, "성공했습니다."),
    FAIL(-1, "실패했습니다.");

    int code;
    String message;

    CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}