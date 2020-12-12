/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 12/12/2020
 *  Description: Revision of SuffixSort to Circular SuffixSort
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffix {

    private final char[] text;
    private final int[] index;   // index[i] = j means text.substring(j) is ith largest suffix
    private final int n;         // number of characters in text

    /**
     * Initializes a suffix array for the given {@code text} string.
     *
     * @param text the input string
     */
    public CircularSuffix(String text) {
        n = text.length();
        StringBuilder strb = new StringBuilder();
        strb.append(text).append(text);
        text = strb.toString();
        this.text = text.toCharArray();
        this.index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i % n;
        sort(0, n - 1, 0);
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) {
        if (hi <= lo) return;

        int lt = lo, gt = hi;
        char v = text[index[lo] + d];
        int i = lo + 1;
        while (i <= gt) {
            char t = text[index[i] + d];
            if (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(lo, lt - 1, d);
        if (d < n) sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    /**
     * Returns the length of the input string.
     *
     * @return the length of the input string
     */
    public int length() {
        return n;
    }


    /**
     * Returns the index into the original string of the <em>i</em>th smallest suffix.
     * That is, {@code text.substring(sa.index(i))} is the <em>i</em> smallest suffix.
     *
     * @param i an integer between 0 and <em>n</em>-1
     * @return the index into the original string of the <em>i</em>th smallest suffix
     * @throws java.lang.IllegalArgumentException unless {@code 0 <=i < n}
     */
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return index[i];
    }

    /**
     * Returns the length of the longest common prefix of the <em>i</em>th
     * smallest suffix and the <em>i</em>-1st smallest suffix.
     *
     * @param i an integer between 1 and <em>n</em>-1
     * @return the length of the longest common prefix of the <em>i</em>th
     * smallest suffix and the <em>i</em>-1st smallest suffix.
     * @throws java.lang.IllegalArgumentException unless {@code 1 <= i < n}
     */
    public int lcp(int i) {
        if (i < 1 || i >= n) throw new IllegalArgumentException();
        return lcp(index[i], index[i - 1]);
    }

    // longest common prefix of text[i..n) and text[j..n)
    private int lcp(int i, int j) {
        int length = 0;
        while (i < n && j < n) {
            if (text[i] != text[j]) return length;
            i++;
            j++;
            length++;
        }
        return length;
    }

    /**
     * Returns the <em>i</em>th smallest suffix as a string.
     *
     * @param i the index
     * @return the <em>i</em> smallest suffix as a string
     * @throws java.lang.IllegalArgumentException unless {@code 0 <= i < n}
     */
    public String select(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return new String(text, index[i], n - index[i]);
    }

    /**
     * Returns the number of suffixes strictly less than the {@code query} string.
     * We note that {@code rank(select(i))} equals {@code i} for each {@code i}
     * between 0 and <em>n</em>-1.
     *
     * @param query the query string
     * @return the number of suffixes strictly less than {@code query}
     */
    public int rank(String query) {
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, index[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }

    // is query < text[i..n) ?
    private int compare(String query, int i) {
        int m = query.length();
        int j = 0;
        while (i < n && j < m) {
            if (query.charAt(j) != text[i]) return query.charAt(j) - text[i];
            i++;
            j++;

        }
        if (i < n) return -1;
        if (j < m) return +1;
        return 0;
    }


    /**
     * Unit tests the {@code SuffixArrayx} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String s = StdIn.readAll().replaceAll("\n", " ").trim();
        CircularSuffix suffix1 = new CircularSuffix(s);
        CircularSuffix suffix2 = new CircularSuffix(s);
        boolean check = true;
        for (int i = 0; check && i < s.length(); i++) {
            if (suffix1.index(i) != suffix2.index(i)) {
                StdOut.println("suffix1(" + i + ") = " + suffix1.index(i));
                StdOut.println("suffix2(" + i + ") = " + suffix2.index(i));
                String ith = "\"" + s
                        .substring(suffix1.index(i), Math.min(suffix1.index(i) + 50, s.length()))
                        + "\"";
                String jth = "\"" + s
                        .substring(suffix2.index(i), Math.min(suffix2.index(i) + 50, s.length()))
                        + "\"";
                StdOut.println(ith);
                StdOut.println(jth);
                check = false;
            }
        }

        StdOut.println("  i ind lcp rnk  select");
        StdOut.println("---------------------------");

        for (int i = 0; i < s.length(); i++) {
            int index = suffix2.index(i);
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            int rank = suffix2.rank(s.substring(index));
            assert s.substring(index).equals(suffix2.select(i));
            if (i == 0) {
                StdOut.printf("%3d %3d %3s %3d  %s\n", i, index, "-", rank, ith);
            }
            else {
                // int lcp  = suffix.lcp(suffix2.index(i), suffix2.index(i-1));
                int lcp = suffix2.lcp(i);
                StdOut.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
            }
        }
    }

}

