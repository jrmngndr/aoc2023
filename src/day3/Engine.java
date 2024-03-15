package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Engine {

    private static List<String> lines;
    private static HashSet<List<Integer>> set;
    private static HashMap<List<Integer>, List<Integer>> gearToNumbers;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day3/input.txt";
        getLines(fName);
        set = new HashSet<>();
        gearToNumbers = new HashMap<>();
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static int compute() {
        //iterate once, record all symbol positions
        //iterate second time, record number if it is adjacent to any
        //keep adjacent stars in a set, add count to a map and multiply sum in the second map
        for ( int i=0;i<lines.size();i++) {
            for (int j=0; j<lines.get(0).length(); j++) {
                char c = lines.get(i).charAt(j);
                if ( !Character.isDigit(c) && c!='.') {
                    set.add(Arrays.asList(i,j));
                }
            }
        }
//        for (List<Integer> arr: set) {
//            System.out.println(arr);
//        }
        int sum=0;
        for ( int i=0;i<lines.size();i++) {
            System.out.println(lines.get(i));
            for (int j=0; j<lines.get(0).length();) {
                char c = lines.get(i).charAt(j);
                boolean inside=false;
                if ( Character.isDigit(c) && !inside) {
                    int num=0;
                    boolean adjacent=false;
                    HashSet<List<Integer>> gearsNearby = new HashSet<>();
                    while (j<lines.get(i).length() && Character.isDigit(lines.get(i).charAt(j))) {
                        adjacent |= isAdjacent(i,j);
                        getGears(i,j, gearsNearby);
                        for ( var integer : gearsNearby) System.out.println(integer.get(0)+" "+integer.get(1));
                        num=num*10+Integer.parseInt(lines.get(i).substring(j,j+1));
                        j++;
                    }
                    j-=1;
                    if (adjacent) {
                        for ( var gear : gearsNearby){
                            List<Integer> innerList = gearToNumbers.getOrDefault(gear, new ArrayList<>());
                            innerList.add(num);
                            gearToNumbers.put(gear, innerList);
                            for ( Integer integer : innerList) System.out.println(integer+" ");
                        }
                    }
                }
                j+=1;
            }
        }
        int sumgears = gearToNumbers.values().stream().filter( v -> v.size()==2).map( v -> v.get(0)*v.get(1)).reduce(0, Integer::sum);
        return sumgears;
    }

    private static void getGears(int i, int j, HashSet<List<Integer>> hs) {
        for (int m=i-1;m<=i+1;m++) {
            for (int n = j-1; n<=j+1; n++) {
                if ( set.contains( Arrays.asList(m,n)) && lines.get(m).charAt(n)=='*') {
                    hs.add(Arrays.asList(m,n));
                }
            }
        }
    }
    private static boolean isAdjacent(int i, int j) {
        boolean contains = false;
        contains |= set.contains( Arrays.asList(i-1,j-1));
        contains |= set.contains(Arrays.asList(i-1,j));
        contains |= set.contains(Arrays.asList(i-1,j+1));
        contains |= set.contains(Arrays.asList(i,j-1));
        contains |= set.contains(Arrays.asList(i,j));
        contains |= set.contains(Arrays.asList(i,j+1));
        contains |= set.contains(Arrays.asList(i+1,j-1));
        contains |= set.contains(Arrays.asList(i+1,j));
        contains |= set.contains(Arrays.asList(i+1,j+1));
        return contains;
    }
}
