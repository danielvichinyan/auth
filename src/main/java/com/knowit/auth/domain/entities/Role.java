package com.knowit.auth.domain.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private ERole name;

    private Set<User> users;

    @Enumerated(EnumType.STRING)
    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}