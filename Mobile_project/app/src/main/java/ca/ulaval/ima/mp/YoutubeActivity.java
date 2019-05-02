package ca.ulaval.ima.mp;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class YoutubeActivity extends YouTubeBaseActivity {

    private static String API_KEY = "AIzaSyBOzHSNB8Hca862coYGHqmFntkkkUMh3w0";
    private String VIDEO_CODE = "srH-2pQdKhg";
    YouTubePlayerView player;
    private static final String key = "title";
    private String Title = "";
    private String Artist = "";
    private AlbumFragment.OnListFragmentAlbumInteractionListener mListener;
    private String YOUTUBE_ID;
    private static YouTube youtube;
    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

    private static final String APPLICATION_NAME = "ca.ulaval.ima.mp";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);



        Bundle args = getIntent().getExtras();
        if (args != null) {
            Title = args.getString(key, null);
            Artist = args.getString("artist", null);

            System.out.println(Title);
            System.out.println(Artist);
        }

        new Thread(new Runnable(){
            public void run() {
                try {
                    researchyt();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = new com.google.api.client.http.javanet.NetHttpTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void researchyt ()
            throws GeneralSecurityException, IOException, GoogleJsonResponseException, JSONException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list("snippet");
        SearchListResponse response = request.setKey(API_KEY)
                .setQ(Title.toString() + " "  + Artist + " vevo")
                .execute();

        JSONObject videofind = new JSONObject(response);
        System.out.println(videofind);
        JSONArray response_array = videofind.getJSONArray("items");
        JSONObject test = response_array.getJSONObject(0);
        System.out.println(test);
        JSONObject test2 = test.getJSONObject("id");

        String id = test2.getString("videoId");
        System.out.println(id);
        updateUI(id);
    }

    public void updateUI(final String id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_youtube);
                player = (YouTubePlayerView) findViewById(R.id.player);
                player.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if (!b) {
                            youTubePlayer.loadVideo(id);
                            youTubePlayer.setFullscreen(true);
                        }
                    }


                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

            }
        });
    }


}