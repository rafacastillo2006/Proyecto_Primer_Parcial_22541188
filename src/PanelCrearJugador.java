import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.*;
import java.awt.*;



public class PanelCrearJugador extends JFrame {

    private GestorJugadores gestorJugadores;

    public PanelCrearJugador(GestorJugadores gestorJugadores) {

this.gestorJugadores = gestorJugadores;

        setTitle("Crear Jugador");
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

        JPanel panelCrearJugador = new JPanel();

                panelCrearJugador.setLayout(new BoxLayout(panelCrearJugador, BoxLayout.Y_AXIS));
                panelCrearJugador.setOpaque(false);

                JLabel jugadorLabel = new JLabel(" Crear Jugador");
                    jugadorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    jugadorLabel.setFont(new Font("Algerian", Font.BOLD, 50));
                    jugadorLabel.setForeground(Color.white);

                    panelCrearJugador.add(jugadorLabel);
                    panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));

                JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
                     configEtiqueta(nombreUsuarioLabel);
                JTextField nombreUsuarioTextField = new JTextField();
                    configCampoTexto(nombreUsuarioTextField);

                    panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));
                    panelCrearJugador.add(nombreUsuarioLabel);
                    panelCrearJugador.add(nombreUsuarioTextField);
                    panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel contraseñaLabel = new JLabel("Contraseña");
        configEtiqueta(contraseñaLabel);
        JTextField contraseñaTextField = new JTextField();
        configCampoTexto(contraseñaTextField);

        panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCrearJugador.add(contraseñaLabel);
        panelCrearJugador.add(contraseñaTextField);
        panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel confirmarContraseñaLabel = new JLabel("Confirmar Contraseña");
        configEtiqueta(confirmarContraseñaLabel);
        JTextField confirmarContraseñaTextField = new JTextField();
        configCampoTexto(confirmarContraseñaTextField);

        panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCrearJugador.add(confirmarContraseñaLabel);
        panelCrearJugador.add(confirmarContraseñaTextField);
        panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton botonCrear = new JButton("Crear Jugador");
        botonCrear.setOpaque(true);
        botonCrear.setBackground(Color.white);
        botonCrear.setFont(new Font("Algerian", Font.BOLD, 20));
        botonCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCrearJugador.add(botonCrear);
        panelCrearJugador.add(Box.createRigidArea(new Dimension(0, 10)));

GridBagConstraints empujarPanel = new GridBagConstraints();
empujarPanel.gridx = 0;
empujarPanel.gridy = 0;
empujarPanel.insets = new Insets(220, 0, 0, 0) ;

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


botonCrear.addActionListener(e -> {

    String username = nombreUsuarioTextField.getText().trim();
    String contraseña = contraseñaTextField.getText().trim();
    String confirmarContraseña = confirmarContraseñaTextField.getText().trim();

  if(username.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
      JOptionPane.showMessageDialog(null, "Favor, no dejar campos vacios.");
      return;
  }

  if(gestorJugadores.localizarJugador(username) != null) {
      JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.");
      return;
  }

  if (contraseña.length() > 5 || contraseña.length() < 5){
      JOptionPane.showMessageDialog(null,"La contraseña debe contener 5 caracteres.");
      return;
  }

  if(!contraseña.equals(confirmarContraseña)){
      JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
      return;
  }

  if(gestorJugadores.localizarJugador(username) == null && contraseñaTextField.getText().trim().equals(confirmarContraseña) && !(contraseña.length() > 5 || contraseña.length() < 5)) {
      gestorJugadores.agregarJugador(username, contraseña);
      JOptionPane.showMessageDialog(null, "Jugador agregado correctamente.");
      nombreUsuarioTextField.setText("");
      contraseñaTextField.setText("");
      confirmarContraseñaTextField.setText("");
      }
});

        regresarMenu.addActionListener(e -> {
            new PaginaInicio(this.gestorJugadores);
            this.dispose();
        });


        fondoLabel.add(panelCrearJugador, empujarPanel);        setVisible(true);
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
