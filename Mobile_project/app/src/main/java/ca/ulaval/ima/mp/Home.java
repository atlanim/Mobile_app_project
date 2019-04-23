package ca.ulaval.ima.mp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity implements AlbumFragment.OnListFragmentAlbumInteractionListener, TrackFragment.OnListAlbumTrackFragmentInteractionListener {

    private EditText editTextSearch;
    private RadioGroup searchType;
    private Button search;
    private Button myPlayListButton;
    private UserParameters currentUserParameters;
    private ArrayList<Album> albums;
    private ArrayList<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        editTextSearch = findViewById(R.id.editTextSearch);
        searchType = findViewById(R.id.radioGroupSearchType);
        search = findViewById(R.id.serchButton);
        myPlayListButton = findViewById(R.id.buttonPlaylist);
        currentUserParameters = getIntent().getExtras().getParcelable("currentUserParameters");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        
        myPlayListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMyPlayList();
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
                .header("Authorization",  "Bearer " +token)
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
                    String     ResponseString = response.body().string();
                    openSearchResultFragment(ResponseString);
                }
            };
        });

    }

    private void openSearchResultFragment(String responseString) {
        String type = getType();
        findAlbums( responseString);
        if(type.equalsIgnoreCase("album")){
            openAlbumFragment("noName");
        }else if(type.equalsIgnoreCase("artist")){
            openArtistFragment();
        }else if(type.equalsIgnoreCase("track")){
            openTrackFragment("noName");
        }else if(type.equalsIgnoreCase("playlist")){
            openPlaylistFragment();
        }

    }

    private void findAlbums(String responseString){

        try{
            albums = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject albumsJson = Jobject.getJSONObject("albums");
            JSONArray itemJArray = albumsJson.getJSONArray("items");
           for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject albumItem = itemJArray.getJSONObject(i);
               String  id = (String)albumItem.get("id");
                String name = (String) albumItem.get("name");
                Album album = new Album(name, id);
                this.albums.add(album);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

    private void openTrackFragment(String playlistName) {
        Fragment newTrackFragment = TrackFragment.newInstance(playlistName, tracks);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newTrackFragment);
        fragmentTransaction.commit();
    }

    private void openPlaylistFragment() {
        Fragment newPlaylistFragment = new PlaylistFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newPlaylistFragment);
        fragmentTransaction.commit();
    }

    private void openAlbumFragment(String artistName){
        Fragment newAlbumFragment = AlbumFragment.newInstance(artistName, albums);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newAlbumFragment);
        fragmentTransaction.commit();
    }

    private void openArtistFragment(){
        Fragment newArtistFragment = new ConnectionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newArtistFragment);
        fragmentTransaction.commit();
    }

    private String getType(){
        String type = "album";
       int checkedButtonId = searchType.getCheckedRadioButtonId();
       if(checkedButtonId == R.id.searchByArtist ){
           type = "artist";
       }else if(checkedButtonId == R.id.searchByPlaylist){
           type = "playlist";
       }else if(checkedButtonId == R.id.searchByTrack){
           type = "track";
       }
        return  type;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getAlbumTracks(String albumId) {
        String searchUrl = "https://api.spotify.com/v1/albums/"+ albumId +"/tracks";
        String token = currentUserParameters.getAccessToken();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.parse(searchUrl).newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .header("Authorization",  "Bearer " +token)
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
                    String     ResponseString = response.body().string();
                    openAlbumTrackFragment(ResponseString);
                }
            };
        });

    }

    private void openAlbumTrackFragment(String responseString) {
        try{
            tracks = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONArray itemJArray = Jobject.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject albumItem = itemJArray.getJSONObject(i);
                String  id = (String)albumItem.get("id");
                String name = (String) albumItem.get("name");
                Track track = new Track(name, id);
                this.tracks.add(track);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onListFragmentAlbumInteraction(Album album) {
        getAlbumTracks(album.getAlbumId());
        openTrackFragment(album.getAlbumName());
    }

    @Override
    public void onListAlbumTrackFragmentInteraction(String trackId) {
       // playMusic(trackId)
    }
}
