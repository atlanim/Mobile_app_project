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

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentAlbumInteractionListener}
 * interface.
 */
public class AlbumFragment extends Fragment {

    private static final String ARG_ARTIST_NAME = "artist-name";
    private static final String ARG_ALBUMS_LIST = "album-list";
    private OnListFragmentAlbumInteractionListener mListener;
    private ArrayList<Album> mAlbums;


    public AlbumFragment() {
    }

    public static AlbumFragment newInstance(String artistName, ArrayList<Album> albums) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ALBUMS_LIST, albums);
        args.putString(ARG_ARTIST_NAME, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAlbums = new ArrayList<>();
            mAlbums = getArguments().getParcelableArrayList(ARG_ALBUMS_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyAlbumRecyclerViewAdapter(mAlbums, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentAlbumInteractionListener) {
            mListener = (OnListFragmentAlbumInteractionListener) context;
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

    public interface OnListFragmentAlbumInteractionListener {
        void onListFragmentAlbumInteraction(Album item);
    }
}
