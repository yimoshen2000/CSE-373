package disjointsets;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {

    List<Integer> pointers;
    private final HashMap<T, Integer> ids;

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>();
        this.ids = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (ids.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        ids.put(item, ids.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!ids.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        List<Integer> pathWay = new ArrayList<>();

        int index = ids.get(item);

        while (pointers.get(index) >= 0) {
            pathWay.add(index);
            index = pointers.get(index);
        }

        for (int i : pathWay) {
            pointers.set(i, index);
        }

        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!ids.containsKey(item1) || !ids.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int indexOne = findSet(item1);
        int indexTwo = findSet(item2);

        if (indexOne == indexTwo) {
            return false;
        }

        if (-1 * pointers.get(indexOne) < -1 * pointers.get(indexTwo)) {
            int weightOne = pointers.get(indexOne);
            pointers.set(indexOne, indexTwo);
            pointers.set(indexTwo, weightOne + pointers.get(indexTwo));
        } else {
            int weightTwo = pointers.get(indexTwo);
            pointers.set(indexTwo, indexOne);
            pointers.set(indexOne, weightTwo + pointers.get(indexOne));
        }

        return true;
    }
}
