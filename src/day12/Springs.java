package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Springs {
    private static List<String> lines;
    private static HashMap<Pair, Long> waysCache = new HashMap<>();;

    private static int maxFitCounts;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day12/input.txt";
        getLines(fName);
        final long sum = compute();
        System.out.println( sum );
    }

    private static long compute() {
        long total = 0;
        for (var line: lines) {
            var info = line.split(" ");
            var counts = Arrays.stream(info[1].split(",")).map(Integer::parseInt).toList();
            counts = Collections.nCopies(5,counts).stream().flatMap(List::stream).toList();
            var rowTemp=info[0];
            var row=rowTemp;
            for (int i=0;i<4;i++){
                row+="?"+rowTemp;
            }
            long rtotal=isOkCached(row,counts);
            total+=rtotal;
//            System.out.println(row+" "+counts+" "+rtotal);
        }
        return total;
    }

    private static long isOkCached(String row, List<Integer> counts) {
        var key = new Pair(row, counts);

        if (waysCache.containsKey(key)) return waysCache.get(key);

        var ans =0L;
        if (counts.size()==0) {
            ans =row.contains("#")?0:1;
            waysCache.putIfAbsent(new Pair(row,counts),ans);
            return ans;
        }

        if (row.length()==0 || row.length()<counts.get(0)) {
            for (int i=0;i<row.length();i++) waysCache.putIfAbsent(new Pair(row.substring(i),counts),ans);
            return ans;
        }

        if (row.charAt(0)=='.'){
            ans=isOkCached(row.substring(1),counts);
            waysCache.putIfAbsent(new Pair(row.substring(1),counts),ans);
            return ans;
        }
        if (row.charAt(0)=='?'){
            var one = isOkCached(row.substring(1),counts);
            var two=isOkCached("#"+row.substring(1),counts);
            waysCache.putIfAbsent(new Pair(row.substring(1),counts),one);
            waysCache.putIfAbsent(new Pair("#"+row.substring(1),counts),two);

            return one+two;
        }

        for (int i=0;i<counts.get(0);i++){
            if (row.charAt(i)=='.') {
                waysCache.putIfAbsent(new Pair(row.substring(0,i+1),counts.subList(0,1)),ans);
                return ans;
            }
        }
        waysCache.putIfAbsent(new Pair(row.substring(0,counts.get(0)),counts.subList(0,1)),1L);

        if (row.length()>counts.get(0) && row.charAt(counts.get(0))=='#') {
            waysCache.putIfAbsent(new Pair(row,counts),ans);
            return ans;
        }
        if (row.length()<=counts.get(0)){
            ans=counts.size()>1?0:1;
            waysCache.putIfAbsent(new Pair(row,counts),ans);
            return ans;
        }
        ans = isOkCached(row.substring(counts.get(0)+1), counts.subList(1,counts.size()));
        waysCache.putIfAbsent(new Pair(row.substring(counts.get(0)+1),counts.subList(1,counts.size())),ans);
        return ans;
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }
}
