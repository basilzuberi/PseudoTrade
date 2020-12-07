package com.project.pseudotrade;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

//Reference: https://www.geeksforgeeks.org/image-slider-in-android-using-viewpager/

public class ImageSlider extends AppCompatActivity {

    ViewPager imageSlider;
    ImageSliderAdapter imageSliderAdapter;
    int[] tutorial = {R.drawable.main_1, R.drawable.stock1, R.drawable.stock2,
            R.drawable.stock3, R.drawable.stock4, R.drawable.stock5, R.drawable.stock6, R.drawable.stock7,
            R.drawable.stock8,  R.drawable.stock9, R.drawable.stock10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slider);
        imageSlider = (ViewPager)findViewById(R.id.viewImageSlider);
        imageSliderAdapter = new ImageSliderAdapter(ImageSlider.this, tutorial);
        imageSlider.setAdapter(imageSliderAdapter);
    }

}
