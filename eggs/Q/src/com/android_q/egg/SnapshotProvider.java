package com.android_q.egg;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.dede.basic.DimenUtils;


public class SnapshotProvider extends com.dede.basic.provider.SnapshotProvider {

    @Override
    public boolean getIncludeBackground() {
        return true;
    }

    @NonNull
    @Override
    public View create(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.q_platlogo_snapshot_layout, null, false);

        Drawable mBackslash = new PlatLogoActivity.BackslashDrawable(DimenUtils.getDp(50));
        mBackslash.setAlpha(0x20);

        ImageView mOneView = view.findViewById(R.id.one);
        mOneView.setImageDrawable(new PlatLogoActivity.OneDrawable());
        ImageView mZeroView = view.findViewById(R.id.zero);
        mZeroView.setImageDrawable(new PlatLogoActivity.ZeroDrawable());

        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorBackground, value, true);
        int color = value.resourceId != 0 ? context.getResources().getColor(value.resourceId) : value.data;
        Drawable bg = new LayerDrawable(new Drawable[]{
                new ColorDrawable(color),
                mBackslash,
        });

        view.setBackground(bg);
        return view;
    }
}
