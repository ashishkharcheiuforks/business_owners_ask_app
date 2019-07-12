package com.example.businessownersaskapptenk.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.businessownersaskapptenk.Adapters.DrinkAdapter;
import com.example.businessownersaskapptenk.ApiService;
import com.example.businessownersaskapptenk.ApiServiceBuilder;
import com.example.businessownersaskapptenk.JsonModelObject.Drink;
import com.example.businessownersaskapptenk.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkListActivity extends AppCompatActivity {
    private FloatingActionButton fabToMealsActivity;
    private ArrayList<Drink> drinkArrayList;
    private RecyclerView recyclerView;
    private DrinkAdapter drinkAdapter;
    private Drink[] listArr = new Drink[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_list);
        fabToMealsActivity = findViewById(R.id.fab_to_meals_activity);
        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("restaurantId");
        String restaurantName = intent.getStringExtra("restaurantName");
        getSupportActionBar().setTitle(restaurantName);
        drinkArrayList = new ArrayList<>();
        drinkAdapter = new DrinkAdapter(drinkArrayList, this, restaurantId);
        recyclerView = findViewById(R.id.drink_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(drinkAdapter);
        getDrinks(restaurantId);
        addSearchFunction();
        fabToMealsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrinkListActivity.this, "Go to Meals Activity", Toast.LENGTH_SHORT).show();
                Intent goToMealIntent = new Intent(DrinkListActivity.this, MealListActivity.class);
                goToMealIntent.putExtra("restaurantId", restaurantId);
                goToMealIntent.putExtra("restaurantName", restaurantName);
                startActivity(goToMealIntent);
                finish();
            }
        });
    }

    private void getDrinks(String restaurantId) {
        //This is an instance of our ApiService, and we are gonna pass our API service class,
        ApiService apiService = ApiServiceBuilder.getService();
        // Next we are Calling our method on this API
        Call<Drink> drinkCall = apiService.getDrinks(Integer.parseInt(restaurantId));
        // Now we choose if we are going to call it synchronously or asynchronously
        // Since we are in an activity and an UI thread we need to do an Async with the method enqueue
        drinkCall.enqueue(new Callback<Drink>() {
            @Override
            public void onResponse(Call<Drink> call, Response<Drink> response) {
                //What happens when we get a reponse from our server
                for (int i = 0; i < response.body().getDrinkList().size(); i++) {
                    drinkArrayList.add(response.body().getDrinkList().get(i));
                }
                listArr = new Drink[drinkArrayList.size()];
                listArr = drinkArrayList.toArray(listArr);
                drinkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Drink> call, Throwable t) {
                // This throws all the http error codes, network failures, null exceptions
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addSearchFunction() {
        EditText searchInput = findViewById(R.id.events_search);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d("SEARCH", charSequence.toString());
                drinkArrayList.clear();
                for (Drink d : listArr) {
                    if (d.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        drinkArrayList.add(d);
                    }
                }
                drinkAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}