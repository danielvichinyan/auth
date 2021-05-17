package com.knowit.auth.repositories;

import com.knowit.auth.domain.entities.ERole;
import com.knowit.auth.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByName(ERole name);
}

