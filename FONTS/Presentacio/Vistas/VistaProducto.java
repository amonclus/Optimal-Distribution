package FONTS.Presentacio.Vistas;

import FONTS.Presentacio.ControladorsPresentacio.CtrlPresentacio;
import FONTS.Utils.ButtonUtils;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
/**
 * Vista encargada de gestionar los productos del inventario.
 */
public class VistaProducto extends JPanel {
    /** Color de fondo de la vista. */
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    /** Color del título de la vista. */
    private static final Color TITLE_COLOR = new Color(60, 63, 65);
    /** Color del encabezado de la tabla. */
    private static final Color TABLE_HEADER_COLOR = new Color(100, 149, 237);
    /** Color del texto de la tabla. */
    private static final Color TABLE_FOREGROUND_COLOR = Color.BLACK;
    /** Color de fondo de la tabla. */
    private static final Color TABLE_BACKGROUND_COLOR = Color.WHITE;
    /** Tabla que muestra los productos del inventario. */
    private final JTable table;
    /** Nombres de las columnas de la tabla. */
    private final String[] columnNames = {"Nombre", "Tipo", "Atributos"};
    /** Controlador de presentación asociado a esta vista. */
    private final CtrlPresentacio ctrlPresentacio;

    /**
     * Constructor que inicializa la vista de productos con los componentes necesarios.
     * 
     * @param listener Controlador de presentación asociado.
     */
    public VistaProducto(CtrlPresentacio listener) {
        ctrlPresentacio = listener;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Gestión de Productos", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        table = new JTable(ctrlPresentacio.getInventarioData(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);

        table.setBackground(TABLE_BACKGROUND_COLOR);
        table.setForeground(TABLE_FOREGROUND_COLOR);
        table.setGridColor(new Color(200, 200, 200));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        buttonPanel.add(ButtonUtils.createStyledButton("Añadir Producto", e->addProduct()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(ButtonUtils.createStyledButton("Eliminar Producto", e->removeProduct()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(ButtonUtils.createStyledButton("Modificar Relaciones", e->updateRelationships()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(ButtonUtils.createStyledButton("Mostrar Relaciones", e->displayRelationships()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(ButtonUtils.createStyledButton("Atrás", listener));

        add(buttonPanel, BorderLayout.EAST);
    }

    /**
     * Actualiza los datos del inventario en la tabla.
     */
    public void updateInventory() {
        DefaultTableModel model = new DefaultTableModel(ctrlPresentacio.getInventarioData(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }
    /**
     * Abre un cuadro de diálogo para añadir un nuevo producto al inventario.
     */
    public void addProduct() {
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField attributesField = new JTextField();

        panel.add(new JLabel("Introduce el nombre del producto:"));
        panel.add(nameField);
        panel.add(new JLabel("Introduce el tipo del producto:"));
        panel.add(typeField);
        panel.add(new JLabel("Introduce los atributos del producto (separados por ,):"));
        panel.add(attributesField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Añadir Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            String attributes = attributesField.getText().trim();

            if (ctrlPresentacio.ExistProduct(name)) {
                JOptionPane.showMessageDialog(this, "Ya existe un producto con nombre: " + name, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar campos obligatorios
            if (name.isEmpty() || type.isEmpty() || attributes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] attributesArr = attributes.split(",");
            for (int i = 0; i < attributesArr.length; i++) {
                attributesArr[i] = attributesArr[i].trim();
            }

            // Relación entre productos
            Map<String, Double> relacionesMap = new HashMap<>();
            JPanel relacionesPanel = new JPanel(new GridLayout(0, 2));
            for (String existingProduct : ctrlPresentacio.getInventoryNames()) {
                if (!existingProduct.equals(name)) {
                    relacionesPanel.add(new JLabel(existingProduct));
                    JTextField similarityField = new JTextField();
                    relacionesPanel.add(similarityField);
                }
            }
            

            int relacionesResult = JOptionPane.showConfirmDialog(this, relacionesPanel, "Relaciones de Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (relacionesResult == JOptionPane.OK_OPTION) {
                Component[] components = relacionesPanel.getComponents();
                for (int i = 0; i < components.length; i += 2) {
                    JLabel label = (JLabel) components[i];
                    JTextField textField = (JTextField) components[i + 1];
                    String similarityStr = textField.getText().trim();

                    // Validar que el campo no esté vacío y que el valor sea un float entre 0 y 1
                    if (similarityStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "La relación con el producto " + label.getText() + " es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        double similarity = Double.parseDouble(similarityStr);
                        if (similarity < 0 || similarity > 1) {
                            JOptionPane.showMessageDialog(this, "La relación con el producto " + label.getText() + " debe ser un valor entre 0 y 1.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        relacionesMap.put(label.getText(), similarity);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Grado inválido para el producto " + label.getText() + ": " + similarityStr + ". Debe ser un decimal.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Agregar el producto y actualizar el inventario
                ctrlPresentacio.addProduct(name, type, attributesArr, relacionesMap);
                updateInventory();
            }
        }
    }

    /**
     * Abre un cuadro de diálogo para eliminar productos seleccionados del inventario.
     */
    public void removeProduct() {
        if (ctrlPresentacio.getInventoryNames().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El inventario está vacío. No hay productos para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        Set<String> inventory = ctrlPresentacio.getInventoryNames();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Map<String, JCheckBox> checkBoxMap = new HashMap<>();

        for (String productName : inventory) {
            JCheckBox checkBox = new JCheckBox(productName);
            checkBoxMap.put(productName, checkBox);
            panel.add(checkBox);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Eliminar Productos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            StringBuilder productsToDelete = new StringBuilder("¿Estás seguro de que deseas eliminar los siguientes productos?\n");
            for (Map.Entry<String, JCheckBox> entry : checkBoxMap.entrySet()) {
                if (entry.getValue().isSelected()) {
                    productsToDelete.append(entry.getKey()).append("\n");
                }
            }

            int confirmResult = JOptionPane.showConfirmDialog(this, productsToDelete.toString(), "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmResult == JOptionPane.YES_OPTION) {
                for (Map.Entry<String, JCheckBox> entry : checkBoxMap.entrySet()) {
                    if (entry.getValue().isSelected()) {
                        ctrlPresentacio.removeProduct(entry.getKey());
                    }
                }
                updateInventory();
            }
        }
    }

    /**
     * Muestra las relaciones entre los productos del inventario.
     */
    public void displayRelationships() {
        if (ctrlPresentacio.getInventoryNames().size() < 2) {
            JOptionPane.showMessageDialog(this, "Hay menos de 2 productos en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Set<String> inventory = ctrlPresentacio.getInventoryNames();
        StringBuilder relationships = new StringBuilder();

        for (String product1 : inventory) {
            relationships.append(ctrlPresentacio.getRelationshipsString(product1)).append("\n");
        }

        JTextArea textArea = new JTextArea(relationships.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Relaciones entre Productos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Abre un cuadro de diálogo para actualizar las relaciones entre dos productos.
     */
    public void updateRelationships() {
        if (ctrlPresentacio.getInventoryNames().size() < 2) {
            JOptionPane.showMessageDialog(this, "Hay menos de 2 productos en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        Set<String> inventory = ctrlPresentacio.getInventoryNames();
        JPanel panel = new JPanel(new GridLayout(0, 2));

        JComboBox<String> product1ComboBox = new JComboBox<>(inventory.toArray(new String[0]));
        JComboBox<String> product2ComboBox = new JComboBox<>(inventory.toArray(new String[0]));
        JTextField similarityField = new JTextField();
        JLabel currentSimilarityLabel = new JLabel("Grado de similitud actual: ");
        JLabel valor = new JLabel("N/A");
        panel.add(new JLabel("Producto 1:"));
        panel.add(product1ComboBox);
        panel.add(new JLabel("Producto 2:"));
        panel.add(product2ComboBox);
        panel.add(new JLabel("Grado de similitud:"));
        panel.add(similarityField);
        panel.add(currentSimilarityLabel);
        panel.add(valor);

        ActionListener updateSimilarityListener = e -> {
            String product1 = (String) product1ComboBox.getSelectedItem();
            String product2 = (String) product2ComboBox.getSelectedItem();
            if (product1 != null && product2 != null && !product1.equals(product2)) {
                Double similarity = ctrlPresentacio.getRelationship(product1, product2);
                if (similarity != null) {
                    valor.setText("" + similarity);
                } else {
                    valor.setText("N/A");
                }
            } else {
                valor.setText("N/A");
            }
        };

        product1ComboBox.addActionListener(updateSimilarityListener);
        product2ComboBox.addActionListener(updateSimilarityListener);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Relaciones", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String product1 = (String) product1ComboBox.getSelectedItem();
            String product2 = (String) product2ComboBox.getSelectedItem();
            String similarityStr = similarityField.getText().trim();
            assert product1 != null;
            if (product1.equals(product2)) {
                JOptionPane.showMessageDialog(this, "No se puede establecer una relación entre el mismo producto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!similarityStr.isEmpty()) {
                try {
                    double similarity = Double.parseDouble(similarityStr);
                    if(similarity < 0 || similarity > 1) {
                        JOptionPane.showMessageDialog(this, "El grado de similitud debe estar entre 0 y 1.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ctrlPresentacio.updateRelationship(product1, product2, similarity);
                    JOptionPane.showMessageDialog(this, "Relación actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Grado inválido: " + similarityStr + ". Debe ser un numero decimal.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El campo de grado de similitud no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}