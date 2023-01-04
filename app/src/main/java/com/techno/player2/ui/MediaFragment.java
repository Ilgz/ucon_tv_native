//package com.techno.player2.ui;
//
//import static android.content.Context.MODE_PRIVATE;
//import static android.view.View.INVISIBLE;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.SearchView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.MenuItemCompat;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager.widget.ViewPager;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.Volley;
//import com.techno.player2.R;
//import com.techno.player2.adapters.MovieAdapter;
//import com.techno.player2.adapters.SliderPageAdapter;
//import com.techno.player2.adapters.StoriesAdapter;
//import com.techno.player2.auxi.JavaStoriesDecoration;
//import com.techno.player2.auxi.Xhelper;
//import com.techno.player2.custom_interfaces.API_SlideRetrofitModel;
//import com.techno.player2.custom_interfaces.MovieItemClickList;
//import com.techno.player2.data_source.DataSourceCartoons;
//import com.techno.player2.data_source.DataSourceMovies;
//import com.techno.player2.data_source.DataSourceSeries;
//import com.techno.player2.data_source.DataSourceTelePrograms;
//import com.techno.player2.models.Movie;
//import com.techno.player2.models.Slide;
//import com.techno.player2.models.SlideRetrofitModel;
//import com.techno.player2.models.Stories;
//import com.techno.player2.utils.SpacingItemDecorator;
//
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//
//public class MediaFragment extends Fragment implements MovieItemClickList {
//    private final List<Movie> listMovies = new ArrayList<>();
//    private final List<Movie> listSerials = new ArrayList<>();
//    private final List<Movie> listCartoons = new ArrayList<>();
//    private final List<Movie> listTelePrograms = new ArrayList<>();
//    private View v;
//    private String name;
//    private String link;
//    private RecyclerView MoviesRv, moviesRvPop, cartoonsRv, allRv, Tele_programsRV;
//    private List<Slide> slideList;
//    private ViewPager sliderPager;
//    private MovieAdapter popularAdapter;
//    private MovieAdapter movieAdapter;
//    private MovieAdapter cartoonAdapter;
//    private MovieAdapter allAdapter;
//    private MovieAdapter tele_programAdapter;
//    private TextView one,two,three,four;
//    private RequestQueue requestQueue;
//    private RequestQueue requestQueueTrailers;
//    private String ImageLink;
//    private String SiteLink;
//
//    public MediaFragment() {
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.bottomnavlay, container, false);
//        SpacingItemDecorator vertical = new SpacingItemDecorator(15, 0);
//        SpacingItemDecorator horizontal = new SpacingItemDecorator(0, 15);
//        if (getActivity() != null) {
//            inView();
//            inCartoons();
//            MoviesRv.addItemDecoration(vertical);
//            cartoonsRv.addItemDecoration(vertical);
//            moviesRvPop.addItemDecoration(vertical);
//            Tele_programsRV.addItemDecoration(vertical);
//            allRv.addItemDecoration(horizontal);
//
//        }
//
//
//        return v;
//    }
//

//
//
//    private void inView() {
//
//        Tele_programsRV = v.findViewById(R.id.rv_teleprograms);
//        MoviesRv = v.findViewById(R.id.Rv_telik);
//        moviesRvPop = v.findViewById(R.id.rv_movies_popular);
//        cartoonsRv = v.findViewById(R.id.rv_cartoons);
//        MoviesRv.requestFocus();
//        allRv = v.findViewById(R.id.AllRec);
//        one = v.findViewById(R.id.tvFilms);
//        two = v.findViewById(R.id.tvSerials);
//        three = v.findViewById(R.id.tvMultfilms);
//        four=v.findViewById(R.id.tv_teleprograms);
//        one.setOnClickListener(v1 -> {
//            SharedPreferences shared = getActivity().getSharedPreferences("Category", MODE_PRIVATE);
//            SharedPreferences.Editor edit = shared.edit();
//            edit.putString("key", "one");
//            edit.putString("title", "Фильмы");
//            edit.apply();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PokazFirst()).commit();
//            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setNavigationIcon(R.drawable.ic_back_button);
//        });
//
//        two.setOnClickListener(v1 -> {
//            SharedPreferences shared = getActivity().getSharedPreferences("Category", MODE_PRIVATE);
//            SharedPreferences.Editor edit = shared.edit();
//            edit.putString("key", "two");
//            edit.putString("title", "Сериалы");
//            edit.apply();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PokazFirst()).commit();
//            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setNavigationIcon(R.drawable.ic_back_button);
//        });
//        three.setOnClickListener(v1 -> {
//            SharedPreferences shared = getActivity().getSharedPreferences("Category", MODE_PRIVATE);
//            SharedPreferences.Editor edit = shared.edit();
//            edit.putString("key", "three");
//            edit.putString("title", "Мультфильмы");
//
//            edit.apply();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PokazFirst()).commit();
//            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setNavigationIcon(R.drawable.ic_back_button);
//        });
//        four.setOnClickListener(v1 -> {
//            SharedPreferences shared = getActivity().getSharedPreferences("Category", MODE_PRIVATE);
//            SharedPreferences.Editor edit = shared.edit();
//            edit.putString("key", "eleven");
//            edit.putString("title", "Телепередачи");
//            edit.apply();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PokazFirst()).commit();
//            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setNavigationIcon(R.drawable.ic_back_button);
//        });
//
//    }
//
//
//    private void inCartoons() {
//        addingNewFilms("Films_Code");
//        addingNewFilms("Serial_Code");
//        addingNewFilms("TelePrograms_Code");
//        addingNewFilms("Cartoons_Code");
//        addingNewFilms("All_Code");
//    }
//
//
//    private void addingNewFilms(String constructor) {
//        DataSourceSeries dataSourceSeries = new DataSourceSeries();
//        DataSourceCartoons dataSourceCartoons = new DataSourceCartoons();
//        DataSourceMovies dataSourceMovies = new DataSourceMovies();
//        DataSourceTelePrograms dataSourceTelePrograms = new DataSourceTelePrograms();
//        String url = "";
//        requestQueue = Volley.newRequestQueue(getActivity());
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Category", MODE_PRIVATE);
//        String FilmUpdate = sharedPreferences.getString("FilmUpdate", "https://jsonkeeper.com/b/Y322");
//        String SerialUpdate = sharedPreferences.getString("SerialUpdate", "https://jsonkeeper.com/b/Y322");
//        String CartoonUpdate = sharedPreferences.getString("CartoonUpdate", "https://jsonkeeper.com/b/Y322");
//        String TeleProgramsUpdate = sharedPreferences.getString("TeleProgram", "https://jsonkeeper.com/b/Y322");
//        if (constructor.equals("Films_Code")) {
//            listMovies.addAll(dataSourceMovies.getNewestMoviesAll());
//
//            movieAdapter = new MovieAdapter(getActivity(), listMovies, this);
//
//            MoviesRv.setAdapter(movieAdapter);
//            MoviesRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            url = FilmUpdate;
//
//        } else if (constructor.equals("TelePrograms_Code")) {
//            listTelePrograms.addAll(dataSourceTelePrograms.getListPrograms());
//            tele_programAdapter = new MovieAdapter(getActivity(), listTelePrograms, this);
//            Tele_programsRV.setAdapter(tele_programAdapter);
//            Tele_programsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            url = TeleProgramsUpdate;
//        } else if (constructor.equals("Serial_Code")) {
//            listSerials.addAll(dataSourceSeries.getSeries());
//            popularAdapter = new MovieAdapter(getActivity(), listSerials, this);
//            moviesRvPop.setAdapter(popularAdapter);
//            moviesRvPop.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            url = SerialUpdate;
//        } else if (constructor.equals("Cartoons_Code")) {
//            listCartoons.addAll(dataSourceCartoons.getCartoons());
//            cartoonAdapter = new MovieAdapter(getActivity(), listCartoons, this);
//            cartoonsRv.setAdapter(cartoonAdapter);
//            cartoonsRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            url = CartoonUpdate;
//        }
//        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
//            try {
//                for (int i = 0; i < response.length(); i++) {
//                    JSONObject jsonObject = response.getJSONObject(i);
//                    String Title = jsonObject.getString("Name");
//                    String ImgLink = jsonObject.getString("ImageLink");
//                    String SiteLink = jsonObject.getString("SiteLink");
//                    if (constructor.equals("Films_Code")) {
//                        listMovies.add(0, new Movie(Title, ImgLink, SiteLink));
//                    } else if (constructor.equals("Serial_Code")) {
//                        listSerials.add(0, new Movie(Title, ImgLink, SiteLink));
//                    } else if (constructor.equals("Cartoons_Code")) {
//                        listCartoons.add(0, new Movie(Title, ImgLink, SiteLink));
//                    } else if (constructor.equals("TelePrograms_Code")) {
//                        listTelePrograms.add(0, new Movie(Title, ImgLink, SiteLink));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            requestQueue.stop();
//        }, error -> {
//            requestQueue.stop();
//
//        });
//        requestQueue.add(requesting);
//        requestQueue.addRequestFinishedListener(request -> {
//            if (constructor.equals("Films_Code")) {
//                movieAdapter = new MovieAdapter(getActivity(), listMovies, this);
//                MoviesRv.setAdapter(movieAdapter);
//            } else if (constructor.equals("Serial_Code")) {
//                popularAdapter = new MovieAdapter(getActivity(), listSerials, this);
//                moviesRvPop.setAdapter(popularAdapter);
//            } else if (constructor.equals("Cartoons_Code")) {
//                cartoonAdapter = new MovieAdapter(getActivity(), listCartoons, this);
//                cartoonsRv.setAdapter(cartoonAdapter);
//                final List<Movie> listAllSpeak = new ArrayList<>();
//                final List<Movie> listAllMain = new ArrayList<>();
//                listAllSpeak.addAll(listMovies);
//                listAllSpeak.addAll(listSerials);
//                listAllMain.addAll(listAllSpeak);
//                listAllMain.addAll(listCartoons);
//                allAdapter = new MovieAdapter(getActivity(), listAllMain, this);
//                allRv.setAdapter(allAdapter);
//                allRv.setLayoutManager(new GridLayoutManager(getActivity(), 7, RecyclerView.VERTICAL, false));
//            } else if (constructor.equals("TelePrograms_Code")) {
//                tele_programAdapter = new MovieAdapter(getActivity(), listTelePrograms, this);
//                Tele_programsRV.setAdapter(tele_programAdapter);
//
//            }
//            MoviesRv.requestFocus();
//
//
//        });
//    }
//
//    @Override
//    public void OnMovieClick(Movie movie, ImageView movieImageView) {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("access", MODE_PRIVATE);
//        boolean status = sharedPreferences.getBoolean("access", false);
//        if (!status) {
//            new Xhelper().DialogBuild(getContext());
//
//        } else {
//            Intent intent = new Intent(getActivity(), Tranzit.class);
//            intent.putExtra("title", movie.getTitle());
//            intent.putExtra("imgURL", movie.getThumbnail());
//            intent.putExtra("siteLink", movie.getSiteLink());
//            if (movie.getTitle().contains("Сезон")) {
//                intent.putExtra("type", "s");
//            } else {
//                intent.putExtra("type", "nes");
//            }
//
//            startActivity(intent);
//            getActivity().finish();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.P)
//    @SuppressLint("ResourceAsColor")
//    @SuppressWarnings("deprecation")
//    @Override
//    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu, menu);
//
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setFocusable(false);
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                cartoonAdapter.getFilter().filter(query);
//                movieAdapter.getFilter().filter(query);
//                popularAdapter.getFilter().filter(query);
//                allAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                cartoonAdapter.getFilter().filter(newText);
//                movieAdapter.getFilter().filter(newText);
//                popularAdapter.getFilter().filter(newText);
//                allAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        searchView.setOnSearchClickListener(view -> {
//            menu.findItem(R.id.action_profile).setVisible(false);
//            storiesBar.setVisibility(View.GONE);
//            sliderPager.setVisibility(View.GONE);
//            MoviesRv.setVisibility(View.GONE);
//            moviesRvPop.setVisibility(View.GONE);
//            cartoonsRv.setVisibility(View.GONE);
//            one.setVisibility(View.GONE);
//            two.setVisibility(View.GONE);
//            three.setVisibility(View.GONE);
//            allRv.setVisibility(View.VISIBLE);
//            Tele_programsRV.setVisibility(View.GONE);
//            four.setVisibility(View.GONE);
//
//            SharedPreferences shared = getActivity().getSharedPreferences("Category", MODE_PRIVATE);
//            SharedPreferences.Editor edit = shared.edit();
//
//            edit.putString("key", "four");
//            edit.putString("title", "Триллеры");
//            edit.apply();
//        });
//        searchView.setOnCloseListener(() -> {
//            storiesBar.setVisibility(View.VISIBLE);
//            sliderPager.setVisibility(View.VISIBLE);
//            MoviesRv.setVisibility(View.VISIBLE);
//            moviesRvPop.setVisibility(View.VISIBLE);
//            cartoonsRv.setVisibility(View.VISIBLE);
//            Tele_programsRV.setVisibility(View.VISIBLE);
//
//            one.setVisibility(View.VISIBLE);
//            two.setVisibility(View.VISIBLE);
//            three.setVisibility(View.VISIBLE);
//            four.setVisibility(View.VISIBLE);
//            allRv.setVisibility(INVISIBLE);
//            return false;
//        });
//
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_profile:
//                startActivity(new Intent(getContext(), ProfileFragment.class));
//                break;
//        }
//        return true;
//    }
//
//
//    class SliderTimer extends TimerTask {
//
//        @Override
//        public void run() {
//            if (getActivity() != null) getActivity().runOnUiThread(() -> {
//                if (sliderPager.getCurrentItem() < slideList.size() - 1) {
//                    sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);
//
//                } else {
//                    sliderPager.setCurrentItem(0);
//                }
//            });
//        }
//    }
//
//
//}
