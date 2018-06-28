package com.example.nidhij1.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.nidhij1.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    //the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    //instance fields
    AsyncHttpClient client;
    //the base url for loading images
    String imageBaseUrl;
    //the poster size to use when fetching images, part of the url
    String posterSize;
    //the list of currently playing movies
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();
        //get the configuration on app creation
        getConfiguration();

    }

    //get the list of currently playing movies from the API
    private void getNowPlaying() {
        //create the URL
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameters
        RequestParams params = new RequestParams();

        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    //get the configuration from the API
    private void getConfiguration(){
        //create the URL
        String url = API_BASE_URL + "/configuration";
        //set the request parameters
        RequestParams params = new RequestParams();
        Log.i(TAG, "test");
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("images");
                    //get the image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    //get the poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //use the option at index 3 or w342
                    posterSize = posterSizeOptions.optString(3,"w342");
                    Log.i(TAG, String.format("Loaded config with imageBaseUrl %s and posterSize $s", imageBaseUrl, posterSize));
                    //get the now playing list
                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }
        } );


    }

    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.i(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser) {
            //show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}