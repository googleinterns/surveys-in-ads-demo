package com.example.surveyspubdemoapp.ui.slideshow;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.Image;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import com.example.surveyspubdemoapp.R;
import com.example.surveyspubdemoapp.ui.Dialog;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Handler;


public class Game {
    private static final int LIVES = 3;
    private static final int ROWS = 3;
    private static final int COLS = 2;
    private static final long DELAY = 1000;
    private Integer mLives;
    private ImageView mTurnedCard;
    private Integer mPairsLeft;
    private Context mContext;
    private View mRoot;
    private HashMap<Integer, Integer> mCardsImg;

    public Game(Context context, View root){
        mContext = context;
        mRoot = root;
    }

//    Add listeners to cards and do initial shuffling
    public void run() {
        mCardsImg = new HashMap<>();
        mTurnedCard = null;
        mPairsLeft = ROWS * COLS / 2;
        mLives = LIVES;
        enableCards();
        setLives();
        int restartId = mContext.getResources().getIdentifier("restartButton", "id",
                mContext.getPackageName());
        mRoot.findViewById(restartId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
        int showCardsButtonId = mContext.getResources().getIdentifier("showButton", "id",
                mContext.getPackageName());
        mRoot.findViewById(showCardsButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCards();
            }
        });
        shuffleCards();
    }

    private void enableCards(){
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1){
                int cardId =mContext.getResources().getIdentifier("row" + row + "col" + col, "id",
                        mContext.getPackageName());
                mRoot.findViewById(cardId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        onImgClickHandler(v);
                    }
                });
            }
        }
    }

    private void restartGame(){
        mTurnedCard = null;
        mPairsLeft = ROWS * COLS / 2;
        mLives = 3;
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1){
                int cardId =mContext.getResources().getIdentifier("row" + row + "col" + col, "id",
                        mContext.getPackageName());
                mRoot.findViewById(cardId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        onImgClickHandler(v);
                    }
                });
            }
        }
        for (int i = 0; i < LIVES; i++){
            View heart = mRoot.findViewWithTag("heart" + i);
            heart.setVisibility(View.VISIBLE);
        }
        shuffleCards();
    }

    public void onImgClickHandler(View v){
//        ObjectAnimator animatorTurned = new ObjectAnimator.();
//        ObjectAnimator animatorNewCard = new ObjectAnimator()
        ImageView button = (ImageView) v;
        button.setImageResource(mCardsImg.get(button.getId()));
        int backCard = mContext.getResources().getIdentifier("ic_emoji", "drawable",
                mContext.getPackageName());
        if (mTurnedCard == null) {
            mTurnedCard = button;
            return;
        }
        if (mTurnedCard.getId() == button.getId()){
            openDialog("Oops!", "Don't tap this card again, try getting a matching card");
            return;
        }
        if (!mCardsImg.get(mTurnedCard.getId()).equals(mCardsImg.get(button.getId()))) {
//            killCards();
            button.postDelayed(new HideCard(button), DELAY);
            mTurnedCard.postDelayed(new HideCard(mTurnedCard), DELAY);
//            mRoot.postDelayed(new EnableCards(this), DELAY);
            mLives -= 1;
            dropLife();
            mTurnedCard = null;
            checkEndGame();
            return;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenedCardClickHandler(v);
            }
        });
        mTurnedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenedCardClickHandler(v);
            }
        });
        mTurnedCard = null;
        mPairsLeft -= 1;
        checkEndGame();
    }

    private void onOpenedCardClickHandler(View v){
        openDialog("Don't worry about this one", "You've already turned this card!");
    }

    private void shuffleCards() {
        LinkedList<String> pictures = new LinkedList<String>(Arrays.asList("ic_cat", "ic_cat",
                "ic_dog", "ic_dog", "ic_hamster", "ic_hamster"));
        Collections.shuffle(pictures);
        int backCardImage = mContext.getResources().getIdentifier("ic_emoji",
                "drawable", mContext.getPackageName());
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1){
                int cardId = mContext.getResources().getIdentifier("row" + row + "col" + col, "id",
                        mContext.getPackageName());
                int imgId = mContext.getResources().getIdentifier(pictures.pop(), "drawable",
                        mContext.getPackageName());
                mCardsImg.put(cardId, imgId);
                ImageView button = mRoot.findViewById(cardId);
                button.setImageResource(backCardImage);
//                button.setImageResource(imgId);

            }
        }
    }

    private void showCards() {
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1){
                int cardId = mContext.getResources().getIdentifier("row" + row + "col" + col, "id",
                        mContext.getPackageName());
                ImageView button = mRoot.findViewById(cardId);
                button.setImageResource(mCardsImg.get(cardId));
            }
        }
        killCards();
    }

    private void openDialog(String title, String message){
        Dialog dialog = new Dialog(title, message);
        dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "example dialog");
    }

    private void checkEndGame(){
        if (mPairsLeft == 0){
            openDialog("Congrats!",
                    "You've found all the matching cards, press restart to play again");
            killCards();
            return;
        }
        if (mLives == 0){
            openDialog("You've lost :(",
                    "Press restart to play again, good luck next time");
            killCards();
            return;
        }

    }

    private void killCards(){
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1){
                int cardId =mContext.getResources().getIdentifier("row" + row + "col" + col, "id",
                        mContext.getPackageName());
                mRoot.findViewById(cardId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        return;
                    }
                });
            }
        }
    }

    private void setLives(){
        int heartId = mContext.getResources().getIdentifier("ic_heart", "drawable",
                mContext.getPackageName());
        LinearLayout livesLayout = mRoot.findViewById(R.id.layout_lives_left);
        for (int i = 0; i < LIVES; i++){
            ImageView life = new ImageView(mContext);
            life.setImageResource(heartId);
            life.setTag("heart" + i);
            life.setPadding(20, 0, 0, 0);
            livesLayout.addView(life, new LinearLayout.LayoutParams(
                    100,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void dropLife(){
        View heart = mRoot.findViewWithTag("heart" + mLives);
        heart.setVisibility(View.INVISIBLE);
    }

    public long getRows(){
        return ROWS;
    }

    public long getCols(){
        return COLS;
    }

    public Context getContext(){
        return mContext;
    }

    public View getRoot(){
        return mRoot;
    }
}


