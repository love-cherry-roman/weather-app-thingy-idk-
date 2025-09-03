package th.roman;
// me
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//json stufs
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
//idk but it needs it

public class Main {
    public static void main(String[] args) {
        try{
            Scanner scanner = new Scanner(System.in);
            //scans when asked
            String location;
            //save this for later
            do{
                System.out.print("Enter city : ");
                location = scanner.nextLine();
                //take a wild guess
                if(location.equalsIgnoreCase("No")) break;
                //no means no
                JSONObject LocData = (JSONObject) getLocationData(location);
                //makes an array of allllll the data i think
                double latitude = (double) LocData.get("latitude");
                double longitude = (double) LocData.get("longitude");
                //you know
                displayWeatherData(latitude, longitude); //!!dont delete this again!!
                //it shows the data
            }while(!location.equalsIgnoreCase("No"));
            //do nothing when no is said

        }catch(Exception e){
            e.printStackTrace();
        }
        //if you're actually reading this put "api's are cool!" in the comment
    }

    private static JSONObject getLocationData(String location){
        location = location.replaceAll(" ", "+");
        //turns "new york" into "new+york" so the link works

        String URL_API = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                location + "&count=1&language=en&format=json";
        //gets the biblical textx (api link) and puts the location in it

        try{
            HttpURLConnection connectApiSOMETHING = fetchApiResponse(URL_API);
            //makes a request and uhhhhhhhh gets the response from the url for the api
            if(connectApiSOMETHING.getResponseCode() != 200){
                System.out.println("Error: api dumb");
                return null;
                //fun fact: HTTP 200 means it worked! it quite literally sends "OK" to the sending machine
            }
            String jsonResponse = readApiResponse(connectApiSOMETHING);
            //anytime i want the response it reads it (wow)
            JSONParser parser = new JSONParser();
            //I looked up parser because i actually didnt know what it meant
            //"a computer that parses"
            //oh google, such a help
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);
            //jargon
            //it seperates it into the values
            //but still jargon
            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return (JSONObject) locationData.get(0);

        }catch(Exception e){
            e.printStackTrace();
            //My computer science teacher said to put more comments
            //is this good enough Mr.L
            //is it
            //is it
            //is it good enough
            //do i still need more comments
        }
        return null;
    }

    private static void displayWeatherData(double latitude, double longitude){
        try{
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m";
            HttpURLConnection connectApiSOMETHING = fetchApiResponse(url);
            //gets the thing for the latitude and loooooongitude
            if(connectApiSOMETHING.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return;
                //200 = yay
            }
            String jsonResponse = readApiResponse(connectApiSOMETHING);
            //memory doesnt grow on trees
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current");
            //i'm not that smart
            String time = (String) currentWeatherJson.get("time");
            System.out.println("Current Time: " + time);
            //i think the time is wrong always

            double weUseCewseeusInBurrrmingUm = (double) currentWeatherJson.get("temperature_2m");
            System.out.println("Current Temperature (C): " + weUseCewseeusInBurrrmingUm);
            //bloimey mate i cont belive the amuricans use the far en hights for the tempacha

            double AmericaUnits = (double) currentWeatherJson.get("temperature_2m");
            System.out.println("Current Temperature (F): " + ((AmericaUnits*(1.8))+32));
            //idk which i should put
            long relativeHumidity = (long) currentWeatherJson.get("relative_humidity_2m");
            System.out.println("Relative Humidity: " + relativeHumidity);
            //jsut assume its 100
            double windSpeed = (double) currentWeatherJson.get("wind_speed_10m");
            System.out.println("Weather Description: " + windSpeed);
            //its too hot
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String readApiResponse(HttpURLConnection connectApiSOMETHING) {
        try {
            StringBuilder resultJson = new StringBuilder();
            //a string that can be (modified) woah!
            Scanner scanner = new Scanner(connectApiSOMETHING.getInputStream());
            //scans the input
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            //i had to marinate my eyes on the docs for a good chunk of this section
            scanner.close();
            return resultJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String URL_API){
        try{
            URL url = new URL(URL_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //this part too
            conn.setRequestMethod("GET");
            //GET
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}