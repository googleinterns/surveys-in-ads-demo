package com.example.surveyspubdemoapp.ui.slideshow;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.nfc.Tag;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.surveyspubdemoapp.R;
import com.example.surveyspubdemoapp.ui.AdActivity;
import com.example.surveyspubdemoapp.ui.Dialog;
import com.example.surveyspubdemoapp.ui.Tuple;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import androidx.appcompat.app.AppCompatActivity;



public class Game {
    private static final int LIVES = 3;
    private int mRows = 3;
    private int mCols = 2;
    private static final long DELAY = 1000;
    private static final Tuple EASY = new Tuple(3,2);
    private static final Tuple MEDIUM = new Tuple(4,3);
    private static final Tuple HARD = new Tuple(4,4);
    private Integer mLives;
    public ImageView mTurnedCard;
    private Integer mPairsLeft;
    private Context mContext;
    private View mRoot;
    private HashMap<Integer, Integer> mCardsImg;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private RewardedAdLoadCallback mAdLoadCallback = new RewardedAdLoadCallback() {
        @Override
        public void onRewardedAdLoaded() {
            // Ad successfully loaded.
        }

        @Override
        public void onRewardedAdFailedToLoad(int errorCode) {
            // Ad failed to load.
        }
    };


    public Game(Context context, View root){
        mContext = context;
        mRoot = root;
    }

//    Add listeners to cards and do initial shuffling
    public void run() {
        mCardsImg = new HashMap<>();
        mTurnedCard = null;
        mPairsLeft = mRows * mCols / 2;
        mLives = LIVES;
        enableDifficultyButtons();
        createCards();
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

    public void enableCards(){
        for (int row = 0; row < mRows; row += 1) {
            for (int col = 0; col < mCols; col += 1){
                mRoot.findViewWithTag("row" + row + "col" + col).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        onImgClickHandler(v);
                    }
                });
            }
        }
    }

    private void restartGame(){
        if (((AdActivity) mContext).mInterstitialAds[0].isLoaded()) {
            ((AdActivity) mContext).mInterstitialAds[0].show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        mTurnedCard = null;
        mPairsLeft = mRows * mCols / 2;
        mLives = 3;
        createCards();
        for (int row = 0; row < mRows; row += 1) {
            for (int col = 0; col < mCols; col += 1){
                mRoot.findViewWithTag("row" + row + "col" + col).setOnClickListener(new View.OnClickListener() {
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
        if (mTurnedCard == null) {
            mTurnedCard = button;
            return;
        }
        if (mTurnedCard.getId() == button.getId()){
            openDialog("Oops!", "Don't tap this card again, try getting a matching card");
            return;
        }
        if (!mCardsImg.get(mTurnedCard.getId()).equals(mCardsImg.get(button.getId()))) {
            killCards();
            disableDifficultyButtons();
            mLives -= 1;
            dropLife();
            mRoot.postDelayed(new WrongCardDelay(this, button), DELAY);
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

    public void hideCard(ImageView card){
        card.setImageResource(R.drawable.ic_emoji);
    }

    private void shuffleCards() {
//        LinkedList<String> pictures = new LinkedList<String>(Arrays.asList("ic_cat", "ic_cat",
//                "ic_dog", "ic_dog", "ic_hamster", "ic_hamster", "ic_cow", "ic_cow", "ic_deer", "ic_deer",
//                "ic_fishes", "ic_fishes", "ic_rat", "ic_rat", "ic_wildlife", "ic_wildlife"));
        String[] picturesArray = {"ic_cat", "ic_cat",
                "ic_dog", "ic_dog", "ic_hamster", "ic_hamster", "ic_cow", "ic_cow", "ic_deer", "ic_deer",
                "ic_fishes", "ic_fishes", "ic_rat", "ic_rat", "ic_wildlife", "ic_wildlife"};
        LinkedList<String> pictures = new LinkedList<String>(Arrays.asList(
                Arrays.copyOfRange(picturesArray, 0, mRows * mCols)));

        Collections.shuffle(pictures);
        int backCardImage = mContext.getResources().getIdentifier("ic_emoji",
                "drawable", mContext.getPackageName());
        for (int row = 0; row < mRows; row += 1) {
            for (int col = 0; col < mCols; col += 1){
                ImageView button = mRoot.findViewWithTag("row" + row + "col" + col);
                int imgId = mContext.getResources().getIdentifier(pictures.pop(), "drawable",
                        mContext.getPackageName());
                mCardsImg.put(button.getId(), imgId);
                button.setImageResource(backCardImage);
//                button.setImageResource(imgId);

            }
        }
    }

    private void showCards() {
        for (int row = 0; row < mRows; row += 1) {
            for (int col = 0; col < mCols; col += 1){
                ImageView button = mRoot.findViewWithTag("row" + row + "col" + col);
                button.setImageResource(mCardsImg.get(button.getId()));
            }
        }
        killCards();
    }

    private void openDialog(String title, String message){
        Dialog dialog = new Dialog(title, message);
        dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "example dialog");
    }

    public void checkEndGame(){
        if (mPairsLeft == 0){
            openDialog("Congrats!",
                    "You've found all the matching cards, press restart to play again");
            killCards();
            return;
        }
        if (mLives == 0){
            openDialog("You've lost :(",
                    "Press restart to play again, good luck next time");
            ((AdActivity) mContext).mRewardedAds[0].loadAd(new AdRequest.Builder().build(), mAdLoadCallback);
            return;
        }

    }

    private void killCards(){
        for (int row = 0; row < mRows; row += 1) {
            for (int col = 0; col < mCols; col += 1){
                mRoot.findViewWithTag("row" + row + "col" + col).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        return;
                    }
                });
            }
        }
    }

    private void disableDifficultyButtons(){
        int radioGroupId = mContext.getResources().getIdentifier("difficulty_selection", "id",
                mContext.getPackageName());
        RadioGroup radioGroup = mRoot.findViewById(radioGroupId);
        for(int index = 0; index < radioGroup.getChildCount(); index++) {
            RadioButton nextChild = (RadioButton) radioGroup.getChildAt(index);
            nextChild.setEnabled(false);
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

    public void enableDifficultyButtons(){
        int radioGroupId = mContext.getResources().getIdentifier("difficulty_selection", "id",
                mContext.getPackageName());
        RadioGroup radioGroup = mRoot.findViewById(radioGroupId);
        for(int index = 0; index < radioGroup.getChildCount(); index++) {
            RadioButton nextChild = (RadioButton) radioGroup.getChildAt(index);
            final Tuple difficulty;
            if (index == 0){
                difficulty = EASY;
            } else if(index == 1){
                difficulty = MEDIUM;
            } else {
                difficulty =HARD;
            }
            nextChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton button = (RadioButton) view;
                    if( button.isChecked()) {
                        mRows = (int) difficulty.getFirst();
                        mCols = (int) difficulty.getSecond();
                        restartGame();
                    }
                }
            });
            nextChild.setEnabled(true);
        }
    }

    private void createCards(){
        LinearLayout cardsSpace = mRoot.findViewWithTag("cards_space");
        cardsSpace.removeAllViews();
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f);
        for (int row = 0; row < mRows; row++){
            LinearLayout rowLayout = new LinearLayout(mContext);
            rowLayout.setId(generateViewId());
            rowLayout.setTag("row"+row);
            rowLayout.setLayoutParams(rowParams);
            cardsSpace.addView(rowLayout);
            for (int col = 0; col < mCols; col++){
                ImageView card = new ImageView(mContext);
                card.setId(generateViewId());
                card.setTag("row"+row+"col"+col);
                rowLayout.addView(card, cardParams);
            }
        }
    }

    public long getRows(){
        return mRows;
    }

    public long getCols(){
        return mCols;
    }

    public Context getContext(){
        return mContext;
    }

    public View getRoot(){
        return mRoot;
    }

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

}


