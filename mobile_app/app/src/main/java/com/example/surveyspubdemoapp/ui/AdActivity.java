package com.example.surveyspubdemoapp.ui;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

public interface AdActivity {
    InterstitialAd[] mInterstitialAds = new InterstitialAd[]{null};
    RewardedAd[] mRewardedAds = new RewardedAd[]{null};
}
