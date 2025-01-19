package FONTS.Persistencia;

import java.io.*;

/**
 * Clase que se encarga de la persistencia del inventario inicial. Este representa el inventario base que se carga al
 * iniciar la aplicación por primera vez o cuando el usuario quiere reestablecer el inventario a su estado original.
 */
public class BDDInventarioInicial {
    /** Ruta del inventario base */
    private static final String INVENTORY_BASE_FILE = "../DATA/inventario_base.txt";
    /** Ruta de las relaciones base */
    private static final String RELATIONS_BASE_FILE = "../DATA/relations_base.txt";
    /** Ruta de las distribuciones base */
    private static final String DISTRIBUTIONS_BASE_FILE = "../DATA/distributions_base.txt";
    /** Ruta de la estantería base */
    private static final String ESTANTERIA_BASE_FILE = "../DATA/estanteria_base.txt";
    /** Ruta del inventario editado*/
    private static final String INVENTORY_FILE = "../DATA/inventory.txt";
    /** Ruta de las relaciones editadas*/
    private static final String RELATIONS_FILE = "../DATA/relations.txt";
    /** Ruta de las distribuciones editadas*/
    private static final String DISTRIBUTIONS_FILE = "../DATA/distributions.txt";
    /** Ruta de la estantería editada*/
    private static final String ESTANTERIA_FILE = "../DATA/estanteria.txt";

    /**
     * Constructor de la clase vacío
     */
    public BDDInventarioInicial() {
        // Constructor vacío, inicialización explícita necesaria
    }

    /**
     * Sobrescribe el inventario editado con el inventario base en los archivos de texto.
     *  @throws RuntimeException si hay un error al sobrescribir el inventario
     */
    public void sobrescribirInventarioConBase() {
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_BASE_FILE));
             BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobrescribir el inventario editado con el inventario base: " + e.getMessage());
        }
    }

    /**
     * Sobrescribe las relaciones editadas con las relaciones base en los archivos de texto.
     * @throws RuntimeException si hay un error al sobrescribir las relaciones
     */
    public void sobrescribirRelacionesConBase() {
        try (BufferedReader br = new BufferedReader(new FileReader(RELATIONS_BASE_FILE));
             BufferedWriter bw = new BufferedWriter(new FileWriter(RELATIONS_FILE))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobrescribir las relaciones editadas con las relaciones base: " + e.getMessage());
        }
    }

    /**
     * Sobrescribe la estantería editada con la estantería base en los archivos de texto.
     *  @throws RuntimeException si hay un error al sobrescribir la estantería
     */
    public void sobrescribirEstanteriaConBase() {
        try (BufferedReader br = new BufferedReader(new FileReader(ESTANTERIA_BASE_FILE));
             BufferedWriter bw = new BufferedWriter(new FileWriter(ESTANTERIA_FILE))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobrescribir la estantería editada con la estantería base: " + e.getMessage());
        }
    }

    /**
     * Sobrescribe las distribuciones editadas con las distribuciones base en los archivos de texto.
     * @throws RuntimeException si hay un error al sobrescribir las distribuciones
     */
    public void sobrescribirDistribucionesConBase() {
        try (BufferedReader br = new BufferedReader(new FileReader(DISTRIBUTIONS_BASE_FILE));
             BufferedWriter bw = new BufferedWriter(new FileWriter(DISTRIBUTIONS_FILE))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobrescribir las distribuciones editadas con las distribuciones base: " + e.getMessage());
        }
    }
}