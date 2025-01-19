package FONTS.Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa un algoritmo de ordenación de productos. En este caso, el algoritmo es un stub que asigna a cada producto una posición en el mapa.
 */
public class AlgoritmoStub implements  Algoritmo {

    /**
     * Ejecuta el algoritmo de ordenación de productos. En este caso, el algoritmo es un stub que asigna a cada producto la posición del mapa en la que se encuentra en la lista de productos.
     * @param relaciones Relaciones entre productos
     * @param productos Lista de productos
     * @return Mapa con el recorrido de productos
     */
    @Override
    public Map<Integer, Producto> ejecutarAlgoritmo(Relacion relaciones, ArrayList<Producto> productos) {
        // Retorna un mapa simulado con posiciones predefinidas para los productos
        Map<Integer, Producto> posicionesStub = new HashMap<>();

        // Simulación: asignar cada producto en la lista de productos a una posición en el map
        for (int i = 0; i < productos.size(); i++) {
            posicionesStub.put(i, productos.get(i));
        }

        return posicionesStub;
    }
}
