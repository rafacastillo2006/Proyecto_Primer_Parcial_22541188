import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;

public class PanelInicioSesion extends JFrame {

    private GestorJugadores gestorJugadores;

    public PanelInicioSesion(GestorJugadores gestorJugadores) {

        this.gestorJugadores = gestorJugadores;

            setTitle("Inicio de Sesión");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(true);
            setLocationRelativeTo(null);

            Dimension tamañoPantalla = Toolkit.getDefaultToolkit().getScreenSize();

            ImageIcon imagenFondo = new ImageIcon(getClass().getResource("/fondopantallainicio.jpg"));
            Image imagenAgrandada = imagenFondo.getImage().getScaledInstance(tamañoPantalla.width, tamañoPantalla.height, Image.SCALE_SMOOTH);

        JLabel fondoLabel = new JLabel(new ImageIcon(imagenAgrandada));
            fondoLabel.setLayout(new GridBagLayout());
            setContentPane(fondoLabel);

        JPanel panelIniciarSesion = new JPanel();
            panelIniciarSesion.setLayout(new BoxLayout(panelIniciarSesion, BoxLayout.Y_AXIS));
            panelIniciarSesion.setOpaque(false);

        JLabel jugadorLabel = new JLabel(" Iniciar Sesión");
            jugadorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jugadorLabel.setFont(new Font("Algerian", Font.BOLD, 50));
            jugadorLabel.setForeground(Color.white);

            panelIniciarSesion.add(jugadorLabel);
            panelIniciarSesion.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
            configEtiqueta(nombreUsuarioLabel);
            JTextField nombreUsuarioTextField = new JTextField();
            configCampoTexto(nombreUsuarioTextField);

            panelIniciarSesion.add(nombreUsuarioLabel);
            panelIniciarSesion.add(Box.createRigidArea(new Dimension(0, 10)));
            panelIniciarSesion.add(nombreUsuarioTextField);
            panelIniciarSesion.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel contraseñaLabel = new JLabel("Contraseña");
            configEtiqueta(contraseñaLabel);
            JPasswordField contraseñaTextField = new JPasswordField();
            configCampoTexto(contraseñaTextField);

            panelIniciarSesion.add(contraseñaLabel);
            panelIniciarSesion.add(Box.createRigidArea(new Dimension(0, 10)));
            panelIniciarSesion.add(contraseñaTextField);
            panelIniciarSesion.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton botonIniciarSesion  =  new JButton("Iniciar Sesion");
            botonIniciarSesion.setOpaque(true);
            botonIniciarSesion.setBackground(Color.white);
            botonIniciarSesion.setFont(new Font("Algerian", Font.BOLD, 20));
            botonIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIniciarSesion.add(botonIniciarSesion);
            panelIniciarSesion.add(Box.createRigidArea(new Dimension(0, 10)));

            GridBagConstraints empujarPanel = new GridBagConstraints();
            empujarPanel.gridx = 0;
            empujarPanel.gridy = 0;
            empujarPanel.insets = new Insets(220, 0, 0, 0);

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

        //Inicio boton inicio sesion
        botonIniciarSesion.addActionListener(e ->{
    String username =  nombreUsuarioTextField.getText().trim();
    String contraseña = contraseñaTextField.getText().trim();


    if(username.isEmpty()){
        JOptionPane.showMessageDialog(null, "Ingrese su nombre de usuario.");
    return;
    }
    else if (contraseña.isEmpty()){
        JOptionPane.showMessageDialog(null, "Ingrese su contraseña.");
        return;
    }
    else if (username.isEmpty() && contraseña.isEmpty()){
        JOptionPane.showMessageDialog(null, "Ingrese sus credenciales.");
    }

    if(gestorJugadores.localizarJugador(username) == null){
        JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
    }
    else if(!(gestorJugadores.localizarJugador(username) == null)){
        gestorJugadores.iniciarSesion(username,contraseña);
    if(gestorJugadores.iniciarSesion(username, contraseña) == true){
        new MenuPrincipal(gestorJugadores);
        dispose();
    }
    else{
        JOptionPane.showMessageDialog(null, "Sus datos de inicio de sesión son incorrectos.");
    }
    }

            //Final boton inicio sesion
            //Inicio boton Regresar
        });
        regresarMenu.addActionListener(e -> {
            new PaginaInicio(this.gestorJugadores);
            this.dispose();
        });
    //Final boton Regresar

fondoLabel.add(panelIniciarSesion, empujarPanel);
        setVisible(true);
    }
    private void configEtiqueta(JLabel etiquetas) {
        etiquetas.setFont(new Font("Algerian", Font.PLAIN, 18));
        etiquetas.setForeground(Color.white);
        etiquetas.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    private void configCampoTexto(JTextField campoTexto) {
        campoTexto.setFont(new Font("Arial", Font.PLAIN, 16));
        campoTexto.setMaximumSize(new Dimension(320, 45));
        campoTexto.setPreferredSize(new Dimension(320, 45));
        campoTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
