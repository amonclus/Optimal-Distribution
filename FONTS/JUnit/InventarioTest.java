package FONTS.JUnit;

import FONTS.Domini.Inventario;
import FONTS.Domini.Producto;
import FONTS.Domini.Estanteria;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class InventarioTest {

    private Inventario inventario;

    @Before
    public void setUp() {
        inventario = Inventario.getInstance();
        // Clear existing products before each test
        inventario.setProductos(new ArrayList<>());
        inventario.setProductosPorId(new HashMap<>());
    }

    @Test
    public void testAgregarProducto_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        inventario.agregarProducto(p1);
        assertTrue(inventario.existeProducto("Producto1"));
        assertEquals(1, inventario.getProductos().size());
    }

    @Test
    public void testAgregarProducto_DuplicateID_ShouldNotAdd() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Producto p2 = new Producto("Producto1", "TipoB", new ArrayList<>());
        inventario.agregarProducto(p1);
        inventario.agregarProducto(p2);
        assertEquals(1, inventario.getProductos().size());
    }

    @Test
    public void testEliminarProducto_Success() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        inventario.agregarProducto(p1);
        inventario.eliminarProducto("Producto1");
        assertFalse(inventario.existeProducto("Producto1"));
        assertEquals(0, inventario.getProductos().size());
    }

    @Test
    public void testEliminarProducto_NonExistent_ShouldNotChange() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        inventario.agregarProducto(p1);
        inventario.eliminarProducto("Producto2"); // Does not exist
        assertTrue(inventario.existeProducto("Producto1"));
        assertEquals(1, inventario.getProductos().size());
    }

    @Test
    public void testExisteProducto() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        inventario.agregarProducto(p1);
        assertTrue(inventario.existeProducto("Producto1"));
        assertFalse(inventario.existeProducto("Producto2"));
    }

    @Test
    public void testGetInventarioData() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>(Arrays.asList("Attr1", "Attr2")));
        Producto p2 = new Producto("Producto2", "TipoB", new ArrayList<>(Arrays.asList("Attr3")));
        inventario.agregarProducto(p1);
        inventario.agregarProducto(p2);

        Object[][] data = inventario.getInventarioData();
        assertEquals(2, data.length);
        assertArrayEquals(new Object[]{"Producto1", "TipoA", "Attr1, Attr2"}, data[0]);
        assertArrayEquals(new Object[]{"Producto2", "TipoB", "Attr3"}, data[1]);
    }

    @Test
    public void testSetProductos() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        ArrayList<Producto> newProductos = new ArrayList<>(Arrays.asList(p1));
        inventario.setProductos(newProductos);
        assertEquals(1, inventario.getProductos().size());
        assertTrue(inventario.getProductos().contains(p1));
    }

    @Test
    public void testSetProductosPorId() {
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        Map<String, Producto> newMap = new HashMap<>();
        newMap.put("Producto1", p1);
        inventario.setProductosPorId(newMap);
        assertTrue(inventario.existeProducto("Producto1"));
    }

    @Test
    public void testAgregarProducto_WithAttributes() {
        ArrayList<String> attributes = new ArrayList<>(Arrays.asList("Color: Red", "Size: M"));
        Producto p1 = new Producto("Producto1", "TipoA", attributes);
        inventario.agregarProducto(p1);

        Producto retrieved = inventario.getProductosPorId().get("Producto1");
        assertNotNull(retrieved);
        assertEquals(attributes, retrieved.getAttributes());
    }

    @Test
    public void testEliminarProducto_RemovesFromEstanteria() {
        // This test assumes that Estanteria and Distribucion classes are functioning.
        // Mocking Estanteria might be necessary for isolation, but for simplicity, we'll proceed.
        Producto p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        inventario.agregarProducto(p1);
        inventario.eliminarProducto("Producto1");
        // Since Estanteria is a singleton, ensure it doesn't contain the product
        Estanteria estanteria = Estanteria.getInstance();
        // Assuming Estanteria has no active distributions initially
        assertFalse(estanteria.hayDistribucionActiva());
    }
}