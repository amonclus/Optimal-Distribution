package FONTS.JUnit;

import FONTS.Domini.Relacion;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RelacionTest {

    private Relacion relacion;

    @Before
    public void setUp() {
        relacion = Relacion.getInstance();
        relacion.setRelaciones(new java.util.HashMap<>());
    }

    @Test
    public void testAddRelation_Success() {
        relacion.addRelation("ProductoA", "ProductoB", 0.8);
        Double similarity = relacion.getSimilarity("ProductoA", "ProductoB");
        assertNotNull(similarity);
        assertEquals(Double.valueOf(0.8), similarity);

        // Ensure bidirectional
        Double similarityReverse = relacion.getSimilarity("ProductoB", "ProductoA");
        assertEquals(Double.valueOf(0.8), similarityReverse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRelation_SameProduct_ShouldThrowException() {
        relacion.addRelation("ProductoA", "ProductoA", 1.0);
    }

    @Test
    public void testAddRelation_OverwriteExisting() {
        relacion.addRelation("ProductoA", "ProductoB", 0.8);
        relacion.addRelation("ProductoA", "ProductoB", 0.9);
        Double similarity = relacion.getSimilarity("ProductoA", "ProductoB");
        assertEquals(Double.valueOf(0.9), similarity);
    }

    @Test
    public void testGetSimilarity_NonExistentRelation() {
        Double similarity = relacion.getSimilarity("ProductoA", "ProductoC");
        assertNull(similarity);
    }

    @Test
    public void testRemoveProduct_Success() {
        relacion.addRelation("ProductoA", "ProductoB", 0.8);
        relacion.addRelation("ProductoA", "ProductoC", 0.9);
        relacion.removeProduct("ProductoA");

        assertNull(relacion.getSimilarity("ProductoA", "ProductoB"));
        assertNull(relacion.getSimilarity("ProductoA", "ProductoC"));
        assertNull(relacion.getSimilarity("ProductoB", "ProductoA"));
        assertNull(relacion.getSimilarity("ProductoC", "ProductoA"));
    }

    @Test
    public void testRemoveProduct_NonExistentProduct() {
        relacion.addRelation("ProductoA", "ProductoB", 0.8);
        relacion.removeProduct("ProductoC"); // Should not throw exception
        // Existing relations should remain unaffected
        Double similarity = relacion.getSimilarity("ProductoA", "ProductoB");
        assertEquals(Double.valueOf(0.8), similarity);
    }

    @Test
    public void testToString_WithRelations() {
        relacion.addRelation("ProductoA", "ProductoB", 0.8);
        relacion.addRelation("ProductoA", "ProductoC", 0.9);
        String expected = "Relaciones para ProductoA:\nProductoB 0.8\nProductoC 0.9\n";
        assertEquals(expected, relacion.toString("ProductoA"));
    }

    @Test
    public void testToString_NoRelations() {
        String expected = "0 relaciones encontradas para:  ProductoA";
        assertEquals(expected, relacion.toString("ProductoA"));
    }

    @Test
    public void testGetRelaciones() {
        relacion.addRelation("ProductoA", "ProductoB", 0.8);
        relacion.addRelation("ProductoC", "ProductoD", 0.7);
        Map<String, Map<String, Double>> relacionesMap = relacion.getRelaciones();

        // Update the expected size from 2 to 4
        assertEquals(4, relacionesMap.size());

        // Check for all four keys
        assertTrue(relacionesMap.containsKey("ProductoA"));
        assertTrue(relacionesMap.containsKey("ProductoB"));
        assertTrue(relacionesMap.containsKey("ProductoC"));
        assertTrue(relacionesMap.containsKey("ProductoD"));

        // Additionally, verify the correctness of the relations
        assertEquals(Double.valueOf(0.8), relacionesMap.get("ProductoA").get("ProductoB"));
        assertEquals(Double.valueOf(0.8), relacionesMap.get("ProductoB").get("ProductoA"));
        assertEquals(Double.valueOf(0.7), relacionesMap.get("ProductoC").get("ProductoD"));
        assertEquals(Double.valueOf(0.7), relacionesMap.get("ProductoD").get("ProductoC"));
    }

    @Test
    public void testSetRelaciones() {
        Map<String, Map<String, Double>> newRelaciones = new java.util.HashMap<>();
        Map<String, Double> innerMap = new java.util.HashMap<>();
        innerMap.put("ProductoB", 0.85);
        newRelaciones.put("ProductoA", innerMap);
        relacion.setRelaciones(newRelaciones);

        Double similarity = relacion.getSimilarity("ProductoA", "ProductoB");
        assertEquals(Double.valueOf(0.85), similarity);
    }
}