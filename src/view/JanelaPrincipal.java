package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JanelaPrincipal extends JFrame {
    private JPanel painelCartoes;
    private CardLayout cardLayout;
    private JPanel menuLateral;
    private boolean menuVisivel = true;
    private ArrayList<JButton> botoesMenu = new ArrayList<>();

    private PainelSelecoes painelSelecoes;
    private PainelJogos painelJogos;
    private PainelGrupos painelGrupos;
    private PainelEstadios painelEstadios;

    public JanelaPrincipal() {
        setTitle("Sistema de Gestão - FIFA World Cup 2030");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel barraSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        barraSuperior.setBackground(new Color(64, 64, 64));
        JButton btnMenu = new JButton("Menu");
        btnMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setBackground(new Color(90, 90, 90));
        btnMenu.setFocusPainted(false);
        barraSuperior.add(btnMenu);
        add(barraSuperior, BorderLayout.NORTH);

        menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(new Color(78, 52, 46));
        menuLateral.setPreferredSize(new Dimension(220, 0));

        JButton btnHome     = criarBotaoMenu("Início");
        JButton btnSelecoes = criarBotaoMenu("Seleções");
        JButton btnJogos    = criarBotaoMenu("Jogos");
        JButton btnGrupos   = criarBotaoMenu("Grupos");
        JButton btnEstadios = criarBotaoMenu("Estádios");

        menuLateral.add(Box.createRigidArea(new Dimension(0, 20)));
        menuLateral.add(btnHome);
        menuLateral.add(btnSelecoes);
        menuLateral.add(btnJogos);
        menuLateral.add(btnGrupos);
        menuLateral.add(btnEstadios);
        add(menuLateral, BorderLayout.WEST);

        cardLayout = new CardLayout();
        painelCartoes = new JPanel(cardLayout);
        painelCartoes.setBackground(new Color(240, 240, 240));

        painelSelecoes = new PainelSelecoes();
        painelJogos    = new PainelJogos();
        painelGrupos   = new PainelGrupos();
        painelEstadios = new PainelEstadios();

        painelCartoes.add(new PainelHome(),  "Início");
        painelCartoes.add(painelSelecoes,    "Seleções");
        painelCartoes.add(painelJogos,       "Jogos");
        painelCartoes.add(painelGrupos,      "Grupos");
        painelCartoes.add(painelEstadios,    "Estádios");

        add(painelCartoes, BorderLayout.CENTER);
        selecionarBotao(btnHome);

        btnMenu.addActionListener(e -> {
            menuVisivel = !menuVisivel;
            menuLateral.setVisible(menuVisivel);
        });

        btnHome.addActionListener(e -> {
            cardLayout.show(painelCartoes, "Início");
            selecionarBotao(btnHome);
        });
        btnSelecoes.addActionListener(e -> {
            cardLayout.show(painelCartoes, "Seleções");
            selecionarBotao(btnSelecoes);
            painelSelecoes.atualizarTabela();
        });
        btnJogos.addActionListener(e -> {
            cardLayout.show(painelCartoes, "Jogos");
            selecionarBotao(btnJogos);
            painelJogos.atualizarTabela();
        });
        btnGrupos.addActionListener(e -> {
            cardLayout.show(painelCartoes, "Grupos");
            selecionarBotao(btnGrupos);
            painelGrupos.carregarListas();
        });
        btnEstadios.addActionListener(e -> {
            cardLayout.show(painelCartoes, "Estádios");
            selecionarBotao(btnEstadios);
            painelEstadios.atualizarTabela();
        });
    }

    private void selecionarBotao(JButton botaoAtivo) {
        for (JButton b : botoesMenu) {
            if (b == botaoAtivo) {
                b.setBackground(new Color(50, 30, 25));
                b.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(220, 220, 220)));
            } else {
                b.setBackground(new Color(78, 52, 46));
                b.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
            }
        }
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton("   " + texto);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(78, 52, 46));
        btn.setForeground(new Color(220, 220, 220));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        botoesMenu.add(btn);
        return btn;
    }
}