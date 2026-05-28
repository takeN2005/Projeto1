package model;

import java.io.Serializable;

public class Jogo implements Serializable {
    private String dataHora;
    private Selecao equipaCasa;
    private Selecao equipaFora;
    private int golosCasa;
    private int golosFora;
    private String estadio;
    private String estado;

    public Jogo(String dataHora, Selecao equipaCasa, Selecao equipaFora, String estadio) {
        this.dataHora = dataHora;
        this.equipaCasa = equipaCasa;
        this.equipaFora = equipaFora;
        this.estadio = estadio;
        this.estado = "Agendado";
        this.golosCasa = 0;
        this.golosFora = 0;
    }

    public String getDataHora() { return dataHora; }
    public Selecao getEquipaCasa() { return equipaCasa; }
    public Selecao getEquipaFora() { return equipaFora; }
    public String getEstadio() { return estadio; }

    public String getResultadoFormatado() {
        if (estado.equals("Agendado")) {
            return " vs ";
        }
        return golosCasa + " - " + golosFora;
    }
}