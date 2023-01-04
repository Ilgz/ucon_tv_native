package com.techno.player2.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecorator extends RecyclerView.ItemDecoration {
    public final int verticalSpaceHeight;
    public final int horizonatSpaceHeight;

    public SpacingItemDecorator(int verticalSpaceHeight,int horizonatSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizonatSpaceHeight = horizonatSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
         outRect.right=verticalSpaceHeight;
         outRect.bottom=horizonatSpaceHeight;

    }
}
