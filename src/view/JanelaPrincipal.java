package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JanelaPrincipal extends JFrame {

    private static final Color BG_MAIN    = new Color(245, 242, 238);
    private static final Color MENU_BG    = new Color(78, 52, 46);
    private static final Color MENU_SEL   = new Color(50, 30, 25);
    private static final Color MENU_HOVER = new Color(95, 65, 56);
    private static final Color TOPBAR_BG  = new Color(62, 39, 35);
    private static final Color TEXT_MENU  = new Color(225, 215, 208);
    private static final Color ACCENT     = new Color(214, 178, 140);

    private JPanel painelCartoes;
    private CardLayout cardLayout;
    private JPanel menuLateral;
    private boolean menuVisivel = true;
    private ArrayList<JButton> botoesMenu = new ArrayList<>();

    private PainelEquipasGrupos painelEquipasGrupos;
    private PainelJogos painelJogos;
    private PainelEstadios painelEstadios;
    private PainelLogistica painelLogistica;
    private PainelClassificacao painelClassificacao;

    public JanelaPrincipal() {
        setTitle("Sistema de Gestão — FIFA World Cup 2030");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildMenu(),   BorderLayout.WEST);

        cardLayout = new CardLayout();
        painelCartoes = new JPanel(cardLayout);
        painelCartoes.setBackground(BG_MAIN);

        painelEquipasGrupos = new PainelEquipasGrupos();
        painelJogos          = new PainelJogos();
        painelEstadios       = new PainelEstadios();
        painelLogistica      = new PainelLogistica();
        painelClassificacao  = new PainelClassificacao();

        painelCartoes.add(new PainelHome(), "Início");
        painelCartoes.add(painelJogos,      "Jogos");
        painelCartoes.add(painelEstadios,   "Estádios");
        painelCartoes.add(new PainelEmBreve("Arbitragem",
                "Atribuição de árbitros e gestão de certificações."), "Arbitragem");
        painelCartoes.add(new PainelEmBreve("Bilhetes",
                "Venda e gestão de bilhetes por jogo e setor."), "Bilhetes");
        painelCartoes.add(painelLogistica,     "Logística");
        painelCartoes.add(painelEquipasGrupos, "Equipas & Grupos");
        painelCartoes.add(painelClassificacao, "Classificação");
        painelCartoes.add(new PainelEmBreve("Fase Eliminatória",
                "Chaveamento e progressão das eliminatórias."), "Fase Eliminatória");

        add(painelCartoes, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(TOPBAR_BG);
        bar.setPreferredSize(new Dimension(0, 50));

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        esq.setOpaque(false);

        JButton btnMenu = new JButton("☰");
        btnMenu.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnMenu.setForeground(TEXT_MENU);
        btnMenu.setBackground(null);
        btnMenu.setOpaque(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setBorderPainted(false);
        btnMenu.setFocusPainted(false);
        btnMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMenu.addActionListener(e -> {
            menuVisivel = !menuVisivel;
            menuLateral.setVisible(menuVisivel);
        });
        btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnMenu.setForeground(Color.WHITE); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnMenu.setForeground(TEXT_MENU); }
        });

        JLabel lblTitulo = new JLabel("Mundial 2030");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);

        esq.add(btnMenu);
        esq.add(lblTitulo);

        JLabel lblHora = new JLabel();
        lblHora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblHora.setForeground(TEXT_MENU);
        javax.swing.Timer t = new javax.swing.Timer(1000, e ->
                lblHora.setText(java.time.LocalTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "  "));
        t.start();

        bar.add(esq,     BorderLayout.WEST);
        bar.add(lblHora, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildMenu() {
        menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(MENU_BG);
        menuLateral.setPreferredSize(new Dimension(220, 0));

        menuLateral.add(Box.createRigidArea(new Dimension(0, 16)));

        JLabel lblSec = new JLabel("  NAVEGAÇÃO");
        lblSec.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSec.setForeground(new Color(190, 170, 160));
        lblSec.setMaximumSize(new Dimension(220, 24));
        menuLateral.add(lblSec);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 6)));

        String[] nomes = {
                "Início", "Jogos", "Estádios", "Arbitragem", "Bilhetes",
                "Logística", "Equipas & Grupos", "Classificação", "Fase Eliminatória"
        };

        for (String nome : nomes) {
            JButton btn = criarBotaoMenu(nome);
            menuLateral.add(btn);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        menuLateral.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("  v1.4 — ES 2025/26");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ver.setForeground(new Color(150, 130, 120));
        ver.setMaximumSize(new Dimension(220, 24));
        menuLateral.add(ver);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 12)));

        if (!botoesMenu.isEmpty()) selecionarBotao(botoesMenu.get(0));
        return menuLateral;
    }

    private JButton criarBotaoMenu(String secao) {
        JButton btn = new JButton(secao) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                if (getBackground().equals(MENU_SEL)) {
                    g2.setColor(ACCENT);
                    g2.fillRect(0, 0, 4, getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setMaximumSize(new Dimension(220, 42));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(MENU_BG);
        btn.setForeground(TEXT_MENU);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(MENU_SEL)) {
                    btn.setBackground(MENU_HOVER);
                    btn.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(MENU_SEL)) {
                    btn.setBackground(MENU_BG);
                    btn.setForeground(TEXT_MENU);
                }
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(painelCartoes, secao);
            selecionarBotao(btn);
            if (secao.equals("Jogos"))            painelJogos.atualizarTabela();
            if (secao.equals("Estádios"))         painelEstadios.atualizarTabela();
            if (secao.equals("Logística"))        painelLogistica.atualizarConteudo();
            if (secao.equals("Equipas & Grupos"))  painelEquipasGrupos.atualizarTudo();
            if (secao.equals("Classificação"))     painelClassificacao.atualizarTabelas();
        });

        botoesMenu.add(btn);
        return btn;
    }

    private void selecionarBotao(JButton ativo) {
        for (JButton b : botoesMenu) {
            if (b == ativo) {
                b.setBackground(MENU_SEL);
                b.setForeground(Color.WHITE);
                b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            } else {
                b.setBackground(MENU_BG);
                b.setForeground(TEXT_MENU);
                b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }
            b.repaint();
        }
    }
}