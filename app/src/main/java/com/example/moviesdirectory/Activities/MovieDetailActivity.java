package com.example.moviesdirectory.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesdirectory.Model.Movie;
import com.example.moviesdirectory.R;
import com.example.moviesdirectory.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

public class MovieDetailActivity extends AppCompatActivity {


    private Movie movie;
    private TextView movieTitle;
    private ImageView movieImage;
    private TextView movieYear;
    private TextView director;
    private TextView actors;
    private TextView category;
    private TextView rating;
    private TextView writer;
    private TextView plot;
    private TextView boxOffice;
    private TextView runTime;

    private RequestQueue requestQueue;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        requestQueue = Volley.newRequestQueue(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();

        setUpUi();
        getMovieDetails(movieId);

    }

    private void getMovieDetails(String id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + id + Constants.API_KEY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    /**Some of the response objects might not have the Ratings array. So, in order to avoid any error, we can explicitly
                     * check for a particular property in the response object and act accordingly*/


                    if (response.has("Ratings")) {
                        JSONArray ratingsArray = response.getJSONArray("Ratings");

                        String source = null;
                        String value = null;
                        if (ratingsArray.length() > 0) {

                            JSONObject ratingsObject = ratingsArray.getJSONObject(ratingsArray.length() - 1);
                            source = ratingsObject.getString("Source");
                            value = ratingsObject.getString("Value");

                            rating.setText(MessageFormat.format("{0} : {1}", source, value));
                        } else {
                            rating.setText("N/A");
                        }

                        movieTitle.setText(response.getString("Title"));
                        movieYear.setText(String.format("%s%s", getString(R.string.year) + " ", response.getString("Year")));
                        director.setText(String.format("%s%s", getString(R.string.director) + " ", response.getString("Director")));
                        writer.setText(String.format("%s%s", getString(R.string.writer) + " ", response.getString("Writer")));
                        plot.setText(String.format("%s%s", getString(R.string.plot) + " ", response.getString("Plot")));
                        runTime.setText(String.format("%s%s", getString(R.string.runtime) + " ", response.getString("Runtime")));
                        actors.setText(String.format("%s%s", getString(R.string.actors) + " ", response.getString("Actors")));


                        Picasso.get()
                                .load(response.getString("Poster"))
                                .into(movieImage);

                        boxOffice.setText(response.getString("BoxOffice"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error: ", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setUpUi() {

        movieTitle = findViewById(R.id.movieTitleIdDet);
        movieImage = findViewById(R.id.movieImageIdDet);
        movieYear = findViewById(R.id.movieReleaseIdDet);
        director = findViewById(R.id.movieDirectedByDet);
        actors = findViewById(R.id.actorsDet);
        category = findViewById(R.id.movieCategoryIdDet);
        rating = findViewById(R.id.movieRatingDet);
        writer = findViewById(R.id.writersDet);
        plot = findViewById(R.id.plotDet);
        boxOffice = findViewById(R.id.boxOfficeDet);
        runTime = findViewById(R.id.movieRuntimeDet);

    }
}
