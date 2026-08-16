import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class RankingJugadores extends JFrame {

    private GestorJugadores gestorJugadores;

    public RankingJugadores(GestorJugadores gestorJugadores) {
        this.gestorJugadores = gestorJugadores;

        setTitle("Ranking de Jugadores");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Dimension tamanoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        ImageIcon imagenFondo = new ImageIcon(getClass().getResource("/fondopantallainicio.jpg"));
        Image imagenAgrandada = imagenFondo.getImage().getScaledInstance(tamanoPantalla.width, tamanoPantalla.height, Image.SCALE_SMOOTH);

        JLabel fondoLabel = new JLabel(new ImageIcon(imagenAgrandada));
        fondoLabel.setLayout(new GridBagLayout());
        setContentPane(fondoLabel);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel labelTitulo = new JLabel("RANKING DE JUGADORES", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Algerian", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(320, 10, 15, 10);
        fondoLabel.add(labelTitulo, gbc);

        String[] columnas = {"Posición", "Usuario", "Puntos de Victoria"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Jugador[] jugadores = gestorJugadores.getTodosLosJugadores();
        Arrays.sort(jugadores, (j1, j2) -> Integer.compare(j2.getPuntaje(), j1.getPuntaje()));

        int posicion = 1;
        for (Jugador j : jugadores) {
            modeloTabla.addRow(new Object[]{posicion++, j.getUsername(), j.getPuntaje()});
        }

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Serif", Font.PLAIN, 18));
        tabla.setRowHeight(35);
        tabla.getTableHeader().setFont(new Font("Algerian", Font.BOLD, 18));
        tabla.getTableHeader().setBackground(new Color(20, 20, 20));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setBackground(new Color(10, 15, 25, 220));
        tabla.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(750, 220));
        scrollTabla.setOpaque(false);
        scrollTabla.getViewport().setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        fondoLabel.add(scrollTabla, gbc);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setOpaque(true);
        botonRegresar.setBackground(Color.WHITE);
        botonRegresar.setFont(new Font("Algerian", Font.BOLD, 20));
        botonRegresar.setPreferredSize(new Dimension(160, 45));
        botonRegresar.addActionListener(e -> {
            new PanelReportes(gestorJugadores);
            this.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(10, 10, 30, 40);
        fondoLabel.add(botonRegresar, gbc);

        setVisible(true);
    }
}