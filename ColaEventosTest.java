package aeropuerto;

import junit.framework.TestCase;


/**
 * Clase de prueba de la funcionalidad de la clase ColaEventos.
 * No se prueba el método toString.
 * 
 * Mi estrategia se ha basado en mirar los métodos de ColaEventos y crear las pruebas de los métodos que 
 * realmente puedan fallar, y aunque utilizo el método extrae() está claro que no falla por eso no he hecho
 * ninguna prueba específica sobre éste método, mientras que inserta() falla claramente en la ordenación de
 * los eventos en la cola, y aunque utilizo el método inserta() en algunas pruebas y no fallan es porque esas
 * pruebas no se basan en inserta() y solo lo utiliza para meter eventos en la cola sin tener en cuenta la 
 * ordenación. 
 * @author Javier López Medina
 * @version 1.0
 */
public class ColaEventosTest extends TestCase {

    /**
     * Prueba si al introducir dos eventos, el método getNroEventos
     * cuenta bien el número de eventos que hay en cola.
     */
    public void testCompruebaNumeroDeEventos() {
        ColaEventos a =new ColaEventos();
        a.inserta(EventoAeroportuario.generaCualquierEvento(1));
        a.inserta(EventoAeroportuario.generaCualquierEvento(2));
        assertEquals(2, a.getNroEventos());
    }
    /**
     * Prueba si el método isVacia funciona correctamente, para eso
     * creo una lista de eventos vacia y miro si realmente devuelve True, como se
     * espera.
     * ¡Esta prueba falla! No devuelve True al estar la cola vacia.
     * El método isVacia() está mal.
     * Después de arreglar isVacia() si pasa la prueba.
     */
    public void testCompruebaSiEstaVacia() {
        ColaEventos a =new ColaEventos();
        assertTrue(a.isVacia());
    }
    /**
     * Prueba si el método extrae funciona correctamente, cuando tenemos una lista de eventos
     * con el mismo tiempo y tiene que extraer el primero insertado.
     * ¡Esta prueba falla! No devuelve el primer evento insertado.
     * El método que falla es inserta().
     * Después de arreglar inserta() si pasa la prueba.
     */
    public void testExtraeEventosConMismoTiempo() {
        ColaEventos a =new ColaEventos();
        EventoAeroportuario b = EventoAeroportuario.generaCualquierEvento(1);
        EventoAeroportuario c = EventoAeroportuario.generaCualquierEvento(1);
        EventoAeroportuario d = EventoAeroportuario.generaCualquierEvento(1);
        a.inserta(b);
        a.inserta(c);
        a.inserta(d);
        assertEquals (b , a.extrae());
    }
    /**
     * Prueba si el método extrae funciona correctamente cuando tenemos una lista de eventos
     * con distinto tiempo y tiene que extraer el anterior en el tiempo.
     * ¡Esta prueba falla! No devuelve el evento anterior en el tiempo.
     * El método que falla es inserta().
     * Después de arreglar inserta() si pasa la prueba.
     */
    public void testExtraeEventosConDistintoTiempo() {
        ColaEventos a =new ColaEventos();
        EventoAeroportuario b = EventoAeroportuario.generaCualquierEvento(2);
        EventoAeroportuario c = EventoAeroportuario.generaCualquierEvento(1);
        a.inserta(b);
        a.inserta(c);
        assertEquals (c , a.extrae());
    } 
}
