package com.codeimagination.properties.splash_screen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeimagination.properties.R;
import com.codeimagination.properties.signup_login.MainActivity;

public class Intro_Activity extends AppCompatActivity {

    private TextView[] dots;
    private LinearLayout dotsLayout;

    private ViewPager viewPager;
    private IntroManager introManager;

    private ViewpagerAdapter viewpagerAdapter;

    private int[] layouts;

    private Button next, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intro activity to check if user is opening the app for the first time
        introManager = new IntroManager(this);

        if (!introManager.check()){

            introManager.setFirst(false);
            Intent intent = new Intent(Intro_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        if (Build.VERSION.SDK_INT>21) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }

        setContentView(R.layout.activity_intro_);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layout_dots);
        next = findViewById(R.id.btn_next);
        skip = findViewById(R.id.btn_skip);


        layouts = new int[]{
                R.layout.layout_screen_1,
                R.layout.layout_screen_2,
                R.layout.layout_screen_3,
                R.layout.layout_screen_4,
                R.layout.layout_screen_5
        };

        addBottomDots(0);
        changeStatusBarColor();
        viewpagerAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intro_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(+1);

                if (current<layouts.length){

                    viewPager.setCurrentItem(current);

                } else{

                    Intent intent = new Intent(Intro_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });


    }



    private void addBottomDots(int position){

        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInActive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for(int i = 0; i < dots.length; ++i ){

            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));

            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInActive[position]);
            dotsLayout.addView(dots[i]);

        }

        if (dots.length>0)
            dots[position].setTextColor(colorActive[position]);

    }

    private int getItem(int i){

        return viewPager.getCurrentItem() + i;

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addBottomDots(i);
            if (i == layouts.length - 1){

                next.setText("FINISH");
                skip.setVisibility(View.GONE);

            }else {

                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

    };

    private void changeStatusBarColor(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }

    }

    public class ViewpagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts [position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {


            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;


        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            View view = (View) object;
            container.removeView(view);

        }


    }



}
