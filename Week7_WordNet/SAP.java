/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 10-27-2020
 *  Description: SAP data type
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private Digraph wordnet;
    private BreadthFirstDirectedPaths bfs_path_v;
    private BreadthFirstDirectedPaths bfs_path_w;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.wordnet = new Digraph(G); // a defensive copy
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > wordnet.V() || w < 0 || w > wordnet.V())
            throw new IllegalArgumentException();
        Bag<Integer> tempv = new Bag<Integer>();
        int distance = Integer.MAX_VALUE;

        // O(V + E)
        bfs_path_v = new BreadthFirstDirectedPaths(wordnet, v);
        bfs_path_w = new BreadthFirstDirectedPaths(wordnet, w);
        // O(V)
        for (int ver = 0; ver < wordnet.V(); ver++) {
            if (bfs_path_v.hasPathTo(ver)) tempv.add(ver);
        }
        // Loop through reachable vertices from v
        // and check if w can also reach them
        // compare distance
        // O(V)
        for (int ver : tempv) {
            if (bfs_path_w.hasPathTo(ver)) {
                if (bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver) <= distance)
                    distance = bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver);
            }
        }
        if (distance == Integer.MAX_VALUE)
            return -1;
        return distance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > wordnet.V() || w < 0 || w > wordnet.V())
            throw new IllegalArgumentException();
        Bag<Integer> tempv = new Bag<Integer>();
        int distance = Integer.MAX_VALUE;
        int common_v = 0;

        // O(V + E)
        bfs_path_v = new BreadthFirstDirectedPaths(wordnet, v);
        bfs_path_w = new BreadthFirstDirectedPaths(wordnet, w);
        // O(V)
        for (int ver = 0; ver < wordnet.V(); ver++) {
            if (bfs_path_v.hasPathTo(ver)) tempv.add(ver);
        }
        // Loop through reachable vertices from v
        // and check if w can also reach them
        // compare distance
        // O(V)
        for (int ver : tempv) {
            if (bfs_path_w.hasPathTo(ver)) {
                if (bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver) <= distance) {
                    common_v = ver;
                    distance = bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver);
                }
            }
        }
        if (distance == Integer.MAX_VALUE)
            return -1;
        return common_v;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        for (Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
        }
        for (Integer i : w) {
            if (i == null)
                throw new IllegalArgumentException();
        }

        Bag<Integer> tempv = new Bag<Integer>();
        int distance = Integer.MAX_VALUE;
        int common_v = 0;

        // O(V + E)
        bfs_path_v = new BreadthFirstDirectedPaths(wordnet, v);
        bfs_path_w = new BreadthFirstDirectedPaths(wordnet, w);
        // O(V)
        for (int ver = 0; ver < wordnet.V(); ver++) {
            if (bfs_path_v.hasPathTo(ver)) tempv.add(ver);
        }
        // Loop through reachable vertices from v
        // and check if w can also reach them
        // compare distance
        // O(V)
        for (int ver : tempv) {
            if (bfs_path_w.hasPathTo(ver)) {
                if (bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver) <= distance) {
                    common_v = ver;
                    distance = bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver);
                }
            }
        }
        if (distance == Integer.MAX_VALUE)
            return -1;
        return distance;

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        for (Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
        }
        for (Integer i : w) {
            if (i == null)
                throw new IllegalArgumentException();
        }

        Bag<Integer> tempv = new Bag<Integer>();
        int distance = Integer.MAX_VALUE;
        int common_v = 0;

        // O(V + E)
        bfs_path_v = new BreadthFirstDirectedPaths(wordnet, v);
        bfs_path_w = new BreadthFirstDirectedPaths(wordnet, w);
        // O(V)
        for (int ver = 0; ver < wordnet.V(); ver++) {
            if (bfs_path_v.hasPathTo(ver)) tempv.add(ver);
        }
        // Loop through reachable vertices from v
        // and check if w can also reach them
        // compare distance
        // O(V)
        for (int ver : tempv) {
            if (bfs_path_w.hasPathTo(ver)) {
                if (bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver) <= distance) {
                    common_v = ver;
                    distance = bfs_path_v.distTo(ver) + bfs_path_w.distTo(ver);
                }
            }
        }
        if (distance == Integer.MAX_VALUE)
            return -1;
        return common_v;

    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}
