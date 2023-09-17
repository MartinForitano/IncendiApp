package org.unse.incendiapp.reciclerViewAssets.telefonosUtiles.reciclerViewAssets;

public class item_telefonos_utiles {

    private String nroTelefono, descripcionTelefono;

    private Integer numeroLlamada;


    public item_telefonos_utiles(Integer numeroLlamada, String nroTelefono, String descripcionTelefono) {
        this.numeroLlamada = numeroLlamada;
        this.nroTelefono = nroTelefono;
        this.descripcionTelefono = descripcionTelefono;
    }
    public Integer getNumeroLlamada() {
        return numeroLlamada;
    }

    public void setNumeroLlamada(Integer numeroLlamada) {
        this.numeroLlamada = numeroLlamada;
    }

    public String getNroTelefono() {
        return nroTelefono;
    }

    public void setNroTelefono(String nroTelefono) {
        this.nroTelefono = nroTelefono;
    }

    public String getDescripcionTelefono() {
        return descripcionTelefono;
    }

    public void setDescripcionTelefono(String descripcionTelefono) {
        this.descripcionTelefono = descripcionTelefono;
    }

}
