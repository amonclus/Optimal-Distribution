package FONTS.Persistencia;

import FONTS.Domini.Distribucion;
import FONTS.Domini.Producto;
import FONTS.Utils.Pair;
import java.io.*;
import java.util.*;

/**
 * Clase que se encarga de la persistencia de las distribuciones
 */
public class BDDDistribucion {
    /** Ruta de las distribuciones */
    private static final String DISTRIBUTIONS_FILE = "../DATA/distributions.txt";
    /** Ruta de la información de la estantería */
    private static final String ESTANTERIA_FILE = "../DATA/estanteria.txt";

    /**
     * Obtiene un producto a partir de su nombre
     * @param productName Nombre del producto
     * @param productos Lista de productos
     * @return Producto con el nombre especificado, o null si no se encuentra
     */
    private Producto obtenerProductoPorNombre(String productName, List<Producto> productos) {
        for (Producto producto : productos) {
            if (producto.getName().equals(productName)) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Obtiene las distribuciones guardadas. Lee el archivo de distribuciones y crea un mapa con sus contenidos. Incluye las posiciones
     * de los productos y las restricciones de las distribuciones.
     * @param productos Lista de productos
     * @return Mapa de distribuciones
     */
    public Map<String, Distribucion> obtenerDistribuciones(List<Producto> productos) {
        Map<String, Distribucion> distribuciones = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DISTRIBUTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    boolean aproximado = Boolean.parseBoolean(parts[1].trim());
                    Map<Integer, Producto> posiciones = new HashMap<>();
                    Map<Producto, Integer> restricciones = new HashMap<>();

                    // Read positions
                    if (parts.length > 2) {
                        String[] posParts = parts[2].split(";");
                        for (String posPart : posParts) {
                            String[] pos = posPart.split(":");
                            int position = Integer.parseInt(pos[0].trim());
                            String productName = pos[1].trim();
                            Producto producto = obtenerProductoPorNombre(productName, productos);
                            if (producto != null) {
                                posiciones.put(position, producto);
                            }
                        }
                    }

                    // Read restrictions
                    if (parts.length > 3) {
                        String[] restrParts = parts[3].split(";");
                        for (String restrPart : restrParts) {
                            String[] restr = restrPart.split(":");
                            String productName = restr[0].trim();
                            int position = Integer.parseInt(restr[1].trim());
                            Producto producto = obtenerProductoPorNombre(productName, productos);
                            if (producto != null) {
                                restricciones.put(producto, position);
                            }
                        }
                    }

                    Distribucion distribucion = new Distribucion(posiciones);
                    distribucion.setId(id);
                    distribucion.setType(aproximado);
                    distribucion.setRestricciones(restricciones);
                    distribuciones.put(id, distribucion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distribuciones;
    }

    /**
     * Guarda las distribuciones de la sesión actual en los ficheros de texto. Incluye las posiciones de los productos y las restricciones
     * @param distribuciones
     */
    public void guardarDistribuciones(Map<String, Distribucion> distribuciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DISTRIBUTIONS_FILE))) {
            for (Distribucion distribucion : distribuciones.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(distribucion.getId()).append(",");
                sb.append(distribucion.getAproximado()).append(",");

                // Save positions
                for (Map.Entry<Integer, Producto> entry : distribucion.getPosiciones().entrySet()) {
                    sb.append(entry.getKey()).append(":").append(entry.getValue().getName()).append(";");
                }
                sb.append(",");

                // Save restrictions
                for (Map.Entry<Producto, Integer> entry : distribucion.getRestricciones().entrySet()) {
                    sb.append(entry.getKey().getName()).append(":").append(entry.getValue()).append(";");
                }

                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda la información de la estantería (número de estantes y ID de la distribución activa si la hay) en un fichero de texto
     * @param distribucionActiva ID de la distribución activa
     * @param numeroEstantes  Número de estantes
     */
    public void guardarInfoEstanteria(Distribucion distribucionActiva, int numeroEstantes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ESTANTERIA_FILE))) {
            if(distribucionActiva != null) {
                bw.write(distribucionActiva.getId() + "," + numeroEstantes);
            }
            else{
                bw.write("null," + numeroEstantes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la información de la estantería guardada
     * @return Par con el ID de la distribución activa y el número de estantes
     */
    public Pair<String, Integer> obtenerInfoEstanteriaGuardada() {
        try (BufferedReader br = new BufferedReader(new FileReader(ESTANTERIA_FILE))) {
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String distribucionActivaId = parts[0];
                    int numeroEstantes = Integer.parseInt(parts[1]);
                    return new Pair<>(distribucionActivaId, numeroEstantes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>("null", 1); // Devuelve 1 en lugar de 0 si no se encuentra información
    }
}