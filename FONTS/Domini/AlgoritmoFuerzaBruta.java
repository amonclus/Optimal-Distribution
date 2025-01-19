package FONTS.Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa un algoritmo de fuerza bruta para la maximización de las relaciones entre productos contiguos en la solución.
 */
public class AlgoritmoFuerzaBruta implements Algoritmo {

    /**
     * Ejecuta el algoritmo de fuerza bruta para maximizar las relaciones entre productos contiguos en la solución.
     * @param relaciones Relaciones entre productos
     * @param productos Lista de productos
     * @return Mapa con el recorrido de productos
     */
    @Override
    public Map<Integer, Producto> ejecutarAlgoritmo(Relacion relaciones, ArrayList<Producto> productos) {
        ArrayList<Producto> mejorRecorrido = new ArrayList<>();
        Set<Producto> visitados = new HashSet<>();
        double[] maxCost = {Double.MIN_VALUE};  // Usamos un arreglo para simular el paso por referencia &
        calcularMejorRecorrido(productos, relaciones, new ArrayList<>(), mejorRecorrido, 0.0, maxCost, visitados);

        // Convertir mejorRecorrido en un Map<Integer, Producto>
        Map<Integer, Producto> resultado = new HashMap<>();
        for (int i = 0; i < mejorRecorrido.size(); i++) {
            resultado.put(i, mejorRecorrido.get(i));
        }
        return resultado;
    }

    /**
     * Calcula el mejor recorrido de productos de forma recursiva. Actualiza el coste actual y hace poda en cada llamada recursiva.
     * @param productos Lista de productos
     * @param relaciones Relaciones entre productos
     * @param recorridoActual Recorrido actual
     * @param mejorRecorrido Mejor recorrido
     * @param costoActual Coste actual del recorrido
     * @param maxCost Coste máximo
     * @param visitados Set de productos visitados
     */
    private void calcularMejorRecorrido(ArrayList<Producto> productos, Relacion relaciones,
                                        ArrayList<Producto> recorridoActual, ArrayList<Producto> mejorRecorrido, 
                                        double costoActual, double[] maxCost, Set<Producto> visitados) {
        if (recorridoActual.size() == productos.size()) {
            // Al tener todas las ciudades visitadas, se agrega la ciudad inicial para hacer un ciclo cerrado
            Producto inicio = recorridoActual.get(0);
            Producto ultimo = recorridoActual.get(recorridoActual.size() - 1);
            costoActual += relaciones.getSimilarity(ultimo.getName(), inicio.getName());

            if (costoActual > maxCost[0]) {
                maxCost[0] = costoActual;  
                mejorRecorrido.clear();
                mejorRecorrido.addAll(recorridoActual);
            }
            return;
        }

        // Recorrido recursivo, probamos con cada ciudad no visitada
        for (Producto producto : productos) {
            if (!visitados.contains(producto)) {
                visitados.add(producto);
                recorridoActual.add(producto);

                // Actualizamos el costo
                double costoSiguiente = costoActual;
                if (recorridoActual.size() > 1) {
                    Producto anterior = recorridoActual.get(recorridoActual.size() - 2);
                    costoSiguiente += relaciones.getSimilarity(anterior.getName(), producto.getName());
                }

                // Llamamos recursivamente si tiene potencial de mejorar el costo actual
                if (productos.size() + 1 - recorridoActual.size() + costoSiguiente > maxCost[0]) {
                    calcularMejorRecorrido(productos, relaciones, recorridoActual, mejorRecorrido, costoSiguiente, maxCost, visitados);
                }

                recorridoActual.remove(recorridoActual.size() - 1);
                visitados.remove(producto);
            }
        }
    }
}

