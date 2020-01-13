package com.jatstatus.adapter;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jatstatus.R;

import java.util.ArrayList;
import java.util.List;

import osmandroid.project_basics.Task;

// public class CardPagerAdapterS {


public class CardPagerAdapterS extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItemString> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapterS(Context context) {
        this.context = context;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(CardItemString item) {
        mViews.add(null);
        mData.add(item);
    }


    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItemString item, View view) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        final TextView contentTextView = view.findViewById(R.id.contentTextView);
        final FloatingActionButton share_btn = view.findViewById(R.id.share_btn);
        final FloatingActionButton whats_btn = view.findViewById(R.id.whats_btn);
        FloatingActionButton clipboard_btn = view.findViewById(R.id.clipboard_btn);
        String s;
        s = item.getTitle();
        titleTextView.setText(s);
        contentTextView.setText(item.getText());
        final String text = item.getText();
        clipboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipBoard(text);
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(text);
            }
        });
        whats_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whats_share(text);

            }
        });

    }


    private void share(String shareText) {
        Task.ShareApp(context, "com.jatstatus", "Share", shareText + "\n" + "For more Jat Status");
    }

    private void whats_share(String shareText) {
        try {
            final ComponentName name = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
            Intent oShareIntent = new Intent();
            oShareIntent.setComponent(name);
            oShareIntent.setType("text/plain");
            oShareIntent.putExtra(Intent.EXTRA_TEXT, shareText + "\n" + "For more Jat Status:" + "https://play.google.com/store/apps/details?id=com.jatstatus");
            context.startActivity(oShareIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Whats App Not Found!", Toast.LENGTH_SHORT).show();
            Log.d("Error", String.valueOf(e));
        }

    }

    private void copyToClipBoard(String selectedText) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", selectedText);
        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }
}

