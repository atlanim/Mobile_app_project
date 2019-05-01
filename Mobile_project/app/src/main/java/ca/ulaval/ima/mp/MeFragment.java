package ca.ulaval.ima.mp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class MeFragment extends Fragment {

   private Me me = null;
   private static final String ARG_ME_INFO = "me-info";

    public MeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MeFragment newInstance(Me me) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ME_INFO, me);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            me = getArguments().getParcelable(ARG_ME_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView picture = view.findViewById(R.id.picture);
        if (!me.getMePicture().equals("")) {
            System.out.println(me.getMePicture());
            if (android.os.Build.VERSION.SDK_INT >= 20) {
                Glide.with(getContext())
                        .load(me.getMePicture())
                        .into(picture);
            }
            else {
                Glide.with(getContext())
                        .load(R.drawable.ic_avatar)
                        .into(picture);
            }
        }
        TextView name = view.findViewById(R.id.name);
        TextView product = view.findViewById(R.id.product);
        TextView email = view.findViewById(R.id.email);
        TextView country = view.findViewById(R.id.country);
        TextView followers = view.findViewById(R.id.followers);
        TextView birthdate = view.findViewById(R.id.birthdate);

        name.setText(me.getMeName());
        product.setText(me.getMeProduct());
        email.setText(me.getMeEmail());
        country.setText(me.getMeCountry());
        followers.setText(me.getMeFollowers().toString());
        birthdate.setText(me.getMeBirthday());

    }
}