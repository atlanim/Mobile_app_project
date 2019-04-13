package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    private String trackName;
    private String trackId;

    public Track(Parcel in) {
        this.trackName = in.readString();
        this.trackId =  in.readString();

    }


    public String getTrackId() {
        return trackId;
    }


    public String getTrackName() {
        return trackName;
    }

    public Track(String trackName, String trackId) {
        this.trackName = trackName;
        this.trackId = trackId;
    }

    public static final Parcelable.Creator<UserParameters> CREATOR = new Parcelable.Creator<UserParameters>() {
        @Override
        public UserParameters createFromParcel(Parcel in) {
            return new UserParameters(in);
        }

        @Override
        public UserParameters[] newArray(int size) {
            return new UserParameters[size];
        }
    };

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
    }


}
