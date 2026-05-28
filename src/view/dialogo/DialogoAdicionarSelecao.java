package view.dialogo;

import model.Jogador;
import model.Selecao;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogoAdicionarSelecao extends JDialog {

    private Selecao resultado = null;

    // Passo 1
    private JTextField txtNome;
    private JTextField txtTreinador;
    private JComboBox<String> comboTatica;

    // Passo 2 — campo
    private PainelCampo painelCampo;

    // Passo 3 — suplentes
    private List<JTextField> txtNomesSuplentes = new ArrayList<>();
    private List<JComboBox<String>> combosPosicoes = new ArrayList<>();

    private JPanel painelPassos;
    private CardLayout cardPassos;
    private int passoAtual = 1;
    private JButton btnAnterior, btnSeguinte, btnGuardar;
    private JLabel lblPasso;

    private static final String[] POSICOES = {
            "Guarda-Redes", "Defesa", "Médio-Defensivo",
            "Médio", "Médio-Ofensivo", "Extremo", "Avançado"
    };

    public DialogoAdicionarSelecao(Frame parent) {
        super(parent, "Adicionar Seleção", true);
        setSize(520, 680);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        // ── Cabeçalho ─────────────────────────────────────────
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(62, 39, 35));
        cabecalho.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Nova Seleção");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        lblPasso = new JLabel("Passo 1 de 3");
        lblPasso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPasso.setForeground(new Color(200, 200, 200));

        cabecalho.add(lblTitulo, BorderLayout.WEST);
        cabecalho.add(lblPasso, BorderLayout.EAST);
        add(cabecalho, BorderLayout.NORTH);

        // ── Passos ────────────────────────────────────────────
        cardPassos = new CardLayout();
        painelPassos = new JPanel(cardPassos);
        painelPassos.setBackground(new Color(240, 240, 240));
        painelPassos.add(buildPasso1(), "1");
        painelPassos.add(buildPasso2(), "2");
        painelPassos.add(buildPasso3(), "3");
        add(painelPassos, BorderLayout.CENTER);

        // ── Botões de navegação ───────────────────────────────
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rodape.setBackground(new Color(230, 230, 230));
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        btnAnterior = criarBotao("← Anterior", new Color(100, 100, 100));
        btnSeguinte = criarBotao("Seguinte →", new Color(41, 128, 185));
        btnGuardar  = criarBotao("✔ Guardar",  new Color(39, 174, 96));
        btnGuardar.setVisible(false);
        btnAnterior.setEnabled(false);

        btnAnterior.addActionListener(e -> navegarPasso(-1));
        btnSeguinte.addActionListener(e -> navegarPasso(1));
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = criarBotao("Cancelar", new Color(192, 57, 43));
        btnCancelar.addActionListener(e -> dispose());

        rodape.add(btnCancelar);
        rodape.add(btnAnterior);
        rodape.add(btnSeguinte);
        rodape.add(btnGuardar);
        add(rodape, BorderLayout.SOUTH);
    }

    // ── Passo 1: Dados gerais ──────────────────────────────────
    private JPanel buildPasso1() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(240, 240, 240));
        p.setBorder(new EmptyBorder(30, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridwidth = 1;

        JLabel lblInfo = new JLabel("Informações da Seleção");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblInfo.setForeground(new Color(62, 39, 35));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        p.add(lblInfo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.35;
        p.add(labelForm("Nome da Seleção:"), gbc);
        txtNome = campoTexto();
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(txtNome, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.35;
        p.add(labelForm("Treinador:"), gbc);
        txtTreinador = campoTexto();
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(txtTreinador, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.35;
        p.add(labelForm("Tática:"), gbc);
        comboTatica = new JComboBox<>(PainelCampo.getTaticasDisponiveis().toArray(new String[0]));
        comboTatica.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(comboTatica, gbc);

        // Preview da tática selecionada
        JLabel lblPreview = new JLabel("No passo seguinte, irás preencher o 11 inicial no campo.", SwingConstants.CENTER);
        lblPreview.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblPreview.setForeground(new Color(120, 120, 120));
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        p.add(lblPreview, gbc);

        return p;
    }

    // ── Passo 2: Campo com os 11 titulares ────────────────────
    private JPanel buildPasso2() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(new Color(240, 240, 240));
        p.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lbl = new JLabel("Clica em cada posição para inserir o nome do jogador", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lbl.setForeground(new Color(100, 100, 100));
        p.add(lbl, BorderLayout.NORTH);

        painelCampo = new PainelCampo("4-3-3");
        p.add(painelCampo, BorderLayout.CENTER);

        return p;
    }

    // ── Passo 3: Suplentes ────────────────────────────────────
    private JPanel buildPasso3() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(240, 240, 240));
        wrapper.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lbl = new JLabel("9 Suplentes");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(new Color(62, 39, 35));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        wrapper.add(lbl, BorderLayout.NORTH);

        JPanel grelha = new JPanel(new GridBagLayout());
        grelha.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cabeçalhos
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.05;
        grelha.add(labelForm("#"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        grelha.add(labelForm("Nome"), gbc);
        gbc.gridx = 2; gbc.weightx = 0.40;
        grelha.add(labelForm("Posição"), gbc);

        for (int i = 0; i < 9; i++) {
            JTextField tf = campoTexto();
            JComboBox<String> cb = new JComboBox<>(POSICOES);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            txtNomesSuplentes.add(tf);
            combosPosicoes.add(cb);

            gbc.gridy = i + 1;
            gbc.gridx = 0; gbc.weightx = 0.05;
            grelha.add(labelForm(String.valueOf(i + 12)), gbc);
            gbc.gridx = 1; gbc.weightx = 0.55;
            grelha.add(tf, gbc);
            gbc.gridx = 2; gbc.weightx = 0.40;
            grelha.add(cb, gbc);
        }

        JScrollPane scroll = new JScrollPane(grelha);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(240, 240, 240));
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    // ── Navegação entre passos ────────────────────────────────
    private void navegarPasso(int direcao) {
        if (direcao == 1 && !validarPassoAtual()) return;

        passoAtual += direcao;
        cardPassos.show(painelPassos, String.valueOf(passoAtual));
        lblPasso.setText("Passo " + passoAtual + " de 3");
        btnAnterior.setEnabled(passoAtual > 1);
        btnSeguinte.setVisible(passoAtual < 3);
        btnGuardar.setVisible(passoAtual == 3);

        // Ao entrar no passo 2, atualiza a tática no campo
        if (passoAtual == 2) {
            painelCampo.setTatica((String) comboTatica.getSelectedItem());
        }
    }

    private boolean validarPassoAtual() {
        if (passoAtual == 1) {
            if (txtNome.getText().trim().isEmpty()) {
                erro("O nome da seleção é obrigatório.");
                return false;
            }
            if (txtTreinador.getText().trim().isEmpty()) {
                erro("O nome do treinador é obrigatório.");
                return false;
            }
        }
        if (passoAtual == 2) {
            if (!painelCampo.todosPreenchidos()) {
                erro("Preenche todos os 11 jogadores no campo antes de continuar.");
                return false;
            }
        }
        return true;
    }

    // ── Guardar a seleção ─────────────────────────────────────
    private void guardar() {
        String tatica = (String) comboTatica.getSelectedItem();
        resultado = new Selecao(
                txtNome.getText().trim(),
                txtTreinador.getText().trim(),
                tatica
        );

        // Titulares
        String[] nomes = painelCampo.getNomeJogadores();
        String[] posicoes = painelCampo.getNomesPosicoes();
        for (int i = 0; i < nomes.length; i++) {
            resultado.adicionarJogador(new Jogador(nomes[i], posicoes[i], true));
        }

        // Suplentes
        for (int i = 0; i < 9; i++) {
            String nome = txtNomesSuplentes.get(i).getText().trim();
            String pos  = (String) combosPosicoes.get(i).getSelectedItem();
            if (!nome.isEmpty()) {
                resultado.adicionarJogador(new Jogador(nome, pos, false));
            }
        }

        dispose();
    }

    public Selecao getResultado() { return resultado; }

    // ── Utilitários de UI ─────────────────────────────────────
    private JLabel labelForm(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(70, 70, 70));
        return l;
    }

    private JTextField campoTexto() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                new EmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}