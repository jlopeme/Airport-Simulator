package aeropuerto;

import log.Logger;

import java.io.IOException;

/**
 * Prepara una simulacion, la lanza
 * e imprime los resultados estadísticos obtenidos.
 * <p/>
 * 
 *
 * @author Javier López Medina
 * @version 1.0
 */
public class Simulador {
	
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger("aeropuerto.Simulador");

    /**
     * Generador aleatorio
     */
    private Generador generador;

    /**
     * Controlador aeroportuario
     */
    private Controlador controlador;

    /**
     * Instante fin de simulacion
     */
    private long finSimulacion = 0;

    /**
     * Instante actual de simulacion
     */
    private long ahora = 0;

    /**
     * Constructor.
     * Inicializa el generador aleatorio, el controlador y el instante fin de simulacion.
     *
     * @param parametros para la simulación
     */
    public Simulador(Parametros parametros) {
        generador = new Generador(parametros);
        controlador = new Controlador(parametros.getNumeroPistas(), (int) Math.round(parametros.getDuracionSlot()));
        finSimulacion = 0;
    }

    /**
     * @return instante de fin de simulacion
     */
    public long getFinSimulacion() {
        return finSimulacion;
    }

    /**
     * @return instante actual de simulacion
     */
    public long getAhora() {
        return ahora;
    }

    /**
     * Calcula y devuelve la estadistica de la simulacion
     *
     * @param instante instante en el que se calculan estadísticas
     * @return estadistica de la simulacion
     * @throws IllegalArgumentException si cuando es anterior al instante actual de simulacion
     */
    public Estadistica calculaEstadistica(long instante) {
        Estadistica esta = null;
        try {
            esta = controlador.getEstadistica(instante);
        }
        catch (Exception e) {
            LOGGER.warning("Error al calcular estadistica." + e);
        }

        return esta;
    }
    /**
     * Lógica del simulador.
     * Este método es la lógica que debe seguir cada evento en el simulador.
     * @param miEvento evento que va a empezar a procesar la lógica del simulador
     * @return evento resultante después de terminar la lógica
     */
    private EventoAeroportuario logica(EventoAeroportuario miEvento) {
    	EventoAeroportuario eventoOtro = null;
    	if( (miEvento.isAterrizaje() && miEvento.isIntento()) || (miEvento.isReintento() && miEvento.isAterrizaje()) ) {
    		if(controlador.getPistasLibres() > 0){
    			controlador.ocupaPista(miEvento);
    			eventoOtro = EventoAeroportuario.generaFin(miEvento, controlador.getSlotAeroportuario());
    		}else{
    			eventoOtro = EventoAeroportuario.generaReintento(miEvento, generador);
    		}
    	}
    	if (miEvento.isFin() && miEvento.isAterrizaje()){
			controlador.liberaPista(miEvento);
			eventoOtro = EventoAeroportuario.generaSalida(miEvento, generador);
    	}
    	if( (miEvento.isDespegue() && miEvento.isIntento()) || (miEvento.isDespegue()&& miEvento.isReintento()) ){
    		if(controlador.getPistasLibres()>0){
    			controlador.ocupaPista(miEvento);
    			eventoOtro = EventoAeroportuario.generaFin(miEvento, controlador.getSlotAeroportuario());
    		}else{
    			eventoOtro = EventoAeroportuario.generaReintento(miEvento, generador);
    		}
    	}
    	if(miEvento.isFin()&& miEvento.isDespegue()){
    		controlador.liberaPista(miEvento);
    	}
    	return eventoOtro;
    	
    }
    /**
     * Simulador.
     * Este método es el bucle que utiliza la lógica para cada evento.
     * Saltan las siguiente excepciones si hay algún problema y la simulación termina.
     * @throws IllegalArgumentException si algún valor está fuera de rango, según el método Parametros.compruebaParametros().
     * @throws NumberFormatException si algún valor está mal escrito.
     * @throws IOException si hay problemas con el fichero.
     * @param hastaCuando es el tiempo que vamos a simular
     */
    public void simulaBucle(long hastaCuando){
    	LOGGER.info("Comienza la simulacion");
        ColaEventos cola = new ColaEventos();
        EventoAeroportuario miEvento;
        miEvento = EventoAeroportuario.generaLlegada(null,generador);
        while (miEvento.getCuando() <= hastaCuando){
        	EventoAeroportuario evento2= logica(miEvento);
        	cola.inserta(evento2);
        	if (miEvento.isAterrizaje() && miEvento.isIntento()){
        		EventoAeroportuario eventoOtro = EventoAeroportuario.generaLlegada(miEvento,generador);
        		cola.inserta(eventoOtro);
        	}
        	miEvento = (EventoAeroportuario) cola.extrae();
        }
        LOGGER.info("Ha concluido la simulacion");
    }
    /**
     * Prepara y lanza la simulación.
     * Al acabar imprime las estadísticas.
     * Requiere dos argumentos de entrada:
     * <ol>
     * <li>tiempo que dura la simulación (debe ser > 0)
     * <li>fichero con los parámetros de simulación
     * </ol>
     *
     * @param argumentos [tiempo_de_simulación] [fichero_de_par�metros]
     * @throws IOException              si hay problemas con el fichero de parámetros
     * @throws IllegalArgumentException si algún argumento o parámetro está fuera de rango
     * @throws NumberFormatException    si el valor de algún parámetro está mal escrito
     * @see aeropuerto.Parametros
     */
    public static void main(String[] argumentos)
            throws IOException {
        if (argumentos.length != 2) {
            System.err.println("Simulador [hasta_cuando] [fichero_parametros.ini]");
            System.exit(1);
        }

        /*
        * Inicialización
        */
        
        long finSimulacion = Long.parseLong(argumentos[0]);
        Parametros parametros = new Parametros(argumentos[1]);

        LOGGER.info("Inicio de simulacion. Duraci�n=" + finSimulacion);
        LOGGER.info("Par�metros de simulacion = " + parametros);

        Simulador sim = new Simulador(parametros);

        /*
        * Ejecuci�n del bucle de simulación
        */
        sim.simulaBucle(finSimulacion);

        LOGGER.info("Fin de simulacion");

        /*
        * Resultados estadisticos finales
        */
        
        Estadistica estadistica = sim.calculaEstadistica(finSimulacion);
        LOGGER.info("Resultado " + estadistica);
        System.out.println("Resultado " + estadistica);
    }
}

