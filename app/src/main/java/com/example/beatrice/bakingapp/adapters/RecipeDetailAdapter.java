package com.example.beatrice.bakingapp.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beatrice.bakingapp.R;
import com.example.beatrice.bakingapp.RecipeStepDetailFragment;
import com.example.beatrice.bakingapp.models.Ingredient;
import com.example.beatrice.bakingapp.models.Recipe;
import com.example.beatrice.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecyclerViewHolder> {
    List<Step> lSteps;
    private String recipeName;


    final private ListItemClickListener lOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName);
    }

    public RecipeDetailAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }

    public void setData(Recipe recipesIn, Context context) {
        //lSteps = recipesIn;
        lSteps = recipesIn.getSteps();
        recipeName = recipesIn.getName();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.recipe_detail_cardview_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(lSteps.get(position).getId() + ". " + lSteps.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {

        return lSteps != null ? lSteps.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = itemView.findViewById(R.id.name_recipe);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            lOnClickListener.onListItemClick(lSteps, getAdapterPosition(), recipeName);
        }

    }
}
