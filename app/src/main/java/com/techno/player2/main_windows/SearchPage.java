package com.techno.player2.main_windows;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.player2.R;
import com.techno.player2.adapters.MovieAdapter;
import com.techno.player2.custom_interfaces.MovieItemClickList;
import com.techno.player2.models.Movie;
import com.techno.player2.utils.SpacingItemDecorator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchPage extends AppCompatActivity implements MovieItemClickList {
    private RecyclerView recyclerView;
    private TextView textView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        initViews();

    }

    private void initViews() {
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(0, 40);
        recyclerView = findViewById(R.id.rv_search);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(itemDecorator);
        textView=findViewById(R.id.tv_search);
        LinearLayout toolbar = findViewById(R.id.toolbar);
        toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.dark_blue)));
        ImageView back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            onBackPressed();
        });
        EditText editText = findViewById(R.id.et_search);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(String.valueOf(v.getText()));
            }
            return false;
        });
    }
    void startLoadingDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }
    private void search(String query) {
        startLoadingDialog();
       String  url = "https://rezka.ag/search/?do=search&subaction=search&q=" + query + "&page=1";
        List<Movie> movieList = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                Document document = Jsoup.connect(url).get();

                for (int i = 0; i < document.getElementsByClass("b-content__inline_item-cover").size(); i++) {
                    String name = document.getElementsByClass("b-content__inline_item-link").get(i).children().first().text();
                    String image = document.getElementsByClass("b-content__inline_item-cover").get(i).children().first().children().first().absUrl("src");
                    String site = document.getElementsByClass("b-content__inline_item-cover").get(i).children().first().absUrl("href");
                    String detail = document.getElementsByClass("b-content__inline_item-link").get(i).children().get(1).text();
                    movieList.add(new Movie(name, image, site, detail));
                }

            } catch (IOException e) {
                Log.d("Ilgiz", e.toString());
                e.printStackTrace();
            }
            handler.post(() -> {
                alertDialog.dismiss();
                MovieAdapter searchAdapter = new MovieAdapter(this, movieList, this);
                recyclerView.setAdapter(searchAdapter);
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.requestFocus();

            });
        });
    }

    @Override
    public void OnMovieClick(Movie movie, ImageView movieImageView) {
        Intent intent=new Intent(this,MoviePlayPage.class);
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("poster",movie.getThumbnail());
        intent.putExtra("details",movie.getDetails());
        intent.putExtra("siteLink",movie.getSiteLink());
        startActivity(intent);
    }
}