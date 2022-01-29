package com.devthink.devthink_server.errors;

public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String eamil) {
        super("이메일을 찾을 수 없습니다"+eamil);
    }
}
