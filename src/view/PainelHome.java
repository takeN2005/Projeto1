package view;

import javax.swing.*;
import java.awt.*;

public class PainelHome extends JPanel {
    public PainelHome() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("MUNDIAL 2030", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 75));
        lblTitulo.setForeground(new Color(62, 39, 35));

        JLabel lblSub = new JLabel("Sistema de Gestão Desportiva", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblSub.setForeground(new Color(97, 97, 97));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);
        centerPanel.add(lblTitulo);
        centerPanel.add(lblSub);

        add(centerPanel);
    }
}