package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {

    private String artistName;
    private String artistId;
    private String artistPicture;

    public Artist(Parcel in) {
        this.artistName = in.readString();
        this.artistId =  in.readString();
        this.artistPicture = in.readString();
    }


    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public String getArtistId() {
        return artistId;
    }


    public String getArtistName() {
        return artistName;
    }
    public String getArtistPicture() {
        return artistPicture;
    }

    public Artist(String artistName, String artistId, String picture_url) {
        this.artistName = artistName;
        this.artistId = artistId;
        this.artistPicture = picture_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return artistName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artistName);
        dest.writeString(this.artistId);
        dest.writeString(this.artistPicture);
    }
}
