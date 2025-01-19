package FONTS.Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//CLASE SINGLETON

/**
 * Clase que representa el inventario del supermercado. Contiene una lista de productos y un mapa de productos por ID.
 */
public class Inventario {
    /** Instancia de la clase Inventario */
    private static final Inventario instance = new Inventario();
    /** Lista de productos del inventario */
    private ArrayList<Producto> productos;
    /** Mapa de productos por ID para verificar que no existan productos con el mismo ID*/
    private Map<String, Producto> productosPorId;           // Mapa para verificar que no existan productos con el mismo nombre (ID)

    /**
     *  Constructor vacío de la clase.
     */
    private Inventario() {
        this.productos = new ArrayList<>();
        this.productosPorId = new HashMap<>();
    }

    /**
     * Añade un producto al inventario
     * @param p Producto a añadir
     */
    public void agregarProducto(Producto p) {
        if (existeProducto(p.getName())) {
            return;  // El producto no se agrega si el ID ya existe
        }
        productos.add(p);                  // Agregar el producto a la lista
        productosPorId.put(p.getName(), p); // Agregar el producto al mapa
    }

    /**
     * Elimina un producto del inventario y de las distribuciones
     * @param productName Nombre del producto a eliminar
     */
    public void eliminarProducto(String productName) {
        Producto productToRemove = productosPorId.get(productName);
        if (productToRemove == null) {
            return;
        }

        // Eliminar el producto de las distribuciones en la estantería
        Estanteria.getInstance().eliminarProductoDeDistribuciones(productToRemove);

        // Eliminar el producto del inventario
        productos.removeIf(producto -> producto.getName().equals(productName));
        productosPorId.remove(productName);

    }

    /**
     * Comprueba si existe un producto en el inventario
     * @param nombre Nombre del producto a comprobar
     * @return True si el producto existe, false si no
     */
    public boolean existeProducto(String nombre) {
        return productosPorId.containsKey(nombre);
    }

    //Getters y setters

    public static Inventario getInstance() {
        return instance;
    }
    
    public Map<String, Producto> getProductosPorId() {
        return productosPorId;
    }

    public ArrayList<Producto> getProductos() { return productos; }

    public Object[][] getInventarioData() {
        Object[][] data = new Object[productosPorId.size()][3];
        int i = 0;
        for (Map.Entry<String, Producto> entry : productosPorId.entrySet()) {
            data[i][0] = entry.getValue().getName();
            data[i][1] = entry.getValue().getType();
            data[i][2] = String.join(", ", entry.getValue().getAttributes());
            i++;
        }
        return data;
    }

    public void setProductos(ArrayList<Producto> prods){this.productos = prods;}

    public void setProductosPorId(Map<String, Producto> prods){
        this.productosPorId = prods;
    }

}

