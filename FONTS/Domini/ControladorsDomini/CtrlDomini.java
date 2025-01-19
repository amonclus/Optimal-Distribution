package FONTS.Domini.ControladorsDomini;

import FONTS.Domini.*;
import FONTS.Persistencia.CtrlPersistencia.CtrlPersistencia;
import FONTS.Presentacio.ControladorsPresentacio.CtrlPresentacio;
import FONTS.Utils.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.management.RuntimeErrorException;

//CONTROLADOR DE DOMINIO: Delega en los controladores de las clases del dominio para realizar las operaciones
/** 
 * Clase que representa el controlador de dominio. Se encarga de gestionar las operaciones de las clases del dominio delegando en los controladores de las clases.
 */
public class CtrlDomini {
    /** Controlador de estantería */
    private final CtrlEstanteria ctrlEstanteria;
    /** Controlador de inventario */
    private final CtrlInventario ctrlInventario;
    /** Controlador de restricciones */
    private final CtrlRestriccion ctrlRestriccion;
    /** Controlador de persistencia */
    private final CtrlPersistencia ctrlPersistencia;

    /** 
     * Constructor de la clase. Inicializa los controladores de las clases del dominio y el controlador de persistencia.
     * @param seleccion Número que indica el tipo de persistencia a utilizar
     */
    public CtrlDomini(int seleccion) {
        this.ctrlEstanteria = new CtrlEstanteria();
        this.ctrlInventario = new CtrlInventario();
        this.ctrlRestriccion = new CtrlRestriccion();
        this.ctrlPersistencia = new CtrlPersistencia();
        try {
            this.ctrlPersistencia.inicializarSesion(seleccion);   
        } catch (RuntimeErrorException e) {
            CtrlPresentacio.muestraErrorIO(e.getMessage());
        }
    }

    /** 
     * Carga la sesión anterior a partir de los archivos de persistencia.
     */
    public void loadSession() {
        ArrayList<Producto> inventario = (ArrayList<Producto>) ctrlPersistencia.obtenerProductosInventarioGuardado();
        Map<String, Producto> productosPorId = ctrlPersistencia.obtenerProductosPorIDInventarioGuardado();
        Map<String, Map<String, Double>> relaciones = ctrlPersistencia.obtenerRelacionesInventariouardado();
        Map<String, Distribucion> distribuciones = ctrlPersistencia.obtenerDistribucionesGuardadas();
        Pair<String, Integer> estanteriaInfo = ctrlPersistencia.obtenerInfoEstanteriaGuardada();
        
        ctrlInventario.setInventario(inventario, productosPorId, relaciones);
        ctrlEstanteria.setDistribuciones(distribuciones);
        ctrlEstanteria.setNumeroEstantes(estanteriaInfo.getValue());
        if (estanteriaInfo.getKey() != null && !estanteriaInfo.getKey().equals("null")) { // Verificar que el ID no sea null
            ctrlEstanteria.activateDistribution(estanteriaInfo.getKey());
        }
    }

    /** 
     * Guarda la sesión actual en los archivos de persistencia.
     */
    public void saveSession() {
        ArrayList<Producto> inventario = ctrlInventario.getProductosInventario();
        Map<String, Producto> productosPorId = ctrlInventario.getProductosPorId();
        Map<String, Map<String, Double>> relaciones = ctrlInventario.getRelaciones().getRelaciones();
        Map<String, Distribucion> distribuciones = ctrlEstanteria.getDistribuciones();
        Distribucion distribucionActiva = ctrlEstanteria.getDistribucionActiva();
        int numEstantes = ctrlEstanteria.getNumeroEstantes();

        try {
            ctrlPersistencia.guardarSesion(inventario, productosPorId, relaciones, distribuciones);
        } catch (RuntimeException e) {
            CtrlPresentacio.muestraErrorIO(e.getMessage());
        }
        
        ctrlPersistencia.guardarInfoEstanteria(distribucionActiva, numEstantes);
    }

    // Métodos de dominio
    /** 
     * Crea un producto a partir de los parámetros y lo añade al inventario usando el controlador de inventario.
     * @param name Nombre del producto
     * @param type Tipo del producto
     * @param attributesArr Lista de atributos del producto
     * @param relacionesMap Mapa de relaciones del producto
     */	
    public void addProduct(String name, String type, String[] attributesArr, Map<String, Double> relacionesMap) {
        // Limpiar espacios en blanco de los atributos
        for (int i = 0; i < attributesArr.length; i++) {
            attributesArr[i] = attributesArr[i].trim();
        }

        // Usar CtrlInventario para agregar el producto a las estructuras de datos de la sesión actual
        ctrlInventario.createProducto(name, type, attributesArr, relacionesMap);
    }

    /** 
     * Elimina un producto del inventario usando el controlador de inventario.
     * @param removeName Nombre del producto a eliminar
     */
    public void removeProduct(String removeName) {
        ctrlInventario.removeProducto(removeName);      // Usar CtrlInventario para eliminar el producto del inventario actual
        try {
            ctrlPersistencia.eliminarProductoDelInventario(removeName);     //Eliminamos el producto de los archivos de persistencia
        } catch (RuntimeException e) {
            CtrlPresentacio.muestraErrorIO(e.getMessage());
        }
       
        saveSession();
    }

    //Los siguientes métodos delegan en CtrlInventario y CtrlEstanteria para obtener información y modificar los productos y las distribuciones

    /** 
     * Activa una distribución a partir de su ID usando el controlador de estantería.
     * @param id ID de la distribución a activar
     */
    public void activateDistribution(String id) {
        ctrlEstanteria.activateDistribution(id);
    }

    /** 
     * Consigue los productos identificados por los parámetros usando el controlador de inventario y modifica la distribución activa con ellos usando el controlador de estantería.
     * @param product1 Nombre del primer producto
     * @param product2 Nombre del segundo producto
     */
    public void modificarDistribucion(String product1, String product2) {
        if (!ctrlEstanteria.isActiveDistribution()) return;
        ctrlEstanteria.modifyDistribution(ctrlInventario.getProducto(product1), ctrlInventario.getProducto(product2));
    }

    /** 
     * Elimina la distribución activa usando el controlador de estantería.
     * @param id ID de la distribución a eliminar
     */
    public void eliminateDistribution(String id) {
        ctrlEstanteria.deleteDistribution(id);
    }

    /** 
     * Crea una nueva distribución a partir de los parámetros y el inventario. La añade a la estantería usando el controlador de estantería.
     * @param option Opción de algoritmo
     * @param id ID de la distribución
     */
    public void createDistribution(boolean option, String id) {
        ArrayList<Producto> productos = ctrlInventario.getProductosInventario();
        Relacion relaciones = ctrlInventario.getRelaciones();
        ctrlEstanteria.createDistribution(id, option, productos, relaciones);
    }

    /** 
     * Devuelve el string con la información.
     * @param id ID de la distribución
     * @return String con la información de la distribución a mostrar
     */
    public String displayDistribution(String id) {
        String distributionDetails = ctrlEstanteria.displayDistribution(id);
        return distributionDetails;
    }

    /** 
     * Añade una restricción a la distribución usando el controlador de restricciones.
     * @param distributionId ID de la distribución
     * @param productName Nombre del producto
     * @param pos Posición del producto
     * @throws IllegalArgumentException Si el producto con el nombre no existe
     * @throws IllegalArgumentException Si la distribución con el ID no existe
     * @throws IllegalArgumentException Si la posición no es válida
     * @throws IllegalArgumentException Si la restricción ya existe o no se pudo añadir
     */
    public void addRestriction(String distributionId, String productName, int pos) {
        Producto producto = ctrlInventario.getProductByName(productName);
        if (producto == null) {
            throw new IllegalArgumentException("El producto con el nombre " + productName + " no existe.");
        }

        Distribucion distribucion = ctrlEstanteria.getDistribucionById(distributionId);
        if (distribucion == null) {
            throw new IllegalArgumentException("La distribución con el ID " + distributionId + " no existe.");
        }

        if (!ctrlInventario.isValidPosition(pos)) {
            throw new IllegalArgumentException("La posición " + pos + " no es válida.");
        }

        try {
            ctrlRestriccion.addRestriction(producto, pos, distribucion);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La restricción ya existe o no se pudo añadir: " + e.getMessage());
        }
    }

    /** 
     * Elimina una restricción de la distribución usando el controlador de restricciones.
     * @param name Nombre del producto
     * @param idDistribution ID de la distribución
     */
    public void eliminateRestriction(String name, String idDistribution) {
        Producto producto = ctrlInventario.getProductByName(name);
        Distribucion distribucion = ctrlEstanteria.getDistribucionById(idDistribution);
        ctrlRestriccion.eliminateRestriction(producto, distribucion);
    }

    /** 
     * Modifica una restricción de la distribución usando el controlador de restricciones.
     * @param name Nombre del producto
     * @param newPos Nueva posición
     */
    public void modifyRestriction(String name, int newPos) {
        if (!ctrlEstanteria.isActiveDistribution()) return;
        Producto producto = ctrlInventario.getProductByName(name);
        Distribucion distribucionActiva = ctrlEstanteria.getDistribucionActiva();
        if (ctrlInventario.isValidPosition(newPos)) {
            ctrlRestriccion.modifyRestriction(producto, newPos, distribucionActiva);
        }
    }

    /**
     * Comprueba si existe una restricción con alguno de los productos dados en la distribución.
     * @param product1 Producto 1
     * @param product2 Producto 2
     * @param distribucionActiva Distribución
     * @return True si existe una restricción con alguno de los productos dados en la distribución, False en caso contrario
     */
    public boolean restriccionescontains(String product1, String product2, Distribucion distribucionActiva) {
        if (distribucionActiva == null) {
            return false;
        }

        Map<Producto, Integer> restricciones = distribucionActiva.getRestricciones();
        for (Map.Entry<Producto, Integer> entry : restricciones.entrySet()) {
            Producto producto = entry.getKey();
            if (producto.getName().equals(product1) || producto.getName().equals(product2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Añade el número de estantes del parámetro a la estantería.
     * @param numeroEstantes
     */
    public void addShelf(int numeroEstantes) {
        ctrlEstanteria.addEstante(numeroEstantes);
    }

    /**
     * Elimina el número de estantes del parámetro de la estantería.
     * @param numeroEstantes
     */
    public void removeShelf(int numeroEstantes) {
        ctrlEstanteria.removeEstante(numeroEstantes);
    }

    // Getters y setters

    public boolean ExisteProducto(String id) { return ctrlInventario.getProductByName(id) != null; }

    public void updateRelationship(String product1, String product2, double similarity) {
        // Actualizar la relación en CtrlInventario
        ctrlInventario.updateRelaciones(product1, product2, similarity);
    }

    public Map<Integer, String> getDistribucionMap(String idDistribution) {
        // Obtener el mapa de productos
        Map<Integer, Producto> productos = ctrlEstanteria.getDistribucionById(idDistribution).getProductos();

        // Crear el mapa con claves enteras y nombres de productos
        Map<Integer, String> distribucionActivaMap = new HashMap<>();
        for (Map.Entry<Integer, Producto> entry : productos.entrySet()) {
            distribucionActivaMap.put(entry.getKey(), entry.getValue().getName());
        }

        return distribucionActivaMap;
    }

    public boolean EsAlgoritmoAproximado(String id) { return ctrlEstanteria.EsApxorimado(id);}

    public Distribucion getDistribucionActiva() {
        return ctrlEstanteria.getDistribucionActiva();
    }

    public int getNumeroEstantes() {
        return Estanteria.getNumeroEstantes();
    }

    public String getRelationshipsString(String product) {
        return ctrlInventario.getRelationshipsString(product);
    }

    public Double getRelationship(String product1, String product2) {return ctrlInventario.getRelation(product1, product2);}

    public Object[][] getInventarioData() {return ctrlInventario.getInventarioData();}

    public Set<String> getInventoryNames() {return ctrlInventario.getProductosPorId().keySet();}

    public Distribucion getDistribucionById(String id) {return ctrlEstanteria.getDistribucionById(id);}

    public Map<String, String> getDistributions() {return ctrlEstanteria.getAllDistributions();}

    public Map<String, Producto> getInventory() {return ctrlInventario.getProductosPorId();}


}
