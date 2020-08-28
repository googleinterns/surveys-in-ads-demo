package com.example.surveyspubdemoapp.ui.AdMob;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.surveyspubdemoapp.R;
import com.example.surveyspubdemoapp.ui.AdActivity;
import com.example.surveyspubdemoapp.ui.Dialog;
import com.example.surveyspubdemoapp.ui.Tuple;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

// This class handles the memory game. It uses the WrongCardDelay class and the HideCard classes
// for UI delays in some cases.
public class Game {
  private static final int EASY_LIVES = 3;
  private static final int MEDIUM_LIVES = 5;
  private static final int HARD_LIVES = 7;
  private int mMaxLives;
  private RewardedAd mRewardedAd;
  private InterstitialAd mInterstitialAd;
  private boolean mShowBanner = true;
  private boolean mShowInterstitial = true;
  private boolean mShowRewarded = true;
  private int mRows = 3;
  private int mCols = 2;
  private static final long DELAY = 1000;
  private static final Tuple EASY = new Tuple(3, 2);
  private static final Tuple MEDIUM = new Tuple(4, 3);
  private static final Tuple HARD = new Tuple(4, 4);
  private HashMap<Integer, Boolean> mMatchedCards = new HashMap<Integer, Boolean>();
  private Integer mLives;
  public ImageView mTurnedCard;
  private Integer mPairsLeft;
  private Context mContext;
  private View mRoot;
  private HashMap<Integer, Integer> mCardsImg;
  private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
  private RewardedAdLoadCallback mAdLoadCallback;
  private RewardedAdCallback mAdCallback;

  public Game(Context context, View root) {
    mContext = context;
    mRoot = root;
  }

  //    Initializes the game (all variables, creates cards, ads, shuffles cards, sets up UI and
  // action
  //    buttons)
  public void run() {
    mCardsImg = new HashMap<>();
    mTurnedCard = null;
    mPairsLeft = mRows * mCols / 2;
    mMaxLives = EASY_LIVES;
    mLives = mMaxLives;
    mRewardedAd = ((AdActivity) mContext).mRewardedAds[0];
    mInterstitialAd = ((AdActivity) mContext).mInterstitialAds[0];
    enableDifficultyButtons();
    createCards();
    enableCards();
    enableAdsSwitch();
    setLives();
    enableRestartButton();
    enableShowCardsButton();
    enableLoseButton();
    shuffleCards();
    setUpInterstitialAd();
    setUpRewardedAd();
  }
  //    Enable button  to restart game from scratch
  private void enableRestartButton() {
    int restartId =
        mContext.getResources().getIdentifier("restartButton", "id", mContext.getPackageName());
    mRoot
        .findViewById(restartId)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                restartGame();
              }
            });
  }
  //  Enable button to show all the cards in the game and end the current round
  private void enableShowCardsButton() {
    int showCardsButtonId =
        mContext.getResources().getIdentifier("showButton", "id", mContext.getPackageName());
    mRoot
        .findViewById(showCardsButtonId)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                showCards();
              }
            });
  }

  private void enableLoseButton() {
    int loseButtonId =
        mContext.getResources().getIdentifier("loseButton", "id", mContext.getPackageName());
    mRoot
        .findViewById(loseButtonId)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                while (mLives > 0) {
                  mLives--;
                  dropLife();
                }
                checkEndGame();
              }
            });
  }
  //  Setup the rewarded ad, creates the Ad Request and loads the ad. Does not show the ad
  private void setUpRewardedAd() {
    mAdLoadCallback =
        new RewardedAdLoadCallback() {
          @Override
          public void onRewardedAdLoaded() {
            Log.d("onRewardLoaded", "Add Successfully loaded ");
          }

          @Override
          public void onRewardedAdFailedToLoad(LoadAdError errorCode) {
            Log.d("onRewardedAdFailed", "Error code: " + errorCode);
            mRewardedAd =
                    new RewardedAd(mContext, mContext.getResources().getString(R.string.rewarded));
 mRewardedAd.loadAd(new AdRequest.Builder().build(), mAdLoadCallback);
          }
        };
    mAdCallback =
        new RewardedAdCallback() {
          @Override
          public void onRewardedAdOpened() {}

          @Override
          public void onRewardedAdClosed() {
            mRewardedAd =
                new RewardedAd(mContext, mContext.getResources().getString(R.string.rewarded));
            mRewardedAd.loadAd(new AdRequest.Builder().build(), mAdLoadCallback);
          }

          @Override
          public void onUserEarnedReward(@NonNull RewardItem reward) {
            enableCards();
            mLives = mMaxLives;
            setLives();
          }

          @Override
          public void onRewardedAdFailedToShow(AdError errorCode) {
            Log.d("RewardedFailedShow", "Error code: " + errorCode);
            mRewardedAd = new RewardedAd(mContext, mContext.getResources().getString(R.string.rewarded));
            mRewardedAd.loadAd(new AdRequest.Builder().build(), mAdLoadCallback);
          }
        };
    mRewardedAd.loadAd(new AdRequest.Builder().build(), mAdLoadCallback);
  }
  //  Sets up the interstitial ad, creates the ad requests and the functions onAdClosed and
  // onAdFailedToLoad
  private void setUpInterstitialAd() {
    mInterstitialAd.setAdListener(
        new AdListener() {
          @Override
          public void onAdClosed() {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
          }

          @Override
          public void onAdFailedToLoad(LoadAdError errorCode) {
            Log.d("onAdFailedToLoad", "Interstitial ad failed to load, error code " + errorCode);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
          }

          @Override
          public void onAdLoaded() {
            Log.d("onAdLoaded", "Interstitial ad loaded");
          }
        });
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
  }

  //    Restarts the game, set back lives, turns all the cards face-down and sets all the
  //    parameter to their initial values. Assigns all cards their OnClick functions.
  //    Shuffles the cards and creates a new round.
  private void restartGame() {
    if (mShowInterstitial && mInterstitialAd.isLoaded()) {
      mInterstitialAd.show();
    }
    mMatchedCards = new HashMap<Integer, Boolean>();
    mTurnedCard = null;
    mPairsLeft = mRows * mCols / 2;
    mLives = mMaxLives;
    createCards();
    for (int row = 0; row < mRows; row += 1) {
      for (int col = 0; col < mCols; col += 1) {
        mRoot
            .findViewWithTag("row" + row + "col" + col)
            .setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    onImgClickHandler(v);
                  }
                });
      }
    }
    setLives();
    shuffleCards();
  }

  //    Function for OnClick listener for cards that have already been matched
  private void onOpenedCardClickHandler(View v) {}

  //    Shuffles all the cards. Assign different pictures to every card
  private void shuffleCards() {
    String[] picturesArray = {
      "ic_cat",
      "ic_cat",
      "ic_dog",
      "ic_dog",
      "ic_hamster",
      "ic_hamster",
      "ic_cow",
      "ic_cow",
      "ic_deer",
      "ic_deer",
      "ic_fishes",
      "ic_fishes",
      "ic_rat",
      "ic_rat",
      "ic_wildlife",
      "ic_wildlife"
    };
    LinkedList<String> pictures =
        new LinkedList<String>(Arrays.asList(Arrays.copyOfRange(picturesArray, 0, mRows * mCols)));

    Collections.shuffle(pictures);
    int backCardImage =
        mContext.getResources().getIdentifier("ic_emoji", "drawable", mContext.getPackageName());
    for (int row = 0; row < mRows; row += 1) {
      for (int col = 0; col < mCols; col += 1) {
        ImageView button = mRoot.findViewWithTag("row" + row + "col" + col);
        int imgId =
            mContext
                .getResources()
                .getIdentifier(pictures.pop(), "drawable", mContext.getPackageName());
        mCardsImg.put(button.getId(), imgId);
        button.setImageResource(backCardImage);
      }
    }
  }
  //    Turns all the cards face-up and finished the round
  private void showCards() {
    for (int row = 0; row < mRows; row += 1) {
      for (int col = 0; col < mCols; col += 1) {
        ImageView button = mRoot.findViewWithTag("row" + row + "col" + col);
        button.setImageResource(mCardsImg.get(button.getId()));
      }
    }
    killCards();
  }
  // Opens a dialog box with the given title and message
  private void openDialog(String title, String message) {
    Dialog dialog = new Dialog(title, message);
    dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "example dialog");
  }

  //    Disables all cards, they are assigned no action for OnClick
  private void killCards() {
    for (int row = 0; row < mRows; row += 1) {
      for (int col = 0; col < mCols; col += 1) {
        mRoot
            .findViewWithTag("row" + row + "col" + col)
            .setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    return;
                  }
                });
      }
    }
  }

  //    Disables the difficulty buttons.
  private void disableDifficultyButtons() {
    int radioGroupId =
        mContext
            .getResources()
            .getIdentifier("difficulty_selection", "id", mContext.getPackageName());
    RadioGroup radioGroup = mRoot.findViewById(radioGroupId);
    for (int index = 0; index < radioGroup.getChildCount(); index++) {
      RadioButton nextChild = (RadioButton) radioGroup.getChildAt(index);
      nextChild.setEnabled(false);
    }
  }

  //    Sets all the lives back to the original number of lives.
  private void setLives() {
    int heartId =
        mContext.getResources().getIdentifier("ic_heart", "drawable", mContext.getPackageName());
    LinearLayout livesLayout = mRoot.findViewById(R.id.layout_lives_left);
    livesLayout.removeAllViews();
    for (int i = 0; i < mMaxLives; i++) {
      ImageView life = new ImageView(mContext);
      life.setImageResource(heartId);
      life.setTag("heart" + i);
      life.setPadding(20, 0, 0, 0);
      livesLayout.addView(
          life, new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT));
    }
  }

  //    Removes one live when the player makes a mistake. The life is also removes from the UI
  private void dropLife() {
    View heart = mRoot.findViewWithTag("heart" + mLives);
    heart.setVisibility(View.INVISIBLE);
  }

  //    Creates all the ImageViews that represent the cards, it creates mRows * mCols number of
  // cards
  private void createCards() {
    LinearLayout cardsSpace = mRoot.findViewWithTag("cards_space");
    cardsSpace.removeAllViews();
    LinearLayout.LayoutParams cardParams =
        new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
    LinearLayout.LayoutParams rowParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
    for (int row = 0; row < mRows; row++) {
      LinearLayout rowLayout = new LinearLayout(mContext);
      rowLayout.setId(generateViewId());
      rowLayout.setTag("row" + row);
      rowLayout.setLayoutParams(rowParams);
      cardsSpace.addView(rowLayout);
      for (int col = 0; col < mCols; col++) {
        ImageView card = new ImageView(mContext);
        card.setId(generateViewId());
        card.setTag("row" + row + "col" + col);
        card.setPadding(0, 0, 0, 15);
        rowLayout.addView(card, cardParams);
      }
    }
  }

  //    Enables the switch to turn on/off every each type of ad
  private void enableAdsSwitch() {

    ((Switch)
            mRoot.findViewById(
                mContext
                    .getResources()
                    .getIdentifier("switch_banner", "id", mContext.getPackageName())))
        .setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mShowBanner = !mShowBanner;
                int visibility;
                if (mShowBanner) {
                  visibility = View.VISIBLE;
                } else {
                  visibility = View.INVISIBLE;
                }
                AdView banner =
                    mRoot.findViewById(
                        mContext
                            .getResources()
                            .getIdentifier("banner01", "id", mContext.getPackageName()));
                banner.setVisibility(visibility);
              }
            });
    ((Switch)
            mRoot.findViewById(
                mContext
                    .getResources()
                    .getIdentifier("switch_interstitial", "id", mContext.getPackageName())))
        .setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mShowInterstitial = !mShowInterstitial;
              }
            });
    ((Switch)
            mRoot.findViewById(
                mContext
                    .getResources()
                    .getIdentifier("switch_rewarded", "id", mContext.getPackageName())))
        .setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mShowRewarded = !mShowRewarded;
              }
            });
  }

  //    Turns card face-down
  public void hideCard(ImageView card) {
    card.setImageResource(R.drawable.ic_emoji);
  }

  //  Assigns each cards its corresponding OnClick listener, if the cards has not been turned then
  // the
  //  card must flip if touched, if the cards has already been turned it should stay like that until
  // the
  //  end of the round.
  public void enableCards() {
    for (int row = 0; row < mRows; row += 1) {
      for (int col = 0; col < mCols; col += 1) {
        ImageView card = mRoot.findViewWithTag("row" + row + "col" + col);
        if (mMatchedCards.containsKey(card.getId())) {
          card.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  onOpenedCardClickHandler(v);
                }
              });
        } else {
          card.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  onImgClickHandler(v);
                }
              });
        }
      }
    }
  }

  //    Checks if the game should end due to no lives left or if all cards have been matched
  public void checkEndGame() {
    if (mPairsLeft == 0) {
      openDialog("Congrats!", "You've found all the matching cards, press restart to play again");
      killCards();
      return;
    }
    if (mLives == 0) {
      killCards();
      if (mShowRewarded && mRewardedAd.isLoaded()) {
        mRewardedAd.show((Activity) mContext, mAdCallback);
        return;
      }
      openDialog("You've lost", "Press restart to play again");
      return;
    }
  }

  //    Sets up all the difficulty buttons to change levels
  public void enableDifficultyButtons() {
    int radioGroupId =
        mContext
            .getResources()
            .getIdentifier("difficulty_selection", "id", mContext.getPackageName());
    RadioGroup radioGroup = mRoot.findViewById(radioGroupId);
    int maxLives;
    for (int index = 0; index < radioGroup.getChildCount(); index++) {
      RadioButton nextChild = (RadioButton) radioGroup.getChildAt(index);
      final Tuple difficulty;
      if (index == 0) {
        difficulty = EASY;
      } else if (index == 1) {
        difficulty = MEDIUM;
      } else {
        difficulty = HARD;
      }
      nextChild.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              RadioButton button = (RadioButton) view;
              if (difficulty == EASY) {
                mMaxLives = EASY_LIVES;
              } else if (difficulty == MEDIUM) {
                mMaxLives = MEDIUM_LIVES;
              } else {
                mMaxLives = HARD_LIVES;
              }
              if (button.isChecked()) {
                mRows = (int) difficulty.getFirst();
                mCols = (int) difficulty.getSecond();
                restartGame();
              }
            }
          });
      nextChild.setEnabled(true);
    }
  }

  //    Handles the logic for when a face_down card is touched. Checks if the cards is the first
  // one
  //    to be turned and if not it check that it matches the previosuly turned cards. Takes lives
  // away
  //    cards don't match or keeps the cards face-up if they do match
  public void onImgClickHandler(View v) {
    ImageView button = (ImageView) v;
    button.setImageResource(mCardsImg.get(button.getId()));
    if (mTurnedCard == null) {
      mTurnedCard = button;
      return;
    }
    if (mTurnedCard.getId() == button.getId()) {
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
    mMatchedCards.put(button.getId(), true);
    mMatchedCards.put(mTurnedCard.getId(), true);
    button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onOpenedCardClickHandler(v);
          }
        });
    mTurnedCard.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onOpenedCardClickHandler(v);
          }
        });
    mTurnedCard = null;
    mPairsLeft -= 1;
    checkEndGame();
  }

  //    Getters to be used by the HideCard and WrongCardDelay classes
  public long getRows() {
    return mRows;
  }

  public long getCols() {
    return mCols;
  }

  public Context getContext() {
    return mContext;
  }

  public View getRoot() {
    return mRoot;
  }

  //    Helper function to create random id to use in API <= 16
  public static int generateViewId() {
    for (; ; ) {
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
