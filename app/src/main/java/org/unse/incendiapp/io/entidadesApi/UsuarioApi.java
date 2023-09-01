package org.unse.incendiapp.io.entidadesApi;

public class UsuarioApi {



    private String nombre;

    private String contrasenia;

    private Integer tipoUsuario;


    public UsuarioApi(String nombre, String contrasenia, Integer tipousuario) {
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.tipoUsuario = tipousuario;
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

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
