package com.example.businessownersaskapptenk.Adapters;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//import com.example.businessownersaskapptenk.Activities.MealListActivity;
//import com.example.businessownersaskapptenk.Objects.Restaurant;
//import com.example.businessownersaskapptenk.R;
//
//public class RestaurantAdapter extends BaseAdapter {
//
//    private Activity activity;
//    private ArrayList<Restaurant> restaurantList;
//
//    public RestaurantAdapter(Activity activity, ArrayList<Restaurant> restaurantList) {
//        this.activity = activity;
//        this.restaurantList = restaurantList;
//    }
//
//    @Override
//    public int getCount() {
//        return restaurantList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return restaurantList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
//        if (view == null) {
//            view = LayoutInflater.from(activity).inflate(R.layout.list_item_restaurant, null);
//        }
//
//        final Restaurant restaurant = restaurantList.get(i);
//
//        TextView resName = (TextView) view.findViewById(R.id.res_name);
//        TextView resAddress = (TextView) view.findViewById(R.id.res_address);
//        ImageView resLogo = (ImageView) view.findViewById(R.id.res_logo);
//
//        resName.setText(restaurant.getName());
//        resAddress.setText(restaurant.getHobby());
//        Picasso.with(activity.getApplicationContext()).load(restaurant.getLogo()).fit().into(resLogo);
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Ovestrride
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, MealListActivity.class);
//                intent.putExtra("restaurantId", restaurant.getId());
//                intent.putExtra("restaurantName", restaurant.getName());
//                activity.startActivity(intent);
//            }
//        });
//
//        return view;
//    }
//}
