package view;

import model.BaseDeDados;
import model.Estadio;
import model.Jogo;
import model.Selecao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelJogos extends JPanel {
    private DefaultTableModel modeloTabela;

    public PainelJogos() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        String[] colunas = {"Data/Hora", "Equipa Casa", "Resultado", "Equipa Fora", "Estádio"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        atualizarTabela();

        JTable tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelFundo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelFundo.setOpaque(false);
        JButton btnAdicionar = criarBotaoEstilizado("+ Agendar Novo Jogo", new Color(41, 128, 185));
        painelFundo.add(btnAdicionar);
        add(painelFundo, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> mostrarFormularioAgendamento());
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Jogo j : BaseDeDados.jogos) {
            modeloTabela.addRow(new Object[]{j.getDataHora(), j.getEquipaCasa().getNome(), j.getResultadoFormatado(), j.getEquipaFora().getNome(), j.getEstadio()});
        }
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

    private void mostrarFormularioAgendamento() {
        if (BaseDeDados.selecoes.size() < 2 || BaseDeDados.estadios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Precisas de adicionar pelo menos 2 seleções e 1 estádio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agendar Novo Jogo", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(62, 39, 35));
        JLabel lblTitulo = new JLabel("Detalhes do Novo Jogo");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelTopo.add(lblTitulo);
        dialog.add(painelTopo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 15));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelForm.setOpaque(false);

        JSpinner spinnerData = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorData = new JSpinner.DateEditor(spinnerData, "yyyy-MM-dd HH:mm");
        spinnerData.setEditor(editorData);

        JComboBox<Selecao> comboCasa = new JComboBox<>(BaseDeDados.selecoes.toArray(new Selecao[0]));
        JComboBox<Selecao> comboFora = new JComboBox<>(BaseDeDados.selecoes.toArray(new Selecao[0]));
        JComboBox<Estadio> comboEstadio = new JComboBox<>(BaseDeDados.estadios.toArray(new Estadio[0]));

        painelForm.add(new JLabel("Data/Hora:")); painelForm.add(spinnerData);
        painelForm.add(new JLabel("Equipa da Casa:")); painelForm.add(comboCasa);
        painelForm.add(new JLabel("Equipa de Fora:")); painelForm.add(comboFora);
        painelForm.add(new JLabel("Estádio:")); painelForm.add(comboEstadio);
        dialog.add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setOpaque(false);
        JButton btnGuardar = criarBotaoEstilizado("Guardar", new Color(39, 174, 96));
        JButton btnCancelar = criarBotaoEstilizado("Cancelar", new Color(192, 57, 43));

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            Selecao casa = (Selecao) comboCasa.getSelectedItem();
            Selecao fora = (Selecao) comboFora.getSelectedItem();
            Estadio estadio = (Estadio) comboEstadio.getSelectedItem();

            if (casa == fora) {
                JOptionPane.showMessageDialog(dialog, "Uma seleção não pode jogar contra si própria!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.text.SimpleDateFormat formatador = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dataFormatada = formatador.format(spinnerData.getValue());

            BaseDeDados.jogos.add(new Jogo(dataFormatada, casa, fora, estadio.getNome()));
            BaseDeDados.salvarDados();
            atualizarTabela();
            dialog.dispose();
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnGuardar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}