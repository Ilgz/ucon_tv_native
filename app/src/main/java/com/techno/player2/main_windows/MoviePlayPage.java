package com.techno.player2.main_windows;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.common.io.BaseEncoding;
import com.techno.player2.R;
import com.techno.player2.custom_interfaces.EpisodeItemClick;
import com.techno.player2.ui.ChangeEpisodeDialog;
import com.techno.player2.utils.TimerFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class MoviePlayPage extends AppCompatActivity implements EpisodeItemClick {
    String contentType = "", description, translatorId, movieId;
    List<Integer> episodeList = new ArrayList<>();
    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;
    private boolean _isFullscreen = false;
    private TextView tvLength;
    private TextView tvPosition;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    private ImageView pausedIcon;
    private LinearLayout seekBarContainer;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        public void run() {
            seekBarContainer.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (seekBarContainer.getVisibility() == View.VISIBLE) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnable, 5000);
            if (keyCode == 19 && Objects.equals(contentType, "initCDNSeriesEvents")) {
                ChangeEpisodeDialog cdd = new ChangeEpisodeDialog(this, episodeList, this);
                cdd.show();
                return true;
            } else if (keyCode == 22) {
                seekPlayer(1);
                return true;
            } else if (keyCode == 21) {
                seekPlayer(-1);
                return false;
            } else if (keyCode == 20) {
                pauseOrPlay();
            } else if (keyCode == 23) {
                resizeScreen();
                return true;
            }
        } else {
            handler.postDelayed(runnable, 5000);
            seekBarContainer.setVisibility(View.VISIBLE);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_layout);
        initViews();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        executor.execute(() -> {

            try {
                Document document = Jsoup.connect(getIntent().getStringExtra("siteLink")).get();
                Elements metaElements = document.getElementsByTag("meta");
                for (Element element : metaElements) {
                    if (Objects.equals(element.attributes().get("property"), "og:type")) {
                        if (Objects.equals(element.attributes().get("content"), "video.tv_series")) {
                            contentType = "initCDNSeriesEvents";
                        } else {
                            contentType = "initCDNMoviesEvents";
                        }
                    }
                }
                description = document.getElementsByClass("b-post__description_text").get(0).text();
                if (document.getElementById("translators-list") != null) {
                    translatorId = document.getElementById("translators-list").children().first().attributes().get("data-translator_id");
                    String[] tmpList = document.select("script").toString().split(Pattern.quote("sof.tv." + contentType));
                    String tmp = tmpList[tmpList.length - 1].split(Pattern.quote("{"))[0];
                    movieId = tmp.split(",")[0].trim();
                } else {
                    String[] tmpList = document.select("script").toString().split("sof.tv." + contentType);
                    String tmp = tmpList[tmpList.length - 1].split("[{]")[0];
                    translatorId = tmp.split(",")[1].trim();
                    movieId = tmp.split(",")[0].trim();
                }
                movieId = movieId.replace("(", "");

            } catch (IOException e) {
                Log.d("Ilgiz", e.toString());
                e.printStackTrace();
            }
            handler.post(() -> {
                TextView textView = findViewById(R.id.text_view_movie);
                textView.setText(description);
                getStream("1", "1");
                if (contentType.equals("initCDNSeriesEvents")) {
                    getEpisodes();
                }
            });
        });
    }

    public List<List<String>> cartesianProduct(int repeat) {
        List<String> a = Arrays.asList("@", "#", "!", "^", "$");
        List<List<String>> ff = new ArrayList<>();
        if (repeat == 2) {
            for (String element : a) {
                for (String element2 : a) {
                    List<String> list = new ArrayList<>();
                    list.add(element);
                    list.add(element2);
                    ff.add(list);
                }
            }
        } else if (repeat == 3) {
            for (String element : a) {
                for (String element2 : a) {
                    for (String element3 : a) {
                        List<String> list = new ArrayList<>();
                        list.add(element);
                        list.add(element2);
                        list.add(element3);
                        ff.add(list);
                    }

                }
            }
        }
        return ff;
    }

    private void getEpisodes() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezka.ag/ajax/get_cdn_series/",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String episodes = jsonObject.getString("episodes");
                        Document document = Jsoup.parse(episodes);
                        Elements episodeElements = document.getElementsByClass("b-simple_episode__item");
                        for (Element element : episodeElements) {
                            int episode = Integer.parseInt(element.attributes().get("data-episode_id"));
                            episodeList.add(episode);
                        }
                    } catch (JSONException e) {
                        Log.d("Ilgiz", "error");
                        e.printStackTrace();
                    }
                    // Display the first 500 characters of the response string.
                }, error -> {
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("id", movieId);
                jsonBody.put("translator_id", translatorId);
                jsonBody.put("action", "get_episodes");
                return jsonBody;
            }
        };
        queue.add(stringRequest);
    }

    private void getStream(String season, String episode) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezka.ag/ajax/get_cdn_series/",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String rawList = jsonObject.getString("url");
                        List<String> trashCodesSet = new ArrayList<>();
                        for (int i = 2; i < 4; i++) {
                            String startChar = "";
                            for (List<String> chars : cartesianProduct(i)) {
                                byte[] dataBytes = String.join(startChar, chars).getBytes(StandardCharsets.UTF_8);
                                String newTrashCombo = BaseEncoding.base64().encode(dataBytes);
                                trashCodesSet.add(newTrashCombo);
                            }
                        }
                        String trashString = String.join("", rawList.replace("#h", "").split("//_//"));
                        for (String i : trashCodesSet) {
                            trashString = trashString.replaceAll(i, "");
                        }
                        List<String> finalString = Arrays.asList(String.valueOf(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(BaseEncoding.base64().decode(trashString)))).split(","));
                        String finalLink = "";
                        for (String element : finalString) {
                            if (element.split("\\[")[1].split("]")[0].equals("1080p")) {
                                finalLink = element.split("\\[")[1].split("]")[1].split(" or ")[1];
                                break;
                            }
                        }
                        if (Objects.equals(finalLink, "")) {
                            finalLink = finalString.get(finalString.size() - 1).split("\\[")[1].split("]")[1].split(" or ")[1];
                        }
                        playVideo(finalLink);
                    } catch (JSONException e) {
                        Log.d("Ilgiz", "error");
                        e.printStackTrace();
                    }
                    // Display the first 500 characters of the response string.
                }, error -> {
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("id", movieId);
                jsonBody.put("translator_id", translatorId);
                if (Objects.equals(contentType, "initCDNSeriesEvents")) {
                    jsonBody.put("season", season);
                    jsonBody.put("episode", episode);
                    jsonBody.put("action", "get_stream");
                } else {
                    jsonBody.put("action", "get_movie");
                }

                return jsonBody;
            }
        };
        queue.add(stringRequest);
    }

    private void playVideo(String videoLink) {
        pausedIcon.setVisibility(View.INVISIBLE);
        mMediaPlayer.setMedia(new Media(mLibVLC, Uri.parse(videoLink)));
        mMediaPlayer.play();
    }

    private void initViews() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        seekBarContainer = findViewById(R.id.seekBarContainer);
        pausedIcon = findViewById(R.id.paused_icon);
        seekBar = findViewById(R.id.seekBar);
        progressBar = findViewById(R.id.progressBar3);
        tvLength = findViewById(R.id.movie_length_indicator);
        tvPosition = findViewById(R.id.movie_position_indicator);
        ImageView imageView = findViewById(R.id.movie_play_img);
        TextView tvDetail = findViewById(R.id.movie_play_details);
        TextView tvTitle = findViewById(R.id.movie_play_title);
        Glide.with(this).load(getIntent().getStringExtra("poster")).into(imageView);
        tvTitle.setText(getIntent().getStringExtra("title"));
        if(!getIntent().getStringExtra("details").isEmpty()){
            List<String> detailGenres = Arrays.asList(getIntent().getStringExtra("details").split(","));
            String strDetail = "Год: " + detailGenres.get(0) + "\nСтрана: " + detailGenres.get(1) + "\nЖанр: " + detailGenres.get(2);
            tvDetail.setText(strDetail);
        }

        mLibVLC = new LibVLC(this);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        VLCVideoLayout vlcVideoLayout = findViewById(R.id.videoLayout);
        mMediaPlayer.attachViews(vlcVideoLayout, null, false, false);
        mMediaPlayer.getPlayerState();

        mMediaPlayer.setEventListener((event -> {
            System.out.println(event.getBuffering());
            String time = new TimerFormatter().formatterTime((mMediaPlayer.getLength() / 1000));
            String position = new TimerFormatter().formatterTime(mMediaPlayer.getTime() / 1000);
            if (!position.contentEquals(tvPosition.getText())) {
                seekBar.setMax((int) (mMediaPlayer.getLength() / 1000));
                seekBar.setProgress((int) (mMediaPlayer.getTime() / 1000));
                tvLength.setText(time);
                tvPosition.setText(position);
            }
            if (mMediaPlayer.isPlaying()) {

                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);

                }
            }
        }));
    }

    private void seekPlayer(int a) {
        if (mMediaPlayer.getTime() + (20 * 1000 * a) < 0) {
            mMediaPlayer.setTime(0);
        } else if (mMediaPlayer.getTime() + (20 * 1000 * a) < mMediaPlayer.getLength()) {
            mMediaPlayer.setTime(mMediaPlayer.getTime() + (20 * 1000 * a));
        }
    }

    private void pauseOrPlay() {
        if (mMediaPlayer.isPlaying()) {
            pausedIcon.setVisibility(View.VISIBLE);
            mMediaPlayer.pause();
        } else {
            pausedIcon.setVisibility(View.INVISIBLE);
            mMediaPlayer.play();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }

    @Override
    public void onEpisodeClick(int season, int episode) {
        getStream(String.valueOf(season), String.valueOf(episode));
    }

    private void resizeScreen() {
        LinearLayout leftSide = findViewById(R.id.left_side);
        LinearLayout wholeSide = findViewById(R.id.whole_side);
        ConstraintLayout moviePlayer = findViewById(R.id.movie_play_player);
        if (_isFullscreen) {
            leftSide.setVisibility(View.VISIBLE);
            int pad = this.getResources().getDimensionPixelSize(R.dimen.movie_page_padding);
            wholeSide.setPadding(pad, pad, pad, pad);
            moviePlayer.setLayoutParams(new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, this.getResources().getDimensionPixelSize(R.dimen.movie_player_height)));
            _isFullscreen = false;
        } else {
            leftSide.setVisibility(View.GONE);
            wholeSide.setPadding(0, 0, 0, 0);
            moviePlayer.setLayoutParams(new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
            _isFullscreen = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (_isFullscreen) {
            resizeScreen();
        } else {
            super.onBackPressed();
        }

    }
}
