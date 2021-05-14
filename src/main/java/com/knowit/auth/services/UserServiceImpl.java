package com.knowit.auth.services;

import com.knowit.auth.constants.KafkaConstants;
import com.knowit.auth.domain.entities.User;
import com.knowit.auth.domain.models.LoginRequestModel;
import com.knowit.auth.domain.models.RegisterUserModel;
import com.knowit.auth.domain.models.RegistrationRequestModel;
import com.knowit.auth.domain.models.RegistrationResponseModel;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;
import com.knowit.auth.jwt.JwtUtils;
import com.knowit.auth.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils utils;
    private final PasswordEncoder passwordEncoder;
    private final StreamBridge streamBridge;

    public UserServiceImpl(
            UserRepository userRepository,
            ModelMapper modelMapper,
            JwtUtils utils,
            PasswordEncoder passwordEncoder,
            StreamBridge streamBridge) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.utils = utils;
        this.passwordEncoder = passwordEncoder;
        this.streamBridge = streamBridge;
    }

    @Override
    public RegistrationResponseModel register(RegistrationRequestModel request) throws PasswordsDoNotMatchException {
        User user = new User();
        user.setUsername(request.getUsername());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        this.userRepository.saveAndFlush(user);

        RegisterUserModel userModel = this.modelMapper.map(request, RegisterUserModel.class);
        User sentUserId = this.userRepository.findFirstByUsername(request.getUsername());
        userModel.setId(sentUserId.getId());
        this.streamBridge.send(KafkaConstants.USER_PUBLISHER, userModel);

        return this.modelMapper.map(user, RegistrationResponseModel.class);
    }

    @Override
    public String loginUser(LoginRequestModel request) throws WrongCredentialsException {
        User user = this.userRepository.findFirstByUsername(request.getUsername());

        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword()) ||
                !request.getUsername().equals(user.getUsername())) {
            throw new WrongCredentialsException();
        }

        return this.utils.generateJwtToken(user);
    }

    @Bean
    public Supplier<Mono<RegisterUserModel>> publishUser() {
        return () -> Mono.just(new RegisterUserModel());
    }
}
