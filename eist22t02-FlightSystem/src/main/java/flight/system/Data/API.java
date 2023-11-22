package flight.system.Data;

import flight.system.FlightManager.FlightInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;


public class API {

    public JSONObject getCityData(String city) throws JSONException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://foreca-weather.p.rapidapi.com/location/search/" + city + "?lang=en"))
                .header("X-RapidAPI-Key", "6d09089f9fmshbe60b427515c6b3p184c32jsna62595bf2d7c")
                .header("X-RapidAPI-Host", "foreca-weather.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body());
        return new JSONObject(response.body());
    }

    public ArrayList<String[]> weatherNextWeek(String city) throws JSONException {
        JSONObject cityInfo = getCityData(city);
        String cityId = Integer.toString(cityInfo.getJSONArray("locations").getJSONObject(0).getInt("id"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://foreca-weather.p.rapidapi.com/forecast/daily/" + cityId + "?alt=0&tempunit=C&windunit=MS&periods=8&dataset=full"))
                .header("X-RapidAPI-Key", "6d09089f9fmshbe60b427515c6b3p184c32jsna62595bf2d7c")
                .header("X-RapidAPI-Host", "foreca-weather.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        JSONObject weekWeather = new JSONObject(response.body());
        JSONArray dailyWeather = weekWeather.getJSONArray("forecast");
        ArrayList<String[]> days = new ArrayList<>();
        for (int i = 0; i < dailyWeather.length(); i++) {
            days.add(new String[] {dailyWeather.getJSONObject(i).getString("date"),
                    Integer.toString(dailyWeather.getJSONObject(i).getInt("maxTemp")) + "C",
                    dailyWeather.getJSONObject(i).getString("symbolPhrase")});
        }

        days.forEach(s -> System.out.println(s[0] + ", " + s[1] + ", " + s[2]));
        return days;
    }
    public ArrayList<FlightInfo> getFlights(String origin, String destination) throws JSONException {
        JSONObject originJson = getCityData(origin);
        String originCountryName = originJson.getJSONArray("locations").getJSONObject(0).getString("country");
        FlightsInfoGenerator gen = new FlightsInfoGenerator();
        return gen.flightsCountry(origin, originCountryName, destination);
    }
    public String getMap(String from, String to) throws JSONException {
        JSONObject fromJ = getCityData(from);
        JSONObject toJ = getCityData(to);
        double[] cordsF = new double[] {fromJ.getJSONArray("locations").getJSONObject(0).getDouble("lon"),
                fromJ.getJSONArray("locations").getJSONObject(0).getDouble("lat")};
        System.out.println(Arrays.toString(cordsF));
        double[] cordsT = new double[] {toJ.getJSONArray("locations").getJSONObject(0).getDouble("lon"),
                toJ.getJSONArray("locations").getJSONObject(0).getDouble("lat")};
        System.out.println(Arrays.toString(cordsT));
        double[] cordsM = new double[] {(cordsF[0] + cordsT[0])/2.,(cordsF[1] + cordsT[1])/2.};
        double dist = Math.sqrt(Math.pow((cordsT[0] - cordsF[0]),2) + Math.pow((cordsT[1] - cordsF[1]),2));
        double zoom = 1 + (1 - (dist / 180)) * 2.5;
        System.out.println(zoom + ", " + dist / 180);
        String apiKey = "5cf40b505b534c59902884cf8e9191bf";
        String url = "https://maps.geoapify.com/v1/staticmap?style=positron&width=750&height=500&center=lonlat:" + cordsM[0] + "," + cordsM[1] + "&zoom=" + zoom + "&marker=lonlat:" + cordsF[0] + "," + cordsF[1] + ";type:awesome;color:%23ff0f00;size:x-large;icon:plane|lonlat:" + cordsT[0] + "," + cordsT[1] + ";type:awesome;color:%23ff0f00;size:x-large;icon:plane&apiKey=" + apiKey;
        System.out.println(url);
        return url;
    }
    public static void main(String[] args){
        API api = new API();
    }

}
