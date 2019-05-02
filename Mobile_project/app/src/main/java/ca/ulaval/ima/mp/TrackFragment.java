package ca.ulaval.ima.mp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListTrackFragmentInteractionListener}
 * interface.
 */
public class TrackFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PLAYLIST_NAME = "playListName";
    private static final String ARG_TRACK_LIST ="track-list" ;
    private String myprefs = "MyPrefs";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListTrackFragmentInteractionListener mListener;
    private ArrayList<Track> mTracks;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrackFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TrackFragment newInstance(String playListName, ArrayList<Track> tracks) {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_NAME, playListName);
        args.putParcelableArrayList(ARG_TRACK_LIST, tracks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTracks = new ArrayList<>();
            mTracks = getArguments().getParcelableArrayList(ARG_TRACK_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_list, container, false);
        View Recycler_view = view.findViewById(R.id.list);
        ImageView picture_all = view.findViewById(R.id.picture_all);
        RelativeLayout picture_all_container = view.findViewById(R.id.picture_all_container);
        final SharedPreferences prefs = getContext().getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        String pics_check = "";
        pics_check = prefs.getString("playlist_cover", null);
        System.out.println(pics_check);
        String pics_check2 = "";
        pics_check2 = prefs.getString("album_cover", null);
        System.out.println(pics_check2);
        if (pics_check == null || pics_check.equals("") && pics_check2 == null || pics_check2.equals(""))
        {
            System.out.println("here 3");
            picture_all_container.setVisibility(View.GONE);
        }
        else if (!pics_check2.equals("") || pics_check2 != null && pics_check == "" || pics_check == null)
        {
            System.out.println("here 6");
            picture_all.setVisibility(View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= 20) {
                Glide.with(getContext())
                        .load(pics_check2)
                        .into(picture_all);
            }
        }
        else if (pics_check2.equals("") || pics_check2 == null && pics_check != "" || pics_check != null){
            System.out.println("here 7");
            picture_all.setVisibility(View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= 20) {
                Glide.with(getContext())
                        .load(pics_check)
                        .into(picture_all);
            }
        }

        if (Recycler_view instanceof RecyclerView) {
            Context context = Recycler_view.getContext();
            RecyclerView recyclerView = (RecyclerView) Recycler_view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyTrackRecyclerViewAdapter(mTracks, mListener, getContext()));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListTrackFragmentInteractionListener) {
            mListener = (OnListTrackFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentAlbumTrackInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListTrackFragmentInteractionListener {

        void onListAlbumTrackFragmentInteraction(String trackId);

        void playAudio(String trackId);

        void removeTrackInPlaylist(String trackId, String playlistId);

        void addTrackInPlaylist(String trackId);

        void playVideo(String trackId);
    }
}
