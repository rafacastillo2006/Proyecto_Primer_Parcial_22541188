import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Jugador {

    private String username;
    private String password;
    private int puntaje;
    private Calendar ingresoJugador;
    private boolean jugadorActivo;
    private String[] logPartidas;

    public Jugador(String username, String password, int puntaje, Calendar ingresoJugador) {
        this.username = username;
        this.password = password;
        this.puntaje = puntaje;
        this.ingresoJugador = Calendar.getInstance();
        this.jugadorActivo = true;
        this.logPartidas = new String[10];
    }

    public String getFechaIngreso() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDate.format(ingresoJugador.getTime());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public Calendar getIngresoJugador() {
        return ingresoJugador;
    }

    public boolean isJugadorActivo() {
        return jugadorActivo;
    }

    public String mostrarInfoJugador() {
        return ("Jugador: " + username + "\nPuntos: " + puntaje + "\nFecha de Ingreso:\n" + getFechaIngreso() + "\nActivo: " + jugadorActivo);

    }

    @Override
    public String toString() {
        return "Jugador: " + username;
    }
}
