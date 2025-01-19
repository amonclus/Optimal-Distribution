package FONTS.JUnit;

import FONTS.Domini.AlgoritmoAproximacion;
import FONTS.Domini.AlgoritmoFuerzaBruta;
import FONTS.Domini.Producto;
import FONTS.Domini.Relacion;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AlgoritmoFuerzaBrutaTest {

    private AlgoritmoFuerzaBruta algoritmo;
    private Relacion relaciones;
    private ArrayList<Producto> productos;

    @Before
    public void setUp() {
        algoritmo = new AlgoritmoFuerzaBruta();
        relaciones = Relacion.getInstance();

        // Reset relaciones to ensure a clean state for each test
        relaciones.setRelaciones(new HashMap<>());

        productos = new ArrayList<>();
        productos.add(new Producto("Producto1", "TipoA", new ArrayList<>()));
        productos.add(new Producto("Producto2", "TipoB", new ArrayList<>()));
        productos.add(new Producto("Producto3", "TipoC", new ArrayList<>()));
        productos.add(new Producto("Producto4", "TipoD", new ArrayList<>()));

        Map<String, Map<String, Double>> similitudes = new HashMap<>();
        for (Producto p : productos) {
            similitudes.put(p.getName(), new HashMap<>());
        }

        // Define similarity values (ensure all necessary pairs are covered)
        similitudes.get("Producto1").put("Producto2", 0.8);
        similitudes.get("Producto2").put("Producto1", 0.8); // Symmetric

        similitudes.get("Producto1").put("Producto3", 0.5);
        similitudes.get("Producto3").put("Producto1", 0.5); // Symmetric

        similitudes.get("Producto1").put("Producto4", 0.4);
        similitudes.get("Producto4").put("Producto1", 0.4); // Symmetric

        similitudes.get("Producto2").put("Producto3", 0.55);
        similitudes.get("Producto3").put("Producto2", 0.55); // Symmetric

        similitudes.get("Producto2").put("Producto4", 0.7);
        similitudes.get("Producto4").put("Producto2", 0.7); // Symmetric

        similitudes.get("Producto3").put("Producto4", 0.6);
        similitudes.get("Producto4").put("Producto3", 0.6); // Symmetric

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
    public void testRecorridoCompleto() {
        Map<Integer, Producto> solucion = algoritmo.ejecutarAlgoritmo(relaciones, productos);
        List<Producto> recorrido = new ArrayList<>(solucion.values());

        double costoTotal = 0.0;
        for (int i = 0; i < recorrido.size() - 1; i++) {
            costoTotal += relaciones.getSimilarity(recorrido.get(i).getName(), recorrido.get(i + 1).getName());
        }
        // Añadir costo del ciclo cerrado
        costoTotal += relaciones.getSimilarity(recorrido.get(recorrido.size() - 1).getName(), recorrido.get(0).getName());

        assertTrue(costoTotal > 0);
    }

}
