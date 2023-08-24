package org.unse.incendiapp.io.entidadesApi;

public class loginDatosResponde {

    private String status_code, token;

    public loginDatosResponde(String status_code, String token) {
        token = token;
        this.status_code = status_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        token = token;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String response) {
        this.status_code = response;
    }
}
