package com.example.beatrice.bakingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beatrice.bakingapp.adapters.RecipeCardAdapter;
import com.example.beatrice.bakingapp.data.RetrofitClient;
import com.example.beatrice.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeFragment extends Fragment {
    RecyclerView cardList;
    public RecipeCardAdapter mRecipeCardAdapter;
    public List<Recipe> mRecipes = new ArrayList<>();

    ProgressDialog pdLoading;

    RecyclerView recyclerView;
    View rootView;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        recyclerView = rootView.findViewById(R.id.recipe_recycleView);


        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }


        pdLoading = new ProgressDialog(getContext());

        //new AsyncCaller().execute();
        RetrofitClient.initialize().getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                mRecipes = response.body();
                response.body().toString();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList("all_recipes", (ArrayList<? extends Parcelable>) mRecipes);

                mRecipeCardAdapter = new RecipeCardAdapter(getContext(), mRecipes);
                recyclerView.setAdapter(mRecipeCardAdapter);

                //cardList.setAdapter(mRecipeCardAdapter);
                if (pdLoading.isShowing()) {
                    pdLoading.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });

        return rootView;
    }

    //TODO async caller
    private class AsyncCaller extends AsyncTask<Void, Void, List<Recipe>> {
        protected void onPreExecute() {
            pdLoading.setMessage("loading...");
            pdLoading.show();
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {

            RetrofitClient.initialize().getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    mRecipes = response.body();
                    ArrayList<Recipe> recipes = response.body();
                    response.body().toString();
                    Bundle recipesBundle = new Bundle();
                    recipesBundle.putParcelableArrayList("all_recipes", recipes);

                    mRecipeCardAdapter.setRecipeData(recipes, getContext());


                    //cardList.setAdapter(mRecipeCardAdapter);
                    if (pdLoading.isShowing()) {
                        pdLoading.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call, Throwable t) {

                }
            });
            return mRecipes;
        }

    }
}
