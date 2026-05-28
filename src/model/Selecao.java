package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Selecao implements Serializable {
    private String nome;
    private String treinador;
    private String grupo;
    private String tatica;
    private int pontos;
    private ArrayList<Jogador> plantel;

    public Selecao(String nome, String treinador, String tatica) {
        this.nome = nome;
        this.treinador = treinador;
        this.tatica = tatica;
        this.grupo = "Nenhum";
        this.pontos = 0;
        this.plantel = new ArrayList<>();
    }

    public String getNome()      { return nome; }
    public String getTreinador() { return treinador; }
    public String getGrupo()     { return grupo; }
    public String getTatica()    { return tatica; }
    public int getPontos()       { return pontos; }

    public ArrayList<Jogador> getPlantel() { return plantel; }

    public List<Jogador> getTitulares() {
        return plantel.stream()
                .filter(Jogador::isTitular)
                .collect(Collectors.toList());
    }

    public List<Jogador> getSuplentes() {
        return plantel.stream()
                .filter(j -> !j.isTitular())
                .collect(Collectors.toList());
    }

    public void adicionarJogador(Jogador j) { plantel.add(j); }
    public void adicionarPontos(int p)      { this.pontos += p; }
    public void setPontos(int p)            { this.pontos = p; }
    public void setGrupo(String grupo)      { this.grupo = grupo; }

    @Override
    public String toString() { return nome; }
}