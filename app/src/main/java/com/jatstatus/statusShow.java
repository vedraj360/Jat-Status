package com.jatstatus;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

public class statusShow extends AppCompatActivity {

    String Rajasthani_Key = "1lXjnOniDultCOtj3LMj4SPVo9Nv-OuQGlQTwvpoPFAI";
    String Hindi_Key = "1QtsycsDqW4myHokPh6qNeNHaGf0ZdvC-qNRcc72VfXg";
    String English_Key = "19wisab3PvJeBtUn4OEd7tl-BB4Am1VPmolsXt1vr35Q";
    String Punjabi_Key = "1t9KerdobJLgasN9Qm73YorR8hlPB7_E5vEt7-5gpUH8";
    String Haryanvi_Key = "1foD2nDoyTaJkMpWn-0Oq7jZ74Qv8D25IR1hiXQcEaps";
    String links[] = {"https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=" + Hindi_Key + "&sheet=Sheet1",
            "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=" + Rajasthani_Key + "&sheet=Sheet1",
            "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=" + Haryanvi_Key + "&sheet=Sheet1",
            "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=" + Punjabi_Key + "&sheet=Sheet1",
            "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=" + English_Key + "&sheet=Sheet1"};
    private String[] List = {"Jat", "Hindi", "Haryanvi", "Punjabi", "English",};
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private Boolean check = true;
    private RecyclerView recyclerView;
    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_show);
        recyclerView = findViewById(R.id.recycler);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stausBarColor();
        }
        rotateLoading = findViewById(R.id.rotateloading);
        rotateLoading.setLoadingColor(R.color.colorAccent);
        rotateLoading.start();
        Intent intent = getIntent();
        String status = intent.getStringExtra("model");
        Toolbar(status);
        HandlerRepeat();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (status.equals("Hindi")) {
            JsonFetch(links[0], status);
        } else if (status.equals("Rajasthani")) {
            JsonFetch(links[1], status);
        } else if (status.equals("Haryanvi")) {
            JsonFetch(links[2], status);
        } else if (status.equals("Punjabi")) {
            JsonFetch(links[3], status);
        } else {
            JsonFetch(links[4], status);
        }
        LoadBanner();
        ShowAds();
    }

    public void JsonFetch(String link, final String title) {
        StringRequest stringRequest = new StringRequest(link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rotateLoading.stop();
                rotateLoading.setVisibility(View.GONE);
                //GsonBuilder gsonBuilder =  new GsonBuilder();
                recyclerView.setVisibility(View.VISIBLE);


                final Gson gson = new Gson();
                try {

                    ModelStatus modelStatus = gson.fromJson(response, ModelStatus.class);

                    List<Sheet1> sheet1List = modelStatus.getSheet1();
                    if (sheet1List != null) {
                        final statusShowAdapter statusShowAdapter = new statusShowAdapter(sheet1List, title, statusShow.this);
                        recyclerView.setAdapter(statusShowAdapter);
                        statusShowAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    rotateLoading.stop();
                    rotateLoading.setVisibility(View.GONE);
                    Log.d("Error", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 2 * 60 * 1000; // in 2 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new String(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stausBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void Toolbar(String title) {
        Toolbar toolbar = findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title + " Status");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        mInterstitialAd.setAdUnitId("ca-app-pub-9435995636387073/8858077159");

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