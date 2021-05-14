package com.knowit.auth.domain.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegisterUserModel {

    @NotBlank
    private String id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private LocalDate bornOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBornOn() {
        return bornOn;
    }

    public void setBornOn(LocalDate bornOn) {
        this.bornOn = bornOn;
    }
}
