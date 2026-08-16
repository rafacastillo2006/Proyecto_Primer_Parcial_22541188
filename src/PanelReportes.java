import javax.swing.*;
import java.awt.*;

public class PanelReportes extends JFrame {

    private GestorJugadores gestorJugadores;

    public PanelReportes(GestorJugadores gestorJugadores) {

        this.gestorJugadores = gestorJugadores;

        setTitle("Reportes");
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

        JPanel panelReportes = new JPanel();
        panelReportes.setLayout(new BoxLayout(panelReportes, BoxLayout.Y_AXIS));
        panelReportes.setOpaque(false);

        JLabel labelReportes = new JLabel("Reportes de Juego");
        labelReportes.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelReportes.setFont(new Font("Algerian", Font.BOLD, 50));
        labelReportes.setForeground(Color.white);


        panelReportes.add(labelReportes);
        panelReportes.add(Box.createRigidArea(new Dimension(20, 15)));

        JButton botonRankingJugadores =  new JButton("Ranking de Jugadores");
        botonRankingJugadores.setOpaque(true);
        botonRankingJugadores.setBackground(Color.white);
        botonRankingJugadores.setFont(new Font("Algerian", Font.BOLD, 20));
        botonRankingJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelReportes.add(botonRankingJugadores);
        panelReportes.add(Box.createRigidArea(new Dimension(20, 15)));

        JButton botonMisReportes =  new JButton("Mis Reportes");
        botonMisReportes.setOpaque(true);
        botonMisReportes.setBackground(Color.white);
        botonMisReportes.setFont(new Font("Algerian", Font.BOLD, 20));
        botonMisReportes.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelReportes.add(botonMisReportes);
        panelReportes.add(Box.createRigidArea(new Dimension(20, 15)));

        fondoLabel.add(panelReportes);
        setVisible(true);

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

        botonRankingJugadores.addActionListener(e -> {
            new RankingJugadores(gestorJugadores);
            dispose();
        });
        botonMisReportes.addActionListener(e -> {
            new MisReportes(gestorJugadores);
            dispose();
        });
        regresarMenu.addActionListener(e -> {
            new MenuPrincipal(this.gestorJugadores);
            this.dispose();
        });
    }


}
