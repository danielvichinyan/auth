package com.knowit.auth.services.user;

import com.knowit.auth.constants.KafkaConstants;
import com.knowit.auth.domain.entities.ERole;
import com.knowit.auth.domain.entities.Role;
import com.knowit.auth.domain.entities.User;
import com.knowit.auth.domain.models.*;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;
import com.knowit.auth.jwt.JwtUtils;
import com.knowit.auth.repositories.RoleRepository;
import com.knowit.auth.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils utils;
    private final PasswordEncoder passwordEncoder;
    private final StreamBridge streamBridge;
    private final RoleRepository roleRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            ModelMapper modelMapper,
            JwtUtils utils,
            PasswordEncoder passwordEncoder,
            StreamBridge streamBridge,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.utils = utils;
        this.passwordEncoder = passwordEncoder;
        this.streamBridge = streamBridge;
        this.roleRepository = roleRepository;
    }

    @Override
    public RegistrationResponseModel register(RegistrationRequestModel request) throws PasswordsDoNotMatchException {
        Set<Role> roles = new HashSet<>();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setQuizPoints(request.getQuizPoints());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        user.setPassword(this.passwordEncoder.encode(request.getPassword()));

        Role role = null;
        RegisterUserRequestModel registerUserRequestModel = new RegisterUserRequestModel();

        if (this.userRepository.count() == 0) {
            role = this.roleRepository.findByName(ERole.ADMIN);
            registerUserRequestModel.setRoleADMINId(role.getId());
            registerUserRequestModel.setRoleADMIN(role.getName().getAuthority());
        } else if (this.userRepository.count() == 1) {
            Role roleA = this.roleRepository.findByName(ERole.ADMIN);
            role = this.roleRepository.findByName(ERole.USER);
            registerUserRequestModel.setRoleADMINId(roleA.getId());
            registerUserRequestModel.setRoleADMIN(roleA.getName().getAuthority());
            registerUserRequestModel.setRoleUSERId(role.getId());
            registerUserRequestModel.setRoleUSER(role.getName().getAuthority());
            roles.add(role);
            roles.add(roleA);
        } else {
            role = this.roleRepository.findByName(ERole.USER);
            registerUserRequestModel.setRoleUSERId(role.getId());
            registerUserRequestModel.setRoleUSER(role.getName().getAuthority());
        }
        roles.add(role);

        if (role.getName() == ERole.ADMIN) {
            Role role2 = this.roleRepository.findByName(ERole.USER);
            Role role3 = this.roleRepository.findByName(ERole.EDITOR);

            registerUserRequestModel.setRoleEDITORId(role3.getId());
            registerUserRequestModel.setRoleEDITOR(role3.getName().getAuthority());

            registerUserRequestModel.setRoleUSERId(role2.getId());
            registerUserRequestModel.setRoleUSER(role2.getName().getAuthority());
            roles.add(role2);
            roles.add(role3);
        }

        user.setRoles(roles);
        this.userRepository.saveAndFlush(user);

        RegisterUserModel userModel = this.modelMapper.map(request, RegisterUserModel.class);
        User sentUserId = this.userRepository.findFirstByUsername(request.getUsername());
        userModel.setId(sentUserId.getId());
        registerUserRequestModel.setUserId(sentUserId.getId());

        this.streamBridge.send(KafkaConstants.USER_ID_AND_ROLE, registerUserRequestModel);
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

    @Bean
    public Supplier<Mono<RegisterUserRequestModel>> publishUserIdAndRole() {
        return () -> Mono.just(new RegisterUserRequestModel());
    }
}
