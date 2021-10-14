package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private int size;
    private int currentCapacity;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        this.size = 0;
        this.currentCapacity = initialCapacity;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                return entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        V val = null;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
               val = entries[i].getValue();
               entries[i].setValue(value);
               return val;
            }
        }
        if (size == currentCapacity) {
            SimpleEntry<K, V>[] doubleEntries = this.createArrayOfEntries(currentCapacity * 2);
            currentCapacity = currentCapacity * 2;
            if (size >= 0) {
                System.arraycopy(entries, 0, doubleEntries, 0, size);
            }
            entries = doubleEntries;
        }
        entries[size] = new SimpleEntry<>(key, value);
        size++;
        return val;
    }

    @Override
    public V remove(Object key) {
        V val;
        // if (size != 0) {
        //     if (size == 1 && Objects.equals(entries[0].getKey(), key)) {
        //         val = entries[0].getValue();
        //         size--;
        //     } else if (Objects.equals(entries[size - 1].getKey(), key)) {
        //         val = entries[size - 1].getValue();
        //         size--;
        //     } else {
        //         for (int i = 0; i < size; i++) {
        //             if (Objects.equals(entries[i].getKey(), key)) {
        //                 val = entries[i].getValue();
        //                 entries[i] = entries[size - 1];
        //                 size--;
        //                 return val;
        //             }
        //         }
        //     }
        // }
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                val = entries[i].getValue();
                entries[i] = entries[size - 1];
                size--;
                return val;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(entries[i].getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ArrayMapIterator<>(this.entries, this.size, 0);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private final int size;
        private int index;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size, int index) {
            this.entries = entries;
            this.size = size;
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            Map.Entry<K, V> entry;
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                entry = entries[index];
                index++;
            }
            return entry;
        }
    }
}
