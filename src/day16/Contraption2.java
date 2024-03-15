package day16;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Contraption2 {

    private static char[][] lines;
    private static HashMap<Point, List<Point>> leftGoesThrough = new HashMap<>();
    private static HashMap<Point, List<Point>> leftEnergizes = new HashMap<>();

    private static HashMap<Point, List<Point>> rightGoesThrough = new HashMap<>();
    private static HashMap<Point, List<Point>> rightEnergizes = new HashMap<>();

    private static HashMap<Point, List<Point>> upGoesThrough = new HashMap<>();
    private static HashMap<Point, List<Point>> upEnergizes = new HashMap<>();

    private static HashMap<Point, List<Point>> downGoesThrough = new HashMap<>();
    private static HashMap<Point, List<Point>> downEnergizes = new HashMap<>();

    private static HashMap<direction,HashMap<Point,List<Point>>> dirToGoes = new HashMap<>();
    private static HashMap<direction,HashMap<Point,List<Point>>> dirToEnergizes = new HashMap<>();
    static {
        dirToGoes.put(direction.LEFT,leftGoesThrough);
        dirToGoes.put(direction.RIGHT,rightGoesThrough);
        dirToGoes.put(direction.UP,upGoesThrough);
        dirToGoes.put(direction.DOWN,downGoesThrough);

        dirToEnergizes.put(direction.LEFT,leftEnergizes);
        dirToEnergizes.put(direction.RIGHT,rightEnergizes);
        dirToEnergizes.put(direction.UP,upEnergizes);
        dirToEnergizes.put(direction.DOWN,downEnergizes);


    }
    private static List<List<String>> patterns;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day16/test.txt";
        getLines(fName);
        final int sum = compute2();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path).stream().map(String::toCharArray).toArray(char[][]::new);
    }

    private static void computeEnergized() {
        for(int m=0;m<lines.length;m++){
            for (int n=0; n<lines[0].length;n++){
                if (lines[m][n]=='.') continue;
                for (var direction:direction.values()) {
                    var next = new Point(m, n, direction);
                    var start=next;
                    var goesThrough=new ArrayList<Point>();
                    var energizes = new ArrayList<Point>();
                    while (true) {

                        goesThrough.add(next);
                        char nxt = lines[next.i][next.j];
                        if (nxt == '.' || (nxt == '|' && next.direction.ordinal() < 2) || (nxt == '-' && next.direction.ordinal() >= 2)) {
                            int i = next.i + next.direction.i;
                            int j = next.j + next.direction.j;
                            if (i < 0 || i >= lines.length || j < 0 || j >= lines[0].length) break;
                            next = new Point(i, j, next.direction);
                        } else if (nxt == '|' || nxt == '-') {
                            var nextDir = nxt == '|' ? direction.UP : direction.LEFT;
//                            int i = next.i + nextDir.i;
//                            int j = next.j + nextDir.j;
//                            if (i >= 0 && i < lines.length && j >= 0 && j < lines[0].length) {
                                energizes.add(new Point(next.i, next.j, nextDir));
//                            }
                            nextDir = nxt == '|' ? direction.DOWN : direction.RIGHT;
//                            i = next.i + nextDir.i;
//                            j = next.j + nextDir.j;
//                            if (i >= 0 && i < lines.length && j >= 0 && j < lines[0].length) {
                                energizes.add(new Point(next.i, next.j, nextDir));
//                            }
                            break;
                        } else if ((nxt == '/' && next.direction == direction.RIGHT) || (nxt == '\\' && next.direction == direction.LEFT)) {
                            int i = next.i + direction.UP.i;
                            int j = next.j + direction.UP.j;
                            if (i < 0 || i >= lines.length || j < 0 || j >= lines[0].length) break;
                            energizes.add(new Point(i,j, direction.UP));
                            break;
                        } else if ((nxt == '\\' && next.direction == direction.RIGHT) || (nxt == '/' && next.direction == direction.LEFT)) {
                            int i = next.i + direction.DOWN.i;
                            int j = next.j + direction.DOWN.j;
                            if (i < 0 || i >= lines.length || j < 0 || j >= lines[0].length) break;
                            energizes.add(new Point(i,j, direction.DOWN));
                            break;
                        } else if ((nxt == '\\' && next.direction == direction.UP) || (nxt == '/' && next.direction == direction.DOWN)) {
                            int i = next.i + direction.LEFT.i;
                            int j = next.j + direction.LEFT.j;
                            if (i < 0 || i >= lines.length || j < 0 || j >= lines[0].length) break;
                            energizes.add(new Point(i,j, direction.LEFT));
                            break;
                        } else if ((nxt == '\\' && next.direction == direction.DOWN) || (nxt == '/' && next.direction == direction.UP)) {
                            int i = next.i + direction.RIGHT.i;
                            int j = next.j + direction.RIGHT.j;
                            if (i < 0 || i >= lines.length || j < 0 || j >= lines[0].length) break;
                            energizes.add(new Point(i,j, direction.RIGHT));
                            break;
                        }
                    }
                    dirToGoes.get(direction).put(start,goesThrough);
                    dirToEnergizes.get(direction).put(start,energizes);
                }
            }
        }
    }

    private static int compute2(){
        computeEnergized();
        int max = 0;
        for (int j=0;j<lines[0].length;j++){
            var start =new Point(0,j,direction.DOWN);
            var toFollow = new Stack<Point>();
            var energized = new HashSet<Point>();
            var goes=dirToGoes.get(direction.DOWN).getOrDefault(start, null);
            if (goes==null){
                int i = 0;
                while (i < lines.length && (lines[i][j] == '.'||lines[i][j]=='|')){
                    energized.add(new Point(i,j,direction.DOWN));
                    i+=1;
                }
                start=new Point(i,j,direction.DOWN);
            }
            toFollow.add(start);
            while (toFollow.size()!=0){
                var next = toFollow.pop();
                if (energized.contains(next)) continue;
                var vals=dirToGoes.get(next.direction).getOrDefault(next,null);
                if (vals==null) continue;
                energized.addAll(vals);
                toFollow.addAll(dirToEnergizes.get(next.direction).get(next));
            }
            max = Math.max(max,energized.stream().map(p->p.i+" "+p.j).distinct().toList().size());
        }

        for (int j=0;j<lines[0].length;j++){
            var start =new Point(lines.length-1,j,direction.UP);
            var toFollow = new Stack<Point>();
            var energized = new HashSet<Point>();
            var goes=dirToGoes.get(direction.UP).getOrDefault(start, null);
            if (goes==null){
                int i = lines.length-1;
                while (i >=0 && (lines[i][j] == '.'||lines[i][j]=='|')){
                    energized.add(new Point(i,j,direction.UP));
                    i-=1;
                }
                start=new Point(i,j,direction.UP);
            }
            toFollow.add(start);
            while (toFollow.size()!=0){
                var next = toFollow.pop();
                if (energized.contains(next)) continue;
                var vals=dirToGoes.get(next.direction).getOrDefault(next,null);
                if (vals==null) continue;
                energized.addAll(vals);
                toFollow.addAll(dirToEnergizes.get(next.direction).get(next));
            }
            max = Math.max(max,energized.stream().map(p->p.i+" "+p.j).distinct().toList().size());
        }

        for (int i=0;i<lines.length;i++){
            var start =new Point(i,0,direction.RIGHT);
            var toFollow = new Stack<Point>();
            var energized = new HashSet<Point>();
            var goes=dirToGoes.get(direction.RIGHT).getOrDefault(start, null);
            if (goes==null){
                int j = 0;
                while (j < lines[0].length && (lines[i][j] == '.'||lines[i][j]=='-')){
                    energized.add(new Point(i,j,direction.RIGHT));
                    j+=1;
                }
                start=new Point(i,j,direction.RIGHT);
            }
            toFollow.add(start);
            while (toFollow.size()!=0){
                var next = toFollow.pop();
                if (energized.contains(next)) continue;
                var vals=dirToGoes.get(next.direction).getOrDefault(next,null);
                if (vals==null) continue;
                energized.addAll(vals);
                toFollow.addAll(dirToEnergizes.get(next.direction).get(next));
            }
            max = Math.max(max,energized.stream().map(p->p.i+" "+p.j).distinct().toList().size());
        }

        for (int i=0;i<lines.length;i++){
            var start =new Point(i,lines[0].length-1,direction.LEFT);
            var toFollow = new Stack<Point>();
            var energized = new HashSet<Point>();
            var goes=dirToGoes.get(direction.LEFT).getOrDefault(start, null);
            if (goes==null){
                int j = lines[0].length-1;
                while (j >=0 && (lines[i][j] == '.'||lines[i][j]=='-')){
                    energized.add(new Point(i,j,direction.LEFT));
                    j-=1;
                }
                start=new Point(i,j,direction.LEFT);
            }
            toFollow.add(start);
            while (toFollow.size()!=0){
                var next = toFollow.pop();
                if (energized.contains(next)) continue;
                var vals=dirToGoes.get(next.direction).getOrDefault(next,null);
                if (vals==null) continue;
                energized.addAll(vals);
                toFollow.addAll(dirToEnergizes.get(next.direction).get(next));
            }
            max = Math.max(max,energized.stream().map(p->p.i+" "+p.j).distinct().toList().size());
        }
        return max;
    }

    private static int compute(){

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
