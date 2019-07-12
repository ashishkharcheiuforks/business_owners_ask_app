package com.example.businessownersaskapptenk.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.businessownersaskapptenk.Activities.DrinkDetailActivity;
import com.example.businessownersaskapptenk.JsonModelObject.Drink;
import com.example.businessownersaskapptenk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {
    private static final String TAG = "lgx_DrinkAdapter";
    private List<Drink> drinkModelList;
    private Activity activity;
    private String restaurantId;

    public DrinkAdapter(List<Drink> drinkModelList, Activity activity, String restaurantId) {
        this.drinkModelList = drinkModelList;
        this.activity = activity;
        this.restaurantId = restaurantId;
    }

    @NonNull
    @Override
    public DrinkAdapter.DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_drink, viewGroup, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkAdapter.DrinkViewHolder drinkViewHolder, final int position) {
        String drinkNameText = drinkModelList.get(position).getName();
        String drinkDescText = drinkModelList.get(position).getShortDescription();
        String drinkPriceText = String.valueOf(drinkModelList.get(position).getPrice());
        String drinkLogoImage = drinkModelList.get(position).getImage();
        drinkViewHolder.setData(drinkNameText, drinkDescText, drinkPriceText, drinkLogoImage);
        drinkViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DrinkDetailActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                intent.putExtra("drinkId", drinkModelList.get(position).getId());
                intent.putExtra("drinkName", drinkModelList.get(position).getName());
                Log.d(TAG, "onClick: putExtra drinkName --> " + drinkModelList.get(position).getName());
                intent.putExtra("drinkDescription", drinkModelList.get(position).getShortDescription());
                Log.d(TAG, "onClick: putExtra drinkDesc --> " + drinkModelList.get(position).getShortDescription());
                intent.putExtra("drinkPrice", drinkModelList.get(position).getPrice());
                intent.putExtra("drinkImage", drinkModelList.get(position).getImage());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinkModelList.size();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder {
        TextView drinkName;
        TextView drinkDesc;
        TextView drinkPrice;
        ImageView drinkImage;

        public DrinkViewHolder(@NonNull View view) {
            super(view);
            drinkName = (TextView) view.findViewById(R.id.drink_name);
            drinkDesc = (TextView) view.findViewById(R.id.drink_desc);
            drinkPrice = (TextView) view.findViewById(R.id.drink_price);
            drinkImage = (ImageView) view.findViewById(R.id.drink_image);
        }

        private void setData(String drinkNameText, String drinkDescText, String drinkPriceText, String drinkLogoImage) {
            drinkName.setText(drinkNameText);
            drinkDesc.setText(drinkDescText);
            drinkPrice.setText(drinkPriceText);
            Log.d(TAG, "setData: Drinks --> " + drinkNameText + drinkDescText + drinkPriceText);
            Picasso.with(activity.getApplicationContext()).load(drinkLogoImage).fit().centerInside().into(drinkImage);
        }
    }
}