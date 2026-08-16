
public final class Vampire extends Pieza {

    private static final int VIDA_MAXIMA = 4;

    public Vampire(Jugador propietario, String rImagen) {
        super("Vampire", 3, 4, 5, propietario, rImagen);
    }

    @Override
    public boolean esMovimientoValido(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        int difFila = Math.abs(fD - fO);
        int difCol = Math.abs(cD - cO);

        if (difFila == 0 && difCol == 0) return false;

        return difFila <= 1 && difCol <= 1;
    }

    @Override
    public boolean moverPieza(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        if (esMovimientoValido(fO, cO, fD, cD, tablero) && tablero[fD][cD] == null) {
            tablero[fD][cD] = this;
            tablero[fO][cO] = null;
            return true;
        }

        return false;
    }

    @Override
    public String atacar(Pieza enemigo) {
        enemigo.danoPieza(this.ataque);

        if (!enemigo.estaViva()) {
            return "Se destruyó la pieza " + enemigo.getNombre() +
                    " del jugador " + enemigo.getPropietario().getUsername() + ".";
        }

        return "Se atacó la pieza " + enemigo.getNombre() +
                " y se le quitaron " + this.ataque + " puntos; " +
                "le quedan " + enemigo.getEscudo() +
                " puntos de escudo y " + enemigo.getVida() + " de vida.";
    }

    public String absorberSangre(Pieza enemigo) {
        enemigo.danoVida(1);

        if (this.vida < VIDA_MAXIMA) {
            this.vida++;
        }

        if (!enemigo.estaViva()) {
            return "¡Absorción fatal! Se destruyó la pieza " +
                    enemigo.getNombre() +
                    " del jugador " +
                    enemigo.getPropietario().getUsername() + ".";
        }

        return "El Vampiro absorbió 1 punto de vida de " +
                enemigo.getNombre() +
                ". Le quedan " +
                enemigo.getEscudo() +
                " de escudo y " +
                enemigo.getVida() +
                " de vida.";
    }
}