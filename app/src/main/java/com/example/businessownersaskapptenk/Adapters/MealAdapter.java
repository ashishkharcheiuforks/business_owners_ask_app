package com.example.businessownersaskapptenk.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.businessownersaskapptenk.Activities.MealDetailActivity;
import com.example.businessownersaskapptenk.JsonModelObject.Meal;
import com.example.businessownersaskapptenk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealModelList;
    private Activity activity;
    private String restaurantId;

    public MealAdapter(List<Meal> mealModelList, Activity activity, String restaurantId) {
        this.mealModelList = mealModelList;
        this.activity = activity;
        this.restaurantId = restaurantId;
    }

    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_meal, viewGroup, false);

        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealViewHolder mealViewHolder, final int position) {

        String mealNameText = mealModelList.get(position).getName();
          String mealDescText = mealModelList.get(position).getShortDescription();
        String mealPriceText = String.valueOf(mealModelList.get(position).getPrice());
          String mealLogoImage = mealModelList.get(position).getImage();

        mealViewHolder.setData(mealNameText, mealDescText, mealPriceText,mealLogoImage);
        mealViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MealDetailActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                intent.putExtra("mealId", mealModelList.get(position).getId());
                intent.putExtra("mealName", mealModelList.get(position).getName());
                intent.putExtra("mealDescription", mealModelList.get(position).getShortDescription());
                intent.putExtra("mealPrice", mealModelList.get(position).getPrice());
                intent.putExtra("mealImage", mealModelList.get(position).getImage());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealModelList.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {

        TextView mealName;
        TextView mealDesc;
        TextView mealPrice;
        ImageView mealImage;


        public MealViewHolder(@NonNull View view) {
            super(view);

            mealName = (TextView) view.findViewById(R.id.meal_name);
            mealDesc = (TextView) view.findViewById(R.id.meal_desc);
            mealPrice = (TextView) view.findViewById(R.id.meal_price);
            mealImage = (ImageView) view.findViewById(R.id.meal_image);
        }

        private void setData(String mealNameText, String mealDescText, String mealPriceText, String mealLogoImage) {

            mealName.setText(mealNameText);
            mealDesc.setText(mealDescText);
            mealPrice.setText(mealPriceText);
            Picasso.with(activity.getApplicationContext()).load(mealLogoImage).fit().centerInside().into(mealImage);

        }
    }

}