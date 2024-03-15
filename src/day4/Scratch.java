package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scratch {

    private static List<String> lines;
    private static HashMap<Integer, Integer> m;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day4/input.txt";
        getLines(fName);
        m = new HashMap<>();
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static int compute() {
        int sum=0;
        int cardnum=1;
        for ( var card : lines) {
            String[] numbers = card.split(": ")[1].split(" \\| ");
            String[] winning = numbers[0].split(" ");
            String[] present = numbers[1].split( " " );
            var winningSet = Arrays.stream(winning).filter(s->s.length()>0).map(Integer::parseInt).collect(Collectors.toSet());
            var presentSet = Arrays.stream(present).filter(s->s.length()>0).map(Integer::parseInt).collect(Collectors.toSet());
            presentSet.retainAll(winningSet);

            m.merge(cardnum, 1, Integer::sum);
            for (int i=1;i<=presentSet.size();i++){
                m.merge(cardnum+i,m.get(cardnum),Integer::sum);
            }
            cardnum+=1;
        }
        int finalCardnum = cardnum;
        return m.entrySet().stream().filter( e-> e.getKey()< finalCardnum).map(Map.Entry::getValue).reduce(0, Integer::sum);
    }
}
