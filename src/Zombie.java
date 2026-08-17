public final class Zombie extends Pieza {

    public Zombie(Jugador propietario, String rImagen) {
        super("Zombie", 1, 1, 0, propietario, rImagen);
    }

    @Override
    public boolean esMovimientoValido(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        return false;
    }

    @Override
    public boolean moverPieza(int fO, int cO, int fD, int cD, Pieza[][] tablero) {
        return false;
    }

    @Override
    public String atacar(Pieza enemigo) {
        enemigo.danoPieza(this.ataque);

        if (!enemigo.estaViva()) {
            return "Se destruyó la pieza " + enemigo.getNombre() + " del jugador " + enemigo.getPropietario().getUsername() + ".";
        }
        return "El Zombie atacó a " + enemigo.getNombre() + " restando 1 punto. " +
                "Le quedan " + enemigo.getEscudo() + " de escudo y " + enemigo.getVida() + " de vida.";
    }
}