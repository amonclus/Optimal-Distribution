package FONTS.Domini.ControladorsDomini;

import FONTS.Domini.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de la clase Estanteria
 */
public class CtrlEstanteria {
    /** Estantería única */
    private final Estanteria estanteria;

    /**
     * Constructora de la clase
     */
    public CtrlEstanteria() {
        this.estanteria = Estanteria.getInstance();
    }

    /**
     * Añade el número de estantes especificado a la estantería
     * @param numeroEstantes Número de estantes a añadir
     */
    public void addEstante(int numeroEstantes) {
        estanteria.addEstante(numeroEstantes);
    }

    /**
     * Elimina el número de estantes especificado de la estantería
     * @param numeroEstantes Número de estantes a eliminar
     */
    public void removeEstante(int numeroEstantes) {
        estanteria.deleteEstante(numeroEstantes);
    }

    // Métodos para manipular las distribuciones

    /**
     * Crea una nueva distribución
     * @param id ID de la distribución
     * @param option Algoritmo a utilizar
     * @param productos Productos a distribuir
     * @param relaciones Relaciones entre los productos
     * @throws IllegalArgumentException Si ya existe una distribución con el mismo ID
     * @throws IllegalArgumentException Si no hay al menos dos productos
     */
    public void createDistribution(String id, boolean option, ArrayList<Producto> productos, Relacion relaciones) {
        if (estanteria.getDistribucionesInactivas().containsKey(id)) {
            throw new IllegalArgumentException("Una distribución con el mismo ID ya existe.");
        }
        if (productos.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos dos productos para crear una distribución.");
        }
        Algoritmo algoritmo = option ? new AlgoritmoAproximacion() : new AlgoritmoFuerzaBruta();
        estanteria.calcularNuevaDistribucion(id, relaciones, productos, algoritmo, option);
    }

    /**
     * Obtiene todas las distribuciones.
     * @return Mapa con las distribuciones. La clave es el ID y el valor es la representación de la distribución
     */
    public Map<String, String> getAllDistributions() {
        Map<String, String> distributions = new HashMap<>();
        estanteria.getDistribucionesInactivas().forEach((id, distribucion) -> distributions.put(id, estanteria.displayDistribucion(id)));
        return distributions;
    }

    /**
     * Elimina una distribución
     * @param id ID de la distribución
     * @throws IllegalArgumentException Si no existe una distribución con el mismo ID
     */
    public void deleteDistribution(String id) {
        if (estanteria.getDistribucionesInactivas().containsKey(id)) {
            estanteria.eliminarDistribucion(id);
        } else {
            throw new IllegalArgumentException("Una distribución con el mismo ID no existe.");
        }
    }

    /**
     * Activa una distribución
     * @param id ID de la distribución
     * @throws IllegalArgumentException Si no existe una distribución con el mismo ID
     */
    public void activateDistribution(String id) {
        if (estanteria.getDistribucionesInactivas().containsKey(id)) {
            estanteria.activarDistribucion(id);
        } else {
            throw new IllegalArgumentException("No se encontró una distribución con el ID: " + id);
        }
    }

    /**
     * Devuelve la representación de una distribución
     * @param id ID de la distribución
     * @return String con la distribución
     */
    public String displayDistribution(String id) {
        return estanteria.displayDistribucion(id);
    }

    /**
     * Intercambia dos productos de posición
     * @param product1 Producto 1 a mover
     * @param product2 Producto 2 a mover
     */
    public void modifyDistribution(Producto product1, Producto product2) {

        if (product1 != null && product2 != null) {
            estanteria.getDistribucionActiva().modificarDistribucion(product1, product2);
        }
    }

    /**
     * Comprueba si hay una distribución activa
     * @return True si hay una distribución activa, false en caso contrario
     */
    public boolean isActiveDistribution() {
        return estanteria.getDistribucionActiva() != null;
    }

    // Getters y setters

    //Retorna la distribución con id = id
    public Distribucion getDistribucionById(String id) {
        if (estanteria.getDistribucionActiva() != null && estanteria.getDistribucionActiva().getId().equals(id)) {
            return estanteria.getDistribucionActiva();
        }
        return estanteria.getDistribucionesInactivas().get(id);
    }

    public boolean EsApxorimado(String id) {return estanteria.getDistribucionesInactivas().get(id).getAproximado(); }


    public void setDistribuciones(Map<String, Distribucion> distribuciones) {estanteria.setDistribucionesInactivas(distribuciones);}

    public Distribucion getDistribucionActiva() {
        return estanteria.getDistribucionActiva();
    }

    public int getNumeroEstantes(){
        return Estanteria.getNumeroEstantes();
    }

    public void setNumeroEstantes(Integer n){
        Estanteria.setNumeroEstantes(n);
    }

    public Map<String, Distribucion> getDistribuciones() {
        return estanteria.getDistribucionesInactivas();
    }

}
