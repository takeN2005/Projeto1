package view;

import model.BaseDeDados;
import model.Selecao;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class PainelClassificacao extends JPanel {

    private static final Color BG_DARK  = new Color(245, 242, 238);
    private static final Color BG_CARD  = new Color(255, 255, 255);
    private static final Color GOLD     = new Color(62, 39, 35);
    private static final Color TEXT_PRI = new Color(50, 40, 38);
    private static final Color TEXT_SEC = new Color(130, 115, 108);
    private static final Color TEXT_DIM = new Color(160, 145, 138);
    private static final Color BORDER   = new Color(220, 210, 200);
    private static final Color GREEN_OK = new Color(40, 150, 80);
    private static final Color RED_BAD  = new Color(190, 50, 50);

    private JPanel listaGrupos;

    public PainelClassificacao() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_DARK);

        JLabel lblTitulo = new JLabel("Classificação");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(GOLD);
        add(lblTitulo, BorderLayout.NORTH);

        listaGrupos = new JPanel();
        listaGrupos.setLayout(new BoxLayout(listaGrupos, BoxLayout.Y_AXIS));
        listaGrupos.setBackground(BG_DARK);

        JScrollPane scroll = new JScrollPane(listaGrupos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        atualizarTabelas();
    }

    public void atualizarTabelas() {
        listaGrupos.removeAll();

        java.util.List<String> grupos = BaseDeDados.selecoes.stream()
                .map(Selecao::getGrupo)
                .filter(g -> !g.equals("Nenhum"))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (grupos.isEmpty()) {
            JLabel vazio = new JLabel("Ainda não há seleções atribuídas a grupos.");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            vazio.setForeground(TEXT_SEC);
            vazio.setAlignmentX(Component.LEFT_ALIGNMENT);
            vazio.setBorder(new EmptyBorder(20, 4, 0, 0));
            listaGrupos.add(vazio);
        } else {
            for (String grupo : grupos) {
                listaGrupos.add(buildTabelaGrupo(grupo));
                listaGrupos.add(Box.createVerticalStrut(16));
            }
        }

        listaGrupos.revalidate();
        listaGrupos.repaint();
    }

    private JPanel buildTabelaGrupo(String grupo) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(16, 18, 16, 18)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JLabel lblGrupo = new JLabel(grupo.toUpperCase());
        lblGrupo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGrupo.setForeground(GOLD);
        card.add(lblGrupo, BorderLayout.NORTH);

        java.util.List<Selecao> equipas = BaseDeDados.selecoes.stream()
                .filter(s -> s.getGrupo().equals(grupo))
                .sorted(Comparator
                        .comparingInt(Selecao::getPontos).reversed()
                        .thenComparing(Comparator.comparingInt(Selecao::getDiferencaGolos).reversed())
                        .thenComparing(Comparator.comparingInt(Selecao::getGolosMarcados).reversed()))
                .collect(Collectors.toList());

        JPanel tabela = new JPanel(new GridBagLayout());
        tabela.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);

        String[] cabecalhos = {"#", "Seleção", "J", "V", "E", "D", "GM", "GS", "DG", "Pts"};
        for (int i = 0; i < cabecalhos.length; i++) {
            JLabel h = new JLabel(cabecalhos[i], i == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
            h.setFont(new Font("Segoe UI", Font.BOLD, 11));
            h.setForeground(TEXT_DIM);
            gbc.gridx = i; gbc.gridy = 0; gbc.weightx = (i == 1) ? 1.0 : 0.0;
            tabela.add(h, gbc);
        }

        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = cabecalhos.length; gbc.weightx = 1.0;
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        tabela.add(sep, gbc);
        gbc.gridwidth = 1;

        for (int pos = 0; pos < equipas.size(); pos++) {
            Selecao s = equipas.get(pos);
            int row = pos + 2;

            addCelula(tabela, gbc, 0, row, String.valueOf(pos + 1), SwingConstants.LEFT, TEXT_DIM, false);
            addCelula(tabela, gbc, 1, row, s.getNome(), SwingConstants.LEFT, TEXT_PRI, true);
            addCelula(tabela, gbc, 2, row, String.valueOf(s.getJogosDisputados()), SwingConstants.CENTER, TEXT_SEC, false);
            addCelula(tabela, gbc, 3, row, String.valueOf(s.getVitorias()), SwingConstants.CENTER, GREEN_OK, false);
            addCelula(tabela, gbc, 4, row, String.valueOf(s.getEmpates()), SwingConstants.CENTER, TEXT_SEC, false);
            addCelula(tabela, gbc, 5, row, String.valueOf(s.getDerrotas()), SwingConstants.CENTER, RED_BAD, false);
            addCelula(tabela, gbc, 6, row, String.valueOf(s.getGolosMarcados()), SwingConstants.CENTER, TEXT_SEC, false);
            addCelula(tabela, gbc, 7, row, String.valueOf(s.getGolosSofridos()), SwingConstants.CENTER, TEXT_SEC, false);
            int dg = s.getDiferencaGolos();
            addCelula(tabela, gbc, 8, row, (dg >= 0 ? "+" : "") + dg, SwingConstants.CENTER, dg >= 0 ? GREEN_OK : RED_BAD, false);
            addCelula(tabela, gbc, 9, row, String.valueOf(s.getPontos()), SwingConstants.CENTER, GOLD, true);
        }

        if (equipas.isEmpty()) {
            JLabel vazio = new JLabel("Sem seleções neste grupo.");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            vazio.setForeground(TEXT_SEC);
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = cabecalhos.length;
            tabela.add(vazio, gbc);
        }

        card.add(tabela, BorderLayout.CENTER);
        return card;
    }

    private void addCelula(JPanel p, GridBagConstraints gbc, int col, int row,
                           String texto, int alinhamento, Color cor, boolean negrito) {
        JLabel l = new JLabel(texto, alinhamento);
        l.setFont(new Font("Segoe UI", negrito ? Font.BOLD : Font.PLAIN, 12));
        l.setForeground(cor);
        gbc.gridx = col; gbc.gridy = row; gbc.weightx = (col == 1) ? 1.0 : 0.0;
        p.add(l, gbc);
    }
}