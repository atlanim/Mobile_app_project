package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    private String albumName;
    private String albumId;

    public Album(Parcel in) {
        this.albumName = in.readString();
        this.albumId =  in.readString();

    }


    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getAlbumId() {
        return albumId;
    }


    public String getAlbumName() {
        return albumName;
    }

    public Album(String albumName, String albumId) {
        this.albumName = albumName;
        this.albumId = albumId;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return albumName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumName);
        dest.writeString(this.albumId);
    }
}