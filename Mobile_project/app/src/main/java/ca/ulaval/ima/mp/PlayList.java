package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayList implements Parcelable {
    private String playListName;
    private String playListId;
    private String playListPicture;

    public PlayList(Parcel in) {
        this.playListName = in.readString();
        this.playListId =  in.readString();
        this.playListPicture = in.readString();

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

    public String getPlayListPicture() {
        return playListPicture;
    }

    public PlayList(String playListName, String playListId, String picture_url) {
        this.playListName = playListName;
        this.playListId = playListId;
        this.playListPicture = picture_url;
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
        dest.writeString(this.playListPicture);
    }
}
