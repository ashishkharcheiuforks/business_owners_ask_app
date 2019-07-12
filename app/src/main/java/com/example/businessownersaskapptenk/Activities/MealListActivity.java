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

import com.example.businessownersaskapptenk.Adapters.MealAdapter;
import com.example.businessownersaskapptenk.ApiService;
import com.example.businessownersaskapptenk.ApiServiceBuilder;
import com.example.businessownersaskapptenk.JsonModelObject.Meal;
import com.example.businessownersaskapptenk.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealListActivity extends AppCompatActivity {
    private FloatingActionButton fabToDrinksActivity;
    private ArrayList<Meal> mealArrayList;
    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    private Meal[] listArr = new Meal[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        fabToDrinksActivity = findViewById(R.id.fab_to_drinks_activity);
        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("restaurantId");
        String restaurantName = intent.getStringExtra("restaurantName");
        getSupportActionBar().setTitle(restaurantName);
        mealArrayList = new ArrayList<>();
        mealAdapter = new MealAdapter(mealArrayList, this, restaurantId);
        recyclerView = findViewById(R.id.meal_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mealAdapter);
        getMeals(restaurantId);
        addSearchFunction();
        fabToDrinksActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MealListActivity.this, "Go to Drinks Activity", Toast.LENGTH_SHORT).show();
                Intent goToDrinkIntent = new Intent(MealListActivity.this, DrinkListActivity.class);
                goToDrinkIntent.putExtra("restaurantId", restaurantId);
                goToDrinkIntent.putExtra("restaurantName", restaurantName);
                startActivity(goToDrinkIntent);
                finish();
            }
        });
    }

    private void getMeals(String restaurantId) {
        //This is an instance of our ApiService, and we are gonna pass our API service class,
        ApiService apiService = ApiServiceBuilder.getService();
        // Next we are Calling our method on this API
        Call<Meal> mealCall = apiService.getMeals(Integer.parseInt(restaurantId));
        // Now we choose if we are going to call it synchronously or asynchronously
        // Since we are in an activity and an UI thread we need to do an Async with the method enqueue
        mealCall.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                //What happens when we get a reponse from our server
                for (int i = 0; i < response.body().getMealList().size(); i++) {
                    mealArrayList.add(response.body().getMealList().get(i));
                }
                listArr = new Meal[mealArrayList.size()];
                listArr = mealArrayList.toArray(listArr);
                mealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
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
                mealArrayList.clear();
                for (Meal m : listArr) {
                    if (m.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        mealArrayList.add(m);
                    }
                }
                mealAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}