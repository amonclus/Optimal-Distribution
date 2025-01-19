package FONTS.Domini;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa un algoritmo de aproximación para resolver el problema de ordenación de productos.
 */
public class AlgoritmoAproximacion implements Algoritmo {

    /**
     * Ejecuta el algoritmo de aproximación para resolver el problema de ordenación de productos.
     * @param relaciones Relaciones entre los productos
     * @param productos Lista de productos a ordenar
     * @return Mapa con el orden de los productos
     */
    @Override
    public Map<Integer, Producto> ejecutarAlgoritmo(Relacion relaciones, ArrayList<Producto> productos) {
        Grafo G = new Grafo();
        G.generarListaAdyacencia(productos, relaciones);
        List<Producto> ordenProductos = G.primMST();
        return eliminarRepetidos(ordenProductos,relaciones, productos);
    }

    /**
     * Cálcula varias listas sin repetidos del MST y devuelve el mapa con el mejor coste después de haber hecho cerca local iterada en cada lista sin repetidos.
     * @param ordenProductos Lista de productos en el orden del camino
     * @param relaciones Relaciones entre los productos
     * @param productos Lista de productos
     * @return Mapa con el orden de los productos
     */
    public Map<Integer, Producto>  eliminarRepetidos(List<Producto> ordenProductos, Relacion relaciones, ArrayList<Producto> productos) {
        Map<Integer, Producto> solucion = new HashMap<>();
        Double mejorCoste = Double.NEGATIVE_INFINITY;
    
        // Recorremos cada posible punto de inicio en el ciclo
        for (int inicio = 0; inicio < ordenProductos.size(); inicio++) {
            Set<Producto> visitados = new HashSet<>();
            List<Producto> ordenSinRepetidos = new ArrayList<>();
    
            // Construimos el orden sin repetidos comenzando desde el punto actual
            for (int i = 0; i < ordenProductos.size(); i++) {
                Producto producto = ordenProductos.get((inicio + i) % ordenProductos.size());
                if (!visitados.contains(producto)) {
                    visitados.add(producto);
                    ordenSinRepetidos.add(producto);
                }
            }
    
            // Creamos la primera solucion de este orden
            Map<Integer, Producto> solucionTemp = new HashMap<>();
            for (int j = 0; j < ordenSinRepetidos.size(); j++) {
                solucionTemp.put(j, ordenSinRepetidos.get(j));
            }
            // Hacemos busqueda local iterada sobre la primera solucion de este orden
            solucionTemp = cercaLocalIterada(productos, ordenSinRepetidos, relaciones);
            Double costeActual = evaluarSolucion(solucionTemp, relaciones);
            // Actualizamos si encontramos un mejor orden
            if (costeActual > mejorCoste) {
                mejorCoste = costeActual;
                solucion = solucionTemp;
            }
        }

        return solucion;
    }

    /**
     * Realiza una búsqueda local iterada sobre una solución inicial usando búsquedas locales 2-opt y 3-opt. Realiza un máximo de n iteraciones y devuelve el mapa con el mejor coste encontrado.
     * @param productos Lista de productos
     * @param ordenProductos Lista de productos en el orden actual
     * @param relaciones Relaciones entre los productos
     * @return Mapa con el orden de los productos
     */
    public Map<Integer, Producto> cercaLocalIterada(ArrayList<Producto> productos, List<Producto> ordenProductos, Relacion relaciones) {
        // Inicialización de la solución
        Map<Integer, Producto> solucionActual = inicializarSolucion(ordenProductos);
        Double puntuacionActual = evaluarSolucion(solucionActual, relaciones);

        // Inicializacion de la mejor solucion
        Map<Integer, Producto> mejorSolucion = solucionActual;
        Double mejorPuntuacion = puntuacionActual;

        // Bucle de iteración de la cerca local iterada
        boolean hayMejora = true;
        int veces =0;
        while (hayMejora) {
            hayMejora = false;

            // Realizar la búsqueda local 3-opt
            Map<Integer, Producto> solucionVecina3opt = busquedaLocal3opt(solucionActual, relaciones);
            Double puntuacionVecina3opt;
            if(solucionVecina3opt!=null)
                puntuacionVecina3opt= evaluarSolucion(solucionVecina3opt, relaciones);
            else
                puntuacionVecina3opt = 0.0;

            // Si encontramos una mejor solución, la actualizamos
            if (puntuacionVecina3opt > mejorPuntuacion) {
                mejorSolucion = solucionVecina3opt;
                mejorPuntuacion = puntuacionVecina3opt;
                hayMejora = true;
            }

            //Realizamos busqueda local con 2-opt
            Double puntuacionVecina2opt = 0.0;
            Map<Integer, Producto> solucionVecina2opt = null;
            List<Map<Integer, Producto>> vecinos = generarVecinos2opt(mejorSolucion);
            for (Map<Integer, Producto> vecino : vecinos) {
                Double puntuacionVecino2opt = evaluarSolucion(vecino, relaciones);
                if (puntuacionVecino2opt > puntuacionVecina2opt) {
                    solucionVecina2opt = vecino;
                    puntuacionVecina2opt = puntuacionVecino2opt;
                }
            }

            // Si encontramos una mejor solución, la actualizamos
            if(puntuacionVecina2opt>mejorPuntuacion) {
                mejorSolucion = solucionVecina2opt;
                mejorPuntuacion = puntuacionVecina2opt;
                hayMejora = true;
            }

            // Guardar una perturbación con 2-opt
            Map<Integer, Producto> solucionPerturbada = solucionVecina2opt;

            // Actualizamos la solución actual con la perturbada
            solucionActual = solucionPerturbada;

            veces++;
            //Como mucho el bucle se ejecutará n veces, siendo n el número de productos
            if(hayMejora && veces<productos.size()) {
                hayMejora = false;

            }

        }
        // Al finalizar, retornamos la mejor solución encontrada
        return mejorSolucion;
    }

    /**
     * Realiza una búsqueda local 3-opt sobre una solución actual.
     * @param solucionActual Solución actual
     * @param relaciones Relaciones entre los productos
     * @return Mapa con el orden de los productos
     */
    private Map<Integer, Producto> busquedaLocal3opt(Map<Integer, Producto> solucionActual, Relacion relaciones) {
        Map<Integer, Producto> mejorVecino = null;
        Double mejorPuntuacion = evaluarSolucion(solucionActual, relaciones);

        List<Map<Integer, Producto>> vecinos = generarVecinos3opt(solucionActual);
        for (Map<Integer, Producto> vecino : vecinos) {
            Double puntuacionVecino = evaluarSolucion(vecino, relaciones);
            if (puntuacionVecino > mejorPuntuacion) {
                mejorVecino = vecino;
                mejorPuntuacion = puntuacionVecino;
            }
        }

        return mejorVecino;
    }


    /**
     * Inicializa una solución con el orden de los productos.
     * @param ordenProductos Lista de productos en el orden actual
     * @return Mapa con el orden de los productos
     */
    private Map<Integer, Producto> inicializarSolucion(List<Producto> ordenProductos) {
        Map<Integer, Producto> solucion = new HashMap<>();
        for (int i = 0; i < ordenProductos.size(); i++) {
            solucion.put(i, ordenProductos.get(i));
        }

        return solucion;
    }

    /**
     * Evalúa una solución dada.
     * @param solucion Solución a evaluar
     * @param relaciones Relaciones entre los productos
     * @return Coste total de la solución
     */
    public Double evaluarSolucion(Map<Integer, Producto> solucion, Relacion relaciones) {
        Double costeTotal = 0.0;
        // Iteramos por el mapa 'solucion' considerando el orden de los productos
        List<Producto> productosOrdenados = new ArrayList<>(solucion.values());
        
        for (int i = 0; i < productosOrdenados.size() - 1; i++) {
            Producto actual = productosOrdenados.get(i);
            Producto siguiente = productosOrdenados.get(i + 1);
            
            // Se suma el coste de la relación entre 'actual' y 'siguiente'
            costeTotal += relaciones.getSimilarity(actual.getName(), siguiente.getName());
        }
    
        // Finalmente, cerramos el ciclo, sumando el coste entre el primer y el último producto
        Producto primerProducto = productosOrdenados.get(0);
        Producto ultimoProducto = productosOrdenados.get(productosOrdenados.size() - 1);
        costeTotal += relaciones.getSimilarity(ultimoProducto.getName(), primerProducto.getName());
    
        return costeTotal;
    }

    /**
     * Genera los vecinos de una solución mediante un movimiento 2-opt.
     * @param solucion Solución actual
     * @return Lista de vecinos generados
     */
    public List<Map<Integer, Producto>> generarVecinos2opt(Map<Integer, Producto> solucion) {
        List<Map<Integer, Producto>> vecinos = new ArrayList<>();
        for (int i = 0; i < solucion.size() - 1; i++) {
            Map<Integer, Producto> vecino = new HashMap<>(solucion);
            Producto temp = vecino.get(i);
            vecino.put(i, vecino.get(i + 1));
            vecino.put(i + 1, temp);
            vecinos.add(vecino);
        }
        return vecinos;
    }

    /**
     * Genera los vecinos de una solución mediante un movimiento 3-opt.
     * @param solucion Solución actual
     * @return Lista de vecinos generados
     */
    public List<Map<Integer, Producto>> generarVecinos3opt(Map<Integer, Producto> solucion) {
        List<Map<Integer, Producto>> vecinos = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(solucion.keySet()); // Claves del mapa
        List<Producto> valores = new ArrayList<>(solucion.values()); // Valores del mapa

        for (int i = 0; i < keys.size() - 2; i++) {
            for (int j = i + 1; j < keys.size() - 1; j++) {
                for (int k = j + 1; k < keys.size(); k++) {
                    Map<Integer, Producto> vecino = new HashMap<>(solucion);

                    // Dividir la lista en segmentos
                    List<Producto> segmento1 = new ArrayList<>(valores.subList(0, i));
                    List<Producto> segmento2 = new ArrayList<>(valores.subList(i, j));
                    List<Producto> segmento3 = new ArrayList<>(valores.subList(j, k));
                    List<Producto> segmento4 = new ArrayList<>(valores.subList(k, valores.size()));

                    // Reorganizar los segmentos
                    Collections.reverse(segmento2);
                    Collections.reverse(segmento3);

                    // Reconstruir la solución
                    List<Producto> nuevoOrden = new ArrayList<>();
                    nuevoOrden.addAll(segmento1);
                    nuevoOrden.addAll(segmento2);
                    nuevoOrden.addAll(segmento3);
                    nuevoOrden.addAll(segmento4);

                    // Convertir la lista de nuevo en un mapa
                    for (int index = 0; index < nuevoOrden.size(); index++) {
                        vecino.put(keys.get(index), nuevoOrden.get(index));
                    }

                    vecinos.add(vecino);
                }
            }
        }
        return vecinos;
    }

}
