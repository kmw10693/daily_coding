package com.devthink.devthink_server.errors;

public class UserNickNameDuplicationException extends RuntimeException {
    public UserNickNameDuplicationException(String nickname) {
        super("User nickname is already exists : "+ nickname);
    }
}
