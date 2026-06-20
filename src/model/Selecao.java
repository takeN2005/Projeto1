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
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golosMarcados;
    private int golosSofridos;
    private ArrayList<Jogador> plantel;

    public Selecao(String nome, String treinador, String tatica) {
        this.nome = nome;
        this.treinador = treinador;
        this.tatica = tatica;
        this.grupo = "Nenhum";
        this.plantel = new ArrayList<>();
    }

    public String getNome()      { return nome; }
    public String getTreinador() { return treinador; }
    public String getGrupo()     { return grupo; }
    public String getTatica()    { return tatica; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public int getVitorias()      { return vitorias; }
    public int getEmpates()       { return empates; }
    public int getDerrotas()      { return derrotas; }
    public int getGolosMarcados() { return golosMarcados; }
    public int getGolosSofridos() { return golosSofridos; }
    public int getPontos()          { return vitorias * 3 + empates; }
    public int getDiferencaGolos()  { return golosMarcados - golosSofridos; }
    public int getJogosDisputados() { return vitorias + empates + derrotas; }

    public void registarVitoria(int gm, int gs) { vitorias++;  golosMarcados += gm; golosSofridos += gs; }
    public void registarEmpate(int gm, int gs)  { empates++;   golosMarcados += gm; golosSofridos += gs; }
    public void registarDerrota(int gm, int gs) { derrotas++;  golosMarcados += gm; golosSofridos += gs; }

    public ArrayList<Jogador> getPlantel() { return plantel; }
    public void adicionarJogador(Jogador j) { plantel.add(j); }

    public List<Jogador> getTitulares() {
        return plantel.stream().filter(Jogador::isTitular).collect(Collectors.toList());
    }
    public List<Jogador> getSuplentes() {
        return plantel.stream().filter(j -> !j.isTitular()).collect(Collectors.toList());
    }

    @Override
    public String toString() { return nome; }
}