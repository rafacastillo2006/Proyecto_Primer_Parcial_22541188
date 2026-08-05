import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JButton;

public class PaginaInicio extends JFrame {

    private GestorJugadores gestorJugadores;

    public PaginaInicio(GestorJugadores gestorJugadores) {

        this.gestorJugadores = gestorJugadores;

        setTitle("Página de Inicio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);

//Sección de Label para fondo de pantalla de la pagina de inicio.
        Dimension tamañoPantalla = Toolkit.getDefaultToolkit().getScreenSize();

        ImageIcon imagenFondo = new ImageIcon(getClass().getResource("/fondopantallainicio.jpg"));
                 Image imagenAgrandada = imagenFondo.getImage().getScaledInstance(tamañoPantalla.width, tamañoPantalla.height,Image.SCALE_SMOOTH);

                    JLabel fondoLabel = new JLabel(new ImageIcon(imagenAgrandada));
                        fondoLabel.setLayout(new GridBagLayout());
                          setContentPane(fondoLabel);

//Armar Selección de Botones
                          JPanel panelBotones = new JPanel();
                       panelBotones.setLayout(new GridLayout(3, 1, 0,20 ));
                    panelBotones.setOpaque(false);

             JButton iniciarSesion = new JButton("INICIAR SESION");
         JButton crearJugador = new JButton("CREAR JUGADOR");
        JButton salir = new JButton("SALIR");

        Font fuenteTextoBoton = new Font("Algerian", Font.BOLD, 22);
        Dimension tamañoBoton = new Dimension(260, 50);
        JButton[] listaBotones = {iniciarSesion, crearJugador , salir};

        for (int i = 0; i < listaBotones.length; i++) {
            listaBotones[i].setPreferredSize(tamañoBoton);
            listaBotones[i].setFont(fuenteTextoBoton);
            listaBotones[i].setBackground(Color.WHITE);
            panelBotones.add(listaBotones[i]);
        }


//GridBag para ajustar la posicion de los botones
        GridBagConstraints ajusteGrid = new GridBagConstraints();
        ajusteGrid.gridx = 0;
        ajusteGrid.gridy = 0;
        ajusteGrid.insets = new Insets(220, 0, 0, 0);

        fondoLabel.add(panelBotones, ajusteGrid);


    //Acciones de los botones
        salir.addActionListener(e -> {
            System.exit(0);
        });
        crearJugador.addActionListener(e -> {
            new PanelCrearJugador(gestorJugadores);
            this.dispose();
        });
        iniciarSesion.addActionListener(e -> {
            new PanelInicioSesion(gestorJugadores);
            this.dispose();
        });


        setVisible(true);
    }
}