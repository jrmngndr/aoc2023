package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Pipes {
    private static List<String> lines;
    private static HashMap<Character, List<Character>> left=new HashMap<>();
    private static HashMap<Character, List<Character>> right=new HashMap<>();
    private static HashMap<Character, List<Character>> up=new HashMap<>();
    private static HashMap<Character, List<Character>> down=new HashMap<>();

    private static char[][] map;
    public static void main( String[] args ) throws IOException {
        final String fName = "src/day10/input.txt";
        getLines(fName);
        initMapping();
        final int sum = compute();
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
    
    private static int compute(){
        parseMap();
        return followCritter()/2;
    }

    private static int followCritter() {
        var start = findS();
        var prev = start;
        var curr = getNext(start, start);
        int steps=0;
        while(curr!=null){
            var next =getNext(curr, prev);
            prev=curr;
            curr=next;
            steps+=1;
        }
        return steps+1;
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
