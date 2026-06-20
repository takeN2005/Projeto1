package model;

import java.io.Serializable;

public class Alojamento implements Serializable {
    private Selecao selecao;
    private String hotel;
    private String cidade;
    private String tipo;
    private String dataInicio;
    private String dataFim;
    private boolean ativo;

    public Alojamento(Selecao selecao, String hotel, String cidade, String tipo,
                      String dataInicio, String dataFim) {
        this.selecao = selecao;
        this.hotel = hotel;
        this.cidade = cidade;
        this.tipo = tipo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativo = true;
    }

    public Selecao getSelecao()   { return selecao; }
    public String getHotel()      { return hotel; }
    public String getCidade()     { return cidade; }
    public String getTipo()       { return tipo; }
    public String getDataInicio() { return dataInicio; }
    public String getDataFim()    { return dataFim; }
    public boolean isAtivo()      { return ativo; }

    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}