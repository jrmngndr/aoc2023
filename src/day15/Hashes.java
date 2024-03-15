package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Hashes {
    private static List<String> lines;
    private static HashMap<Integer, List<String>> m = new HashMap<>();
    private static List<List<String>> patterns;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day15/input.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Arrays.stream(Files.readAllLines(path).get(0).split(",")).toList();
    }

    private static int compute(){
        int sum=0;

        for (var line:lines){
            var hash = hash(line);
            if (line.contains("-")) {
                if (m.get(hash)!=null) m.get(hash).removeIf(s->s.contains(line.split("-")[0]));
            } else {
                var list = m.get(hash);
                if (list == null){
                    list = new ArrayList<>();
                    list.add(line);
                    m.put(hash, list);
                    continue;
                }
                var item = list.stream().filter(s->s.contains(line.split("=")[0])).findFirst().orElse(null);
                if (item!=null) list.set(list.indexOf(item),line);
                else list.add(line);
            }
        }

        for (int box=0;box<255;box++){
            if (m.get(box)==null) continue;
            var contents= m.get(box);
            for (int i=0;i<contents.size();i++) sum+= (box+1)*(1+i)*Integer.parseInt(contents.get(i).split("=")[1]);
        }
        return sum;
    }

    private static int hash(String line){
        char[] chars = line.toCharArray();
        int i=0;
        for ( int c: chars){
            if (c=='-'||c=='=') break;
            i+=c;
            i*=17;
            i=i%256;
        }
        return i;
    }
}
