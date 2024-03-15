package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamelCards2 {

    private static List<String> lines;
    private static HashMap<String, Integer> bids = new HashMap<>();
    private static List<String> fiveOfKind = new ArrayList<>();
    private static List<String> fourOfKind = new ArrayList<>();
    private static List<String> fullhouse = new ArrayList<>();
    private static List<String> threeOfKind = new ArrayList<>();
    private static List<String> twoPairs = new ArrayList<>();
    private static List<String> onePair = new ArrayList<>();
    private static List<String> highCard = new ArrayList<>();


    public static void main( String[] args ) throws IOException {
        final String fName = "src/day7/input.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static int compute() {
        readAndDistributeBids();
        sortByStrength();
        return rankAndCompute();
    }

    private static int rankAndCompute() {
        int sum=0;
        int rank=1;
        for ( var hands: List.of(highCard,onePair,twoPairs,threeOfKind,fullhouse,fourOfKind,fiveOfKind)) {
            for (var hand : hands) {
                sum += bids.get(hand) * rank;
                rank += 1;
            }
        }
        return sum;
    }

    private static void sortByStrength() {
        for ( var hands: List.of(highCard,onePair,twoPairs,threeOfKind,fullhouse,fourOfKind,fiveOfKind)) {
            hands.sort(CamelCards2::compareHands);
        }
    }

    private static int compareHands(String s, String s2) {
        List<Character> ranks = List.of('A','K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');
        for (int i=0;i<5;i++) {
            int rank= -ranks.indexOf(s.charAt(i));
            int rank2 = -ranks.indexOf(s2.charAt(i));
            if (rank>rank2) return 1;
            else if (rank2>rank) return -1;
        }
        return 0;
    }

    private static int getHighestCount( String hand) {
        HashMap<Character, Integer> counts = new HashMap<>();
        for (int i=0; i<hand.length();i++) counts.compute(hand.charAt(i), (k,v)-> v==null?1:v+1 );
        return counts.entrySet().stream().filter(e->e.getKey()!='J').map(Map.Entry::getValue).max(Integer::compareTo).orElse(0);
    }

    private static void readAndDistributeBids() {
        for (var line : lines){
            var handInfo = line.split("[\s]+");
            bids.put(handInfo[0], Integer.parseInt(handInfo[1]));
            var hand= handInfo[0];
            long distinct = hand.chars().filter(c->c!='J').distinct().count();
            long jokers = hand.chars().filter(c->c=='J').count();
            int highestCount = getHighestCount(hand);

            if (jokers+highestCount==5) fiveOfKind.add(hand);
            else if (jokers+highestCount==4) fourOfKind.add(hand);
            else if (jokers+highestCount==3 && distinct==3) threeOfKind.add(hand);
            else if (jokers+highestCount==3 && distinct==2) fullhouse.add(hand);
            else if (jokers+highestCount==2 && distinct==4) onePair.add(hand);
            else if ( distinct==3 && getHighestCount(hand)==2) twoPairs.add(hand);
            else if (distinct==5) highCard.add(hand);
        }
    }
}
