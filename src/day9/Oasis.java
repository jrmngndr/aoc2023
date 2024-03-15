package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Oasis {

    private static List<String> lines;
    private static HashMap<Integer, Integer> m;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day9/input.txt";
        getLines(fName);
        m = new HashMap<>();
        final int sum = compute2();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static int compute() {
        //for each line
        //get a list
        //create next list
        //keep last val of current list
        //add final values to sum
        int sum=0;
        for (var line:lines){

            List<Integer> readings = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            Stack<Integer> stack = new Stack<>();
            boolean stable = false;
            while (!stable) {
                stack.push(readings.get(readings.size()-1));
                var diffs = new ArrayList<Integer>();
                for (int i=1;i<readings.size();i++){
                    diffs.add(readings.get(i)-readings.get(i-1));
                }
                stable = diffs.stream().allMatch(n->n==0);
                readings=diffs;
            }
            int num = 0;
            while (stack.size()!=0) num+=stack.pop();
            sum+=num;
        }
        return sum;
    }

    private static int compute2() {
        int sum=0;
        for (var line:lines){

            List<Integer> readings = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            Stack<Integer> stack = new Stack<>();
            boolean stable = false;
            while (!stable) {
                stack.push(readings.get(0));
                var diffs = new ArrayList<Integer>();
                for (int i=1;i<readings.size();i++){
                    diffs.add(readings.get(i)-readings.get(i-1));
                }
                stable = diffs.stream().allMatch(n->n==0);
                readings=diffs;
            }
            int num = 0;
            while (stack.size()!=0) num=stack.pop()-num;
            sum+=num;
        }
        return sum;
    }
}
