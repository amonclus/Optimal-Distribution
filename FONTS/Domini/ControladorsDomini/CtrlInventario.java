package FONTS.Domini.ControladorsDomini;

import FONTS.Domini.Inventario;
import FONTS.Domini.Producto;
import FONTS.Domini.Relacion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Controlador de la clase Inventario
 */
public class CtrlInventario {
    /** Inventario de productos */
    private final Inventario inventario;
    /** Relaciones entre productos */
    private final Relacion relaciones;

    /**
     * Constructora vacía de la clase
     */
    public CtrlInventario() {
        this.relaciones = Relacion.getInstance();
        this.inventario = Inventario.getInstance();
    }
    /**
     * Establece el inventario con los productos y relaciones de los parámetros
     * @param productos Lista de productos
     * @param productosPorId Mapa de productos por ID
     * @param relaciones Mapa de relaciones entre productos
     */
    public void setInventario(ArrayList<Producto> productos, Map<String, Producto> productosPorId, Map<String, Map<String, Double>> relaciones) {
        inventario.setProductos(productos);
        inventario.setProductosPorId(productosPorId);
        this.relaciones.setRelaciones(relaciones);
    }

    /**
     * Crea un nuevo producto, lo añade al inventario y a las relaciones
     * @param name Nombre del producto
     * @param type Tipo del producto
     * @param attributesArr Atributos del producto
     * @param relacionesMap Relaciones del producto con otros productos
     */
    public void createProducto(String name, String type, String[] attributesArr, Map<String, Double> relacionesMap) {
        if (inventario.getProductosPorId().containsKey(name)) {
            return;  // No añadir el producto si ya existe
        }

        ArrayList<String> attributes = new ArrayList<>(Arrays.asList(attributesArr));
        Producto producto = new Producto(name, type, attributes);
        inventario.agregarProducto(producto);

        // Añadir relaciones para el nuevo producto con cada producto existente
        relacionesMap.forEach((key, value) -> relaciones.addRelation(name, key, value));
    }

    /**
     * Actualiza la relación entre dos productos
     * @param product1 Producto 1
     * @param product2 Producto 2
     * @param similarity Nuevo grado de similitud entre el Producto 1 y el Producto 2
     */
    public void updateRelaciones(String product1, String product2, double similarity) {
        if (product1.equals(product2)) {
            return;  // Terminar la ejecución si los productos son iguales
        }

        if (inventario.getProductosPorId().containsKey(product1) && inventario.getProductosPorId().containsKey(product2)) {
            relaciones.addRelation(product1, product2, similarity);
        }
    }

    /**
     * Elimina un producto del inventario y de las relaciones
     * @param name Nombre del producto a eliminar
     */
    public void removeProducto(String name) {
        inventario.eliminarProducto(name);
        relaciones.removeProduct(name);
    }

    // Getters y setters

    public Double getRelation(String product1, String product2) {
        return relaciones.getSimilarity(product1, product2);
    }
    public ArrayList<Producto>  getProductosInventario() {
        return inventario.getProductos();
    }

    public Relacion getRelaciones() {
        return relaciones;
    }

    public Producto getProducto(String productName) {
        return inventario.getProductosPorId().get(productName);
    }

    public Producto getProductByName(String name) {
        Producto producto = inventario.getProductosPorId().get(name);
        return producto;
    }

    public boolean isValidPosition(int pos) {
        return !(pos < 0 || pos >= inventario.getProductos().size());
    }

    public String getRelationshipsString(String product) {
        return relaciones.toString(product);
    }

    public Map<String, Producto> getProductosPorId() {
        return inventario.getProductosPorId();
    }
    public Object[][] getInventarioData() {
        return inventario.getInventarioData();
    }

}
