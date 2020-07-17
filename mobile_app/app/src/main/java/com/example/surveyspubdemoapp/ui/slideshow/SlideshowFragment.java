package com.example.surveyspubdemoapp.ui.slideshow;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.surveyspubdemoapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        AdView mAdView = root.findViewById(R.id.banner01);
        mAdView.setAdListener(new AdListener() {
                                  @Override
                                  public void onAdFailedToLoad(int errorCode) {
                                      Log.d("onAdFailedToLoad", "This is why: " + errorCode);
                                  }
                              }
        );
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return root;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {
        Game game = new Game(getActivity(), view);
        game.run();
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}

