package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;

    private HashMap<T, Integer> mapIndex;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        mapIndex = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> elementA = items.get(a);
        PriorityNode<T> elementB = items.get(b);
        items.set(a, elementB);
        items.set(b, elementA);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException("item already exists");
        }

        PriorityNode<T> node = new PriorityNode<>(item, priority);
        mapIndex.put(item, items.size()); // for contains method
        if (items.size() == 0) {
            items.add(node);
        } else {
            int parentIndex = (items.size() - 1) / 2;
            items.add(node);
            int currentIndex = items.size() - 1;
            while (items.get(parentIndex).getPriority() > items.get(currentIndex).getPriority()) {
                T parentItem = items.get(parentIndex).getItem();
                //T currentItem = items.get(currentIndex).getItem();
                swap(currentIndex, parentIndex);
                mapIndex.put(parentItem, currentIndex);
                mapIndex.put(item, parentIndex);
                currentIndex = parentIndex;
                parentIndex = (parentIndex - 1) / 2;
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return mapIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (items.size() < 1) {
            throw new NoSuchElementException();
        }
        return items.get(0).getItem();
    }

    @Override
    public T removeMin() {
        if (items.size() == 0) {
            throw new NoSuchElementException();
        }
        T min;
        swap(items.size() - 1, 0);
        min = items.remove(items.size() - 1).getItem();
        mapIndex.remove(min);

        int currentIndex = 0;
        int childIndex;
        int leftChildIndex = 2 * currentIndex + 1;
        int rightChildIndex = 2 * currentIndex + 2;
        if (rightChildIndex < items.size()) {
            if (items.get(rightChildIndex).getPriority() == items.get(leftChildIndex).getPriority()) {
                if ((int) items.get(rightChildIndex).getItem() < (int) items.get(leftChildIndex).getItem()) {
                    childIndex = leftChildIndex;
                } else {
                    childIndex = rightChildIndex;
                }
            } else if (items.get(rightChildIndex).getPriority() > items.get(leftChildIndex).getPriority()) {
                childIndex = leftChildIndex;
            } else {
                childIndex = rightChildIndex;
            }
        } else {
            childIndex = leftChildIndex;
        }

        while (childIndex < items.size() && items.get(currentIndex).getPriority() >
            items.get(childIndex).getPriority()) {
            T currentItem = items.get(currentIndex).getItem();
            T childItem = items.get(childIndex).getItem();
            swap(currentIndex, childIndex);
            mapIndex.put(childItem, currentIndex);
            mapIndex.put(currentItem, childIndex);
            currentIndex = childIndex;
            leftChildIndex = 2 * currentIndex + 1;
            rightChildIndex = 2 * currentIndex + 2;
            if (rightChildIndex < items.size()) {
                if (items.get(rightChildIndex).getPriority() == items.get(leftChildIndex).getPriority()) {
                    if ((int) items.get(rightChildIndex).getItem() < (int) items.get(leftChildIndex).getItem()) {
                        childIndex = leftChildIndex;
                    } else {
                        childIndex = rightChildIndex;
                    }
                } else if (items.get(rightChildIndex).getPriority() > items.get(leftChildIndex).getPriority()) {
                    childIndex = leftChildIndex;
                } else {
                    childIndex = rightChildIndex;
                }
            } else {
                childIndex = leftChildIndex;
            }
        }
        return min;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }

        int currentIndex = mapIndex.get(item);
        items.get(currentIndex).setPriority(priority);
        int parentIndex = (currentIndex - 1) / 2;
        int leftChildIndex = 2 * currentIndex + 1;
        int rightChildIndex = 2 * currentIndex + 2;

        while (items.get(parentIndex).getPriority() > items.get(currentIndex).getPriority()) {
            T parentItem = items.get(parentIndex).getItem();
            T currentItem = items.get(currentIndex).getItem();
            swap(currentIndex, parentIndex);
            mapIndex.put(parentItem, currentIndex);
            mapIndex.put(currentItem, parentIndex);
            currentIndex = parentIndex;
            parentIndex = (parentIndex - 1) / 2;
        }

        int childIndex;

        if (rightChildIndex < items.size()) {
            if (items.get(rightChildIndex).getPriority() == items.get(leftChildIndex).getPriority()) {
                if ((int) items.get(rightChildIndex).getItem() < (int) items.get(leftChildIndex).getItem()) {
                    childIndex = leftChildIndex;
                } else {
                    childIndex = rightChildIndex;
                }
            } else if (items.get(rightChildIndex).getPriority() > items.get(leftChildIndex).getPriority()) {
                childIndex = leftChildIndex;
            } else {
                childIndex = rightChildIndex;
            }
        } else {
            childIndex = leftChildIndex;
        }

        while (childIndex < size() && items.get(currentIndex).getPriority() > items.get(childIndex).getPriority()) {
            T childItem = items.get(childIndex).getItem();
            T currentItem = items.get(currentIndex).getItem();
            swap(currentIndex, childIndex);
            mapIndex.put(childItem, currentIndex);
            mapIndex.put(currentItem, childIndex);
            currentIndex = childIndex;

            leftChildIndex = 2 * currentIndex + 1;
            rightChildIndex = 2 * currentIndex + 2;

            if (rightChildIndex < items.size()) {
                if (items.get(rightChildIndex).getPriority() == items.get(leftChildIndex).getPriority()) {
                    if ((int) items.get(rightChildIndex).getItem() < (int) items.get(leftChildIndex).getItem()) {
                        childIndex = leftChildIndex;
                    } else {
                        childIndex = rightChildIndex;
                    }
                } else if (items.get(rightChildIndex).getPriority() > items.get(leftChildIndex).getPriority()) {
                    childIndex = leftChildIndex;
                } else {
                    childIndex = rightChildIndex;
                }
            } else {
                childIndex = leftChildIndex;
            }
        }
    }

    @Override
    public int size() {
        return items.size();
    }
}
