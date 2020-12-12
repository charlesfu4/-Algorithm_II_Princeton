/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 07-12-2020
 *  Description: Burrows-Wheeler Transformation
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        int len = s.length();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i = 0; i < len; i++) {
            if (csa.index(i) == 0)
                BinaryStdOut.write(i);
        }
        for (int i = 0; i < len; i++)
            if (csa.index(i) == 0)
                BinaryStdOut.write(s.charAt(len - 1));
            else
                BinaryStdOut.write(s.charAt(csa.index(i) - 1));
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] s = BinaryStdIn.readString().toCharArray();
        int len = s.length;
        char[] aux = new char[len];
        int[] next = new int[len];

        int[] count = new int[256 + 1];

        // Now counting sort and accumulate
        for (int i = 0; i < len; i++)
            count[s[i] + 1]++;

        for (int r = 0; r < 256; r++)
            count[r + 1] += count[r];
        // When print out the position after sorted
        // we increase count
        for (int i = 0; i < len; i++) {
            next[count[s[i]]] = i;
            aux[count[s[i]]++] = s[i];
        }

        int c = 0;
        while (c < len) {
            BinaryStdOut.write(aux[first]);
            first = next[first];
            c++;
        }
        BinaryStdOut.flush();
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform

    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");

    }
}
