package com.example.bibliotecaapp.models;

public class LoginResponse {
    private String token;
    private int userId;
    private String nombre;
    private String email;
    private String rol;

    public String getToken() { return token; }
    public int getUserId() { return userId; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}