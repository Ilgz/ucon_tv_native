package com.techno.player2.main_windows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.techno.player2.R;
import com.techno.player2.adapters.MovieAdapter;
import com.techno.player2.adapters.SliderPageAdapter;
import com.techno.player2.adapters.TeleAdapter;
import com.techno.player2.auxi.Xhelper;
import com.techno.player2.custom_interfaces.MovieItemClickList;
import com.techno.player2.data_source.DataSourceTVChannelsAll;
import com.techno.player2.models.Movie;
import com.techno.player2.models.Slide;
import com.techno.player2.utils.SpacingItemDecorator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MovieItemClickList {
    private List<Slide> slideList;
    private ViewPager sliderPager;
    private RecyclerView rvPremiers, rvCartoons, rvSerials;
    private MovieAdapter adCartoons, adSerials;
    private static boolean isLoading = false;
    private int cartoonPage = 1;
    private int serialPage = 1;
    private AlertDialog alertDialog;
    private long mLastKeyDownTime = 0;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long current = System.currentTimeMillis();
        boolean res = false;
        if (current - mLastKeyDownTime < 400) {
            res = true;
        } else {
            res = super.onKeyDown(keyCode, event);
            if(keyCode==22){
                    if (!rvCartoons.canScrollHorizontally(RecyclerView.FOCUS_RIGHT)) {
                        if (!isLoading) {
                            ++cartoonPage;
                            getMovies("cartoons", cartoonPage, false);
                            isLoading = true;
                            startLoadingDialog();
                        }
                            res=true;
                    }
            else if (!rvSerials.canScrollHorizontally(RecyclerView.FOCUS_RIGHT)) {
                if (!isLoading) {
                    ++serialPage;
                    getMovies("series", serialPage, false);
                    isLoading = true;
                    startLoadingDialog();
                }
                        res=true;
            }

            }
            mLastKeyDownTime = current;
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayout toolbar = findViewById(R.id.toolbar);
        toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.dark_blue)));
        slideInitialization();
        initRecyclers();
        loadData();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
        System.out.println(sharedPreferences.getString("Slider","llll"));
        System.out.println(sharedPreferences.getBoolean("access",false));
    }

    private void slideInitialization() {

         sliderPager = findViewById(R.id.slider);
        slideList = new ArrayList<>();
        sliderPager.setPadding(300, 0, 300, 0);
        sliderPager.setClipToPadding(false);
        sliderPager.setClipChildren(false);
        sliderPager.setOffscreenPageLimit(3);
        Timer timer = new Timer();
        SliderTimer sliderTimer = new SliderTimer();
        timer.scheduleAtFixedRate(sliderTimer, 16000, 20000);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SliderPageAdapter adapter = new SliderPageAdapter(this, slideList);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, sharedPreferences.getString("Slider","https://www.jsonkeeper.com/b/1Q9X"), null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getString("sliderImage");
                    String poster = jsonObject.getString("image");
                    String site = jsonObject.getString("site");
                    slideList.add(new Slide(image, name,poster,site));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            sliderPager.setAdapter(adapter);

        }, error -> {
            slideList.add(new Slide(R.drawable.img));
            sliderPager.setAdapter(adapter);
        });
        requestQueue.add(requesting);
    }

    private void loadData() {
        getMovies("cartoons", 1, true);
        getMovies("premiers", 1, true);
        getMovies("series", 1, true);
    }

    private void getMovies(String category, int page, boolean isInitial) {
            String url;
            if (category.equals("premiers")) {
                url = "https://rezka.ag";
            } else {
                url = "https://rezka.ag/" + category + "/page/" + page + "/?filter=last";
            }
            List<Movie> movieList = new ArrayList<>();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                try {
                    Document document = Jsoup.connect(url).get();
                    if (category.equals("premiers")) {
                        for (int i = 0; i < document.getElementById("newest-slider-content").getElementsByClass("b-content__inline_item-cover").size(); i++) {
                            String name = document.getElementById("newest-slider-content").getElementsByClass("b-content__inline_item-link").get(i).children().first().text();
                            String image = document.getElementById("newest-slider-content").getElementsByClass("b-content__inline_item-cover").get(i).children().first().children().first().absUrl("src");
                            String site = document.getElementById("newest-slider-content").getElementsByClass("b-content__inline_item-cover").get(i).children().first().absUrl("href");
                            String detail= document.getElementById("newest-slider-content").getElementsByClass("b-content__inline_item-link").get(i).children().get(1).text();
                            movieList.add(new Movie(name, image, site,detail));
                        }
                    } else {
                        for (int i = 0; i < document.getElementsByClass("b-content__inline_item-cover").size(); i++) {
                            String name = document.getElementsByClass("b-content__inline_item-link").get(i).children().first().text();
                            String image = document.getElementsByClass("b-content__inline_item-cover").get(i).children().first().children().first().absUrl("src");
                            String site = document.getElementsByClass("b-content__inline_item-cover").get(i).children().first().absUrl("href");
                            String detail = document.getElementsByClass("b-content__inline_item-link").get(i).children().get(1).text();
                            movieList.add(new Movie(name, image, site, detail));
                        }
                        ;
                    }

                } catch (IOException e) {
                    Log.d("Ilgiz",e.toString());
                    e.printStackTrace();
                }
                handler.post(() -> {

                    if (category.equals("premiers")) {
                        MovieAdapter premierAdapter = new MovieAdapter(this, movieList, this);
                        rvPremiers.setAdapter(premierAdapter);
                    } else if (category.equals("cartoons")) {
                        if (isInitial) {
                            adCartoons = new MovieAdapter(this, movieList, this);
                            rvCartoons.setAdapter(adCartoons);
                        } else {
                            adCartoons.updateMovieList(movieList);
                            isLoading = false;
                            alertDialog.dismiss();
                        }
                    } else if (category.equals("series")) {
                        if (isInitial) {
                            adSerials = new MovieAdapter(this, movieList, this);
                            rvSerials.setAdapter(adSerials);
                        } else {
                            adSerials.updateMovieList(movieList);
                            isLoading = false;
                            alertDialog.dismiss();
                        }
                    }

                });
            });

    }

    private void initRecyclers() {
        ImageView toolbar_profile=findViewById(R.id.toolbar_profile);
        ImageView toolbar_search=findViewById(R.id.toolbar_search);
        toolbar_search.setOnClickListener(v -> {
            startActivity(new Intent(this,SearchPage.class));
        });
        toolbar_profile.setOnClickListener(v -> {
            startActivity(new Intent(this,ProfilePage.class));
        });

        ArrayList<Movie> movies=new ArrayList<>();
            movies.add(new Movie("Загрузка...","",""));
        MovieAdapter movieAdapter = new MovieAdapter(this, movies,this);
        SpacingItemDecorator vertical = new SpacingItemDecorator(15, 0);
        rvPremiers = findViewById(R.id.rv_premiers);
        rvPremiers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPremiers.addItemDecoration(vertical);
rvPremiers.setAdapter(movieAdapter);
        RecyclerView rvChannels = findViewById(R.id.rv_channels);
        rvChannels.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false));
        TeleAdapter teleAdapter = new TeleAdapter(this, DataSourceTVChannelsAll.getChannels());
        rvChannels.setAdapter(teleAdapter);
        rvCartoons = findViewById(R.id.rv_cartoons);
        rvCartoons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCartoons.addItemDecoration(vertical);
rvCartoons.setAdapter(movieAdapter);
        rvSerials = findViewById(R.id.rv_serials);
        rvSerials.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSerials.addItemDecoration(vertical);rvSerials.setAdapter(movieAdapter);
    }

    @Override
    public void OnMovieClick(Movie movie, ImageView movieImageView) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.k_shared),MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("access", false);
        if (!status) {
            new Xhelper().DialogBuild(this);
        }
        else{
            Intent intent=new Intent(this,MoviePlayPage.class);
            intent.putExtra("title",movie.getTitle());
            intent.putExtra("poster",movie.getThumbnail());
            intent.putExtra("details",movie.getDetails());
            intent.putExtra("siteLink",movie.getSiteLink());
            startActivity(intent);
        }

    }
      @Override
    public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Нажмите кнопку \"Назад\" еще раз,чтобы выйти из приложения.", Toast.LENGTH_LONG).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
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
    public  void goToCategoryHD(View view){
        SharedPreferences sharedPreferences=getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
        int vps=sharedPreferences.getInt("vps", 1);
        if(vps==1){
            new android.app.AlertDialog.Builder(this).setTitle("Нет доступа").setMessage("Доступно только для абонентов интернет провайдера UconNet").setPositiveButton("OK", (dialog, which) -> dialog.cancel()).create().show();
        }else{
            Intent intent=new Intent(this,CategoryPage.class);
            intent.putExtra("category","Подборки");
            startActivity(intent);
        }

    }
    public void goToCategory(View view) {
        TextView textView=findViewById(view.getId());
        Intent intent=new Intent(this,CategoryPage.class);
        intent.putExtra("category",String.valueOf(textView.getText()));
        startActivity(intent);
    }


    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(() -> {
                if (sliderPager.getCurrentItem() < slideList.size() - 1) {
                    sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);

                } else {
                    sliderPager.setCurrentItem(0);
                }
            });
        }
    }
}
