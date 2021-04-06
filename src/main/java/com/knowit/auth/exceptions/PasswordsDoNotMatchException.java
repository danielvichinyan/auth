package com.knowit.auth.exceptions;

import com.knowit.auth.constants.ExceptionConstants;

public class PasswordsDoNotMatchException extends Exception{

    public PasswordsDoNotMatchException() {
        super(ExceptionConstants.PASSWORDS_DO_NOT_MATCH);
    }
}
