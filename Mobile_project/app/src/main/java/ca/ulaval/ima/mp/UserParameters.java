package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class UserParameters implements Parcelable {
    private String accessToken;
    private String refreshToken;
    private String expires_in;

    public UserParameters(Parcel in) {
            this.accessToken = in.readString();
            this.refreshToken = in.readString();
            this.expires_in =  in.readString();

    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public UserParameters(String accessToken, String refreshToken, String expires_in) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires_in = expires_in;
    }

    public static final Creator<UserParameters> CREATOR = new Creator<UserParameters>() {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.refreshToken);
        dest.writeString(this.expires_in);
    }
}
