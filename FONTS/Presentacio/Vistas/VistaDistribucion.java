package FONTS.Presentacio.Vistas;

import FONTS.Domini.Distribucion;
import FONTS.Domini.Producto;
import FONTS.Presentacio.ControladorsPresentacio.CtrlPresentacio;
import FONTS.Utils.ButtonUtils;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Vista encargada de gestionar las operaciones relacionadas con las distribuciones.
 */
public class VistaDistribucion extends JPanel {
    /** Color de fondo de la vista. */
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    /** Color del título de la vista. */
    private static final Color TITLE_COLOR = new Color(60, 63, 65);
    /** Controlador de presentación asociado a la vista. */
    private final CtrlPresentacio ctrlPresentacio;

    /**
     * Constructor que inicializa la vista de distribuciones con los componentes necesarios.
     * 
     * @param ctrlPresentacio El controlador de presentación asociado.
     */
    public VistaDistribucion(CtrlPresentacio ctrlPresentacio) {
        this.ctrlPresentacio = ctrlPresentacio;         //Guardamos una referencia al controlador de presentación

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Gestión de Distribuciones", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        //Añadimos botones y los asociamos a una función
        centerPanel.add(ButtonUtils.createStyledButton("Crear Distribución", e -> handleCreateDistribution()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Eliminar Distribución", e -> handleEliminateDistribution()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Activar Distribución", e -> handleActivateDistribution()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Añadir Restricción", e -> handleAddRestriction()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Eliminar Restricción", e -> handleRemoveRestriction()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Mostrar Distribución", e -> handleDisplayDistribution()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Atrás", e -> ctrlPresentacio.handleBack()));

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Maneja la acción de crear una nueva distribución y mostrar mensajes de error o éxito.
     */
    private void handleCreateDistribution() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        JCheckBox approximationCheckBox = new JCheckBox("Usar algoritmo de aproximación");
        JTextField idField = new JTextField();

        panel.add(new JLabel("Introduce el ID de la distribución:"));
        panel.add(idField);
        panel.add(approximationCheckBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Crear Distribución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            boolean useApproximation = approximationCheckBox.isSelected();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID de la distribución no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane loadingPane = new JOptionPane("Creando distribución, por favor espera...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog loadingDialog = loadingPane.createDialog(this, "Cargando");
            loadingDialog.setModal(false);
            loadingDialog.setVisible(true);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                private boolean success = false;

                @Override
                protected Void doInBackground() {
                    try {
                        ctrlPresentacio.createDistribution(useApproximation, id);
                        success = true;
                    } catch (Exception e) {
                        success = false;
                        JOptionPane.showMessageDialog(VistaDistribucion.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    if (success) {
                        JOptionPane.showMessageDialog(VistaDistribucion.this, "Distribución creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        
                    }
                }
            };
            worker.execute();
        }
    }

    /**
     * Maneja la acción de eliminar distribuciones seleccionadas por el usuario y mostrar mensajes de error o éxito.
     */
    
    private void handleEliminateDistribution() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.yesButtonText", "Sí");
        Map<String, String> distributions = ctrlPresentacio.getDistributions();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Map<String, JCheckBox> checkBoxMap = new HashMap<>();

        int creadas = 0;

        for (String distributionId : distributions.keySet()) {
            JCheckBox checkBox = new JCheckBox(distributionId);
            checkBoxMap.put(distributionId, checkBox);
            panel.add(checkBox);
            ++creadas;
        }
        if (creadas==0) {
            JOptionPane.showMessageDialog(this, "No hay distribuciones existentes en la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(this, panel, "Eliminar Distribuciones", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            StringBuilder distributionsToDelete = new StringBuilder("¿Estás seguro de que deseas eliminar las siguientes distribuciones?\n");
            for (Map.Entry<String, JCheckBox> entry : checkBoxMap.entrySet()) {
                if (entry.getValue().isSelected()) {
                    distributionsToDelete.append(entry.getKey()).append("\n");
                }
            }

            int confirmResult = JOptionPane.showConfirmDialog(this, distributionsToDelete.toString(), "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmResult == JOptionPane.YES_OPTION) {
                for (Map.Entry<String, JCheckBox> entry : checkBoxMap.entrySet()) {
                    if (entry.getValue().isSelected()) {
                        ctrlPresentacio.eliminateDistribution(entry.getKey());
                    }
                }
                ((VistaEstanteria) ctrlPresentacio.getViewMapping().get("ShelfView")).updateActiveDistribution();
                ((VistaEstanteria) ctrlPresentacio.getViewMapping().get("ShelfView")).updateShelfInfo();
            }
        }
    }

    /**
     * Maneja la acción de activar una distribución seleccionada por el usuario y mostrar mensajes de error o éxito.
     */

    private void handleActivateDistribution() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JComboBox<String> distributionComboBox = new JComboBox<>();
        int creadas = 0;
        for (String distributionId : ctrlPresentacio.getDistributions().keySet()) {
            distributionComboBox.addItem(distributionId);
            ++creadas;
        }
        panel.add(distributionComboBox);
        if(creadas==0) {
            JOptionPane.showMessageDialog(this, "No hay distribuciones existentes en la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Activar Distribución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            boolean activated = false;
            String distributionId = (String) distributionComboBox.getSelectedItem();
            if (distributionId == null) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar una distribución.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

                    try {
                        ctrlPresentacio.activateDistribution(distributionId);
                        ((VistaEstanteria) ctrlPresentacio.getViewMapping().get("ShelfView")).updateActiveDistribution();
                        JOptionPane.showMessageDialog(this, "Distribución activada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        activated = true;
                        ((VistaEstanteria) ctrlPresentacio.getViewMapping().get("ShelfView")).updateProductComboBoxes();

                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
            if (!activated) {
                JOptionPane.showMessageDialog(this, "No se seleccionó ninguna distribución.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Maneja la acción de añadir una restricción a una distribución y mostrar mensajes de error o éxito.
     */
    private void handleAddRestriction() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        // Paso 1: Seleccionar la distribución
        JComboBox<String> distributionComboBox = new JComboBox<>();
        int distribucionesCreadas = 0;

        for (String distributionId : ctrlPresentacio.getDistributions().keySet()) {
            distributionComboBox.addItem(distributionId);
            ++distribucionesCreadas;
        }

        if (distribucionesCreadas == 0) {
            JOptionPane.showMessageDialog(this, "No hay distribuciones existentes en la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel distributionPanel = new JPanel(new GridLayout(0, 1));
        distributionPanel.add(new JLabel("Selecciona la distribución:"));
        distributionPanel.add(distributionComboBox);

        int result = JOptionPane.showConfirmDialog(this, distributionPanel, "Seleccionar Distribución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return; // El usuario canceló o cerró la ventana
        }

        String selectedDistributionId = (String) distributionComboBox.getSelectedItem();

        if (selectedDistributionId == null) {
            JOptionPane.showMessageDialog(this, "No se seleccionó ninguna distribución.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Paso 2: Seleccionar el producto y la posición
        JComboBox<String> productComboBox = new JComboBox<>();
        JTextField positionField = new JTextField();

        Map<Integer, String> productos = ctrlPresentacio.getDistribucionMap(selectedDistributionId);

        // Rellenar los comboBox con los nombres de los productos
        for (String productName : productos.values()) {
            productComboBox.addItem(productName);
        }

        JPanel productPanel = new JPanel(new GridLayout(0, 1));
        productPanel.add(new JLabel("Selecciona el producto a restringir:"));
        productPanel.add(productComboBox);
        productPanel.add(new JLabel("Introduce la posición a la que lo quieres restringir:"));
        productPanel.add(positionField);

        result = JOptionPane.showConfirmDialog(this, productPanel, "Añadir Restricción", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return; // El usuario canceló o cerró la ventana
        }

        String selectedProductName = (String) productComboBox.getSelectedItem();
        String positionStr = positionField.getText().trim();

        if (selectedProductName == null || positionStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son necesarios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int position = Integer.parseInt(positionStr);
            ctrlPresentacio.addRestriction(selectedDistributionId, selectedProductName, position);
            JOptionPane.showMessageDialog(this, "Restricción añadida y aplicada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (ctrlPresentacio.getDistribucionActiva() != null && ctrlPresentacio.getDistribucionActiva().getId().equals(selectedDistributionId)) {
                ((VistaEstanteria) ctrlPresentacio.getViewMapping().get("ShelfView")).updateActiveDistribution();
                ((VistaEstanteria) ctrlPresentacio.getViewMapping().get("ShelfView")).updateShelfInfo();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La posición debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Maneja la acción de eliminar restricciones de una distribución y mostrar mensajes de error o éxito.
     */
    private void handleRemoveRestriction() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        JComboBox<String> distributionComboBox = new JComboBox<>();
        Map<String, JCheckBox> checkBoxMap = new HashMap<>();

        int creadas = 0;
        // Llenar el comboBox con las distribuciones disponibles
        for (String distributionId : ctrlPresentacio.getDistributions().keySet()) {
            distributionComboBox.addItem(distributionId);
            ++creadas;
        }

        if (creadas==0) {
            JOptionPane.showMessageDialog(this, "No hay distribuciones existentes en la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel.add(new JLabel("Selecciona la distribución:"));
        panel.add(distributionComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Seleccionar Distribución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String distributionId = (String) distributionComboBox.getSelectedItem();
            if (distributionId == null) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar una distribución.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Distribucion distribucion = ctrlPresentacio.getDistribucionById(distributionId);
            if (distribucion == null) {
                JOptionPane.showMessageDialog(this, "Distribución no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(distribucion.getRestricciones().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La distribución no tiene restricciones.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }   

            panel = new JPanel(new GridLayout(0, 1));
            for (Map.Entry<Producto, Integer> entry : distribucion.getRestricciones().entrySet()) {
                JCheckBox checkBox = new JCheckBox(entry.getKey().getName() + " en posición " + entry.getValue());
                checkBoxMap.put(entry.getKey().getName(), checkBox);
                panel.add(checkBox);
            }

            result = JOptionPane.showConfirmDialog(this, panel, "Eliminar Restricciones", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int num = 0;
                for (Map.Entry<String, JCheckBox> entry : checkBoxMap.entrySet()) {
                    if (entry.getValue().isSelected()) {
    
                        ctrlPresentacio.eliminateRestriction(entry.getKey(), distributionId);
                        ++num;
                    }
                }
                if(num!=0) JOptionPane.showMessageDialog(this, "Restricciones eliminadas correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(this, "No se seleccionó ninguna restricción.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     * Maneja la acción de mostrar los detalles de una distribución seleccionada  y mostrar mensajes de error o éxito.
     */
    private void handleDisplayDistribution() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        Map<String, String> distributions = ctrlPresentacio.getDistributions();
        JPanel panel = new JPanel(new GridLayout(0, 1));

        int creadas = 0;
        // Usar JComboBox para permitir una sola selección
        JComboBox<String> distributionComboBox = new JComboBox<>();
        for (String distributionId : distributions.keySet()) {
            distributionComboBox.addItem(distributionId);
            ++creadas;
        }

        if (creadas==0) {
            JOptionPane.showMessageDialog(this, "No hay distribuciones existentes en la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel.add(new JLabel("Selecciona una distribución:"));
        panel.add(distributionComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Seleccionar Distribución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String selectedDistributionId = (String) distributionComboBox.getSelectedItem();
            if (selectedDistributionId != null) {
                // Mostrar detalles de la distribución seleccionada
                String distributionDetails = ctrlPresentacio.displayDistribution(selectedDistributionId);
                VistaMostrarDistribucion vistaDistribuciones = new VistaMostrarDistribucion(selectedDistributionId, distributionDetails, ctrlPresentacio);
                ctrlPresentacio.getMainPanel().add(vistaDistribuciones, "DistributionDetailsView");
                ctrlPresentacio.getCardLayout().show(ctrlPresentacio.getMainPanel(), "DistributionDetailsView");
            } else {
                JOptionPane.showMessageDialog(this, "Debes seleccionar una distribución.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
