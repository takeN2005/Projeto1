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

    public String getDataHora()    { return dataHora; }
    public Selecao getEquipaCasa() { return equipaCasa; }
    public Selecao getEquipaFora() { return equipaFora; }
    public String getEstadio()     { return estadio; }
    public int getGolosCasa()      { return golosCasa; }
    public int getGolosFora()      { return golosFora; }
    public String getEstado()      { return estado; }

    public void setGolosCasa(int golosCasa) { this.golosCasa = golosCasa; }
    public void setGolosFora(int golosFora) { this.golosFora = golosFora; }
    public void setEstado(String estado)    { this.estado = estado; }

    /** Regista o resultado final e atualiza automaticamente as estatísticas
     *  de ambas as seleções (vitórias/empates/derrotas/golos). */
    public void registarResultado(int golosCasa, int golosFora) {
        if (this.estado.equals("Terminado")) return; // evita registar duas vezes

        this.golosCasa = golosCasa;
        this.golosFora = golosFora;
        this.estado = "Terminado";

        if (golosCasa > golosFora) {
            equipaCasa.registarVitoria(golosCasa, golosFora);
            equipaFora.registarDerrota(golosFora, golosCasa);
        } else if (golosFora > golosCasa) {
            equipaFora.registarVitoria(golosFora, golosCasa);
            equipaCasa.registarDerrota(golosCasa, golosFora);
        } else {
            equipaCasa.registarEmpate(golosCasa, golosFora);
            equipaFora.registarEmpate(golosFora, golosCasa);
        }
    }

    public String getResultadoFormatado() {
        if (estado.equals("Agendado")) return " vs ";
        return golosCasa + " - " + golosFora;
    }
}