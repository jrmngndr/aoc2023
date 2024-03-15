package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Race {
    private static List<String> lines;
    private static HashMap<Integer, Integer> m;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day6/input.txt";
        getLines(fName);
        m = new HashMap<>();
        final int sum = compute2();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }

    private static int compute2(){
        int mul = 1;
        String[] times = lines.get(0).split("[\s]+");
        String[] distances = lines.get(1).split("[\s]+");

        String timeStr = "";
        for (int i=1;i<times.length;i++) timeStr+=times[i];
        String distStr = "";
        for (int i=1;i<distances.length;i++) distStr+=distances[i];

        long time = Long.parseLong(timeStr);
        long dist = Long.parseLong(distStr);

        for ( long t=0; t<time;t++) {
            long d = (time-t)*t;
            if (d>dist) {
                mul*= time-2*t+1;
                break;
            }
        }

        return  mul;

    }

    private static int compute() {

        int mul = 1;
        String[] times = lines.get(0).split("[\s]+");
        String[] distances = lines.get(1).split("[\s]+");


        for ( int i = 1; i<times.length;i++) {
            int time = Integer.parseInt(times[i]);
            int dist = Integer.parseInt(distances[i]);

            for ( int t=0; t<time;t++) {
                int d = (time-t)*t;
                if (d>dist) {
                    mul*= time-2*t+1;
                    break;
                }
            }
        }

        return  mul;

    }
}
