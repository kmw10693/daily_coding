package com.devthink.devthink_server.errors;

public class UserRoomNotFoundException extends RuntimeException {
    public UserRoomNotFoundException(Long id) {
        super("UserRoom not found: " + id);
    }
}
