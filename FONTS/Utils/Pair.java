package FONTS.Utils;

/**
 * Clase que representa un par de elementos.
 * @param <K> Tipo del primer elemento
 * @param <V> Tipo del segundo elemento
 */
public class Pair<K, V> {
    private final K key;
    private final V value;

    /**
     * Constructor de la clase
     * @param key Primer elemento
     * @param value Segundo elemento
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}