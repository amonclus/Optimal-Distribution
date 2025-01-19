package FONTS.Presentacio.ControladorsPresentacio;

import FONTS.Domini.ControladorsDomini.CtrlDomini;
import FONTS.Domini.Distribucion;
import FONTS.Domini.Producto;
import FONTS.Presentacio.Vistas.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.*;

/**
 * Controlador de la capa de presentación: se encarga de gestionar las vistas y la interacción con el usuario.
 */
public class CtrlPresentacio implements ActionListener {
    /** Controlador de la capa de dominio para gestionar la lógica principal.*/
    private final CtrlDomini ctrlDomini;
    /** Marco principal de la interfaz gráfica.*/
    private JFrame frame;
    /** Panel principal que contiene las vistas.*/
    private JPanel mainPanel;
    /** Diseño de tarjetas para cambiar entre vistas.*/
    private CardLayout cardLayout;
    /** Pila de vistas para poder navegar hacia atrás.*/
    private final Stack<String> viewStack;
    /** Mapeo de nombres de vistas a componentes.*/
    private Map<String, Component> viewMapping;     

    /**
     * Constructor que inicializa el controlador de presentación, la interfaz gráfica, el controlador de dominio, la pila de vistas y carga la sesión.
     */
    public CtrlPresentacio() {
        int seleccion = VistaPrincipal.loadSession();
        this.ctrlDomini = new CtrlDomini(seleccion);
        try {
            ctrlDomini.loadSession();           //Cargamos la sesión de los archivos de persistencia
        } catch (RuntimeException e) {
            muestraErrorIO(e.getMessage());
        }
        
        this.viewStack = new Stack<>();     //Pila de vistas para poder navegar hacia atrás
        initializeGUI();                    //Inicializamos la interfaz gráfica
    }
    /**
     * Inicializa la interfaz gráfica del sistema.
     */
    private void initializeGUI() {
        frame = new JFrame("Sistema de gestión de supermercados");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        viewMapping = new HashMap<>();

        //Vistas que se añaden al panel principal
        addView("MainView", new VistaPrincipal(this));
        addView("ShelfView", new VistaEstanteria(this));
        addView("DistributionView", new VistaDistribucion(this));
        addView("ProductView", new VistaProducto(this));

        frame.add(mainPanel);
        cardLayout.show(mainPanel, "MainView");         //Iniciamos con la vista principal
        viewStack.push("MainView");
        frame.setVisible(true);

        //Ponemos un hook para que la sesión se guarde al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(ctrlDomini::saveSession));
    }

    /**
     * Gestiona las acciones realizadas por el usuario en la interfaz gráfica.
     * 
     * @param e Evento que representa la acción realizada.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Estantería":
                showView("ShelfView");
                break;
            case "Distribución":
            case "Gestionar distribuciones":
                showView("DistributionView");
                break;
            case "Producto":
                showView("ProductView");
                break;
            case "Atrás":
                handleBack();
                break;
            case "Volver atrás desde distribuciones":
                cardLayout.show(mainPanel, "DistributionView");
                break;
            case "Salir":
                ctrlDomini.saveSession();
                System.exit(0);
                break;
        }
    }
    /**
     * Añade una nueva vista al panel principal.
     * 
     * @param viewName Nombre de la vista.
     * @param view Componente correspondiente a la vista.
     */
    private void addView(String viewName, Component view) {
        mainPanel.add(view, viewName);
        viewMapping.put(viewName, view);
    }

    /**
     * Muestra una vista específica en la interfaz gráfica.
     * 
     * @param viewName Nombre de la vista a mostrar.
     */
    private void showView(String viewName) {
        String currentView = getCurrentView();
        //Si la vista actual no es nula y no es la misma que la que queremos mostrar, la añadimos a la pila
        if (currentView != null && !currentView.equals(viewName)) {
            viewStack.push(currentView);
        }
        cardLayout.show(mainPanel, viewName);

        if ("ShelfView".equals(viewName)) {
            Component component = viewMapping.get(viewName);
            if (component instanceof VistaEstanteria) {
                ((VistaEstanteria) component).updateActiveDistribution();
            }
        }
    }

    /**
     * Maneja la acción de retroceder a la vista anterior en la pila de navegación.
     */
    public void handleBack() {
        if (viewStack.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay más vistas para regresar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String previousView = viewStack.pop();
        cardLayout.show(mainPanel, previousView);
    }

    /**
     * Obtiene el nombre de la vista actualmente visible.
     * 
     * @return El nombre de la vista visible actualmente, o null si no hay ninguna activa.
     */
    private String getCurrentView() {
        for (Component component : mainPanel.getComponents()) {
            if (component.isVisible()) {
                for (Map.Entry<String, Component> entry : viewMapping.entrySet()) {
                    if (entry.getValue().equals(component)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return null;
    }

    /**
     Los siguientes métodos reciben parámetros de las vistas y ejecutan una acción en el controlador de dominio y devuelven el resultado a las vistas
    */

    /**
     * Añade un producto al inventario y actualiza las vistas.
     * 
     * @param name Nombre del producto.
     * @param type Tipo del producto.
     * @param attributes Atributos del producto.
     * @param relacionesMap Relaciones asociadas al producto.
     */
    public void addProduct(String name, String type, String[] attributes, Map<String, Double> relacionesMap) {
        ctrlDomini.addProduct(name, type, attributes, relacionesMap);
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateProductComboBoxes();
        ((VistaProducto) viewMapping.get("ProductView")).updateInventory();     //Actualiza el inventario de la vista con el nuevo prducto
    }

    /**
     * Elimina un producto del inventario y actualiza las vistas.
     * 
     * @param name Nombre del producto a eliminar.
     */
    public void removeProduct(String name) {
        ctrlDomini.removeProduct(name);
        //Actualiza los componentes afectados por la eliminación de un producto
        ((VistaProducto) viewMapping.get("ProductView")).updateInventory();
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateProductComboBoxes();
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateActiveDistribution();
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateShelfInfo();
    }

    /**
     * Actualiza la relación de similitud entre dos productos.
     * 
     * @param product1 Nombre del primer producto.
     * @param product2 Nombre del segundo producto.
     * @param similarity Valor de similitud.
     */
    public void updateRelationship(String product1, String product2, double similarity) {
        ctrlDomini.updateRelationship(product1, product2, similarity);
    }


    /**
     * Comprueba si existen restricciones entre dos productos en una distribución específica.
     * 
     * @param product1 Nombre del primer producto.
     * @param product2 Nombre del segundo producto.
     * @param distribucionActiva Distribución.
     * @return true si existen restricciones, false en caso contrario.
     */
    public boolean restriccionescontains(String product1, String product2, Distribucion distribucionActiva ){
        return ctrlDomini.restriccionescontains(product1, product2, distribucionActiva);
    }
    /**
     * Muestra los detalles de una distribución específica.
     * 
     * @param id Identificador de la distribución.
     * @return Cadena de texto con los detalles de la distribución.
     */
    public String displayDistribution(String id){
        return ctrlDomini.displayDistribution(id);
    }
    /**
     * Añade estantes a la estantería.
     * 
     * @param numeroEstantes Número de estantes a añadir.
     */
    public void addShelf(int numeroEstantes) {
        ctrlDomini.addShelf(numeroEstantes);
    }
    /**
     * Elimina estantes de la estantería.
     * 
     * @param numeroEstantes Número de estantes de la estantería a eliminar.
     */
    public void removeShelf(int numeroEstantes) {
        ctrlDomini.removeShelf(numeroEstantes);
    }

    /**
     * Elimina una restricción de una distribución específica.
     * 
     * @param name Nombre del producto asociado a la restricción.
     * @param idDistribution Identificador de la distribución.
     */
    public void eliminateRestriction(String name, String idDistribution ){
        ctrlDomini.eliminateRestriction(name, idDistribution);
    }
     /**
     * Crea una nueva distribución.
     * 
     * @param option Indica si se utiliza el algoritmo de aproximación.
     * @param id Identificador de la nueva distribución.
     */
    public void createDistribution(boolean option, String id ){
        ctrlDomini.createDistribution(option, id);
    }
    /**
     * Elimina una distribución existente y actualiza las vistas.
     * 
     * @param id Identificador de la distribución a eliminar.
     */
    public void eliminateDistribution(String id){
        ctrlDomini.eliminateDistribution(id);
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateProductComboBoxes();
    }
    /**
     * Activa una distribución específica y actualiza las vistas.
     * 
     * @param id Identificador de la distribución a activar.
     */
    public void activateDistribution(String id){
        ctrlDomini.activateDistribution(id);
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateProductComboBoxes();
        ((VistaEstanteria) viewMapping.get("ShelfView")).updateShelfInfo();
    }
     /**
     * Modifica una distribución intercambiando la posición de dos productos.
     * 
     * @param product1 Nombre del primer producto.
     * @param product2 Nombre del segundo producto.
     */
    public void modificarDistribucion(String product1, String product2 ){
        ctrlDomini.modificarDistribucion(product1, product2);
    }
    /**
     * Obtiene la distribución activa en el sistema.
     * 
     * @return La distribución activa o null si no existe ninguna activa.
     */
    public Distribucion getDistribucionActiva(){
        if (ctrlDomini.getDistribucionActiva() == null) return null;
        else return ctrlDomini.getDistribucionActiva();
    }
    /**
     * Añade una restricción a una distribución específica.
     * 
     * @param distributionId Identificador de la distribución.
     * @param productName Nombre del producto.
     * @param pos Posición asociada a la restricción.
     */
    public void addRestriction(String distributionId, String productName, int pos ){
        ctrlDomini.addRestriction(distributionId, productName, pos);
    }
    /**
     * Obtiene el inventario actual del sistema.
     * 
     * @return Mapa con los productos y sus detalles en el inventario.
     */
    public Map<String, Producto> getInventory() {
        return ctrlDomini.getInventory();
    }
    /**
     * Obtiene los datos del inventario en formato de matriz para visualización.
     * 
     * @return Matriz de objetos con los datos del inventario.
     */
    public Object[][] getInventarioData() {
        return ctrlDomini.getInventarioData();
    }
    /**
     * Obtiene los nombres de los productos en el inventario.
     * 
     * @return Conjunto de nombres de los productos.
     */
    public Set<String> getInventoryNames() {
        return ctrlDomini.getInventoryNames();
    }

    /**
     * Obtiene las distribuciones existentes en el sistema.
     * 
     * @return Mapa con los identificadores y detalles de las distribuciones.
     */
    public Map<String, String> getDistributions(){
        return ctrlDomini.getDistributions();
    }
    /**
     * Obtiene una distribución específica por su identificador.
     * 
     * @param id Identificador de la distribución.
     * @return La distribución correspondiente.
     */
    public Distribucion getDistribucionById(String id ){
        return ctrlDomini.getDistribucionById(id);
    }
    /**
     * Obtiene el mapa de una distribución activa.
     * 
     * @return Mapa de la distribución activa con las posiciones y productos.
     */
    public Map<Integer,String> getDistribucionActivaMap(){
        String idActiva = ctrlDomini.getDistribucionActiva().getId();
        return ctrlDomini.getDistribucionMap(idActiva);
    }
    /**
     * Obtiene el mapa de una distribución específica.
     * 
     * @param id Identificador de la distribución.
     * @return Mapa de la distribución con las posiciones y productos.
     */
    public Map<Integer,String> getDistribucionMap(String id){
        return ctrlDomini.getDistribucionMap(id);
    }
    /**
     * Obtiene el número de estantes en el sistema.
     * 
     * @return Número de estantes.
     */
    public int getNumeroEstantes(){
        return ctrlDomini.getNumeroEstantes();
    }
    /**
    * Obtiene una cadena con las relaciones de un producto.
    * 
    * @param name Nombre del producto.
    * @return Cadena con las relaciones del producto.
    */
    public String getRelationshipsString(String name) {
        return ctrlDomini.getRelationshipsString(name);
    }
    /**
     * Obtiene la similitud entre dos productos.
     * 
     * @param product1 Nombre del primer producto.
     * @param product2 Nombre del segundo producto.
     * @return Valor de la similitud.
     */
    public Double getRelationship(String product1, String product2) {
        return ctrlDomini.getRelationship(product1, product2);}

    /**
     * Obtiene el mapeo de vistas actuales.
     * 
     * @return Mapa de nombres de vistas a componentes.
     */
    public Map<String, Component> getViewMapping() {
        return viewMapping;
    }
    /** Verifica si un producto existe en el inventario.
    * 
    * @param name Nombre del producto.
    * @return true si el producto existe, false en caso contrario.
    */
    public boolean ExistProduct(String name) {
        return ctrlDomini.ExisteProducto(name);
    }
    /**
     * Obtiene el panel principal de la interfaz gráfica.
     * 
     * @return El panel principal.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
    /**
     * Obtiene el diseño de tarjetas utilizado para manejar las vistas.
     * 
     * @return El diseño de tarjetas.
     */
    public CardLayout getCardLayout() {
        return cardLayout;
    }

    /**
     * Muestra un mensaje de error en la interfaz gráfica.
     * 
     * @param err Mensaje de error a mostrar.
     */
    public static void muestraErrorIO(String err){
        VistaPrincipal.muestraErrorIO(err);
    }

    /**
     * Método principal que inicia la ejecución del controlador de presentación.
     * 
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CtrlPresentacio::new);
    }

}