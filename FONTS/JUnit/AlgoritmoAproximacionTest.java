package FONTS.JUnit;

import FONTS.Domini.AlgoritmoAproximacion;
import FONTS.Domini.Producto;
import FONTS.Domini.Relacion;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AlgoritmoAproximacionTest {

    private AlgoritmoAproximacion algoritmo;
    private Relacion relaciones;
    private ArrayList<Producto> productos;

    @Before
    public void setUp() {
        algoritmo = new AlgoritmoAproximacion();
        relaciones = Relacion.getInstance();

        productos = new ArrayList<>();
        productos.add(new Producto("Producto1", "TipoA", new ArrayList<>()));
        productos.add(new Producto("Producto2", "TipoB", new ArrayList<>()));
        productos.add(new Producto("Producto3", "TipoC", new ArrayList<>()));
        productos.add(new Producto("Producto4", "TipoD", new ArrayList<>()));

        Map<String, Map<String, Double>> similitudes = new HashMap<>();
        similitudes.put("Producto1", new HashMap<>());
        similitudes.put("Producto2", new HashMap<>());
        similitudes.put("Producto3", new HashMap<>());
        similitudes.put("Producto4", new HashMap<>());

        // Define similarity values
        similitudes.get("Producto1").put("Producto2", 0.8);
        similitudes.get("Producto2").put("Producto1", 0.8); // Symmetric

        similitudes.get("Producto1").put("Producto3", 0.5);
        similitudes.get("Producto3").put("Producto1", 0.5); // Symmetric

        similitudes.get("Producto2").put("Producto4", 0.7);
        similitudes.get("Producto4").put("Producto2", 0.7); // Symmetric

        similitudes.get("Producto3").put("Producto4", 0.6);
        similitudes.get("Producto4").put("Producto3", 0.6); // Symmetric

        // Optionally, define similarities for all product pairs
        similitudes.get("Producto1").put("Producto4", 0.4);
        similitudes.get("Producto4").put("Producto1", 0.4);

        similitudes.get("Producto2").put("Producto3", 0.55);
        similitudes.get("Producto3").put("Producto2", 0.55);

        relaciones.setRelaciones(similitudes);
    }

    @Test
    public void testEjecutarAlgoritmo() {
        Map<Integer, Producto> solucion = algoritmo.ejecutarAlgoritmo(relaciones, productos);

        assertNotNull(solucion);
        assertEquals(4, solucion.size());
        assertTrue(solucion.values().containsAll(productos));
    }

    @Test
    public void testEliminarRepetidos() {
        List<Producto> ordenProductos = Arrays.asList(
                productos.get(0), productos.get(1), productos.get(2), productos.get(3), productos.get(0)
        );

        Map<Integer, Producto> solucion = algoritmo.eliminarRepetidos(ordenProductos, relaciones, productos);

        assertNotNull(solucion);
        assertEquals(4, solucion.size());
        assertTrue(solucion.values().containsAll(productos));
    }

    @Test
    public void testCercaLocalIterada() {
        List<Producto> ordenProductos = Arrays.asList(
                productos.get(0), productos.get(1), productos.get(2), productos.get(3)
        );

        Map<Integer, Producto> solucion = algoritmo.cercaLocalIterada(productos, ordenProductos, relaciones);

        assertNotNull(solucion);
        assertEquals(4, solucion.size());
        assertTrue(solucion.values().containsAll(productos));
    }

    @Test
    public void testEvaluarSolucion() {
        Map<Integer, Producto> solucion = new HashMap<>();
        solucion.put(0, productos.get(0));
        solucion.put(1, productos.get(1));
        solucion.put(2, productos.get(2));
        solucion.put(3, productos.get(3));

        Double puntuacion = algoritmo.evaluarSolucion(solucion, relaciones);

        assertNotNull(puntuacion);
        assertTrue(puntuacion > 0);
    }

    @Test
    public void testGenerarVecinos2opt() {
        Map<Integer, Producto> solucion = new HashMap<>();
        solucion.put(0, productos.get(0));
        solucion.put(1, productos.get(1));
        solucion.put(2, productos.get(2));
        solucion.put(3, productos.get(3));

        List<Map<Integer, Producto>> vecinos = algoritmo.generarVecinos2opt(solucion);

        assertNotNull(vecinos);
        assertFalse(vecinos.isEmpty());
        assertTrue(vecinos.size() > 0);
    }

    @Test
    public void testGenerarVecinos3opt() {
        Map<Integer, Producto> solucion = new HashMap<>();
        solucion.put(0, productos.get(0));
        solucion.put(1, productos.get(1));
        solucion.put(2, productos.get(2));
        solucion.put(3, productos.get(3));

        List<Map<Integer, Producto>> vecinos = algoritmo.generarVecinos3opt(solucion);

        assertNotNull(vecinos);
        assertFalse(vecinos.isEmpty());
        assertTrue(vecinos.size() > 0);
    }
}
