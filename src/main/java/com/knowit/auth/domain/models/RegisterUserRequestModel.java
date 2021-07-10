package com.knowit.auth.domain.models;

public class RegisterUserRequestModel {

    private String userId;

    private String roleUSERId;

    private String roleUSER;

    private String roleADMINId;

    private String roleADMIN;

    private String roleEDITORId;

    private String roleEDITOR;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleUSERId() {
        return roleUSERId;
    }

    public void setRoleUSERId(String roleUSERId) {
        this.roleUSERId = roleUSERId;
    }

    public String getRoleUSER() {
        return roleUSER;
    }

    public void setRoleUSER(String roleUSER) {
        this.roleUSER = roleUSER;
    }

    public String getRoleADMINId() {
        return roleADMINId;
    }

    public void setRoleADMINId(String roleADMINId) {
        this.roleADMINId = roleADMINId;
    }

    public String getRoleADMIN() {
        return roleADMIN;
    }

    public void setRoleADMIN(String roleADMIN) {
        this.roleADMIN = roleADMIN;
    }

    public String getRoleEDITORId() {
        return roleEDITORId;
    }

    public void setRoleEDITORId(String roleEDITORId) {
        this.roleEDITORId = roleEDITORId;
    }

    public String getRoleEDITOR() {
        return roleEDITOR;
    }

    public void setRoleEDITOR(String roleEDITOR) {
        this.roleEDITOR = roleEDITOR;
    }
}
