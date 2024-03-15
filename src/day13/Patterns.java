package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Patterns {

    private static List<String> lines;
    private static HashMap<Integer, Integer> m;
    private static List<List<String>> patterns;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day13/input.txt";
        getLines(fName);
        m = new HashMap<>();
        final int sum = compute();
        System.out.println( sum );
    }

    private static int compute() {

        parsePatterns();

        Set<Integer> rowsAll = new HashSet<>();
        Set<Integer> rows = new HashSet<>();
        Map<Integer,Integer> rowMap = new HashMap<>();

        Set<Integer> colsAll = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        Map<Integer,Integer> colMap = new HashMap<>();


        int sum=0;
        for ( var pattern: patterns) {
            for (int i=0;i<pattern.size();i++){
                char row = pattern.get(i).charAt(0);
                for (int j=1;j<pattern.get(i).length();j++){
                    if (row==pattern.get(i).charAt(j)) rowMap.compute(j-1,(k,v)->(v==null)?1:v+1 );
                    row=pattern.get(i).charAt(j);
                }
            }
            rowsAll=rowMap.entrySet().stream().filter(e->e.getValue()>=pattern.size()-1).map(Map.Entry::getKey).collect(Collectors.toSet());


            for (int j=0;j<pattern.get(0).length();j++){
                char col = pattern.get(0).charAt(j);
                for (int i=1;i<pattern.size();i++){
                    if (col==pattern.get(i).charAt(j)) colMap.compute(i-1,(k,v)->(v==null)?1:v+1 );
                    col=pattern.get(i).charAt(j);
                }
            }
            colsAll=colMap.entrySet().stream().filter(e->e.getValue()>=pattern.get(0).length()-1).map(Map.Entry::getKey).collect(Collectors.toSet());

            for (var row:rowsAll){
                if (checkRowMatches(pattern, row)) sum+= row+1;
            }
            for (var col:colsAll){
                if (checkColMatches(pattern, col)) sum+= (col+1)*100;
            }
        }
        return sum;
    }

    private static boolean checkColMatches(List<String> pattern, Integer col) {
        int numDiff=0;
        for (int j=0;j<pattern.get(0).length();j++){
            for (int i=0;i<=col;i++){
                if (col-i<0 || col+1+i>pattern.size()-1) break;
                if (pattern.get(col-i).charAt(j)!=pattern.get(col+i+1).charAt(j)) numDiff+=1;
            }
        }
        return numDiff==1;
    }

    private static boolean checkRowMatches(List<String> pattern, Integer row) {
        int numDiff=0;
        for (var line: pattern){
            for (int i=0;i<=row;i++){
                if (row-i<0 || row+1+i>line.length()-1) break;
                if (line.charAt(row-i)!=line.charAt(row+1+i)) numDiff+=1;
            }
        }
        return numDiff==1;
    }

    private static void parsePatterns() {
        patterns = new ArrayList<>();
        for (int i=0;i< lines.size();){
            var pattern = new ArrayList<String>();
            while (!lines.get(i).isBlank() && i< lines.size()-1){
                pattern.add(lines.get(i));
                i++;
            }
            patterns.add(pattern);
            i++;
        }
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

}
