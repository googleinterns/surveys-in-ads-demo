package com.example.surveyspubdemoapp.ui;

import android.animation.ObjectAnimator;

public class Tuple {
    private Object mFirst;
    private Object mSecond;
    public Tuple(Object first, Object second) {
        mFirst = first;
        mSecond = second;
    }
    public Object getFirst(){
        return mFirst;
    }
    public Object getSecond(){
        return mSecond;
    }
    public void setFirst(Object o){
        mFirst = o;
    }
    public void setSecond(Object o){
        mSecond = 0;
    }
}
