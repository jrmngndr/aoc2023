package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class LavaCells {

    private static char[][] lines;
    private static HashMap<Integer, List<String>> m = new HashMap<>();
    private static List<List<String>> patterns;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day16/input.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path).stream().map(String::toCharArray).toArray(char[][]::new);
    }

}
