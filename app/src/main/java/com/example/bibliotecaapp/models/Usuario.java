package com.example.bibliotecaapp.models;

import java.io.Serializable;



import com.google.gson.annotations.SerializedName;

public class Usuario implements Serializable {
    private String id;
    private String nombre;
    private String email;
    private String rol;

    @SerializedName("password")
    private String passwordHash;

    private String passwordSalt;
    public Usuario(String email, String id, String passwordHash, String nombre, String passwordSalt, String rol) {
        this.email = email;
        this.id = id;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.passwordSalt = passwordSalt;
        this.rol = rol;
    }

    public Usuario(String nombre, String email, String passwordHash) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Usuario() { }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }
}
