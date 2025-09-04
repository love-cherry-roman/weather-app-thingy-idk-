package th.roman;
// me
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//json stufs
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.swing.*;
//idk but it needs it

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("enter city");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocation(430, 100);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 400, 20));
        panel.setBackground(new Color(245, 245, 255));
        frame.add(panel);

        JLabel label = new JLabel("pick a city!");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JTextField textfield = new JTextField(20);
        textfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textfield.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(textfield);

        JButton enter = new JButton("Enter");
        enter.setFont(new Font("SansSerif", Font.BOLD, 14));
        enter.setBackground(new Color(100, 149, 237)); // Cornflower blue
        enter.setForeground(Color.WHITE);
        enter.setFocusPainted(false);
        enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(enter);

        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BorderLayout());
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        weatherPanel.setBackground(new Color(230, 240, 255));
        panel.add(weatherPanel, BorderLayout.CENTER);



        // i should comment
        enter.addActionListener(e -> {
            String location = textfield.getText().trim();
            if (location.equalsIgnoreCase("No")) {
                System.exit(0);
            }

            JSONObject LocData = getLocationData(location);
            if (LocData == null) {
                JOptionPane.showMessageDialog(frame, "Location not found.");
                return;
            }

            double latitude = (double) LocData.get("latitude");
            double longitude = (double) LocData.get("longitude");

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

                double weUseCewseeusInBurrrmingUm = (double) currentWeatherJson.get("temperature_2m");

                double AmericaUnits = (double) currentWeatherJson.get("temperature_2m");

                long relativeHumidity = (long) currentWeatherJson.get("relative_humidity_2m");

                double windSpeed = (double) currentWeatherJson.get("wind_speed_10m");



                JLabel data = new JLabel();
                String weatherInfo = "<html><b>Weather Info:</b><br>Time: " + time +
                        "<br>Temperature: " + weUseCewseeusInBurrrmingUm + " °C" +
                        "<br>Humidity: " + relativeHumidity + " %" +
                        "<br>Wind Speed: " + windSpeed + " m/s</html>";
                data.setText(weatherInfo);
                data.setFont(new Font("SansSerif", Font.PLAIN, 14));
                weatherPanel.add(data, BorderLayout.CENTER);
                weatherPanel.revalidate();
                weatherPanel.repaint();


            }catch(Exception b){
                b.printStackTrace();
            }




        });
        frame.setVisible(true);
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