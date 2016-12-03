import java.util.Comparator;
import java.util.List;

/**
 * Heap Sort implementation
 */
public class HeapSort<T> {

    // sort this unsorted list with heap sort
    public List<T> heapSort(List<T> list, Comparator<T> comp) {
        this.buildHeap(list, comp);
        this.heapOrder(list, comp);
        return list;
    }

    // build a heap from an arraylist
    void buildHeap(List<T> list, Comparator<T> comp) {
        for (int n = list.size() / 2 - 1; n >= 0; n--) {
            downHeap(n, list, comp, list.size() - 1);
        }
    }

    // swap two elements in a list
    void swap(List<T> list, int i1, int i2) {
        T temp = list.get(i1);
        list.set(i1, list.get(i2));
        list.set(i2, temp);
    }

    // downHeap an element in a list
    // e represents last element we would like to downheap too
    void downHeap(int i, List<T> list, Comparator<T> comp, int e) {

        int leftChild = i * 2 + 1;
        int rightChild = i * 2 + 2;
        while (leftChild <= e) {
            if (rightChild <= e) {
                if (comp.compare(list.get(i), list.get(leftChild)) >= 0
                        && comp.compare(list.get(i), list.get(rightChild)) >= 0) {
                    return;
                }
                if (comp.compare(list.get(i), list.get(leftChild)) < 0
                        && comp.compare(list.get(i), list.get(rightChild)) < 0) {

                    if (comp.compare(list.get(rightChild), list.get(leftChild)) > 0) {
                        this.swap(list, i, rightChild);
                        i = rightChild;
                    } else {
                        this.swap(list, i, leftChild);
                        i = leftChild;
                    }
                } else if (comp.compare(list.get(i), list.get(leftChild)) < 0) {
                    this.swap(list, i, leftChild);
                    i = leftChild;
                } else {
                    this.swap(list, i, rightChild);
                    i = rightChild;
                }
            } else if (leftChild == e) {
                if (comp.compare(list.get(i), list.get(leftChild)) >= 0) {
                    return;
                } else {
                    this.swap(list, i, leftChild);
                    i = leftChild;
                }
            }
            leftChild = i * 2 + 1;
            rightChild = i * 2 + 2;
        }
    }

    // takes a heap, mutates to sorted list
    void heapOrder(List<T> list, Comparator<T> comp) {
        for (int n = 0; n < list.size() - 1; n++) {
            this.swap(list, 0, list.size() - n - 1);
            this.downHeap(0, list, comp, list.size() - 2 - n);
        }
    }
}

class CompareEdges implements Comparator<Edge> {

    // Compare edge1 to edge 2
    public int compare(Edge e1, Edge e2) {
        if (e1.weight < e2.weight) {
            return -1;
        } else if (e1.weight == e2.weight) {
            return 0;
        } else {
            return 1;
        }
    }
}
