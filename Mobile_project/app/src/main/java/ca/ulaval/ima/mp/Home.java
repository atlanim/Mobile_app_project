package ca.ulaval.ima.mp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity implements AlbumFragment.OnListFragmentAlbumInteractionListener,
        TrackFragment.OnListTrackFragmentInteractionListener,
        ArtistFragment.OnListFragmentArtistInteractionListener,
        PlaylistFragment.OnListPlayListFragmentInteractionListener {

    private EditText editTextSearch;
    private RadioGroup searchType;
    private Button myPlayListButton;
    private Button profilButton;
    private UserParameters currentUserParameters;
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Track> tracks = new ArrayList<>();
    private Me me = null;
    private ArrayList<Artist> artists = new ArrayList<>();
    private ArrayList<PlayList> playLists;
    private String myprefs = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        editTextSearch = findViewById(R.id.editTextSearch);
        searchType = findViewById(R.id.radioGroupSearchType);
        myPlayListButton = findViewById(R.id.buttonPlaylist);
        profilButton = findViewById(R.id.profil);
        currentUserParameters = getIntent().getExtras().getParcelable("currentUserParameters");
        editTextSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    search();
                    return true;
                }
                return false;
            }
        });
        myPlayListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMyPlayList();
            }
        });
        profilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                me();
            }
        });
        setSupportActionBar(toolbar);
    }

    private void findMyPlayList() {
    }

    private void search() {
        String searchUrl = "https://api.spotify.com/v1/search";
        String type = getType();
        String seachWord = this.editTextSearch.getText().toString();
        String token = currentUserParameters.getAccessToken();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.parse(searchUrl).newBuilder()
                .addQueryParameter("q", seachWord)
                .addQueryParameter("type", type)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String ResponseString = response.body().string();
                    openSearchResultFragment(ResponseString);
                }
            }

            ;
        });

    }

    private void me() {
        String searchUrl = "https://api.spotify.com/v1/me";
        String type = getType();
        String token = currentUserParameters.getAccessToken();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.parse(searchUrl).newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String ResponseString = response.body().string();
                    getMe(ResponseString);
                }
            }

            ;
        });

    }

    private void getMe(String responseString) {

        try {
            JSONObject Jobject = new JSONObject(responseString);
            System.out.println(Jobject);
            String name = Jobject.getString("display_name");
            JSONObject followersobject = Jobject.getJSONObject("followers");
            Integer followers = followersobject.getInt("total");
            String country = Jobject.getString("country");
            String email = Jobject.getString("email");
            String product = Jobject.getString("product");
            String birthdate = Jobject.getString("birthdate");
            JSONArray images = Jobject.getJSONArray("images");
            JSONObject imagesObject = images.getJSONObject(0);
            String images_url = imagesObject.getString("url");
            if (images_url.equals("") || images_url == null)
                images_url = "";
            me = new Me(name, birthdate, country, email, followers, images_url, product);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        openMeFragment();
    }

    private void openSearchResultFragment(String responseString) {
        if (getType().equalsIgnoreCase("album")) {
            findAlbums(responseString);
            openAlbumFragment("noName");
        } else if (getType().equalsIgnoreCase("artist")) {
            findArtists(responseString);
            openArtistFragment();
        } else if (getType().equalsIgnoreCase("track")) {
            findTracks(responseString, 0);
            openTrackFragment("noName");
        } else if (getType().equalsIgnoreCase("playlist")) {
            findPlayLists(responseString);
            openPlaylistFragment();
        }

    }

    private void findPlaylistTracks(String responseString, Integer where) throws JSONException {
        try {
            tracks = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject tracksJson = Jobject.getJSONObject("tracks");
            JSONArray itemJArray = tracksJson.getJSONArray("items");
            final SharedPreferences prefs = getSharedPreferences(myprefs, Context.MODE_PRIVATE);
            String picture_url = prefs.getString("playlist_cover", null);
            System.out.println(itemJArray);
            for (int i = 0; i < itemJArray.length(); i++) {
                Track track = null;
                JSONObject trackItem = itemJArray.getJSONObject(i);
                JSONObject trackobject = trackItem.getJSONObject("track");
                String id = (String) trackobject .get("id");
                String name = (String) trackobject .get("name");
                JSONObject albumJson = trackobject .getJSONObject("album");
                JSONArray artists = albumJson.getJSONArray("artists");
                JSONObject artistItem = artists.getJSONObject(0);
                String artistName = (String) artistItem.get("name");
                System.out.println(where);
                if (where == 2) {
                    track = new Track(name, id, artistName, picture_url);
                }
                this.tracks.add(track);
                System.out.println(this.tracks.get(0).getTrackName());
            }
        } catch (Exception exception) {
            System.out.println("test");
            exception.printStackTrace();
        }
        JSONObject Jobject = new JSONObject(responseString);
        openTrackFragment(Jobject.getString("name"));
    }

    private void findTracks(String responseString, Integer where) {

        try {
            tracks = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject tracksJson = Jobject.getJSONObject("tracks");
            JSONArray itemJArray = tracksJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject trackItem = itemJArray.getJSONObject(i);
                String id = (String) trackItem.get("id");
                String name = (String) trackItem.get("name");
                JSONObject albumJson = trackItem.getJSONObject("album");
                JSONArray artists = albumJson.getJSONArray("artists");
                JSONObject artistItem = artists.getJSONObject(0);
                String artistName = (String) artistItem.get("name");
                Track track = null;
                if (where == 0) {
                    final SharedPreferences prefs = getSharedPreferences(myprefs, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.remove("album_cover");
                    edit.remove("playlist_cover");
                    edit.apply();
                    JSONArray picture_array = albumJson.getJSONArray("images");
                    JSONObject picture_object = picture_array.getJSONObject(0);
                    String picture_url = picture_object.getString("url");
                    System.out.println(picture_url);
                    track = new Track(name, id, artistName, picture_url);
                }
                this.tracks.add(track);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void findPlayLists(String responseString) {
        try {
            playLists = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject playlistJson = Jobject.getJSONObject("playlists");
            JSONArray itemJArray = playlistJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject playlistItem = itemJArray.getJSONObject(i);
                String id = (String) playlistItem.get("id");
                String name = (String) playlistItem.get("name");
                System.out.println(playlistItem);
                JSONArray picture = playlistItem.getJSONArray("images");
                JSONObject pictureobject = picture.getJSONObject(0);
                String picture_url = pictureobject.getString("url");
                System.out.println(picture_url);
                PlayList playList = new PlayList(name, id, picture_url);
                this.playLists.add(playList);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void findArtists(String responseString) {

        try {
            artists = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject artistsJson = Jobject.getJSONObject("artists");
            JSONArray itemJArray = artistsJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject artistItem = itemJArray.getJSONObject(i);
                String id = (String) artistItem.get("id");
                String name = (String) artistItem.get("name");
                JSONArray picture = artistItem.getJSONArray("images");
                System.out.println(picture);
                JSONObject pictureobject = picture.getJSONObject(2);
                String picture_url = pictureobject.getString("url");
                Artist artist = new Artist(name, id, picture_url);
                this.artists.add(artist);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void findAlbums(String responseString) {

        try {
            albums = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject albumsJson = Jobject.getJSONObject("albums");
            JSONArray itemJArray = albumsJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject albumItem = itemJArray.getJSONObject(i);
                String id = (String) albumItem.get("id");
                String name = (String) albumItem.get("name");
                JSONArray picture_array = albumItem.getJSONArray("images");
                JSONObject pictureObject = picture_array.getJSONObject(2);
                String picture_url = pictureObject.getString("url");
                Album album = new Album(name, id, picture_url);
                this.albums.add(album);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void openTrackFragment(String playlistName) {
        Fragment newTrackFragment = TrackFragment.newInstance(playlistName, tracks);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newTrackFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openPlaylistFragment() {
        Fragment newPlaylistFragment = PlaylistFragment.newInstance(playLists);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newPlaylistFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openAlbumFragment(String artistName) {
        Fragment newAlbumFragment = AlbumFragment.newInstance(artistName, albums);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newAlbumFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openArtistFragment() {
        Fragment newArtistFragment = ArtistFragment.newInstance(artists);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newArtistFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openMeFragment() {
        Intent mIntent = new Intent(this, BiometricActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("currentUserParameters", currentUserParameters);
        mBundle.putParcelable("me", me);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    private String getType() {
        String type = "album";
        int checkedButtonId = searchType.getCheckedRadioButtonId();
        if (checkedButtonId == R.id.searchByArtist) {
            type = "artist";
        } else if (checkedButtonId == R.id.searchByPlaylist) {
            type = "playlist";
        } else if (checkedButtonId == R.id.searchByTrack) {
            type = "track";
        }
        return type;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getTracks(String searchUrl, final boolean isForAlbum, final int i) {
        String token = currentUserParameters.getAccessToken();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.parse(searchUrl).newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String ResponseString = response.body().string();
                    if (isForAlbum) {
                        findAlbumTracks(ResponseString);
                    } else {
                        if (i == 0)
                            findTracks(ResponseString, i);
                        else {
                            System.out.println("test5");
                            try {
                                findPlaylistTracks(ResponseString, i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

            ;
        });

    }

    private void findAlbumTracks(String responseString) {
        try {
            tracks = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONArray itemJArray = Jobject.getJSONArray("items");
            final SharedPreferences prefs = getSharedPreferences(myprefs, Context.MODE_PRIVATE);
            String picture = prefs.getString("album_picture", null);
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject trackItem = itemJArray.getJSONObject(i);
                JSONArray artists = trackItem.getJSONArray("artists");
                JSONObject artistItem = artists.getJSONObject(0);
                String artistName = (String) artistItem.get("name");
                String id = (String) trackItem.get("id");
                String name = (String) trackItem.get("name");
                Track track = new Track(name, id, artistName, picture);
                this.tracks.add(track);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void getArtistAlbum(String artistId) {
        String searchUrl = "https://api.spotify.com/v1/artists/" + artistId + "/albums";
        String token = currentUserParameters.getAccessToken();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.parse(searchUrl).newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String ResponseString = response.body().string();
                    findAlbums(ResponseString);
                }
            }

            ;
        });
    }

    @Override
    public void onListFragmentAlbumInteraction(Album album) {
        String searchUrl = "https://api.spotify.com/v1/albums/" + album.getAlbumId() + "/tracks";
        getTracks(searchUrl, true, 1);
        openTrackFragment(album.getAlbumName());
    }

    @Override
    public void onListAlbumTrackFragmentInteraction(String trackId) {

    }

    @Override
    public void onListArtistFragmentInteraction(Artist artist) {
        getArtistAlbum(artist.getArtistId());
        openAlbumFragment(artist.getArtistName());
    }


    @Override
    public void onListPlaylistFragmentInteraction(PlayList playList) {
        String searchUrl = "https://api.spotify.com/v1/playlists/" + playList.getPlayListId();
        getTracks(searchUrl, false, 2);
    }
}
