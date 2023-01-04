package com.techno.player2.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techno.player2.R;
import com.techno.player2.auxi.Xhelper;
import com.techno.player2.main_windows.MoviePlayPage;
import com.techno.player2.models.Slide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SliderPageAdapter extends PagerAdapter {
    private final Context mcontext;
    private final List<Slide> mList;
    private RequestQueue requestQueue;

    public SliderPageAdapter(Context mcontext, List<Slide> mList) {
        this.mcontext = mcontext;
        this.mList = mList;

    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View SlideLayout = inflater.inflate(R.layout.slide_item, container, false);
        ImageView slideImage = SlideLayout.findViewById(R.id.slider_img);
        TextView slideText = SlideLayout.findViewById(R.id.slide_title);
        if (mList.get(position).getDrawable() != 0) {
            slideImage.setImageResource(mList.get(position).getDrawable());
            slideText.setText("");
        } else {
            RequestOptions option = new RequestOptions();
            Glide.with(mcontext).load(mList.get(position).getImage()).apply(option).optionalCenterCrop().into(slideImage);
            slideText.setText(mList.get(position).getTitle());
            SharedPreferences sharedPreferences = mcontext.getSharedPreferences(mcontext.getString(R.string.k_shared), MODE_PRIVATE);
            SlideLayout.setOnClickListener(v -> {
                boolean status = sharedPreferences.getBoolean("access", false);
                if (!status) {
                    new Xhelper().DialogBuild(mcontext);
                } else {
                    Intent intent = new Intent(mcontext, MoviePlayPage.class);
                    intent.putExtra("title", mList.get(position).getTitle());
                    intent.putExtra("poster", mList.get(position).getPoster());
                    intent.putExtra("details", "");
                    intent.putExtra("siteLink", mList.get(position).getSiteLink());
                    mcontext.startActivity(intent);
                }

            });
        }


        container.addView(SlideLayout);
        return SlideLayout;


    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);

    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

}
