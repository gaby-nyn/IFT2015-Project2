package Project2;

import java.util.Comparator;

public class PQ implements Comparator<Event> {

    private Event[] Heap;
    private int size;
    private int maxsize;

    private static final int FRONT = 1;

    public PQ(int maxsize)
    {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new Event[maxsize];
    }
    private int parent(int pos)
    {
        return pos / 2;
    }
    private int leftChild(int pos)
    {
        return (2 * pos);
    }
    private int rightChild(int pos)
    {
        return (2 * pos) + 1;
    }
    private boolean isLeaf(int pos)
    {
        if (pos >= (size / 2) && pos <= size) {
            return true;
        }
        return false;
    }
    private void swap(int fpos, int spos)
    {
        Event tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }
    private void minHeapify(int pos)
    {
        if (pos <= Heap.length) {
            if (!isLeaf(pos)) {
                if (Heap[pos].time > Heap[leftChild(pos)].time
                        || Heap[pos].time > Heap[rightChild(pos)].time) {

                    if (Heap[leftChild(pos)].time < Heap[rightChild(pos)].time) {
                        swap(pos, leftChild(pos));
                        minHeapify(leftChild(pos));
                    } else {
                        swap(pos, rightChild(pos));
                        minHeapify(rightChild(pos));
                    }
                }
            }
        }
    }

    public void insert(Event element)
    {
        if (size == maxsize-1) {
            Event[] tmp = Heap;
            maxsize += maxsize;
            Heap = new Event[maxsize];
            for(int i = 0; i < tmp.length; i++) {
                Heap[i] = tmp[i];
            }
        }

        Heap[++size] = element;
        int current = size;
        if (Heap[current].time != 0) {
            while (compare(Heap[current], Heap[parent(current)]) == -1) {
                swap(current, parent(current));
                current = parent(current);
                if (Heap[parent(current)] == null) {
                    break;
                }
            }
        }
    }
    public Event deleteMin()
    {
        Event popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        minHeapify(FRONT);
        return popped;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int compare(Event o1, Event o2) {
        if (o1.time < o2.time) {
            return -1;
        }
        else if (o1.time > o2.time) {
            return 1;
        }
        return 0;
    }
}
