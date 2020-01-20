
import edu.princeton.cs.algs4.Digraph;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KosarajuSharirSCC;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

class TopologicalSort {
    private Stack<Integer> stack = new Stack<>();
    private Queue<Integer> queue = new Queue<>();
    private boolean[] marked;
    private int count = 0;
    public TopologicalSort(Digraph G) {
        marked = new boolean[G.V()];
        for (int i = 0; i < G.V(); i++) {
            if (!marked[i]) {
                DFS(G,i);
            }
        }
    }

    private void DFS(Digraph G, int V) {
        marked[V] = true;
        for (int adj: G.adj(V)) {
            if (!marked[adj]) DFS(G, adj);
        }
        stack.push(V);
        queue.enqueue(V);
    }

    public Iterable<Integer> sort() {
        return stack;
    }
    public Iterable<Integer> reverseGraphSort() { return queue; }
}
class StrongConnectedComponent {
    private int count;
    private boolean[] marked;
    private int[] id;
    public StrongConnectedComponent(Digraph G) {
        count = 0;
        marked = new boolean[G.V()];
        id = new int[G.V()];
        TopologicalSort topo = new TopologicalSort(G);
        Queue<Integer> queue = (Queue<Integer>) topo.reverseGraphSort();
        for (int v: queue) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }

    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w: G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }
    public int count() {
        return count;
    }
}
public class WordNet {
    private List<String> synsetList;
    private Map<String, List<Integer>> synsetMap;
    private Digraph G;
    private SAP sapG;

    // constructor takes the name of the two input file
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        synsetList = new ArrayList<>();
        synsetMap = new HashMap<>();

        In strIn = new In(synsets);
        while (strIn.hasNextLine()) {
            String strLine = strIn.readLine();
            String[] tokens = strLine.split(",");
            int id = Integer.parseInt(tokens[0]);
            synsetList.add(tokens[1]);
            String[] nouns = tokens[1].split(" ");
            for (String noun: nouns) {
                if (synsetMap.containsKey(noun)) {
                    synsetMap.get(noun).add(id);
                }
                else {
                    synsetMap.put(noun, new ArrayList<Integer>());
                    synsetMap.get(noun).add(id);
                }
            }
        }

        G = new Digraph(synsetList.size());
        In hypIn = new In(hypernyms);
        int[] outputs = new int[synsetList.size()];
        while (hypIn.hasNextLine()) {
            String hypLine = hypIn.readLine();
            String[] tokens = hypLine.split(",");
            int v = Integer.parseInt(tokens[0]);
            outputs[v] = tokens.length - 1;
            for (int i = 1; i < tokens.length; i++) {
                int w = Integer.parseInt(tokens[i]);
                G.addEdge(v, w);
            }
        }

        if (!checkRootedDigraph(outputs)) {
            throw new IllegalArgumentException();
        }

        sapG = new SAP(G);

    }

    private boolean checkRootedDigraph(int[] outputs) {
        int rootCount = 0;
        for (int v: outputs) {
            if (v == 0) rootCount++;
            if (rootCount > 1) return false;
        }
        if (new StrongConnectedComponent(G).count() < synsetList.size()) {
            return false;
        }
        return true;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {return synsetMap.keySet();}

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synsetMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return sapG.length(synsetMap.get(nounA), synsetMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int ancestor = sapG.ancestor(synsetMap.get(nounA), synsetMap.get(nounB));
        return synsetList.get(ancestor);
    }



    // do unit testing of this class
    public static void main(String[] args) {

//        String synsets = Files.readString(Paths.get("src/synsets.txt") , StandardCharsets.US_ASCII);
//        String hypernyms = Files.readString(Paths.get("src/hypernyms.txt") , StandardCharsets.US_ASCII);
//        WordNet wordNet = new WordNet(synsets, hypernyms);
//        for (String s: wordNet.nouns()) {
//            System.out.println(s);
//        }
//        System.out.println(wordNet.isNoun("numbertwelve"));
//        System.out.println(wordNet.distance("numberfive","numberone"));
//        System.out.println(wordNet.sap("numberfive", "numberone"));
    }
}
