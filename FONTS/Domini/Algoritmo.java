package FONTS.Domini;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interfaz que representa un algoritmo de ordenación de productos.
 */
public interface Algoritmo {
    /**
     * Método base que sobreescribirán los algoritmos concretos.
     * @param relaciones Relaciones entre productos.
     * @param productos Lista de productos a ordenar.
     * @return Mapa con los productos ordenados.
     */
    Map<Integer, Producto> ejecutarAlgoritmo(Relacion relaciones, ArrayList<Producto> productos);
}
