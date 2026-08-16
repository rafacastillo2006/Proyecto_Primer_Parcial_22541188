import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Tablero extends JFrame {

    private GestorJugadores gestorJugadores;
    private Jugador jugadorBlanco;
    private Jugador jugadorNegro;
    private Jugador jugadorEnTurno;

    private Ruleta ruleta;
    private Pieza[][] tableroLogico;
    private JButton[][] botonesTablero;
    private int girosDisponibles = 1;
    private int girosRealizados = 0;

    private JLabel labelTurno;
    private JLabel labelSubtitulo;
    private JLabel labelRuletaResultado;
    private JTextArea areaHistorial;
    private JLabel labelGiros;
    private JButton btnGirarRuleta;
    private JButton btnRetirarse;
    private String piezaRuletaActual = null;

    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;

    private final Color fondoOscuro = Color.decode("#0a0f1d");
    private final Color colorPanelRuleta = Color.decode("#0d1527");
    private final Color bordeAzul = Color.decode("#22487a");
    private final Color azulBrilloso = Color.decode("#3fa9f5");
    private final Color azulMedio = Color.decode("#3179b8");
    private final Color colorCasilla1 = Color.decode("#182846");
    private final Color colorCasilla2 = Color.decode("#0e172a");

    public Tablero(GestorJugadores gestorJugadores, Jugador oponente) {
        this.gestorJugadores = gestorJugadores;
        this.jugadorBlanco = gestorJugadores.getJugadorLoggedIn();
        this.jugadorNegro = oponente;
        this.jugadorEnTurno = jugadorBlanco;

        this.tableroLogico = new Pieza[6][6];
        this.botonesTablero = new JButton[6][6];

        setTitle("Vampire Wargame");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        add(armarPanelArriba(), BorderLayout.NORTH);
        add(crearPanelRuleta(), BorderLayout.WEST);
        add(crearTableroGUI(), BorderLayout.CENTER);

        inicializarTableroLogico();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarTableroGrafico();
            }
        });

        registrarAccion("Inicio del Juego\n" + jugadorBlanco.getUsername() + " vs " + jugadorNegro.getUsername());
        setVisible(true);
        SwingUtilities.invokeLater(() -> actualizarTableroGrafico());
    }

    private JPanel armarPanelArriba() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(fondoOscuro);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, bordeAzul),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));

        labelTurno = new JLabel("Turno: " + jugadorEnTurno.getUsername());
        labelTurno.setFont(new Font("Serif", Font.BOLD, 24));
        labelTurno.setForeground(azulBrilloso);
        labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelSubtitulo = new JLabel("Gira la ruleta para determinar qué pieza puede actuar.");
        labelSubtitulo.setFont(new Font("Serif", Font.ITALIC, 14));
        labelSubtitulo.setForeground(new Color(180, 205, 235));
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(labelTurno);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(labelSubtitulo);

        return header;
    }

    private JPanel crearPanelRuleta() {
        JPanel panelRuleta = new JPanel(new BorderLayout(0, 10));
        panelRuleta.setPreferredSize(new Dimension(320, 0));
        panelRuleta.setBackground(colorPanelRuleta);
        panelRuleta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, bordeAzul),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel panelRuletaInfo = new JPanel();
        panelRuletaInfo.setLayout(new BoxLayout(panelRuletaInfo, BoxLayout.Y_AXIS));
        panelRuletaInfo.setOpaque(false);

        ruleta = new Ruleta();
        ruleta.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelRuletaResultado = new JLabel("Gira la Ruleta", SwingConstants.CENTER);
        labelRuletaResultado.setFont(new Font("Serif", Font.BOLD, 16));
        labelRuletaResultado.setForeground(azulBrilloso);
        labelRuletaResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelGiros = new JLabel("Giros disponibles: " + (girosDisponibles - girosRealizados), SwingConstants.CENTER);
        labelGiros.setFont(new Font("Arial", Font.PLAIN, 13));
        labelGiros.setForeground(new Color(200, 220, 245));
        labelGiros.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelRuletaInfo.add(ruleta);
        panelRuletaInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelRuletaInfo.add(labelRuletaResultado);
        panelRuletaInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelRuletaInfo.add(labelGiros);

        JPanel panelRegistro = new JPanel(new BorderLayout());
        panelRegistro.setOpaque(false);

        JLabel labelRegistroMovimientos = new JLabel("Registro de Movimientos");
        labelRegistroMovimientos.setFont(new Font("Serif", Font.BOLD, 14));
        labelRegistroMovimientos.setForeground(azulMedio);
        labelRegistroMovimientos.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaHistorial.setBackground(fondoOscuro);
        areaHistorial.setForeground(new Color(180, 210, 240));
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);

        JScrollPane scrollRegistro = new JScrollPane(areaHistorial);
        scrollRegistro.setBorder(BorderFactory.createLineBorder(bordeAzul));

        panelRegistro.add(labelRegistroMovimientos, BorderLayout.NORTH);
        panelRegistro.add(scrollRegistro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 8));
        panelBotones.setOpaque(false);

        btnGirarRuleta = new JButton("Girar Ruleta");
        btnGirarRuleta.setFont(new Font("Serif", Font.BOLD, 15));
        btnGirarRuleta.setBackground(azulMedio);
        btnGirarRuleta.setForeground(Color.WHITE);
        btnGirarRuleta.setFocusPainted(false);
        btnGirarRuleta.setBorder(BorderFactory.createLineBorder(azulBrilloso));
        btnGirarRuleta.addActionListener(e -> girarRuleta());

        btnRetirarse = new JButton("Retirarse");
        btnRetirarse.setFont(new Font("Serif", Font.BOLD, 13));
        btnRetirarse.setBackground(new Color(25, 35, 55));
        btnRetirarse.setForeground(new Color(170, 190, 210));
        btnRetirarse.setFocusPainted(false);
        btnRetirarse.setBorder(BorderFactory.createLineBorder(bordeAzul));
        btnRetirarse.addActionListener(e -> retirarDePartida());

        panelBotones.add(btnGirarRuleta);
        panelBotones.add(btnRetirarse);

        panelRuleta.add(panelRuletaInfo, BorderLayout.NORTH);
        panelRuleta.add(panelRegistro, BorderLayout.CENTER);
        panelRuleta.add(panelBotones, BorderLayout.SOUTH);

        return panelRuleta;
    }

    private JPanel crearTableroGUI() {
        JPanel panelTablero = new JPanel(new GridLayout(6, 6));
        panelTablero.setBackground(fondoOscuro);
        panelTablero.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 6; col++) {
                JButton btnCasilla = new JButton();
                btnCasilla.setFocusPainted(false);
                btnCasilla.setMargin(new Insets(0, 0, 0, 0));
                btnCasilla.setBorder(BorderFactory.createLineBorder(new Color(15, 25, 45)));

                if ((fila + col) % 2 == 0) {
                    btnCasilla.setBackground(colorCasilla1);
                } else {
                    btnCasilla.setBackground(colorCasilla2);
                }

                final int f = fila;
                final int c = col;
                btnCasilla.addActionListener(e -> accionCasilla(f, c));

                botonesTablero[fila][col] = btnCasilla;
                panelTablero.add(btnCasilla);
            }
        }
        return panelTablero;
    }

    private void inicializarTableroLogico() {
        tableroLogico[0][0] = new Werewolf(jugadorNegro, "/WerewolhBlack.png");
        tableroLogico[0][1] = new Vampire(jugadorNegro, "/Black Vampire.png");
        tableroLogico[0][2] = new Necromancer(jugadorNegro, "/Black Necromancer.png");
        tableroLogico[0][3] = new Necromancer(jugadorNegro, "/Black Necromancer.png");
        tableroLogico[0][4] = new Vampire(jugadorNegro, "/Black Vampire.png");
        tableroLogico[0][5] = new Werewolf(jugadorNegro, "/WerewolhBlack.png");

        tableroLogico[5][0] = new Werewolf(jugadorBlanco, "/WerewolhWhite.png");
        tableroLogico[5][1] = new Vampire(jugadorBlanco, "/White Vampire.png");
        tableroLogico[5][2] = new Necromancer(jugadorBlanco, "/White Necromancer.png");
        tableroLogico[5][3] = new Necromancer(jugadorBlanco, "/White Necromancer.png");
        tableroLogico[5][4] = new Vampire(jugadorBlanco, "/White Vampire.png");
        tableroLogico[5][5] = new Werewolf(jugadorBlanco, "/WerewolhWhite.png");
    }

    private void actualizarTableroGrafico() {
        for (int f = 0; f < 6; f++) {
            for (int c = 0; c < 6; c++) {
                Pieza p = tableroLogico[f][c];
                JButton btn = botonesTablero[f][c];

                if (p != null) {
                    int anchoBtn = btn.getWidth() > 0 ? btn.getWidth() : 90;
                    int altoBtn = btn.getHeight() > 0 ? btn.getHeight() : 90;

                    btn.setIcon(p.getImagen(anchoBtn - 6, altoBtn - 6));
                } else {
                    btn.setIcon(null);
                }
                btn.revalidate();
                btn.repaint();
            }
        }
    }

    private void registrarAccion(String mensaje) {
        areaHistorial.append("-> " + mensaje + "\n");
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    private void girarRuleta() {
        if (girosRealizados >= girosDisponibles) {
            JOptionPane.showMessageDialog(this, "Sin giros disponibles en este turno.");
            return;
        }

        btnGirarRuleta.setEnabled(false);

        ruleta.girar(() -> {
            piezaRuletaActual = ruleta.getResultadoPieza();
            girosRealizados++;

            labelRuletaResultado.setText("Pieza: " + piezaRuletaActual.toUpperCase());
            labelGiros.setText("Giros disponibles: " + (girosDisponibles - girosRealizados));

            registrarAccion(jugadorEnTurno.getUsername() + " giró la ruleta y obtuvo: " + piezaRuletaActual);
        });
    }

    private void accionCasilla(int fila, int col) {
        if (piezaRuletaActual == null) {
            JOptionPane.showMessageDialog(this, "Gira la ruleta antes de seleccionar una pieza.");
            return;
        }

        if (filaSeleccionada == -1 && colSeleccionada == -1) {
            Pieza pieza = tableroLogico[fila][col];

            if (pieza == null) {
                return;
            }

            if (pieza.getPropietario() != jugadorEnTurno) {
                JOptionPane.showMessageDialog(this, "Esta pieza pertenece a tu oponente.");
                return;
            }

            if (!pieza.getNombre().equalsIgnoreCase(piezaRuletaActual)) {
                JOptionPane.showMessageDialog(this, "Solo puedes mover piezas de tipo: " + piezaRuletaActual);
                return;
            }

            filaSeleccionada = fila;
            colSeleccionada = col;
            botonesTablero[fila][col].setBorder(BorderFactory.createLineBorder(azulBrilloso, 3));
            registrarAccion("Pieza seleccionada en [" + fila + "][" + col + "]");
            return;
        }

        if (filaSeleccionada == fila && colSeleccionada == col) {
            limpiarSeleccion();
            return;
        }

        Pieza piezaOrigen = tableroLogico[filaSeleccionada][colSeleccionada];
        Pieza piezaDestino = tableroLogico[fila][col];

        if (piezaDestino == null) {
            if (piezaOrigen.esMovimientoValido(filaSeleccionada, colSeleccionada, fila, col, tableroLogico)) {
                tableroLogico[fila][col] = piezaOrigen;
                tableroLogico[filaSeleccionada][colSeleccionada] = null;

                registrarAccion(jugadorEnTurno.getUsername() + " movió " + piezaOrigen.getNombre() + " a [" + fila + "][" + col + "]");
                finalizarAccion();
            } else {
                JOptionPane.showMessageDialog(this, "Movimiento no válido para el " + piezaOrigen.getNombre());
            }
        } else {
            if (piezaDestino.getPropietario() == jugadorEnTurno) {
                JOptionPane.showMessageDialog(this, "No puedes atacar a tus propias piezas.");
                limpiarSeleccion();
                return;
            }

            if (piezaOrigen.esMovimientoValido(filaSeleccionada, colSeleccionada, fila, col, tableroLogico)) {
                ejecutarAtaque(piezaOrigen, piezaDestino, fila, col);
                finalizarAccion();
            } else {
                JOptionPane.showMessageDialog(this, "Rango de ataque no válido.");
            }
        }
    }

    private void ejecutarAtaque(Pieza atacante, Pieza objetivo, int fDestino, int cDestino) {
        int dano = atacante.getAtaque();
        objetivo.danoPieza(dano);

        registrarAccion(atacante.getNombre() + " atacó a " + objetivo.getNombre() + " causando " + dano + " de daño.");

        if (!objetivo.estaViva()) {
            tableroLogico[fDestino][cDestino] = atacante;
            tableroLogico[filaSeleccionada][colSeleccionada] = null;
            registrarAccion(objetivo.getNombre() + " ha sido destruido.");
            verificarFinDeJuego();
        }
    }

    private void finalizarAccion() {
        limpiarSeleccion();
        actualizarTableroGrafico();
        cambiarTurno();
    }

    private void limpiarSeleccion() {
        if (filaSeleccionada != -1 && colSeleccionada != -1) {
            botonesTablero[filaSeleccionada][colSeleccionada].setBorder(BorderFactory.createLineBorder(new Color(15, 25, 45)));
        }
        filaSeleccionada = -1;
        colSeleccionada = -1;
    }

    private void cambiarTurno() {
        jugadorEnTurno = (jugadorEnTurno == jugadorBlanco) ? jugadorNegro : jugadorBlanco;
        labelTurno.setText("Turno: " + jugadorEnTurno.getUsername());

        piezaRuletaActual = null;
        girosRealizados = 0;
        labelRuletaResultado.setText("Gira la Ruleta");
        labelGiros.setText("Giros disponibles: " + (girosDisponibles - girosRealizados));
        btnGirarRuleta.setEnabled(true);

        registrarAccion("--- Cambió el turno a " + jugadorEnTurno.getUsername() + " ---");
    }

    private void declararVictoria(Jugador ganador) {
        gestorJugadores.sumarPuntosAJugador(ganador.getUsername(), 3);
    }
    private void verificarFinDeJuego() {
        boolean negroTienePiezas = false;
        boolean blancoTienePiezas = false;

        for (int f = 0; f < 6; f++) {
            for (int c = 0; c < 6; c++) {
                Pieza p = tableroLogico[f][c];
                if (p != null) {
                    if (p.getPropietario() == jugadorNegro) negroTienePiezas = true;
                    if (p.getPropietario() == jugadorBlanco) blancoTienePiezas = true;
                }
            }
        }

        if (!negroTienePiezas || !blancoTienePiezas) {
            Jugador ganador = blancoTienePiezas ? jugadorBlanco : jugadorNegro;
            declararVictoria(ganador);

            JOptionPane.showMessageDialog(this, "¡PARTIDA FINALIZADA!\nGanador: " + ganador.getUsername());
            new MenuPrincipal(gestorJugadores);
            this.dispose();
        }
    }

    private void retirarDePartida() {
        int opt = JOptionPane.showConfirmDialog(this, "¿Deseas retirarte de la partida?", "Retiro", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            Jugador ganador = (jugadorEnTurno == jugadorBlanco) ? jugadorNegro : jugadorBlanco;
            declararVictoria(ganador);

            String msj = jugadorEnTurno.getUsername() + " se ha retirado. Ganador: " + ganador.getUsername();
            JOptionPane.showMessageDialog(this, msj);

            new MenuPrincipal(gestorJugadores);
            this.dispose();
        }
    }
    }
