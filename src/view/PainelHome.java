package view;

import javax.swing.*;
import java.awt.*;

public class PainelHome extends JPanel {

    private static final Color BG_MAIN    = new Color(245, 242, 238);
    private static final Color BROWN_DARK = new Color(62, 39, 35);

    public PainelHome() {
        setLayout(new GridBagLayout());
        setBackground(BG_MAIN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        g2.setColor(BG_MAIN);
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(139, 94, 60, 28));
        g2.setStroke(new BasicStroke(1f));
        int cx = w/2, cy = h/2 - 30;
        for (int r : new int[]{90, 160, 230, 310}) {
            g2.drawOval(cx - r, cy - r, r*2, r*2);
        }
        g2.dispose();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        JLabel lblSub = new JLabel("FIFA WORLD CUP", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSub.setForeground(new Color(139, 94, 60));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblSub);
        centro.add(Box.createVerticalStrut(6));

        JLabel lblTitulo = new JLabel("MUNDIAL 2030", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 72));
        lblTitulo.setForeground(BROWN_DARK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblTitulo);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(139, 94, 60, 150));
        sep.setMaximumSize(new Dimension(350, 2));
        centro.add(Box.createVerticalStrut(8));
        centro.add(sep);
        centro.add(Box.createVerticalStrut(12));

        JLabel lblDesc = new JLabel("Sistema de Gestão Desportiva", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblDesc.setForeground(new Color(97, 97, 97));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblDesc);

        centro.add(Box.createVerticalStrut(24));
        JLabel hint = new JLabel("Utiliza o menu lateral para navegar", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(new Color(140, 140, 140));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(hint);

        add(centro, gbc);
    }
}