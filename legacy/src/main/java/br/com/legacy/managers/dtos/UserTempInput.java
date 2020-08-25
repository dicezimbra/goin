package br.com.legacy.managers.dtos;

public class UserTempInput {
    public String email;
    public String password;

    public UserTempInput(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
