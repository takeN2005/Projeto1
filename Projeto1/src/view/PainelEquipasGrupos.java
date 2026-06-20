package view;

import javax.swing.*;
import java.awt.*;

public class PainelEquipasGrupos extends JPanel {

    private static final Color BG_DARK   = new Color(245, 242, 238);
    private static final Color TOGGLE_BG = new Color(255, 255, 255);
    private static final Color TOGGLE_SEL= new Color(78, 52, 46);
    private static final Color GOLD      = new Color(62, 39, 35);
    private static final Color TEXT_SEC  = new Color(130, 115, 108);

    private PainelSelecoes painelSelecoes;
    private PainelGrupos painelGrupos;
    private CardLayout cardLayout;
    private JPanel cartoes;
    private JButton btnSelecoes, btnGrupos;

    public PainelEquipasGrupos() {
        setLayout(new BorderLayout(0, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        JLabel lblTitulo = new JLabel("Equipas & Grupos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(GOLD);
        topo.add(lblTitulo, BorderLayout.WEST);

        JPanel toggleLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toggleLinha.setOpaque(false);
        toggleLinha.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        btnSelecoes = criarToggle("Seleções", true);
        btnGrupos   = criarToggle("Grupos", false);

        JPanel pill = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        pill.setBackground(TOGGLE_BG);
        pill.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 60)));
        pill.add(btnSelecoes);
        pill.add(btnGrupos);
        toggleLinha.add(pill);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(topo);
        header.add(toggleLinha);
        add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cartoes = new JPanel(cardLayout);
        cartoes.setBackground(BG_DARK);

        painelSelecoes = new PainelSelecoes();
        painelGrupos   = new PainelGrupos();

        cartoes.add(painelSelecoes, "Seleções");
        cartoes.add(painelGrupos,   "Grupos");
        add(cartoes, BorderLayout.CENTER);

        btnSelecoes.addActionListener(e -> mostrar("Seleções"));
        btnGrupos.addActionListener(e -> mostrar("Grupos"));
    }

    private void mostrar(String secao) {
        cardLayout.show(cartoes, secao);
        estilizarToggle(btnSelecoes, secao.equals("Seleções"));
        estilizarToggle(btnGrupos, secao.equals("Grupos"));
        if (secao.equals("Seleções")) painelSelecoes.atualizarTabela();
        else                          painelGrupos.carregarListas();
    }

    public void atualizarTudo() {
        painelSelecoes.atualizarTabela();
        painelGrupos.carregarListas();
    }

    private JButton criarToggle(String texto, boolean ativo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarToggle(btn, ativo);
        return btn;
    }

    private void estilizarToggle(JButton btn, boolean ativo) {
        btn.setBackground(ativo ? TOGGLE_SEL : TOGGLE_BG);
        btn.setForeground(ativo ? Color.WHITE : TEXT_SEC);
    }
}