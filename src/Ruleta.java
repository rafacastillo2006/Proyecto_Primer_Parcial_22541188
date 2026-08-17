import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Random;

public class Ruleta extends JPanel {

    private String[] opciones = {
            "Werewolf",
            "Vampire",
            "Necromancer",
            "Werewolf",
            "Vampire",
            "Necromancer"
    };

    private String[] imagenes = {
            "/WerewolhWhite.png",
            "/White Vampire.png",
            "/White Necromancer.png",
            "/WerewolhWhite.png",
            "/White Vampire.png",
            "/White Necromancer.png"
    };

    private Color[] colores = {
            Color.decode("#3fa9f5"),
            Color.decode("#3179b8"),
            Color.decode("#22487a"),
            Color.decode("#3fa9f5"),
            Color.decode("#3179b8"),
            Color.decode("#22487a")
    };

    private ImageIcon[] iconos;

    private double anguloActual = 0;
    private double velocidad = 0;
    private Timer timerAnimacion;
    private Runnable alTerminarGiro;
    private String resultadoPieza = null;

    public Ruleta() {
        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);

        iconos = new ImageIcon[imagenes.length];

        for (int i = 0; i < imagenes.length; i++) {
            iconos[i] = new ImageIcon(
                    getClass().getResource(imagenes[i])
            );
        }
    }

    public void girar(Runnable alTerminar) {
        this.alTerminarGiro = alTerminar;

        Random r = new Random();
        this.velocidad = 25 + r.nextInt(15);

        if (timerAnimacion != null &&
                timerAnimacion.isRunning()) {

            timerAnimacion.stop();
        }

        timerAnimacion = new Timer(30, e -> {

            anguloActual =
                    (anguloActual + velocidad) % 360;

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

        double anguloEfectivo =
                (360 - (anguloActual % 360) + 270) % 360;

        int tamanoSeccion =
                360 / opciones.length;

        int indice =
                (int) (anguloEfectivo / tamanoSeccion)
                        % opciones.length;

        resultadoPieza = opciones[indice];
    }

    public String getResultadoPieza() {
        return resultadoPieza;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int ancho = getWidth();
        int alto = getHeight();

        int diametro =
                Math.min(ancho, alto) - 30;

        int x =
                (ancho - diametro) / 2;

        int y =
                (alto - diametro) / 2;

        int numSecciones =
                opciones.length;

        double anguloSeccion =
                360.0 / numSecciones;

        for (int i = 0; i < numSecciones; i++) {

            double inicio =
                    anguloActual +
                            (i * anguloSeccion);

            g2.setColor(colores[i]);

            g2.fill(
                    new Arc2D.Double(
                            x,
                            y,
                            diametro,
                            diametro,
                            inicio,
                            anguloSeccion,
                            Arc2D.PIE
                    )
            );

            g2.setColor(
                    new Color(180, 140, 40)
            );

            g2.draw(
                    new Arc2D.Double(
                            x,
                            y,
                            diametro,
                            diametro,
                            inicio,
                            anguloSeccion,
                            Arc2D.PIE
                    )
            );

            ImageIcon icono = iconos[i];

            if (icono != null) {

                int tamanoIcono = 45;

                double anguloCentro =
                        Math.toRadians(
                                -(
                                        anguloActual +
                                                (i * anguloSeccion) +
                                                (anguloSeccion / 2)
                                )
                        );

                int radio =
                        diametro / 3;

                int centroX =
                        x + diametro / 2;

                int centroY =
                        y + diametro / 2;

                int iconoX =
                        (int) (
                                centroX +
                                        radio *
                                                Math.cos(anguloCentro)
                        ) - tamanoIcono / 2;

                int iconoY =
                        (int) (
                                centroY +
                                        radio *
                                                Math.sin(anguloCentro)
                        ) - tamanoIcono / 2;

                Image imagen =
                        icono.getImage();

                Image imagenEscalada =
                        imagen.getScaledInstance(
                                tamanoIcono,
                                tamanoIcono,
                                Image.SCALE_SMOOTH
                        );

                ImageIcon iconoEscalado =
                        new ImageIcon(imagenEscalada);

                iconoEscalado.paintIcon(
                        this,
                        g2,
                        iconoX,
                        iconoY
                );
            }
        }

        g2.setStroke(
                new BasicStroke(3)
        );

        g2.setColor(
                new Color(180, 140, 40)
        );

        g2.drawOval(
                x,
                y,
                diametro,
                diametro
        );

        int centroX =
                ancho / 2;

        int topY =
                y - 5;

        Polygon flecha =
                new Polygon();

        flecha.addPoint(
                centroX,
                topY + 12
        );

        flecha.addPoint(
                centroX - 8,
                topY
        );

        flecha.addPoint(
                centroX + 8,
                topY
        );

        g2.setColor(
                new Color(230, 190, 80)
        );

        g2.fill(flecha);

        g2.setColor(Color.BLACK);

        g2.draw(flecha);

        g2.dispose();
    }
}