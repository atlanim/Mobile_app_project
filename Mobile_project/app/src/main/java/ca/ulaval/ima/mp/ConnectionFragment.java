package ca.ulaval.ima.mp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.MyWebViewClient.MyJavaScriptInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentConnectionInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectionFragment extends Fragment implements MyWebViewClient.OnGetCodeConnexion {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URI = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String connectionCode = "";
    private String accessToken = "";
    private boolean redirect = false;

    // TODO: Rename and change types of parameters
    private String mParamUri;
    private String mParam2;

    private OnFragmentConnectionInteractionListener mListener;
    private  String uriAuth = "https://accounts.spotify.com/authorize?client_id=1ebb5b8a927a4b5c8ecfe45c5a05c541&response_type=code&redirect_uri=https://www.google.com/" ;
    private String REDIRECT_URI = "https://www.google.com/";
    private String code = "";
    private WebView webView ;
    private String client_id = "1ebb5b8a927a4b5c8ecfe45c5a05c541";
    private String client_secret = "25b1637b05af4c7ba563c9b47da09eba";
    private String refreshToken = "";
    private String expiredTokenTime = "";

    public ConnectionFragment() {
    }

    public static ConnectionFragment newInstance(String param1) {
        ConnectionFragment fragment = new ConnectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URI, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamUri = getArguments().getString(ARG_URI);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_fragment, container, false);
        webView = view.findViewById(R.id.webView2);

        webView.getSettings().setJavaScriptEnabled(true);
        MyWebViewClient MyWebViewClient = new MyWebViewClient();
        webView.setWebViewClient( MyWebViewClient);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "android");
        webView.loadUrl(uriAuth);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentConnectionInteractionListener) {
            mListener = (OnFragmentConnectionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentConnectionInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void closeAndAskForToken(String code) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", "https://www.google.com/")
                .add("client_id", client_id)
                .add("client_secret", client_secret)

                .build();
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                {
                    setToken(response.code(), response.body().string());
                }
            }
        });

    }

    private void setToken(int code, String responseString) {
        if(code!=200){
            mListener.showAlertDiaglog();
        }else {
            try{
                JSONObject Jobject = new JSONObject(responseString);
                accessToken= Jobject.getString("access_token");
                refreshToken = Jobject.getString("refresh_token");
                expiredTokenTime = Jobject.getString("expires_in");
                UserParameters userParameters = new UserParameters(accessToken, refreshToken, expiredTokenTime);
                mListener.displayToast();
                mListener.openHomeFragment(userParameters);

            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

    }


    public interface OnFragmentConnectionInteractionListener {
        void onFragmentInteraction(Uri uri);
        void  displayToast();
        void openHomeFragment(UserParameters userParameters);
        void showAlertDiaglog();

    }


}
