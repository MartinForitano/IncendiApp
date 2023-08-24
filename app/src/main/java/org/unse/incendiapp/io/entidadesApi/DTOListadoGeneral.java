package org.unse.incendiapp.io.entidadesApi;

import java.util.List;

public class DTOListadoGeneral {

    private String response_status;

    private List<DTOEventoResponse> listaEventos;

    public DTOListadoGeneral(String response_status, List<DTOEventoResponse> listaEventos) {
        this.response_status = response_status;
        this.listaEventos = listaEventos;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public List<DTOEventoResponse> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(List<DTOEventoResponse> listaEventos) {
        this.listaEventos = listaEventos;
    }
}
