package com.techno.player2.main_windows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techno.player2.R;
import com.techno.player2.adapters.MovieAdapter;
import com.techno.player2.auxi.Xhelper;
import com.techno.player2.custom_interfaces.CategoryItemClick;
import com.techno.player2.custom_interfaces.MovieItemClickList;
import com.techno.player2.data_source.ProducerRepo;
import com.techno.player2.models.Movie;
import com.techno.player2.ui.ChangeCategoryDialog;
import com.techno.player2.ui.ChangeEpisodeDialog;
import com.techno.player2.utils.SpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryPage extends AppCompatActivity implements CategoryItemClick, MovieItemClickList {
    private AlertDialog alertDialog;
    private RecyclerView recyclerView;
    int page=1;
    private static boolean isLoading = false;
    private long mLastKeyDownTime = 0;
    private String currentLink;
    private MovieAdapter movieAdapter;
    private String category;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long current = System.currentTimeMillis();
        boolean res = false;
        if (current - mLastKeyDownTime < 400) {
            res = true;
        } else {
            res = super.onKeyDown(keyCode, event);
            if(keyCode==20){
                if(!Objects.equals(category,"Подборки")){
                if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                    if (!isLoading) {
                        Log.d("Ilgiz","pending");
                        isLoading = true;
                        update(currentLink,false);
                    }
                    res=true;
                }
                }

            }
            mLastKeyDownTime = current;
        }
        return res;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        initViews();
    }
    private void initViews() {
        category = getIntent().getStringExtra("category");
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(0, 40);
        recyclerView=findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(itemDecorator);
        ImageView back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            onBackPressed();
        });
        ProducerRepo producerRepo=new ProducerRepo(category);
        TextView changeCategory=findViewById(R.id.tv_change_category);
        if(Objects.equals(category,"Подборки")){
            changeCategory.setVisibility(View.GONE);
        }else{
            changeCategory.setOnClickListener(v -> {
                ChangeCategoryDialog cdd = new ChangeCategoryDialog(this,(Objects.equals(category, "Мультфильмы") ?producerRepo.cartoonCategories():producerRepo.categories()), this);
                cdd.show();
            });
        }

        String categoryLink=(Objects.equals(category, "Мультфильмы") ?producerRepo.cartoonCategories().get(0).getLink():producerRepo.categories().get(0).getLink());
        currentLink=categoryLink;
        isLoading = true;
        if(Objects.equals(category,"Подборки")){
            SharedPreferences sharedPreferences=getSharedPreferences(getResources().getString(R.string.k_shared),MODE_PRIVATE);
            String link=sharedPreferences.getString("FilmUpdate","");
            loadHDFilms(link);
        }else{
            update(categoryLink,true);
        }

    }
    private  void loadHDFilms(String link){
          startLoadingDialog();
        List<Movie> movieList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(link, response -> {
            try {

                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String name = jsonObject.getString("Name");
                    String image = jsonObject.getString("ImageLink");
                    String site = jsonObject.getString("FilmLink");
                    String detail = jsonObject.getString("Details");
                    movieList.add(new Movie(name, image, site, detail+",-"));
                }


            }catch (JSONException e) {
                e.printStackTrace();
            }finally {
                alertDialog.dismiss();
                movieAdapter= new MovieAdapter(this, movieList, this);
                recyclerView.setAdapter(movieAdapter);
                recyclerView.requestFocus();
            }

        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }
    private void update(String link,boolean isInitial) {
        startLoadingDialog();
        String  url = "https://rezka.ag/" + link + "/page/"+page+"/";
        Log.d("Ilgiz",url);
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
                page++;
                isLoading=false;
                alertDialog.dismiss();
                if(isInitial){
                   movieAdapter= new MovieAdapter(this, movieList, this);
                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.requestFocus();

                }else{
                    movieAdapter.updateMovieList(movieList);
                }
            });
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

    @Override
    public void onCategoryClick(String categoryLink) {
        page=1;
        currentLink=categoryLink;
        isLoading = true;
     update(categoryLink,true);
    }

    @Override
    public void OnMovieClick(Movie movie, ImageView movieImageView) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.k_shared),MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("access", false);
        if (!status) {
            new Xhelper().DialogBuild(this);
        }
        else {
            Intent intent = new Intent(this, MoviePlayPage.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("poster", movie.getThumbnail());
            intent.putExtra("details", movie.getDetails());
            intent.putExtra("siteLink", movie.getSiteLink());
            startActivity(intent);
        }
    }
}