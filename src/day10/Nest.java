package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Nest {
    private static List<String> lines;
    private static HashMap<Character, List<Character>> left=new HashMap<>();
    private static HashMap<Character, List<Character>> right=new HashMap<>();
    private static HashMap<Character, List<Character>> up=new HashMap<>();
    private static HashMap<Character, List<Character>> down=new HashMap<>();
    private static ArrayList<List<Integer>> path = new ArrayList<>();
    private static char[][] map;
    public static void main( String[] args ) throws IOException {
        final String fName = "src/day10/input.txt";
        getLines(fName);
        initMapping();
        final double sum = compute();
        System.out.println( sum );
    }

    private static void initMapping(){
        right.put('F',List.of('-','7','J'));
        left.put('F', Collections.emptyList());
        up.put('F', Collections.emptyList());
        down.put('F',List.of('|','L','J'));

        right.put('L',List.of('-','J', '7'));
        left.put('L', Collections.emptyList());
        down.put('L', Collections.emptyList());
        up.put('L',List.of('|','F','7'));

        left.put('J',List.of('-','F','L'));
        right.put('J', Collections.emptyList());
        down.put('J', Collections.emptyList());
        up.put('J',List.of('|','F','7'));

        left.put('7',List.of('-','F','L'));
        right.put('7', Collections.emptyList());
        up.put('7', Collections.emptyList());
        down.put('7',List.of('|','J','L'));

        left.put('-',List.of('F','L','-'));
        up.put('-', Collections.emptyList());
        down.put('-', Collections.emptyList());
        right.put('-',List.of('J','7','-'));


        up.put('|',List.of('F','7','|'));
        right.put('|', Collections.emptyList());
        left.put('|', Collections.emptyList());
        down.put('|',List.of('L','J','|'));

        up.put('S',List.of('F','7','|'));
        right.put('S', List.of('J','7','-'));
        left.put('S', List.of('F','L','-'));
        down.put('S',List.of('L','J','|'));
    }
    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static double compute(){
        parseMap();
        followCritter();
        var area = shoelace();
        System.out.println(area);
        var i = area+1- ((double)path.size())/2;
        return i;

    }

    public static double shoelace()
    {

        var ar = path.stream().filter(l -> map[l.get(0)][l.get(1)] != '|' && map[l.get(0)][l.get(1)] != '-').toList();
        var arr=new ArrayList<>(ar);
        arr.add(ar.get(0));
        int n = arr.size();

        double det = 0.0;
//        for (int i = 1; i < n - 1; i++)
//            det += arr.get(i).get(0)*(arr.get(i+1).get(1) - arr.get(i-1).get(1) );

        for (int i = 0; i < n - 1; i++)
            det += (double)(arr.get(i).get(0) * arr.get(i+1).get(1) );
        for (int i = 0; i < n - 1; i++)
            det -= (double)(arr.get(i).get(1)  * arr.get(i+1).get(0) );

        det = Math.abs(det);
        det /= 2;
        return det;
    }

    private static int findArea() {
        int sum=0;
        for (int i=0;i<map.length;i++){
            int j =0;
            while (j<map[0].length){
                if (path.contains(List.of(i,j))) {
                    j++;
                    int tempsum = 0;
                    while ( j<map[0].length && !path.contains(List.of(i,j))) {
                        tempsum+=1;
                        System.out.print(i+1+" "+(1+j)+", ");
                        j++;
                    }
                    if (j!=map[0].length)sum+=tempsum;
                }
                j++;
            }
            System.out.println("");
        }
        return sum;
    }

    private static int followCritter() {
        var start = findS();
        path.add(start);
        var prev = start;
        var curr = getNext(start, start);
        int steps=1;
        while(curr!=null){
            path.add(curr);
            var next =getNext(curr, prev);
            prev=curr;
            curr=next;
            steps+=1;
        }
        return steps;
    }

    private static List<Integer> getNext(List<Integer> start, List<Integer> prev) {
        int i = start.get(0);
        int j = start.get(1);
        int i_pr = prev.get(0);
        int j_pr = prev.get(1);
        char st = map[i][j];
        if (!(i_pr==i-1&&j_pr==j) &&i-1>=0 && up.get(st).contains(map[i-1][j]) )return List.of(i-1,j);
        if (!(i_pr==i&&j_pr==j-1) && j-1>=0 && left.get(st).contains(map[i][j-1]) )return List.of(i,j-1);
        if (!(i_pr==i&&j_pr==j+1) &&j+1< map[0].length && right.get(st).contains(map[i][j+1]) )return List.of(i,j+1);
        if (!(i_pr==i+1&&j_pr==j) && i+1< map.length && down.get(st).contains(map[i+1][j]) )return List.of(i+1,j);

//        for (int i=start.get(0)-1;i<=start.get(0)+1;i++){
//            for (int j=start.get(1)-1;j<=start.get(1)+1;j++){
//                if (!(i==start.get(0)&&j==start.get(1)) && !(i==prev.get(0)&&j==prev.get(1))){
//                    if (m.get(start).contains(map[i][j])) return List.of(i,j);
//                }
//            }
//        }
        return null;
    }

    private static List<Integer> findS(){
        for (int i=0;i< map.length;i++){
            for (int j=0;j<map[0].length;j++){
                if (map[i][j]=='S')return List.of(i,j);
            }
        }
        return null;
    }

    private static void parseMap() {
        map = new char[lines.size()][];
        for (int i =0;i< lines.size();i++){
            map[i]=lines.get(i).toCharArray();
        }
    }
}
