import java.util.ArrayList;

public class GestorJugadores {

        private ArrayList<Jugador> listaJugadores;
        private Jugador jugadorLoggedIn;

        public GestorJugadores() {
            this.listaJugadores = new ArrayList<>();
            this.jugadorLoggedIn =  null;
        }

        public Jugador localizarJugador(String username){

            for (int i=0; i<listaJugadores.size();i++){
                if(listaJugadores.get(i).getUsername().equals(username)){
                    return listaJugadores.get(i);
                }
            }
            return null;
        }

        public boolean agregarJugador(String username, String contraseña){

            if (localizarJugador(username) != null){
                return false;
            }

            Jugador nuevoJugador = new Jugador(username, contraseña, 0, null);
            listaJugadores.add(nuevoJugador);
            return true;
        }

        public boolean iniciarSesion(String username, String password){

            Jugador jugador = localizarJugador(username);

            if (jugador != null && jugador.getPassword().equals(password) && jugador.getUsername().equals(username)){
                this.jugadorLoggedIn = jugador;
                return true;
            }
            return false;
        }

        public void cerrarSesion(){
            this.jugadorLoggedIn = null;
        }

        public boolean eliminarJugadorActual() {
            if (this.jugadorLoggedIn != null) {
                this.listaJugadores.remove(this.jugadorLoggedIn);
                this.jugadorLoggedIn = null;
                return true;
            }
            return false;
        }

public Jugador getJugadorLoggedIn(){
            return jugadorLoggedIn;
}

public ArrayList<Jugador> getListaJugadores(){
            return listaJugadores;
}

public int getCantidadJugadores(){
        return listaJugadores.size();
}

}

