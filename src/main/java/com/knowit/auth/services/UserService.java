package com.knowit.auth.services;

import com.knowit.auth.domain.models.LoginRequest;
import com.knowit.auth.domain.models.RegisterRequest;
import com.knowit.auth.domain.models.RegisterResponse;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;

public interface UserService {

    RegisterResponse register(RegisterRequest request) throws PasswordsDoNotMatchException;

    String login(LoginRequest request) throws WrongCredentialsException;
}
