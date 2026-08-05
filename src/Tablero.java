import javax.swing.*;
import java.awt.*;

public class Tablero extends JFrame {

    private GestorJugadores gestorJugadores;

    public Tablero(GestorJugadores gestorJugadores) {

        this.gestorJugadores = gestorJugadores;

        setTitle("Tablero de Juego");
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

        setVisible(true);


    }
}
