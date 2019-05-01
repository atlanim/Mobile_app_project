package ca.ulaval.ima.mp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import ca.ulaval.ima.mp.TrackFragment.OnListTrackFragmentInteractionListener;
import ca.ulaval.ima.mp.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListTrackFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTrackRecyclerViewAdapter extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder> {

    private final List<Track> mValues;
    private final OnListTrackFragmentInteractionListener mListener;
    private final Context context;
    private String myprefs = "MyPrefs";


    public MyTrackRecyclerViewAdapter(List<Track> tracks, OnListTrackFragmentInteractionListener listener, Context context) {
        mValues = tracks;
        mListener = listener;
        this.context = context;
        System.out.println("here 8");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final SharedPreferences prefs = context.getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        String check_pic = prefs.getString("playlist_cover", null);
        if (check_pic == null || check_pic.equals(""))
        {
            Picasso.get().load(mValues.get(position).getTrackPicture()).into(holder.mPicture);
            holder.mDelete.setVisibility(View.GONE);
            holder.mAdd.setVisibility(View.GONE);
        }
        else
        {
            holder.mPicture.setVisibility(View.GONE);
        }
        holder.mContentView.setText(mValues.get(position).getTrackName());
        holder.mArtistName.setText("Artiste:   "   + mValues.get(position).getArtistName());

        holder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.playAudio(holder.mItem.getTrackId());
                }
            }
        });
        holder.mPlay_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.playVideo(holder.mItem.getTrackId());
                }
            }
        });
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.addTrackInPlaylist(holder.mItem.getTrackId());
                }
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.removeTrackInPlaylist(holder.mItem.getTrackId(), holder.mItem.getPlayListId());
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListAlbumTrackFragmentInteraction(holder.mItem.getTrackId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mArtistName;
        public final ImageView mPlay;
        public final ImageView mPlay_video;
        public final ImageView mAdd;
        public final ImageView mDelete;
        public final ImageView mPicture_all;
        public final ImageView mPicture;
        public Track mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mArtistName = (TextView) view.findViewById(R.id.track_artist_name);
            mPlay = (ImageView) view.findViewById(R.id.play);
            mPlay_video = (ImageView) view.findViewById(R.id.play_video);
            mAdd = (ImageView) view.findViewById(R.id.add);
            mDelete = (ImageView) view.findViewById(R.id.delete);
            mPicture_all =(ImageView) view.findViewById(R.id.picture_all);
            mPicture =(ImageView) view.findViewById(R.id.picture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
