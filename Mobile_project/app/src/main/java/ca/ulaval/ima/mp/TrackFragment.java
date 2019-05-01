package ca.ulaval.ima.mp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private static final String ARG_IS_MY__PLAYLIST_TRACK ="isMyPlaylistTrack" ;
    private  boolean isMyPlaylistTrack = false;
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
    public static TrackFragment newInstance(String playListName, ArrayList<Track> tracks, boolean isMyPlaylistTrack) {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_NAME, playListName);
        args.putParcelableArrayList(ARG_TRACK_LIST, tracks);
        args.putBoolean(ARG_IS_MY__PLAYLIST_TRACK, isMyPlaylistTrack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTracks = new ArrayList<>();
            mTracks = getArguments().getParcelableArrayList(ARG_TRACK_LIST);
            isMyPlaylistTrack= getArguments().getBoolean(ARG_IS_MY__PLAYLIST_TRACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new MyTrackRecyclerViewAdapter(mTracks, mListener, isMyPlaylistTrack));
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
