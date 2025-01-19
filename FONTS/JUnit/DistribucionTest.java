package FONTS.JUnit;

import FONTS.Domini.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DistribucionTest {

    private Distribucion distribucion;
    private Relacion relacion;
    private ArrayList<Producto> productos;
    private Algoritmo algoritmoStub;

    @Before
    public void setUp() {
        Map<Integer, Producto> posiciones = new HashMap<>();
        distribucion = new Distribucion(posiciones);
        distribucion.setId("D1");
        relacion = Relacion.getInstance();
        relacion.setRelaciones(new HashMap<>());
        productos = new ArrayList<>();
        algoritmoStub = new AlgoritmoStub(); // Using manual stub
    }


    @Test
    public void testAddRestriccion_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.addRestriccion(p1, 1);
        assertTrue(distribucion.getRestricciones().containsKey(p1));
        assertEquals(Integer.valueOf(1), distribucion.getRestricciones().get(p1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRestriccion_DuplicateProduct() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.addRestriccion(p1, 1);
        // Attempt to add the same product again
        distribucion.addRestriccion(p1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRestriccion_DuplicatePosition() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.getPosiciones().put(2, p2);
        distribucion.addRestriccion(p1, 1);
        // Attempt to add another product to the same position
        distribucion.addRestriccion(p2, 1);
    }

    @Test
    public void testAddRestriccion_SwapProducts() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.getPosiciones().put(2, p2);
        distribucion.addRestriccion(p1, 2); // Should swap p1 and p2

        assertEquals(p1, distribucion.getPosiciones().get(2));
        assertEquals(p2, distribucion.getPosiciones().get(1));
        assertTrue(distribucion.getRestricciones().containsKey(p1));
        assertEquals(Integer.valueOf(2), distribucion.getRestricciones().get(p1));
    }

    @Test
    public void testEliminarRestriccion_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.addRestriccion(p1, 1);
        distribucion.eliminarRestriccion("Producto1");
        assertFalse(distribucion.getRestricciones().containsKey(p1));
    }

    @Test
    public void testEliminarRestriccion_NonExistentProduct() {
        distribucion.eliminarRestriccion("ProductoX"); // Should handle gracefully
        assertTrue(distribucion.getRestricciones().isEmpty());
    }

    @Test
    public void testModificarDistribucion_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.getPosiciones().put(2, p2);
        distribucion.modificarDistribucion(p1, p2);
        assertEquals(p2, distribucion.getPosiciones().get(1));
        assertEquals(p1, distribucion.getPosiciones().get(2));
    }

    @Test
    public void testModificarDistribucion_WithRestriction_ShouldNotModify() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.getPosiciones().put(2, p2);
        distribucion.addRestriccion(p1, 1);
        distribucion.modificarDistribucion(p1, p2); // Should not modify
        assertEquals(p1, distribucion.getPosiciones().get(1));
        assertEquals(p2, distribucion.getPosiciones().get(2));
    }

    @Test
    public void testSwapProductos_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.getPosiciones().put(2, p2);
        distribucion.swapProductos(p1, p2);
        assertEquals(p2, distribucion.getPosiciones().get(1));
        assertEquals(p1, distribucion.getPosiciones().get(2));
    }

    @Test
    public void testSwapProductos_SameProduct() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.swapProductos(p1, p1); // Should not swap
        assertEquals(p1, distribucion.getPosiciones().get(1));
    }

    @Test
    public void testSwapProductos_ProductNotInDistribucion() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.swapProductos(p1, p2); // p2 not in distribucion
        assertEquals(p1, distribucion.getPosiciones().get(1));
        assertNull(distribucion.getPosiciones().get(2));
    }

    @Test
    public void testDisplayDistribucion_MoreEstantesThanProductos() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        Map<Integer, Producto> posiciones = new HashMap<>();
        posiciones.put(0, p1);
        posiciones.put(1, p2);
        distribucion = new Distribucion(posiciones);

        String display = distribucion.displayDistribucion(3);
        String expected = "Producto1 \nProducto2 \n\n";
        assertEquals(expected, display);
    }

    @Test
    public void testDisplayDistribucion_LessEstantesThanProductos() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        Producto p3 = new Producto("Producto3", "TipoC", new ArrayList<>());
        Map<Integer, Producto> posiciones = new HashMap<>();
        posiciones.put(0, p1);
        posiciones.put(1, p2);
        posiciones.put(2, p3);
        distribucion = new Distribucion(posiciones);

        String display = distribucion.displayDistribucion(2);
        // Depending on the logic, the exact string may vary. Adjust as necessary.
        String expected = "Producto1 Producto2 \nProducto3 \n";
        assertEquals(expected, display);
    }

    @Test
    public void testCalcularDistribucion_WithAlgoritmoStub() {
        // Using AlgoritmoStub which returns each product mapped to its index
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        productos.addAll(Arrays.asList(p1, p2));

        Distribucion newDistribucion = distribucion.calcularDistribucion(relacion, productos, algoritmoStub, true);
        assertNotNull(newDistribucion);

        Map<Integer, Producto> expected = new HashMap<>();
        expected.put(0, p1);
        expected.put(1, p2);

        assertEquals(expected, newDistribucion.getPosiciones());
    }

    @Test
    public void testEliminarProducto_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        distribucion.getPosiciones().put(1, p1);
        distribucion.getPosiciones().put(2, p2);
        distribucion.addRestriccion(p1, 1);

        distribucion.eliminarProducto("Producto1");
        assertFalse(distribucion.getPosiciones().containsValue(p1));
        assertFalse(distribucion.getRestricciones().containsKey(p1));
        // Check position adjustments
        assertEquals(p2, distribucion.getPosiciones().get(1));
        assertFalse(distribucion.getPosiciones().containsKey(2));
    }

    @Test
    public void testEliminarProducto_NonExistent() {
        distribucion.eliminarProducto("ProductoX"); // Should handle gracefully
        // Ensure no changes
        assertTrue(distribucion.getPosiciones().isEmpty());
    }

    @Test
    public void testSetId() {
        distribucion.setId("D2");
        assertEquals("D2", distribucion.getId());
    }

    @Test
    public void testSetType_GetAproximado() {
        distribucion.setType(true);
        assertTrue(distribucion.getAproximado());
        distribucion.setType(false);
        assertFalse(distribucion.getAproximado());
    }

}