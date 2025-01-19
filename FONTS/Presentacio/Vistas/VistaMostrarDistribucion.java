package FONTS.Presentacio.Vistas;

import FONTS.Utils.ButtonUtils;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

/**
 * Vista encargada de mostrar una distribución en formato de tabla.
 */
public class VistaMostrarDistribucion extends JPanel {
    /** Tabla que muestra los datos de la distribución. */
    private JTable table;
    /** Panel de desplazamiento que contiene la tabla.*/
    private JScrollPane scrollPane;
     /** Nombres de las columnas de la tabla.*/
    private String[] columnNames = {"ID"};

    /**
     * Constructor que inicializa la vista para mostrar una distribución.
     * 
     * @param id Identificador de la distribución.
     * @param distributionDetails Detalles de la distribución en formato de texto.
     * @param listener Controlador de eventos asociado.
     */
    public VistaMostrarDistribucion(String id, String distributionDetails, ActionListener listener) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Distribución: " + id, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 63, 65));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        table = new JTable(getTableData(distributionDetails), columnNames) {
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
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(200, 200, 200));

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backButton = ButtonUtils.createStyledButton("Atrás", listener);
        backButton.setActionCommand("Volver atrás desde distribuciones");
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
    /**
     * Procesa los detalles de la distribución para generar los datos de la tabla.
     * 
     * @param distributionDetails Detalles de la distribución en formato de texto.
     * @return Matriz de objetos con los datos de la tabla.
     */
    private Object[][] getTableData(String distributionDetails) {
        String[] rows = distributionDetails.split("\n");
        Object[][] data = new Object[rows.length][2];
        for (int i = 0; i < rows.length; i++) {
            String[] columns = rows[i].split(",");
            if (columns.length == 2) {
                data[i][0] = columns[0].trim();
                data[i][1] = columns[1].trim();
            } else {
                data[i][0] = columns[0].trim();
                data[i][1] = "";
            }
        }
        return data;
    }

}