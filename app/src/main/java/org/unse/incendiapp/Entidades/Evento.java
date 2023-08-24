package org.unse.incendiapp.Entidades;

import java.time.LocalDateTime;
import java.util.Date;

public class Evento {

    private Long id;
    private String tipo;
    private Integer cantVictimas;
    private String[] autoridades;
    private String areaInfluencia;
    private String ubicacionEvento;
    private Date tiempoInicio;
    private Date tiempoFin;
    private Double ubiLatitud;
    private Double ubiLongitud;

    private Boolean esVerificado;

    public Evento(Long id, String tipo, Integer cantVictimas, String[] autoridades, String areaInfluencia, String ubicacionEvento, Date tiempoInicio, Date tiempoFin, Double ubiLatitud, Double ubiLongitud, Boolean esverificado) {
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
        this.esVerificado = esverificado;
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

    public Date getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(Date tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public Date getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(Date tiempoFin) {
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
