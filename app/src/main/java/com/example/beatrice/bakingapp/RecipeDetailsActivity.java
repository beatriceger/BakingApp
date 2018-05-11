package com.example.beatrice.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.beatrice.bakingapp.adapters.RecipeDetailAdapter;
import com.example.beatrice.bakingapp.models.Recipe;
import com.example.beatrice.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener, RecipeStepDetailFragment.ListItemClickListener {

    private Recipe recipe;
    String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();

            recipe = bundle.getParcelable("currentRecipe");
            recipeName = recipe.getName();

            final RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack("recipe_Detail")
                    .commit();

            if (findViewById(R.id.recipe_layout).getTag() != null && findViewById(R.id.recipe_layout).getTag().equals("tablet-land")) {

                final RecipeStepDetailFragment fragment2 = new RecipeStepDetailFragment();
                fragment2.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment2).addToBackStack("step_detail")
                        .commit();

            }


        } else {
            recipeName = savedInstanceState.getString("Title");
        }


        //Toolbar myToolbar = findViewById(R.id.my_toolbar);
        // setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);


//        myToolbar.setNavigationOnClickListener(v -> {
//            FragmentManager fm = getSupportFragmentManager();
//            if (findViewById(R.id.fragment_container2) == null) {
//                if (fm.getBackStackEntryCount() > 1) {
//
//                    fm.popBackStack("recipe_detail", 0);
//                } else if (fm.getBackStackEntryCount() > 0) {
//
//                    finish();
//
//                }
//
//
//            } else {
//
//                finish();
//
//            }
//
//        });

    }
//TODO open/close activity depending on the fragment you are on
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", recipeName);
    }

    @Override
    public void onListItemClick(List<Step> stepsOut, int selectedItemIndex, String recipeName) {


        final RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);
//TODO change to current step
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList("selected_steps", (ArrayList<Step>) stepsOut);
        stepBundle.putInt("index", selectedItemIndex);
        stepBundle.putString("Title", recipeName);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_layout).getTag() != null && findViewById(R.id.recipe_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment).addToBackStack("step_detail")
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack("step_detail")
                    .commit();
        }

    }

}
