package FONTS.Domini;

import java.util.ArrayList;

/**
 * Clase que representa un producto. Cada producto tiene un nombre, un tipo y una lista de atributos.
 */
public class Producto {
    /** Nombre del producto, identificador único */
    private String name;
    /** Tipo del producto */
    private String type;
    /** Lista de atributos del producto */
    private ArrayList<String> attributes;

    /**
     * Constructor de la clase
     * @param name Nombre del producto
     * @param type Tipo del producto
     * @param attributes Lista de atributos del producto
     */
    public Producto(String name, String type, ArrayList<String> attributes) {
        this.name = name;
        this.type = type;
        this.attributes = attributes;
    }

    /**
     * Devuelve una representación en String del producto de la forma: "Producto [Nombre=nombre, Tipo=tipo, Atributos=atributos]"
     * @return String con la representación del producto
     */
    @Override
    public String toString() {
        StringBuilder attributesString = new StringBuilder();
        if (attributes != null) {
            for (String attribute : attributes) {
                attributesString.append(attribute).append(" ");
            }
        }
        return "Producto [Nombre=" + name + ", Tipo=" + type + ", Atributos=" + attributesString.toString().trim() + "]";
    }

    // Getters y Setters

    public String getName() {
        return name;
    }

    public String getType() {return type;}

    public ArrayList<String> getAttributes() {return attributes;}

    public void setName(String name) {this.name = name;}

    public void setType(String type) {this.type = type;}

    public void setAttributes(ArrayList<String> attributes) {this.attributes = attributes;}
}


