package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Nodes {

    private static List<String> lines;
    private static HashMap<String, String> r = new HashMap<>();;
    private static HashMap<String, String> l = new HashMap<>();;
    private static String directions;
    private static HashMap<Character, Map<String,String>> toDirection = new HashMap<>();
    static {
        toDirection.put('R',r);
        toDirection.put('L',l);
    }

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day8/input.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static int compute() {
        parseInstructions();
        return traverseNodes();
    }

    private static int traverseNodes() {

        var curr = "AAA";
        int idx=0;
        int steps=0;
        while (!"ZZZ".equals(curr)){
            curr = toDirection.get(directions.charAt(idx)).get(curr);
            steps+=1;
            idx = (idx+1)%(directions.length());
        }
        return steps;
    }

    private static void parseInstructions() {
        directions = lines.get(0).strip();
        Pattern p = Pattern.compile("[A-Z]{3}");
        for (int i=2;i<lines.size();i++){
            Matcher m = p.matcher(lines.get(i));
            List<String > groups = new ArrayList<>();
            while (m.find()) groups.add(m.group());
            r.put(groups.get(0), groups.get(2));
            l.put(groups.get(0), groups.get(1));
        }
    }


    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

}
