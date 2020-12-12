/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 07-12-2020
 *  Description: Circular Suffix for Array
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int len;
    private CircularSuffix cs;


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        len = s.length();
        cs = new CircularSuffix(s);
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > len - 1) throw new IllegalArgumentException();
        return cs.index(i);
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        CircularSuffixArray csa = new CircularSuffixArray(in.readAll());
        for (int i = 0; i < csa.length(); i++) {
            StdOut.println(csa.index(i));
        }
    }
}
