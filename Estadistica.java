package aeropuerto;

/**
 * Acumula datos para un análisis estadístico de la simulacion.
 * <p/>
 * Es de destacar el cálculo del número medio de aeronaves en el aeropuerto.
 * El número de aeronaves en el aeropuerto es variable,
 * crece cada vez que finaliza una operación de aterrizaje y disminuye
 * cada vez que comienza una operación de despegue.
 * <p/>
 * Si durante un tiempo T1 hemos tenido N1 aeronaves;
 * durante un tiempo T2, N2 aeronaves,
 * y así sucesivamente:<br>
 * {N1, T1}, {N2, T2}, ...<br>
 * la media es
 * <blockquote>
 * media= (N1*T1 + N2*T2 + ...) / (T1 + T2 + ...);
 * </blockquote>
 * La suma de los productos <tt>Ni*T1</tt> la llevaremos
 * en la variable <code>enAeropuertoPorTiempo</code>.
 * Mientras que el tiempo total es desde 0 hasta el cierre.
 * <p/>
 * De modo similar se calcula el número medio de aeronaves en las pistas.
 *
 * @author Javier López Medina
 * @version 1.0
 */
public class Estadistica {
    /**
     * Lleva cuenta del número total de aterrizajes sin retraso
     */
    private int totalAterrizajesEnHora = 0;

    /**
     * Lleva cuenta del número total de despegues sin retraso
     */
    private int totalDespeguesEnHora = 0;

    /**
     * Lleva cuenta del número total de aterrizajes con demora
     */
    private int totalAterrizajesDemora = 0;

    /**
     * Lleva cuenta del número total de despegues con demora
     */
    private int totalDespeguesDemora = 0;

    /**
     * Lleva cuenta del número total de aterrizajes realizados
     */
    private int totalFinAterrizajes = 0;

    /**
     * Lleva cuenta del número total de despegues realizados
     */
    private int totalFinDespegues = 0;

    /**
     * Lleva cuenta del número de aeronaves en el aeropuerto
     * en cada momento (desde el fin del aterrizaje hasta el
     * inicio del despegue)
     */
    private int cuentaEnAeropuerto = 0;

    /**
     * Lleva cuenta del número máximo de aeronaves en el aeropuerto
     * en algún momento de la simulación.
     */
    private int maximoEnAeropuerto = 0;

    /**
     * Esta variable va acumulando el producto de numero de aeronaves
     * por el tiempo que están en el aeropuerto, para calcular la
     * ocupación media.
     */
    private long enAeropuertoPorTiempo = 0;

    /**
     * Número medio de aeronaves en el aeropuerto durante
     * la simulación.
     */
    private double mediaEnAeropuerto;

    /**
     * Lleva cuenta del número de aeronaves en pistas
     * en cada momento (entre inicio de operación y fin
     * de operación).
     */
    private int cuentaEnPistas = 0;

    /**
     * Lleva cuenta del número máximo de aeronaves en pistas
     * en algún momento de la simulación.
     */
    private int maximoEnPistas = 0;

    /**
     * Esta variable va acumulando el producto de numero de aeronaves
     * por el tiempo que están en las pistas, para calcular la
     * ocupación media.
     */
    private long enPistasPorTiempo = 0;

    /**
     * Número medio de aeronaves en pistas durante
     * la simulación.
     */
    private double mediaEnPistas;

    /**
     * Apunte de cuando fue la última vez que cambió
     * el número de aeronaves en el aeropuerto o pistas.
     * Es necesario para calcular el número medio de aeronaves.
     */
    private long ultimoCambioAeronaves = 0;

    /**
     * Apunte de cuando se produjo el último evento
     * Necesario para asegurar consistencia de las estadísticas.
     */
    private long tUltimoEvento = 0;

    /**
     * Se llama a este método para registrar un evento y
     * actualizar las estadísticas correspondientes
     *
     * @param evento que se registra
     */
    public void registraEvento(EventoAeroportuario evento) {
        long tiempo = evento.getCuando();
        enPistasPorTiempo += (long) cuentaEnPistas * (tiempo - ultimoCambioAeronaves);
        enAeropuertoPorTiempo += (long) cuentaEnAeropuerto * (tiempo - ultimoCambioAeronaves);
        ultimoCambioAeronaves = tiempo;
        tUltimoEvento = tiempo;

        /*
        * Fin de operación
        */
        if (evento.isFin()) {
            cuentaEnPistas--;
            if (evento.isAterrizaje()) {
                totalFinAterrizajes++;
                cuentaEnAeropuerto++;
                maximoEnAeropuerto = Math.max(maximoEnAeropuerto, cuentaEnAeropuerto);
            } else if (evento.isDespegue()) {
                totalFinDespegues++;
            }
        } else {
            cuentaEnPistas++;
            maximoEnPistas = Math.max(maximoEnPistas, cuentaEnPistas);
            /*
            * Intento = se inicia la operación en hora
            */
            if (evento.isIntento()) {
                if (evento.isAterrizaje())
                    totalAterrizajesEnHora++;
                else if (evento.isDespegue()) {
                    totalDespeguesEnHora++;
                    cuentaEnAeropuerto--;
                }
            }
            /*
            * Reintento = se inicia la operación con demora
            */
            else if (evento.isReintento()) {
                if (evento.isAterrizaje())
                    totalAterrizajesDemora++;
                else if (evento.isDespegue()) {
                    totalDespeguesDemora++;
                    cuentaEnAeropuerto--;
                }
            }
        }
    }

    /**
     * Número total de aterrizajes sin retraso
     *
     * @return número total de aterrizajes sin retraso
     */
    public int getAterrizajesEnHora() {
        return totalAterrizajesEnHora;
    }

    /**
     * Número total de despegues sin retraso
     *
     * @return número total de despegues sin retraso
     */
    public int getDespeguesEnHora() {
        return totalDespeguesEnHora;
    }

    /**
     * N�mero total de aterrizajes con demora
     *
     * @return n�mero total de aterrizajes con demora
     */
    public int getAterrizajesDemora() {
        return totalAterrizajesDemora;
    }

    /**
     * Número total de despegues con demora
     *
     * @return número total de despegues con demora
     */
    public int getDespeguesDemora() {
        return totalDespeguesDemora;
    }

    /**
     * Número total de aterrizajes finalizados
     *
     * @return número de aterrizajes
     */
    public int getFinAterrizajes() {
        return totalFinAterrizajes;
    }

    /**
     * Número total de despegues finalizados
     *
     * @return número de despegues
     */
    public int getFinDespegues() {
        return totalFinDespegues;
    }

    /**
     * Número máximo de aeronaves concurrentes
     * (a la vez) en el aeropuerto en algún momento de la simulación.
     *
     * @return máximo número de aeronaves simultaneas en aeropuerto
     */
    public int getMaximoEnAeropuerto() {
        return maximoEnAeropuerto;
    }

    /**
     * Número medio de aeronaves que han estado en
     * en el aeropuerto.
     *
     * @return media de aeronaves en aeropuerto
     */
    public double getMediaEnAeropuerto() {
        mediaEnAeropuerto = (double) enAeropuertoPorTiempo / ultimoCambioAeronaves;
        return Math.round(mediaEnAeropuerto * 100) / 100.0;
    }

    /**
     * Número actual de aeronaves en el aeropuerto.
     *
     * @return número actual de aeronaves en aeropuerto
     */
    public long getNroEnAeropuerto() {
        return cuentaEnAeropuerto;
    }

    /**
     * Número máximo de aeronaves concurrentes
     * (a la vez) en las pistas en algún momento de la simulación.
     *
     * @return máximo número de aeronaves en pistas
     */
    public int getMaximoEnPistas() {
        return maximoEnPistas;
    }

    /**
     * Número medio de aeronaves en las pistas.
     *
     * @return media de aeronaves en pistas
     */
    public double getMediaEnPistas() {
        mediaEnPistas = (double) enPistasPorTiempo / ultimoCambioAeronaves;
        return Math.round(mediaEnPistas * 100) / 100.0;
    }

    /**
     * Número actual de aeronaves en las pistas.
     *
     * @return número actual de aeronaves en pistas
     */
    public long getNroEnPistas() {
        return cuentaEnPistas;
    }

    /**
     * Se llama a este método cuando termina una simulación.
     * Si no se hace así, los valores estadísticos medios corresponden al
     * instante del último evento registrado.
     *
     * @param tiempoE instante de cierre de estadistica para calculo de medias
     * @throws IllegalArgumentException si tiempoE es anterior al último evento registrado
     */
    public void cierraEstadistica(long tiempoE) {
        if (tiempoE < tUltimoEvento)
            throw new IllegalArgumentException("Estadistica.cierraEstadistica: inconsistencia"
                    + tiempoE + " (cierre estadistica) "
                    + " < " + tUltimoEvento + " (ultimo evento)");

        enAeropuertoPorTiempo += (long) cuentaEnAeropuerto * (tiempoE - ultimoCambioAeronaves);
        enPistasPorTiempo += (long) cuentaEnPistas * (tiempoE - ultimoCambioAeronaves);
        ultimoCambioAeronaves = tiempoE;
        tUltimoEvento = tiempoE;
    }

    /**
     * Devuelve instante del último evento registrado
     *
     * @return instante del último evento registrado
     */
    public long getCuandoUltimoRegistro() {
        return tUltimoEvento;
    }

    /**
     * Devuelve un String con resultados estadísticos.
     *
     * @return String con los resultados estadísticos
     */
    public String toString() {
        int totalAte = getAterrizajesEnHora() + getAterrizajesDemora();
        int totalDes = getDespeguesEnHora() + getDespeguesDemora();
        double puntualidadAte = 100;
        double puntualidadDes = 100;
        double puntualidadTot = 100;
        if (totalAte > 0) puntualidadAte = 1000 * getAterrizajesEnHora() / totalAte / 10.0;
        if (totalDes > 0) puntualidadDes = 1000 * getDespeguesEnHora() / totalDes / 10.0;
        if (totalDes + totalAte > 0) puntualidadTot =
                1000 * (getAterrizajesEnHora() + getDespeguesEnHora()) / (totalAte + totalDes) / 10.0;

        String a = "Estad�sticas en el instante T= " + tUltimoEvento + "\n";
        a += "\tATERRIZAJES";
        a += "\tEnHora: " + getAterrizajesEnHora();
        a += "\tRetrasos: " + getAterrizajesDemora();
        a += "\tPuntualidad: " + puntualidadAte + "\n";
        a += "\tDESPEGUES  ";
        a += "\tEnHora: " + getDespeguesEnHora();
        a += "\tRetrasos: " + getDespeguesDemora();
        a += "\tPuntualidad: " + puntualidadDes + "\n";
        a += "\tTOTAL      ";
        a += "\tEnHora: " + (getDespeguesEnHora() + getAterrizajesEnHora());
        a += "\tRetrasos: " + (getDespeguesDemora() + getAterrizajesDemora());
        a += "\tPuntualidad: " + puntualidadTot + "\n";
        a += "\tOCUPACION_AEROPUERTO ";
        a += "\tActual: " + getNroEnAeropuerto();
        a += "\tM�xima: " + getMaximoEnAeropuerto();
        a += "\tMedia:  " + getMediaEnAeropuerto() + "\n";
        a += "\tOCUPACION_PISTAS     ";
        a += "\tActual: " + getNroEnPistas();
        a += "\tM�xima: " + getMaximoEnPistas();
        a += "\tMedia:  " + getMediaEnPistas() + "\n";
        return a;
    }

}
