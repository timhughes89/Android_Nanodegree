package com.timsimonhughes.a04_bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {
    @SerializedName("id")
    private int mId;
    @SerializedName("shortDescription")
    private String mShortDescription;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("videoURL")
    private String mVideoUrl;
    @SerializedName("thumbnailURL")
    private String mThumbnailUrl;

    public Step(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }

    protected Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}



