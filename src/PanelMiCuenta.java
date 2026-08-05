import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;

public class PanelMiCuenta extends JFrame {

    private GestorJugadores gestorJugadores;

    public PanelMiCuenta(GestorJugadores gestorJugadores) {

        this.gestorJugadores = gestorJugadores;

        setTitle("Página de Inicio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);

//Sección de Label para fondo de pantalla de la pagina de inicio.
        Dimension tamañoPantalla = Toolkit.getDefaultToolkit().getScreenSize();

        ImageIcon imagenFondo = new ImageIcon(getClass().getResource("/fondopantallainicio.jpg"));
        Image imagenAgrandada = imagenFondo.getImage().getScaledInstance(tamañoPantalla.width, tamañoPantalla.height, Image.SCALE_SMOOTH);

        JLabel fondoLabel = new JLabel(new ImageIcon(imagenAgrandada));
            fondoLabel.setLayout(new GridBagLayout());
            setContentPane(fondoLabel);

        JPanel panelMiCuenta = new JPanel();
            panelMiCuenta.setLayout(new BoxLayout(panelMiCuenta, BoxLayout.Y_AXIS));
            panelMiCuenta.setOpaque(false);

        JLabel jugadorLabel = new JLabel("Mi Cuenta");
            jugadorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jugadorLabel.setFont(new Font("Algerian", Font.BOLD, 50));
            jugadorLabel.setForeground(Color.white);

            panelMiCuenta.add(jugadorLabel);
            panelMiCuenta.add(Box.createRigidArea(new Dimension(20, 15)));

        JButton botonMiInformacion =  new JButton("Mi información");
            botonMiInformacion.setOpaque(true);
            botonMiInformacion.setBackground(Color.white);
            botonMiInformacion.setFont(new Font("Algerian", Font.BOLD, 20));
            botonMiInformacion.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelMiCuenta.add(botonMiInformacion);
            panelMiCuenta.add(Box.createRigidArea(new Dimension(20, 15)));

        JButton botonCambiarContraseña =  new JButton("Cambiar Contraseña");
            botonCambiarContraseña.setOpaque(true);
            botonCambiarContraseña.setBackground(Color.white);
            botonCambiarContraseña.setFont(new Font("Algerian", Font.BOLD, 20));
            botonCambiarContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelMiCuenta.add(botonCambiarContraseña);
            panelMiCuenta.add(Box.createRigidArea(new Dimension(20, 15)));

        JButton botonCerrarCuenta =  new JButton("Eliminar mi Cuenta");
            botonCerrarCuenta.setOpaque(true);
            botonCerrarCuenta.setBackground(Color.white);
            botonCerrarCuenta.setFont(new Font("Algerian", Font.BOLD, 20));
            botonCerrarCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelMiCuenta.add(botonCerrarCuenta);
            panelMiCuenta.add(Box.createRigidArea(new Dimension(20, 15)));

            botonMiInformacion.addActionListener(e -> {
                Jugador jugadorActual = this.gestorJugadores.getJugadorLoggedIn();

                String informacion = jugadorActual.mostrarInfoJugador();
                JOptionPane.showMessageDialog(this, informacion, "Mi Información",  JOptionPane.INFORMATION_MESSAGE);
            });

            botonCambiarContraseña.addActionListener(e -> {
                String contraseñaJugadorActual = this.gestorJugadores.getJugadorLoggedIn().getPassword();
                String contraseñaActual = JOptionPane.showInputDialog(this, "Ingrese su Contraseña Actual:");

                if (contraseñaActual == null) {
                    return;
                }
                else if (contraseñaActual.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Campo vacío, operación no válida.");
                    return;
                }

                else if (!contraseñaActual.equals(contraseñaJugadorActual)){
                    JOptionPane.showMessageDialog(this, "Contraseña actual incorrecta, no podrá realizar el cambio.");
                    return;
                }
                    String nuevaContrasena = JOptionPane.showInputDialog(this, "Ingrese su Nueva Contraseña:");

                    if (nuevaContrasena == null) {
                        return;
                    }
                    else if (nuevaContrasena.isEmpty()) {
                        JOptionPane.showInputDialog(this, "Nueva contraseña vacía, cancelando operación.");
                        return;
                    }

                    nuevaContrasena = nuevaContrasena.trim();

                    if (nuevaContrasena.length() < 5 || nuevaContrasena.length() > 5) {
                        JOptionPane.showMessageDialog(this, "La contraseña debe contener 5 caracteres.");
                        return;
                    }

                    gestorJugadores.getJugadorLoggedIn().setPassword(nuevaContrasena);
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada con éxito.");


            });

            botonCerrarCuenta.addActionListener(e -> {
                Jugador jugador = gestorJugadores.getJugadorLoggedIn();

                int eliminarCuenta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar su cuenta? Acción no revertible.", "Confirmación de Eliminación", JOptionPane.YES_NO_OPTION);

                if (eliminarCuenta != JOptionPane.YES_OPTION) {
                    return;
                }

                String contrasena = JOptionPane.showInputDialog(this, "Ingresar contraseña para eliminar la cuenta." );

                    if (contrasena == null) {
                        return;
                    }
                    else if (contrasena.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Contraseña vacía, cancelando operación.");
                        return;
                    }

                    if(!contrasena.equals(gestorJugadores.getJugadorLoggedIn().getPassword())) {
                        JOptionPane.showMessageDialog(this, "Contraseña incorrecta, cancelando operación.");
                    return;
                    }
                    boolean eliminado = gestorJugadores.eliminarJugadorActual();

                    if (eliminado == true) {
                        JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.");

                        new PaginaInicio(this.gestorJugadores);
                        this.dispose();
                    }


            });

        JButton regresarMenu  =  new JButton("Regresar");
        regresarMenu.setOpaque(true);
        regresarMenu.setBackground(Color.white);
        regresarMenu.setFont(new Font("Algerian", Font.BOLD, 20));
        JPanel panelBotonRegresar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonRegresar.setOpaque(false);
        panelBotonRegresar.add(regresarMenu);

        GridBagConstraints ubucacionRegresar = new GridBagConstraints();
        ubucacionRegresar.gridx = 0;
        ubucacionRegresar.gridy = 0;
        ubucacionRegresar.weightx = 1.0;
        ubucacionRegresar.weighty = 1.0;
        ubucacionRegresar.anchor = GridBagConstraints.LAST_LINE_END;
        ubucacionRegresar.insets = new Insets(0, 0, 30, 30);
        fondoLabel.add(panelBotonRegresar, ubucacionRegresar);

        regresarMenu.addActionListener(e -> {
            new MenuPrincipal(this.gestorJugadores);
            this.dispose();
        });

     GridBagConstraints ubicarPanel =  new GridBagConstraints();
     ubicarPanel.gridx = 0;
     ubicarPanel.gridy = 0;

        fondoLabel.add(panelMiCuenta, ubicarPanel);
        setVisible(true);

    }
}
