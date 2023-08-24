package org.unse.incendiapp.reciclerViewAssets;

public class item_evento {

    private int idImagenEvento;
    private Long idEvento;
    private String tipoEvento, ubicacionEvento;

    private Boolean esVerificado;

    public item_evento(Long idEvento, String tipoEvento, String ubicacionEvento, Boolean esverificado) {
        this.idEvento = idEvento;
        this.tipoEvento = tipoEvento;
        this.ubicacionEvento = ubicacionEvento;
        this.esVerificado = esverificado;
    }

    public int getIdImagenEvento() {
        return idImagenEvento;
    }

    public void setIdImagenEvento(int idImagenEvento) {
        this.idImagenEvento = idImagenEvento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getUbicacionEvento() {
        return ubicacionEvento;
    }

    public void setUbicacionEvento(String ubicacionEvento) {
        this.ubicacionEvento = ubicacionEvento;
    }

    public Boolean getEsVerificado() {
        return esVerificado;
    }

    public void setEsVerificado(Boolean esVerificado) {
        this.esVerificado = esVerificado;
    }
}
