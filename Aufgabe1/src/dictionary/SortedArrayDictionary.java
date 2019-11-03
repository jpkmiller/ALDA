package dictionary;

import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 32; //Maximale Kapazität des Feldes
    private Entry<K, V>[] dict; //ist nach dem  key sortiert
    private int size; //gibt die Länge des Feldes an

    SortedArrayDictionary() {
        this.dict = new Entry[DEF_CAPACITY];
        this.size = 0;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced by the specified value.
     * Returns the previous value associated with key,
     * or null if there was no mapping for key.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public V insert(K key, V value) {
        ensurecapacity();
        for (int i = 0; i < size; i++) {
            if (dict[i].getKey().equals(key)) {
                V old = dict[i].getValue();
                dict[i].setValue(value);
                return old;
            }
        }

        //Element einfügen und Feld neu sortieren
        int i = size - 1;
        while (i >= 0 && dict[i].getKey().compareTo(key) < 0) {
            dict[i + 1] = dict[i];
            i--;
        }

        dict[i + 1] = new Entry<>(key, value);
        size++;

        return null;
    }


    /**
     * Returns the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     */
    @Override
    public V search(K key) {
        int li = 0;
        int re = size - 1;

        while (re >= li) {
            int m = (li + re) / 2;
            if (key.compareTo(dict[m].getKey()) > 0) {
                re = m - 1;
            } else if (key.compareTo(dict[m].getKey()) < 0) {
                li = m + 1;
            } else {
                return dict[m].getValue();
            }
        }
        return null;
    }

    private void ensurecapacity() {
        if (dict.length <= this.size()) {
            //System.out.printf("Enlarging array by %d\n", this.size() * 2);
            Entry[] old = new Entry[this.size() * 2];
            System.arraycopy(dict, 0, old, 0, dict.length);
            dict = old;
        }
    }

    /**
     * Removes the key-value-pair associated with the key.
     * Returns the value to which the key was previously associated,
     * or null if the key is not contained in the dictionary.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public V remove(K key) {
        if (search(key) == null) return null;

        int m = 0, li = 0, re = size - 1;

        while (li <= re) {
            m = (re + li) / 2;
            if (dict[m].getKey().compareTo(key) > 0) {
                li = m + 1; //suche im linken Teil des Feldes
            } else if (dict[m].getKey().compareTo(key) < 0) {
                re = m - 1; //suche im rechten Teil des Feldes
            } else {
                break; //key gefunden
            }
        }

        V old = dict[m].getValue();

        for (int i = m; i < size - 1; i++)
            dict[i] = dict[i + 1];
        dict[--size] = null;
        return old;
    }

    /**
     * Returns the number of elements in this dictionary.
     *
     * @return the number of elements in this dictionary.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns an iterator over the entries in this dictionary.
     * There are no guarantees concerning the order in which the elements are returned
     * (unless this dictionary is an instance of some class that provides a guarantee).
     *
     * @return an Iterator over the entries in this dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return dict[index] != null;
            }

            @Override
            public Entry<K, V> next() {
                return dict[index++];
            }
        };
    }
}
