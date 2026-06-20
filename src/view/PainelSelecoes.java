package view;

import model.BaseDeDados;
import model.Selecao;
import view.dialogo.DialogoAdicionarSelecao;
import view.dialogo.DialogoPlantel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelSelecoes extends JPanel {
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelSelecoes() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 242, 238));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelTopo.setOpaque(false);

        JButton btnRemover    = criarBotao("Remover",         new Color(192, 57, 43));
        JButton btnVerPlantel = criarBotao("Ver Plantel",     new Color(41, 98, 185));
        JButton btnAdd        = criarBotao("+ Adicionar Seleção", new Color(39, 174, 96));

        painelTopo.add(btnRemover);
        painelTopo.add(btnVerPlantel);
        painelTopo.add(btnAdd);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"Seleção", "Treinador", "Tática", "Grupo", "Pontos", "Jogadores"};
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
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 244, 240));
                    c.setForeground(new Color(50, 40, 38));
                }
                return c;
            }
        };

        tabela.setRowHeight(32);
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(new Color(50, 40, 38));
        tabela.setGridColor(new Color(220, 210, 200));
        tabela.setSelectionBackground(new Color(214, 178, 140));
        tabela.getTableHeader().setBackground(new Color(62, 39, 35));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setShowVerticalLines(false);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 200)));
        scroll.getViewport().setBackground(Color.WHITE);
        atualizarTabela();
        add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            DialogoAdicionarSelecao dlg = new DialogoAdicionarSelecao(
                    (Frame) SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
            Selecao nova = dlg.getResultado();
            if (nova != null) {
                BaseDeDados.selecoes.add(nova);
                BaseDeDados.salvarDados();
                atualizarTabela();
            }
        });

        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) { aviso("Seleciona uma seleção primeiro."); return; }
            int conf = JOptionPane.showConfirmDialog(this,
                    "Remover esta seleção?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                BaseDeDados.selecoes.remove(row);
                BaseDeDados.salvarDados();
                atualizarTabela();
            }
        });

        btnVerPlantel.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) { aviso("Seleciona uma seleção primeiro."); return; }
            new DialogoPlantel((Frame) SwingUtilities.getWindowAncestor(this),
                    BaseDeDados.selecoes.get(row)).setVisible(true);
        });
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Selecao s : BaseDeDados.selecoes) {
            modeloTabela.addRow(new Object[]{
                    s.getNome(), s.getTreinador(), s.getTatica(),
                    s.getGrupo(), s.getPontos(), s.getPlantel().size() + " jogadores"
            });
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(cor); }
        });
        return btn;
    }
}