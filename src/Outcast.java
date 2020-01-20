import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;

public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordnet)      {
        this.wordNet = wordnet;
    }    // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        String outcast = "";
        int distance = 0;
        for (String s: nouns) {
            int d = 0;
            for (String s1: nouns) {
                d += wordNet.distance(s, s1);
            }
            if (d > distance) {
                distance = d;
                outcast = s;
            }
        }
        return outcast;
    }   // given an array of WordNet nouns, return an outcast
    public static void main(String[] args)  {
//        String synsets = Files.readString(Paths.get("src/synsets.txt") , StandardCharsets.US_ASCII);
//        String hypernyms = Files.readString(Paths.get("src/hypernyms.txt") , StandardCharsets.US_ASCII);
//        WordNet wordNet = new WordNet(synsets, hypernyms);
//        Outcast outcast = new Outcast(wordNet);
//        String[] s = new String[3];
//        s[0] = "zero";
//        s[1] = "two";
//        s[2] = "numberseven";
//        System.out.println(outcast.outcast(s));
    }  // see test client below
}