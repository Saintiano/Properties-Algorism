package com.codeimagination.properties.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeimagination.properties.R;
import com.codeimagination.properties.models.CreateProperty_Model;
import com.codeimagination.properties.props.Property_Details;
import com.codeimagination.properties.props.Update_Properties;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter_ViewProperty extends RecyclerView.Adapter<RecyclerViewAdapter_ViewProperty.MyViewHolder>{

    Context mContext;
    ArrayList<CreateProperty_Model> providers;

    public static final String KEY_NAME = "name";
    public static final String UNIQUE_KEY = "uniquekey";
    public static final String KEY_USERNAME = "url";

    public RecyclerViewAdapter_ViewProperty(Context context, ArrayList<CreateProperty_Model> changeProviders) {

        mContext = context;
        providers = changeProviders;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.property_card, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {

        CreateProperty_Model createProperty_model = providers.get(i);



        try{

            //check if there is image in url and set
            Picasso.get().load(createProperty_model.getProfileImageUrl()).into(viewHolder.property_preview_image_card);
            //Picasso.get().load(coverImageUrl).into(profile_Image_background);



        }catch (Exception e){

            //if no image set default image
            Picasso.get().load(R.drawable.profile_image).into(viewHolder.property_preview_image_card);
            //Picasso.get().load(R.drawable.profile_background).into(profile_Image_background);

        }

        viewHolder.property_name_tv.setText(createProperty_model.getProperty_name());

        viewHolder.property_price_card_tv.setText(createProperty_model.getProperty_price());
        viewHolder.property_description_card_tv.setText(createProperty_model.getProperty_description());
        viewHolder.seller_username_card_tv.setText(createProperty_model.getProperty_username());

        //get date
        viewHolder.bath_count_property_tv.setText(createProperty_model.getBath_count_property());
        viewHolder.parking_count_property.setText(createProperty_model.getParking_count_property());
        viewHolder.property_bed_count_card_tv.setText(createProperty_model.getBed_count_property());





        viewHolder.selling_properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext, (CharSequence) providers.get(i), Toast.LENGTH_SHORT).show();




                CreateProperty_Model createProperty_model = providers.get(i);

//
                Intent intent1 = new Intent(mContext, Update_Properties.class); //ACTIVITY_NUM = 0

                intent1.putExtra(KEY_NAME, createProperty_model.getProperty_name());
                intent1.putExtra(UNIQUE_KEY, createProperty_model.getProperty_uniqueKey());
                intent1.putExtra(KEY_USERNAME, createProperty_model.getUsername_uniqueKey());

                mContext.startActivity(intent1);


            }
        });

    }


    @Override
    public int getItemCount() {
        return providers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout parentLinearLayout;

        public CardView selling_properties;

        public CircularImageView property_preview_image_card;

        public String username_uniqueKey, property_uniqueKey;

        public TextView  property_name_tv, property_price_card_tv, property_description_card_tv,
                seller_username_card_tv, bath_count_property_tv, parking_count_property, property_bed_count_card_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            property_preview_image_card = itemView.findViewById(R.id.property_preview_image_card);
            property_name_tv = itemView.findViewById(R.id.property_name_tv);
            property_price_card_tv = itemView.findViewById(R.id.property_price_card_tv);
            property_description_card_tv = itemView.findViewById(R.id.property_description_card_tv);
            seller_username_card_tv = itemView.findViewById(R.id.seller_username_card_tv);
            bath_count_property_tv = itemView.findViewById(R.id.bath_count_property_tv);
            parking_count_property = itemView.findViewById(R.id.parking_count_property);
            property_bed_count_card_tv = itemView.findViewById(R.id.property_bed_count_card_tv);


            parentLinearLayout = itemView.findViewById(R.id.parentLinearLayout);
            selling_properties = itemView.findViewById(R.id.selling_properties);


        }
    }

}
