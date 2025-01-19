package FONTS.Domini;

import java.util.*;

/**
 * Clase que representa una distribución de productos.
 * Cada distribución puede tener una serie de restricciones que obligan a mantener
 * ciertos productos en posiciones concretas.
 * La clase también permite calcular una nueva distribución a partir de una
 * serie de relaciones entre productos y un algoritmo concreto.
 */
public class Distribucion {
    /** El identificador único de la distribución */
    private String id;
    /** El mapa de las restricciones que cada una obliga a mantener un producto en una posición */
    private Map<Producto, Integer> restricciones;   //Obliga a mantener el producto a la posición dada
    /** El mapa de las posiciones de los productos en la distribución */
    private Map<Integer, Producto> posiciones;      //Mapea posiciones a productos
    /** Booleano que indica si la distribución se calculó con el algoritmo de aproximación o no */
    private boolean AlgoritmoAproximado;            //Si es verdadero, se usó el algoritmo de aproximación, en caso contrario, el de fuerza burta.

    /**
     * Constructor de la clase Distribucion. Inicializa las posiciones de la distribución.
     * @param posiciones Mapa que mapea posiciones a productos.
     */
    public Distribucion (Map<Integer, Producto> posiciones) {
        this.id = "";
        this.posiciones = posiciones;
        this.restricciones = new HashMap<>();
    }

    /**
     * Añade una restricción a la distribución.
     * @param prod Producto a restringir.
     * @param pos Posición en la que se debe mantener el producto.
     * @throws IllegalArgumentException Si ya existe una restricción para el producto o la posición.
     * 
     */
    public void addRestriccion(Producto prod, Integer pos) {
        if (restricciones.containsKey(prod)) {
            throw new IllegalArgumentException("Error: Ya existe una restricción para el producto " + prod.getName());
        }
        if (restricciones.containsValue(pos)) {
            throw new IllegalArgumentException("Error: Ya existe una restricción para la posición " + pos);
        }
        if (posiciones.containsKey(pos)) {
            restricciones.put(prod, pos);
            Producto productoEnPosicion = posiciones.get(pos);                 // Comprobamos si ya hay un producto en la posición de la restricción

            if (productoEnPosicion != null) {
                swapProductos(prod, productoEnPosicion);                       // Si ya hay un producto en la posición, intercambiamos los productos
            } else {
                posiciones.put(pos, prod);                                     // Comprobamos si ya hay un producto en la posición de la restricción
            }
        } 
    }

    /**
     * Elimina una restricción de la distribución.
     * @param productName Nombre del producto a eliminar.
     */
    public void eliminarRestriccion(String productName) {
        restricciones.entrySet().removeIf(entry -> entry.getKey().getName().equals(productName));
    }

    /**
     * Calcula una nueva distribución a partir de las relaciones entre productos y un algoritmo concreto.
     * @param relaciones Relaciones entre productos.
     * @param productos Lista de productos.
     * @param algoritmo Algoritmo a utilizar para calcular la nueva distribución.
     * @param option Opción para elegir el algoritmo aproximado o no.
     * @return Nueva distribución calculada.
     */
    public Distribucion calcularDistribucion(Relacion relaciones, ArrayList<Producto> productos, Algoritmo algoritmo, boolean option) {
        Map<Integer, Producto> res =  algoritmo.ejecutarAlgoritmo(relaciones, productos);
        return new Distribucion(res);
    }

    /**
     * Modifica la posición de dos productos en la distribución intercambiandolos.
     * @param prod1 Primer producto a intercambiar.
     * @param prod2 Segundo producto a intercambiar.
     */
    public void modificarDistribucion(Producto prod1, Producto prod2) {
        boolean hasRestriction = restricciones.keySet().stream()
                .anyMatch(p -> p.getName().equals(prod1.getName()) || p.getName().equals(prod2.getName()));

        if (!hasRestriction) {
            swapProductos(prod1, prod2);
        } 
    }

    /**
     * Intercambia dos productos en la distribución.
     * @param prod1 Primer producto a intercambiar.
     * @param prod2 Segundo producto a intercambiar.
     */
    public void swapProductos(Producto prod1, Producto prod2) {
        if (Objects.equals(prod1.getName(), prod2.getName())) {
            return;
        }
        boolean prod1Exists = posiciones.values().stream().anyMatch(p -> p.getName().equals(prod1.getName()));
        boolean prod2Exists = posiciones.values().stream().anyMatch(p -> p.getName().equals(prod2.getName()));

        if (prod1Exists && prod2Exists) {           //Si ambos productos existen
            Integer pos1 = null;
            Integer pos2 = null;

            // Buscar las posiciones de los productos
            for (Map.Entry<Integer, Producto> entry : posiciones.entrySet()) {
                if (entry.getValue().getName().equals(prod1.getName())) {
                    pos1 = entry.getKey();
                } else if (entry.getValue().getName().equals(prod2.getName())) {
                    pos2 = entry.getKey();
                }
            }

            // Realizar el intercambio si ambas posiciones son válidas
            if (pos1 != null && pos2 != null) {
                posiciones.put(pos1, prod2);
                posiciones.put(pos2, prod1);
            } 
        } 
    }

    /**
     * Devuelve el string con la distribución de productos en la estantería según el número de estantes.
     * @param numEstantes Número de estantes en los que se mostrará la distribución.
     * @return String con la distribución de productos.
     */
    public String displayDistribucion(int numEstantes) {
        StringBuilder sb = new StringBuilder();  // Usamos StringBuilder para construir la cadena

        if (posiciones == null || posiciones.isEmpty()) {
            sb.append("No hay productos en la distribución.");
            return sb.toString();
        }

        int numProductos = posiciones.size();

        // Crear una lista de estantes, donde cada estante es una lista de productos
        List<List<Producto>> estantes = new ArrayList<>();
        for (int i = 0; i < numEstantes; i++) {
            estantes.add(new ArrayList<>());
        }

        if (numEstantes >= numProductos) {
            // Caso 1: Más estantes que productos
            int index = 0;
            for (Map.Entry<Integer, Producto> entry : posiciones.entrySet()) {
                if (index < numEstantes) {
                    estantes.get(index).add(entry.getValue());
                    index++;
                }
            }
        } else {
            // Caso 2: Menos estantes que productos
            int productosPorEstante = numProductos / numEstantes;
            int productosRestantes = numProductos % numEstantes;

            int posicionActual = 0;
            for (int i = 0; i < numEstantes; i++) {
                int productosEnEsteEstante = productosPorEstante + (i < productosRestantes ? 1 : 0);

                if (i % 2 == 0) {
                    // Caso par: Rellenar en orden normal
                    for (int j = 0; j < productosEnEsteEstante; j++) {
                        estantes.get(i).add(posiciones.get(posicionActual));
                        posicionActual++;
                    }
                } else {
                    // Caso impar: Rellenar en orden inverso
                    posicionActual += productosEnEsteEstante - 1;
                    for (int j = 0; j < productosEnEsteEstante; j++) {
                        estantes.get(i).add(posiciones.get(posicionActual));
                        posicionActual--;
                    }
                    posicionActual += productosEnEsteEstante + 1;
                }

            }
        }

        // Construir la cadena con la distribución
        for (List<Producto> estante : estantes) {
            for (Producto producto : estante) {
                sb.append(producto.getName()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Elimina un producto de la distribución.
     * @param productName Nombre del producto a eliminar.
     */
    public void eliminarProducto(String productName) {
        // Encontrar el producto segun el nombre
        Producto producto = null;
        for (Producto prod : posiciones.values()) {
            if (prod.getName().equals(productName)) {
                producto = prod;
                break;
            }
        }

        if (producto == null) {
            return;
        }

        // Encontrar y eliminar el producto de las posiciones
        Integer posToRemove = null;
        for (Map.Entry<Integer, Producto> entry : posiciones.entrySet()) {
            if (entry.getValue().equals(producto)) {
                posToRemove = entry.getKey();
                break;
            }
        }

        if (posToRemove != null) {
            posiciones.remove(posToRemove);

            // Ajustar las posiciones restantes
            Map<Integer, Producto> nuevasPosiciones = new HashMap<>();
            for (Map.Entry<Integer, Producto> entry : posiciones.entrySet()) {
                int nuevaPos = entry.getKey() > posToRemove ? entry.getKey() - 1 : entry.getKey();
                nuevasPosiciones.put(nuevaPos, entry.getValue());
            }
            posiciones = nuevasPosiciones;

        }

        producto = null;
        for (Producto prod : restricciones.keySet()) {
            if (prod.getName().equals(productName)) {
                producto = prod;
                break;
            }
        }
        // Eliminar el producto de las restricciones
        if (restricciones.containsKey(producto)) {
            restricciones.remove(producto);
        }

    }

    //Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(boolean aproximado) { this.AlgoritmoAproximado = aproximado;}

    public boolean getAproximado(){return this.AlgoritmoAproximado; }

    public Map<Integer, Producto> getPosiciones() {
        return posiciones;
    }

    public Map<Producto, Integer> getRestricciones() {
        return restricciones;
    }

    public Map<Integer, Producto> getProductos() {return posiciones;}

    public void setRestricciones(Map<Producto, Integer> restricciones) {this.restricciones = restricciones;}
}



