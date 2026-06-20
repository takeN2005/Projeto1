package view;

import model.BaseDeDados;
import model.Estadio;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelEstadios extends JPanel {

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

    public PainelEstadios() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_MAIN);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Estádios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(BROWN);
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton btnRemover = criarBotao("Remover",          new Color(192, 57, 43));
        JButton btnAdd     = criarBotao("+ Adicionar Estádio", new Color(39, 174, 96));
        btns.add(btnRemover);
        btns.add(btnAdd);
        painelTopo.add(btns, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"Estádio", "Cidade", "País", "Lotação"};
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
                    c.setForeground(col == 3 ? BROWN : TEXT_PRI);
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

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(BG_TABLE);
        atualizarTabela();
        add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> mostrarFormularioAdicionar());

        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleciona um estádio primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int conf = JOptionPane.showConfirmDialog(this,
                    "Remover este estádio?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                BaseDeDados.estadios.remove(row);
                BaseDeDados.salvarDados();
                atualizarTabela();
            }
        });
    }

    private void mostrarFormularioAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Novo Estádio", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BG_MAIN);

        JPanel cab = new JPanel();
        cab.setBackground(BG_HEADER);
        cab.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblTit = new JLabel("Registar Novo Estádio");
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setForeground(Color.WHITE);
        cab.add(lblTit);
        dialog.add(cab, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 14));
        form.setBackground(BG_MAIN);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JTextField txtNome    = campoTexto();
        JTextField txtCidade  = campoTexto();
        JTextField txtPais    = campoTexto();
        JTextField txtLotacao = campoTexto();

        form.add(labelForm("Nome do Estádio:")); form.add(txtNome);
        form.add(labelForm("Cidade:"));          form.add(txtCidade);
        form.add(labelForm("País:"));            form.add(txtPais);
        form.add(labelForm("Lotação (nº):"));    form.add(txtLotacao);
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
            try {
                String nome    = txtNome.getText().trim();
                String cidade  = txtCidade.getText().trim();
                String pais    = txtPais.getText().trim();
                int lotacao    = Integer.parseInt(txtLotacao.getText().trim());
                if (nome.isEmpty() || cidade.isEmpty() || pais.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Preenche todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                BaseDeDados.estadios.add(new Estadio(nome, cidade, pais, lotacao));
                BaseDeDados.salvarDados();
                atualizarTabela();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "A lotação deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Estadio e : BaseDeDados.estadios) {
            modeloTabela.addRow(new Object[]{
                    e.getNome(), e.getCidade(), e.getPais(),
                    String.format("%,d", e.getLotacao())
            });
        }
    }

    private JTextField campoTexto() {
        JTextField tf = new JTextField();
        tf.setBackground(Color.WHITE);
        tf.setForeground(TEXT_PRI);
        tf.setCaretColor(BROWN);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(4, 8, 4, 8)));
        return tf;
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