package aeropuerto;

import log.Logger;

/**
 * Evento es un suceso que ocurre durante la simulacion.
 * Los posibles eventos aeroportuarios se obtienen de la
 * combinación de los posibles tipos de operaciones con los
 * posibles estados de las mismas.
 *
 * @author Javier López Medina
 * @version 1.0
 * @see aeropuerto.TipoOperacion
 * @see aeropuerto.EstadoOperacion
 */

public class EventoAeroportuario implements Evento {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger("aeropuerto.EventoAeroportuario");

    /**
     * Contador de identificadores
     */
    private static int contadorEventos = 0;

    /**
     * Identificador de evento
     */
    private int idEvento;

    /**
     * Identificador de aeronave, para tener trazabilidad de sus eventos
     */
    private int idAeronave;

    /**
     * Instante en que ocurre el evento
     */
    private long cuando;

    /**
     * Tipo de operacion
     */
    private TipoOperacion tipoOperacion;

    /**
     * Estado de la operacion
     */
    private EstadoOperacion estadoEvento;

    /**
     * Constructor de EventoAeroportuario privado
     *
     * @param idA      identificador de aeronave (si es != 0) o nuevo identificador (si es == 0)
     * @param estado   estado del evento
     * @param tipo     tipo de evento
     * @param instante cuando ocurre el evento
     */
    private EventoAeroportuario(int idA, EstadoOperacion estado, TipoOperacion tipo, long instante) {
        idEvento = contadorEventos++;
        if (idA != 0)
            idAeronave = idA;
        else
            idAeronave = idEvento;
        estadoEvento = estado;
        tipoOperacion = tipo;
        cuando = instante;
    }

    /**
     * Factor�a para generar un evento aeroportuario cualquiera,
     * en el instante de tiempo indicado. Este método tiene inter�s para
     * generar eventos para probar la ColaEventos.
     *
     * @param instante para el evento
     * @return evento
     */
    public static EventoAeroportuario generaCualquierEvento(long instante) {
        EventoAeroportuario miEvento = new EventoAeroportuario(0, null, null, instante);
        LOGGER.info("generaCualquierEvento " + miEvento);
        return miEvento;
    }

    /**
     * Factoría para generar un evento de intento de aterrizaje (llegada
     * de aeronave al espacio aéreo del aeropuerto)
     *
     * @param ultimaLlegada ultimo evento de llegada ocurrido o null si no existe
     * @param generador     generador aleatorio
     * @return evento
     */
    public static EventoAeroportuario generaLlegada(EventoAeroportuario ultimaLlegada,
                                                    Generador generador) {
        long instante = generador.entreLlegadas();
        if (ultimaLlegada != null) instante += ultimaLlegada.cuando;
        EventoAeroportuario miEvento =
                new EventoAeroportuario(0,
                        EstadoOperacion.INTENTO, TipoOperacion.ATERRIZAJE, instante);
        LOGGER.info("generaLlegada " + miEvento);
        return miEvento;
    }

    /**
     * Factoría para generar eventos de reintento de operación
     * (aterrizaje o despegue), por congestión de las pistas del aeropuerto
     *
     * @param solicitud evento aeroportuario que se debe reintentar
     * @param generador aleatorio
     * @return evento
     */
    public static EventoAeroportuario generaReintento(EventoAeroportuario solicitud, Generador generador) {
        long instante = solicitud.cuando + generador.demoraReintento();
        EventoAeroportuario miEvento =
                new EventoAeroportuario(solicitud.idAeronave,
                        EstadoOperacion.REINTENTO, solicitud.tipoOperacion, instante);
        LOGGER.info("generaReintento " + miEvento);
        return miEvento;
    }

    /**
     * Factoría para generar un evento de fin de operación
     *
     * @param solicitud evento de solicitud que debe finalizar
     * @param slot      duracion del slot aeroportuario
     * @return evento
     */
    public static EventoAeroportuario generaFin(EventoAeroportuario solicitud, int slot) {
        long instante = solicitud.cuando + slot;
        EventoAeroportuario miEvento =
                new EventoAeroportuario(solicitud.idAeronave,
                        EstadoOperacion.FIN, solicitud.tipoOperacion, instante);
        LOGGER.info("generaFin    " + miEvento);
        return miEvento;
    }

    /**
     * Factoría para generar un evento de intento de despegue (salida de aeronave)
     *
     * @param finAterrizaje evento de fin de aterrizaje que provoca el nuevo evento
     *                      de intento de despegue
     * @param generador     generador aleatorio
     * @return evento
     */
    public static EventoAeroportuario generaSalida(EventoAeroportuario finAterrizaje, Generador generador) {
        long instante = finAterrizaje.cuando + generador.duracionEnTierra();
        EventoAeroportuario miEvento =
                new EventoAeroportuario(finAterrizaje.idAeronave,
                        EstadoOperacion.INTENTO, TipoOperacion.DESPEGUE, instante);
        LOGGER.info("generaSalida " + miEvento);
        return miEvento;
    }

    /**
     * Método que devuelve el identificador del evento
     *
     * @return identificador del evento
     */
    public int getIdentificador() {
        return idEvento;
    }

    /**
     * Método que devuelve el identificador de aeronave
     *
     * @return identificador de aeronave
     */
    public int getIdAeronave() {
        return idAeronave;
    }

    /**
     * Método que devuelve el tipo de operacion.
     *
     * @return tipo de operacion.
     */
    public TipoOperacion getTipo() {
        return tipoOperacion;
    }

    /**
     * Método que devuelve el estado del evento.
     *
     * @return estado del evento.
     */
    public EstadoOperacion getEstado() {
        return estadoEvento;
    }

    /**
     * Método que comprueba si el tipo es ATERRIZAJE
     *
     * @return true si es un evento de tipo ATERRIZAJE.
     */
    public boolean isAterrizaje() {
        return tipoOperacion.equals(TipoOperacion.ATERRIZAJE);
    }

    /**
     * Método que comprueba si el tipo es DESPEGUE
     *
     * @return true si es un evento de tipo DESPEGUE.
     */
    public boolean isDespegue() {
        return tipoOperacion.equals(TipoOperacion.DESPEGUE);
    }

    /**
     * Método que comprueba si el estado es INTENTO
     *
     * @return true si es un evento INTENTO.
     */
    public boolean isIntento() {
        return estadoEvento.equals(EstadoOperacion.INTENTO);
    }

    /**
     * Método que comprueba si el estado es REINTENTO
     *
     * @return true si es un evento REINTENTO.
     */
    public boolean isReintento() {
        return estadoEvento.equals(EstadoOperacion.REINTENTO);
    }

    /**
     * Método que comprueba si el estado es FIN
     *
     * @return true si es un evento FIN.
     */
    public boolean isFin() {
        return estadoEvento.equals(EstadoOperacion.FIN);
    }

    /**
     * Método que nos dice cuando ocurre un evento.
     *
     * @return Cuando ocurre el evento, en segundos.
     */
    public long getCuando() {
        return cuando;
    }

    /**
     * Compara los instantes en que ocurre cada evento.
     *
     * @param otro - evento a comparar conmigo
     * @return TRUE si este evento ocurre estrictamente antes que el otro
     */
    public boolean antesQue(Evento otro) {
        return (this.cuando < otro.getCuando());
    }

    /**
     * Imprime el evento.
     *
     * @return String con descripci�n del evento: (identificador, tipo, estado, cuando)
     */
    public String toString() {
        return "EventoAeroportuario: ID=" + getIdentificador() + ", T=" + getCuando()
                + "\t" + getEstado() + "_" + getTipo() + "(" + getIdAeronave() + ")";
    }


}