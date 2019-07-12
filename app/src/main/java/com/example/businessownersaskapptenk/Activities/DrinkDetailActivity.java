package com.example.businessownersaskapptenk.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businessownersaskapptenk.AppDatabase;
import com.example.businessownersaskapptenk.Objects.Tray;
import com.example.businessownersaskapptenk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.businessownersaskapptenk.Activities.SignInActivity.BUTTON_SKIPPED;

public class DrinkDetailActivity extends AppCompatActivity {
    private AppDatabase db;
    private static final String TAG = "lgx_DrinkDetailActivity";
    String drinkName;
    String restaurantId;
    String drinkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);
        Intent intent = getIntent();
        restaurantId = intent.getStringExtra("restaurantId");
        drinkId = intent.getStringExtra("drinkId");
        drinkName = intent.getStringExtra("drinkName");
        Log.d(TAG, "onCreate: getExtra drinkName --> " + drinkName);
        String drinkDescription = intent.getStringExtra("drinkDescription");
        final Float drinkPrice = intent.getFloatExtra("drinkPrice", 0);
        Toast.makeText(this, "drinkprice float DrinkDetailAct " + String.valueOf(drinkPrice), Toast.LENGTH_SHORT).show();
        String drinkImage = intent.getStringExtra("drinkImage");
        getSupportActionBar().setTitle(drinkName);
        TextView name = findViewById(R.id.drink_name);
        TextView desc = findViewById(R.id.drink_desc);
        final TextView price = findViewById(R.id.drink_price_detail);
        ImageView image = findViewById(R.id.drink_image);
        name.setText(drinkName);
        desc.setText(drinkDescription);
        price.setText("Rs." + drinkPrice);
        Picasso.with(getApplicationContext()).load(drinkImage).fit().centerInside().into(image);
        final TextView labelQuantity = findViewById(R.id.label_quantity);
        Button buttonIncrease = findViewById(R.id.button_increase);
        Button buttonDecrease = findViewById(R.id.button_decrease);
        Button buttonDrinkTray = findViewById(R.id.button_add_drink_tray);
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(labelQuantity.getText().toString());
                qty = qty + 1;
                labelQuantity.setText(qty + "");
                price.setText("Rs." + (qty * drinkPrice));
            }
        });
        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(labelQuantity.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    labelQuantity.setText(qty + "");
                    price.setText("Rs." + (qty * drinkPrice));
                }
            }
        });
        db = AppDatabase.getAppDatabase(this);
        buttonDrinkTray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BUTTON_SKIPPED) {
                    Toast.makeText(DrinkDetailActivity.this, "Login is required for add to cart", Toast.LENGTH_SHORT).show();
                    handleLoginRequired();
                } else {
                    BUTTON_SKIPPED = false;
                    int qty = Integer.parseInt(labelQuantity.getText().toString());
                    validateTray(drinkId, drinkName, drinkPrice, qty, restaurantId);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drink_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertTray(final String drinkId, final String drinkName, final float drinkPrice, final int drinkQty, final String restaurantId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Tray tray = new Tray();
                tray.setDrinkId(drinkId);
                tray.setDrinkName(drinkName);
                tray.setDrinkPrice(drinkPrice);
                tray.setDrinkQuantity(drinkQty);
                tray.setRestaurantId(restaurantId);
                db.trayDao().insertAll(tray);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(DrinkDetailActivity.this, "DRINK ADDED", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.tray_button == id) {
            if (BUTTON_SKIPPED) {
                Toast.makeText(this, "Login is required onOptionItemSelected", Toast.LENGTH_SHORT).show();
                handleLoginRequired();
            } else {
                BUTTON_SKIPPED = false;
                Intent intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
                intent.putExtra("screen", "tray");
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTray() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.trayDao().deleteAll();
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateTray(final int trayId, final int drinkQty) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.trayDao().updateDrinkTray(trayId, drinkQty);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(DrinkDetailActivity.this, "TRAY UPDATED", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void validateTray(final String drinkId, final String drinkName, final float drinkPrice, final int drinkQuantity, final String restaurantId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List<Tray> allTray = db.trayDao().getAll();
                if (allTray.isEmpty() || allTray.get(0).getRestaurantId().equals(restaurantId)) {
                    Tray tray = db.trayDao().getDrinkTray(drinkId);
                    if (tray == null) {
                        return "NOT_EXIST";
                    } else {
                        return tray.getId() + "";
                    }
                } else {
                    return "DIFFERENT_RESTAURANT";
                }
            }

            @Override
            protected void onPostExecute(final String result) {
                super.onPostExecute(result);
                if (result.equals("DIFFERENT_RESTAURANT")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DrinkDetailActivity.this);
                    builder.setTitle("Start New Cart?");
                    builder.setMessage("You are ordering from another department. Would you like to clean the current cart?");
                    builder.setPositiveButton("Cancel", null);
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteTray();
                            insertTray(drinkId, drinkName, drinkPrice, drinkQuantity, restaurantId);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (result.equals("NOT_EXIST")) {
                    insertTray(drinkId, drinkName, drinkPrice, drinkQuantity, restaurantId);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DrinkDetailActivity.this);
                    builder.setTitle("Add More?");
                    builder.setMessage("Your cart already has this product. Do you want to add more?");
                    builder.setPositiveButton("No", null);
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateTray(Integer.parseInt(result), drinkQuantity);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }.execute();
    }

    private void handleLoginRequired() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DrinkDetailActivity.this);
        builder.setTitle(getString(R.string.skip_login_required_title));
        builder.setMessage(getString(R.string.skip_login_required_message));
        builder.setPositiveButton("Cancel", null);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent loginIntent = new Intent(DrinkDetailActivity.this, SignInActivity.class);
                startActivity(loginIntent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
