package model;

import java.io.*;
import java.util.ArrayList;

public class BaseDeDados {
    public static ArrayList<Selecao> selecoes = new ArrayList<>();
    public static ArrayList<Estadio> estadios = new ArrayList<>();
    public static ArrayList<Jogo> jogos = new ArrayList<>();
    public static ArrayList<Alojamento> alojamentos = new ArrayList<>();
    public static ArrayList<Transporte> transportes = new ArrayList<>();

    private static final String ARQUIVO_DADOS = "dados_mundial.dat";

    public static void inicializarDados() {
        if (!carregarDados()) {
            carregarEstadios();
            carregarSelecoesExemplo();
        }
    }

    private static void carregarEstadios() {
        // Estados Unidos
        estadios.add(new Estadio("MetLife Stadium", "Nova Iorque/Nova Jersey", "EUA", 82500));
        estadios.add(new Estadio("SoFi Stadium", "Los Angeles", "EUA", 70240));
        estadios.add(new Estadio("AT&T Stadium", "Dallas", "EUA", 80000));
        estadios.add(new Estadio("Mercedes-Benz Stadium", "Atlanta", "EUA", 71000));
        estadios.add(new Estadio("Lincoln Financial Field", "Filadélfia", "EUA", 69596));
        estadios.add(new Estadio("Lumen Field", "Seattle", "EUA", 68740));
        estadios.add(new Estadio("Levi's Stadium", "Santa Clara", "EUA", 68500));
        estadios.add(new Estadio("Arrowhead Stadium", "Kansas City", "EUA", 76416));
        estadios.add(new Estadio("Gillette Stadium", "Foxborough", "EUA", 65878));
        estadios.add(new Estadio("Hard Rock Stadium", "Miami", "EUA", 64767));

        // México
        estadios.add(new Estadio("Estádio Azteca", "Cidade do México", "México", 87000));
        estadios.add(new Estadio("Estádio Akron", "Guadalajara", "México", 46232));
        estadios.add(new Estadio("Estádio BBVA", "Monterrey", "México", 53500));
        estadios.add(new Estadio("Estádio Corregidora", "Querétaro", "México", 33162));
        estadios.add(new Estadio("Estádio Universitário", "Monterrey", "México", 41615));
        estadios.add(new Estadio("Estádio Jalisco", "Guadalajara", "México", 48071));
        estadios.add(new Estadio("Estádio Olímpico Universitário", "Cidade do México", "México", 52000));
        estadios.add(new Estadio("Estádio Hidalgo", "Pachuca", "México", 30000));
        estadios.add(new Estadio("Estádio Nou Camp", "León", "México", 31000));
        estadios.add(new Estadio("Estádio Cuauhtémoc", "Puebla", "México", 51726));

        // Canadá
        estadios.add(new Estadio("BC Place", "Vancouver", "Canadá", 54500));
        estadios.add(new Estadio("BMO Field", "Toronto", "Canadá", 30000));
        estadios.add(new Estadio("Commonwealth Stadium", "Edmonton", "Canadá", 56302));
        estadios.add(new Estadio("McMahon Stadium", "Calgary", "Canadá", 35650));
        estadios.add(new Estadio("Mosaic Stadium", "Regina", "Canadá", 33350));
        estadios.add(new Estadio("IG Field", "Winnipeg", "Canadá", 33422));
        estadios.add(new Estadio("TD Place Stadium", "Otava", "Canadá", 24000));
        estadios.add(new Estadio("Tim Hortons Field", "Hamilton", "Canadá", 23000));
        estadios.add(new Estadio("Percival Molson Stadium", "Montreal", "Canadá", 25012));
        estadios.add(new Estadio("Saputo Stadium", "Montreal", "Canadá", 19619));
    }

    private static void carregarSelecoesExemplo() {
        selecoes.add(criarSelecao("Portugal", "Roberto Martínez", "4-3-3", new String[][]{
                {"Diogo Costa","Guarda-Redes"}, {"Rui Patrício","Guarda-Redes"}, {"José Sá","Guarda-Redes"},
                {"Rúben Dias","Defesa"}, {"António Silva","Defesa"}, {"Diogo Dalot","Defesa"},
                {"Nélson Semedo","Defesa"}, {"Nuno Mendes","Defesa"}, {"Gonçalo Inácio","Defesa"},
                {"Danilo Pereira","Defesa"}, {"João Cancelo","Defesa"},
                {"João Palhinha","Médio-Defensivo"}, {"Rúben Neves","Médio-Defensivo"},
                {"Bruno Fernandes","Médio"}, {"Vitinha","Médio"}, {"João Neves","Médio"},
                {"William Carvalho","Médio"}, {"Matheus Nunes","Médio"},
                {"Bernardo Silva","Médio-Ofensivo"}, {"João Félix","Médio-Ofensivo"},
                {"Rafael Leão","Extremo"}, {"Pedro Neto","Extremo"}, {"Francisco Conceição","Extremo"},
                {"Cristiano Ronaldo","Avançado"}, {"Gonçalo Ramos","Avançado"}
        }));

        selecoes.add(criarSelecao("Espanha", "Luis de la Fuente", "4-3-3", new String[][]{
                {"Unai Simón","Guarda-Redes"}, {"David Raya","Guarda-Redes"}, {"Robert Sánchez","Guarda-Redes"},
                {"Dani Carvajal","Defesa"}, {"Aymeric Laporte","Defesa"}, {"Robin Le Normand","Defesa"},
                {"Pau Cubarsí","Defesa"}, {"Marc Cucurella","Defesa"}, {"Jesús Navas","Defesa"}, {"Nacho Fernández","Defesa"},
                {"Martín Zubimendi","Médio-Defensivo"}, {"Rodri","Médio-Defensivo"},
                {"Pedri","Médio"}, {"Gavi","Médio"}, {"Fabián Ruiz","Médio"}, {"Mikel Merino","Médio"},
                {"Dani Olmo","Médio-Ofensivo"}, {"Marco Asensio","Médio-Ofensivo"},
                {"Lamine Yamal","Extremo"}, {"Nico Williams","Extremo"}, {"Ferran Torres","Extremo"}, {"Mikel Oyarzabal","Extremo"},
                {"Álvaro Morata","Avançado"}, {"Ayoze Pérez","Avançado"}, {"Joselu","Avançado"}
        }));

        selecoes.add(criarSelecao("França", "Didier Deschamps", "4-2-3-1", new String[][]{
                {"Mike Maignan","Guarda-Redes"}, {"Brice Samba","Guarda-Redes"}, {"Lucas Chevalier","Guarda-Redes"},
                {"William Saliba","Defesa"}, {"Dayot Upamecano","Defesa"}, {"Jules Koundé","Defesa"},
                {"Theo Hernández","Defesa"}, {"Ferland Mendy","Defesa"}, {"Benjamin Pavard","Defesa"},
                {"Ibrahima Konaté","Defesa"}, {"Lucas Hernández","Defesa"},
                {"Aurélien Tchouaméni","Médio-Defensivo"}, {"N'Golo Kanté","Médio-Defensivo"},
                {"Eduardo Camavinga","Médio"}, {"Adrien Rabiot","Médio"}, {"Warren Zaïre-Emery","Médio"},
                {"Antoine Griezmann","Médio-Ofensivo"}, {"Christopher Nkunku","Médio-Ofensivo"},
                {"Ousmane Dembélé","Extremo"}, {"Bradley Barcola","Extremo"}, {"Michael Olise","Extremo"},
                {"Kylian Mbappé","Avançado"}, {"Marcus Thuram","Avançado"}, {"Randal Kolo Muani","Avançado"}, {"Hugo Ekitike","Avançado"}
        }));

        selecoes.add(criarSelecao("Inglaterra", "Thomas Tuchel", "4-2-3-1", new String[][]{
                {"Jordan Pickford","Guarda-Redes"}, {"Aaron Ramsdale","Guarda-Redes"}, {"James Trafford","Guarda-Redes"},
                {"Harry Maguire","Defesa"}, {"John Stones","Defesa"}, {"Marc Guéhi","Defesa"},
                {"Kyle Walker","Defesa"}, {"Trent Alexander-Arnold","Defesa"}, {"Levi Colwill","Defesa"}, {"Ezri Konsa","Defesa"},
                {"Declan Rice","Médio-Defensivo"}, {"Adam Wharton","Médio-Defensivo"},
                {"Jude Bellingham","Médio"}, {"Conor Gallagher","Médio"}, {"Kobbie Mainoo","Médio"},
                {"Phil Foden","Médio-Ofensivo"}, {"Cole Palmer","Médio-Ofensivo"}, {"Eberechi Eze","Médio-Ofensivo"},
                {"Bukayo Saka","Extremo"}, {"Anthony Gordon","Extremo"}, {"Marcus Rashford","Extremo"}, {"Morgan Rogers","Extremo"},
                {"Harry Kane","Avançado"}, {"Ollie Watkins","Avançado"}, {"Dominic Solanke","Avançado"}
        }));
    }

    private static Selecao criarSelecao(String nome, String treinador, String tatica, String[][] jogadores) {
        Selecao s = new Selecao(nome, treinador, tatica);
        for (String[] j : jogadores) {
            s.adicionarJogador(new Jogador(j[0], j[1], false));
        }
        return s;
    }

    public static void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(selecoes);
            oos.writeObject(estadios);
            oos.writeObject(jogos);
            oos.writeObject(alojamentos);
            oos.writeObject(transportes);
        } catch (IOException e) {
            System.out.println("Erro ao guardar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            selecoes    = (ArrayList<Selecao>)    ois.readObject();
            estadios    = (ArrayList<Estadio>)    ois.readObject();
            jogos       = (ArrayList<Jogo>)       ois.readObject();
            alojamentos = (ArrayList<Alojamento>) ois.readObject();
            transportes = (ArrayList<Transporte>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }
}