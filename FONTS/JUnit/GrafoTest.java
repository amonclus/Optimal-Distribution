package FONTS.JUnit;

import FONTS.Utils.Arista;
import FONTS.Domini.Grafo;
import FONTS.Domini.Producto;
import FONTS.Domini.Relacion;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GrafoTest {

    private Grafo grafo;
    private Relacion relaciones;
    private Producto p1;
    private Producto p2;
    private Producto p3;
    private Producto p4;

    @Before
    public void setUp() {
        grafo = new Grafo();
        relaciones = Relacion.getInstance();

        p1 = new Producto("Producto1", "TipoA", new ArrayList<>());
        p2 = new Producto("Producto2", "TipoB", new ArrayList<>());
        p3 = new Producto("Producto3", "TipoC", new ArrayList<>());
        p4 = new Producto("Producto4", "TipoD", new ArrayList<>());

        Map<String, Map<String, Double>> similitudes = new HashMap<>();
        similitudes.put("Producto1", new HashMap<>());
        similitudes.put("Producto2", new HashMap<>());
        similitudes.put("Producto3", new HashMap<>());
        similitudes.put("Producto4", new HashMap<>());

        similitudes.get("Producto1").put("Producto2", 0.8);
        similitudes.get("Producto1").put("Producto3", 0.5);
        similitudes.get("Producto2").put("Producto4", 0.7);

        relaciones.setRelaciones(similitudes);
    }

    @Test
    public void testAgregarProducto() {
        grafo.agregarProducto(p1);
        assertTrue(grafo.getListaAdyacencia().containsKey(p1));
    }

    @Test
    public void testAgregarArista() {
        grafo.agregarProducto(p1);
        grafo.agregarProducto(p2);
        grafo.agregarArista(p1, p2, 1.0);

        List<Arista> adyacencias = grafo.obtenerAdyacencias(p1);
        assertEquals(1, adyacencias.size());
        assertEquals(p2, adyacencias.get(0).getDestino());
        assertEquals(1.0, adyacencias.get(0).getPeso(), 0.01);
    }

    @Test
    public void testGenerarListaAdyacencia() {
        ArrayList<Producto> productos = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        grafo.generarListaAdyacencia(productos, relaciones);

        Map<Producto, ArrayList<Arista>> listaAdyacencia = grafo.getListaAdyacencia();

        assertTrue(listaAdyacencia.containsKey(p1));
        assertTrue(listaAdyacencia.containsKey(p2));
        assertTrue(listaAdyacencia.containsKey(p3));
        assertTrue(listaAdyacencia.containsKey(p4));

        List<Arista> adyacenciasP1 = listaAdyacencia.get(p1);
        assertEquals(2, adyacenciasP1.size());
        assertTrue(adyacenciasP1.stream().anyMatch(a -> a.getDestino().equals(p2)));
        assertTrue(adyacenciasP1.stream().anyMatch(a -> a.getDestino().equals(p3)));
    }

    @Test
    public void testPrimMST_Conexo() {
        ArrayList<Producto> productos = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        grafo.generarListaAdyacencia(productos, relaciones);

        List<Producto> mst = grafo.primMST();

        assertNotNull(mst);
        assertEquals(4, mst.size()); // Debería incluir todos los nodos
        assertTrue(mst.contains(p1));
        assertTrue(mst.contains(p2));
        assertTrue(mst.contains(p3));
        assertTrue(mst.contains(p4));
    }

    @Test
    public void testPrimMST_NoConexo() {
        grafo.agregarProducto(p1);
        grafo.agregarProducto(p2);

        List<Producto> mst = grafo.primMST();

        assertNull(mst); // Grafo no conexo
    }
}
