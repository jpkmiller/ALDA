package dictionary;

import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private int key;
    private int size;
    private int load;
    LinkedList<Entry<K, V>>[] dict;

    HashDictionary(int key, int load) {
        this.dict = new LinkedList[load];
        this.key = key;
        this.load = load;
        this.size = 0;
    }

    /**
     * Generiert einen Hashcode und gibt die passende Zeilennummer für das Array aus verketteten Listen zurück
     *
     * @param k der Key, der zu Beginn übergeben wird
     * @return Zeilennummer von Array
     */
    private int genHash(K k) {
        String key;

        if (!(k instanceof String))
            key = String.valueOf(k);
        else
            key = (String) k;

        int adr = 0;
        for (int i = 0; i < key.length(); i++)
            adr = this.key * adr + key.charAt(i);

        if (adr < 0)
            adr = -adr;

        //System.out.printf("TabellenNr. %d%n", adr % (dict.length - 1));
        return adr % (dict.length - 1);
    }

    private static boolean isprime(int n) {
        for (int i = 2; i * i <= n; i++)
            if (n % i == 0)
                return false;
        return true;
    }

    private static int getprime(int i) {
        while (true) {
            if (isprime(++i)) break;
        }
        return i;
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
        int i = genHash(key);
        if (search(key) == null) {
            if (dict[i] == null) //Überprüfen, ob Liste schon vorhanden
                dict[i] = new LinkedList<>(); //Bevor etwas hinzugefügt werden kann, muss die LinkedList erst initialisiert werden!!
            dict[i].add(new Entry<>(key, value));
            size++;
            return null;
        }
        for (Entry<K, V> e : dict[i])
            if (e.getKey().equals(key)) {
                V old = e.getValue();
                e.setValue(value);
                return old;
            }
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
        int i = genHash(key);
        if (dict[i] == null)
            return null;
        else
            for (Entry<K, V> e : dict[i])
                if (e.getKey().equals(key))
                    return e.getValue();
        return null;
    }

    /**
     * ensuring the capacity of the HashDictionary
     * if not enough space -> enlarge space
     */
    private void ensurecapacity() {
        HashDictionary<K, V> backup;
        if (this.size >= load) {
            load = getprime((int) (load * 1.2));
            backup = new HashDictionary<>(3, load);
            for (Dictionary.Entry<K, V> e : this)
                backup.insert(e.getKey(), e.getValue());
            dict = backup.dict;
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
        int i = genHash(key);

        for (Entry<K, V> e : dict[i])
            if (e.getKey().equals(key)) {
                V old = e.getValue();
                dict[i].remove(e);
                return old;
            }
        return null;
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

            int index = -1;
            Iterator<Entry<K, V>> listIterator;

            @Override
            public boolean hasNext() {
                if (listIterator != null && listIterator.hasNext())
                    return true;
                while (++index < dict.length) {
                    if (dict[index] != null) {
                        listIterator = dict[index].iterator();
                        return listIterator.hasNext();
                    }
                }
                return false;
            }

            @Override
            public Entry<K, V> next() {
                return listIterator.next();
            }
        };
    }
}
