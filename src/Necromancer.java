public class Necromancer extends Pieza {

    public Necromancer(Jugador propietario, String rImagen) {
        super("Necromancer", 4, 2, 1, propietario, rImagen);
    }

    @Override
    public boolean esMovimientoValido(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        int difFila = Math.abs(fD - fO);
        int difCol = Math.abs(cD - cO);

        if (difFila == 0 && difCol == 0) return false;

        return (difFila <= 1) && (difCol <= 1);
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
            return "Se destruyó la pieza " + enemigo.getNombre() + " del jugador " + enemigo.getPropietario().getUsername() + ".";
        }
        return "Se atacó la pieza " + enemigo.getNombre() + " y se le quitaron " + this.ataque + " puntos; " +
                "le quedan " + enemigo.getEscudo() + " puntos de escudo y " + enemigo.getVida() + " de vida.";
    }

    public String ataqueLanza(Pieza enemigo) {
        enemigo.danoVida(2);

        if (!enemigo.estaViva()) {
            return "¡Lanza mortal! Se destruyó la pieza " + enemigo.getNombre() + " del jugador " + enemigo.getPropietario().getUsername() + ".";
        }
        return "Se atacó con lanza a " + enemigo.getNombre() + " ignorando su escudo; le quedan " + enemigo.getVida() + " de vida.";
    }
}
