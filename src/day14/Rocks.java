package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Rocks {
    private static List<String> lines;
    private static HashMap<Integer, Integer> m= new HashMap<>();;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day14/input.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static int compute() {
        var maxWeight = lines.size();
        for ( int i = 0;i<lines.get(0).length();i++){
            m.put(i,maxWeight);
        }
        int sum=0;

        for (int i=0;i<lines.size();i++){
            for (int j=0; j<lines.get(i).length();j++){
                char c = lines.get(i).charAt(j);
                if (c=='O') {
                    sum+=m.get(j);
                    m.put(j,m.get(j)-1);
                } else if (c=='#'){
                    m.put(j,maxWeight-i-1);
                }
            }
        }
        return sum;
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

}
