import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Random;

public class Ruleta extends JPanel {

    private String[] opciones = {"Werewolf", "Vampire", "Necromancer", "Werewolf", "Vampire", "Necromancer"};
    private Color[] colores = {
            Color.decode("#3fa9f5"),
            Color.decode("#3179b8"),
            Color.decode("#22487a"),
            Color.decode("#3fa9f5"),
            Color.decode("#3179b8"),
            Color.decode("#22487a")
    };

    private double anguloActual = 0;
    private double velocidad = 0;
    private Timer timerAnimacion;
    private Runnable alTerminarGiro;
    private String resultadoPieza = null;

    public Ruleta() {
        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);
    }

    public void girar(Runnable alTerminar) {
        this.alTerminarGiro = alTerminar;

        Random r = new Random();
        this.velocidad = 25 + r.nextInt(15);

        if (timerAnimacion != null && timerAnimacion.isRunning()) {
            timerAnimacion.stop();
        }

        timerAnimacion = new Timer(30, e -> {
            anguloActual = (anguloActual + velocidad) % 360;
            velocidad *= 0.96;

            if (velocidad < 0.3) {
                ((Timer) e.getSource()).stop();
                velocidad = 0;
                determinarResultado();
                if (alTerminarGiro != null) {
                    alTerminarGiro.run();
                }
            }
            repaint();
        });

        timerAnimacion.start();
    }

    private void determinarResultado() {
        double anguloEfectivo = (360 - (anguloActual % 360) + 270) % 360;
        int tamanoSeccion = 360 / opciones.length;
        int indice = (int) (anguloEfectivo / tamanoSeccion) % opciones.length;

        resultadoPieza = opciones[indice];
    }

    public String getResultadoPieza() {
        return resultadoPieza;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int diametro = Math.min(ancho, alto) - 30;
        int x = (ancho - diametro) / 2;
        int y = (alto - diametro) / 2;

        int numSecciones = opciones.length;
        double anguloSeccion = 360.0 / numSecciones;

        for (int i = 0; i < numSecciones; i++) {
            g2.setColor(colores[i]);
            double inicio = anguloActual + (i * anguloSeccion);
            g2.fill(new Arc2D.Double(x, y, diametro, diametro, inicio, anguloSeccion, Arc2D.PIE));

            g2.setColor(new Color(180, 140, 40));
            g2.draw(new Arc2D.Double(x, y, diametro, diametro, inicio, anguloSeccion, Arc2D.PIE));
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 10));
        for (int i = 0; i < numSecciones; i++) {
            double anguloTexto = Math.toRadians(- (anguloActual + (i * anguloSeccion) + (anguloSeccion / 2)));
            int radioTexto = diametro / 3;
            int tx = (int) (x + diametro / 2 + radioTexto * Math.cos(anguloTexto)) - 12;
            int ty = (int) (y + diametro / 2 + radioTexto * Math.sin(anguloTexto)) + 4;

            g2.drawString(opciones[i].substring(0, Math.min(4, opciones[i].length())).toUpperCase(), tx, ty);
        }

        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(180, 140, 40));
        g2.drawOval(x, y, diametro, diametro);

        int centroX = ancho / 2;
        int topY = y - 5;
        Polygon flecha = new Polygon();
        flecha.addPoint(centroX, topY + 12);
        flecha.addPoint(centroX - 8, topY);
        flecha.addPoint(centroX + 8, topY);

        g2.setColor(new Color(230, 190, 80));
        g2.fill(flecha);
        g2.setColor(Color.BLACK);
        g2.draw(flecha);

        g2.dispose();
    }
}