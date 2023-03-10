package com.techno.player2.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.player2.R;
import com.techno.player2.custom_interfaces.MovieItemClickList;
import com.techno.player2.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>  {
    final Context context;
     List<Movie> mData;
    final MovieItemClickList movieItemClickList;

    public MovieAdapter(Context context, List<Movie> mData, MovieItemClickList listener) {
        this.context = context;
        this.mData = mData;
        movieItemClickList = listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      holder.TvTitle.setText(mData.get(position).getTitle());
        if(mData.get(position).getThumbnail().isEmpty()){
            holder.ImgMovie.setImageResource(R.drawable.sample_image);
        }else{
            Glide.with(context).load(mData.get(position).getThumbnail()).into(holder.ImgMovie);

        }
//        if(position==(mData.size()-1)){
//            mData.add(new Movie("Kotokbash","https://i.ibb.co/Pr1TT3r/Morbius-poster.jpg",""));
//        }
    }
    public void updateMovieList(List<Movie> movies){
        mData.addAll(movies);
        this.notifyItemChanged(0,"");
      //  this.notifyDataSetChanged();
    }
    public  void updateCategoryMovieList
            (List<Movie> movies){
        mData.addAll(movies);
          notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TvTitle;
        private final ImageView ImgMovie;

        public MyViewHolder(View itemview) {

            super(itemview);
            TvTitle = itemview.findViewById(R.id.item_movie_title);
            ImgMovie = itemview.findViewById(R.id.item_movie_img);
            itemview.setOnClickListener(v -> movieItemClickList.OnMovieClick(mData.get(getAdapterPosition()), ImgMovie));

        }
    }
}
