package com.shahnil.post;


import android.os.Parcelable;

import java.io.Serializable;

public class userinformation implements Serializable {


    public String mText1;
    public String mName;
    public String mDate;
    public String imageUrl;

    public userinformation(){
        //needed for firebase

    }

    public userinformation( String Name, String text1, String Date, String ImageUrl) {



        this.mName=Name;
        this.mText1=text1;
        this.mDate = Date;
        this.imageUrl = ImageUrl;
    }


    public String getmText1() {
        return mText1;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
