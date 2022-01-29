package com.devthink.devthink_server.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Error {
    HttpStatus status;
    String message;
}
