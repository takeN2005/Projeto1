package view;

import model.BaseDeDados;
import model.Jogo;
import model.Selecao;

import javax.swing.*;
import java.awt.*;

public class PainelGrupos extends JPanel {
    private JComboBox<String> comboGrupos;
    private DefaultListModel<Selecao> modeloSemGrupo;
    private DefaultListModel<Selecao> modeloNoGrupo;
    private JList<Selecao> listaSemGrupo;
    private JList<Selecao> listaNoGrupo;
    private JButton btnGerarJogos;

    public PainelGrupos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.setOpaque(false);
        painelTopo.add(new JLabel("Selecionar Grupo:"));
        String[] grupos = {"Grupo A", "Grupo B", "Grupo C", "Grupo D", "Grupo E", "Grupo F", "Grupo G", "Grupo H"};
        comboGrupos = new JComboBox<>(grupos);
        painelTopo.add(comboGrupos);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelListas = new JPanel(new GridLayout(1, 3, 10, 0));
        painelListas.setOpaque(false);

        modeloSemGrupo = new DefaultListModel<>();
        listaSemGrupo = new JList<>(modeloSemGrupo);
        JScrollPane scrollEsq = new JScrollPane(listaSemGrupo);
        scrollEsq.setBorder(BorderFactory.createTitledBorder("Seleções sem Grupo"));

        modeloNoGrupo = new DefaultListModel<>();
        listaNoGrupo = new JList<>(modeloNoGrupo);
        JScrollPane scrollDir = new JScrollPane(listaNoGrupo);
        scrollDir.setBorder(BorderFactory.createTitledBorder("Seleções no Grupo Atual"));

        JPanel painelBotoesTransferir = new JPanel(new GridBagLayout());
        painelBotoesTransferir.setOpaque(false);
        JButton btnAdd = criarBotaoPequeno(">>", new Color(52, 73, 94));
        JButton btnRemover = criarBotaoPequeno("<<", new Color(52, 73, 94));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 0, 5, 0);
        painelBotoesTransferir.add(btnAdd, gbc);
        gbc.gridy = 1;
        painelBotoesTransferir.add(btnRemover, gbc);

        painelListas.add(scrollEsq);
        painelListas.add(painelBotoesTransferir);
        painelListas.add(scrollDir);
        add(painelListas, BorderLayout.CENTER);

        JPanel painelFundo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelFundo.setOpaque(false);
        btnGerarJogos = criarBotaoEstilizado("Gerar Jogos do Grupo (Requer 4 Seleções)", new Color(230, 126, 34));
        btnGerarJogos.setEnabled(false);
        painelFundo.add(btnGerarJogos);
        add(painelFundo, BorderLayout.SOUTH);

        comboGrupos.addActionListener(e -> carregarListas());

        btnAdd.addActionListener(e -> {
            Selecao s = listaSemGrupo.getSelectedValue();
            if (s != null && modeloNoGrupo.getSize() < 4) {
                s.setGrupo((String) comboGrupos.getSelectedItem());
                BaseDeDados.salvarDados();
                carregarListas();
            }
        });

        btnRemover.addActionListener(e -> {
            Selecao s = listaNoGrupo.getSelectedValue();
            if (s != null) {
                s.setGrupo("Nenhum");
                BaseDeDados.salvarDados();
                carregarListas();
            }
        });

        btnGerarJogos.addActionListener(e -> gerarJogosAutomaticos());

        carregarListas();
    }

    public void carregarListas() {
        modeloSemGrupo.clear();
        modeloNoGrupo.clear();
        String grupoAtual = (String) comboGrupos.getSelectedItem();

        for (Selecao s : BaseDeDados.selecoes) {
            if (s.getGrupo().equals("Nenhum")) {
                modeloSemGrupo.addElement(s);
            } else if (s.getGrupo().equals(grupoAtual)) {
                modeloNoGrupo.addElement(s);
            }
        }

        // Botão só ativo se houver 4 seleções E jogos ainda não gerados
        btnGerarJogos.setEnabled(modeloNoGrupo.getSize() == 4 && !jogosJaGerados(grupoAtual));
    }

    private boolean jogosJaGerados(String grupoAtual) {
        for (Jogo j : BaseDeDados.jogos) {
            if (j.getEquipaCasa().getGrupo().equals(grupoAtual)) {
                return true;
            }
        }
        return false;
    }

    private void gerarJogosAutomaticos() {
        if (BaseDeDados.estadios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Regista pelo menos um Estádio primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String grupoAtual = (String) comboGrupos.getSelectedItem();

        // Verificação extra de segurança
        if (jogosJaGerados(grupoAtual)) {
            JOptionPane.showMessageDialog(this,
                    "Os jogos do " + grupoAtual + " já foram gerados!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Selecao s1 = modeloNoGrupo.getElementAt(0);
        Selecao s2 = modeloNoGrupo.getElementAt(1);
        Selecao s3 = modeloNoGrupo.getElementAt(2);
        Selecao s4 = modeloNoGrupo.getElementAt(3);

        int numEstadios = BaseDeDados.estadios.size();

        String est1 = BaseDeDados.estadios.get(0 % numEstadios).getNome();
        String est2 = BaseDeDados.estadios.get(1 % numEstadios).getNome();
        String est3 = BaseDeDados.estadios.get(2 % numEstadios).getNome();
        String est4 = BaseDeDados.estadios.get(3 % numEstadios).getNome();
        String est5 = BaseDeDados.estadios.get(4 % numEstadios).getNome();
        String est6 = BaseDeDados.estadios.get(5 % numEstadios).getNome();

        BaseDeDados.jogos.add(new Jogo("A Definir", s1, s2, est1));
        BaseDeDados.jogos.add(new Jogo("A Definir", s3, s4, est2));
        BaseDeDados.jogos.add(new Jogo("A Definir", s1, s3, est3));
        BaseDeDados.jogos.add(new Jogo("A Definir", s2, s4, est4));
        BaseDeDados.jogos.add(new Jogo("A Definir", s1, s4, est5));
        BaseDeDados.jogos.add(new Jogo("A Definir", s2, s3, est6));

        BaseDeDados.salvarDados();
        JOptionPane.showMessageDialog(this, "6 jogos do " + grupoAtual + " gerados com sucesso!");
        carregarListas();
    }

    private JButton criarBotaoPequeno(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton criarBotaoEstilizado(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}