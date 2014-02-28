package aeropuerto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Esta clase es un contenedor que agrupa los diferentes
 * parámetros que caracterizan una simulacion.
 * <p/>
 * 
 *
 * @author Javier López Medina
 * @version 1.0
 */
public class Parametros {
    /*
    * Valor de los parámetros
    */
    private final long semilla;
    private final int numeroPistas;
    private final double duracionSlot;
    private final double frecuencia;
    private final double duracionMedia;
    private final double duracionDesviacion;
    private final double duracionMinima;
    private final double demoraMedia;
    private final double demoraDesviacion;

    /*
    * Valores por defecto de los parámetros
    */
    private final long SEMILLA_DEFECTO = 1;
    private final int NUMERO_PISTAS_DEFECTO = 2;
    private final double DURACION_SLOT_DEFECTO = 120;
    private final double FRECUENCIA_DEFECTO = 4;
    private final double DURACION_MEDIA_DEFECTO = 600;
    private final double DURACION_DESVIACION_DEFECTO = 200;
    private final double DURACION_MINIMA_DEFECTO = 100;
    private final double DEMORA_MEDIA_DEFECTO = 180;
    private final double DEMORA_DESVIACION_DEFECTO = 60;

    /*
    * Etiquetas para lectura de parámetros desde fichero
    */
    private static final String SEMILLA = "semilla";
    private static final String NUMERO_PISTAS = "numero.pistas";
    private static final String DURACION_SLOT = "duracion.slot";
    private static final String FRECUENCIA_LLEGADAS = "frecuencia.llegadas";
    private static final String DURACION_MEDIA = "duracion.media";
    private static final String DURACION_DESVIACION = "duracion.desviacion";
    private static final String DURACION_MINIMA = "duracion.minima";
    private static final String DEMORA_MEDIA = "demora.media";
    private static final String DEMORA_DESVIACION = "demora.desviacion";
    
    /**
     * Constructor con valores por defecto
     */
    public Parametros() {
        semilla = SEMILLA_DEFECTO;
        numeroPistas = NUMERO_PISTAS_DEFECTO;
        duracionSlot = DURACION_SLOT_DEFECTO;
        frecuencia = FRECUENCIA_DEFECTO;
        duracionMedia = DURACION_MEDIA_DEFECTO;
        duracionDesviacion = DURACION_DESVIACION_DEFECTO;
        duracionMinima = DURACION_MINIMA_DEFECTO;
        demoraMedia = DEMORA_MEDIA_DEFECTO;
        demoraDesviacion = DEMORA_DESVIACION_DEFECTO;
    }
    /**
     * Nombre del fichero de parámetros
     */
    private static final String FICHERO = "parametros.ini";
    /**
     * Constructor a partir de fichero
     * <p/>
     * Este método carga el fichero parametros.ini, y si salta alguna de las tres excepciones que
     * lanza(IOException,IllegalArgumentException,NumberFormatException) este método, termina
     * la simulación.
     * @param fichero Este es el fichero que contiene los parámetros para el simulador
     * @throws java.io.IOException Salta si hay problemas con el fichero.
     * @throws IllegalArgumentException si algún valor está fuera de rango, según el método Parametros.compruebaParametros().
     * @throws NumberFormatException si algún valor está mal escrito.
     */
    public Parametros(String fichero)
            throws IOException {
        Properties configuracion;
        	 FileInputStream f = new FileInputStream(FICHERO);
        	 configuracion = new Properties ();
        	 configuracion.load(f);
        	 numeroPistas = Integer.parseInt(configuracion.getProperty(NUMERO_PISTAS));
        	 duracionSlot = Double.parseDouble(configuracion.getProperty(DURACION_SLOT));
        	 semilla = Long.parseLong(configuracion.getProperty(SEMILLA));
        	 frecuencia = Double.parseDouble(configuracion.getProperty(FRECUENCIA_LLEGADAS));
        	 duracionMedia = Double.parseDouble(configuracion.getProperty(DURACION_MEDIA));
        	 duracionDesviacion = Double.parseDouble(configuracion.getProperty(DURACION_DESVIACION));
        	 duracionMinima = Double.parseDouble(configuracion.getProperty(DURACION_MINIMA));
        	 demoraMedia = Double.parseDouble(configuracion.getProperty(DEMORA_MEDIA));
        	 demoraDesviacion = Double.parseDouble(configuracion.getProperty(DEMORA_DESVIACION));
        	 compruebaParametros();
    }

    /**
     * Constructor a partir de valores explícitos
     *
     * @param semilla            para forzar un comportamiento repetitivo en las secuencias de números aleatorios
     * @param numeroPistas       número de pistas del Aeropuerto
     * @param duracionSlot       duración del slot aeroportuario para uso de pistas
     * @param frecuencia         frecuencia (llegadas / minuto).
     * @param duracionMedia      duración media de la asistencia en tierra. En segundos.
     * @param duracionDesviacion desviación típica de la asistencia en tierra. En segundos.
     * @param duracionMinima     duración mínima de la asistencia en tierra. En segundos.
     * @param demoraMedia        demora media de las solicitudes cuando pistas ocupadas. En segundos.
     * @param demoraDesviacion   desviación típica de las solicitudes cuando pistas ocupadas. En segundos.
     * @throws IllegalArgumentException si algún valor está fuera de rango
     */
    public Parametros(long semilla,
                      int numeroPistas,
                      double duracionSlot,
                      double frecuencia,
                      double duracionMedia,
                      double duracionDesviacion,
                      double duracionMinima,
                      double demoraMedia,
                      double demoraDesviacion) {
        this.semilla = semilla;
        this.numeroPistas = numeroPistas;
        this.duracionSlot = duracionSlot;
        this.frecuencia = frecuencia;
        this.duracionMedia = duracionMedia;
        this.duracionDesviacion = duracionDesviacion;
        this.duracionMinima = duracionMinima;
        this.demoraMedia = demoraMedia;
        this.demoraDesviacion = demoraDesviacion;
        compruebaParametros();
    }

    /**
     * Comprueba que los valores de los parámetros son correctos
     *
     * @throws IllegalArgumentException si algún valor está fuera de rango
     */
    private void compruebaParametros() {
        if (numeroPistas < 1)
            parametroIncorrecto(Integer.toString(numeroPistas), NUMERO_PISTAS);

        if (duracionSlot < 1.0)
            parametroIncorrecto(Double.toString(duracionSlot), DURACION_SLOT);

        double entreLlegadas = 60.0 / frecuencia;
        if (entreLlegadas < 1.0)
            parametroIncorrecto(Double.toString(frecuencia), FRECUENCIA_LLEGADAS);

        if (duracionMedia < 1.0)
            parametroIncorrecto(Double.toString(duracionMedia), DURACION_MEDIA);

        if (duracionDesviacion < 1.0)
            parametroIncorrecto(Double.toString(duracionDesviacion), DURACION_DESVIACION);

        if (duracionMinima < 0)
            parametroIncorrecto(Double.toString(duracionMinima), DURACION_MINIMA);

        if (demoraMedia < 1.0)
            parametroIncorrecto(Double.toString(demoraMedia), DEMORA_MEDIA);

        if (demoraDesviacion < 1.0)
            parametroIncorrecto(Double.toString(demoraDesviacion), DEMORA_DESVIACION);

    }

    /**
     * Lanza una IllegalArgumentException para un parametro incorrecto
     *
     * @param valor valor del parámetro
     * @param clave nombre del parámetro
     * @throws IllegalArgumentException indicando el parámetro incorrecto
     */
    private void parametroIncorrecto(String valor, String clave) {
        throw new IllegalArgumentException(clave + "= " + valor);
    }

    /**
     * Listado de parámetros
     *
     * @return todos los parámetros
     */
    public String toString() {
        String a = "\n";
        a += "\t" + SEMILLA + "=" + getSemilla() + "\n";
        a += "\t" + NUMERO_PISTAS + "=" + getNumeroPistas() + "\n";
        a += "\t" + DURACION_SLOT + "=" + getDuracionSlot() + "\n";
        a += "\t" + FRECUENCIA_LLEGADAS + "=" + getFrecuencia() + "\n";
        a += "\t" + DURACION_MEDIA + "=" + getDuracionMedia() + "\n";
        a += "\t" + DURACION_DESVIACION + "=" + getDuracionDesviacion() + "\n";
        a += "\t" + DURACION_MINIMA + "=" + getDuracionMinima() + "\n";
        a += "\t" + DEMORA_MEDIA + "=" + getDemoraMedia() + "\n";
        a += "\t" + DEMORA_DESVIACION + "=" + getDemoraDesviacion();
        return a;
    }

    /**
     * La semilla puede forzar un comportamiento repetitivo del generador
     * de números aleatorios.
     * Si vale 0, la serie de números es diferente en cada ejecución.
     * Cualquier otro valor fuerza una serie que se repite.
     *
     * @return semilla para forzar un comportamiento repetitivo
     *         en las secuencias de números aleatorios
     */
    public long getSemilla() {
        return semilla;
    }

    /**
     * Número de pistas del Aeropuerto
     *
     * @return número de pistas del Aeropuerto
     */
    public int getNumeroPistas() {
        return numeroPistas;
    }

    /**
     * Duración del slot (tiempo de uso de la pista por operaci�n)
     *
     * @return duración del slot. En segundos.
     */
    public double getDuracionSlot() {
        return duracionSlot;
    }

    /**
     * Intervalo medio de tiempo entre llegadas
     *
     * @return intervalo medio entre llegadas. En segundos.
     */
    public double getEntreLlegadas() {
        return 60.0 / frecuencia;
    }

    /**
     * Frecuencia de llegadas de aeronaves
     *
     * @return frecuencia (llegadas / minuto).
     */
    public double getFrecuencia() {
        return frecuencia;
    }

    /**
     * Duración media de la asistencia en tierra (tiempo de atención por aeronave)
     *
     * @return duraci�n media de asistencia en tierra. En segundos.
     */
    public double getDuracionMedia() {
        return duracionMedia;
    }

    /**
     * Desviación típica de duración de asistencia en tierra
     *
     * @return desviación típica de la duración de asistencia en tierra. En segundos.
     */
    public double getDuracionDesviacion() {
        return duracionDesviacion;
    }

    /**
     * Duración mínima de asistencia en tierra
     *
     * @return duración mínima de asistencia en tierra. En segundos.
     */
    public double getDuracionMinima() {
        return duracionMinima;
    }

    /**
     * Duración media de las demoras para nuevas solicitudes de pista
     * (cuando pistas ocupadas)
     *
     * @return duración media de demoras. En segundos.
     */
    public double getDemoraMedia() {
        return demoraMedia;
    }

    /**
     * Desviación típica de las demoras para nuevas solicitudes de pista
     * (cuando pistas ocupadas)
     *
     * @return desviación típica de demoras. En segundos.
     */
    public double getDemoraDesviacion() {
        return demoraDesviacion;
    }

}
