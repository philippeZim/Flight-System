package flight.system.Data;

import flight.system.FlightManager.FlightInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FlightsInfoGenerator {
    public String randomFlightnumber(){
        Random r = new Random();
        char c1 = (char)(r.nextInt(26) + 'A');
        char c2 = (char)(r.nextInt(26) + 'A');
        char c3 = (char)(r.nextInt(9) + '0');
        int selector = r.nextInt(4);
        String airlineCode;
        switch (selector){
            case 0 -> airlineCode = Character.toString(c3) + Character.toString(c1);
            case 1 -> airlineCode = Character.toString(c1) + Character.toString(c3);
            default -> airlineCode = Character.toString(c1) + Character.toString(c2);
        }
        return airlineCode + " " + Integer.toString(r.nextInt(9999));
    }
    public String randomGate(){
        Random r = new Random();
        char c1 = (char)(r.nextInt(5) + 'A');
        char c2 = (char)(r.nextInt(7)  + 1 + '0');
        char c3 = (char)(r.nextInt(10) + '0');
        int selector = r.nextInt(2);
        if (selector == 0) {
            return Character.toString(c1) + Character.toString(c2);
        } else {
            return Character.toString(c1) + Character.toString(c2) + Character.toString(c3);
        }
    }
    public String randomTerminal(){
        Random r = new Random();
        char c1 = (char)(r.nextInt(4)  + 1 + '0');
        int selector = r.nextInt(2);
        return "T" + Character.toString(c1);
    }
    public String randomAircraft(){
        String[] lines = {"ATR42;288","AT43;117","AT44;7","AT45;105","AT46;59","ATR72;928","AT73;28","AT75;342","AT76;558","BCSX;181","BCS1;48","BCS3;133","A300;235","A306;200","A30B;30","A3ST;5","A310;85","A310;85","A340;270","A342;13","A343;155","A345;17","A346;85","A350;425","A359;374","A35K;51","A380;247","A388;247","B747;658","B741;2","B742;48","B743;6","B744;455","B748;147","B757;877","B752;821","B753;56","B767;1000","B762;179","B763;783","B764;38","B787;1004","B788;375","B789;565","B78X;64","DHCX;1100","DH8A;203","DH8B;81","DH8C;230","DH8D;586","E17X;853","E170;174","E75L;493","E75S;186","E19X;730","E190;564","E195;166","E29X;50","E290;18"};
        Random r = new Random();
        int sel = r.nextInt(17830);
        int accum = 0;
        for (String item : lines) {
            String see = item.split(";")[1];
            accum += Integer.parseInt(see);
            if (sel <= accum) {
                return item.split(";")[0];
            }
        }
        return "";
    }
    public String randomTime(){
        Random r = new Random();
        int firstInt = r.nextInt(13);
        String first = Integer.toString(firstInt);
        if(first.length() == 1){
            first = "0" + first;
        }
        String second = Integer.toString(r.nextInt(60));
        if(second.length() == 1){
            second = "0" + second;
        }
        String half = firstInt > 6 ? "am" : "pm";
        return first + ":" + second + " " + half;
    }

    public ArrayList<String[]> getAirlines(){
        String str = "";
        try {
            str = Files.readString(Path.of("src/main/java/flight/system/Data/airlinesEng.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] lines = str.split("\r\n");
        ArrayList<String[]> res = new ArrayList<>();
        Random r = new Random();
        for(String line : lines){
            String[] spl = line.trim().split("\t");
            if(spl.length == 3){
                res.add(new String[] {spl[0].substring(0,2) + r.nextInt(10000), spl[1], spl[2]});
            }
        }
        return res;
    }
    public String[] generateAirlineAndFlightnumberAndOriginAndDestination(){
        /* Generatest String[] with random Values for:
        [0] = airline
        [1] = flightNumber
        [2] = origin
        [3] = destination
         */
        String str = "";
        try {
            str = Files.readString(Path.of("src\\airlinesEng.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] lines = str.split("\r\n");
        ArrayList<String[]> res = new ArrayList<>();
        for(String line : lines){
            String[] spl = line.trim().split("\t");
            if(spl.length == 3){
                res.add(new String[] {spl[0].substring(0,2), spl[1], spl[2]});
            }
        }
        Random r = new Random();
        int index = r.nextInt(res.size());
        int index2 = index;
        while(index2 == index){
            index2 = r.nextInt(res.size());
        }
        return new String[] {res.get(index)[1], res.get(index)[0] + r.nextInt(10000), res.get(index)[2], res.get(index2)[2]};
    }

    public FlightInfo generateFlightByCountry(String[] alFnOr, String origin, String destination){
        return new FlightInfo(alFnOr[0],randomTime(), randomTime(), randomGate(), randomTerminal(), randomAircraft(), alFnOr[1],origin, destination);
    }
    public FlightInfo generateRandomFlight(){
        String[] alFnOrDs = generateAirlineAndFlightnumberAndOriginAndDestination();

        return new FlightInfo(alFnOrDs[1],randomTime(), randomTime(), randomGate(), randomTerminal(), randomAircraft(), alFnOrDs[0],alFnOrDs[2],alFnOrDs[3]);
    }

    public HashMap<String, ArrayList<FlightInfo>> flightsByCountry(String origin, String destination){
        HashMap<String, ArrayList<FlightInfo>> result = new HashMap<>();
        ArrayList<String[]> allAirlines = getAirlines();
        for (String[] line : allAirlines){
            if(!result.containsKey(line[2])){
                result.put(line[2], new ArrayList<>());
            }
            result.get(line[2]).add(generateFlightByCountry(line, origin, destination));
        }
        return result;
    }
    public ArrayList<FlightInfo> flightsCountry(String origin, String originCountryName, String destination){
        return flightsByCountry(origin, destination).get(originCountryName);
    }

    public void generateDocument(String filename){
        FlightInfo[] flights = new FlightInfo[100];
        for (int i = 0; i < flights.length; i++) {
            flights[i] = generateRandomFlight();
        }
        StringBuilder output = new StringBuilder();
        for (FlightInfo f : flights){
            output.append(f.getFlightNumber()).append("|").append(f.getStartTime()).append("|").append(f.getEndTime()).append("|").append(f.getGate()).append("|").append(f.getTerminal()).append("|").append(f.getAirplaneType()).append("|").append(f.getAirline()).append("|").append(f.getOrigin()).append("|").append(f.getDestination()).append(System.lineSeparator());
        }
        try (PrintWriter out = new PrintWriter(filename + ".txt")) {
            out.println(output.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        FlightsInfoGenerator test = new FlightsInfoGenerator();
        //test.randomAircraft();

    }
}
