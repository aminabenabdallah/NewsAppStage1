package com.example.newsappstage1;
import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of NewsStory objects.
     */
    public static List<News> fetchNewsStoryData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of NewsStories
        List<News> newsStories = extractFeatureFromJson(jsonResponse);

        // Return the list of NewsStories
        return newsStories;
    }


    /**
     * Returns new URL object from the given String URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newsStory JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();


    }

    /**
     * Return a list of NewsStory objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsStoryJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsStoryJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news stories to
        List<News> newsStories = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsStoryJSON);

            //Create the JSONObject with the key "response"
            JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");
            //JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news stories.
            JSONArray newsStoryArray = responseJSONObject.getJSONArray("results");

            // For each newsStory in the newsStoryArray, create an NewsStory object
            for (int i = 0; i < newsStoryArray.length(); i++) {

                // Get a single newsStory at position i within the list of news stories
                JSONObject currentStory = newsStoryArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = currentStory.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String sectionName = currentStory.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String date = currentStory.getString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = currentStory.getString("webUrl");

                //Extract the JSONArray with the key "tag"
                JSONArray tagsArray = currentStory.getJSONArray("tags");

                //Declare String variable to hold author name
                String authorName = null;

                if (tagsArray.length() == 1) {
                    JSONObject contributorTag = (JSONObject) tagsArray.get(0);
                    authorName = contributorTag.getString("webTitle");
                }

                // Create a new NewsStory object with the title, section name, date,
                // and url from the JSON response.
                News newsStory = new News(title, sectionName, date, url, authorName);

                // Add the new NewsStory to the list of newsStories.
                newsStories.add(newsStory);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the newsStory JSON results", e);
        }

        // Return the list of earthquakes
        return newsStories;
    }


}




