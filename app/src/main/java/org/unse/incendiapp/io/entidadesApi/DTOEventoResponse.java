package org.unse.incendiapp.io.entidadesApi;

import java.util.Date;

public class DTOEventoResponse {


    private Long id;
    private String tipo;
    private Integer cantVictimas;
    private String[] autoridades;
    private String areaInfluencia;
    private String ubicacionEvento;
    private Long tiempoInicio;
    private Long tiempoFin;
    private Double ubiLatitud;
    private Double ubiLongitud;

    private Boolean esVerificado;

    public DTOEventoResponse(Long id, String tipo, Integer cantVictimas, String[] autoridades, String areaInfluencia, String ubicacionEvento, Long tiempoInicio, Long tiempoFin, Double ubiLatitud, Double ubiLongitud, Boolean esVerificado) {
        this.id = id;
        this.tipo = tipo;
        this.cantVictimas = cantVictimas;
        this.autoridades = autoridades;
        this.areaInfluencia = areaInfluencia;
        this.ubicacionEvento = ubicacionEvento;
        this.tiempoInicio = tiempoInicio;
        this.tiempoFin = tiempoFin;
        this.ubiLatitud = ubiLatitud;
        this.ubiLongitud = ubiLongitud;
        this.esVerificado = esVerificado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCantVictimas() {
        return cantVictimas;
    }

    public void setCantVictimas(Integer cantVictimas) {
        this.cantVictimas = cantVictimas;
    }

    public String[] getAutoridades() {
        return autoridades;
    }

    public void setAutoridades(String[] autoridades) {
        this.autoridades = autoridades;
    }

    public String getAreaInfluencia() {
        return areaInfluencia;
    }

    public void setAreaInfluencia(String areaInfluencia) {
        this.areaInfluencia = areaInfluencia;
    }

    public String getUbicacionEvento() {
        return ubicacionEvento;
    }

    public void setUbicacionEvento(String ubicacionEvento) {
        this.ubicacionEvento = ubicacionEvento;
    }

    public Long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(Long tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public Long getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(Long tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public Double getUbiLatitud() {
        return ubiLatitud;
    }

    public void setUbiLatitud(Double ubiLatitud) {
        this.ubiLatitud = ubiLatitud;
    }

    public Double getUbiLongitud() {
        return ubiLongitud;
    }

    public void setUbiLongitud(Double ubiLongitud) {
        this.ubiLongitud = ubiLongitud;
    }

    public Boolean getEsVerificado() {
        return esVerificado;
    }

    public void setEsVerificado(Boolean esVerificado) {
        this.esVerificado = esVerificado;
    }
}
