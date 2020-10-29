/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 10-27-2020
 *  Description: Outcase data type
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet net;
    private int distance;
    private String outcasenoun;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException();
        this.net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        distance = 0;
        for (String nount : nouns) {
            int tempdist = 0;
            for (String noun : nouns) {
                if (!noun.equals(nount)) {
                    tempdist += net.distance(nount, noun);
                }
            }
            if (tempdist >= distance) {
                distance = tempdist;
                outcasenoun = nount;
            }

        }
        return outcasenoun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
