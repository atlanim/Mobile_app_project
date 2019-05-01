package ca.ulaval.ima.mp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ulaval.ima.mp.dummy.DummyContent;
import ca.ulaval.ima.mp.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListPlayListFragmentInteractionListener}
 * interface.
 */
public class PlaylistFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PLAY_LISTS = "playlist-list";
    private static final String ARG_PLAY_LIST_NAME = "playlist-name";
    private ArrayList<PlayList> mPlayList;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListPlayListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaylistFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlaylistFragment newInstance(ArrayList<PlayList> playLists) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PLAY_LISTS, playLists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPlayList = new ArrayList<>();
            mPlayList = getArguments().getParcelableArrayList(ARG_PLAY_LISTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_list, container, false);
        View Recycler_view = view.findViewById(R.id.list);
        if (Recycler_view instanceof RecyclerView) {
            Context context = Recycler_view.getContext();
            RecyclerView recyclerView = (RecyclerView) Recycler_view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyPlaylistRecyclerViewAdapter(mPlayList, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListPlayListFragmentInteractionListener) {
            mListener = (OnListPlayListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentAlbumInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListPlayListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListPlaylistFragmentInteraction(PlayList playList);
    }
}
