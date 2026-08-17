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

    private String accionActual = null;
    private boolean partidaFinalizada = false;

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

        registrarAccion(
                "Inicio del Juego\n" +
                        jugadorBlanco.getUsername() +
                        " vs " +
                        jugadorNegro.getUsername()
        );

        setVisible(true);

        SwingUtilities.invokeLater(this::actualizarTableroGrafico);
    }

    private JPanel armarPanelArriba() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(fondoOscuro);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, bordeAzul),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));

        labelTurno = new JLabel(
                "Turno: " + jugadorEnTurno.getUsername()
        );
        labelTurno.setFont(new Font("Algerian", Font.BOLD, 24));
        labelTurno.setForeground(azulBrilloso);
        labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelSubtitulo = new JLabel(
                "Gira la ruleta para jugar el turno."
        );
        labelSubtitulo.setFont(new Font("Algerian", Font.ITALIC, 14));
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

        labelRuletaResultado = new JLabel(
                "Gira la Ruleta",
                SwingConstants.CENTER
        );
        labelRuletaResultado.setFont(new Font("Algerian", Font.BOLD, 16));
        labelRuletaResultado.setForeground(azulBrilloso);
        labelRuletaResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelGiros = new JLabel(
                "Giros disponibles: " +
                        (girosDisponibles - girosRealizados),
                SwingConstants.CENTER
        );
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

        JLabel labelRegistroMovimientos =
                new JLabel("Registro de Movimientos");

        labelRegistroMovimientos.setFont(
                new Font("Algerian", Font.BOLD, 14)
        );

        labelRegistroMovimientos.setForeground(azulMedio);
        labelRegistroMovimientos.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 5, 0)
        );

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(
                new Font("Monospaced", Font.PLAIN, 11)
        );
        areaHistorial.setBackground(fondoOscuro);
        areaHistorial.setForeground(new Color(180, 210, 240));
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);

        JScrollPane scrollRegistro = new JScrollPane(areaHistorial);
        scrollRegistro.setBorder(
                BorderFactory.createLineBorder(bordeAzul)
        );

        panelRegistro.add(
                labelRegistroMovimientos,
                BorderLayout.NORTH
        );
        panelRegistro.add(
                scrollRegistro,
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel(new GridLayout(2, 1, 5, 8));

        panelBotones.setOpaque(false);

        btnGirarRuleta = new JButton("Girar Ruleta");
        btnGirarRuleta.setFont(
                new Font("Algerian", Font.BOLD, 15)
        );
        btnGirarRuleta.setBackground(azulMedio);
        btnGirarRuleta.setForeground(Color.WHITE);
        btnGirarRuleta.setFocusPainted(false);
        btnGirarRuleta.setBorder(
                BorderFactory.createLineBorder(azulBrilloso)
        );
        btnGirarRuleta.addActionListener(e -> girarRuleta());

        btnRetirarse = new JButton("Retirarse");
        btnRetirarse.setFont(
                new Font("Algerian", Font.BOLD, 13)
        );
        btnRetirarse.setBackground(
                new Color(25, 35, 55)
        );
        btnRetirarse.setForeground(
                new Color(170, 190, 210)
        );
        btnRetirarse.setFocusPainted(false);
        btnRetirarse.setBorder(
                BorderFactory.createLineBorder(bordeAzul)
        );
        btnRetirarse.addActionListener(
                e -> retirarDePartida()
        );

        panelBotones.add(btnGirarRuleta);
        panelBotones.add(btnRetirarse);

        panelRuleta.add(
                panelRuletaInfo,
                BorderLayout.NORTH
        );
        panelRuleta.add(
                panelRegistro,
                BorderLayout.CENTER
        );
        panelRuleta.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        return panelRuleta;
    }

    private JPanel crearTableroGUI() {
        JPanel panelTablero =
                new JPanel(new GridLayout(6, 6));

        panelTablero.setBackground(fondoOscuro);
        panelTablero.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 6; col++) {

                JButton btnCasilla = new JButton();
                btnCasilla.setFocusPainted(false);
                btnCasilla.setMargin(
                        new Insets(0, 0, 0, 0)
                );

                btnCasilla.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(15, 25, 45)
                        )
                );

                if ((fila + col) % 2 == 0) {
                    btnCasilla.setBackground(colorCasilla1);
                } else {
                    btnCasilla.setBackground(colorCasilla2);
                }

                final int f = fila;
                final int c = col;

                btnCasilla.addActionListener(
                        e -> accionCasilla(f, c)
                );

                botonesTablero[fila][col] = btnCasilla;
                panelTablero.add(btnCasilla);
            }
        }

        return panelTablero;
    }

    private void inicializarTableroLogico() {
        tableroLogico[0][0] =
                new Werewolf(jugadorNegro, "/WerewolhBlack.png");

        tableroLogico[0][1] =
                new Vampire(jugadorNegro, "/Black Vampire.png");

        tableroLogico[0][2] =
                new Necromancer(
                        jugadorNegro,
                        "/Black Necromancer.png"
                );

        tableroLogico[0][3] =
                new Necromancer(
                        jugadorNegro,
                        "/Black Necromancer.png"
                );

        tableroLogico[0][4] =
                new Vampire(jugadorNegro, "/Black Vampire.png");

        tableroLogico[0][5] =
                new Werewolf(
                        jugadorNegro,
                        "/WerewolhBlack.png"
                );

        tableroLogico[5][0] =
                new Werewolf(
                        jugadorBlanco,
                        "/WerewolhWhite.png"
                );

        tableroLogico[5][1] =
                new Vampire(
                        jugadorBlanco,
                        "/White Vampire.png"
                );

        tableroLogico[5][2] =
                new Necromancer(
                        jugadorBlanco,
                        "/White Necromancer.png"
                );

        tableroLogico[5][3] =
                new Necromancer(
                        jugadorBlanco,
                        "/White Necromancer.png"
                );

        tableroLogico[5][4] =
                new Vampire(
                        jugadorBlanco,
                        "/White Vampire.png"
                );

        tableroLogico[5][5] =
                new Werewolf(
                        jugadorBlanco,
                        "/WerewolhWhite.png"
                );
    }

    private void actualizarTableroGrafico() {
        for (int f = 0; f < 6; f++) {
            for (int c = 0; c < 6; c++) {

                Pieza p = tableroLogico[f][c];
                JButton btn = botonesTablero[f][c];

                if (p != null) {

                    int anchoBtn =
                            btn.getWidth() > 0
                                    ? btn.getWidth()
                                    : 90;

                    int altoBtn =
                            btn.getHeight() > 0
                                    ? btn.getHeight()
                                    : 90;

                    btn.setIcon(
                            p.getImagen(
                                    anchoBtn - 6,
                                    altoBtn - 6
                            )
                    );

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
        areaHistorial.setCaretPosition(
                areaHistorial.getDocument().getLength()
        );
    }

    private void girarRuleta() {
        if (partidaFinalizada) {
            return;
        }

        if (girosRealizados >= girosDisponibles) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sin giros disponibles en este turno."
            );
            return;
        }

        btnGirarRuleta.setEnabled(false);

        ruleta.girar(() -> {
            piezaRuletaActual = ruleta.getResultadoPieza();
            girosRealizados++;

            labelRuletaResultado.setText(
                    "Pieza: " + piezaRuletaActual.toUpperCase()
            );

            labelGiros.setText(
                    "Giros disponibles: " +
                            (girosDisponibles - girosRealizados)
            );

            registrarAccion(
                    jugadorEnTurno.getUsername() +
                            " giró la ruleta y obtuvo: " +
                            piezaRuletaActual
            );

            if (!jugadorTienePiezaDisponible(piezaRuletaActual)) {

                JOptionPane.showMessageDialog(
                        this,
                        "No tienes ninguna pieza de tipo " +
                                piezaRuletaActual +
                                " disponible.\n" +
                                "Tu turno termina."
                );

                registrarAccion(
                        jugadorEnTurno.getUsername() +
                                " no tiene piezas de tipo " +
                                piezaRuletaActual +
                                ". El turno termina."
                );

                piezaRuletaActual = null;
                cambiarTurno();
            }
        });
    }

    private void accionCasilla(int fila, int col) {
        if (partidaFinalizada) {
            return;
        }

        if (piezaRuletaActual == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gira la ruleta antes de seleccionar una pieza."
            );
            return;
        }

        if (filaSeleccionada == -1) {
            seleccionarPieza(fila, col);
            return;
        }

        if (filaSeleccionada == fila &&
                colSeleccionada == col) {

            limpiarSeleccion();
            return;
        }

        procesarAccion(fila, col);
    }

    private void seleccionarPieza(int fila, int col) {
        Pieza pieza = tableroLogico[fila][col];

        if (pieza == null) {
            return;
        }

        if (pieza.getPropietario() != jugadorEnTurno) {
            JOptionPane.showMessageDialog(
                    this,
                    "Esta pieza pertenece a tu oponente."
            );
            return;
        }

        if (!pieza.getNombre().equalsIgnoreCase(
                piezaRuletaActual
        )) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo puedes utilizar piezas de tipo: " +
                            piezaRuletaActual
            );
            return;
        }

        String[] opciones;

        if (pieza instanceof Vampire) {
            opciones = new String[]{
                    "Mover",
                    "Ataque normal",
                    "Absorción de sangre"
            };

        } else if (pieza instanceof Necromancer) {
            opciones = new String[]{
                    "Mover",
                    "Ataque normal",
                    "Ataque con lanza",
                    "Invocar Zombie",
                    "Ataque mediante Zombie"
            };

        } else if (pieza instanceof Werewolf) {
            opciones = new String[]{
                    "Mover",
                    "Ataque normal"
            };

        } else {
            return;
        }

        int opcion = JOptionPane.showOptionDialog(
                this,
                "Selecciona la acción para el " +
                        pieza.getNombre(),
                "Acción",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion < 0) {
            return;
        }

        filaSeleccionada = fila;
        colSeleccionada = col;

        accionActual = opciones[opcion];

        botonesTablero[fila][col].setBorder(
                BorderFactory.createLineBorder(
                        azulBrilloso,
                        3
                )
        );

        registrarAccion(
                jugadorEnTurno.getUsername() +
                        " seleccionó " +
                        pieza.getNombre() +
                        " para " +
                        accionActual + "."
        );

        if (accionActual.equals("Invocar Zombie")) {
            labelSubtitulo.setText(
                    "Selecciona una casilla vacía para invocar el Zombie."
            );

        } else if (accionActual.equals(
                "Ataque mediante Zombie")) {

            labelSubtitulo.setText(
                    "Selecciona un enemigo adyacente a un Zombie propio."
            );

        } else {
            labelSubtitulo.setText(
                    "Seleccionar casilla de destino"
            );
        }
    }

    private void procesarAccion(int fila, int col) {
        Pieza piezaOrigen =
                tableroLogico[filaSeleccionada][colSeleccionada];

        Pieza piezaDestino =
                tableroLogico[fila][col];

        if (accionActual.equals("Mover")) {
            procesarMovimiento(
                    piezaOrigen,
                    fila,
                    col
            );
            return;
        }

        if (accionActual.equals("Ataque normal")) {
            procesarAtaqueNormal(
                    piezaOrigen,
                    piezaDestino,
                    fila,
                    col
            );
            return;
        }

        if (accionActual.equals("Absorción de sangre")) {
            procesarAbsorcion(
                    piezaOrigen,
                    piezaDestino,
                    fila,
                    col
            );
            return;
        }

        if (accionActual.equals("Ataque con lanza")) {
            procesarLanza(
                    piezaOrigen,
                    piezaDestino,
                    fila,
                    col
            );
            return;
        }

        if (accionActual.equals("Invocar Zombie")) {
            procesarInvocacionZombie(
                    fila,
                    col
            );
            return;
        }

        if (accionActual.equals("Ataque mediante Zombie")) {
            procesarAtaqueMedianteZombie(
                    piezaDestino,
                    fila,
                    col
            );
        }
    }

    private void procesarMovimiento(
            Pieza pieza,
            int fila,
            int col) {

        if (tableroLogico[fila][col] != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "La casilla está ocupada."
            );
            return;
        }

        if (!pieza.esMovimientoValido(
                filaSeleccionada,
                colSeleccionada,
                fila,
                col,
                tableroLogico)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Movimiento no válido para el " +
                            pieza.getNombre()
            );
            return;
        }

        tableroLogico[fila][col] = pieza;
        tableroLogico[filaSeleccionada][colSeleccionada] = null;

        registrarAccion(
                jugadorEnTurno.getUsername() +
                        " movió " +
                        pieza.getNombre() +
                        " a [" +
                        fila +
                        "][" +
                        col +
                        "]."
        );

        finalizarAccion();
    }

    private boolean jugadorTienePiezaDisponible(String nombrePieza) {
        return jugadorTienePiezaDisponibleR(nombrePieza, 0, 0);
    }

    private boolean jugadorTienePiezaDisponibleR(String nombrePieza, int fila, int col) {
        if (fila >= 6) {
            return false;
        }

        if (col >= 6) {
            return jugadorTienePiezaDisponibleR(
                    nombrePieza,
                    fila + 1,
                    0
            );
        }

        Pieza pieza = tableroLogico[fila][col];

        if (pieza != null &&
                pieza.getPropietario() == jugadorEnTurno &&
                pieza.getNombre().equalsIgnoreCase(nombrePieza)) {
            return true;
        }

        return jugadorTienePiezaDisponibleR(
                nombrePieza,
                fila,
                col + 1
        );
    }

    private void procesarAtaqueNormal(
            Pieza atacante,
            Pieza objetivo,
            int fila,
            int col) {

        if (objetivo == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar una pieza enemiga."
            );
            return;
        }

        if (objetivo.getPropietario() ==
                jugadorEnTurno) {

            JOptionPane.showMessageDialog(
                    this,
                    "No puedes atacar tus propias piezas."
            );
            return;
        }

        if (!esAtaqueAdyacente(
                filaSeleccionada,
                colSeleccionada,
                fila,
                col)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ataque normal solo alcanza piezas adyacentes."
            );
            return;
        }

        String resultado = atacante.atacar(objetivo);

        registrarAccion(resultado);

        if (!objetivo.estaViva()) {
            tableroLogico[fila][col] = atacante;
            tableroLogico[filaSeleccionada][colSeleccionada] = null;

            registrarAccion(
                    objetivo.getNombre() +
                            " fue destruido."
            );

            verificarFinDeJuego();
        }

        finalizarAccion();
    }

    private void procesarAbsorcion(
            Pieza atacante,
            Pieza objetivo,
            int fila,
            int col) {

        if (!(atacante instanceof Vampire)) {
            limpiarSeleccion();
            return;
        }

        if (objetivo == null ||
                objetivo.getPropietario() ==
                        jugadorEnTurno) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una pieza enemiga."
            );
            return;
        }

        if (!esAtaqueAdyacente(
                filaSeleccionada,
                colSeleccionada,
                fila,
                col)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La absorción solo puede utilizarse contra una pieza adyacente."
            );
            return;
        }

        String resultado =
                ((Vampire) atacante)
                        .absorberSangre(objetivo);

        registrarAccion(resultado);

        if (!objetivo.estaViva()) {
            registrarAccion(
                    objetivo.getNombre() +
                            " ha sido destruido."
            );

            verificarFinDeJuego();
        }

        finalizarAccion();
    }

    private void procesarLanza(
            Pieza atacante,
            Pieza objetivo,
            int fila,
            int col) {

        if (!(atacante instanceof Necromancer)) {
            limpiarSeleccion();
            return;
        }

        if (objetivo == null ||
                objetivo.getPropietario() ==
                        jugadorEnTurno) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una pieza enemiga."
            );
            return;
        }

        if (!esAtaqueLanzaValido(
                filaSeleccionada,
                colSeleccionada,
                fila,
                col)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La lanza debe alcanzar exactamente 2 casillas."
            );
            return;
        }

        String resultado =
                ((Necromancer) atacante)
                        .ataqueLanza(objetivo);

        registrarAccion(resultado);

        if (!objetivo.estaViva()) {
            registrarAccion(
                    objetivo.getNombre() +
                            " ha sido destruido."
            );

            verificarFinDeJuego();
        }

        finalizarAccion();
    }

    private void procesarInvocacionZombie(
            int fila,
            int col) {

        if (tableroLogico[fila][col] != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo puedes invocar el Zombie en una casilla vacía."
            );
            return;
        }

        String imagenZombie;

        if (jugadorEnTurno == jugadorBlanco) {
            imagenZombie = "/White Zombie.png";
        } else {
            imagenZombie = "/Black Zombie.png";
        }

        tableroLogico[fila][col] =
                new Zombie(
                        jugadorEnTurno,
                        imagenZombie
                );

        registrarAccion(
                jugadorEnTurno.getUsername() +
                        " invocó un Zombie en [" +
                        fila +
                        "][" +
                        col +
                        "]."
        );

        actualizarTableroGrafico();
        finalizarAccion();
    }

    private void procesarAtaqueMedianteZombie(
            Pieza objetivo,
            int fila,
            int col) {

        if (!(tableroLogico[filaSeleccionada][colSeleccionada]
                instanceof Necromancer)) {

            limpiarSeleccion();
            return;
        }

        if (objetivo == null ||
                objetivo.getPropietario() ==
                        jugadorEnTurno) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una pieza enemiga."
            );
            return;
        }

        if (!hayZombieAdyacente(
                fila,
                col,
                jugadorEnTurno)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El enemigo debe estar adyacente a un Zombie propio."
            );
            return;
        }

        objetivo.danoPieza(1);

        registrarAccion(
                "Un Zombie de " +
                        jugadorEnTurno.getUsername() +
                        " atacó a " +
                        objetivo.getNombre() +
                        " causando 1 punto de daño."
        );

        if (!objetivo.estaViva()) {
            for (int f = 0; f < 6; f++) {
                for (int c = 0; c < 6; c++) {
                    if (tableroLogico[f][c] == objetivo) {
                        tableroLogico[f][c] = null;
                    }
                }
            }

            registrarAccion(
                    objetivo.getNombre() +
                            " fue destruido."
            );

            verificarFinDeJuego();
        }

        finalizarAccion();
    }

    private boolean esAtaqueAdyacente(
            int fO,
            int cO,
            int fD,
            int cD) {

        int difFila = Math.abs(fD - fO);
        int difCol = Math.abs(cD - cO);

        return (difFila <= 1 &&
                difCol <= 1 &&
                !(difFila == 0 && difCol == 0));
    }

    private boolean esAtaqueLanzaValido(
            int fO,
            int cO,
            int fD,
            int cD) {

        int difFila = Math.abs(fD - fO);
        int difCol = Math.abs(cD - cO);

        return (difFila == 2 && difCol == 0) ||
                (difFila == 0 && difCol == 2) ||
                (difFila == 2 && difCol == 2);
    }

    private boolean hayZombieAdyacente(
            int filaObjetivo,
            int colObjetivo,
            Jugador propietario) {

        for (int f = 0; f < 6; f++) {
            for (int c = 0; c < 6; c++) {

                Pieza pieza = tableroLogico[f][c];

                if (pieza instanceof Zombie &&
                        pieza.getPropietario() == propietario) {

                    int difFila =
                            Math.abs(filaObjetivo - f);

                    int difCol =
                            Math.abs(colObjetivo - c);

                    if (difFila <= 1 &&
                            difCol <= 1 &&
                            !(difFila == 0 && difCol == 0)) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void finalizarAccion() {
        limpiarSeleccion();
        actualizarTableroGrafico();

        if (partidaFinalizada) {
            return;
        }

        cambiarTurno();
    }

    private void limpiarSeleccion() {
        if (filaSeleccionada != -1 &&
                colSeleccionada != -1) {

            botonesTablero[filaSeleccionada][colSeleccionada]
                    .setBorder(
                            BorderFactory.createLineBorder(
                                    new Color(15, 25, 45)
                            )
                    );
        }

        filaSeleccionada = -1;
        colSeleccionada = -1;
        accionActual = null;

        labelSubtitulo.setText(
                "Gira la ruleta para determinar qué pieza puede actuar."
        );
    }

    private void cambiarTurno() {
        jugadorEnTurno =
                (jugadorEnTurno == jugadorBlanco)
                        ? jugadorNegro
                        : jugadorBlanco;

        labelTurno.setText(
                "Turno: " +
                        jugadorEnTurno.getUsername()
        );

        piezaRuletaActual = null;
        girosRealizados = 0;

        labelRuletaResultado.setText(
                "Gira la Ruleta"
        );

        labelGiros.setText(
                "Giros disponibles: " +
                        girosDisponibles
        );

        btnGirarRuleta.setEnabled(true);

        registrarAccion(
                "Siguiente turno: " +
                        jugadorEnTurno.getUsername()

        );
    }

    private void declararVictoria(Jugador ganador) {
        gestorJugadores.sumarPuntosAJugador(
                ganador.getUsername(),
                3
        );
    }

    private void verificarFinDeJuego() {
        boolean negroTienePiezas =
                contarPiezasR(
                        jugadorNegro,
                        0,
                        0
                ) > 0;

        boolean blancoTienePiezas =
                0 < contarPiezasR(
                        jugadorBlanco,
                        0,
                        0
                );

        if (!negroTienePiezas ||
                !blancoTienePiezas) {

            partidaFinalizada = true;

            Jugador ganador =
                    blancoTienePiezas
                            ? jugadorBlanco
                            : jugadorNegro;

            declararVictoria(ganador);

            JOptionPane.showMessageDialog(
                    this,
                    "La partida ha finalizado.\nGanador: " +
                            ganador.getUsername()
            );

            new MenuPrincipal(gestorJugadores);
            this.dispose();
        }
    }

    private int contarPiezasR(
            Jugador jugador,
            int fila,
            int col) {

        if (fila >= 6) {
            return 0;
        }

        if (col >= 6) {
            return contarPiezasR(
                    jugador,
                    fila + 1,
                    0
            );
        }

        Pieza pieza = tableroLogico[fila][col];

        int cantidad =
                (pieza != null &&
                        pieza.getPropietario() == jugador)
                        ? 1
                        : 0;

        return cantidad +
                contarPiezasR(
                        jugador,
                        fila,
                        col + 1
                );
    }

    private void retirarDePartida() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas retirarte de la partida?",
                "Retiro",
                JOptionPane.YES_NO_OPTION
        );

        if (opt == JOptionPane.YES_OPTION) {

            partidaFinalizada = true;

            Jugador ganador =
                    (jugadorEnTurno == jugadorBlanco)
                            ? jugadorNegro
                            : jugadorBlanco;

            declararVictoria(ganador);

            String msj =
                    jugadorEnTurno.getUsername() +
                            " se ha retirado. Ganador: " +
                            ganador.getUsername();

            JOptionPane.showMessageDialog(
                    this,
                    msj
            );

            new MenuPrincipal(gestorJugadores);
            this.dispose();
        }
    }
}
