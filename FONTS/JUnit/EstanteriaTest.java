package FONTS.JUnit;

import FONTS.Domini.Estanteria;
import FONTS.Domini.Distribucion;
import FONTS.Domini.Producto;
import FONTS.Domini.Algoritmo;
import FONTS.Domini.AlgoritmoStub;
import FONTS.Domini.Relacion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class EstanteriaTest {

    private Estanteria estanteria;

    @Before
    public void setUp() {
        estanteria = Estanteria.getInstance();
        // Reset the singleton's state
        estanteria.setDistribucionesInactivas(new HashMap<>());
        estanteria.setDistribucionActiva(null);
    }

    @Test
    public void testSingletonInstance() {
        Estanteria anotherInstance = Estanteria.getInstance();
        assertSame(estanteria, anotherInstance);
    }

    @Test
    public void testAddEstante() {
        int initialEstantes = Estanteria.getNumeroEstantes();
        estanteria.addEstante(2);
        assertEquals(initialEstantes + 2, Estanteria.getNumeroEstantes());
    }

    @Test
    public void testDeleteEstante_Success() {
        Estanteria.setNumeroEstantes(5);
        estanteria.deleteEstante(2);
        assertEquals(3, Estanteria.getNumeroEstantes());
    }

    @Test
    public void testDeleteEstante_Invalid() {
        Estanteria.setNumeroEstantes(1);
        estanteria.deleteEstante(2); // Should not allow deleting more than existing
        assertEquals(1, Estanteria.getNumeroEstantes());
    }

    @Test
    public void testAddDistribucion_Success() {
        Distribucion d1 = new Distribucion(new HashMap<>());
        d1.setId("D1");
        estanteria.addDistribucion(d1);
        assertTrue(estanteria.getDistribucionesInactivas().containsKey("D1"));
    }

    @Test
    public void testAddDistribucion_DuplicateID() {
        Distribucion d1 = new Distribucion(new HashMap<>());
        d1.setId("D1");
        Distribucion d2 = new Distribucion(new HashMap<>());
        d2.setId("D1");
        estanteria.addDistribucion(d1);
        estanteria.addDistribucion(d2); // Should not add duplicate
        assertEquals(1, estanteria.getDistribucionesInactivas().size());
    }

    @Test
    public void testEliminarDistribucion_Success() {
        Distribucion d1 = new Distribucion(new HashMap<>());
        d1.setId("D1");
        estanteria.addDistribucion(d1);
        estanteria.eliminarDistribucion("D1");
        assertFalse(estanteria.getDistribucionesInactivas().containsKey("D1"));
    }

    @Test
    public void testEliminarDistribucion_NonExistent() {
        estanteria.eliminarDistribucion("D1"); // Should handle gracefully
        assertFalse(estanteria.getDistribucionesInactivas().containsKey("D1"));
    }

    @Test
    public void testActivarDistribucion_Success() {
        Distribucion d1 = new Distribucion(new HashMap<>());
        d1.setId("D1");
        estanteria.addDistribucion(d1);
        estanteria.activarDistribucion("D1");
        assertTrue(estanteria.hayDistribucionActiva());
        assertEquals(d1, estanteria.getDistribucionActiva());
    }

    @Test
    public void testActivarDistribucion_NonExistent() {
        estanteria.activarDistribucion("D1"); // Should not activate
        assertFalse(estanteria.hayDistribucionActiva());
    }

    @Test
    public void testDisplayDistribucion_Active() {
        Distribucion d1 = new Distribucion(new HashMap<>());
        d1.setId("D1");
        estanteria.addDistribucion(d1);
        estanteria.activarDistribucion("D1");
        String display = estanteria.displayDistribucion("D1");
        // Assuming displayDistribucion returns a string representation
        assertNotNull(display);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayDistribucion_NonExistent() {
        estanteria.displayDistribucion("D1"); // Should throw exception
    }

    @Test
    public void testCalcularNuevaDistribucion() {
        // Mocking Algoritmo is recommended, but for simplicity, using AlgoritmoStub
        Algoritmo algoritmo = new AlgoritmoStub();
        Relacion relaciones = Relacion.getInstance();
        relaciones.setRelaciones(new HashMap<>()); // Clear relations
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Producto1", "TipoA", new ArrayList<>()));
        productos.add(new Producto("Producto2", "TipoB", new ArrayList<>()));

        estanteria.calcularNuevaDistribucion("D1", relaciones, productos, algoritmo, true);
        assertTrue(estanteria.getDistribucionesInactivas().containsKey("D1"));
    }

    @Test
    public void testEliminarProductoDeDistribuciones() {
        // Setup
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Map<Integer, Producto> posiciones = new HashMap<>();
        posiciones.put(0, p1);
        Distribucion d1 = new Distribucion(posiciones);
        d1.setId("D1");
        estanteria.addDistribucion(d1);

        // Action
        estanteria.eliminarProductoDeDistribuciones(p1);

        // Assertion
        Distribucion updatedD1 = estanteria.getDistribucionesInactivas().get("D1");
        assertFalse(updatedD1.getPosiciones().containsValue(p1));
        // Active distribution check can be added if necessary
    }

    @Test
    public void testEliminarProductoDeDistribuciones_NoActive() {
        // Setup
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Map<Integer, Producto> posiciones = new HashMap<>();
        posiciones.put(0, p1);
        Distribucion d1 = new Distribucion(posiciones);
        d1.setId("D1");
        estanteria.addDistribucion(d1);

        // Action
        estanteria.eliminarProductoDeDistribuciones(p1);

        // Assertion
        Distribucion updatedD1 = estanteria.getDistribucionesInactivas().get("D1");
        assertFalse(updatedD1.getPosiciones().containsValue(p1));
    }
}