package com.codeimagination.properties.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeimagination.properties.R;
import com.codeimagination.properties.models.Dashboard_Slider_Items;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Dashboard_Slider_Adapter extends PagerAdapter {

    private Context context;
    private List<Dashboard_Slider_Items> dashboardSliderList;
    private LayoutInflater inflater;

    public Dashboard_Slider_Adapter(Context context, List<Dashboard_Slider_Items> dashboardSliderList) {
        this.context = context;
        this.dashboardSliderList = dashboardSliderList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dashboardSliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //inflates view
        View view = inflater.inflate(R.layout.view_pager_dashboard_items, container, false);

        //intantiate views
        ImageView view_pager_image = (ImageView) view.findViewById(R.id.dashboard_viewpager_image);
        TextView viewpager_title = (TextView) view.findViewById(R.id.viewpager_item_title);
        TextView viewpager_description = (TextView) view.findViewById(R.id.viewpager_item_description);
        FloatingActionButton btn_fav = (FloatingActionButton)view.findViewById(R.id.btn_fav);

        //event listener
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //set on click listener for slider items
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "slider clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //set data to views
        Picasso.get().load(dashboardSliderList.get(position).getImage()).into(view_pager_image);
        viewpager_title.setText(dashboardSliderList.get(position).getName());
        viewpager_description.setText(dashboardSliderList.get(position).getDescription());


        container.addView(view);

        return view;
    }



}
