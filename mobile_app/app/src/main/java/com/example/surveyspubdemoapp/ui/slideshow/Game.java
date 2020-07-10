package com.example.surveyspubdemoapp.ui.slideshow;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.surveyspubdemoapp.R;
import com.example.surveyspubdemoapp.ui.Dialog;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;


public class Game {
    private Integer lives = 3;
    private ImageView turnedCard;
    private  Integer pairsLeft;
    private Context context;
    private Integer nRows = 3;
    private Integer nCols = 2;
    private View root;
    private HashMap<Integer, Integer> cardsImg;

    public Game(Context context, View root){
        this.context = context;
        this.root = root;
    }

//    Add listeners to cards and do initial shuffling
    public void run() {
        turnedCard = null;
        pairsLeft = nRows * nCols / 2;
        for (int row = 0; row < nRows; row += 1) {
            for (int col = 0; col < nCols; col += 1){
                int cardId = context.getResources().getIdentifier("row" + row + "col" + col, "id",
                        "com.example.surveyspubdemoapp.ui.slideshow");
                root.findViewById(cardId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        onImgClickHandler(v);
                    }
                });
            }
        }
        int restartId = this.context.getResources().getIdentifier("restartButton", "id",
                "com.example.surveyspubdemoapp.ui.slideshow");
        root.findViewById(restartId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
        shuffleCards();
        startLoop();
    }

    private void restartGame(){
        turnedCard = null;
        pairsLeft = nRows * nCols / 2;
        lives = 3;
        shuffleCards();
        startLoop();
    }

    private void startLoop(){
        while(lives > 0  && pairsLeft > 0){
        }
        if (lives == 0) {
            openDialog("No lives left!", "Click the restart button to play again");
        }
        if (pairsLeft == 0) {
            openDialog("Congrats!", "You've won!");
        }
    }


    private void onImgClickHandler(View v){
        ImageButton button = (ImageButton) v;
        button.setImageResource(cardsImg.get(button.getId()));
        int backCard = context.getResources().getIdentifier("article_image", "drawable",
                context.getPackageName());
        if (turnedCard == null) {
            turnedCard = button;
            return;
        }
        if (!cardsImg.get(turnedCard.getId()).equals(cardsImg.get(button.getId()))) {
            lives -= 1;
            turnedCard = null;
            button.setImageResource(backCard);
            turnedCard.setImageResource(backCard);
            return;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenedCardClickHandler(v);
            }
        });
        turnedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenedCardClickHandler(v);
            }
        });
        turnedCard = null;
        pairsLeft -= 1;
    }

    private void onOpenedCardClickHandler(View v){
        openDialog("Don't worry about this one", "You've already turned this card!");
    }

    private void shuffleCards() {
        int nRows = 3;
        int nCols = 2;
        LinkedList<String> pictures = (LinkedList <String>) (Arrays.asList("cat", "cat",
                "dog", "dog", "dolphin", "dolphin"));
        Collections.shuffle(pictures);
        int backCardImage = context.getResources().getIdentifier("article_image",
                "drawable", context.getPackageName());
        for (int row = 0; row < nRows; row += 1) {
            for (int col = 0; col < nCols; col += 1){
                int cardId = this.context.getResources().getIdentifier("row" + row + "col" + col, "id",
                        "com.example.surveyspubdemoapp.ui.slideshow");
                int imgId = context.getResources().getIdentifier(pictures.pop(), "drawable",
                        context.getPackageName());
                cardsImg.put(cardId, imgId);
                ImageButton button = root.findViewById(cardId);
                button.setImageResource(backCardImage);
            }
        }
    }

    private void openDialog(String title, String message){
        Dialog dialog = new Dialog(title, message);
        dialog.show(((FragmentActivity) this.context).getSupportFragmentManager(), "example dialog");
    }
}
