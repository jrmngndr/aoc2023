package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Rocks2 {
    private static List<String> lines;
    private static List<Integer> weights= new ArrayList<>();
    private static Map<String, Integer> map = new HashMap<>();
    private static int[][] field;
    public static void main( String[] args ) throws IOException {
        final String fName = "src/day14/input.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static int compute() {
        readRocks();
        int i=0;
        int space=0;
        while (true) {
            shakeNCycles(1);
            var key = "";
            for (var f:field) key+=Arrays.toString(f);
            if (map.containsKey(key)) {
                space=map.get(key);
                weights = weights.subList(space, weights.size() - 1);
                break;
            }
            map.put(key, i);
            i++;
        }

        var cycle= weights.get((1000000000-space)%weights.size()-1);
        return cycle;
    }

    private static void shakeNCycles(int n){
        for (int i =0;i<n;i++){
            north();
            west();
            south();
            east();
            var w = weight();
            weights.add(w);
//            System.out.println(w);
        }
    }

    static int findCycleLength(){
        //floyd cycle detection algorithm (N-K)

        //bunny+turtle
        shakeNCycles(1);
        int turtle=weights.get(0);
        shakeNCycles(turtle);
        int bunny=weights.get(turtle);

        outer: while(bunny != turtle){
            if (weights.size()<turtle) shakeNCycles(weights.size()-turtle);
            turtle = weights.get(turtle);
            if (weights.size()<bunny) shakeNCycles(weights.size()-bunny);
            bunny=weights.get(bunny);
            if (weights.size()<bunny) shakeNCycles(weights.size()-bunny);
            bunny = weights.get(bunny);
        }


        //turtle is @ n-k, where n=cycle length, k=nodes from start to start of cycle
        bunny = 0;
        int i=0;
        while(bunny != turtle){
            turtle = weights.get(turtle);
            bunny = weights.get(bunny);
            i+=1;
        }
        i=bunny;
        shakeNCycles(turtle-(weights.size()-bunny)-1);
        weights=weights.subList(i,weights.size());
        var weights2 = new ArrayList<>(Stream.concat(weights.stream(), weights.stream()).toList());
        Collections.rotate(weights2,-1);
        var idx = Collections.indexOfSubList(weights2,weights)+1;
        return weights.get((1000000000-3)%idx-0);
    }

    private static int weight() {
        int sum=0;
        for (int i=0;i< field.length;i++){
//            System.out.println(Arrays.toString(field[i]));
            for (int j=0; j<field[0].length;j++){
                if (field[i][j]==1)sum+=field[i][j]*(field.length-i);
            }
        }
        return sum;
    }

    private static void north() {
        for (int j=0; j<field[0].length;j++){
            int pointer=0;
            for (int i=0;i<field.length;i++){
                if (field[i][j]==1){
                    field[i][j]=0;
                    field[pointer][j]=1;
                    pointer+=1;
                } else if ( field[i][j]==2){
                    for (int n=pointer;n<i;n++) field[n][j]=0;
                    pointer=i+1;
                }
            }
        }
    }

    private static void south() {
        for (int j=0; j<field[0].length;j++){
            int pointer=field.length-1;
            for (int i=field.length-1;i>=0;i--){
                if (field[i][j]==1){
                    field[i][j]=0;
                    field[pointer][j]=1;
                    pointer-=1;
                } else if ( field[i][j]==2){
                    for (int n=pointer;n>i;n--) field[n][j]=0;
                    pointer=i-1;
                }
            }
        }
    }

    private static void east() {
        for (int i=0; i<field.length;i++){
            int pointer=field[0].length-1;
            for (int j=field[0].length-1;j>=0;j--){
                if (field[i][j]==1){
                    field[i][j]=0;
                    field[i][pointer]=1;
                    pointer-=1;
                } else if ( field[i][j]==2){
                    for (int n=pointer;n>j;n--) field[i][n]=0;
                    pointer=j-1;
                }
            }
        }
    }

    private static void west() {
        for (int i=0; i<field.length;i++){
            int pointer=0;
            for (int j=0;j<field[0].length;j++){
                if (field[i][j]==1){
                    field[i][j]=0;
                    field[i][pointer]=1;
                    pointer+=1;
                } else if ( field[i][j]==2){
                    for (int n=pointer;n<j;n++) field[i][n]=0;
                    pointer=j+1;
                }
            }
        }
    }

    private static void readRocks() {
        field=new int[lines.size()][];
        for (int i=0;i<lines.size();i++){
            int[] subfield = new int[lines.get(i).length()];
            for (int j=0; j<lines.get(i).length();j++){
                char c = lines.get(i).charAt(j);
                subfield[j]= c=='O'? 1 : c=='#'? 2 :0;
            }
            field[i]=subfield;
        }
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

}
