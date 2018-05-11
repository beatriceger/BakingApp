package com.example.beatrice.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Objects;

public class UpdateBakingService extends IntentService {

    public static String INGREDIENTS_LIST = "INGREDIENTS_LIST";

    public UpdateBakingService() {
        super("UpdateBakingService");
    }

    public static void startService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, UpdateBakingService.class);
        intent.putExtra(INGREDIENTS_LIST, ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> fromActivityIngredientsList = intent.getExtras().getStringArrayList(INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(fromActivityIngredientsList);

        }
    }


    private void handleActionUpdateBakingWidgets(ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(INGREDIENTS_LIST, fromActivityIngredientsList);
        sendBroadcast(intent);
    }

}
