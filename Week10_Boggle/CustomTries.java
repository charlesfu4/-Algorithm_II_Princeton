/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 30/11/2020
 *  Description: Trie26 for Boggle Game Solver
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class CustomTries {
    private static final int R = 26;        // A-Z

    private Node root;      // root of trie
    private int n;          // number of keys in trie


    // 26-way trie node
    static class Node {
        int depth;         // depth of current node
        Node[] next = new Node[R];
        boolean isWord;
    }

    public CustomTries() {
    }

    public Node getRoot() {
        return root;
    }

    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
            x.depth = d;
        }
        if (d == key.length()) {
            if (!x.isWord) n++;
            x.isWord = true;
        } else {
            char c = (char) (key.charAt(d) - 65);
            x.next[c] = add(x.next[c], key, d + 1);
        }
        return x;
    }

    public boolean contains(Node n, String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(n, key);
        if (x == null) return false;
        return x.isWord;
    }

    private Node get(Node x, String key) {
        if (x == null) return null;
        if (x.depth == key.length()) return x;
        char c = (char) (key.charAt(x.depth) - 65);
        return get(x.next[c], key);
    }

    public Node nodewithPrefix(Node n, String prefix) {
        if (prefix == null) throw new IllegalArgumentException();
        if (n == null) return null;
        if (n.depth == prefix.length()) return n;
        char c = (char) (prefix.charAt(n.depth) - 65);
        return nodewithPrefix(n.next[c], prefix);
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static void main(String[] args) {
        TrieSET set = new TrieSET();
        In in = new In(args[0]);

        String[] dictionary = in.readAllStrings();
        CustomTries dict = new CustomTries();
        for (String word : dictionary) {
            dict.add(word);
            set.add(word);
        }
        StdOut.println(dict.size());
        StdOut.println(set.size());

    }
}
