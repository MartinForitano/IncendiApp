package org.unse.incendiapp.Entidades;

public class Usuario {

    private Long id;
    private String nombre, contrasenia, token;


    public Usuario(){
        this.id = Long.valueOf(0);
        this.nombre = "";
        this.contrasenia = "";
        this.token = "";
    }

    public Usuario(Long id, String nombre, String contrasenia, String token) {
        this.id = id;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
