package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        front = new Node<>(null);
        back  = new Node<>(null);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        if (size == 1) {
            front.next = new Node<>(item, front, back);
            back.prev = front.next;
        } else {
            front.next = new Node<>(item, front, front.next);
            front.next.next.prev = front.next;
            // front = front.next;
        }
    }

    public void addLast(T item) {
        size += 1;
        if (size == 1) {
            back.prev = new Node<>(item, front, back);
            front.next= back.prev;
        } else {
            back.prev = new Node<>(item, back.prev, back);
            back.prev.prev.next = back.prev;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T result;
        result = front.next.value;
        size -= 1;
        front.next.next.prev = null;
        front.next = front.next.next;
        front.next.prev = front;
        return result;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T result;
        result = back.prev.value;
        back.prev.prev.next = null;
        back.prev = back.prev.prev;
        back.prev.next = back;
        return result;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> current;
        if (index < size / 2.0) {
            current = front.next;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = back.prev;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current.value;
    }

    public int size() {
        return size;
    }
}
