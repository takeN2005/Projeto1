package view;

import model.BaseDeDados;
import model.Estadio;
import model.Jogo;
import model.Selecao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelJogos extends JPanel {

    private static final Color BG_MAIN   = new Color(245, 242, 238);
    private static final Color BG_TABLE  = new Color(255, 255, 255);
    private static final Color BG_TABLE2 = new Color(248, 244, 240);
    private static final Color BG_HEADER = new Color(62, 39, 35);
    private static final Color BROWN     = new Color(62, 39, 35);
    private static final Color TEXT_PRI  = new Color(50, 40, 38);
    private static final Color TEXT_SEC  = new Color(130, 115, 108);
    private static final Color BORDER    = new Color(220, 210, 200);

    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelJogos() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_MAIN);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Jogos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(BROWN);
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel botoesTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoesTopo.setOpaque(false);
        JButton btnResultado = criarBotao("Registar Resultado", new Color(160, 100, 20));
        JButton btnAdicionar = criarBotao("+ Agendar Jogo", new Color(41, 98, 185));
        botoesTopo.add(btnResultado);
        botoesTopo.add(btnAdicionar);
        painelTopo.add(botoesTopo, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"Data/Hora", "Equipa Casa", "Resultado", "Equipa Fora", "Estádio", "Estado"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela) {
            @Override public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(214, 178, 140));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? BG_TABLE : BG_TABLE2);
                    if (col == 2) {
                        String val = (String) getValueAt(row, col);
                        c.setForeground(val.equals(" vs ") ? TEXT_SEC : BROWN);
                    } else if (col == 5) {
                        String estado = (String) getValueAt(row, col);
                        c.setForeground(switch (estado) {
                            case "Terminado"  -> new Color(40, 150, 80);
                            case "Cancelado"  -> new Color(190, 50, 50);
                            default           -> new Color(41, 98, 185);
                        });
                    } else {
                        c.setForeground(TEXT_PRI);
                    }
                }
                return c;
            }
        };

        tabela.setRowHeight(30);
        tabela.setBackground(BG_TABLE);
        tabela.setForeground(TEXT_PRI);
        tabela.setGridColor(BORDER);
        tabela.setShowVerticalLines(false);
        tabela.setSelectionBackground(new Color(214, 178, 140));
        tabela.getTableHeader().setBackground(BG_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(90);

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) abrirRegistoResultado();
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(BG_TABLE);
        atualizarTabela();
        add(scroll, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> mostrarFormularioAgendamento());
        btnResultado.addActionListener(e -> abrirRegistoResultado());
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Jogo j : BaseDeDados.jogos) {
            modeloTabela.addRow(new Object[]{
                    j.getDataHora(), j.getEquipaCasa().getNome(), j.getResultadoFormatado(),
                    j.getEquipaFora().getNome(), j.getEstadio(), j.getEstado()
            });
        }
    }

    private void abrirRegistoResultado() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleciona um jogo na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Jogo jogo = BaseDeDados.jogos.get(row);
        if (jogo.getEstado().equals("Terminado")) {
            JOptionPane.showMessageDialog(this,
                    "Este jogo já tem resultado registado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Registar Resultado", true);
        dlg.setSize(420, 260);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(BG_MAIN);

        JPanel cab = new JPanel();
        cab.setBackground(BG_HEADER);
        cab.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblTit = new JLabel(jogo.getEquipaCasa().getNome() + "  vs  " + jogo.getEquipaFora().getNome());
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTit.setForeground(Color.WHITE);
        cab.add(lblTit);
        dlg.add(cab, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(BG_MAIN);
        centro.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);

        JSpinner spCasa = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
        JSpinner spFora = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
        estilizarSpinner(spCasa);
        estilizarSpinner(spFora);

        JLabel lblCasa = new JLabel(jogo.getEquipaCasa().getNome(), SwingConstants.CENTER);
        lblCasa.setForeground(TEXT_PRI);
        lblCasa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lblFora = new JLabel(jogo.getEquipaFora().getNome(), SwingConstants.CENTER);
        lblFora.setForeground(TEXT_PRI);
        lblFora.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lblSep = new JLabel("—", SwingConstants.CENTER);
        lblSep.setForeground(BROWN);
        lblSep.setFont(new Font("Segoe UI", Font.BOLD, 20));

        gbc.gridx = 0; gbc.gridy = 0; centro.add(lblCasa, gbc);
        gbc.gridx = 1;                centro.add(new JLabel(""), gbc);
        gbc.gridx = 2;                centro.add(lblFora, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; centro.add(spCasa, gbc);
        gbc.gridx = 1; centro.add(lblSep, gbc);
        gbc.gridx = 2; centro.add(spFora, gbc);

        dlg.add(centro, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setBackground(BG_HEADER);
        JButton btnCancelar = criarBotao("Cancelar", new Color(192, 57, 43));
        JButton btnGuardar  = criarBotao("Confirmar", new Color(39, 174, 96));
        botoes.add(btnCancelar);
        botoes.add(btnGuardar);
        dlg.add(botoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dlg.dispose());
        btnGuardar.addActionListener(e -> {
            int gc = (int) spCasa.getValue();
            int gf = (int) spFora.getValue();
            jogo.registarResultado(gc, gf);
            BaseDeDados.salvarDados();
            atualizarTabela();
            dlg.dispose();
        });

        dlg.setVisible(true);
    }

    private void mostrarFormularioAgendamento() {
        if (BaseDeDados.selecoes.size() < 2 || BaseDeDados.estadios.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Precisas de pelo menos 2 seleções e 1 estádio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agendar Novo Jogo", true);
        dialog.setSize(460, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BG_MAIN);

        JPanel cab = new JPanel();
        cab.setBackground(BG_HEADER);
        cab.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblTit = new JLabel("Novo Jogo");
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setForeground(Color.WHITE);
        cab.add(lblTit);
        dialog.add(cab, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 14));
        form.setBackground(BG_MAIN);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JSpinner spinnerData = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorData = new JSpinner.DateEditor(spinnerData, "yyyy-MM-dd HH:mm");
        spinnerData.setEditor(editorData);
        estilizarSpinner(spinnerData);

        JComboBox<Selecao> comboCasa    = estilizarCombo(new JComboBox<>(BaseDeDados.selecoes.toArray(new Selecao[0])));
        JComboBox<Selecao> comboFora    = estilizarCombo(new JComboBox<>(BaseDeDados.selecoes.toArray(new Selecao[0])));
        JComboBox<Estadio> comboEstadio = estilizarCombo(new JComboBox<>(BaseDeDados.estadios.toArray(new Estadio[0])));

        form.add(labelForm("Data/Hora:"));      form.add(spinnerData);
        form.add(labelForm("Equipa da Casa:")); form.add(comboCasa);
        form.add(labelForm("Equipa de Fora:")); form.add(comboFora);
        form.add(labelForm("Estádio:"));        form.add(comboEstadio);
        dialog.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setBackground(BG_HEADER);
        JButton btnCancelar = criarBotao("Cancelar", new Color(192, 57, 43));
        JButton btnGuardar  = criarBotao("Guardar",  new Color(39, 174, 96));
        botoes.add(btnCancelar);
        botoes.add(btnGuardar);
        dialog.add(botoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            Selecao casa    = (Selecao) comboCasa.getSelectedItem();
            Selecao fora    = (Selecao) comboFora.getSelectedItem();
            Estadio estadio = (Estadio) comboEstadio.getSelectedItem();
            if (casa == fora) {
                JOptionPane.showMessageDialog(dialog,
                        "Uma seleção não pode jogar contra si própria!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String data = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(spinnerData.getValue());
            BaseDeDados.jogos.add(new Jogo(data, casa, fora, estadio.getNome()));
            BaseDeDados.salvarDados();
            atualizarTabela();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void estilizarSpinner(JSpinner sp) {
        sp.setBackground(BG_TABLE);
        sp.setForeground(TEXT_PRI);
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setBackground(BG_TABLE);
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setForeground(TEXT_PRI);
    }

    private <T> JComboBox<T> estilizarCombo(JComboBox<T> cb) {
        cb.setBackground(BG_TABLE);
        cb.setForeground(TEXT_PRI);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return cb;
    }

    private JLabel labelForm(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT_SEC);
        return l;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(cor); }
        });
        return btn;
    }
}