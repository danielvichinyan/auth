package com.knowit.auth.domain.models;

import java.time.LocalDate;

public class RegistrationRequestModel {

    private String username;

    private String password;

    private String confirmPassword;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate bornOn;

    private int quizPoints;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getQuizPoints() {
        return quizPoints;
    }

    public void setQuizPoints(int quizPoints) {
        this.quizPoints = quizPoints;
    }
}
