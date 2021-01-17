package ro.mta.se.lab.controller;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import ro.mta.se.lab.model.City;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {

    @FXML
    private ChoiceBox<String> selectedCity=new ChoiceBox();

    @FXML
    private Label labelCity;
    @FXML
    private Label labelW;
    @FXML
    private Label labelMM;
    @FXML
    private Label labelW2;
    @FXML
    private Label labelHum;
    @FXML
    private Label labelPres;
    @FXML
    private Label labelWind;

    private String mFilename;
    private Object BufferedReader;

    private List<City>mCities=new ArrayList<>();
    public String getFilename(){return mFilename;}
    public void setFilename(String filename){this.mFilename=filename;}
    public WeatherController(){};

    private void loadCountries() throws IOException {
        BufferedReader buffer=new BufferedReader(new FileReader(getFilename()));
        String[] words=new String[100];
        String line="";
        line= buffer.readLine();
        //   System.out.println(line);
        while((line=buffer.readLine())!=null)
        {

            words=line.split("[\t ]+");

            String mId=words[0];
            String mName=words[1];
            String mLat=words[2];
            String mLon=words[3];
            String mCountry=words[4];
            City mCity=new City(mName,mCountry,Integer.parseInt(mId),mLat,mLon);
            mCities.add(mCity);



        }
        buffer.close();

        ArrayList<String>example=new ArrayList<String>();
        for(City c:mCities){

            //System.out.println(c.getName());
            example.add(c.getName());}
        for(String c:example)
            System.out.println(c);

        selectedCity.setItems(FXCollections.observableList(new ArrayList<String>(example)));

        //countryChoiceBox.setItems(FXCollections.observableList(new ArrayList<String>(countriesMap.keySet())));

        selectedCity.setOnAction(event -> {
            try {
                showWeather(selectedCity.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    @FXML
    private void initialize() throws IOException {
        setFilename("src/main/resources/in.txt");
        loadCountries();
    }

    private void showWeather(String city) throws IOException
    {
        StringBuilder json = new StringBuilder();
        {
            try {
                URL myUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=7847b657898cb5d8cfbaeca34aceffcf");
                URLConnection connection = myUrl.openConnection();


                InputStreamReader i = new InputStreamReader(connection.getInputStream());
                BufferedReader rd = new BufferedReader(i);


                String l;
                while ((l = rd.readLine()) != null)
                    json.append(l);
                rd.close();

                System.out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        labelCity.setText(city+", "+  Json.parse(String.valueOf(json)).asObject().get("sys").asObject().get("country").asString() );

        JsonArray main = Json.parse(String.valueOf(json)).asObject().get("weather").asArray();
        String w="null";
        for( JsonValue val : main )
        {
            w= val.asObject().getString("main","w");
        }
        labelW.setText(w);

        labelW2.setText("Current temperature : " + Json.parse(String.valueOf(json)).asObject().get("main").asObject().get("humidity").toString() + " K");

        labelHum.setText("Humidity: " + Json.parse(String.valueOf(json)).asObject().get("main").asObject().get("temp").toString());
        labelPres.setText("Pressure: " + Json.parse(String.valueOf(json)).asObject().get("main").asObject().get("pressure").toString());
        labelWind.setText("Wind speed: " + Json.parse(String.valueOf(json)).asObject().get("wind").asObject().get("speed").toString());

        String minMax= "Min temp: " +Json.parse(String.valueOf(json)).asObject().get("main").asObject().get("temp_min").toString() + " K" +
                "\nMax temp: " + Json.parse(String.valueOf(json)).asObject().get("main").asObject().get("temp_max").toString() + " K" ;
        labelMM.setText(minMax);
        //setText(Json.parse(String.valueOf(json)).asObject().get("weather").asArray());
    }
}
