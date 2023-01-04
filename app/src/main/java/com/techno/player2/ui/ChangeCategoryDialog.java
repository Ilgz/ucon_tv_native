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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.techno.player2.R;
import com.techno.player2.custom_interfaces.CategoryItemClick;
import com.techno.player2.custom_interfaces.EpisodeItemClick;
import com.techno.player2.models.Producer;

import java.util.ArrayList;
import java.util.List;

public class ChangeCategoryDialog extends Dialog  {

    public Activity c;
    public Dialog d;
    public List<Producer> producerList=new ArrayList<>();
    final CategoryItemClick episodeItemClickList;
    public ChangeCategoryDialog(Activity a, List<Producer> producerList , CategoryItemClick listener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.episodeItemClickList=listener;
        this.producerList.addAll(producerList);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_episodes_dialog);
        ListView listView=findViewById(R.id.change_episode_listview);
        List<String> arrayList=new ArrayList<>();
        for(Producer x:producerList){
            arrayList.add("");
        }
        listView.setAdapter(new MyAdapter(this.getContext(),arrayList));
        listView.setOnItemClickListener((parent, view, position, id) ->{

                episodeItemClickList.onCategoryClick(producerList.get(position).getLink());
                dismiss();
        });
    }




class MyAdapter extends ArrayAdapter<String> {
    private final Context context;
    List<String> myTitles;

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
        myTitle.setText(producerList.get(position).getName());
        return convertView;
    }
}

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }
}
