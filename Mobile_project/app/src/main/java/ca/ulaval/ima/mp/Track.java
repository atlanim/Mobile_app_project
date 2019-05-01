package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    private String trackName;
    private String trackId;
    private String artistName;
    private String trackPicture;

    public Track(Parcel in) {
        this.trackName = in.readString();
        this.trackId =  in.readString();
        this.artistName = in.readString();
        this.trackPicture = in.readString();
    }


    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getTrackId() {
        return trackId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public Track(String trackName, String trackId, String artistName, String trackPicture) {
        this.trackName = trackName;
        this.trackId = trackId;
        this.artistName = artistName;
        this.trackPicture = trackPicture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return trackName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trackName);
        dest.writeString(this.trackId);
        dest.writeString(this.artistName);
        dest.writeString(this.trackPicture);
    }

    public String getTrackPicture() {
        return trackPicture;
    }
}
