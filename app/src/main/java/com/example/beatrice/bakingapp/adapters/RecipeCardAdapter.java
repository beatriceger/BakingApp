package com.example.beatrice.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beatrice.bakingapp.MainActivity;
import com.example.beatrice.bakingapp.R;
import com.example.beatrice.bakingapp.RecipeDetailsActivity;
import com.example.beatrice.bakingapp.models.Recipe;
import com.example.beatrice.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private Context mContext;

    public RecipeCardAdapter(Context context, List<Recipe> objects) {

        this.recipes = objects;
        this.mContext = context;

    }

    public void setRecipeData(List<Recipe> recipesIn, Context context) {
        recipes = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_receipes, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(recipes.get(position).getName());

        List<Step> steps = recipes.get(position).getSteps();
        String url = steps.get(steps.size() - 1).getVideoURL();

        try {
            holder.card.setBackground(new BitmapDrawable(mContext.getResources(), getVideoFrame(url)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public static Bitmap getVideoFrame(String path) throws Throwable {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(path, new HashMap<>());
            bitmap = mediaMetadataRetriever.getFrameAtTime(3, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception: " + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        final CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_recipe);
            card = itemView.findViewById(R.id.recipe_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
            intent.putExtra("currentRecipe", recipes.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}