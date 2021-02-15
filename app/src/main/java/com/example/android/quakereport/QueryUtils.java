package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }

    public static ArrayList<WordList> fetchEarthquakeData(String requesturl){
        URL url = createUrl(requesturl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream",e);
        }

        ArrayList<WordList> earthquakes = extractEarthquakesfromJson(jsonResponse);

        return earthquakes;
    }

    // Method to create URL Object from a given string

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url",e);
        }
        return url;
    }

    // Method to read data from a input stream

    private static String readFromStream(InputStream inputstream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputstream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputstream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Making an HTTP request to the server

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Error Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the JSON results",e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return a list of {@link WordList} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<WordList> extractEarthquakesfromJson(String SAMPLE_JSON_RESPONSE) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<WordList> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject rawJson = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features = rawJson.optJSONArray("features");
            for (int i = 0; i < features.length(); i++){
                JSONObject earthquake = features.getJSONObject(i);
                JSONObject properties = earthquake.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long timeinMil = properties.getInt("time");
                String url = properties.getString("url");

                //Converting UNIX time into MM-DD-YYYY
                Date dateObject = new Date(timeinMil);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd ,yyyy \n hh:mm aaa");
                String dateToDisplay = dateFormatter.format(dateObject);

                earthquakes.add(new WordList(mag,place,dateToDisplay,url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
