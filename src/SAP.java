import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import edu.princeton.cs.algs4.Queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SAP {
    private Digraph G;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (outOfRange(v) || outOfRange(w)) throw new IllegalArgumentException();

        boolean sameAncestor = false;
        BreadthFirstDirectedPaths bV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bW = new BreadthFirstDirectedPaths(G, w);
        Queue<Integer> qV = new Queue<>();
        boolean[] markedV = new boolean[G.V()];
        bfs(markedV, qV, v);
        int length = Integer.MAX_VALUE;
        for (int ancestor: qV) {
            if (bW.hasPathTo(ancestor)) {
                sameAncestor = true;
                if (bW.distTo(ancestor) + bV.distTo(ancestor) < length) length = bW.distTo(ancestor) + bV.distTo(ancestor);
            }
        }

        if (!sameAncestor) return -1;
        return length;
    }

    private void bfs(boolean[] marked, Queue<Integer> qV, int v) {
        Queue<Integer> q = new Queue<>();
        q.enqueue(v);
        qV.enqueue(v);
        marked[v] = true;
        while(!q.isEmpty()) {
            int temp = q.dequeue();
            for (int adj: G.adj(temp)) {
                if (!marked[adj]) {
                    marked[adj] = true;
                    q.enqueue(adj);
                    qV.enqueue(adj);
                }
            }
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (outOfRange(v) || outOfRange(w)) throw new IllegalArgumentException();
        boolean sameAncestor = false;
        BreadthFirstDirectedPaths bV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bW = new BreadthFirstDirectedPaths(G, w);
        Queue<Integer> qV = new Queue<>();
        boolean[] markedV = new boolean[G.V()];
        bfs(markedV, qV, v);
        int length = Integer.MAX_VALUE;
        int shortestAncestor = 0;
        for (int ancestor: qV) {
            if (bW.hasPathTo(ancestor)) {
                sameAncestor = true;
                if (bW.distTo(ancestor) + bV.distTo(ancestor) < length) {
                    length = bW.distTo(ancestor) + bV.distTo(ancestor);
                    shortestAncestor = ancestor;
                }
            }
        }

        if (!sameAncestor) return -1;
        return shortestAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (containNullItem(v) || containNullItem(w)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bW = new BreadthFirstDirectedPaths(G, w);
        boolean[] markedV = new boolean[G.V()];
        Queue<Integer> qV = new Queue<>();
        bfs(markedV, qV, v);
        boolean hasPathTo = false;
        int length = Integer.MAX_VALUE;
        for (int e: qV) {
            if (bW.hasPathTo(e)) {
                hasPathTo = true;
                if (length > bW.distTo(e) + bV.distTo(e)) length = bW.distTo(e) + bV.distTo(e);
            }
        }
        if (!hasPathTo) return -1;
        return length;
    }

    private void bfs(boolean[] markedV, Queue<Integer> qV, Iterable<Integer> V) {
        Queue<Integer> q = new Queue<>();
        for (int v: V) {
            markedV[v] = true;
            q.enqueue(v);
            qV.enqueue(v);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int adj: G.adj(v)) {
                if (!markedV[adj]) {
                    markedV[adj] = true;
                    q.enqueue(adj);
                    qV.enqueue(adj);
                }
            }
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (containNullItem(v) || containNullItem(w)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bW = new BreadthFirstDirectedPaths(G, w);
        boolean[] markedV = new boolean[G.V()];
        Queue<Integer> qV = new Queue<>();
        bfs(markedV, qV, v);
        boolean hasPathTo = false;
        int length = Integer.MAX_VALUE;
        int root = 0;
        for (int e: qV) {
            if (bW.hasPathTo(e)) {
                hasPathTo = true;
                if (length > bW.distTo(e) + bV.distTo(e)) {
                    length = bW.distTo(e) + bV.distTo(e);
                    root = e;
                }
            }
        }
        if (!hasPathTo) return -1;
        return root;
    }

    // do unit testing of this class
    public static void main(String[] args) {

//        Scanner sc1 = new Scanner(new File("src/digraph1.txt"));
//        int V = sc1.nextInt();
//        Digraph G = new Digraph(V);
//        while(sc1.hasNext()) {
//            int v = sc1.nextInt();
//            int w = sc1.nextInt();
//            G.addEdge(v, w);
//
//        }
//        SAP sap = new SAP(G);
//        Scanner sc = new Scanner(new File("src/testSAP.txt"));
//        while (sc.hasNext()) {
//            int v = sc.nextInt();
//            int w = sc.nextInt();
//            int length   = sap.length(v, w);
//            int ancestor = sap.ancestor(v, w);
//            System.out.println(v + " - " + w);
//            System.out.println("length = " + length +" ancestor = " + ancestor);
//        }
    }

    private boolean outOfRange(int v) {
        return v < 0 || v >= G.V();
    }

    private boolean containNullItem(Iterable<Integer> v) {
        for(Integer i: v) {
            if (i == null) return true;
        }
        return false;
    }
}