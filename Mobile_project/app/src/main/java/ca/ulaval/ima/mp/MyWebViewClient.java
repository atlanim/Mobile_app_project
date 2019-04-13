package ca.ulaval.ima.mp;

import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

     private  String connectionCode = "";
    private OnGetCodeConnexion  mListener;
    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");
    };

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
    {
        String url = view.getUrl();
        return false;
    }
  /* MyJavaScriptInterface getjavaMyJavaScriptInterface(){
        return new MyJavaScriptInterface();
    }*/

   public static class MyJavaScriptInterface {
        private OnGetCodeConnexion mListener;
       public MyJavaScriptInterface(OnGetCodeConnexion mListener) {
           this.mListener = mListener;
       }

       @JavascriptInterface
        public void onUrlChange(String url) {
            if(url.contains("code=")){
                int index = url.lastIndexOf("code=");
               String connectionCode = url.substring(index + 5 );
                mListener.closeAndAskForToken(connectionCode);
            }


        }
    }

    public interface OnGetCodeConnexion{
        public void closeAndAskForToken(String ConnectionCode);
    }

}