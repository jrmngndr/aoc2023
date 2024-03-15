package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Universe {
    private static List<String> lines;
    private static HashMap<Integer, Integer> m;
    private static List<List<Integer>> galaxies = new ArrayList<>();
    private static Set<Integer> emptyRows = new HashSet<>();
    private static Set<Integer> emptyCols = new HashSet<>();



    public static void main( String[] args ) throws IOException {
        final String fName = "src/day11/input.txt";
        getLines(fName);
        m = new HashMap<>();
        final long sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static long compute(){
        findEmptyAndGalaxies();
        return findShortestPaths();
    }

    private static long findShortestPaths() {
        long sum=0;
        for (int first=0;first<galaxies.size();first++){
            for (int second=first+1;second< galaxies.size();second++){
                List<Integer> galaxy1 = galaxies.get(first);
                List<Integer> galaxy2 = galaxies.get(second);
                int a = Math.abs(galaxy1.get(0)- galaxy2.get(0));
                int b = Math.abs(galaxy1.get(1)- galaxy2.get(1));
                long dist = a+b;

                for (var row: emptyRows) if ((galaxy1.get(0)>row && galaxy2.get(0)<row) || (galaxy1.get(0)<row && galaxy2.get(0)>row)) dist+=1000000-1;
                for (var col: emptyCols) if ((galaxy1.get(1)>col && galaxy2.get(1)<col) || (galaxy1.get(1)<col && galaxy2.get(1)>col)) dist+=1000000-1;

                sum+=dist;
            }
        }
        return sum;
    }

    private static void findEmptyAndGalaxies() {

        var universe = new char[lines.size()][];
        for (int i = 0;i< lines.size();i++) universe[i]=lines.get(i).toCharArray();

        for (int i=0;i<universe.length;i++){
            boolean isEmptyRow=true;
            for (int j=0;j<universe[0].length;j++){
                isEmptyRow &= universe[i][j]=='.';
                if (universe[i][j]!='.'){
                    galaxies.add(List.of(i,j));
                }
            }
            if (isEmptyRow) emptyRows.add(i);
        }

        for (int j=0;j<universe[0].length;j++){
            boolean isEmptyCol=true;
            for (int i=0;i<universe.length;i++) isEmptyCol &= universe[i][j]=='.';
            if (isEmptyCol) emptyCols.add(j);
        }
    }
}
