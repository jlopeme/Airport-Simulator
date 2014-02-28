package aeropuerto;

/**
 * Estado de una operacion aeroportuaria:
 * INTENTO = para eventos de inicio de operacion en hora;
 * REINTENTO = para eventos de inicio de operacion demorados;
 * FIN = para eventos de fin de operacion.
 *
 * @author Javier López Medina
 * @version 1.0
 */

public enum EstadoOperacion {
    INTENTO, REINTENTO, FIN
}