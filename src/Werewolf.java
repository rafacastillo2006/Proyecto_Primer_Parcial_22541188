

public final class Werewolf extends Pieza {

    public Werewolf(Jugador propietarioPieza, String rImagen) {
        super("Werewolf", 5, 5, 2, propietarioPieza, rImagen);
    }

    @Override
    public boolean esMovimientoValido(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        int difFila = Math.abs(fD - fO);
        int difCol = Math.abs(cD - cO);

        if (difFila == 0 && difCol == 0) {
            return false;
        }

        if (difFila > 2 || difCol > 2) {
            return false;
        }

        boolean movimientoEnLinea =
                difFila == 0 ||
                        difCol == 0 ||
                        difFila == difCol;

        if (!movimientoEnLinea) {
            return false;
        }

        if (difFila == 2 || difCol == 2) {
            int filaIntermedia = fO + Integer.signum(fD - fO);
            int colIntermedia = cO + Integer.signum(cD - cO);

            if (tablero[filaIntermedia][colIntermedia] != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean moverPieza(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        if (esMovimientoValido(fO, cO, fD, cD, tablero) &&
                tablero[fD][cD] == null) {

            tablero[fD][cD] = this;
            tablero[fO][cO] = null;
            return true;
        }

        return false;
    }

    @Override
    public String atacar(Pieza piezaRival) {
        piezaRival.danoPieza(this.ataque);

        if (!piezaRival.estaViva()) {
            return "La pieza " +
                    piezaRival.getNombre() +
                    " del jugador: " +
                    piezaRival.getPropietario().getUsername() +
                    " fue destruida.";
        }

        return "Se atacó a la pieza " +
                piezaRival.getNombre() +
                " y le restan " +
                piezaRival.getEscudo() +
                " puntos de escudo y " +
                piezaRival.getVida() +
                " puntos de vida.";
    }
}