package dictionary;

import java.util.Iterator;

public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {
    private Node<K, V> root;

    private int size = 0;
    private V oldValue;

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
     * @param p is a Node whose height is returned
     * @return height
     */
    private int getHeight(Node<K, V> p) {
        if (p == null)
            return -1;
        return p.height;
    }

    /**
     * @param p is a Node whose balance is returned
     * @return balance
     */
    private int getBalance(Node<K, V> p) {
        if (p == null) return 0;
        return getBalance(p.right) - getBalance(p.left);
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
        root = insertR(new Entry<>(key, value), root);
        if (root != null)
            root.parent = null;
        return oldValue;
    }

    private Node<K, V> insertR(Entry<K, V> e, Node<K, V> p) {
        if (p == null) {
            //insert new Entry
            p = new Node<>(e);
            oldValue = null;
            size++;
        } else if (e.getKey().compareTo(p.e.getKey()) < 0) {
            //goto left side of tree because our key is smaller than the parent key
            p.left = insertR(e, p.left);
            if (p.left != null)
                p.left.parent = p;
        } else if (e.getKey().compareTo(p.e.getKey()) > 0) {
            //goto right side of tree because our key is smaller than the parent key
            p.right = insertR(e, p.right);
            if (p.right != null)
                p.right.parent = p;
        } else {
            oldValue = p.e.getValue();
            p.e.setValue(e.getValue());
        }
        p = balance(p);
        return p;
    }

    private Node<K, V> balance(Node<K, V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p); //A1
            else
                p = rotateLeftRight(p); //A2
        } else if (getBalance(p) == 2) {
            if (getBalance(p.right) > 0)
                p = rotateLeft(p); //B1
            else
                p = rotateRightLeft(p); //B2
        }
        return p;
    }

    private Node<K, V> rotateRight(Node<K, V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        q.parent = p.parent;
        p.parent = q;
        p.left = q.right;
        q.right = p;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(p.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K, V> rotateLeft(Node<K, V> p) {
        assert p.right != null;
        Node<K, V> q = p.right;
        q.parent = p.parent;
        p.parent = q;
        p.right = q.left;
        q.left = p;
        p.height = Math.max(getHeight(p.right), getHeight(p.left)) + 1;
        q.height = Math.max(getHeight(p.right), getHeight(q.left)) + 1;
        return q;
    }

    private Node<K, V> rotateRightLeft(Node<K, V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }

    private Node<K, V> rotateLeftRight(Node<K, V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        return rotateRight(p);
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
        return searchR(key, root);
    }

    private V searchR(K key, Node<K, V> p) {
        if (p == null)
            return null;
        else if (key.compareTo(p.e.getKey()) < 0)
            return searchR(key, p.left);
        else if (key.compareTo(p.e.getKey()) > 0)
            return searchR(key, p.right);
        else
            return p.e.getValue();
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
        root = removeR(key, root);
        return oldValue;
    }

    private Node<K, V> removeR(K key, Node<K, V> p) {
        if (p == null) {
            size--;
            oldValue = null;
        } else if (key.compareTo(p.e.getKey()) < 0)
            p.left = removeR(key, p.left);
        else if (key.compareTo(p.e.getKey()) > 0)
            p.right = removeR(key, p.right);
        else if (p.left == null || p.right == null) {
            oldValue = p.e.getValue();
            p = (p.left != null) ? p.left : p.right;
        } else {
            MinEntry<K, V> min = new MinEntry<K, V>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.e.getValue();
            p.e = new Entry<>(min.key, min.value);
        }
        return p;
    }

    private Node<K, V> getRemMinR(Node<K, V> p, MinEntry<K, V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.e.getKey();
            min.value = p.e.getValue();
            p = p.right;
        } else
            p.left = getRemMinR(p.left, min);
        return p;
    }

    private Node<K, V> leftMostDescendant(Node<K, V> p) {
        assert p != null;
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K, V> parentOfLeftMostAncestor(Node<K, V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent;
    }

    void prettyPrint() {
        prettyPrintR(root, 10);
    }

    private void prettyPrintR(Node<K, V> head, int height) {
        if (head == null)
            return;
        StringBuilder leer = new StringBuilder();
        leer.append("     ".repeat(Math.max(0, height)));
        System.out.printf("%s%s", leer, head.e.getKey());
        if (head.parent != null)
            System.out.printf(" p: %s", head.parent.e.getKey());
        System.out.print("\n");
        prettyPrintR(head.left, height - 2);
        prettyPrintR(head.right, height + 2);
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

            Node<K, V> head;

            @Override
            public boolean hasNext() {
                if (head == null && root != null)
                    head = leftMostDescendant(root);

                assert head != null;
                if (head.right != null)
                    head = leftMostDescendant(head.right);
                else
                    head = parentOfLeftMostAncestor(head);
                return head != null;
            }

            @Override
            public Entry<K, V> next() {
                return head.e;
            }
        };
    }


    static private class Node<K extends Comparable<? super K>, V> {
        Node<K, V> right;
        Node<K, V> left;
        int height;
        Entry<K, V> e;
        private Node<K, V> parent; //Elternzeiger

        Node(Entry<K, V> e) {
            height = 0;
            this.e = e;
            left = null;
            right = null;
            parent = null;
        }
    }

    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }
}
