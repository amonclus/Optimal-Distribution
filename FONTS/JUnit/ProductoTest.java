package FONTS.JUnit;

import FONTS.Domini.Producto;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductoTest {

    private Producto producto;

    @Before
    public void setUp() {
        ArrayList<String> attributes = new ArrayList<>(Arrays.asList("Attr1", "Attr2"));
        producto = new Producto("Producto1", "TipoA", attributes);
    }

    @Test
    public void testGetName() {
        assertEquals("Producto1", producto.getName());
    }

    @Test
    public void testSetName() {
        producto.setName("Producto2");
        assertEquals("Producto2", producto.getName());
    }

    @Test
    public void testGetType() {
        assertEquals("TipoA", producto.getType());
    }

    @Test
    public void testSetType() {
        producto.setType("TipoB");
        assertEquals("TipoB", producto.getType());
    }

    @Test
    public void testGetAttributes() {
        ArrayList<String> expectedAttributes = new ArrayList<>(Arrays.asList("Attr1", "Attr2"));
        assertEquals(expectedAttributes, producto.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        ArrayList<String> newAttributes = new ArrayList<>(Arrays.asList("Attr3", "Attr4"));
        producto.setAttributes(newAttributes);
        assertEquals(newAttributes, producto.getAttributes());
    }

    @Test
    public void testToString() {
        String expected = "Producto [Nombre=Producto1, Tipo=TipoA, Atributos=Attr1 Attr2]";
        assertEquals(expected, producto.toString());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(producto.equals(producto));
    }


    @Test
    public void testEquals_DifferentObjectDifferentValues() {
        ArrayList<String> attributes = new ArrayList<>(Arrays.asList("Attr3", "Attr4"));
        Producto otroProducto = new Producto("Producto2", "TipoB", attributes);
        assertFalse(producto.equals(otroProducto));
    }

    @Test
    public void testHashCode_Consistency() {
        int initialHashCode = producto.hashCode();
        assertEquals(initialHashCode, producto.hashCode());
    }


    @Test
    public void testHashCode_UnequalObjects() {
        ArrayList<String> attributes = new ArrayList<>(Arrays.asList("Attr3", "Attr4"));
        Producto otroProducto = new Producto("Producto2", "TipoB", attributes);
        assertNotEquals(producto.hashCode(), otroProducto.hashCode());
    }
}