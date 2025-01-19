package FONTS.Domini.ControladorsDomini;
import FONTS.Domini.Distribucion;
import FONTS.Domini.Producto;
import java.util.Map;

/**
 * Controlador de restricciones
 */
public class CtrlRestriccion {

    /**
     * Constructora vacía de la clase
     */
    public CtrlRestriccion() {

    }

    /**
     * Añade una restricción a la distribución recibida
     * @param producto Producto a restringir
     * @param pos   Posición en la que restringir el producto
     * @param id Distribución a la que añadir la restricción
     */
    public void addRestriction(Producto producto, int pos, Distribucion id) {
        if (producto != null) {
            id.addRestriccion(producto, pos);
        }
    }

    /**
     * Elimina una restricción de la distribución recibida
     * @param producto Producto a eliminar la restricción
     * @param distribucion Distribución a la que eliminar la restricción
     */
    public void eliminateRestriction(Producto producto, Distribucion distribucion) {
        if (producto != null) {
            distribucion.eliminarRestriccion(producto.getName());
        }
    }

    /**
     * Modifica la restricción de un producto en la distribución recibida
     * @param producto Producto a modificar la restricción
     * @param newPos Nueva posición de la restricción
     * @param distribucionActiva Distribución a la que modificar la restricción
     */
    public void modifyRestriction(Producto producto, int newPos, Distribucion distribucionActiva) {
        if (producto != null) {
            distribucionActiva.eliminarRestriccion(producto.getName());
            distribucionActiva.addRestriccion(producto, newPos);
        }
    }

    //Getters y setters
    
    public Map<Producto, Integer> getRestrictions(Distribucion distribucionActiva) {
        return distribucionActiva.getRestricciones();
    }
}

