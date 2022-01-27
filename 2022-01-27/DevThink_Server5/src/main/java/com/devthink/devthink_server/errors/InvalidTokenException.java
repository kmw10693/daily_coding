package com.devthink.devthink_server.errors;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String accessToken) {
        super("Invalid Access Token : "+accessToken );
    }
}
