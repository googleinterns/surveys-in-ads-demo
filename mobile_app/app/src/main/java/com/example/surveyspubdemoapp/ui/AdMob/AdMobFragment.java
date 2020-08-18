package com.example.surveyspubdemoapp.ui.AdMob;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.surveyspubdemoapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdMobFragment extends Fragment {

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_admob, container, false);
    AdView mAdView = root.findViewById(R.id.banner01);
    mAdView.setAdListener(
        new AdListener() {
          @Override
          public void onAdFailedToLoad(int errorCode) {
            Log.d("onAdFailedToLoad", "This is why: " + errorCode);
          }
        });
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);
    return root;
  }

  public void onViewCreated(View view, Bundle savedInstanceState) {
    Game game = new Game(getActivity(), view);
    game.run();
  }
}
