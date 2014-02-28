package aeropuerto;

/**
 * Controlador
 *
 * @author Javier López Medina
 * @version 1.0
 */
public class Controlador {

    /**
     * Total de pistas del aeropuerto
     */
    private int nroPistas;

    /**
     * Nro de pistas libres del aeropuerto
     */
    private int nroPistasLibres;

    /**
     * Duracion del slot aeroportuario
     */
    private int slotAeroportuario;

    /**
     * Estad�sticas del aeropuerto
     */
    private Estadistica estadistica;

    /**
     * Constructor
     *
     * @param nroPistas número total de pistas
     * @param slot      duración del slot aeroportuario (en segundos)
     */
    public Controlador(int nroPistas, int slot) {
        this.nroPistas = nroPistas;
        this.nroPistasLibres = nroPistas;
        this.slotAeroportuario = slot;
        this.estadistica = new Estadistica();
    }

    /**
     * Devuelve nro de pistas del aeropuerto
     *
     * @return nro total de pistas del aeropuerto
     */
    public int getPistasTotal() {
        return nroPistas;
    }

    /**
     * Devuelve nro de pistas libres del aeropuerto
     *
     * @return nro de pistas libres del aeropuerto
     */
    public int getPistasLibres() {
        return nroPistasLibres;
    }

    /**
     * Devuelve la duración del slot aeroportuario
     *
     * @return slot duración del slot aeroportuario (en segundos)
     */
    public int getSlotAeroportuario() {
        return slotAeroportuario;
    }

    /**
     * Ocupa una pista. Requiere un evento aeroportuario (que se pasa como parámetro)
     * que solicita la pista que se ocupa. Se registra en el módulo estadístico la
     * ocurrencia del evento, para los cálculos estadísticos.
     *
     * @param solicitud EventoAeroportuario que requiere pista disponible
     * @throws IllegalArgumentException  si solicitud no tiene estado INTENTO o REINTENTO
     * @throws IndexOutOfBoundsException si todas las pistas ya están ocupadas
     */
    public void ocupaPista(EventoAeroportuario solicitud) {
        if (solicitud.isIntento() || solicitud.isReintento()) {
            nroPistasLibres--;
            if (nroPistasLibres < 0)
                throw new IndexOutOfBoundsException("Todas las pistas estan ocupadas");
        } else
            throw new IllegalArgumentException("Evento con estado incorrecto");

        // Registra estad�stica
        estadistica.registraEvento(solicitud);
    }

    /**
     * Libera una pista. Requiere un evento aeroportuario (que se pasa como
     * parámetro) que provoca la liberación de una pista (fin de una operación).
     * Se registra la ocurrencia del evento, para el cálculo estadístico.
     *
     * @param fin EventoAeroportuario con estado FIN que libera pista
     * @throws IllegalArgumentException  si evento fin no tiene estado FIN
     * @throws IndexOutOfBoundsException si todas las pistas ya están libres
     */
    public void liberaPista(EventoAeroportuario fin) {
        if (fin.isFin()) {
            nroPistasLibres++;
            if (nroPistasLibres > nroPistas)
                throw new IndexOutOfBoundsException("Todas las pistas estan libres");
        } else
            throw new IllegalArgumentException("liberaPista requiere evento FIN");

        // Registra estadística
        estadistica.registraEvento(fin);
    }

    /**
     * Cierra Devuelve los resultados estadísticos del Aeropuerto,
     * desde su creación hasta el tiempo indicado.
     *
     * @param tiempoE instante para el que se obtienen resultados estadisticos
     * @return Estadistica con los resultados estadísticos
     * @throws IllegalArgumentException si tiempoE es anterior al último evento registrado
     */
    public Estadistica getEstadistica(long tiempoE) {
        estadistica.cierraEstadistica(tiempoE);
        return estadistica;
    }

    /**
     * Muestra estado actual de ocupación y puntualidad
     */
    public String toString() {
        int nroAterrizajesFinalizados = estadistica.getFinAterrizajes();
        int nroDespeguesIniciados = estadistica.getDespeguesEnHora() + estadistica.getDespeguesDemora();
        int nroAeronavesEnTierra = nroAterrizajesFinalizados - nroDespeguesIniciados;
        int nroAeronavesEnPista = nroPistas - nroPistasLibres;
        int nroOpsOnTime = estadistica.getAterrizajesEnHora() + estadistica.getDespeguesEnHora();
        int nroOpsDelayed = estadistica.getAterrizajesDemora() + estadistica.getDespeguesDemora();
        int nroOpsTotal = (nroOpsOnTime + nroOpsDelayed);
        double puntualidad = 100;
        if (nroOpsTotal > 0) puntualidad = 100 * nroOpsOnTime / nroOpsTotal;

        String a = "T=" + estadistica.getCuandoUltimoRegistro() + " Ocupacion= " + nroAeronavesEnTierra
                + "(aeropuerto) + " + nroAeronavesEnPista + "(pistas)."
                + " Puntualidad=" + puntualidad + "%";

        return a;
    }
}