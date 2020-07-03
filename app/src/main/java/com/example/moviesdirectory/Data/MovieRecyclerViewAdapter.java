package com.example.moviesdirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdirectory.Activities.MovieDetailActivity;
import com.example.moviesdirectory.Model.Movie;
import com.example.moviesdirectory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    @NonNull
    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);

        return new ViewHolder(view, context);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        String posterLink = movie.getPoster();//link of image url

        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());

        Picasso.get()
                .load(posterLink)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.poster);

        holder.year.setText(movie.getYear());


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView year;
        public TextView type;
        public ImageView poster;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.movie_title_id);
            year = itemView.findViewById(R.id.movie_release_id);
            type = itemView.findViewById(R.id.movie_cat_id);
            poster = itemView.findViewById(R.id.movie_image_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Movie movie = movieList.get(getAdapterPosition());

                    Intent intent = new Intent(context, MovieDetailActivity.class);

                    intent.putExtra("movie", movie);
                    ctx.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
