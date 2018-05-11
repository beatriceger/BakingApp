package com.example.beatrice.bakingapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beatrice.bakingapp.adapters.RecipeDetailAdapter;
import com.example.beatrice.bakingapp.models.Ingredient;
import com.example.beatrice.bakingapp.models.Recipe;
import com.example.beatrice.bakingapp.widget.UpdateBakingService;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragment extends Fragment {

    String recipeName;
    Recipe recipe;
    ArrayList<String> recipeIngredientsForWidgets;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView recyclerView;
        TextView textView;


        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable("selected_recipes");

        } else {
            recipe = getArguments().getParcelable("currentRecipe");
        }

        List<Ingredient> ingredients = recipe.getIngredients();
        recipeName = recipe.getName();

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        textView = rootView.findViewById(R.id.recipe_detail_text);

        //TODO
        recipeIngredientsForWidgets = new ArrayList<>();


        ingredients.forEach((a) ->
        {
            textView.append("\u2022 " + a.getIngredient() + "\n");
            textView.append("\t\t\t Quantity: " + a.getQuantity().toString() + "\n");
            textView.append("\t\t\t Measure: " + a.getMeasure() + "\n\n");
            //TODO

            recipeIngredientsForWidgets.add(a.getIngredient() + "\n" +
                    "Quantity: " + a.getQuantity().toString() + "\n" +
                    "Measure: " + a.getMeasure() + "\n");
        });

        recyclerView = rootView.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        RecipeDetailAdapter mRecipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailsActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailAdapter);
        mRecipeDetailAdapter.setData(recipe, getContext());
//TODO
        //updating widget
        UpdateBakingService.startService(getContext(), recipeIngredientsForWidgets);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelable("selected_recipes", recipe);
        currentState.putString("Title", recipeName);
    }

}
