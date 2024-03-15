package day12;

import java.util.List;

public class Pair {
        String s;
        List<Integer> counts;
        Pair(String s, List<Integer> counts){
            this.s = s;
            this.counts=counts;
        }

        @Override
        public int hashCode()
        {
            return s.hashCode()^counts.hashCode();
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof Pair other)) return false;
            return this.s.equals(other.s) && this.counts.equals(other.counts);
        }

        @Override
        public String toString(){
            String st="";
            for (int i =0;i<counts.size();i++) st+= " "+counts.get(i);
            return s+":"+st;
        }

}
