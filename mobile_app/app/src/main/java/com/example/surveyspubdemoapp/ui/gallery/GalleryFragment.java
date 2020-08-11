package com.example.surveyspubdemoapp.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.surveyspubdemoapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class GalleryFragment extends Fragment {
    private PublisherAdView mPublisherAdView;
    private PublisherInterstitialAd mPublisherInterstitialAd;
    private RewardedAd mRewardedAd;
    private RewardedAdLoadCallback mAdLoadCallback;
    private RewardedAdCallback mAdCallback;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        enableGPTBannerAd(root);
        enableGPTInterstitialAd(root);
        enableInterstitialButton(root);
        enableGPTRewardedAd(root);
        enableRewardedButton(root);
        return root;
    }


    public void enableGPTBannerAd(View root){
        LinearLayout container = root.findViewById(R.id.banner_container);
//        mPublisherAdView.setAdSizes(new AdSize(168, 28));
        String ad_168x28 = getResources().getString(R.string.gpt_168x28);
        String gpt_banner_test = getResources().getString(R.string.gpt_banner_test);
        String smart_banner = getResources().getString(R.string.gpt_smart_banner_01);
        mPublisherAdView  = new PublisherAdView(getActivity());
//        mPublisherAdView.setAdSizes(new AdSize(168, 28));
        mPublisherAdView.setAdSizes(AdSize.SMART_BANNER);
        mPublisherAdView.setAdUnitId(smart_banner);
        mPublisherAdView.loadAd(new PublisherAdRequest.Builder().build());
        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                mPublisherAdView.loadAd(new PublisherAdRequest.Builder().build());

            }
        });
        container.addView(mPublisherAdView);
    }

    public void enableGPTInterstitialAd(View root){
        String interstitial = getResources().getString(R.string.gpt_interstitial);
        String interstitial_test = getResources().getString(R.string.gpt_interstitial_test);
        mPublisherInterstitialAd = new PublisherInterstitialAd(getActivity());
        mPublisherInterstitialAd.setAdUnitId(interstitial);
        mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
            }
            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
            }
        });
    }

    public void enableGPTRewardedAd(View root){
        mRewardedAd = new RewardedAd(getActivity(),
                "/6499/example/rewarded-video");
        mAdLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int adError) {
                // Ad failed to load.
                mRewardedAd = new RewardedAd(getActivity(),
                        "/6499/example/rewarded-video");
                mRewardedAd.loadAd(new PublisherAdRequest.Builder().build(), mAdLoadCallback);
            }
        };
        mAdCallback = new RewardedAdCallback() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem reward) {
                // User earned reward.
            }
            @Override
            public void onRewardedAdClosed() {
                // Ad closed.

                mRewardedAd = new RewardedAd(getActivity(),
                        "/6499/example/rewarded-video");
                mRewardedAd.loadAd(new PublisherAdRequest.Builder().build(), mAdLoadCallback);
            }
        };
        mRewardedAd.loadAd(new PublisherAdRequest.Builder().build(), mAdLoadCallback);
    }

    public void enableInterstitialButton(View root){
        Button b = root.findViewById(R.id.interstitial_button);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPublisherInterstitialAd.isLoaded()) {
                            mPublisherInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }
                });
    }

    public void enableRewardedButton(View root){
        Button b = root.findViewById(R.id.rewarded_button);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mRewardedAd.isLoaded()) {
                            mRewardedAd.show(getActivity(), mAdCallback);
                        } else {
                            Log.d("TAG", "The rewarded wasn't loaded yet.");
                        }
                    }
                });
    }
}