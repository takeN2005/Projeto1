package view;

import javax.swing.*;
import java.awt.*;

public class PainelEmBreve extends JPanel {

    private static final Color BG_DARK  = new Color(245, 242, 238);
    private static final Color GOLD     = new Color(62, 39, 35);
    private static final Color TEXT_SEC = new Color(130, 115, 108);

    public PainelEmBreve(String titulo, String descricao) {
        setLayout(new GridBagLayout());
        setBackground(BG_DARK);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(GOLD);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblTitulo);
        centro.add(Box.createVerticalStrut(8));

        JLabel lblDesc = new JLabel(descricao, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(TEXT_SEC);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblDesc);

        centro.add(Box.createVerticalStrut(20));
        JLabel lblTag = new JLabel("Em breve", SwingConstants.CENTER);
        lblTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTag.setForeground(TEXT_SEC);
        lblTag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 210, 200)),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        lblTag.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblTag);

        add(centro);
    }
}