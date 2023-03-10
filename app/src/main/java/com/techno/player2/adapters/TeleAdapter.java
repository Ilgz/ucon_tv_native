package com.techno.player2.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.player2.R;
import com.techno.player2.auxi.Xhelper;
import com.techno.player2.main_windows.TeleDetailActivity;
import com.techno.player2.main_windows.TeleDetailActivityVlc;
import com.techno.player2.models.Movie;

import java.util.List;

public class TeleAdapter extends RecyclerView.Adapter<TeleAdapter.MyViewHolder> {
    final Context context;
    final List<Movie> mData;

    public TeleAdapter(Context context, List<Movie> mData) {
        this.context = context;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tele_item, parent, false);

        return new MyViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(mData.get(position).getThumbnail()).into(holder.ImgMovie);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ImgMovie;

        public MyViewHolder(View itemview) {

            super(itemview);
            ImgMovie = itemview.findViewById(R.id.item_tele_img);
            itemview.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.k_shared), MODE_PRIVATE);
                boolean status = sharedPreferences.getBoolean("access", false);
                if (!status) {
                    new Xhelper().DialogBuild(context);
                }
                else {
                Intent intent;
                if ( sharedPreferences.getBoolean("isVlc", false)){
                    intent = new Intent(context, TeleDetailActivityVlc.class);
                }else{
                    intent = new Intent(context, TeleDetailActivity.class);
                }

                intent.putExtra("cater", mData.get(getAdapterPosition()).getTitle());
                context.startActivity(intent);
                }

            });

        }
    }
}