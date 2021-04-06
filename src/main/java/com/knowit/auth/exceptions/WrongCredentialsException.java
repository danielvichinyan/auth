package com.knowit.auth.exceptions;

import com.knowit.auth.constants.ExceptionConstants;

public class WrongCredentialsException extends Exception {

    public WrongCredentialsException() {
        super(ExceptionConstants.WRONG_CREDENTIALS);
    }
}
