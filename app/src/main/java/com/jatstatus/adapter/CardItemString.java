package com.jatstatus.adapter;


public class CardItemString {

    private String mTextResource;
    private String mTitleResource;

    public CardItemString(String title, String text) {
        mTitleResource = title;
        mTextResource = text;
    }



    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }

}
