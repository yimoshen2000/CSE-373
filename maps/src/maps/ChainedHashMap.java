package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1.0;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 5;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 10;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int mapCount;
    private final double loadThreshold;

    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.chains = this.createArrayOfChains(initialChainCount);
        for (int i = 0; i < chains.length; i++) {
            chains[i] = createChain(chainInitialCapacity);
        }
        this.loadThreshold = resizingLoadFactorThreshold;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % chains.length;
        }
        for (int i = 0; i < chains.length; i++) {
            if (i == index) {
                for (Entry<K, V> currentMap : chains[i]) {
                    if (Objects.equals(currentMap.getKey(), key)) {
                        return currentMap.getValue();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % this.chains.length;
        }

        if (chains[index] == null) {
            chains[index] = new ArrayMap<>();
        }
        if (!chains[index].containsKey(key)) {
            mapCount++;
            if ((double) mapCount / chains.length >= loadThreshold) {
                resizeArrayOfChains();
            }
        }
        return chains[index].put(key, value);
    }

    private void resizeArrayOfChains() {
        AbstractIterableMap<K, V>[] newChains = createArrayOfChains(chains.length * 2);
        System.arraycopy(chains, 0, newChains, 0, chains.length);
        for (Entry<K, V> itr: this) {
            int index = 0;
            if (itr.getKey() != null) {
                index = Math.abs(itr.getKey().hashCode()) % newChains.length;
            }
            if (newChains[index] == null) {
                 newChains[index] = new ArrayMap<>();
            } else {
                newChains[index].put(itr.getKey(), itr.getValue());
            }
        }
        this.chains = newChains;
    }

    @Override
    public V remove(Object key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % this.chains.length;
        }
        if (containsKey(key)) {
            mapCount--;
        }
        return chains[index].remove(key);
    }

    @Override
    public void clear() {
        mapCount = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % this.chains.length;
        }
        return chains[index].containsKey(key);
    }

    @Override
    public int size() {
        return this.mapCount;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains, 0);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private int index;
        private Iterator<Map.Entry<K, V>> currentChain;


        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains, int index) {
            this.chains = chains;
            this.index = index;
            if (chains[index] != null) {
                currentChain = chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = index; i < chains.length; i++) {
                if (currentChain != null && currentChain.hasNext()) {
                    return true;
                }
                index++;
                if (index == chains.length - 1) {
                    return false;
                }
                if (chains[i] != null) {
                    currentChain = chains[i].iterator();
                    // index = i;
                } else {
                    currentChain = null;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                return currentChain.next();
            }
        }
    }
}
