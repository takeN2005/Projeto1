package view;

import model.BaseDeDados;
import model.Estadio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelEstadios extends JPanel {
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelEstadios() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelTopo.setOpaque(false);

        JButton btnRemover = criarBotaoEstilizado("- Remover Estádio", new Color(192, 57, 43));
        JButton btnAdd = criarBotaoEstilizado("+ Adicionar Estádio", new Color(39, 174, 96));

        painelTopo.add(btnRemover);
        painelTopo.add(btnAdd);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"Estádio", "Cidade", "País", "Lotação"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        atualizarTabela();
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> mostrarFormularioAdicionar());

        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                int confirmacao = JOptionPane.showConfirmDialog(this, "Remover este estádio?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    BaseDeDados.estadios.remove(row);
                    BaseDeDados.salvarDados();
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um estádio na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // NOVA JANELA BONITA PARA ADICIONAR ESTÁDIOS
    private void mostrarFormularioAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Estádio", true);
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(62, 39, 35));
        JLabel lblTitulo = new JLabel("Registar Novo Estádio");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelTopo.add(lblTitulo);
        dialog.add(painelTopo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 15));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelForm.setOpaque(false);

        JTextField txtNome = new JTextField();
        JTextField txtCidade = new JTextField();
        JTextField txtPais = new JTextField();
        JTextField txtLotacao = new JTextField();

        painelForm.add(new JLabel("Nome do Estádio:")); painelForm.add(txtNome);
        painelForm.add(new JLabel("Cidade:")); painelForm.add(txtCidade);
        painelForm.add(new JLabel("País:")); painelForm.add(txtPais);
        painelForm.add(new JLabel("Lotação (Nº):")); painelForm.add(txtLotacao);
        dialog.add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setOpaque(false);
        JButton btnGuardar = criarBotaoEstilizado("Guardar", new Color(39, 174, 96));
        JButton btnCancelar = criarBotaoEstilizado("Cancelar", new Color(192, 57, 43));

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                String cidade = txtCidade.getText().trim();
                String pais = txtPais.getText().trim();
                int lotacao = Integer.parseInt(txtLotacao.getText().trim());

                if (nome.isEmpty() || cidade.isEmpty() || pais.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Preenche todos os campos de texto!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BaseDeDados.estadios.add(new Estadio(nome, cidade, pais, lotacao));
                BaseDeDados.salvarDados();
                atualizarTabela();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "A lotação tem de ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnGuardar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JButton criarBotaoEstilizado(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Estadio e : BaseDeDados.estadios) {
            modeloTabela.addRow(new Object[]{e.getNome(), e.getCidade(), e.getPais(), e.getLotacao()});
        }
    }
}