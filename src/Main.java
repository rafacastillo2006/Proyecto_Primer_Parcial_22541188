import javax.swing.*;
import javax.swing.JFrame;
import java.awt.Color;

public class Main {

    public static void main(String[] args) {

        GestorJugadores gestorJugadores = new GestorJugadores();

        new PaginaInicio(gestorJugadores);
    }
}
