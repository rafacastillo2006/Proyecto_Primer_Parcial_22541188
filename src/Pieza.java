
import javax.swing.ImageIcon;
import java.awt.*;

public abstract class Pieza {

    protected String nombre;
    protected int vida;
    protected int escudo;
    protected int ataque;
    protected Jugador propietarioPieza;
    protected String rImagen;

    public Pieza(String nombre, int ataque, int vida, int escudo, Jugador propietario) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.vida = vida;
        this.escudo = escudo;
        this.propietarioPieza = propietario;
        this.rImagen = rImagen;
    }

    public final void danoPieza(int cantidadDano){
        if (cantidadDano <= 0) return;

        if(this.escudo > 0){
            if (cantidadDano <= this.escudo){
                this.escudo -= cantidadDano;
                return;
            } else {
                cantidadDano -= this.escudo;
                this.escudo = 0;
            }
        }

        this.vida -= cantidadDano;
        if(this.vida < 0){
            this.vida = 0;
        }
    }

    public final void danoVida(int cantidadDano){
        this.vida -= cantidadDano;
        if(this.vida < 0){
            this.vida = 0;
        }
    }

    public String getNombre() { return nombre; }
    public int getAtaque() { return ataque; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public int getEscudo() { return escudo; }
    public Jugador getPropietario() { return propietarioPieza; }

    public abstract String atacar(String piezaRival);

    public abstract boolean moverPieza(int fOrigen, int cOrigen, int fDestino, int cDestino, Pieza[][] tablero);
    public abstract boolean esMovimientoValido(int fOrigen, int cOrigen, int fDestino, int cDestino, Pieza[][] tablero);

    public final boolean estaViva(){
        return this.vida > 0;
    }

    public ImageIcon getImagen(int ancho, int alto) {
        try{
            ImageIcon imagen = new ImageIcon(this.getClass().getResource(this.rImagen));
            Image ajustarImagen = imagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(ajustarImagen);
        }
    }
}
