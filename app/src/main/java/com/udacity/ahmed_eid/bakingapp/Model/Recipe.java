package com.udacity.ahmed_eid.bakingapp.Model;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private int rId;
    private String name;
    private String image;
    private int servings;
    private ArrayList<ingredient> ingredients;
    private ArrayList<step> steps;

    public Recipe(int rId, String name, String image, int servings,ArrayList<ingredient>ingredients, ArrayList<step>steps) {
        this.rId = rId;
        this.name = name;
        this.image = image;
        this.servings = servings;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    protected Recipe(Parcel in) {
        rId = in.readInt();
        name = in.readString();
        image = in.readString();
        servings = in.readInt();
        ingredients = in.createTypedArrayList(ingredient.CREATOR);
        steps = in.createTypedArrayList(step.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rId);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(servings);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getRId() {
        return rId;
    }

    public void setRId(int rId) {
        this.rId = rId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public ArrayList<ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<step> steps) {
        this.steps = steps;
    }
}
