package day16;

import day12.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class Contraption {

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

    private static int compute(){
//        for(int i=0;i<lines.length;i++){
//            for (int j=0; j<lines[0].length;j++){
//
//            }
//        }
        var toFollow = new Stack<Point>();
        var energized = new HashSet<Point>();
        toFollow.add(new Point(0,0, direction.RIGHT));
        while ( toFollow.size()!=0){
            var next = toFollow.pop();
            if (energized.contains(next)) continue;
            while (next!=null &&!energized.contains(next)){
                energized.add(next);
                char nxt=lines[next.i][next.j];
                if (nxt=='.'||(nxt=='|'&&next.direction.ordinal()<2)||(nxt=='-'&&next.direction.ordinal()>=2)){
                    int i = next.i+next.direction.i;
                    int j = next.j+next.direction.j;
                    if (i<0||i>=lines.length||j<0||j>=lines[0].length) break;
                    next=new Point(i,j,next.direction);
                }
                else if (nxt=='|'||nxt=='-'){
                    var nextDir = nxt=='|'? direction.UP:direction.LEFT;
                    int i = next.i+nextDir.i;
                    int j = next.j+ nextDir.j;
                    if (i >= 0 && i < lines.length && j >= 0 && j < lines[0].length) {
                        toFollow.add(new Point(i, j, nextDir));
                    }
                    nextDir = nxt=='|'? direction.DOWN:direction.RIGHT;
                    i = next.i+nextDir.i;
                    j = next.j+ nextDir.j;
                    if (i >= 0 && i < lines.length && j >= 0 && j < lines[0].length) {
                        toFollow.add(new Point(i, j, nextDir));
                    }
                    break;
                } else if ((nxt=='/'&&next.direction==direction.RIGHT)||(nxt=='\\'&&next.direction==direction.LEFT)) {
                    int i = next.i+direction.UP.i;
                    int j = next.j+direction.UP.j;
                    if (i<0||i>=lines.length||j<0||j>=lines[0].length) break;
                    next=new Point(i,j,direction.UP);
                } else if ((nxt=='\\'&&next.direction==direction.RIGHT)||(nxt=='/'&&next.direction==direction.LEFT)) {
                    int i = next.i+direction.DOWN.i;
                    int j = next.j+direction.DOWN.j;
                    if (i<0||i>=lines.length||j<0||j>=lines[0].length) break;
                    next=new Point(i,j,direction.DOWN);
                }else if ((nxt=='\\'&&next.direction==direction.UP)||(nxt=='/'&&next.direction==direction.DOWN)) {
                    int i = next.i+direction.LEFT.i;
                    int j = next.j+direction.LEFT.j;
                    if (i<0||i>=lines.length||j<0||j>=lines[0].length) break;
                    next=new Point(i,j,direction.LEFT);
                }else if ((nxt=='\\'&&next.direction==direction.DOWN)||(nxt=='/'&&next.direction==direction.UP)) {
                    int i = next.i+direction.RIGHT.i;
                    int j = next.j+direction.RIGHT.j;
                    if (i<0||i>=lines.length||j<0||j>=lines[0].length) break;
                    next=new Point(i,j,direction.RIGHT);
                }

            }
        }
//        energized.stream().peek(System.out::println).toList();
        return energized.stream().map(p->p.i+" "+p.j).distinct().toList().size();
    }

    enum direction {
        UP(-1, 0),
        DOWN(1,0),
        LEFT(0,-1),
        RIGHT(0,1);

        private final int i;
        private final int j;

        direction(int i, int j) {
            this.i=i;
            this.j=j;
        }
    }

    private static class Point {
        Integer i;
        Integer j;
        direction direction;


        Point(int i, int j, direction direction) {
            this.i=i;
            this.j=j;
            this.direction=direction;
        }

        @Override
        public int hashCode()
        {
            return i.hashCode()^j.hashCode()^direction.hashCode();
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof Point other)) return false;
            return this.i.equals(other.i) && this.j.equals(other.j);
        }

        @Override
        public String toString(){
            return i+" "+j+" "+direction.toString();
        }
    }

}
