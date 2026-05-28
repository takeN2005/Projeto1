package view;

import model.BaseDeDados;
import model.Jogador;
import model.Selecao;
import view.dialogo.DialogoAdicionarSelecao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelSelecoes extends JPanel {
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelSelecoes() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        // ── Topo ──────────────────────────────────────────────
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelTopo.setOpaque(false);

        JButton btnRemover = criarBotao("- Remover", new Color(192, 57, 43));
        JButton btnVerPlantel = criarBotao("👁 Ver Plantel", new Color(41, 128, 185));
        JButton btnAdd = criarBotao("+ Adicionar Seleção", new Color(39, 174, 96));

        painelTopo.add(btnRemover);
        painelTopo.add(btnVerPlantel);
        painelTopo.add(btnAdd);
        add(painelTopo, BorderLayout.NORTH);

        // ── Tabela ────────────────────────────────────────────
        String[] colunas = {"Seleção", "Treinador", "Tática", "Grupo", "Pontos", "Jogadores"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        atualizarTabela();
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ── Eventos ───────────────────────────────────────────
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
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleciona uma seleção primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
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
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleciona uma seleção primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            mostrarPlantel(BaseDeDados.selecoes.get(row));
        });
    }

    // ── Ver plantel numa janela ────────────────────────────────
    private void mostrarPlantel(Selecao s) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Plantel — " + s.getNome(), true);
        dlg.setSize(480, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Cabeçalho
        JPanel cab = new JPanel(new GridLayout(2, 1));
        cab.setBackground(new Color(62, 39, 35));
        cab.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel lblNome = new JLabel(s.getNome() + "  ·  " + s.getTatica());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNome.setForeground(Color.WHITE);
        JLabel lblTrein = new JLabel("Treinador: " + s.getTreinador());
        lblTrein.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTrein.setForeground(new Color(200, 200, 200));
        cab.add(lblNome); cab.add(lblTrein);
        dlg.add(cab, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"#", "Nome", "Posição", "Titular"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Jogador> titulares = s.getTitulares();
        List<Jogador> suplentes = s.getSuplentes();

        int num = 1;
        for (Jogador j : titulares)
            modelo.addRow(new Object[]{num++, j.getNome(), j.getPosicao(), "✔ Titular"});
        for (Jogador j : suplentes)
            modelo.addRow(new Object[]{num++, j.getNome(), j.getPosicao(), "Suplente"});

        JTable tbl = new JTable(modelo);
        tbl.setRowHeight(24);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        dlg.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFechar.addActionListener(ev -> dlg.dispose());
        JPanel rodape = new JPanel();
        rodape.add(btnFechar);
        dlg.add(rodape, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Selecao s : BaseDeDados.selecoes) {
            modeloTabela.addRow(new Object[]{
                    s.getNome(),
                    s.getTreinador(),
                    s.getTatica(),
                    s.getGrupo(),
                    s.getPontos(),
                    s.getPlantel().size() + " jogadores"
            });
        }
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}