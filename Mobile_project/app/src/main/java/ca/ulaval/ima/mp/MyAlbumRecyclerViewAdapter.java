package ca.ulaval.ima.mp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ca.ulaval.ima.mp.AlbumFragment.OnListFragmentAlbumInteractionListener;
import ca.ulaval.ima.mp.dummy.DummyContent.DummyItem;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentAlbumInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAlbumRecyclerViewAdapter extends RecyclerView.Adapter<MyAlbumRecyclerViewAdapter.ViewHolder> {

    private final List<Album> mValues;
    private final OnListFragmentAlbumInteractionListener mListener;
    private String myprefs = "MyPrefs";
    private Context context;

    public MyAlbumRecyclerViewAdapter(List<Album> albums, AlbumFragment.OnListFragmentAlbumInteractionListener listener, Context context) {
        mValues = albums;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getAlbumName());
        Picasso.get().load(mValues.get(position).getAlbumPicture()).into(holder.mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    final SharedPreferences prefs = context.getSharedPreferences(myprefs, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("album_cover", mValues.get(position).getAlbumPicture());
                    edit.apply();
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentAlbumInteraction(holder.mItem);
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
        public final ImageView mImageView;
        public Album mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.picture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
