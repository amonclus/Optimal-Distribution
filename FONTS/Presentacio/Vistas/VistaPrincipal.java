// FONTS/Presentacio/Vistas/VistaPrincipal.java
package FONTS.Presentacio.Vistas;

import FONTS.Utils.ButtonUtils;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Vista principal del sistema que permite la navegación hacia otras vistas.
 */
public class VistaPrincipal extends JPanel {
    /** Color de fondo de la vista. El color es gris muy claro*/
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); 
    /** Color del título de la vista. El color es Dark Gray */
    private static final Color TITLE_COLOR = new Color(60, 63, 65); 

    /**
     * Constructor que inicializa la vista principal con los botones necesarios.
     * 
     * @param listener Controlador de eventos asociado.
     */
    public VistaPrincipal(ActionListener listener) {
        // Establecer un layout de BorderLayout
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Título estilizado
        JLabel titleLabel = new JLabel("Sistema de Inventario y Distribución", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central con BoxLayout (Y_AXIS) para botones
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Añadir botones estilizados
        centerPanel.add(ButtonUtils.createStyledButton("Estantería", listener));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Distribución", listener));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(ButtonUtils.createStyledButton("Producto", listener));
        centerPanel.add(Box.createVerticalGlue()); // Añadir espacio flexible
        centerPanel.add(ButtonUtils.createStyledButton("Salir", listener));

        add(centerPanel, BorderLayout.CENTER);
    }
    /**
     * Método estático que muestra un cuadro de diálogo para seleccionar una sesión a cargar.
     * 
     * @return Entero que representa la sesión seleccionada (0: Base, 1: Editada).
     */
    public static int loadSession(){
        // Crear iconos para opciones
        ImageIcon baseIcon = new ImageIcon("path/to/base_icon.png");
        ImageIcon editadoIcon = new ImageIcon("path/to/editado_icon.png");
    
        // Opciones para el cuadro de diálogo
        Object[] opciones = {
            "<html><h3 style='color:green;'>Cargar Sesión Base</h3></html>",
            "<html><h3 style='color:blue;'>Cargar Sesión Editada</h3></html>"
        };
    
        // Mostrar el cuadro de diálogo
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "Seleccione la sesión a cargar:",
            "Cargar Inventario",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null, // No se necesita un icono principal
            opciones,
            opciones[1]
        );
        // Actuar según la selección
        switch (seleccion) {
            case 0 -> // Base seleccionado
                JOptionPane.showMessageDialog(null, "Se ha cargado la sesión base.", "Sesión Base", JOptionPane.INFORMATION_MESSAGE, baseIcon);
            case 1 -> // Editado seleccionado
                JOptionPane.showMessageDialog(null, "Se ha cargado la sesión editada.", "Sesión Editada", JOptionPane.INFORMATION_MESSAGE, editadoIcon);
            default -> // Cierre o ninguna selección
                JOptionPane.showMessageDialog(null, "No se realizó ninguna selección. Se cargará la sesión por defecto.", "Sesión Predeterminado", JOptionPane.WARNING_MESSAGE);
        }
        return seleccion;
    
    }

    /**
     * Método estático para mostrar un mensaje de error de I/O.
     * @param err Mensaje de error a mostrar.
     */
    public static void muestraErrorIO(String err){
        JOptionPane.showMessageDialog(null, err, "Error IO", JOptionPane.ERROR_MESSAGE);
    }

}