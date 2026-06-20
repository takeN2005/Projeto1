package view.dialogo;

import model.Jogador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PainelCampo extends JPanel {

    // Posições para cada tática: {linha, coluna} normalizadas 0.0-1.0
    // x = horizontal (0=esq, 1=dir), y = vertical (0=baliza de cima, 1=baixo)
    private static final Map<String, double[][]> TATICAS = new LinkedHashMap<>();

    static {
        // Guarda-redes sempre em [0] — posição fixa
        // Formato: {x, y} normalizados — y=0.92 é GR (perto da baliza de baixo)
        TATICAS.put("4-3-3", new double[][]{
                {0.50, 0.90}, // GR
                {0.15, 0.72}, // Def-Esq
                {0.35, 0.72}, // Def-Central Esq
                {0.65, 0.72}, // Def-Central Dir
                {0.85, 0.72}, // Def-Dir
                {0.25, 0.50}, // Med-Esq
                {0.50, 0.50}, // Med-Centro
                {0.75, 0.50}, // Med-Dir
                {0.20, 0.25}, // Av-Esq
                {0.50, 0.22}, // Av-Centro
                {0.80, 0.25}, // Av-Dir
        });
        TATICAS.put("4-2-3-1", new double[][]{
                {0.50, 0.90}, // GR
                {0.15, 0.72}, // Def-Esq
                {0.35, 0.72}, // Def-Central Esq
                {0.65, 0.72}, // Def-Central Dir
                {0.85, 0.72}, // Def-Dir
                {0.35, 0.55}, // Med-Def Esq
                {0.65, 0.55}, // Med-Def Dir
                {0.20, 0.35}, // Med-Ata Esq
                {0.50, 0.35}, // Med-Ata Centro
                {0.80, 0.35}, // Med-Ata Dir
                {0.50, 0.15}, // Avançado
        });
        TATICAS.put("4-4-2", new double[][]{
                {0.50, 0.90}, // GR
                {0.15, 0.72}, // Def-Esq
                {0.35, 0.72}, // Def-Central Esq
                {0.65, 0.72}, // Def-Central Dir
                {0.85, 0.72}, // Def-Dir
                {0.15, 0.50}, // Med-Esq
                {0.38, 0.50}, // Med-Centro Esq
                {0.62, 0.50}, // Med-Centro Dir
                {0.85, 0.50}, // Med-Dir
                {0.35, 0.25}, // Avançado Esq
                {0.65, 0.25}, // Avançado Dir
        });
        TATICAS.put("3-5-2", new double[][]{
                {0.50, 0.90}, // GR
                {0.25, 0.72}, // Def-Esq
                {0.50, 0.72}, // Def-Central
                {0.75, 0.72}, // Def-Dir
                {0.10, 0.50}, // Med-Esq
                {0.32, 0.50}, // Med-Centro Esq
                {0.50, 0.50}, // Med-Centro
                {0.68, 0.50}, // Med-Centro Dir
                {0.90, 0.50}, // Med-Dir
                {0.35, 0.25}, // Avançado Esq
                {0.65, 0.25}, // Avançado Dir
        });
        TATICAS.put("3-4-3", new double[][]{
                {0.50, 0.90}, // GR
                {0.25, 0.72}, // Def-Esq
                {0.50, 0.72}, // Def-Central
                {0.75, 0.72}, // Def-Dir
                {0.18, 0.52}, // Med-Esq
                {0.39, 0.52}, // Med-Centro Esq
                {0.61, 0.52}, // Med-Centro Dir
                {0.82, 0.52}, // Med-Dir
                {0.20, 0.25}, // Av-Esq
                {0.50, 0.20}, // Av-Centro
                {0.80, 0.25}, // Av-Dir
        });
    }

    private static final String[] NOMES_POSICOES_433    = {"GR","Def-Esq","Def-C Esq","Def-C Dir","Def-Dir","Méd-Esq","Méd-Centro","Méd-Dir","Av-Esq","Av-Centro","Av-Dir"};
    private static final String[] NOMES_POSICOES_4231   = {"GR","Def-Esq","Def-C Esq","Def-C Dir","Def-Dir","Méd-Def Esq","Méd-Def Dir","Méd-Ata Esq","Méd-Ata","Méd-Ata Dir","Avançado"};
    private static final String[] NOMES_POSICOES_442    = {"GR","Def-Esq","Def-C Esq","Def-C Dir","Def-Dir","Méd-Esq","Méd-C Esq","Méd-C Dir","Méd-Dir","Av-Esq","Av-Dir"};
    private static final String[] NOMES_POSICOES_352    = {"GR","Def-Esq","Def-Central","Def-Dir","Méd-Esq","Méd-C Esq","Méd-Centro","Méd-C Dir","Méd-Dir","Av-Esq","Av-Dir"};
    private static final String[] NOMES_POSICOES_343    = {"GR","Def-Esq","Def-Central","Def-Dir","Méd-Esq","Méd-C Esq","Méd-C Dir","Méd-Dir","Av-Esq","Av-Centro","Av-Dir"};

    private static final Map<String, String[]> NOMES_POSICOES = new LinkedHashMap<>();
    static {
        NOMES_POSICOES.put("4-3-3",   NOMES_POSICOES_433);
        NOMES_POSICOES.put("4-2-3-1", NOMES_POSICOES_4231);
        NOMES_POSICOES.put("4-4-2",   NOMES_POSICOES_442);
        NOMES_POSICOES.put("3-5-2",   NOMES_POSICOES_352);
        NOMES_POSICOES.put("3-4-3",   NOMES_POSICOES_343);
    }

    private String tatica;
    private String[] nomeJogadores = new String[11];
    private int posicaoSelecionada = -1;

    public PainelCampo(String tatica) {
        this.tatica = tatica;
        setPreferredSize(new Dimension(400, 560));
        setBackground(new Color(34, 139, 34));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = getPosicaoClicada(e.getX(), e.getY());
                if (idx >= 0) {
                    posicaoSelecionada = idx;
                    pedirNomeJogador(idx);
                }
            }
        });
    }

    public void setTatica(String tatica) {
        this.tatica = tatica;
        this.nomeJogadores = new String[11];
        repaint();
    }

    public String[] getNomeJogadores() { return nomeJogadores; }

    public String[] getNomesPosicoes() {
        return NOMES_POSICOES.getOrDefault(tatica, NOMES_POSICOES_433);
    }

    public boolean todosPreenchidos() {
        for (String n : nomeJogadores)
            if (n == null || n.isBlank()) return false;
        return true;
    }

    private void pedirNomeJogador(int idx) {
        String[] posNames = getNomesPosicoes();
        String posicao = posNames[idx];
        String atual = nomeJogadores[idx] != null ? nomeJogadores[idx] : "";

        JTextField txtNome = new JTextField(atual, 20);
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 6));
        p.add(new JLabel("Nome do jogador para: " + posicao));
        p.add(txtNome);

        int res = JOptionPane.showConfirmDialog(
                this, p, "Preencher posição",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            String nome = txtNome.getText().trim();
            if (!nome.isBlank()) {
                nomeJogadores[idx] = nome;
                repaint();
            }
        }
    }

    private int getPosicaoClicada(int mx, int my) {
        double[][] coords = TATICAS.getOrDefault(tatica, TATICAS.get("4-3-3"));
        int w = getWidth(), h = getHeight();
        int raio = 28;
        for (int i = 0; i < coords.length; i++) {
            int cx = (int)(coords[i][0] * w);
            int cy = (int)(coords[i][1] * h);
            if (Math.hypot(mx - cx, my - cy) <= raio) return i;
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        // ── Relva com faixas ──────────────────────────────────
        Color verde1 = new Color(34, 139, 34);
        Color verde2 = new Color(28, 120, 28);
        int faixas = 8;
        for (int i = 0; i < faixas; i++) {
            g2.setColor(i % 2 == 0 ? verde1 : verde2);
            g2.fillRect(0, i * h / faixas, w, h / faixas);
        }

        // ── Linhas do campo ───────────────────────────────────
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(1.5f));

        // Contorno
        g2.drawRect(20, 15, w - 40, h - 30);
        // Linha do meio
        g2.drawLine(20, h / 2, w - 20, h / 2);
        // Círculo central
        g2.drawOval(w / 2 - 40, h / 2 - 40, 80, 80);
        g2.fillOval(w / 2 - 3, h / 2 - 3, 6, 6);

        // Área grande — topo
        int areaW = (int)(w * 0.55), areaH = (int)(h * 0.12);
        g2.drawRect((w - areaW) / 2, 15, areaW, areaH);
        // Área grande — fundo
        g2.drawRect((w - areaW) / 2, h - 15 - areaH, areaW, areaH);

        // Área pequena — topo
        int peqW = (int)(w * 0.28), peqH = (int)(h * 0.055);
        g2.drawRect((w - peqW) / 2, 15, peqW, peqH);
        // Área pequena — fundo
        g2.drawRect((w - peqW) / 2, h - 15 - peqH, peqW, peqH);

        // ── Jogadores ─────────────────────────────────────────
        double[][] coords = TATICAS.getOrDefault(tatica, TATICAS.get("4-3-3"));
        String[] posNames = getNomesPosicoes();

        for (int i = 0; i < coords.length; i++) {
            int cx = (int)(coords[i][0] * w);
            int cy = (int)(coords[i][1] * h);

            boolean preenchido = nomeJogadores[i] != null && !nomeJogadores[i].isBlank();
            boolean selecionado = (i == posicaoSelecionada);

            // Sombra
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillOval(cx - 20 + 2, cy - 20 + 2, 40, 40);

            // Círculo do jogador
            if (i == 0) {
                // Guarda-redes — amarelo
                g2.setColor(preenchido ? new Color(230, 180, 0) : new Color(180, 130, 0, 150));
            } else {
                g2.setColor(preenchido ? new Color(220, 50, 50) : new Color(160, 50, 50, 150));
            }
            g2.fillOval(cx - 20, cy - 20, 40, 40);

            // Borda (branca se selecionado, cinza se não)
            g2.setColor(selecionado ? Color.YELLOW : Color.WHITE);
            g2.setStroke(new BasicStroke(selecionado ? 2.5f : 1.5f));
            g2.drawOval(cx - 20, cy - 20, 40, 40);

            // Nome ou posição
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            String label = preenchido ? nomeJogadores[i] : posNames[i];
            // Truncar se muito longo
            if (label.length() > 9) label = label.substring(0, 8) + ".";

            FontMetrics fm = g2.getFontMetrics();
            int lw = fm.stringWidth(label);

            // Fundo do texto
            g2.setColor(new Color(0, 0, 0, 140));
            g2.fillRoundRect(cx - lw/2 - 3, cy + 22, lw + 6, 14, 4, 4);

            g2.setColor(Color.WHITE);
            g2.drawString(label, cx - lw/2, cy + 33);
        }
    }

    public static Set<String> getTaticasDisponiveis() {
        return TATICAS.keySet();
    }
}