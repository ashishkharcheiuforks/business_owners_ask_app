package com.example.businessownersaskapptenk.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MealModel {
    @SerializedName("meals")
    @Expose
    private ArrayList<MealModel> mealList;
    private String id, name, riko_description, spice_level, image;
    private Float price;

    public MealModel(ArrayList<MealModel> mealList, String id, String name, String riko_description, String spice_level, String image, Float price) {
        this.mealList = mealList;
        this.id = id;
        this.name = name;
        this.riko_description = riko_description;
        this.spice_level = spice_level;
        this.image = image;
        this.price = price;
    }

    public ArrayList<MealModel> getMealList() {
        return mealList;
    }

    public void setMealList(ArrayList<MealModel> mealList) {
        this.mealList = mealList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRiko_description() {
        return riko_description;
    }

    public void setRiko_description(String riko_description) {
        this.riko_description = riko_description;
    }

    public String getSpice_level() {
        return spice_level;
    }

    public void setSpice_level(String spice_level) {
        this.spice_level = spice_level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MealModel{" +
                "mealList=" + mealList +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", riko_description='" + riko_description + '\'' +
                ", spice_level='" + spice_level + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                '}';
    }
}