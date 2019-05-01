package ca.ulaval.ima.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class Me implements Parcelable {
    public String getMeName() {
        return meName;
    }

    public String getMeBirthday() {
        return meBirthday;
    }

    public String getMeCountry() {
        return meCountry;
    }

    public String getMeEmail() {
        return meEmail;
    }

    public Integer getMeFollowers() {
        return meFollowers;
    }

    public String getMePicture() {
        return mePicture;
    }

    public String getMeProduct() {
        return meProduct;
    }

    private String meName;
    private String meBirthday;
    private String meCountry;
    private String meEmail;
    private Integer meFollowers;
    private String mePicture;
    private String meProduct;

    public Me(Parcel in) {
        this.meName = in.readString();
        this.meBirthday = in.readString();
        this.meCountry = in.readString();
        this.meEmail = in.readString();
        this.meFollowers = in.readInt();
        this.mePicture = in.readString();
        this.meProduct = in.readString();
    }


    public static final Creator<Me> CREATOR = new Creator<Me>() {
        @Override
        public Me createFromParcel(Parcel in) {
            return new Me(in);
        }

        @Override
        public Me[] newArray(int size) {
            return new Me[0];
        }

    };


    public Me(String meName, String meBirthday, String meCountry, String meEmail, Integer meFollowers, String mePicture,String meProduct) {
        this.meName = meName;
        this.meBirthday = meBirthday;
        this.meCountry = meCountry;
        this.meEmail =  meEmail;
        this.meFollowers = meFollowers;
        this.mePicture = mePicture;
        this.meProduct = meProduct;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return meName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.meName);
        dest.writeString(this.meBirthday);
        dest.writeString(this.meCountry);
        dest.writeString(this.meEmail);
        dest.writeInt(this.meFollowers);
        dest.writeString(this.mePicture);
        dest.writeString(this.meProduct);
    }
}
