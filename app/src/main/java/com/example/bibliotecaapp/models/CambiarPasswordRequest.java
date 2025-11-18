package com.example.bibliotecaapp.models;

public class CambiarPasswordRequest {
    private String Email;
    private String PasswordActual;
    private String PasswordNueva;

    public CambiarPasswordRequest(String email, String passwordActual, String passwordNueva) {
        Email = email;
        PasswordActual = passwordActual;
        PasswordNueva = passwordNueva;
    }
}