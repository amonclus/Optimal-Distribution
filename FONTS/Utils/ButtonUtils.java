// File: FONTS/Presentacio/Utils/ButtonUtils.java
package FONTS.Utils;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Clase que contiene métodos útiles para la creación de botones personalizados.
 */
public class ButtonUtils {
    /** Colores del botón */
    private static final Color BUTTON_COLOR = new Color(255, 255, 255);
    private static final Color BUTTON_HOVER_COLOR = new Color(113, 141, 162);

    /**
     * Crea un botón con estilo personalizado.
     * @param text Texto del botón
     * @param listener Acción a realizar al hacer clic en el botón
     * @return Botón con estilo personalizado
     */
    public static JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);

        // Estilo general del botón
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(BUTTON_COLOR);
        button.setOpaque(true);
        button.setActionCommand(text);

        // Borde redondeado y sombra
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

        // Tamaño
        button.setMaximumSize(new Dimension(160, 30));
        button.setMinimumSize(new Dimension(160, 30));

        // Hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 0, 0, 80), 1, true),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1, true),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)));
            }
        });

        // Añadir acción
        button.addActionListener(listener);

        return button;
    }
}