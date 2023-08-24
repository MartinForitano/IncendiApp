package org.unse.incendiapp.io.entidadesApi;

public class UsuarioApi {

    private String contrasenia;

    private String nombre;

    public UsuarioApi(String contrasenia, String nombre) {
        this.contrasenia = contrasenia;
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
