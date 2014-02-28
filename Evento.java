package aeropuerto;

/**
 * Evento es un suceso que ocurre durante la simulacion.
 * Los posibles eventos se obtienen de la combinación de los
 * posibles tipos de evento con los posibles estados de eventos.
 *
 * @author Javier López Medina
 * @version 1.0
 */

public interface Evento {

    /**
     * M�todo que devuelve el identificador del evento
     *
     * @return identificador del evento
     */
    public int getIdentificador();

    /**
     * M�todo que nos dice cuando ocurre un evento.
     *
     * @return Cuando ocurre el evento, en segundos.
     */
    public long getCuando();

    /**
     * Compara los instantes en que ocurre cada evento.
     *
     * @param otro - evento a comparar conmigo
     * @return TRUE si este evento ocurre estrictamente antes que el otro
     */
    public boolean antesQue(Evento otro);

    /**
     * Imprime el evento.
     *
     * @return String con descripción del evento: (identificador, cuando, tipo, estado)
     */
    public String toString();

}









