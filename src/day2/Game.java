package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game {

    private static List<String> lines;
    final static int RED = 12, GREEN = 13, BLUE = 14;
    private static HashMap<String,Integer> m;
    public static void main( String[] args ) throws IOException {
        final String fName = "src/day2/input.txt";
        getLines(fName);
        m = new HashMap<>();
        m.put("r", RED);
        m.put("g", GREEN);
        m.put("b", BLUE);
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static int compute() {

        //for each string: split by round
        //for each round in string: split by color
        //for each color: split by space, get number and first letter, compare and break if needed
        int sum =0;
        int impossible = 0;
        for ( String line : lines) {
            String[] game = line.split(":");
            Integer gameId = Integer.parseInt(game[0].split(" ")[1]);
            sum+=gameId;
            String[] rounds = game[1].split(";");
            outer: for ( String round : rounds ) {
                String[] colors = round.split(",");
                for ( String color : colors) {
                    String[] info = color.split(" ");
                    System.out.println(Arrays.toString(info));
                    if ( Integer.parseInt(info[1]) > m.get(info[2].substring(0,1))) {
                        impossible+=gameId;
                        break outer;
                    }
                }
            }
            System.out.println( sum + " " + impossible);
        }
        return sum-impossible;
    }
}
