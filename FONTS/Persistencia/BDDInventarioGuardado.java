package FONTS.Persistencia;

import FONTS.Domini.Producto;
import java.io.*;
import java.util.*;

/**
 * Clase que se encarga de la persistencia del inventario guardado. Este representa el inventario con las modificaciones
 * del usuario a lo largo de las anteriores sesiones.
 */
public class BDDInventarioGuardado {
    /** Ruta del inventario */
    private static final String INVENTORY_FILE = "../DATA/inventory.txt";
    /** Ruta de las relaciones */
    private static final String RELATIONS_FILE = "../DATA/relations.txt";

    /**
     * Constructor vacío de la clase
     */
    public BDDInventarioGuardado() {
        // Constructor vacío, inicialización explícita necesaria
    }

    /**
     * Guarda el inventario de la sesión actual.
     * @param inventario Inventario de la sesión actual.
     * @throws RuntimeException si hay un error al guardar el inventario.
     */
    public void guardarInventarioSesion(List<Producto> inventario) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Producto producto : inventario) {
                bw.write(producto.getName() + "," + producto.getType() + "," + String.join(",", producto.getAttributes()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el inventario: " + e.getMessage());
        }
    }

    /**
     * Lee los ficheros de texto del inventario guardado y devuelve una lista de productos.
     * @return Lista de productos del inventario guardado.
     */
    public List<Producto> obtenerInventario() {
        List<Producto> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    String type = parts[1].trim();
                    List<String> attributes = Arrays.asList(parts[2].trim().split("\s*,\s*"));
                    productos.add(new Producto(name, type, new ArrayList<>(attributes)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el inventario guardado: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Guarda las relaciones entre productos de la sesión actual.
     * @param relaciones Relaciones entre productos de la sesión actual.
     * @throws RuntimeException si hay un error al guardar las relaciones.
     */
    public void guardarRelaciones(Map<String, Map<String, Double>> relaciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RELATIONS_FILE))) {
            for (Map.Entry<String, Map<String, Double>> entry : relaciones.entrySet()) {
                String product1 = entry.getKey();
                for (Map.Entry<String, Double> subEntry : entry.getValue().entrySet()) {
                    String product2 = subEntry.getKey();
                    Double similarity = subEntry.getValue();
                    bw.write(product1 + "," + product2 + "," + String.format("%.3f", similarity));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las relaciones: " + e.getMessage());
        }
    }

    /**
     * Lee los ficheros de texto de las relaciones guardadas y devuelve un mapa de relaciones entre productos.
     * @return Mapa de relaciones entre productos.
     * @throws RuntimeException si hay un error al cargar las relaciones guardadas.
     */
    public Map<String, Map<String, Double>> getRelaciones() {
        Map<String, Map<String, Double>> relaciones = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RELATIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String product1 = parts[0].trim();
                    String product2 = parts[1].trim();
                    Double similarity = Double.parseDouble(parts[2].trim());
                    relaciones.putIfAbsent(product1, new HashMap<>());
                    relaciones.get(product1).put(product2, similarity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar las relaciones guardadas: " + e.getMessage());
        }
        return relaciones;
    }


    /**
     * Devuelve un mapa de productos por ID.
     * @return Mapa de productos por ID.
     */
    public Map<String, Producto> getProductosporId() {
        Map<String, Producto> productosPorId = new HashMap<>();
        List<Producto> productos = obtenerInventario();
        for (Producto producto : productos) {
            productosPorId.put(producto.getName(), producto);
        }
        return productosPorId;
    }

    /**
     * Elimina un producto del inventario guardado.
     * @param productName Nombre del producto a eliminar.
     */
    public void eliminarProductoDelInventario(String productName) {
        List<Producto> productos = obtenerInventario();
        productos.removeIf(producto -> producto.getName().equals(productName));
        guardarInventarioSesion(productos);
    }
}