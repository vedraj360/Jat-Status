package com.jatstatus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.jatstatus.adapter.CardItemString;
import com.jatstatus.adapter.CardPagerAdapterS;
import com.jatstatus.utils.ShadowTransformer;

public class SwipeData extends AppCompatActivity {


    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private Boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_data);
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("DATA");
        int position = intent.getIntExtra("Position", 0);
        String title = intent.getStringExtra("Title");


        ViewPager mViewPager = findViewById(R.id.viewPager);
        CardPagerAdapterS mCardAdapter = new CardPagerAdapterS(SwipeData.this);


        for (String aData : data) {

            mCardAdapter.addCardItemS(new CardItemString("", aData));
        }

        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        LoadBanner();
        ShowAds();
        HandlerRepeat();

    }

    void LoadBanner() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        mAdView.loadAd(adRequest);


    }

    private void ShowAds() {


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9435995636387073/1554302204");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (check)
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {


            }

            @Override
            public void onAdClicked() {

            }
        });

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        check = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        check = true;
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    void Handler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowAds();
            }
        }, 5000);
    }

    void HandlerRepeat() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowAds();
                handler.postDelayed(this, 30000);
            }
        }, 30000);
    }

}
