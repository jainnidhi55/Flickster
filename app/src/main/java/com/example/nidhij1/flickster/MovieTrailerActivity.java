package com.example.nidhij1.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nidhij1.flickster.models.Config;
import com.example.nidhij1.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    //the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key_utb";

    //get the current movie
    Movie movie;

    //instance fields
    AsyncHttpClient client;

    //image config
    Config config;

    String vidID;

    //tag for logging from this activity
    public final static String TAG = "MovieTrailerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        //load the movie
        movie = new Movie();
        //initialize the client
        client = new AsyncHttpClient();
        // temporary test video id -- TODO replace with movie trailer video id
        final String videoId = getTrailer();

        //getTrailer();

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.api_key_utb), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });

    }

    //get the trailer
    private String getTrailer() {

        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        //create the URL
        String url = API_BASE_URL + String.format("/movie/%s/videos", movie.getId());
        //set the request parameters
        RequestParams params = new RequestParams();

        params.put(API_KEY_PARAM, getString(R.string.api_key_utb)); //API key, always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result set and create Movie objects
                    JSONObject resObj = results.getJSONObject(0);
                    vidID = resObj.getString("key");
                } catch (JSONException e) {
                    logError("Failed to get video id", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });

        return vidID;
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
