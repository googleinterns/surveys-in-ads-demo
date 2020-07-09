package com.example.surveyspubdemoapp.ui.slideshow;

import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.surveyspubdemoapp.R;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static androidx.core.graphics.drawable.IconCompat.getResources;

public class Game extends Thread {
    public static int lives;
    public static ImageView turnedCard;
    public static int pairsLeft;

    public void run(View root) {
        Game.lives = 3;
        Game.turnedCard = null;
        int nRows = 3;
        int nCols = 2;
        Game.pairsLeft = nRows * nCols / 2;


        for (int row = 0; row < nRows; row += 1) {
            for (int col = 0; col < nCols; col += 1){
                int cardId = contgetResources().getIdentifier("row" + row + "col" + col, "id",
                        "com.example.surveyspubdemoapp.ui.slideshow");
                root.findViewById(cardId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        onImgClickHandler(v);
                    }
                });
            }
        }


    }

    private void onImgClickHandler(View button){
        if (Game.turnedCard == null) {
            Game.turnedCard = button;
            return;
        }
        if (Game.turnedCard.toString() != button.toString()) {
            Game.lives -= 1;
            Game.turnedCard = null;
            return;
        }
        Field imgField = R.drawable.class.getDeclaredField("article_image");
        int imgId = imgField.getInt(null);
        button.setImageResource(imgId);
        Game.turnedCard.setImageResource(imgId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenedCardClickHandler(v);
            }
        });

        Game.turnedCard = null;
        Game.pairsLeft -= 1;
    }

    private void onOpenedCardClickHandler(View button){
        return;
    }

    private void shuffleCards(View root) {
        int nRows = 3;
        int nCols = 2;
        int nPictures = nRows * nCols;
        LinkedList<String> pictures = new LinkedList(Arrays.asList("cat", "cat",
                "dog", "dog", "dolphin", "dolphin"));
        Collections.shuffle(pictures);
        for (int row = 0; row < nRows; row += 1) {
            for (int col = 0; col < nCols; col += 1){
                int cardId = getResources().getIdentifier("row" + row + "col" + col, "id",
                        "com.example.surveyspubdemoapp.ui.slideshow");
                int imgId;
                try {
                    Field imgField = R.drawable.class.getDeclaredField(pictures.pop());
                    imgId = imgField.getInt(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    imgId = 0;
                }
                ImageButton button = root.findViewById(cardId);
                button.setImageResource(imgId);
            }
        }
    }
}
