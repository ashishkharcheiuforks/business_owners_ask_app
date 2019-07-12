package com.example.businessownersaskapptenk.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businessownersaskapptenk.Objects.Tray;
import com.example.businessownersaskapptenk.R;

import java.util.List;

public class TrayAdapter extends RecyclerView.Adapter<TrayAdapter.TrayViewHolder> {
    private List<Tray> trayModelList;
    private Activity activity;

    public TrayAdapter(List<Tray> trayModelList, Activity activity) {
        this.trayModelList = trayModelList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TrayAdapter.TrayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_tray, viewGroup, false);
        return new TrayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrayAdapter.TrayViewHolder trayViewHolder, int position) {
        if (trayModelList.get(position).getMealName() != null) {
            String mealplusdrinkNameText = trayModelList.get(position).getMealName();
            final String mealplusdrinkQuantityText = String.valueOf(trayModelList.get(position).getMealQuantity());
            String mealplusdrinkSubtotalText = String.valueOf((trayModelList.get(position).getMealPrice() * trayModelList.get(position).getMealQuantity()));
            trayViewHolder.setData(mealplusdrinkNameText, mealplusdrinkQuantityText, mealplusdrinkSubtotalText);
            trayViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, " Clicked on: MEALL!LL!L!L!L " + mealplusdrinkQuantityText + " " + mealplusdrinkNameText + " " + mealplusdrinkSubtotalText, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            String mealplusdrinkNameText = trayModelList.get(position).getDrinkName();
            final String mealplusdrinkQuantityText = String.valueOf(trayModelList.get(position).getDrinkQuantity());
            String mealplusdrinkSubtotalText = String.valueOf(((trayModelList.get(position).getDrinkPrice() * trayModelList.get(position).getDrinkQuantity())));
            trayViewHolder.setData(mealplusdrinkNameText, mealplusdrinkQuantityText, mealplusdrinkSubtotalText);
            trayViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, " Clicked on: DRINKKKKKKKKK" + mealplusdrinkQuantityText + " " + mealplusdrinkNameText + " " + mealplusdrinkSubtotalText, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return trayModelList.size();
    }

    public class TrayViewHolder extends RecyclerView.ViewHolder {
        private TextView mealplusdrinkName;
        private TextView mealplusdrinkQuantity;
        private TextView mealplusdrinkSubTotal;

        public TrayViewHolder(@NonNull View itemView) {
            super(itemView);
            mealplusdrinkName = (TextView) itemView.findViewById(R.id.tray_meal_plus_drink_name);
            mealplusdrinkQuantity = (TextView) itemView.findViewById(R.id.tray_meal_plus_drink_quantity);
            mealplusdrinkSubTotal = (TextView) itemView.findViewById(R.id.tray_meal_plus_drink_subtotal);
        }

        private void setData(String mealplusdrinkNameText, String mealplusdrinkQuantityText, String mealplusdrinkSubtotalText) {
            mealplusdrinkName.setText(mealplusdrinkNameText);
            mealplusdrinkQuantity.setText(mealplusdrinkQuantityText);
            mealplusdrinkSubTotal.setText(mealplusdrinkSubtotalText);
        }
    }
}
