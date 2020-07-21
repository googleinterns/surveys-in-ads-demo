package com.example.surveyspubdemoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.surveyspubdemoapp.ui.AdActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;

import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdActivity {

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_game)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        String interstitialTest = getResources().getString(R.string.interstitial_test);
        String interstitial = getResources().getString(R.string.interstitial);
        String rewardedTest = getResources().getString(R.string.rewarded_test);
        String rewarded = getResources().getString(R.string.rewarded);
        String banner = getResources().getString(R.string.banner);
        String bannerTest = getResources().getString(R.string.banner_test);
        String deviceId = getResources().getString(R.string.device_id);
        RequestConfiguration adsConfiguration = new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList(deviceId)).build();
        MobileAds.setRequestConfiguration(adsConfiguration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAds[0] = new InterstitialAd(this);
        mInterstitialAds[0].setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAds[0].loadAd(new AdRequest.Builder().build());
            }
        });
        mInterstitialAds[0].setAdUnitId(interstitialTest);
        mRewardedAds[0] = new RewardedAd(this,
                rewardedTest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}