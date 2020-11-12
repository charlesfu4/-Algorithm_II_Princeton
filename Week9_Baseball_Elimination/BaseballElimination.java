/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 05/11/2020
 *  Description: Baseball Elimination by Ford-Fulkerson Algorithm
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;


public class BaseballElimination {
    private int numTeam;
    private HashMap<String, Integer> teamid = new HashMap<String, Integer>();
    private HashMap<Integer, String> idteam = new HashMap<Integer, String>();
    private HashMap<String, ArrayList<String>> elimination
            = new HashMap<String, ArrayList<String>>();
    private int[] win;
    private int[] lose;
    private int[] remain;
    private int[][] match;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        numTeam = Integer.parseInt(in.readLine()); // add number of team
        win = new int[numTeam];
        lose = new int[numTeam];
        remain = new int[numTeam];
        match = new int[numTeam][numTeam];

        // Store data into arrays
        for (int i = 0; in.hasNextLine(); i++) {
            String[] read = in.readLine().trim().split("\\s+");
            teamid.put(read[0], i);
            idteam.put(i, read[0]);
            win[i] = Integer.parseInt(read[1]);
            lose[i] = Integer.parseInt(read[2]);
            remain[i] = Integer.parseInt(read[3]);

            for (int k = 0; k < numTeam; k++) {
                match[i][k] = Integer.parseInt(read[4 + k]);
            }
        }
        // Firstly Do trivial elimination
        // Min and max comparison loop
        for (int x = 0; x < numTeam; x++) {
            ArrayList<String> eliminator = new ArrayList<String>();
            for (int i = 0; i < numTeam; i++) {
                if (x != i && eliminator.size() < 1) {
                    if (win[x] + remain[x] < win[i])
                        eliminator.add(idteam.get(i));
                }
            }
            if (eliminator.size() > 0)
                elimination.put(idteam.get(x), eliminator);
        }

        // Secondly, do non-trivial elimination
        // construct FlowNet for remaining team in comp_record
        for (int x = 0; x < numTeam; x++) {
            // if the target team is already trivially eliminated, ignore
            if (elimination.keySet().contains(idteam.get(x)))
                continue;
            int numRemain = numTeam - 1; // # remained team
            int numComp = numRemain * (numRemain - 1) / 2; // # remained competition
            FlowNetwork flownet = new FlowNetwork(2 + numComp + numRemain);

            // create new ArrayList for new results after dropping items
            ArrayList<Integer> winRemained = new ArrayList<Integer>();
            ArrayList<Integer> matchRemained = new ArrayList<Integer>();
            ArrayList<String> teamRemained = new ArrayList<String>();
            for (int i = 0; i < numTeam; i++) {
                if (i != x) {
                    winRemained.add(win[i]);
                    teamRemained.add(idteam.get(i));

                    for (int j = i + 1; j < numTeam; j++) {
                        if (j != x)
                            matchRemained.add(match[i][j]);
                    }
                }
            }
            // src to matches
            // matches to single team
            int vertex = 1;
            int singleTeam = numComp + 1;
            for (int i = 0; i < numRemain; i++) {
                for (int j = i + 1; j < numRemain; j++) {
                    // construct network only under the condition
                    if (vertex > numComp) break;
                    flownet.addEdge(new FlowEdge(0, vertex, matchRemained
                            .get((2 * numRemain - i - 1) * i / 2 + j - i - 1)));
                    flownet.addEdge(new FlowEdge(vertex, numComp + 1 + i,
                                                 Double.MAX_VALUE));
                    flownet.addEdge(new FlowEdge(vertex, numComp + 1 + j,
                                                 Double.MAX_VALUE));
                    vertex++;
                }
                // single team to target
                flownet.addEdge(new FlowEdge(singleTeam, flownet.V() - 1,
                                             win[x] + remain[x] - (int) winRemained
                                                     .toArray()[i]));
                singleTeam++;
            }
            // Now apply  Ford-Fulkerson to examine situation
            FordFulkerson ff = new FordFulkerson(flownet, 0, flownet.V() - 1);
            ArrayList<String> eliminator = new ArrayList<String>();
            for (int v = vertex; v < flownet.V(); v++) {
                if (ff.inCut(v)) eliminator.add(teamRemained.get(v - vertex));
            }
            if (eliminator.size() > 0)
                elimination.put(idteam.get(x), eliminator);
        }
    }

    // number of teams
    public int numberOfTeams() {
        return this.numTeam;
    }

    // all teams
    public Iterable<String> teams() {
        return teamid.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || !teamid.containsKey(team))
            throw new IllegalArgumentException();
        return win[teamid.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || !teamid.containsKey(team))
            throw new IllegalArgumentException();
        return lose[teamid.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !teamid.containsKey(team))
            throw new IllegalArgumentException();
        return remain[teamid.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || !teamid.containsKey(team1) || team2 == null || !teamid
                .containsKey(team2))
            throw new IllegalArgumentException();
        return match[teamid.get(team1)][teamid.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || !teamid.containsKey(team))
            throw new IllegalArgumentException();
        return elimination.containsKey(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !teamid.containsKey(team))
            throw new IllegalArgumentException();
        return elimination.get(team);
    }

    // Test client
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
