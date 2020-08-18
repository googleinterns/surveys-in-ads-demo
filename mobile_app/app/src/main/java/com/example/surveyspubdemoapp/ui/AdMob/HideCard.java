package com.example.surveyspubdemoapp.ui.AdMob;

import android.widget.ImageView;
import com.example.surveyspubdemoapp.R;

public class HideCard implements Runnable {

  private ImageView mCard;

  public HideCard(ImageView v) {
    mCard = v;
  }

  public void run() {
    mCard.setImageResource(R.drawable.ic_emoji);
  }
}
