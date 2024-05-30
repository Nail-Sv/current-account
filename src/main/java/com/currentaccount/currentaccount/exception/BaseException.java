package com.currentaccount.currentaccount.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This is a general service exception that can be used as a basement for more specific ones
 */
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class BaseException extends RuntimeException {

    private final String message;
}
