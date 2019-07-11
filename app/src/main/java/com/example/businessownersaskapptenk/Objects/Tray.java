package com.example.businessownersaskapptenk.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Tray {
    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "meal_id")
    private String mealId;

    @ColumnInfo(name = "meal_name")
    private String mealName;

    @ColumnInfo(name = "meal_price")
    private float mealPrice;

    @ColumnInfo(name = "meal_quantity")
    private int mealQuantity;

    @ColumnInfo(name = "drink_id")
    private String drinkId;

    @ColumnInfo(name = "drink_name")
    private String drinkName;

    @ColumnInfo(name = "drink_price")
    private float drinkPrice;

    @ColumnInfo(name = "drink_quantity")
    private int drinkQuantity;

    @ColumnInfo(name = "registration_id")
    private String restaurantId;

    public String getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(String drinkId) {
        this.drinkId = drinkId;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public float getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(float drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    public int getDrinkQuantity() {
        return drinkQuantity;
    }

    public void setDrinkQuantity(int drinkQuantity) {
        this.drinkQuantity = drinkQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public float getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(float mealPrice) {
        this.mealPrice = mealPrice;
    }

    public int getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(int mealQuantity) {
        this.mealQuantity = mealQuantity;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Tray{" +
                "id=" + id +
                ", mealId='" + mealId + '\'' +
                ", mealName='" + mealName + '\'' +
                ", mealPrice=" + mealPrice +
                ", mealQuantity=" + mealQuantity +
                ", drinkId='" + drinkId + '\'' +
                ", drinkName='" + drinkName + '\'' +
                ", drinkPrice=" + drinkPrice +
                ", drinkQuantity=" + drinkQuantity +
                ", restaurantId='" + restaurantId + '\'' +
                '}';
    }
}
