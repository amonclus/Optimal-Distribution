package FONTS.Domini;
import java.util.HashMap;
import java.util.Map;


//CLASE SINGLETON

/**
 * Clase que representa las relaciones entre productos. Cada relación tiene dos productos y un valor de similitud.
 */
public class Relacion {
    /** Instancia única de la clase Relacion */
    private static final Relacion instance = new Relacion();
    /** Mapa de relaciones entre productos */
    private Map<String, Map<String, Double>> Relaciones;

    /**
     * Constructor de la clase
     */
    private Relacion() {
        Relaciones = new HashMap<>();
    }

    /**
     * Añade una relación entre dos productos con un valor de similitud
     * @param product1 Producto 1
     * @param product2  Producto 2
     * @param similarity Valor de similitud entre los productos
     * @throws IllegalArgumentException Si los productos son iguales
     */
    public void addRelation(String product1, String product2, Double similarity) {
        if (product1.equals(product2)) {
            throw new IllegalArgumentException("A product cannot be related to itself.");
        }
        Relaciones.putIfAbsent(product1, new HashMap<>());
        Relaciones.putIfAbsent(product2, new HashMap<>());
        Relaciones.get(product1).put(product2, similarity);
        Relaciones.get(product2).put(product1, similarity);
    }

    /**
     * Representación en String de las relaciones de un producto
     * @param product Producto del que se quieren obtener las relaciones
     * @return String con las relaciones del producto
     */
    public String toString(String product) {
        if (!Relaciones.containsKey(product)) {
            return "0 relaciones encontradas para:  " + product;
        }
        StringBuilder relations = new StringBuilder("Relaciones para " + product + ":\n");
        for (Map.Entry<String, Double> entry : Relaciones.get(product).entrySet()) {
            relations.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return relations.toString();
    }

    /**
     * Elimina un producto de las relaciones
     * @param producto Producto a eliminar
     */
    public void removeProduct(String producto) {
        // Eliminar el producto de los mapas anidados
        for (Map.Entry<String, Map<String, Double>> entry : Relaciones.entrySet()) {
            entry.getValue().remove(producto);
        }

        // Eliminar el producto como clave principal
        Relaciones.remove(producto);
    }

    //Getters y Setters

    public static Relacion getInstance() {
        return instance;
    }

    public Double getSimilarity(String product1, String product2) {
        if (Relaciones.containsKey(product1) && Relaciones.get(product1).containsKey(product2)) {
            return Relaciones.get(product1).get(product2);
        }
        return null;
    }

    public Map<String, Map<String, Double>>  getRelaciones() {
        return this.Relaciones;
    }

    public void setRelaciones(Map<String, Map<String, Double>> relaciones){
        this.Relaciones = relaciones;
    }

}


