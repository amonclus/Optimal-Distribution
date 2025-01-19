package FONTS.Persistencia.CtrlPersistencia;

import FONTS.Domini.Distribucion;
import FONTS.Domini.Producto;
import FONTS.Persistencia.*;
import FONTS.Utils.Pair;
import java.util.*;

/**
 *  Controlador de la capa de persistencia
 */
public class CtrlPersistencia {
    /** Base de datos de inventario guardado */
    private final BDDInventarioGuardado dbInventarioGuardado = new BDDInventarioGuardado();
    /** Base de datos de inventario inicial */
    private final BDDInventarioInicial dbInventarioInicial = new BDDInventarioInicial();
    /** Base de datos de distribuciones */
    private final BDDDistribucion dbDistribucion = new BDDDistribucion();

    /**
     * Inicializa la sesión con la base de datos
     * @param seleccion 0 si se quiere cargar la base de datos base, 1 si se quiere cargar la sesión guardada
     */
    public void inicializarSesion(int seleccion) {
        
        // Actuar según la selección
        if (seleccion == 0) { // Base seleccionado
            dbInventarioInicial.sobrescribirInventarioConBase();
            dbInventarioInicial.sobrescribirRelacionesConBase();
            dbInventarioInicial.sobrescribirEstanteriaConBase();
            dbInventarioInicial.sobrescribirDistribucionesConBase();
        }
        
    }

    /**
     * Guarda la sesión actual en la base de datos Guardado
     * @param inventario Lista de productos en el inventario
     * @param productosPorId Mapa de productos por ID
     * @param relaciones Mapa de relaciones entre productos
     * @param distribuciones Mapa de distribuciones
     */
    public void guardarSesion(List<Producto> inventario, Map<String, Producto> productosPorId, Map<String, Map<String, Double>> relaciones, Map<String, Distribucion> distribuciones) {
        dbInventarioGuardado.guardarInventarioSesion(inventario);
        dbInventarioGuardado.guardarRelaciones(relaciones);
        dbDistribucion.guardarDistribuciones(distribuciones);
    }

    /**
     * Obtiene los productos por ID del inventario guardado
     * @return Mapa de productos por ID
     */
    public Map<String, Producto> obtenerProductosPorIDInventarioGuardado() {
        return dbInventarioGuardado.getProductosporId();
    }

    /**
     * Obtiene las relaciones del inventario guardado
     * @return Mapa de relaciones entre productos
     */
    public Map<String, Map<String, Double>> obtenerRelacionesInventariouardado() {
        return dbInventarioGuardado.getRelaciones();
    }

    /**
     * Obtiene los productos del inventario guardado
     * @return Lista de productos en el inventario
     */
    public List<Producto> obtenerProductosInventarioGuardado() {
        return dbInventarioGuardado.obtenerInventario();
    }

    /**
     * Obtiene las distribuciones guardadas
     * @return Mapa de distribuciones
     */
    public Map<String, Distribucion> obtenerDistribucionesGuardadas() {
        return dbDistribucion.obtenerDistribuciones(obtenerProductosInventarioGuardado());
    }

    /**
     * Obtiene la información de la estantería guardada
     * @return Par con el ID de la distribución activa y el número de estantes
     */
    public Pair<String, Integer> obtenerInfoEstanteriaGuardada() {
        return dbDistribucion.obtenerInfoEstanteriaGuardada();
    }

    /**
     * Guarda la información de la estantería
     * @param distribucionActiva Distribución activa
     * @param numeroEstantes Número de estantes
     */
    public void guardarInfoEstanteria(Distribucion distribucionActiva, int numeroEstantes) {
        dbDistribucion.guardarInfoEstanteria(distribucionActiva, numeroEstantes);
    }

    /**
     * Elimina un producto del inventario guardado
     * @param productName Nombre del producto a eliminar
     */
    public void eliminarProductoDelInventario(String productName) {
        dbInventarioGuardado.eliminarProductoDelInventario(productName);
    }
}