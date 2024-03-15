//package day5;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class Soil2 {
//    private static List<String> lines;
//    private static List<List<Long>> seeds = new ArrayList<>();
//    private static HashMap<Long, Long> seedSoil = new HashMap<Long, Long>();;
//    private static HashMap<Long, Long> soilFertilizer= new HashMap<Long, Long>();;
//    private static HashMap<Long, Long> fertilizerWater= new HashMap<Long, Long>();;
//    private static HashMap<Long, Long> waterLight= new HashMap<Long, Long>();;
//    private static HashMap<Long, Long> lightTemperature= new HashMap<Long, Long>();;
//    private static HashMap<Long, Long> temperatureHumidity= new HashMap<Long, Long>();;
//    private static HashMap<Long, Long> humidityLocation= new HashMap<Long, Long>();;
//
//
//    public static void main( String[] args ) throws IOException {
//        final String fName = "src/day5/input.txt";
//        getLines(fName);
//        parseMaps();
//        final Long ans = mapAll();
//        System.out.println( ans );
//    }
//
//    private static void getLines(String fName) throws IOException {
//        final Path path = Paths.get(fName);
//        lines = Files.readAllLines(path);
//    }
//
//    private static void addToMap( Collection<List<Long>> set, Map<Long,Long> map, String line ) {
//        String[] ranges = line.split(" ");
//        Long destination = Long.parseLong(ranges[0]);
//        Long source = Long.parseLong(ranges[1]);
//        Long range = Long.parseLong(ranges[2]);
//        for (var item : set) {
////            if (item>=source && item<source+range) {
////                map.put(item, destination+(item-source));
////            }
//        }
//    }
//    private static void parseMaps() {
//
//        for (int i=0; i<lines.size();) {
//            var line = lines.get(i);
//            if (line.startsWith("seeds:")) {
//                String[] seedArray = line.split(": ")[1].split(" ");
//                for (int sNum=0;sNum<seedArray.length;sNum+=2){
//                    Long seed=Long.parseLong(seedArray[sNum]);
//                    Long seedRng=Long.parseLong(seedArray[sNum+1]);
//                    seeds.add(List.of(seed,seed+seedRng-1));
//                }
//                seeds.sort(Comparator.comparingLong(l->l.get(0)));
//            }
//            else if ( line.startsWith("seed-to-soil")) {
//                i++;
//                while (!lines.get(i).isBlank()) {
//                    addToMap(seeds, seedSoil, lines.get(i));
//                    i++;
//                }
//                for (var seed: seeds) seedSoil.putIfAbsent(seed,seed);
//            }
//            else if ( line.startsWith("soil-to-fertilizer")) {
//                i++;
//                while (!lines.get(i).isBlank()) {
//                    addToMap(seedSoil.values(), soilFertilizer, lines.get(i));
//                    i++;
//                }
//                for (var soil : seedSoil.values()) soilFertilizer.putIfAbsent(soil, soil);
//            }
//            else if ( line.startsWith("fertilizer-to-water")) {
//                i++;
//                while (!lines.get(i).isBlank()) {
//                    addToMap(soilFertilizer.values(), fertilizerWater, lines.get(i));
//                    i++;
//                }
//                for (var fertilizer : soilFertilizer.values()) fertilizerWater.putIfAbsent(fertilizer, fertilizer);
//
//            }
//            else if ( line.startsWith("water-to-light")) {
//                i++;
//                while (!lines.get(i).isBlank()) {
//                    addToMap(fertilizerWater.values(), waterLight, lines.get(i));
//                    i++;
//                }
//                for (var water: fertilizerWater.values()) waterLight.putIfAbsent(water,water);
//            }
//            else if ( line.startsWith("light-to-temperature")) {
//                i++;
//                while (!lines.get(i).isBlank()) {
//                    addToMap(waterLight.values(), lightTemperature, lines.get(i));
//                    i++;
//                }
//                for (var light: waterLight.values()) lightTemperature.putIfAbsent(light, light);
//            }
//            else if ( line.startsWith("temperature-to-humidity")) {
//                i++;
//                while (!lines.get(i).isBlank()) {
//                    addToMap(lightTemperature.values(), temperatureHumidity, lines.get(i));
//                    i++;
//                }
//                for (var temp: lightTemperature.values()) temperatureHumidity.putIfAbsent(temp, temp);
//            }
//            else if ( line.startsWith("humidity-to-location")) {
//                i++;
//                while (i< lines.size() && !lines.get(i).isBlank()) {
//                    addToMap(temperatureHumidity.values(), humidityLocation, lines.get(i));
//                    i++;
//                }
//                for (var humidity: temperatureHumidity.values()) humidityLocation.putIfAbsent(humidity, humidity);
//            }
//            i++;
//        }
//    }
//
//    private static Long mapAll() {
//        HashSet<Long> locations = new HashSet<>();
//        for ( Long seed : seeds) {
//            Long soil = seedSoil.getOrDefault(seed,seed);
//            Long fertilizer = soilFertilizer.getOrDefault(soil,soil);
//            Long water = fertilizerWater.getOrDefault(fertilizer,fertilizer);
//            Long light = waterLight.getOrDefault(water,water);
//            Long temp = lightTemperature.getOrDefault(light,light);
//            Long humidity = temperatureHumidity.getOrDefault(temp,temp);
//            Long location = humidityLocation.getOrDefault(humidity,humidity);
//            locations.add(location);
//        }
//        return locations.stream().min(Long::compare).get();
//    }
//}
