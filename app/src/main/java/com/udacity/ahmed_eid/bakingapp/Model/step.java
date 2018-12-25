package com.udacity.ahmed_eid.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class step implements Parcelable {
    private int sId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    protected step(Parcel in) {
        sId = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public step(int sId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.sId = sId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<step> CREATOR = new Creator<step>() {
        @Override
        public step createFromParcel(Parcel in) {
            return new step(in);
        }

        @Override
        public step[] newArray(int size) {
            return new step[size];
        }
    };
}
