/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 07-12-2020
 *  Description: Move to front operation
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // Initialize sequence with 256 ASCII
        StringBuilder R = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            R.append((char) i);
        }
        // Read and Write by move to front operation
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int idx = R.toString().indexOf(c);
            BinaryStdOut.write((char) (idx & 0xff));
            R.deleteCharAt(idx);
            R.insert(0, c);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // Initialize sequence with 256 ASCII
        StringBuilder R = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            R.append((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char i = R.charAt(c);
            BinaryStdOut.write(i);
            R.deleteCharAt(R.toString().indexOf(i));
            R.insert(0, i);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
