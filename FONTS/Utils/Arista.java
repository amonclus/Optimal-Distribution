package FONTS.Utils;

import FONTS.Domini.Producto;

/**
 * Clase que representa una arista de un grafo, con un destino y un peso asociado.
 */
public class Arista {
    /** Producto destino de la arista */
    private Producto destino;
    /** Peso de la arista */
    private Double peso;

    /**
     * Constructor de la clase
     * @param destino Producto destino de la arista
     * @param peso Peso de la arista
     */
    public  Arista(Producto destino, Double peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Producto getDestino() {
        return destino;
    }

    public Double getPeso() {
        return peso;
    }

}