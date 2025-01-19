// FONTS/Presentacio/Vistas/VistaEstanteria.java
package FONTS.Presentacio.Vistas;

import FONTS.Presentacio.ControladorsPresentacio.CtrlPresentacio;
import FONTS.Utils.ButtonUtils;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 * Vista encargada de gestionar las operaciones relacionadas con las estanterías.
 */
public class VistaEstanteria extends JPanel {
    /** Color de fondo de la vista. */
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    /** Color del título de la vista. */
    private static final Color TITLE_COLOR = new Color(60, 63, 65);
    /** Controlador de presentación asociado. */
    private final CtrlPresentacio ctrlPresentacio;
    /** Área de texto que muestra la distribución activa. */
    private JTextArea activeDistributionArea;
    /** Área de texto que muestra información sobre la estantería. */
    private JTextArea shelfInfoArea;
     /** ComboBox para seleccionar el primer producto en la operación de intercambio. */
    private JComboBox<String> product1ComboBox;
    /** ComboBox para seleccionar el segundo producto en la operación de intercambio. */
    private JComboBox<String> product2ComboBox;
    /** Panel de desplazamiento que contiene el panel de productos. */
    private JScrollPane scrollPane;
    /** Número de estantes actuales en la estantería, empezamos con un estante.*/
    private int numShelves; 

    /**
     * Constructor que inicializa la vista de estanterías con los componentes necesarios.
     * 
     * @param listener Controlador de presentación asociado.
     */
    public VistaEstanteria(CtrlPresentacio listener) {
        this.ctrlPresentacio = listener;
        this.numShelves = listener.getNumeroEstantes();
        // Configurar el diseño principal de la vista
        setupMainLayout();
        // Configurar la etiqueta del título
        setupTitleLabel();
        // Configurar el panel central con el área de texto de distribución activa
        setupCenterPanel();
        setupProductGridPanel();
        // Configurar el panel derecho con botones y secciones adicionales
        setupRightPanel(listener);
        // Actualizar la distribución activa
        updateActiveDistribution();
    }

    /**
     * Configura el diseño principal de la vista.
     */
    private void setupMainLayout() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    /**
     * Configura la etiqueta del título.
     */
    private void setupTitleLabel() {
        JLabel titleLabel = new JLabel("Gestión de Estanterías", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }
   
    /**
     * Configura el panel central con el área de texto de distribución activa.
     */
    private void setupCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 0, 20, 20));

        activeDistributionArea = new JTextArea();
        activeDistributionArea.setEditable(false);
        activeDistributionArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        activeDistributionArea.setBackground(Color.WHITE);
        activeDistributionArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(activeDistributionArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }
    /**
     * Configura el panel de productos con una cuadrícula dinámica.
     */
    public void setupProductGridPanel() {
        JPanel productGridPanel = new JPanel();
        if (ctrlPresentacio.getDistribucionActiva() == null) {
            productGridPanel.setLayout(new BorderLayout());
            JLabel noDistributionLabel = new JLabel("No hay distribución activa", JLabel.CENTER);
            noDistributionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            productGridPanel.add(noDistributionLabel, BorderLayout.CENTER);
        } else {
            Map <Integer,String> productos = ctrlPresentacio.getDistribucionActivaMap();
            int numColumns = productos.size() / numShelves; // Calcular columnas necesarias 
            int restantes = productos.size() % numShelves; // Calcular productos restantes 
            productGridPanel.setLayout(new GridLayout(numShelves, numColumns, 10, 10)); // Ajustar filas y columnas dinámicamente
            productGridPanel.setBackground(BACKGROUND_COLOR);
            productGridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            // Obtener lista de productos desde la distribución activa
            List<String> productoList = new ArrayList<>(productos.values());
            int index = 0;
            for (int i = 0; i < numShelves; i++) {
                List<JLabel> rowLabels = new ArrayList<>();
                for (int j = 0; j < numColumns; j++) {
                    if (index < productoList.size()) {
                        String producto = productoList.get(index++);
                        JLabel productLabel = new JLabel(producto, JLabel.CENTER);
                        productLabel.setOpaque(true);
                        productLabel.setBackground(Color.WHITE);
                        productLabel.setForeground(Color.BLACK);
                        productLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        rowLabels.add(productLabel);
                    }
                }
                if(restantes > 0){
                    if (index < productoList.size()) {
                        String producto = productoList.get(index++);
                        JLabel productLabel = new JLabel(producto, JLabel.CENTER);
                        productLabel.setOpaque(true);
                        productLabel.setBackground(Color.WHITE);
                        productLabel.setForeground(Color.BLACK);
                        productLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        rowLabels.add(productLabel);
                        restantes--;
                    }
                }
                else if(productos.size() % numShelves != 0){
                    JLabel emptyLabel = new JLabel("", JLabel.CENTER);
                    emptyLabel.setOpaque(true);
                    emptyLabel.setBackground(Color.WHITE);
                    emptyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    rowLabels.add(emptyLabel);
                    
                }

                // Añadir los productos en orden inverso para cada fila alternada
                if (i % 2 == 0) {
                    for (JLabel label : rowLabels) {
                        productGridPanel.add(label);
                    }
                } else {
                    if(rowLabels.size() == numColumns) {
                        for (int k = rowLabels.size() - 1; k >= 0; k--) {
                            productGridPanel.add(rowLabels.get(k));
                        }
                    }
                    else {
                        for (int k = rowLabels.size() - 2; k >= 0; k--) {
                            productGridPanel.add(rowLabels.get(k));
                        }
                        productGridPanel.add(rowLabels.get(rowLabels.size()-1));
                    }
                }
            }
        }

        scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Configurar el tamaño del JScrollPane para que se ajuste a la ventana
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setPreferredSize(new Dimension(screenSize.width - 100, screenSize.height - 200));

        // Añadir el JScrollPane al centro
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Configura el panel derecho con botones y secciones adicionales.
     * 
     * @param listener Controlador de eventos asociado.
     */
    private void setupRightPanel(ActionListener listener) {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
    
        // Configurar los botones para añadir y eliminar estanterías
        setupShelfButtons(rightPanel, listener);
        // Configurar la sección de información de la estantería
        setupShelfInfo(rightPanel);
        // Configurar la sección para intercambiar productos
        setupSwapSection(rightPanel, listener);
        // Configurar el panel de navegación
        setupNavigationPanel(rightPanel, listener);
    
        add(rightPanel, BorderLayout.EAST);
    }
    /**
     * Configura los botones para añadir y eliminar estanterías.
     * 
     * @param panel Panel donde se colocarán los botones.
     * @param listener Controlador de eventos asociado.
     */
    private void setupShelfButtons(JPanel panel, ActionListener listener) {
        JButton addShelfButton = ButtonUtils.createStyledButton("Añadir Estante", e->{});
        addShelfButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese el número de estantes a añadir:");
            try {
                int shelvesToAdd = Integer.parseInt(input);
                if (numShelves + shelvesToAdd <= 6) {
                    ctrlPresentacio.addShelf(shelvesToAdd);
                    numShelves += shelvesToAdd;
                    remove(scrollPane); // Eliminar el JScrollPane
                    setupProductGridPanel(); // Actualizar la disposición
                    updateShelfInfo(); // Actualizar información de la estantería
                    revalidate();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Se pueden tener como máximo 6 estantes.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
            }
        });

        JButton removeShelfButton = ButtonUtils.createStyledButton("Eliminar Estante", e -> {});
        removeShelfButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese el número de estantes a eliminar:");
            try {
                int shelvesToRemove = Integer.parseInt(input);
                if (numShelves - shelvesToRemove > 0) {
                    ctrlPresentacio.removeShelf(shelvesToRemove);
                    numShelves -= shelvesToRemove;
                    remove(scrollPane); // Eliminar el JScrollPane
                    setupProductGridPanel(); // Actualizar la disposición
                    updateShelfInfo(); // Actualizar información de la estantería
                    revalidate();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Debe haber al menos un estante.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
            }
        });

        panel.add(addShelfButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(removeShelfButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    /**
     * Configura la sección de información de la estantería.
     * 
     * @param panel Panel donde se colocará la información.
     */
    private void setupShelfInfo(JPanel panel) {
        JLabel shelfInfoLabel = new JLabel("Información de la ");
        JLabel shelfInfoLabel2 = new JLabel("Estantería:");
        shelfInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        shelfInfoLabel.setForeground(TITLE_COLOR);
        shelfInfoLabel2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        shelfInfoLabel2.setForeground(TITLE_COLOR);
        shelfInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        shelfInfoLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(shelfInfoLabel);
        panel.add(shelfInfoLabel2);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        shelfInfoArea = new JTextArea();
        shelfInfoArea.setEditable(false);
        shelfInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        shelfInfoArea.setBackground(Color.WHITE);
        shelfInfoArea.setForeground(Color.BLACK);
        shelfInfoArea.setText(getShelfInfo());
        panel.add(shelfInfoArea);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    /**
     * Configura la sección para intercambiar productos.
     * 
     * @param panel Panel donde se colocará la sección de intercambio.
     * @param listener Controlador de eventos asociado.
     */
    private void setupSwapSection(JPanel panel, ActionListener listener) {
        JLabel swapLabel = new JLabel("Intercambiar Productos:");
        swapLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        swapLabel.setForeground(TITLE_COLOR);
        swapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(swapLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        product1ComboBox = new JComboBox<>();
        product2ComboBox = new JComboBox<>();
        updateProductComboBoxes();
    
        panel.add(product1ComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(product2ComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    
        JButton swapButton = ButtonUtils.createStyledButton("Intercambiar", e->handleSwapProducts());
        swapButton.setActionCommand("Intercambiar Productos");
        panel.add(swapButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    /**
     * Configura el panel de navegación con botones para retroceder o gestionar distribuciones.
     * 
     * @param panel Panel donde se colocará el panel de navegación.
     * @param listener Controlador de eventos asociado.
     */
    private void setupNavigationPanel(JPanel panel, ActionListener listener) {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBackground(BACKGROUND_COLOR);
    
        JButton backButton = ButtonUtils.createStyledButton("Atrás", listener);
        JButton goToDistributionsButton = ButtonUtils.createStyledButton("Gestionar distribuciones", listener);
    
        navigationPanel.add(backButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        navigationPanel.add(goToDistributionsButton);
    
        panel.add(navigationPanel);
    }
    /**
     * Maneja el intercambio de productos seleccionados en los ComboBoxes y actualiza la vista.
     */
    public void handleSwapProducts() {
        String product1 = (String) getProduct1ComboBox().getSelectedItem();
        String product2 = (String) getProduct2ComboBox().getSelectedItem();

        if (ctrlPresentacio.getDistribucionActiva() == null) {
            JOptionPane.showMessageDialog(this, "No hay distribución activa. No se pueden intercambiar productos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (product1 == null || product2 == null) {
            JOptionPane.showMessageDialog(this, "Selecciona dos productos para intercambiar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (product1.equals(product2)) {
            JOptionPane.showMessageDialog(this, "No se puede intercambiar un producto consigo mismo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ctrlPresentacio.restriccionescontains(product1, product2, ctrlPresentacio.getDistribucionActiva())) {
            JOptionPane.showMessageDialog(this, "No se puede intercambiar los productos porque uno o ambos contienen una restricción.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ctrlPresentacio.modificarDistribucion(product1, product2);
        updateActiveDistribution();
        JOptionPane.showMessageDialog(this, "Productos intercambiados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Actualiza la distribución activa mostrada en la vista.
     */
    public void updateActiveDistribution() {
        
        if (ctrlPresentacio.getDistribucionActiva() != null) {
            String id = ctrlPresentacio.getDistribucionActiva().getId();
            String distributionDetails = ctrlPresentacio.displayDistribution(id);
            activeDistributionArea.setText(distributionDetails);
        } else {
            activeDistributionArea.setText("No hay distribución activa");
        }
        remove(scrollPane); // Eliminar el JScrollPane actual
        setupProductGridPanel(); // Reconstruir el panel de la estantería
        revalidate();
        repaint();
    }
    /**
     * Actualiza la información mostrada sobre la estantería.
     */
    public void updateShelfInfo() {
        shelfInfoArea.setText(getShelfInfo());
    }
     /**
     * Actualiza los ComboBoxes con los productos disponibles en la distribución activa.
     */
   public void updateProductComboBoxes() {
        product1ComboBox.removeAllItems();
        product2ComboBox.removeAllItems();

        // Obtener el mapa de la distribución activa
        if(ctrlPresentacio.getDistribucionActiva() != null) {
             Map<Integer, String> productos = ctrlPresentacio.getDistribucionActivaMap();

        // Rellenar los comboBox con los nombres de los productos
            for (String productName : productos.values()) {
                product1ComboBox.addItem(productName);
                product2ComboBox.addItem(productName);
            }
        }
    }

   /**
     * Obtiene la información de la estantería actual.
     * 
     * @return Cadena con información sobre la estantería.
     */
    private String getShelfInfo() {
        StringBuilder shelfInfo = new StringBuilder("Número de estantes: " + ctrlPresentacio.getNumeroEstantes());
        if (ctrlPresentacio.getDistribucionActiva() != null) {
            String id = ctrlPresentacio.getDistribucionActiva().getId();
            shelfInfo.append("\nID de la distribución activa: ").append(id);
        } else {
            shelfInfo.append("\nNo hay distribución activa.");
        }
        return shelfInfo.toString();
    }
    /**
     * Obtiene el ComboBox del primer producto.
     * 
     * @return ComboBox del primer producto.
     */
    public JComboBox<String> getProduct1ComboBox() {
        return product1ComboBox;
    }
    /**
     * Obtiene el ComboBox del segundo producto.
     * 
     * @return ComboBox del segundo producto.
     */
    public JComboBox<String> getProduct2ComboBox() {
        return product2ComboBox;
    }
}
