import javax.swing.*;
import java.awt.*;

public class Tablero extends JFrame {

    private JButton[][] botonesCasillas = new JButton[6][6];

    public Tablero() {
        setTitle("Vampire Wargame - Tablero");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(6, 6));

        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 6; col++) {
                JButton casilla = new JButton();

                if ((fila + col) % 2 == 0) {
                    casilla.setBackground(Color.WHITE);
                } else {
                    casilla.setBackground(Color.DARK_GRAY.brighter());
                }

                botonesCasillas[fila][col] = casilla;
                panelTablero.add(casilla);
            }
        }

        add(panelTablero);
        setVisible(true);
    }
}
