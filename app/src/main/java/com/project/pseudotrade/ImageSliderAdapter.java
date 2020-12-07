package com.project.pseudotrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;


//Reference: https://www.geeksforgeeks.org/image-slider-in-android-using-viewpager/

public class ImageSliderAdapter extends PagerAdapter {

    Context context;
    int[] tutorial;
    LayoutInflater layoutInflater;


    public ImageSliderAdapter(Context context, int[] tutorial) {
        this.context = context;
        this.tutorial = tutorial;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tutorial.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);
        imageView.setImageResource(tutorial[position]);
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
