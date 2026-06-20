package view.dialogo;

import model.Jogador;
import model.Selecao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PainelCampoLeitura extends JPanel {

    private static final Map<String, double[][]> TATICAS = new LinkedHashMap<>();
    private static final Map<String, String[]>   POSICOES = new LinkedHashMap<>();

    static {
        TATICAS.put("4-3-3", new double[][]{
                {0.50,0.90},{0.15,0.72},{0.35,0.72},{0.65,0.72},{0.85,0.72},
                {0.25,0.50},{0.50,0.50},{0.75,0.50},
                {0.20,0.25},{0.50,0.22},{0.80,0.25}
        });
        TATICAS.put("4-2-3-1", new double[][]{
                {0.50,0.90},{0.15,0.72},{0.35,0.72},{0.65,0.72},{0.85,0.72},
                {0.35,0.55},{0.65,0.55},
                {0.20,0.35},{0.50,0.35},{0.80,0.35},
                {0.50,0.15}
        });
        TATICAS.put("4-4-2", new double[][]{
                {0.50,0.90},{0.15,0.72},{0.35,0.72},{0.65,0.72},{0.85,0.72},
                {0.15,0.50},{0.38,0.50},{0.62,0.50},{0.85,0.50},
                {0.35,0.25},{0.65,0.25}
        });
        TATICAS.put("3-5-2", new double[][]{
                {0.50,0.90},{0.25,0.72},{0.50,0.72},{0.75,0.72},
                {0.10,0.50},{0.32,0.50},{0.50,0.50},{0.68,0.50},{0.90,0.50},
                {0.35,0.25},{0.65,0.25}
        });
        TATICAS.put("3-4-3", new double[][]{
                {0.50,0.90},{0.25,0.72},{0.50,0.72},{0.75,0.72},
                {0.18,0.52},{0.39,0.52},{0.61,0.52},{0.82,0.52},
                {0.20,0.25},{0.50,0.20},{0.80,0.25}
        });
        POSICOES.put("4-3-3",   new String[]{"GR","Def-Esq","Def-C Esq","Def-C Dir","Def-Dir","Méd-Esq","Méd-Centro","Méd-Dir","Av-Esq","Av-Centro","Av-Dir"});
        POSICOES.put("4-2-3-1", new String[]{"GR","Def-Esq","Def-C Esq","Def-C Dir","Def-Dir","Méd-Def Esq","Méd-Def Dir","Méd-Ata Esq","Méd-Ata","Méd-Ata Dir","Avançado"});
        POSICOES.put("4-4-2",   new String[]{"GR","Def-Esq","Def-C Esq","Def-C Dir","Def-Dir","Méd-Esq","Méd-C Esq","Méd-C Dir","Méd-Dir","Av-Esq","Av-Dir"});
        POSICOES.put("3-5-2",   new String[]{"GR","Def-Esq","Def-Central","Def-Dir","Méd-Esq","Méd-C Esq","Méd-Centro","Méd-C Dir","Méd-Dir","Av-Esq","Av-Dir"});
        POSICOES.put("3-4-3",   new String[]{"GR","Def-Esq","Def-Central","Def-Dir","Méd-Esq","Méd-C Esq","Méd-C Dir","Méd-Dir","Av-Esq","Av-Centro","Av-Dir"});
    }

    private final Selecao selecao;
    private int hoveredIdx = -1;

    public PainelCampoLeitura(Selecao selecao) {
        this.selecao = selecao;
        setPreferredSize(new Dimension(580, 420));
        setOpaque(false);

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int idx = getIdxPerto(e.getX(), e.getY());
                if (idx != hoveredIdx) {
                    hoveredIdx = idx;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                hoveredIdx = -1;
                repaint();
            }
        });
    }

    private int getIdxPerto(int mx, int my) {
        double[][] coords = TATICAS.getOrDefault(selecao.getTatica(), TATICAS.get("4-3-3"));
        int w = getWidth(), h = getHeight();
        for (int i = 0; i < coords.length; i++) {
            int cx = (int)(coords[i][0] * w);
            int cy = (int)(coords[i][1] * h);
            if (Math.hypot(mx - cx, my - cy) <= 30) return i;
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        // ── Relva com faixas ──────────────────────────────────
        Color v1 = new Color(28, 100, 28), v2 = new Color(22, 80, 22);
        int faixas = 8;
        for (int i = 0; i < faixas; i++) {
            g2.setColor(i % 2 == 0 ? v1 : v2);
            g2.fillRect(0, i * h / faixas, w, h / faixas);
        }

        // ── Linhas do campo ───────────────────────────────────
        g2.setColor(new Color(255, 255, 255, 160));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRect(18, 12, w - 36, h - 24);
        g2.drawLine(18, h/2, w - 18, h/2);
        g2.drawOval(w/2 - 38, h/2 - 38, 76, 76);
        g2.fillOval(w/2 - 3, h/2 - 3, 6, 6);

        int aW = (int)(w * 0.55), aH = (int)(h * 0.13);
        g2.drawRect((w - aW)/2, 12, aW, aH);
        g2.drawRect((w - aW)/2, h - 12 - aH, aW, aH);
        int pW = (int)(w * 0.28), pH = (int)(h * 0.06);
        g2.drawRect((w - pW)/2, 12, pW, pH);
        g2.drawRect((w - pW)/2, h - 12 - pH, pW, pH);

        // ── Jogadores ─────────────────────────────────────────
        List<Jogador> titulares = selecao.getTitulares();
        double[][] coords = TATICAS.getOrDefault(selecao.getTatica(), TATICAS.get("4-3-3"));

        for (int i = 0; i < Math.min(titulares.size(), coords.length); i++) {
            Jogador j = titulares.get(i);
            int cx = (int)(coords[i][0] * w);
            int cy = (int)(coords[i][1] * h);
            boolean hov = (i == hoveredIdx);
            int raio = hov ? 26 : 20;

            // Sombra
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillOval(cx - raio + 2, cy - raio + 2, raio * 2, raio * 2);

            // Círculo
            if (i == 0) {
                g2.setColor(hov ? new Color(255, 210, 0) : new Color(220, 170, 0));
            } else {
                g2.setColor(hov ? new Color(255, 80, 80) : new Color(200, 50, 50));
            }
            g2.fillOval(cx - raio, cy - raio, raio * 2, raio * 2);

            // Borda
            g2.setColor(hov ? Color.WHITE : new Color(255, 255, 255, 180));
            g2.setStroke(new BasicStroke(hov ? 2.5f : 1.5f));
            g2.drawOval(cx - raio, cy - raio, raio * 2, raio * 2);

            // Nome
            String nome = j.getNome();
            if (!hov && nome.length() > 8) nome = nome.substring(0, 7) + ".";
            Font fnt = hov
                    ? new Font("Segoe UI", Font.BOLD, 11)
                    : new Font("Segoe UI", Font.BOLD, 9);
            g2.setFont(fnt);
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(nome);

            // Fundo do texto
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(cx - tw/2 - 3, cy + raio + 2, tw + 6, 13, 4, 4);
            g2.setColor(Color.WHITE);
            g2.drawString(nome, cx - tw/2, cy + raio + 12);
        }

        g2.dispose();
    }
}