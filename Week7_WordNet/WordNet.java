/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 10-25-2020
 *  Description: WordNet data type
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.HashSet;

// Should be immutable class
public final class WordNet {
    private final LinearProbingHashST<String, HashSet<Integer>> synsetable;
    private final LinearProbingHashST<Integer, String> idsyntable;
    private final Digraph hypergraph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // null input
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        int hypercount = 0; // for checking if rooted DAG
        synsetable = new LinearProbingHashST<String, HashSet<Integer>>();
        idsyntable = new LinearProbingHashST<Integer, String>();
        In synsets_in = new In(synsets);

        // read in lines of synsets
        while (synsets_in.hasNextLine()) {
            String[] tempreadline = synsets_in.readLine().split(",");
            String[] noun_arr = tempreadline[1].split(" ");

            // For id-synset table
            idsyntable.put(Integer.parseInt(tempreadline[0]), tempreadline[1]);

            // Walk through every noun
            for (int i = 0; i < noun_arr.length; i++) {
                HashSet<Integer> idset;
                String noun = noun_arr[i];
                // new encountered noun
                // create new idset and add id in
                // put kv in
                if (!synsetable.contains(noun)) {
                    idset = new HashSet<Integer>();
                    idset.add(Integer.parseInt(tempreadline[0]));
                    synsetable.put(noun, idset);

                }
                // existed noun
                // get the corresponding idset of this noun
                // add new id in and put kv back
                else {
                    idset = synsetable.get(noun);
                    idset.add(Integer.parseInt(tempreadline[0]));
                    synsetable.put(noun, idset);
                }
            }
        }

        // readin hypernyms with specified number of vertices
        hypergraph = new Digraph(idsyntable.size());
        In hypernyms_in = new In(hypernyms);
        // read in lines of hypernyms, and add Edges
        while (hypernyms_in.hasNextLine()) {
            hypercount++;
            String[] tempreadline = hypernyms_in.readLine().split(",");

            for (int i = 1; i < tempreadline.length; i++) {
                hypergraph.addEdge(Integer.parseInt(tempreadline[0]), Integer.parseInt(tempreadline[i]));
            }
        }

        // Check if number of synset - number of hypernym > 1: Rooted
        if (idsyntable.size() - hypercount > 1) throw new IllegalArgumentException("Not a rooted Digraph!");
        // Check if there is a cycle
        DirectedCycle dc = new DirectedCycle(hypergraph);
        if (dc.hasCycle()) throw new IllegalArgumentException("Not an acylic Digraph!");
        // After checking create sap object
        sap = new SAP(hypergraph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetable.keys();
    }

    // is the word a WordNet noun?
    // better than or equal O(logN)
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synsetable.contains(word);
    }


    // distance between nounA and nounB (defined below)
    // O(wordnet)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!synsetable.contains(nounA) || !synsetable.contains(nounB)) throw new IllegalArgumentException();
        return sap.length(synsetable.get(nounA), synsetable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!synsetable.contains(nounA) || !synsetable.contains(nounB)) throw new IllegalArgumentException();
        return idsyntable.get(sap.ancestor(synsetable.get(nounA), synsetable.get(nounB)));
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet test = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println("Distance: " + test.distance("worm", "bird"));
        StdOut.println("Ancestor: " + test.sap("worm", "bird"));
    }
}
