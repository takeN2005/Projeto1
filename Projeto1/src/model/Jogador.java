package model;

import java.io.Serializable;

public class Jogador implements Serializable {
    private String nome;
    private String posicao;
    private boolean titular;

    public Jogador(String nome, String posicao, boolean titular) {
        this.nome = nome;
        this.posicao = posicao;
        this.titular = titular;
    }

    public String getNome()     { return nome; }
    public String getPosicao()  { return posicao; }
    public boolean isTitular()  { return titular; }

    @Override
    public String toString() {
        return nome + " (" + posicao + ")";
    }
}