package com.example.moviesdirectory.Activities;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesdirectory.Data.MovieRecyclerViewAdapter;
import com.example.moviesdirectory.Model.Movie;
import com.example.moviesdirectory.R;
import com.example.moviesdirectory.Util.Constants;
import com.example.moviesdirectory.Util.Prefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue requestQueue;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showInputDialog();

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();
        //getMovieList(search);
        movieList = getMovieList(search);//movieList gonna have all the movies, returned by the getMovieList() fn
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
    }


    //Get Movies from the Api
    public List<Movie> getMovieList(String searchTerm) {
        movieList.clear(); //EveryTime a new search will be made, the previous data in the list would be cleared first

        //making JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray moviesArray = response.getJSONArray("Search");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject moviesObject = moviesArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(moviesObject.getString("Title"));
                        movie.setYear("Year Released: " + moviesObject.getString("Year"));
                        movie.setImdbId(moviesObject.getString("imdbID"));
                        movie.setMovieType("Type: " + moviesObject.getString("Type"));
                        movie.setPoster(moviesObject.getString("Poster"));

                        movieList.add(movie);

                        Log.d("Movie", movie.getTitle());


                    }
                    movieRecyclerViewAdapter.notifyDataSetChanged(); //Important
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error", "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
        return movieList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showInputDialog();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInputDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText editText = view.findViewById(R.id.searchEdit);
        Button submitButton = view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Prefs prefs = new Prefs(MainActivity.this);

                if (!editText.getText().toString().isEmpty()) {

                    String search = editText.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();

                    getMovieList(search);

                    movieRecyclerViewAdapter.notifyDataSetChanged(); //Very Important

                }

                alertDialog.dismiss();

            }
        });
    }
}
