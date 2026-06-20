package model;

import java.io.Serializable;

public class Transporte implements Serializable {
    private Selecao selecao;
    private String tipo;     // "Voo" ou "Autocarro"
    private String origem;
    private String destino;
    private String dataHora;

    public Transporte(Selecao selecao, String tipo, String origem, String destino, String dataHora) {
        this.selecao = selecao;
        this.tipo = tipo;
        this.origem = origem;
        this.destino = destino;
        this.dataHora = dataHora;
    }

    public Selecao getSelecao()  { return selecao; }
    public String getTipo()      { return tipo; }
    public String getOrigem()    { return origem; }
    public String getDestino()   { return destino; }
    public String getDataHora()  { return dataHora; }
}