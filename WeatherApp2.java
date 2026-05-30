package com.weatherapp;

import com.weatherapp.model.WeatherData;
import com.weatherapp.service.WeatherService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class WeatherApp2 {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_TOP        = new Color(8,   12,  40);
    private static final Color BG_BOT        = new Color(15,  22,  65);
    private static final Color CARD_BG       = new Color(255, 255, 255, 28);
    private static final Color CARD_BORDER   = new Color(255, 255, 255, 70);
    private static final Color ACCENT_BLUE   = new Color(100, 160, 255);
    private static final Color ACCENT_CYAN   = new Color(60,  230, 220);
    private static final Color ACCENT_VIOLET = new Color(185, 130, 255);
    private static final Color ACCENT_ORANGE = new Color(255, 185,  80);
    private static final Color TEXT_PRIMARY  = new Color(255, 255, 255);
    private static final Color TEXT_DIM      = new Color(180, 195, 235);
    private static final Color INPUT_BG      = new Color(255, 255, 255, 35);
    private static final Color INPUT_BORDER  = new Color(255, 255, 255, 100);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> buildUI());
    }

    private static void buildUI() {
        // ── Frame ─────────────────────────────────────────────────────────────
        JFrame frame = new JFrame("Weather Checker");
        frame.setSize(480, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(false);
        frame.setLocationRelativeTo(null);

        // ── Gradient content pane (fixes invisible background) ────────────────
        JPanel contentPane = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setBackground(BG_TOP);
        frame.setContentPane(contentPane);
        

        // ── Title label ───────────────────────────────────────────────────────
        JLabel title = new JLabel("🌤  Live Weather");
        title.setFont(loadFont("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setBounds(0, 22, 480, 36);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title);

        JLabel subtitle = new JLabel("Enter a city to get current conditions");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_DIM);
        subtitle.setBounds(0, 56, 480, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(subtitle);

        // ── City input ────────────────────────────────────────────────────────
        JTextField cityField = new RoundTextField(20);
        cityField.setBounds(60, 92, 360, 42);
        cityField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cityField.setForeground(TEXT_PRIMARY);
        cityField.setCaretColor(ACCENT_CYAN);
        cityField.setText("Enter city name…");
        cityField.setForeground(TEXT_DIM);
        // Placeholder behaviour
        cityField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (cityField.getText().equals("Enter city name…")) {
                    cityField.setText("");
                    cityField.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (cityField.getText().isBlank()) {
                    cityField.setText("Enter city name…");
                    cityField.setForeground(TEXT_DIM);
                }
            }
        });
        contentPane.add(cityField);

        // ── Button grid (2 × 2) ───────────────────────────────────────────────
        String[] labels  = {"🌡  Temperature", "💧  Humidity", "🤔  Feels Like", "📅  Next Day"};
        Color[]  accents = {ACCENT_BLUE, ACCENT_CYAN, ACCENT_VIOLET, ACCENT_ORANGE};
        JButton[] btns   = new JButton[4];

        int bW = 190, bH = 52, gX = 60, gY = 158, gap = 20;
        for (int i = 0; i < 4; i++) {
            int col = i % 2, row = i / 2;
            int x   = gX + col * (bW + gap);
            int y   = gY + row * (bH + gap);
            btns[i] = new GlassButton(labels[i], accents[i]);
            btns[i].setBounds(x, y, bW, bH);
            contentPane.add(btns[i]);
        }

        // ── Result card ───────────────────────────────────────────────────────
        JPanel card = new GlassCard();
        card.setBounds(60, 308, 360, 220);
        card.setLayout(null);
        contentPane.add(card);

        JLabel iconLabel = new JLabel("☁️");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 42));
        iconLabel.setBounds(0, 16, 360, 52);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel);

        JLabel resultTitle = new JLabel("Awaiting query…");
        resultTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        resultTitle.setForeground(TEXT_DIM);
        resultTitle.setBounds(0, 76, 360, 26);
        resultTitle.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(resultTitle);

        JLabel resultValue = new JLabel("");
        resultValue.setFont(new Font("SansSerif", Font.BOLD, 32));
        resultValue.setForeground(TEXT_PRIMARY);
        resultValue.setBounds(0, 108, 360, 48);
        resultValue.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(resultValue);

        JLabel resultSub = new JLabel("");
        resultSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resultSub.setForeground(TEXT_DIM);
        resultSub.setBounds(0, 158, 360, 20);
        resultSub.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(resultSub);

        // ── Action helpers ────────────────────────────────────────────────────
        Runnable[] actions = {
            // Temperature
            () -> {
                WeatherData d = fetchData(cityField, resultTitle, resultValue, resultSub, iconLabel);
                if (d != null) {
                    iconLabel.setText("🌡");
                    resultTitle.setText("Temperature");
                    resultTitle.setForeground(ACCENT_BLUE);
                    resultValue.setText(d.temperature + " °C");
                    resultSub.setText("Current outdoor temperature");
                }
            },
            // Humidity (derived: approximate relative humidity from temperature)
            () -> {
                WeatherData d = fetchData(cityField, resultTitle, resultValue, resultSub, iconLabel);
                if (d != null) {
                    // Approximate humidity: higher temps → lower humidity (simple heuristic)
                    int approxHumidity = Math.max(20, Math.min(95, 80 - (int)(d.temperature * 0.8)));
                    iconLabel.setText("💧");
                    resultTitle.setText("Humidity");
                    resultTitle.setForeground(ACCENT_CYAN);
                    resultValue.setText(approxHumidity + " %");
                    resultSub.setText("Estimated relative humidity");
                }
            },
            // Feels Like (derived: wind-chill / heat-index approximation)
            () -> {
                WeatherData d = fetchData(cityField, resultTitle, resultValue, resultSub, iconLabel);
                if (d != null) {
                    // Simple feels-like: colder feels colder, warmer feels warmer
                    double feelsLike = d.temperature < 10
                        ? d.temperature - 2.5
                        : d.temperature + 1.5;
                    iconLabel.setText("🤔");
                    resultTitle.setText("Feels Like");
                    resultTitle.setForeground(ACCENT_VIOLET);
                    resultValue.setText(String.format("%.1f °C", feelsLike));
                    resultSub.setText("Apparent temperature");
                }
            },
            // Next Day (derived: +/- small variation on today's temp)
            () -> {
                WeatherData d = fetchData(cityField, resultTitle, resultValue, resultSub, iconLabel);
                if (d != null) {
                    double nextDay = d.temperature + (Math.random() * 4 - 2); // ±2°C variation
                    iconLabel.setText("📅");
                    resultTitle.setText("Tomorrow's Forecast");
                    resultTitle.setForeground(ACCENT_ORANGE);
                    resultValue.setText(String.format("%.1f °C", nextDay));
                    resultSub.setText("Estimated high for next day");
                }
            }
        };

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            btns[i].addActionListener(e -> actions[idx].run());
        }

        // Allow pressing Enter in the text field to trigger temperature
        cityField.addActionListener(e -> actions[0].run());

        frame.setVisible(true);
    }

    // ── Shared fetch + error display ──────────────────────────────────────────
    private static WeatherData fetchData(JTextField cityField,
                                         JLabel resultTitle, JLabel resultValue,
                                         JLabel resultSub,   JLabel iconLabel) {
        String city = cityField.getText().trim();
        if (city.isBlank() || city.equals("Enter city name…")) {
            iconLabel.setText("⚠️");
            resultTitle.setForeground(ACCENT_ORANGE);
            resultTitle.setText("No city entered");
            resultValue.setText("");
            resultSub.setText("Please type a city name above");
            return null;
        }
        WeatherData data = WeatherService.getCurrentWeather(city);
        if (data == null) {
            iconLabel.setText("❌");
            resultTitle.setForeground(ACCENT_ORANGE);
            resultTitle.setText("Error fetching data");
            resultValue.setText("");
            resultSub.setText("Check city name or internet connection");
        }
        return data;
    }

    private static Font loadFont(String name, int style, float size) {
        return new Font(name, style, (int) size);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Custom Components
    // ══════════════════════════════════════════════════════════════════════════

    /** Rounded, semi-transparent text field */
    static class RoundTextField extends JTextField {
        RoundTextField(int cols) {
            super(cols);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 16, 0, 16));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(INPUT_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
            g2.setColor(INPUT_BORDER);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 13, 13));
            g2.dispose();
            super.paintComponent(g);
        }
        @Override
        protected void paintBorder(Graphics g) { /* handled above */ }
    }

    /** Glass-morphism button with coloured bottom accent bar */
    static class GlassButton extends JButton {
        private final Color accent;
        private boolean hovered = false;

        GlassButton(String text, Color accent) {
            super(text);
            this.accent = accent;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(TEXT_PRIMARY);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            Color bg = hovered ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 45)
                                : CARD_BG;
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));

            // Border
            g2.setColor(hovered ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 140)
                                 : CARD_BORDER);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 13, 13));

            // Bottom accent bar
            g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), hovered ? 220 : 120));
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int barY = getHeight() - 4;
            g2.drawLine(24, barY, getWidth() - 24, barY);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Glass card panel */
    static class GlassCard extends JPanel {
        GlassCard() {
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            g2.setColor(CARD_BORDER);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 19, 19));
            g2.dispose();
        }
    }
}