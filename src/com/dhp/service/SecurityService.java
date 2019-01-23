package com.dhp.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String cpf, String senha);
}
