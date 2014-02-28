package aeropuerto;

import java.util.Random;

/**
 * Generador aleatorio de tiempos, según los parametros definidos
 * para las distintas distribuciones aleatorias.
 *
 * @author Javier López Medina
 * @version 1.0
 * @see Parametros
 */
public class Generador {

    /**
     * Parámetros para modelar incidencias
     */
    private Parametros parametros;

    /*
    * Generador de números aleatorios
    */
    private Random random;

    /**
     * Constructor. Inicializa el generador de eventos, con los parámetros indicados.
     *
     * @param parametros de las distribuciones aleatorias
     * @throws IllegalArgumentException si parametros es NULL
     * @see Parametros
     */
    public Generador(Parametros parametros) {
        if (parametros == null)
            throw new IllegalArgumentException("Generador " + parametros);
        this.parametros = parametros;
        // Inicializa generador de números aleatorios
        long semilla = parametros.getSemilla();
        if (semilla == 0)
            random = new Random();
        else
            random = new Random(semilla);
    }

    /**
     * Genera aleatoriamente el intervalo entre dos llegadas
     * consecutivas de aeronaves al aeropuerto.
     * El intervalo entre llegadas sigue una distribución de Poisson.
     *
     * @return tiempo hasta la próxima llegada
     */
    public int entreLlegadas() {
        double lambda = parametros.getEntreLlegadas();
        double eLambda = Math.exp(-lambda);
        double producto = 1.0;
        int resultado = 0;
        for (int contador = 0; producto >= eLambda; contador++) {
            producto *= random.nextDouble();
            resultado = contador;
        }
        return resultado;
    }

    /**
     * Genera aleatoriamente la duración de una asistencia en tierra
     * Responde a una distribución normal (campana de Gauss).
     *
     * @return duración de asistencia en tierra
     */
    public int duracionEnTierra() {
        double duracion = random.nextGaussian();
        duracion *= parametros.getDuracionDesviacion();
        duracion += parametros.getDuracionMedia();
        if (duracion < parametros.getDuracionMinima())
            duracion = parametros.getDuracionMinima();
        return (int) Math.round(duracion);
    }

    /**
     * Genera aleatoriamente el tiempo de espera para
     * reintento de utilización de pista.
     * Responde a una distribución normal (campana de Gauss).
     *
     * @return demora
     */
    public int demoraReintento() {
        double demora = random.nextGaussian();
        demora *= parametros.getDemoraDesviacion();
        demora += parametros.getDemoraMedia();
        return (int) Math.round(demora);
    }

}