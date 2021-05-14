package com.knowit.auth.services;

import com.knowit.auth.domain.models.LoginRequestModel;
import com.knowit.auth.domain.models.RegistrationRequestModel;
import com.knowit.auth.domain.models.RegistrationResponseModel;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;

public interface UserService {

    RegistrationResponseModel register(RegistrationRequestModel request) throws PasswordsDoNotMatchException;

    String loginUser(LoginRequestModel request) throws WrongCredentialsException;
}
