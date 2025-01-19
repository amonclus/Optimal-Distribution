package FONTS.Domini;

import FONTS.Utils.Arista;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * Clase que representa un grafo. Cada nodo del grafo es un producto, cada arista es una relación entre dos productos y el peso es la similitud entre ellos.
 */
public class Grafo {
    /** Lista de adyacencia del grafo: key = Producto, value = Lista de aristas adyacentes */
    private Map<Producto, ArrayList<Arista>> listaAdyacencia;

    /**
     * Constructor de la clase. Crea un grafo con una lista de adyacencia vacía.
     */
    public Grafo() {
        this.listaAdyacencia = new HashMap<>();
    }

    /**
     * Añade un producto al grafo
     * @param producto Producto a añadir
     */
    public void agregarProducto(Producto producto) {
        listaAdyacencia.putIfAbsent(producto, new ArrayList<>());
    }

    /**
     * Añade una arista al grafo
     * @param origen Producto origen de la arista
     * @param destino Producto destino de la arista
     * @param peso Peso de la arista
     */
    public void agregarArista(Producto origen, Producto destino, double peso) {
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        listaAdyacencia.get(destino).add(new Arista(origen, peso)); // Si el grafo es no dirigido
    }

    /**
     * Devuelve las adyacencias de un producto
     * @param producto Producto del que se quieren obtener las adyacencias
     * @return Lista de adyacencias del producto
     */
    public ArrayList<Arista> obtenerAdyacencias(Producto producto) {
        return listaAdyacencia.getOrDefault(producto, new ArrayList<>());
    }

    /**
     * Genera la lista de adyacencia del grafo a partir de una lista de productos y una matriz de relaciones
     * @param productos Lista de productos
     * @param relaciones Matriz de relaciones
     */
    public void generarListaAdyacencia(ArrayList<Producto> productos, Relacion relaciones) {

        listaAdyacencia = new HashMap<>();

        for (Producto producto : productos) {
            agregarProducto(producto);
        }
        // Iterar sobre los productos
        for (Producto producto : productos) {
            String nombreProducto = producto.getName();
            // Iterar sobre los demás productos para encontrar relaciones
            for (Producto otroProducto : productos) {
                if (!otroProducto.equals(producto)) {
                    String nombreProductoAdyacente = otroProducto.getName();

                    // Obtener la similitud desde la clase Relacion
                    Double similitud = relaciones.getSimilarity(nombreProducto, nombreProductoAdyacente);

                    // Si existe una relación, añadirla a la lista de adyacencia con el peso invertido
                    if (similitud != null) {
                        agregarArista(producto, otroProducto, similitud * (-1)+1);
                    }
                }
            }
        }
    }

    /**
     * Devuelve el recorrido del MST (Minimum Spanning Tree) del grafo utilizando el algoritmo de Prim y incluyendo los nodos repetidos.
     * @return Lista del orden de productos que forman el MST incluyendo repetidos.
     */
    public List<Producto> primMST() {
        if (listaAdyacencia.isEmpty()) {
            return null;
        }

        Set<Producto> visitado = new HashSet<>();
        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingDouble(Arista::getPeso));
        List<Producto> mstOrden = new ArrayList<>();

        // Start from the first node
        Producto startNode = listaAdyacencia.keySet().iterator().next();
        visitado.add(startNode);
        mstOrden.add(startNode);
        pq.addAll(obtenerAdyacencias(startNode));

        while (!pq.isEmpty()) {
            Arista arista = pq.poll();
            Producto nodo = arista.getDestino();

            if (!visitado.contains(nodo)) {
                visitado.add(nodo);
                mstOrden.add(nodo);
                pq.addAll(obtenerAdyacencias(nodo));
            }
        }

        // Verify if the graph is connected
        if (visitado.size() != listaAdyacencia.size()) {
            return null;
        }

        return mstOrden;
    }

    // Getters y setters
    public Map<Producto, ArrayList<Arista>> getListaAdyacencia() {
        return listaAdyacencia;
    }

}