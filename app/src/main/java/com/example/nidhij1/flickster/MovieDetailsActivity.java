package com.example.nidhij1.flickster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nidhij1.flickster.models.Movie;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    //declare a new field for the movie
    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView releaseDate;
    ImageView movieImage;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        movieImage = (ImageView) findViewById(R.id.trailerPreview);


        context = getApplicationContext();

        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPic(view, movie);
            }
        });


        //set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2.0));
        releaseDate.setText(String.format("Release date: %s", movie.getReleaseDate()));
        ButterKnife.bind(this);


    }


    //@onClick(R.id.trailerPreview)
    public void clickPic(View v, Movie movieP){
        //create the intent to display moviedetailsact
        Intent intent = new Intent(context, MovieTrailerActivity.class);
        //pass the movie as an extra serialized via parcels.wrap
        intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movieP));
        //show the activity
        context.startActivity(intent);
    }

    /*public class ViewHolder implements View.OnClickListener {

        youtubePlayer =

        @Override
        public void onClick(View view) {
            //create the intent to display moviedetailsact
            Intent intent = new Intent(context, MovieTrailerActivity.class);
            //pass the movie as an extra serialized via parcels.wrap
            intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
            //show the activity
            context.startActivity(intent);
        }
    }*/
}
