package com.example.surveyspubdemoapp.ui.slideshow;

import android.content.Context;
import android.view.View;

public class EnableCards implements Runnable {
  private Game mGame;

  public EnableCards(Game game) {
    mGame = game;
  }

  public void run() {
    for (int row = 0; row < mGame.getRows(); row += 1) {
      for (int col = 0; col < mGame.getCols(); col += 1) {
        int cardId =
            mGame
                .getContext()
                .getResources()
                .getIdentifier(
                    "row" + row + "col" + col, "id", mGame.getContext().getPackageName());
        mGame
            .getRoot()
            .findViewById(cardId)
            .setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    mGame.onImgClickHandler(v);
                  }
                });
      }
    }
  }
}
