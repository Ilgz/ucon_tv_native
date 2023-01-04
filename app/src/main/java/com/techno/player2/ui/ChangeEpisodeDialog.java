package com.techno.player2.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.techno.player2.R;
import com.techno.player2.custom_interfaces.EpisodeItemClick;
import com.techno.player2.custom_interfaces.MovieItemClickList;

import java.util.ArrayList;
import java.util.List;

public class ChangeEpisodeDialog extends Dialog  {

    public Activity c;
    public Dialog d;
    int season =0;
    public List<List<Integer>> episodeList=new ArrayList<>();
    final EpisodeItemClick episodeItemClickList;
    public ChangeEpisodeDialog(Activity a, List<Integer> episodeList , EpisodeItemClick listener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.episodeItemClickList=listener;
        for(int x:episodeList){
            if(x==1){
                this.episodeList.add(new ArrayList<>());
            }
            this.episodeList.get(this.episodeList.size()-1).add(x);
        }

        Log.d("Ilgiz", String.valueOf(this.episodeList));
        Log.d("Ilgiz", String.valueOf(episodeList));
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        Log.d("Ilgiz","keyCode "+keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_episodes_dialog);
        ListView listView=findViewById(R.id.change_episode_listview);
        List<String> arrayList=new ArrayList<>();
        for(int i=0;i<episodeList.size();i++){
            arrayList.add("Сезон");
        }
        listView.setAdapter(new MyAdapter(this.getContext(),arrayList));
        listView.setOnItemClickListener((parent, view, position, id) ->{
            if(season==0){
                season=position+1;
                List<String> arrayListEpisodes=new ArrayList<>();
                for(int i=0;i<episodeList.get(position).size();i++){
                    arrayListEpisodes.add("Серия");
                }
                listView.setAdapter(new MyAdapter(this.getContext(),arrayListEpisodes));
            }else{
                episodeItemClickList.onEpisodeClick(season,position+1);
                dismiss();

            }
        });
    }




class MyAdapter extends ArrayAdapter<String> {
    private final Context context;
    List<String> myTitles=new ArrayList<>();

    MyAdapter(Context c, List<String> titles) {
        super(c, R.layout.row, R.id.text1, titles);
        this.context = c;
        this.myTitles = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.row, parent, false);
        TextView images = convertView.findViewById(R.id.logo);
        TextView myTitle = convertView.findViewById(R.id.text1);
        images.setText(String.valueOf(position + 1));
        myTitle.setText(myTitles.get(position));
        return convertView;
    }
}

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }
}
