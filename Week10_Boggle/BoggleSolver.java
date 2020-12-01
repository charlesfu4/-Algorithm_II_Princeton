/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 30/11/2020
 *  Description: Boggle game solver
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    private CustomTries dicTries;
    private HashSet<String> foundWord;
    private BoggleBoard board;
    private ArrayList<Bag<Integer>> neighbor;
    private boolean[] marked;
    private int m;
    private int n;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dicTries = new CustomTries();
        for (String word : dictionary) {
            dicTries.add(word);
        }
    }

    /**
     * Private method for creating adj list
     */
    private void adjCreate() {
        neighbor = new ArrayList<Bag<Integer>>();
        for (int i = 0; i < m * n; i++)
            neighbor.add(new Bag<Integer>());
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 2d array
                if (m > 1 && n > 1) {
                    // on the top border
                    if (i == 0) {
                        if (j == 0) {
                            neighbor.get(0).add(1);
                            neighbor.get(0).add(n);
                            neighbor.get(0).add(n + 1);
                        } else if (j == n - 1) {
                            neighbor.get(j).add(j - 1);
                            neighbor.get(j).add(j + n);
                            neighbor.get(j).add(j + n - 1);
                        } else {
                            neighbor.get(j).add(j - 1);
                            neighbor.get(j).add(j + 1);
                            neighbor.get(j).add(j + n);
                            neighbor.get(j).add(j + n - 1);
                            neighbor.get(j).add(j + n + 1);
                        }
                    }
                    // on the bottom border
                    else if (i == m - 1) {
                        if (j == 0) {
                            neighbor.get(n * i).add(n * i + 1);
                            neighbor.get(n * i).add(n * i - n);
                            neighbor.get(n * i).add(n * i - n + 1);
                        } else if (j == n - 1) {
                            neighbor.get(n * i + j).add(n * i + j - 1);
                            neighbor.get(n * i + j).add(n * i + j - n);
                            neighbor.get(n * i + j).add(n * i + j - n - 1);
                        } else {
                            neighbor.get(n * i + j).add(n * i + j - 1);
                            neighbor.get(n * i + j).add(n * i + j + 1);
                            neighbor.get(n * i + j).add(n * i + j - n);
                            neighbor.get(n * i + j).add(n * i + j - n - 1);
                            neighbor.get(n * i + j).add(n * i + j - n + 1);
                        }
                    }
                    // on the left border
                    else if (j == 0) {
                        neighbor.get(n * i).add(n * i - n);
                        neighbor.get(n * i).add(n * i + n);
                        neighbor.get(n * i).add(n * i + 1);
                        neighbor.get(n * i).add(n * i - n + 1);
                        neighbor.get(n * i).add(n * i + n + 1);
                    }
                    // on the right border
                    else if (j == n - 1) {
                        neighbor.get(n * i + j).add(n * i + j - n);
                        neighbor.get(n * i + j).add(n * i + j + n);
                        neighbor.get(n * i + j).add(n * i + j - 1);
                        neighbor.get(n * i + j).add(n * i + j - n - 1);
                        neighbor.get(n * i + j).add(n * i + j + n - 1);
                    }
                    // in the middle
                    else {
                        neighbor.get(n * i + j).add(n * i + j - 1);
                        neighbor.get(n * i + j).add(n * i + j + 1);
                        neighbor.get(n * i + j).add(n * i + j - n);
                        neighbor.get(n * i + j).add(n * i + j + n);
                        neighbor.get(n * i + j).add(n * i + j - n - 1);
                        neighbor.get(n * i + j).add(n * i + j + n - 1);
                        neighbor.get(n * i + j).add(n * i + j - n + 1);
                        neighbor.get(n * i + j).add(n * i + j + n + 1);
                    }
                }
                // 1d array
                else if (m == 1) {
                    if (j == 0) {
                        neighbor.get(0).add(1);
                    } else if (j == n - 1) {
                        neighbor.get(j).add(j - 1);
                    } else {
                        neighbor.get(j).add(j - 1);
                        neighbor.get(j).add(j + 1);
                    }
                } else if (n == 1) {
                    if (i == 0) {
                        neighbor.get(0).add(1);
                    } else if (i == m - 1) {
                        neighbor.get(i).add(i - 1);
                    } else {
                        neighbor.get(i).add(i - 1);
                        neighbor.get(i).add(i + 1);
                    }
                }
            }
        }
    }

    /**
     * Private method for dfs
     *
     * @param i current row
     * @param j current column
     */
    private void dfs(int i, int j, StringBuilder currStr, CustomTries.Node x, int d) {
        // deal with 'Q' situation
        boolean qu = false;
        if (board.getLetter(i, j) == 'Q') {
            currStr.append("QU");
            qu = true;
        } else {
            currStr.append(board.getLetter(i, j));
        }
        if (currStr.length() >= 3 && dicTries.contains(x, currStr.toString()))
            foundWord.add(currStr.toString());
        // update current node by traversing possible prefix
        x = dicTries.nodewithPrefix(x, currStr.toString());
        if (x != null) {
            marked[i * n + j] = true;
            // Check immediately after append if the word exists in dictionary
            for (Integer adj : neighbor.get(i * n + j)) {
                if (!marked[adj]) {
                    dfs(adj / n, adj % n, currStr, x, d);
                }
            }
        }
        // After DFS remark current tilt back to false
        // and delete appended character(s)
        marked[i * n + j] = false;
        currStr.deleteCharAt(currStr.length() - 1);
        if (qu)
            currStr.deleteCharAt(currStr.length() - 1);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        foundWord = new HashSet<String>();
        this.board = board;
        m = board.rows();
        n = board.cols();
        // Corner case where only one tilt
        if (m == 1 && n == 1) return foundWord;
        this.marked = new boolean[this.m * this.n];
        adjCreate();
        StringBuilder currStr = new StringBuilder();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                dfs(i, j, currStr, dicTries.getRoot(), 0);
            }
        }
        return foundWord;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dicTries.contains(dicTries.getRoot(), word)) {
            if (word.length() >= 3 && word.length() <= 4)
                return 1;
            else if (word.length() == 5)
                return 2;
            else if (word.length() == 6)
                return 3;
            else if (word.length() == 7)
                return 5;
            else if (word.length() >= 8)
                return 11;
            else
                return 0;
        }
        return 0;
    }

    public static void main(String args[]) {
        long startTime = System.nanoTime();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.print(word + " ");
            StdOut.println(solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime / 1000000000.);
    }
}
