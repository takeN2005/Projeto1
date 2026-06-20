package view;

import model.BaseDeDados;
import model.Estadio;
import model.Jogo;
import model.Selecao;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class PainelGrupos extends JPanel {

    private static final Color BG_DARK  = new Color(245, 242, 238);
    private static final Color BG_CARD  = new Color(255, 255, 255);
    private static final Color GOLD     = new Color(62, 39, 35);
    private static final Color TEXT_PRI = new Color(50, 40, 38);
    private static final Color TEXT_SEC = new Color(130, 115, 108);
    private static final Color BORDER   = new Color(220, 210, 200);

    private static final String[] LETRAS = {"A","B","C","D","E","F","G","H"};

    private JComboBox<String> comboGrupos;
    private DefaultListModel<Selecao> modeloSemGrupo;
    private DefaultListModel<Selecao> modeloNoGrupo;
    private JList<Selecao> listaSemGrupo;
    private JList<Selecao> listaNoGrupo;
    private JButton btnGerarJogos;

    public PainelGrupos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_DARK);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setOpaque(false);

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esquerda.setOpaque(false);
        JLabel lblSelGrupo = new JLabel("Grupo:");
        lblSelGrupo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSelGrupo.setForeground(TEXT_SEC);
        String[] grupos = {"Grupo A","Grupo B","Grupo C","Grupo D","Grupo E","Grupo F","Grupo G","Grupo H"};
        comboGrupos = estilizarCombo(new JComboBox<>(grupos));
        esquerda.add(lblSelGrupo);
        esquerda.add(comboGrupos);
        painelTopo.add(esquerda, BorderLayout.WEST);

        JButton btnAutoDistribuir = criarBotao("Auto-Distribuir Grupos", new Color(120, 80, 160));
        btnAutoDistribuir.addActionListener(e -> autoDistribuirGrupos());
        painelTopo.add(btnAutoDistribuir, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelListas = new JPanel(new GridLayout(1, 3, 10, 0));
        painelListas.setOpaque(false);

        modeloSemGrupo = new DefaultListModel<>();
        listaSemGrupo  = estilizarLista(new JList<>(modeloSemGrupo));
        JScrollPane scrollEsq = estilizarScroll(new JScrollPane(listaSemGrupo), "Seleções sem Grupo");

        modeloNoGrupo = new DefaultListModel<>();
        listaNoGrupo  = estilizarLista(new JList<>(modeloNoGrupo));
        JScrollPane scrollDir = estilizarScroll(new JScrollPane(listaNoGrupo), "Seleções no Grupo");

        JPanel painelBtns = new JPanel(new GridBagLayout());
        painelBtns.setOpaque(false);
        JButton btnAdd     = criarBotaoPequeno(">>", new Color(41, 98, 185));
        JButton btnRemover = criarBotaoPequeno("<<", new Color(90, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 0, 5, 0);
        painelBtns.add(btnAdd, gbc);
        gbc.gridy = 1;
        painelBtns.add(btnRemover, gbc);

        painelListas.add(scrollEsq);
        painelListas.add(painelBtns);
        painelListas.add(scrollDir);
        add(painelListas, BorderLayout.CENTER);

        JPanel painelFundo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelFundo.setOpaque(false);
        btnGerarJogos = criarBotao("Gerar Jogos do Grupo (requer 4 seleções)", new Color(160, 110, 20));
        btnGerarJogos.setEnabled(false);
        painelFundo.add(btnGerarJogos);
        add(painelFundo, BorderLayout.SOUTH);

        comboGrupos.addActionListener(e -> carregarListas());

        btnAdd.addActionListener(e -> {
            Selecao s = listaSemGrupo.getSelectedValue();
            if (s != null && modeloNoGrupo.getSize() < 4) {
                s.setGrupo((String) comboGrupos.getSelectedItem());
                BaseDeDados.salvarDados();
                carregarListas();
            }
        });

        btnRemover.addActionListener(e -> {
            Selecao s = listaNoGrupo.getSelectedValue();
            if (s != null) {
                s.setGrupo("Nenhum");
                BaseDeDados.salvarDados();
                carregarListas();
            }
        });

        btnGerarJogos.addActionListener(e -> gerarJogosAutomaticos());
        carregarListas();
    }

    public void carregarListas() {
        modeloSemGrupo.clear();
        modeloNoGrupo.clear();
        String grupoAtual = (String) comboGrupos.getSelectedItem();
        for (Selecao s : BaseDeDados.selecoes) {
            if (s.getGrupo().equals("Nenhum"))         modeloSemGrupo.addElement(s);
            else if (s.getGrupo().equals(grupoAtual))  modeloNoGrupo.addElement(s);
        }
        btnGerarJogos.setEnabled(modeloNoGrupo.getSize() == 4 && !jogosJaGerados(grupoAtual));
    }

    private void autoDistribuirGrupos() {
        List<Selecao> livres = new ArrayList<>();
        for (Selecao s : BaseDeDados.selecoes)
            if (s.getGrupo().equals("Nenhum")) livres.add(s);

        if (livres.size() < 4) {
            JOptionPane.showMessageDialog(this,
                    "Precisas de pelo menos 4 seleções sem grupo para distribuir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Collections.shuffle(livres);
        int numGrupos = Math.min(livres.size() / 4, LETRAS.length);
        int usadas = 0;

        for (int g = 0; g < numGrupos; g++) {
            for (int i = 0; i < 4; i++) {
                livres.get(usadas).setGrupo("Grupo " + LETRAS[g]);
                usadas++;
            }
        }

        BaseDeDados.salvarDados();
        carregarListas();

        int sobraram = livres.size() - usadas;
        String msg = numGrupos + " grupo(s) formado(s) com sucesso!"
                + (sobraram > 0 ? "\n" + sobraram + " seleção(ões) ficaram sem grupo (não chegavam para mais um grupo completo)." : "");
        JOptionPane.showMessageDialog(this, msg);
    }

    private boolean jogosJaGerados(String grupo) {
        for (Jogo j : BaseDeDados.jogos)
            if (j.getEquipaCasa().getGrupo().equals(grupo)) return true;
        return false;
    }

    private void gerarJogosAutomaticos() {
        if (BaseDeDados.estadios.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Regista pelo menos um estádio primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String grupoAtual = (String) comboGrupos.getSelectedItem();
        if (jogosJaGerados(grupoAtual)) {
            JOptionPane.showMessageDialog(this,
                    "Os jogos do " + grupoAtual + " já foram gerados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Selecao s1 = modeloNoGrupo.getElementAt(0);
        Selecao s2 = modeloNoGrupo.getElementAt(1);
        Selecao s3 = modeloNoGrupo.getElementAt(2);
        Selecao s4 = modeloNoGrupo.getElementAt(3);

        int grupoIdx = grupoAtual.charAt(grupoAtual.length() - 1) - 'A'; // "Grupo A" -> 0
        int numEstadios = BaseDeDados.estadios.size();
        LocalDate base = LocalDate.now().plusDays(7);
        int[] horas = {16, 20};

        // Ronda 1: s1-s2, s3-s4 | Ronda 2: s1-s3, s2-s4 | Ronda 3: s1-s4, s2-s3
        Selecao[][] rondas = {
                {s1, s2, s3, s4},
                {s1, s3, s2, s4},
                {s1, s4, s2, s3}
        };

        int contadorEstadio = 0;
        for (int ronda = 0; ronda < 3; ronda++) {
            LocalDate dia = base.plusDays((long) ronda * 8 + grupoIdx);
            for (int jogoNaRonda = 0; jogoNaRonda < 2; jogoNaRonda++) {
                Selecao casa = rondas[ronda][jogoNaRonda * 2];
                Selecao fora = rondas[ronda][jogoNaRonda * 2 + 1];
                LocalDateTime dataHora = dia.atTime(horas[jogoNaRonda], 0);
                String dataStr = dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Estadio estadio = BaseDeDados.estadios.get(contadorEstadio % numEstadios);
                contadorEstadio++;
                BaseDeDados.jogos.add(new Jogo(dataStr, casa, fora, estadio.getNome()));
            }
        }

        BaseDeDados.salvarDados();
        JOptionPane.showMessageDialog(this, "6 jogos do " + grupoAtual + " gerados com datas e horários!");
        carregarListas();
    }

    private <T> JList<T> estilizarLista(JList<T> lista) {
        lista.setBackground(BG_CARD);
        lista.setForeground(TEXT_PRI);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lista.setSelectionBackground(new Color(60, 90, 150));
        lista.setSelectionForeground(Color.WHITE);
        lista.setFixedCellHeight(32);
        return lista;
    }

    private JScrollPane estilizarScroll(JScrollPane scroll, String titulo) {
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER), titulo,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), TEXT_SEC));
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBackground(BG_DARK);
        return scroll;
    }

    private <T> JComboBox<T> estilizarCombo(JComboBox<T> cb) {
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRI);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return cb;
    }

    private JButton criarBotaoPequeno(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(60, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(cor); }
        });
        return btn;
    }
}