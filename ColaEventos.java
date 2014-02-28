package aeropuerto;

import log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Cola de Eventos.
 * Los eventos se insertan ordenadamente, tal y como explico en el método;
 * y se extrae el primer evento, ya que, ya están en orden.
 * Si dos eventos coinciden en el tiempo, saldrá antes el que antes haya ingresado.
 * 
 * @author Javier López Medina
 * @version 1.0
 */
public class ColaEventos {
	/**
	 * Logger
	 */
    private static final Logger LOGGER = Logger.getLogger("aeropuerto.ColaEventos");
    /**
     * Creo una cola donde insertar los eventos
     */
    private List<Evento> cola;

    /**
     * Crea una cola de eventos, de tamaño ilimitado.
     */
    public ColaEventos() {
        cola = new ArrayList<Evento>();
    }

    /**
     * Inserta un evento en la cola de una manera ordenada, es decir,
     * los inserta teniendo en cuenta el tiempo en el que se producen
     * y si tiene el mismo tiempo que otro lo inserta después de todos
     * los que tengan su mismo tiempo.
     * Si evento fuera null, no hace nada.
     * Este método funcionaba mal y lo he modificado para que inserte 
     * los eventos de una manera ordenada.
     * Lo he arreglado poniendo i+1 en cola.add(i+1,evento) en vez de i.
     *
     * @param evento - a insertar
     */
    public void inserta(Evento evento) {
        if (evento == null)
            return;
        if (isVacia()) {
            cola.add(0,evento);
            return;
        }
        for (int i = (cola.size()-1) ; i >=0 ; i--) {
            if (!(evento.antesQue(cola.get(i)))) {
                cola.add(i+1, evento);
                return;
            }
            if ( i == 0){
                cola.add(0, evento);
                return;
            }
        }
        int a = cola.size()-1;
        LOGGER.info("La cola tiene "+ a +" eventos.");
    }

    /**
     * De los eventos insertados y aún no extraídos,
     * devuelve el primero en el sentido de que o bien
     * es anterior en el tiempo a todos los demás eventos en la cola,
     * o bien, de los eventos coincidentes en el tiempo, es el primero apuntado.
     *
     * @return primer evento en la cola; o NULL si la cola está vacía
     */
    public Evento extrae() {
        if (isVacia())
            return null;
        
        return cola.remove(0);
    }

    /**
     * El método nos dice si la cola está vacía.
     * Este método lo he modificado poniendo un 0 donde había un 1
     * ya que si esta vacia el tamaño de la cola es 0 y no 1.
     *
     * @return TRUE si no hay eventos en la cola
     */
    public boolean isVacia() {
        return (cola.size() == 0);
    }

    /**
     * El método nos dice el número de eventos en la cola
     *
     * @return número de eventos en la cola
     */
    public int getNroEventos() {
        return (cola.size());
    }

    /**
     * Listado de eventos en la cola.
     *
     * @return todos los eventos en la cola
     */
    public String toString() {
        if (isVacia())
            return null;
        String todos = "";
        for (int i = 0; i <= (cola.size() - 1); i++) {
            Evento e = cola.get(i);
            todos = todos + e.toString() + "\n";
        }
        return todos;
    }
}
