package view.dialogo;

import model.Jogador;
import model.Selecao;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogoAdicionarSelecao extends JDialog {

    private static final Color BG_MAIN   = new Color(245, 242, 238);
    private static final Color BG_HEADER = new Color(62, 39, 35);
    private static final Color BG_CARD   = new Color(255, 255, 255);
    private static final Color BROWN     = new Color(62, 39, 35);
    private static final Color TEXT_PRI  = new Color(50, 40, 38);
    private static final Color TEXT_SEC  = new Color(130, 115, 108);
    private static final Color BORDER    = new Color(220, 210, 200);

    private static final String[] POSICOES = {
            "Guarda-Redes", "Defesa", "Médio-Defensivo",
            "Médio", "Médio-Ofensivo", "Extremo", "Avançado"
    };
    private static final int TOTAL_JOGADORES = 25;

    private Selecao resultado = null;

    private JTextField txtNome, txtTreinador;
    private JComboBox<String> comboTatica;
    private List<JTextField> txtNomesJogadores = new ArrayList<>();
    private List<JComboBox<String>> combosPosicoes = new ArrayList<>();

    private CardLayout cardPassos;
    private JPanel painelPassos;
    private int passoAtual = 1;
    private JButton btnAnterior, btnSeguinte, btnGuardar;
    private JLabel lblPasso;

    public DialogoAdicionarSelecao(Frame parent) {
        super(parent, "Adicionar Seleção", true);
        setSize(560, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(BG_HEADER);
        cabecalho.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lblTitulo = new JLabel("Nova Seleção");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblPasso = new JLabel("Passo 1 de 2");
        lblPasso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPasso.setForeground(new Color(225, 215, 208));
        cabecalho.add(lblTitulo, BorderLayout.WEST);
        cabecalho.add(lblPasso, BorderLayout.EAST);
        add(cabecalho, BorderLayout.NORTH);

        cardPassos = new CardLayout();
        painelPassos = new JPanel(cardPassos);
        painelPassos.setBackground(BG_MAIN);
        painelPassos.add(buildPasso1(), "1");
        painelPassos.add(buildPasso2(), "2");
        add(painelPassos, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rodape.setBackground(BG_HEADER);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton btnCancelar = criarBotao("Cancelar", new Color(192, 57, 43));
        btnAnterior = criarBotao("← Anterior", new Color(120, 120, 120));
        btnSeguinte = criarBotao("Seguinte →", new Color(41, 98, 185));
        btnGuardar  = criarBotao("Guardar",  new Color(39, 174, 96));
        btnGuardar.setVisible(false);
        btnAnterior.setEnabled(false);

        btnCancelar.addActionListener(e -> dispose());
        btnAnterior.addActionListener(e -> navegar(-1));
        btnSeguinte.addActionListener(e -> navegar(1));
        btnGuardar.addActionListener(e -> guardar());

        rodape.add(btnCancelar);
        rodape.add(btnAnterior);
        rodape.add(btnSeguinte);
        rodape.add(btnGuardar);
        add(rodape, BorderLayout.SOUTH);
    }

    private JPanel buildPasso1() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_MAIN);
        p.setBorder(new EmptyBorder(30, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel lblInfo = new JLabel("Informações da Seleção");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblInfo.setForeground(BROWN);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        p.add(lblInfo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.35;
        p.add(labelForm("País / Seleção:"), gbc);
        txtNome = campoTexto();
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(txtNome, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.35;
        p.add(labelForm("Selecionador:"), gbc);
        txtTreinador = campoTexto();
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(txtTreinador, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.35;
        p.add(labelForm("Tática:"), gbc);
        comboTatica = estilizarCombo(new JComboBox<>(
                PainelCampo.getTaticasDisponiveis().toArray(new String[0])));
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(comboTatica, gbc);

        JLabel lblNota = new JLabel(
                "No passo seguinte vais inserir os 25 jogadores do plantel.",
                SwingConstants.CENTER);
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNota.setForeground(TEXT_SEC);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(24, 0, 0, 0);
        p.add(lblNota, gbc);

        return p;
    }

    private JPanel buildPasso2() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_MAIN);
        wrapper.setBorder(new EmptyBorder(14, 20, 10, 20));

        JLabel lbl = new JLabel("Plantel — 25 Jogadores");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(BROWN);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        wrapper.add(lbl, BorderLayout.NORTH);

        JPanel grelha = new JPanel(new GridBagLayout());
        grelha.setBackground(BG_MAIN);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0.08; grelha.add(labelForm("#"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.57; grelha.add(labelForm("Nome"), gbc);
        gbc.gridx = 2; gbc.weightx = 0.35; grelha.add(labelForm("Posição"), gbc);

        for (int i = 0; i < TOTAL_JOGADORES; i++) {
            JTextField tf = campoTexto();
            JComboBox<String> cb = estilizarCombo(new JComboBox<>(POSICOES));
            txtNomesJogadores.add(tf);
            combosPosicoes.add(cb);

            gbc.gridy = i + 1;
            gbc.gridx = 0; gbc.weightx = 0.08; grelha.add(labelForm(String.valueOf(i + 1)), gbc);
            gbc.gridx = 1; gbc.weightx = 0.57; grelha.add(tf, gbc);
            gbc.gridx = 2; gbc.weightx = 0.35; grelha.add(cb, gbc);
        }

        JScrollPane scroll = new JScrollPane(grelha);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private void navegar(int direcao) {
        if (direcao == 1 && !validarPasso1()) return;
        passoAtual += direcao;
        cardPassos.show(painelPassos, String.valueOf(passoAtual));
        lblPasso.setText("Passo " + passoAtual + " de 2");
        btnAnterior.setEnabled(passoAtual > 1);
        btnSeguinte.setVisible(passoAtual < 2);
        btnGuardar.setVisible(passoAtual == 2);
    }

    private boolean validarPasso1() {
        if (txtNome.getText().trim().isEmpty()) {
            erro("O nome da seleção é obrigatório."); return false;
        }
        if (txtTreinador.getText().trim().isEmpty()) {
            erro("O nome do selecionador é obrigatório."); return false;
        }
        return true;
    }

    private void guardar() {
        int preenchidos = 0;
        for (JTextField tf : txtNomesJogadores) if (!tf.getText().trim().isEmpty()) preenchidos++;
        if (preenchidos < TOTAL_JOGADORES) {
            erro("Faltam " + (TOTAL_JOGADORES - preenchidos) + " jogador(es) para completar os 25.");
            return;
        }

        resultado = new Selecao(txtNome.getText().trim(), txtTreinador.getText().trim(),
                (String) comboTatica.getSelectedItem());

        for (int i = 0; i < TOTAL_JOGADORES; i++) {
            String nome = txtNomesJogadores.get(i).getText().trim();
            String pos  = (String) combosPosicoes.get(i).getSelectedItem();
            resultado.adicionarJogador(new Jogador(nome, pos, false));
        }

        dispose();
    }

    public Selecao getResultado() { return resultado; }

    private JLabel labelForm(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_SEC);
        return l;
    }

    private JTextField campoTexto() {
        JTextField tf = new JTextField();
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_PRI);
        tf.setCaretColor(BROWN);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    private <T> JComboBox<T> estilizarCombo(JComboBox<T> cb) {
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRI);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return cb;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}