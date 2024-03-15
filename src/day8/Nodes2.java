package day8;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Nodes2 {

    private static List<String> lines;
    private static HashMap<String, String> r = new HashMap<>();;
    private static HashMap<String, String> l = new HashMap<>();;
    private static String directions;
    private static HashMap<Character, Map<String,String>> toDirection = new HashMap<>();
    private static List<String> starts = new ArrayList<>();

    static {
        toDirection.put('R',r);
        toDirection.put('L',l);
    }

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day8/input.txt";
        getLines(fName);
        final long sum = compute();
        System.out.println( sum );
    }

    private static long compute() {
        parseInstructions();
        return traverseNodes();
    }

    private static long traverseNodes() {

        List<Long> loops = new ArrayList<>();
        for (var start: starts) {
            var curr = start;
            int idx = 0;
            long steps = 0;
            String lastZ=null;
            int lastIdx=-1;
            while (true) {
                curr = toDirection.get(directions.charAt(idx)).get(curr);
                steps += 1;
                if (curr.endsWith("Z")){
                    if (curr.equals(lastZ)&&lastIdx==idx) break;
                    if (lastZ==null) {
                        lastZ = curr;
                        lastIdx = idx;
                        steps=0;
                    }
                }
                idx = (idx + 1) % (directions.length());
            }
            loops.add(steps);
        }
        return loops.stream().reduce(1L, Nodes2::lcm);
    }

    private static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    private static void parseInstructions() {
        directions = lines.get(0).strip();
        Pattern p = Pattern.compile("[0-9A-Z]{3}");
        for (int i=2;i<lines.size();i++){
            Matcher m = p.matcher(lines.get(i));
            List<String > groups = new ArrayList<>();
            while (m.find()) groups.add(m.group());
            r.put(groups.get(0), groups.get(2));
            l.put(groups.get(0), groups.get(1));
            if (groups.get(0).endsWith("A")) starts.add(groups.get(0));
        }
    }


    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

}
