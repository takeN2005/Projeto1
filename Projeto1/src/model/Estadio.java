package model;

import java.io.Serializable;

public class Estadio implements Serializable {
    private String nome;
    private String cidade;
    private String pais;
    private int lotacao;

    public Estadio(String nome, String cidade, String pais, int lotacao) {
        this.nome = nome;
        this.cidade = cidade;
        this.pais = pais;
        this.lotacao = lotacao;
    }

    public String getNome() { return nome; }
    public String getCidade() { return cidade; }
    public String getPais() { return pais; }
    public int getLotacao() { return lotacao; }

    @Override
    public String toString() {
        return nome + " (" + cidade + ", " + pais + ")";
    }
}