package com.example.surveyspubdemoapp.ui.AdMob;

import android.widget.ImageView;
// This class is used to create a delay between when a player selects a wrong card and when the rest
// of the game continues (hiding that card, subtracting one live ...)
public class WrongCardDelay implements Runnable {
  private Game mGame;
  private ImageView mWrongCard;

  public WrongCardDelay(Game game, ImageView wrongCard) {
    mGame = game;
    mWrongCard = wrongCard;
  }

  @Override
  public void run() {
    mGame.hideCard(mWrongCard);
    mGame.hideCard(mGame.mTurnedCard);
    mGame.enableCards();
    mGame.enableDifficultyButtons();
    mGame.mTurnedCard = null;
    mGame.checkEndGame();
  }
}
