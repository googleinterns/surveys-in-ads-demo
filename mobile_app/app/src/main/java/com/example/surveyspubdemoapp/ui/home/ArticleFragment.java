package com.example.surveyspubdemoapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.surveyspubdemoapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ArticleFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        AdView banner_01 = root.findViewById(R.id.banner01);
        banner_01.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(int errorCode) {
                                        Log.d("onAdFailedToLoad", "This is why: " + errorCode);
                                    }
                                }
        );
        banner_01.loadAd(new AdRequest.Builder().build());
        AdView banner_02 = root.findViewById(R.id.banner02);
        banner_02.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(int errorCode) {
                                        Log.d("onAdFailedToLoad", "This is why: " + errorCode);
                                    }
                                }
        );
        banner_02.loadAd(new AdRequest.Builder().build());
        return root;
    }
}