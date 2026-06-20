package view;

import model.Alojamento;
import model.BaseDeDados;
import model.Selecao;
import model.Transporte;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PainelLogistica extends JPanel {

    private static final Color BG_MAIN   = new Color(245, 242, 238);
    private static final Color BG_CARD   = new Color(255, 255, 255);
    private static final Color BG_HEADER = new Color(62, 39, 35);
    private static final Color BG_BANNER = new Color(250, 240, 225);
    private static final Color TOGGLE_BG = new Color(255, 255, 255);
    private static final Color TOGGLE_SEL= new Color(78, 52, 46);
    private static final Color BROWN     = new Color(62, 39, 35);
    private static final Color TEXT_PRI  = new Color(50, 40, 38);
    private static final Color TEXT_SEC  = new Color(130, 115, 108);
    private static final Color BORDER    = new Color(220, 210, 200);
    private static final Color GREEN_OK  = new Color(40, 150, 80);
    private static final Color GREY_OFF  = new Color(150, 150, 150);

    private static final Map<String, String> CODIGOS_PAIS = new HashMap<>();
    private static final Map<String, String[][]> HOTEIS_POR_PAIS = new LinkedHashMap<>();
    static {
        CODIGOS_PAIS.put("EUA", "US");
        CODIGOS_PAIS.put("México", "MX");
        CODIGOS_PAIS.put("Canadá", "CA");
        CODIGOS_PAIS.put("Portugal", "PT");
        CODIGOS_PAIS.put("Espanha", "ES");
        CODIGOS_PAIS.put("França", "FR");
        CODIGOS_PAIS.put("Inglaterra", "EN");

        HOTEIS_POR_PAIS.put("EUA", new String[][]{
                {"The Plaza Hotel", "Nova Iorque"}, {"Beverly Hills Hotel", "Los Angeles"},
                {"The Ritz-Carlton Dallas", "Dallas"}, {"Four Seasons Atlanta", "Atlanta"},
                {"The Rittenhouse Hotel", "Filadélfia"}, {"Fairmont Olympic Hotel", "Seattle"},
                {"Hotel Valencia Santa Clara", "Santa Clara"}, {"The Fontaine", "Kansas City"},
                {"Boston Harbor Hotel", "Boston"}, {"Fontainebleau Miami Beach", "Miami"}
        });
        HOTEIS_POR_PAIS.put("México", new String[][]{
                {"Four Seasons Mexico City", "Cidade do México"}, {"Hotel Demetria", "Guadalajara"},
                {"Quinta Real Monterrey", "Monterrey"}, {"Hotel Doña Urraca", "Querétaro"},
                {"Holiday Inn Monterrey Centro", "Monterrey"}, {"Hotel Riu Plaza Guadalajara", "Guadalajara"},
                {"Camino Real Polanco", "Cidade do México"}, {"Hotel Ciudad de Pachuca", "Pachuca"},
                {"Hotel Real de Minas León", "León"}, {"Hotel Mesón del Ángel", "Puebla"}
        });
        HOTEIS_POR_PAIS.put("Canadá", new String[][]{
                {"Fairmont Pacific Rim", "Vancouver"}, {"Fairmont Royal York", "Toronto"},
                {"Fairmont Hotel Macdonald", "Edmonton"}, {"Hotel Arts Calgary", "Calgary"},
                {"Hotel Saskatchewan", "Regina"}, {"Fairmont Winnipeg", "Winnipeg"},
                {"Lord Elgin Hotel", "Otava"}, {"Sheraton Hamilton", "Hamilton"},
                {"Fairmont The Queen Elizabeth", "Montreal"}, {"Hotel Bonaventure Montreal", "Montreal"}
        });
    }

    private boolean abaAlojamentos = true;
    private JButton btnAlojamentos, btnTransportes, btnNovo;
    private JPanel grelha;

    public PainelLogistica() {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_MAIN);

        add(buildCabecalho(), BorderLayout.NORTH);

        grelha = new JPanel(new GridLayout(0, 2, 16, 16));
        grelha.setBackground(BG_MAIN);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_MAIN);
        wrapper.add(grelha, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        atualizarConteudo();
    }

    private JPanel buildCabecalho() {
        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Logística");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(BROWN);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        topo.add(lblTitulo);

        JLabel lblSub = new JLabel("Gerir alojamentos e transportes das seleções");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_SEC);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSub.setBorder(new EmptyBorder(2, 0, 12, 0));
        topo.add(lblSub);

        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(BG_BANNER);
        banner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 200, 170)),
                new EmptyBorder(10, 14, 10, 14)));
        JLabel lblTexto = new JLabel(
                "<html><b style='color:#3e2723;'>Prioridade de Alojamento</b><br>" +
                        "<span style='color:#7d655c;'>Regra de negócio planeada: seleções com melhor desempenho " +
                        "terão prioridade na extensão de reservas. Ainda não aplicada automaticamente.</span></html>");
        banner.add(lblTexto, BorderLayout.CENTER);
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        topo.add(banner);
        topo.add(Box.createVerticalStrut(14));

        JPanel linha = new JPanel(new BorderLayout());
        linha.setOpaque(false);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnAlojamentos = criarToggle("Alojamentos", true);
        btnTransportes = criarToggle("Transportes", false);
        JPanel pill = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        pill.setBackground(TOGGLE_BG);
        pill.setBorder(BorderFactory.createLineBorder(BORDER));
        pill.add(btnAlojamentos);
        pill.add(btnTransportes);
        linha.add(pill, BorderLayout.WEST);

        btnNovo = criarBotaoPrincipal("+ Novo Alojamento");
        linha.add(btnNovo, BorderLayout.EAST);
        topo.add(linha);

        btnAlojamentos.addActionListener(e -> trocarAba(true));
        btnTransportes.addActionListener(e -> trocarAba(false));
        btnNovo.addActionListener(e -> {
            if (abaAlojamentos) mostrarFormularioAlojamento();
            else                mostrarFormularioTransporte();
        });

        return topo;
    }

    private void trocarAba(boolean alojamentos) {
        abaAlojamentos = alojamentos;
        estilizarToggle(btnAlojamentos, alojamentos);
        estilizarToggle(btnTransportes, !alojamentos);
        btnNovo.setText(alojamentos ? "+ Novo Alojamento" : "+ Novo Transporte");
        atualizarConteudo();
    }

    public void atualizarConteudo() {
        grelha.removeAll();
        if (abaAlojamentos) {
            if (BaseDeDados.alojamentos.isEmpty()) grelha.add(cartaoVazio("Nenhum alojamento registado."));
            else for (Alojamento a : BaseDeDados.alojamentos) grelha.add(cartaoAlojamento(a));
        } else {
            if (BaseDeDados.transportes.isEmpty()) grelha.add(cartaoVazio("Nenhum transporte registado."));
            else for (Transporte t : BaseDeDados.transportes) grelha.add(cartaoTransporte(t));
        }
        grelha.revalidate();
        grelha.repaint();
    }

    private JPanel cartaoVazio(String msg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createLineBorder(BORDER));
        JLabel l = new JLabel(msg);
        l.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        l.setForeground(TEXT_SEC);
        p.add(l);
        return p;
    }

    private JPanel cartaoAlojamento(Alojamento a) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(14, 16, 14, 16)));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setOpaque(false);
        topo.add(badgePais(a.getSelecao().getNome()));
        JLabel lblNome = new JLabel(a.getSelecao().getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNome.setForeground(TEXT_PRI);
        topo.add(lblNome);
        card.add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);
        centro.add(badgeEstado(a.isAtivo()));
        centro.add(Box.createVerticalStrut(8));

        JLabel lblHotel = new JLabel(a.getHotel());
        lblHotel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHotel.setForeground(TEXT_PRI);
        lblHotel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblHotel);

        JLabel lblCidade = new JLabel(a.getCidade() + "  ·  " + a.getTipo());
        lblCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCidade.setForeground(TEXT_SEC);
        lblCidade.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblCidade);

        centro.add(Box.createVerticalStrut(6));
        JLabel lblData = new JLabel(a.getDataInicio() + "  —  " + a.getDataFim());
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblData.setForeground(TEXT_SEC);
        lblData.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblData);

        card.add(centro, BorderLayout.CENTER);

        JButton btnRemover = criarBotaoRemover();
        btnRemover.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this,
                    "Remover este alojamento?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                BaseDeDados.alojamentos.remove(a);
                BaseDeDados.salvarDados();
                atualizarConteudo();
            }
        });
        card.add(btnRemover, BorderLayout.SOUTH);
        return card;
    }

    private JPanel cartaoTransporte(Transporte t) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(14, 16, 14, 16)));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setOpaque(false);
        topo.add(badgePais(t.getSelecao().getNome()));
        JLabel lblNome = new JLabel(t.getSelecao().getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNome.setForeground(TEXT_PRI);
        topo.add(lblNome);
        card.add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        JLabel lblTipo = new JLabel(t.getTipo());
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTipo.setForeground(BROWN);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblTipo);
        centro.add(Box.createVerticalStrut(6));

        JLabel lblRota = new JLabel(t.getOrigem() + "   →   " + t.getDestino());
        lblRota.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRota.setForeground(TEXT_PRI);
        lblRota.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblRota);

        centro.add(Box.createVerticalStrut(6));
        JLabel lblData = new JLabel(t.getDataHora());
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblData.setForeground(TEXT_SEC);
        lblData.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblData);

        card.add(centro, BorderLayout.CENTER);

        JButton btnRemover = criarBotaoRemover();
        btnRemover.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this,
                    "Remover este transporte?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                BaseDeDados.transportes.remove(t);
                BaseDeDados.salvarDados();
                atualizarConteudo();
            }
        });
        card.add(btnRemover, BorderLayout.SOUTH);
        return card;
    }

    private void mostrarFormularioAlojamento() {
        if (BaseDeDados.selecoes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Regista pelo menos uma seleção primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Alojamento", true);
        dlg.setSize(440, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(BG_MAIN);
        dlg.add(cabecalhoDialogo("Registar Alojamento"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 14));
        form.setBackground(BG_MAIN);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        JComboBox<Selecao> comboSelecao = estilizarCombo(new JComboBox<>(BaseDeDados.selecoes.toArray(new Selecao[0])));
        JComboBox<String> comboPais = estilizarCombo(new JComboBox<>(HOTEIS_POR_PAIS.keySet().toArray(new String[0])));
        JComboBox<String> comboHotel = estilizarCombo(new JComboBox<>());
        JSpinner spInicio = criarSpinnerData();
        JSpinner spFim    = criarSpinnerData();

        atualizarHoteisCombo(comboHotel, (String) comboPais.getSelectedItem());
        comboPais.addActionListener(e -> atualizarHoteisCombo(comboHotel, (String) comboPais.getSelectedItem()));

        form.add(labelForm("Seleção:"));   form.add(comboSelecao);
        form.add(labelForm("País:"));      form.add(comboPais);
        form.add(labelForm("Hotel:"));     form.add(comboHotel);
        form.add(labelForm("Check-in:"));  form.add(spInicio);
        form.add(labelForm("Check-out:")); form.add(spFim);
        dlg.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setBackground(BG_HEADER);
        JButton btnCancelar = criarBotaoAcao("Cancelar", new Color(192, 57, 43));
        JButton btnGuardar  = criarBotaoAcao("Guardar",  new Color(39, 174, 96));
        botoes.add(btnCancelar);
        botoes.add(btnGuardar);
        dlg.add(botoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dlg.dispose());
        btnGuardar.addActionListener(e -> {
            Date dIni = (Date) spInicio.getValue();
            Date dFim = (Date) spFim.getValue();
            if (dFim.before(dIni)) {
                JOptionPane.showMessageDialog(dlg,
                        "A data de check-out não pode ser anterior ao check-in.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String hotelSel = (String) comboHotel.getSelectedItem();
            if (hotelSel == null) {
                JOptionPane.showMessageDialog(dlg, "Seleciona um hotel.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String[] partes = hotelSel.split(" — ");

            Selecao sel = (Selecao) comboSelecao.getSelectedItem();
            BaseDeDados.alojamentos.add(new Alojamento(
                    sel, partes[0], partes.length > 1 ? partes[1] : "", "Hotel",
                    formatarData(spInicio), formatarData(spFim)));
            BaseDeDados.salvarDados();
            atualizarConteudo();
            dlg.dispose();
        });

        dlg.setVisible(true);
    }

    private void atualizarHoteisCombo(JComboBox<String> comboHotel, String pais) {
        comboHotel.removeAllItems();
        for (String[] h : HOTEIS_POR_PAIS.getOrDefault(pais, new String[0][])) {
            comboHotel.addItem(h[0] + " — " + h[1]);
        }
    }

    private void mostrarFormularioTransporte() {
        if (BaseDeDados.selecoes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Regista pelo menos uma seleção primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Transporte", true);
        dlg.setSize(440, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(BG_MAIN);
        dlg.add(cabecalhoDialogo("Registar Transporte"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 14));
        form.setBackground(BG_MAIN);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        JComboBox<Selecao> comboSelecao = estilizarCombo(new JComboBox<>(BaseDeDados.selecoes.toArray(new Selecao[0])));
        JComboBox<String> comboTipo = estilizarCombo(new JComboBox<>(new String[]{"Voo", "Autocarro", "Carro"}));
        JTextField txtOrigem  = campoTexto();
        JTextField txtDestino = campoTexto();
        JSpinner spData = criarSpinnerDataHora();

        form.add(labelForm("Seleção:"));   form.add(comboSelecao);
        form.add(labelForm("Tipo:"));      form.add(comboTipo);
        form.add(labelForm("Origem:"));    form.add(txtOrigem);
        form.add(labelForm("Destino:"));   form.add(txtDestino);
        form.add(labelForm("Data/Hora:")); form.add(spData);
        dlg.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setBackground(BG_HEADER);
        JButton btnCancelar = criarBotaoAcao("Cancelar", new Color(192, 57, 43));
        JButton btnGuardar  = criarBotaoAcao("Guardar",  new Color(39, 174, 96));
        botoes.add(btnCancelar);
        botoes.add(btnGuardar);
        dlg.add(botoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dlg.dispose());
        btnGuardar.addActionListener(e -> {
            String origem  = txtOrigem.getText().trim();
            String destino = txtDestino.getText().trim();
            if (origem.isEmpty() || destino.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Preenche todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Selecao sel = (Selecao) comboSelecao.getSelectedItem();
            String tipo = (String) comboTipo.getSelectedItem();
            String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(spData.getValue());

            BaseDeDados.transportes.add(new Transporte(sel, tipo, origem, destino, dataHora));
            BaseDeDados.salvarDados();
            atualizarConteudo();
            dlg.dispose();
        });

        dlg.setVisible(true);
    }

    private JLabel badgePais(String nomePais) {
        String codigo = CODIGOS_PAIS.getOrDefault(nomePais,
                nomePais.length() >= 2 ? nomePais.substring(0, 2).toUpperCase() : nomePais.toUpperCase());
        JLabel lbl = new JLabel(codigo);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(230, 215, 195));
        lbl.setForeground(BROWN);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
        return lbl;
    }

    private JLabel badgeEstado(boolean ativo) {
        JLabel lbl = new JLabel(ativo ? "Ativa" : "Inativa");
        lbl.setForeground(ativo ? GREEN_OK : GREY_OFF);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton criarBotaoRemover() {
        JButton btn = new JButton("Remover");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(192, 57, 43));
        btn.setBackground(new Color(250, 235, 233));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 195, 190)), new EmptyBorder(6, 0, 6, 0)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel cabecalhoDialogo(String titulo) {
        JPanel cab = new JPanel();
        cab.setBackground(BG_HEADER);
        cab.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        cab.add(lbl);
        return cab;
    }

    private JButton criarToggle(String texto, boolean ativo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarToggle(btn, ativo);
        return btn;
    }

    private void estilizarToggle(JButton btn, boolean ativo) {
        btn.setBackground(ativo ? TOGGLE_SEL : TOGGLE_BG);
        btn.setForeground(ativo ? Color.WHITE : TEXT_SEC);
    }

    private JButton criarBotaoPrincipal(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(39, 174, 96));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(49, 184, 106)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(new Color(39, 174, 96)); }
        });
        return btn;
    }

    private JButton criarBotaoAcao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField campoTexto() {
        JTextField tf = new JTextField();
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_PRI);
        tf.setCaretColor(BROWN);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    private <T> JComboBox<T> estilizarCombo(JComboBox<T> cb) {
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRI);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return cb;
    }

    private JLabel labelForm(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT_SEC);
        return l;
    }

    private JSpinner criarSpinnerData() {
        JSpinner sp = new JSpinner(new SpinnerDateModel());
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy"));
        sp.setBackground(BG_CARD);
        sp.setForeground(TEXT_PRI);
        return sp;
    }

    private JSpinner criarSpinnerDataHora() {
        JSpinner sp = new JSpinner(new SpinnerDateModel());
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy HH:mm"));
        sp.setBackground(BG_CARD);
        sp.setForeground(TEXT_PRI);
        return sp;
    }

    private String formatarData(JSpinner sp) {
        return new SimpleDateFormat("dd/MM/yyyy").format(sp.getValue());
    }
}