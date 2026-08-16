

public final class Werewolf extends Pieza{

    public Werewolf(Jugador propietarioPieza) {
        super("Werewolf", 5,5,2, propietarioPieza);
    }

    @Override
    public boolean esMovimientoValido(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        int difFila = Math.abs(fD - fO);
        int difCol = Math.abs(cD - cO);

        if (difFila == 0 && difCol == 0) {return false;}

        boolean rangoValido = (difFila <= 2) && (difCol <= 2);

        return rangoValido;
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
    public String atacar(Pieza piezaRival) {
        piezaRival.danoPieza(this.ataque);
        if (!piezaRival.estaViva()){
            return "La pieza " + piezaRival.getNombre() + " del jugador: " + piezaRival.getPropietario().getUsername();
        }
else{
    return "Se atacó a la pieza " + piezaRival.getNombre() + " y le restan " + piezaRival.getEscudo() + " puntos de escudo y " + piezaRival.getVida() + " puntos de vida.";
        }
    }
}
