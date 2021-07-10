package com.knowit.auth.services.role;

import com.knowit.auth.domain.entities.ERole;
import com.knowit.auth.domain.entities.Role;
import com.knowit.auth.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @PostConstruct
    public void seedRoles() {
        List<ERole> roles = new ArrayList<>();
        if (this.roleRepository.count() == 0) {
            roles.add(ERole.ADMIN);
            roles.add(ERole.USER);
            roles.add(ERole.EDITOR);
            roles.forEach(e -> {
                Role role = new Role();
                role.setName(e);
                this.roleRepository.saveAndFlush(role);
            });
        }
    }
}