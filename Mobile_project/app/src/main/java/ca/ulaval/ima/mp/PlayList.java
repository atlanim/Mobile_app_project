package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayList implements Parcelable {
    private String playListName;
    private String playListId;

    public PlayList(Parcel in) {
        this.playListName = in.readString();
        this.playListId =  in.readString();

    }


    public static final Creator<PlayList> CREATOR = new Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel in) {
            return new PlayList(in);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[0];
        }

    };

    public String getPlayListId() {
        return playListId;
    }


    public String getPlayListName() {
        return playListName;
    }

    public PlayList(String playListName, String playListId) {
        this.playListName = playListName;
        this.playListId = playListId;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return playListName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playListName);
        dest.writeString(this.playListId);
    }
}
