package com.example.surveyspubdemoapp.ui.slideshow;

import android.widget.ImageView;

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
