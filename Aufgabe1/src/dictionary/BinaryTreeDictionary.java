package dictionary;

import java.util.Iterator;

public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

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
        return null;
    }

    /**
     * Removes the key-vaue-pair associated with the key.
     * Returns the value to which the key was previously associated,
     * or null if the key is not contained in the dictionary.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public V remove(K key) {
        return null;
    }

    /**
     * Returns the number of elements in this dictionary.
     *
     * @return the number of elements in this dictionary.
     */
    @Override
    public int size() {
        return 0;
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
        return null;
    }

    public void prettyPrint() {
    }

    class Node {
        Node right;
        Node left;
        Entry<K, V> e;

        public Node(Entry<K, V> e) {
            this.e = e;
        }
    }
}
