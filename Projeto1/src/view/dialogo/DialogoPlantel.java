package view.dialogo;

import model.Jogador;
import model.Selecao;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DialogoPlantel extends JDialog {

    private static final Color BG_MAIN   = new Color(245, 242, 238);
    private static final Color BG_HEADER = new Color(62, 39, 35);
    private static final Color BG_TABLE  = new Color(255, 255, 255);
    private static final Color BG_TABLE2 = new Color(248, 244, 240);
    private static final Color TEXT_PRI  = new Color(50, 40, 38);
    private static final Color TEXT_SEC  = new Color(130, 115, 108);
    private static final Color BORDER    = new Color(220, 210, 200);

    public DialogoPlantel(Frame parent, Selecao selecao) {
        super(parent, "Plantel — " + selecao.getNome(), true);
        setSize(460, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        JPanel cab = new JPanel();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        cab.setBackground(BG_HEADER);
        cab.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblNome = new JLabel(selecao.getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNome.setForeground(Color.WHITE);
        JLabel lblInfo = new JLabel("Selecionador: " + selecao.getTreinador()
                + "   ·   Tática: " + selecao.getTatica());
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(225, 215, 208));
        cab.add(lblNome);
        cab.add(lblInfo);
        add(cab, BorderLayout.NORTH);

        String[] cols = {"#", "Nome", "Posição"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        int n = 1;
        for (Jogador j : selecao.getPlantel()) {
            modelo.addRow(new Object[]{n++, j.getNome(), j.getPosicao()});
        }

        JTable tbl = new JTable(modelo) {
            @Override public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? BG_TABLE : BG_TABLE2);
                c.setForeground(TEXT_PRI);
                return c;
            }
        };
        tbl.setRowHeight(26);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.setGridColor(BORDER);
        tbl.setShowVerticalLines(false);
        tbl.getTableHeader().setBackground(BG_HEADER);
        tbl.getTableHeader().setForeground(Color.WHITE);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_TABLE);
        add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rodape.setBackground(BG_HEADER);
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setBackground(new Color(120, 120, 120));
        btnFechar.setFocusPainted(false);
        btnFechar.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnFechar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);
        add(rodape, BorderLayout.SOUTH);
    }
}