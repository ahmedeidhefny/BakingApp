package com.udacity.ahmed_eid.bakingapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;
import com.udacity.ahmed_eid.bakingapp.Model.*;
import com.udacity.ahmed_eid.bakingapp.R;
import com.udacity.ahmed_eid.bakingapp.Adapters.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes;
    private RecyclerView recyclerRecipe;
    private TextView noInternetText;
    private ContentLoadingProgressBar loadingProgressBar;
    private RelativeLayout progressLayout;
    private int spanCount;

    private GridLayoutManager layoutManager;
    private RecipeAdapter adapter;

    private RequestQueue requestQueue;
    private ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        //Determine if the device is a smartphone or tablet?
        boolean isTabletVar = isTablet(this);
        if (isTabletVar == true) {
            spanCount = 2;
        } else {
            spanCount = 1;
        }
        if (conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isAvailable()
                || !conMgr.getActiveNetworkInfo().isConnected()) {
            toggleError();
            noInternetText.setText(R.string.error_massage_no_internet);
        } else {
            fetchRecipes();
        }

    }

    private void initializeUI() {
        recyclerRecipe = findViewById(R.id.recipe_recycler);
        loadingProgressBar = findViewById(R.id.progress_bar);
        progressLayout = findViewById(R.id.progress_layout);
        noInternetText = findViewById(R.id.error_massage_display);
        recipes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        progressLayout.setVisibility(View.VISIBLE);
        loadingProgressBar.show();
    }

    public void fetchRecipes() {
        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, AppConstants.JSONURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<ArrayList<ingredient>> ingredients = new ArrayList<>();
                ArrayList<ArrayList<step>> steps = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject recipe = response.getJSONObject(i);
                        int id = recipe.getInt("id");
                        String name = recipe.getString("name");
                        String image = recipe.getString("image");
                        int servings = recipe.getInt("servings");
                        //------------------------------------------------------------------------------
                        JSONArray ingredientsArray = recipe.getJSONArray("ingredients");
                        ArrayList<ingredient> igList = new ArrayList<>();
                        for (int x = 0; x < ingredientsArray.length(); x++) {
                            JSONObject ingredientObject = ingredientsArray.getJSONObject(x);
                            float quantity = Float.parseFloat(ingredientObject.getString("quantity"));
                            String measure = ingredientObject.getString("measure");
                            String ingredient = ingredientObject.getString("ingredient");
                            igList.add(x, new ingredient(quantity, measure, ingredient));
                        }
                        ingredients.add(i, igList);
                        //------------------------------------------------------------------------------
                        JSONArray stepsArray = recipe.getJSONArray("steps");
                        ArrayList<step> stList = new ArrayList<>();
                        for (int y = 0; y < stepsArray.length(); y++) {
                            JSONObject stepObject = stepsArray.getJSONObject(y);
                            int sId = stepObject.getInt("id");
                            String shortDescription = stepObject.getString("shortDescription");
                            String description = stepObject.getString("description");
                            String videoURL = stepObject.getString("videoURL");
                            String thumbnailURL = stepObject.getString("thumbnailURL");
                            stList.add(y, new step(sId, shortDescription, description, videoURL, thumbnailURL));
                        }
                        steps.add(i, stList);
                        recipes.add(i, new Recipe(id, name, image, servings, ingredients.get(i), steps.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setAdapterToRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.GONE);
                error.printStackTrace();
            }
        });

        requestQueue.add(request1);
    }

    public void setAdapterToRecyclerView() {
        if (recipes != null) {
            toggleShowData();
            layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
            adapter = new RecipeAdapter(getApplicationContext(), recipes);
            recyclerRecipe.setLayoutManager(layoutManager);
            recyclerRecipe.setAdapter(adapter);
        } else {
            toggleError();
            noInternetText.setText(R.string.error_massage_list_recipes);
        }
    }

    public Boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void toggleError() {
        progressLayout.setVisibility(View.GONE);
        loadingProgressBar.hide();
        recyclerRecipe.setVisibility(View.GONE);
        noInternetText.setVisibility(View.VISIBLE);
    }

    public void toggleShowData() {
        progressLayout.setVisibility(View.GONE);
        loadingProgressBar.hide();
        noInternetText.setVisibility(View.GONE);
        recyclerRecipe.setVisibility(View.VISIBLE);
    }

}
