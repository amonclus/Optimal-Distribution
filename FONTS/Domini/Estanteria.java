package FONTS.Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//CLASE SINGLETON

/**
 * Clase que representa la estantería del supermercado. Contiene un mapa de distribuciones inactivas y una distribución activa.
 */
public class Estanteria {
    /** Instancia de la clase Estanteria */
    private static final Estanteria instance = new Estanteria();
    /** Número de estantes de la estantería */
    private static int numeroEstantes = 1;

    /**  Mapa de distribuciones inactivas: key = ID de Distribución, value = Distribución*/
    private Map<String, Distribucion> DistribucionesInactivas;
    /** Distribución activa en la estantería*/
    private Distribucion DistribucionActiva;

    /**
     * Constructor de la clase Estanteria. Privado porque la clase es Singleton
     */
    private Estanteria() {
        this.DistribucionesInactivas = new HashMap<>();
        this.DistribucionActiva = null;
    }

    /**
     * Añade estantes a la estantería
     * @param numeroEstantes Número de estantes a añadir
     */
    public void addEstante(int numeroEstantes) {
        setNumeroEstantes(getNumeroEstantes() + numeroEstantes);
    }

    /**
     * Elimina estantes de la estantería
     * @param n Número de estantes a eliminar
     */
    public void deleteEstante(int n) {
        if (n < numeroEstantes) setNumeroEstantes(getNumeroEstantes() - n);
    }

    /**
     * Activa la distribución con id pasado como parámetro
     * @param id ID de la distribución a activar
     */
    public void activarDistribucion(String id) {
        // Busca la distribución en el mapa de distribuciones inactivas
        Distribucion distribucion = DistribucionesInactivas.get(id);
        if (distribucion != null) {
            this.DistribucionActiva = distribucion;
        } 
    }

    /**
     * Elimina la distribución con id pasado como parámetro
     * @param id ID de la distribución a eliminar
     */
    public void eliminarDistribucion(String id) {
        if (DistribucionesInactivas.containsKey(id)) {
            //En caso de que esté activa se desactiva antes de eliminar la distribucion
            if (DistribucionActiva != null && DistribucionActiva.getId().equals(id)) {
                this.DistribucionActiva = null;
            }
            DistribucionesInactivas.remove(id);
        }
    }

    /**
     * Añade una distribución al mapa de distribuciones inactivas
     * @param d Distribución a añadir
     */
    public void addDistribucion(Distribucion d) {
        String id = d.getId();
        if (!DistribucionesInactivas.containsKey(id)) {
            DistribucionesInactivas.put(id, d);
        }
    }

    /**
     * Devuelve la distribución con el ID pasado como parámetro.  
     * @param id ID de la distribución a buscar
     * @return Distribución activa
     * @throws IllegalArgumentException si no se encuentra la distribución
     */
    public String displayDistribucion(String id) {
        // Primero verificamos si la distribución activa tiene el ID que se pasa como parámetro
        if (DistribucionActiva != null && DistribucionActiva.getId().equals(id)) {
            // Si es la distribución activa, llamamos a displayDistribucion directamente sobre ella
            return DistribucionActiva.displayDistribucion(numeroEstantes);
        }

        // Si no es la activa, buscamos la distribución en el mapa de distribuciones inactivas
        if (DistribucionesInactivas.containsKey(id)) {
            // Llamamos a displayDistribucion de la clase Distribucion y retornamos el resultado
            return DistribucionesInactivas.get(id).displayDistribucion(numeroEstantes);
        } else {
            // Si no se encuentra la distribución, lanzamos una excepción
            throw new IllegalArgumentException("No se encontró una distribución con el ID: " + id);
        }
    }

    /**
     * Calcula una nueva distribución y la añade al mapa de distribuciones inactivas
     * @param id ID de la nueva distribución
     * @param relaciones Relaciones entre productos
     * @param productos Lista de productos
     * @param algoritmo Algoritmo a utilizar
     * @param option Opción de algoritmo
     */
    public void calcularNuevaDistribucion(String id, Relacion relaciones, ArrayList<Producto> productos, Algoritmo algoritmo, boolean option) {
        Distribucion nuevaDistribucion = new Distribucion(new HashMap<>());
        nuevaDistribucion = nuevaDistribucion.calcularDistribucion(relaciones, productos, algoritmo, option);
        nuevaDistribucion.setId(id);
        nuevaDistribucion.setType(option);
        addDistribucion(nuevaDistribucion);
    }

    /**
     * Elimina un producto de todas las distribuciones y restricciones
     * @param producto Producto a eliminar
     */
    public void eliminarProductoDeDistribuciones(Producto producto) {

        // Eliminar el producto de todas las distribuciones inactivas
        for (Distribucion distribucion : DistribucionesInactivas.values()) {
            distribucion.eliminarProducto(producto.getName());
            if (distribucion.equals(DistribucionActiva)) {
                DistribucionActiva = distribucion;
            }
        }
    }

    //Getters y setters
    public Map<String, Distribucion> getDistribucionesInactivas() {
        return DistribucionesInactivas;
    }

    public static Estanteria getInstance() {
        return instance;
    }

    public static int getNumeroEstantes() {
        return numeroEstantes;
    }

    public static void setNumeroEstantes(int numeroEstantes) {
        Estanteria.numeroEstantes = numeroEstantes;
    }
    
    public void setDistribucionesInactivas(Map<String, Distribucion> distribucionesInactivas) {
        this.DistribucionesInactivas = distribucionesInactivas;
    }

    public void setDistribucionActiva(Distribucion distribucion) {
        this.DistribucionActiva = distribucion;
    }

    public boolean hayDistribucionActiva() {
        return this.DistribucionActiva != null;
    }

    public Distribucion getDistribucionActiva() {
        return DistribucionActiva;
    }

}
