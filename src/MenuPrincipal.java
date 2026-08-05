import javax.swing.*;
import java.awt.*;


public class MenuPrincipal extends JFrame {

    private GestorJugadores gestorJugadores;
    public MenuPrincipal(GestorJugadores gestorJugadores) {

        this.gestorJugadores = gestorJugadores;

        setTitle("Menu Principal");
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

            Dimension tamañoPantalla = Toolkit.getDefaultToolkit().getScreenSize();

                ImageIcon imagenFondo = new ImageIcon(getClass().getResource("/fondopantallainicio.jpg"));
                Image imagenAgrandada = imagenFondo.getImage().getScaledInstance(tamañoPantalla.width, tamañoPantalla.height,Image.SCALE_SMOOTH);
                JLabel fondoLabel = new JLabel(new ImageIcon(imagenAgrandada));

                fondoLabel.setLayout(new GridBagLayout());
                setContentPane(fondoLabel);

            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new GridLayout(4, 1, 0,20 ));
            panelBotones.setOpaque(false);

         JButton jugarPartida = new JButton("Nueva Partida");
         JButton miCuenta = new JButton("Mi Cuenta");
         JButton reportes = new JButton("Reportes");
         JButton cerrarSesion = new JButton("Cerrar Sesion");

            Font fuenteBotones = new Font("Algerian", Font.BOLD, 22);
            Dimension tamañoBotones = new Dimension(260, 50);
            JButton[] botonesMenu = {jugarPartida, miCuenta, reportes, cerrarSesion};

        for (int i = 0; i < botonesMenu.length; i++){
            botonesMenu[i].setPreferredSize(tamañoBotones);
            botonesMenu[i].setFont(fuenteBotones);
            botonesMenu[i].setBackground(Color.white);
            panelBotones.add(botonesMenu[i]);
        }

        GridBagConstraints ajusteGrid = new GridBagConstraints();
            ajusteGrid.gridx = 0;
            ajusteGrid.gridy = 0;
            ajusteGrid.insets = new Insets(220, 0, 0, 0);

            fondoLabel.add(panelBotones, ajusteGrid);

        JButton regresarMenu  =  new JButton("Regresar");
            regresarMenu.setOpaque(true);
            regresarMenu.setBackground(Color.white);
            regresarMenu.setFont(new Font("Algerian", Font.BOLD, 20));
            JPanel panelBotonRegresar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelBotonRegresar.setOpaque(false);
            panelBotonRegresar.add(regresarMenu);
            fondoLabel.add(panelBotonRegresar);

        GridBagConstraints ubucacionRegresar = new GridBagConstraints();
            ubucacionRegresar.gridx = 0;
            ubucacionRegresar.gridy = 0;
            ubucacionRegresar.weightx = 1.0;
            ubucacionRegresar.weighty = 1.0;
            ubucacionRegresar.anchor = GridBagConstraints.LAST_LINE_END;
            ubucacionRegresar.insets = new Insets(0, 0, 30, 30);
            fondoLabel.add(panelBotonRegresar, ubucacionRegresar);

        regresarMenu.addActionListener(e -> {
            new PaginaInicio(this.gestorJugadores);
            this.dispose();
        });

        cerrarSesion.addActionListener(e -> {
            gestorJugadores.cerrarSesion();
            new PaginaInicio(this.gestorJugadores);
            this.dispose();
        });

        miCuenta.addActionListener(e -> {
            new PanelMiCuenta(this.gestorJugadores);
            this.dispose();
        });

        setVisible(true);
    }

}
