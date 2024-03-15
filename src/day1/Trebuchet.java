package day1;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Trebuchet {

    private static List<String> lines;

    public static void main( String[] args ) throws IOException {
        final String fName = "src/day1/input2.txt";
        getLines(fName);
        final int sum = compute();
        System.out.println( sum );
    }

    private static void getLines(String fName) throws IOException {
        final Path path = Paths.get(fName);
        lines = Files.readAllLines(path);
    }
    
    private static int compute() {
        int sum = 0;
        for ( String s : lines ) {
            int sum2=sum;
            String s2 = s;
            s = replaceNums(s);
            for ( int i = 0; i<s.length(); i++) {
                if ( Character.isDigit(s.charAt(i)) && s.charAt(i)!='0' ) {
                    sum+= 10*Character.getNumericValue(s.charAt(i));
                    break;
                }
            }

            for ( int i = s.length()-1; i>=0; i--) {
                if ( Character.isDigit(s.charAt(i))  && s.charAt(i)!='0' ) {
                    sum+= Character.getNumericValue(s.charAt(i));
                    System.out.println(sum-sum2 + " " + s + " " + s2);
                    break;
                }
            }
        }
        return sum;
    }

    private static String replaceNums( final String s ) {
        Map<String, String> m = new HashMap();
        m.put("one", "1");
        m.put("two", "2");
        m.put("three", "3");
        m.put("four", "4");
        m.put("five", "5");
        m.put("six", "6");
        m.put("seven", "7");
        m.put("eight", "8");
        m.put("nine", "9");
        String firstNum = Stream.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").filter(s::contains).min(Comparator.comparingInt(s::indexOf)).orElse(null);
        final String s2 = firstNum == null ? s : s.substring(0, s.indexOf(firstNum)) + m.get(firstNum) + s.substring(s.indexOf(firstNum) + 1);
        String secondNum = Stream.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").filter(s2::contains).max(Comparator.comparingInt(s2::lastIndexOf)).orElse(null);
        final String s3 = secondNum == null ? s2 : s2.substring(0, s2.lastIndexOf(secondNum)) + m.get(secondNum) + s2.substring(s2.lastIndexOf(secondNum) + 1);
//        System.out.println(s+" "+s2+" "+s3 + " " + firstNum + " " + secondNum);
        return s3;
    }
}
