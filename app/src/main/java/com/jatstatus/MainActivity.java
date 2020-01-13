package com.jatstatus;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import osmandroid.project_basics.Task;

public class MainActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    private String[] List = {"Hindi", "Rajasthani", "Haryanvi", "Punjabi", "English",};
    private List<ModelList> modelLists;
    DataModel dataModel;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private Boolean check = true;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton whatsbtn, sharebtn, ratebtn, abtus_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stausBarColor();
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);
        whatsbtn = findViewById(R.id.whats_btn);
        sharebtn = findViewById(R.id.share_btn);
        abtus_btn = findViewById(R.id.abt_us);
        ratebtn = findViewById(R.id.rate_bt);
        if (!IsConnected()) {
            MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(this)
                    .setTitle("Oops! No Network")
                    .setDescription("Please Check your network and try again.")
                    .setIcon(R.drawable.noic1)
                    .withDialogAnimation(true)
                    .withIconAnimation(true)
                    .setPositiveText("Ok")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .build();
            dialog.show();
        } else {
            mDrawerLayout.setVisibility(View.VISIBLE);
            modelLists = new ArrayList<>();
            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            dataModel = new DataModel(modelLists, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return dataModel.getItemViewType(position) == 2 ? 2 : 1;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(dataModel);
            list();
            ToolBarSetup();
            NavigationSetup();
            LoadBanner();
            setRatebtn();
            setSharebtn();
            setWhatsbtn();
            setAbtus_btn();

        }


    }

    public void setAbtus_btn() {
        abtus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AboutUs.class));

            }
        });
    }

    private void setWhatsbtn() {
        whatsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whats_share();
            }
        });

    }

    private void setSharebtn() {
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();

            }
        });
    }

    private void setRatebtn() {
        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rate();
            }
        });
    }

    private void ToolBarSetup() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void NavigationSetup() {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_Home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.rate:
                        Rate();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.share:
                        share();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.about_us:
                        Intent intent = new Intent(getApplicationContext(), AboutUs.class);
                        startActivity(intent);

                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stausBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void list() {
        for (String aList : List) {
            ModelList mList = new ModelList(aList);
            modelLists.add(mList);
        }


    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
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

    void Handler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowAds();
            }
        }, 5000);
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

    private boolean IsConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void whats_share() {
        try {
            final ComponentName name = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
            Intent oShareIntent = new Intent();
            oShareIntent.setComponent(name);
            oShareIntent.setType("text/plain");
            oShareIntent.putExtra(Intent.EXTRA_TEXT, "Find the latest and best Jat Status for Whats App,Facebook and Instagram in 5 Languages:" + "https://play.google.com/store/apps/details?id=com.jatstatus");
            startActivity(oShareIntent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Whats App Not Found!", Toast.LENGTH_SHORT).show();
            Log.d("Error", String.valueOf(e));
        }

    }

    private void share() {
        Task.ShareApp(MainActivity.this, "com.jatstatus", "Share", "Find the latest and best Jat Status for Whats App,Facebook and Instagram in 5 Languages");
    }

    private void Rate() {
        Task.RateApp(MainActivity.this, "com.jatstatus");
    }

    private void fbShare() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "This is the text that will be shared.");

        boolean installed = checkAppInstall("com.facebook.katana");
        if (installed) {
            intent.setPackage("com.facebook.katana");
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Installed application first", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}
