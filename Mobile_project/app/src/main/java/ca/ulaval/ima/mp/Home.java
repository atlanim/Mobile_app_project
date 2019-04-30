package ca.ulaval.ima.mp;

import android.os.Bundle;
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

public class Home extends AppCompatActivity implements AlbumFragment.OnListFragmentAlbumInteractionListener,
        TrackFragment.OnListTrackFragmentInteractionListener,
        ArtistFragment.OnListFragmentArtistInteractionListener,
        PlaylistFragment.OnListPlayListFragmentInteractionListener{

    private EditText editTextSearch;
    private RadioGroup searchType;
    private Button search;
    private Button myPlayListButton;
    private UserParameters currentUserParameters;
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Track> tracks = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();
    private ArrayList<PlayList> playLists;

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
        if(getType().equalsIgnoreCase("album")){
            findAlbums( responseString);
            openAlbumFragment("noName");
        }else if(getType().equalsIgnoreCase("artist")){
            findArtists(responseString);
            openArtistFragment();
        }else if(getType().equalsIgnoreCase("track")){
            findTracks(responseString);
            openTrackFragment("noName");
        }else if(getType().equalsIgnoreCase("playlist")){
            findPlayLists(responseString);
            openPlaylistFragment();
        }

    }

    private void findTracks(String responseString) {

        try{
            tracks = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject tracksJson = Jobject.getJSONObject("tracks");
            JSONArray itemJArray = tracksJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject trackItem = itemJArray.getJSONObject(i);
                String  id = (String)trackItem.get("id");
                String name = (String) trackItem.get("name");
                JSONObject albumJson = trackItem.getJSONObject("album");
                JSONArray artists = albumJson.getJSONArray("artists");
                JSONObject artistItem = artists.getJSONObject(0);
                String artistName = (String) artistItem.get("name");
                Track track = new Track(name, id, artistName);
                this.tracks.add(track);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void findPlayLists(String responseString) {
        try{
            playLists = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject playlistJson = Jobject.getJSONObject("playlists");
            JSONArray itemJArray = playlistJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject playlistItem = itemJArray.getJSONObject(i);
                String  id = (String)playlistItem.get("id");
                String name = (String) playlistItem.get("name");
                PlayList playList = new PlayList(name, id);
                this.playLists.add(playList);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void findArtists(String responseString){

        try{
            artists = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONObject artistsJson = Jobject.getJSONObject("artists");
            JSONArray itemJArray = artistsJson.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject artistItem = itemJArray.getJSONObject(i);
                String  id = (String)artistItem.get("id");
                String name = (String) artistItem.get("name");
                Artist artist = new Artist(name, id);
                this.artists.add(artist);
            }
        }catch (Exception exception){
            exception.printStackTrace();
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

    private void openAlbumFragment(String artistName){
        Fragment newAlbumFragment = AlbumFragment.newInstance(artistName, albums);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newAlbumFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openArtistFragment(){
        Fragment newArtistFragment = ArtistFragment.newInstance(artists);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_activity, newArtistFragment);
        fragmentTransaction.addToBackStack(null);
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

    private void getTracks(String searchUrl, final boolean isForAlbum) {
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
                    if(isForAlbum){
                        findAlbumTracks(ResponseString);
                    }else{
                        findTracks(ResponseString);
                    }

                }
            };
        });

    }

    private void findAlbumTracks(String responseString) {
        try{
            tracks = new ArrayList<>();
            JSONObject Jobject = new JSONObject(responseString);
            JSONArray itemJArray = Jobject.getJSONArray("items");
            for (int i = 0; i < itemJArray.length(); i++) {
                JSONObject trackItem = itemJArray.getJSONObject(i);
                JSONArray artists = trackItem.getJSONArray("artists");
                JSONObject artistItem = artists.getJSONObject(0);
                String artistName = (String) artistItem.get("name");
                String  id = (String)trackItem.get("id");
                String name = (String) trackItem.get("name");
                Track track = new Track(name, id, artistName);
                this.tracks.add(track);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void getArtistAlbum(String artistId) {
        String searchUrl = "https://api.spotify.com/v1/artists/"+ artistId +"/albums";
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
                    findAlbums(ResponseString);
                }
            };
        });
    }

    @Override
    public void onListFragmentAlbumInteraction(Album album) {
        String searchUrl = "https://api.spotify.com/v1/albums/"+ album.getAlbumId() +"/tracks";
        getTracks(searchUrl, true);
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
        String searchUrl = "https://api.spotify.com/v1/playlists/"+ playList.getPlayListId() +"/tracks";
        getTracks(searchUrl, true);
        openTrackFragment(playList.getPlayListName());
    }
}
