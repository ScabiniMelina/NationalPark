package model.util;

import java.util.HashMap;
import java.util.Map;

public class UnionFind<T> {
    private final Map<T, T> parent = new HashMap<>();

    public void add(T item) {
        parent.putIfAbsent(item, item);
    }

    public T find(T item) {
        if (!parent.containsKey(item)) {
            throw new IllegalArgumentException("Elemento no registrado: " + item);
        }

        T root = parent.get(item);
        if (!root.equals(item)) {
            root = find(root);
            parent.put(item, root); // path compression
        }
        return root;
    }

    public void union(T a, T b) {
        T rootA = find(a);
        T rootB = find(b);
        if (!rootA.equals(rootB)) {
            parent.put(rootA, rootB);
        }
    }

    public boolean connected(T a, T b) {
        return find(a).equals(find(b));
    }
}
