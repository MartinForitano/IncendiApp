package org.unse.incendiapp.io.entidadesApi;

public class loginDatosEnvia {
    private String nombre;

    private String contrasenia;

    public loginDatosEnvia(String nombre, String contrasenia) {
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

