package model;

import java.io.*;
import java.util.ArrayList;

public class BaseDeDados {
    public static ArrayList<Selecao> selecoes = new ArrayList<>();
    public static ArrayList<Estadio> estadios = new ArrayList<>();
    public static ArrayList<Jogo> jogos = new ArrayList<>();

    private static final String ARQUIVO_DADOS = "dados_mundial.dat";

    public static void inicializarDados() {
        if (!carregarDados()) {
            selecoes.add(new Selecao("Portugal", "Roberto Martínez", "4-3-3"));
            selecoes.add(new Selecao("Espanha", "Luis de la Fuente", "4-3-3"));;
            estadios.add(new Estadio("Estádio da Luz", "Lisboa", "Portugal", 65000));
            estadios.add(new Estadio("MetLife Stadium", "Nova Iorque", "EUA", 82500));
        }
    }

    public static void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(selecoes);
            oos.writeObject(estadios);
            oos.writeObject(jogos);
        } catch (IOException e) {
            System.out.println("Erro ao guardar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            selecoes = (ArrayList<Selecao>) ois.readObject();
            estadios = (ArrayList<Estadio>) ois.readObject();
            jogos = (ArrayList<Jogo>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }
}