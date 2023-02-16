package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    String ShowOrHideWebViewInitialUse = "show";
    private WebView mWebView;
    private ImageView imageView;
    private TextView textView;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.splash_image);
//        webview.setWebViewClient(new CustomWebViewClient());
        mWebView = findViewById(R.id.activity_main_webview);
        textView = findViewById(R.id.textView);

        if(isNetworkAvailable()){
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);

            mWebView.setWebViewClient(new CustomWebViewClient());

//         REMOTE RESOURCE
            mWebView.loadUrl("https://seok.com.bd");

            // LOCAL RESOURCE
            // mWebView.loadUrl("file:///android_asset/index.html");

        }
        else {
            mWebView.setVisibility(mWebView.INVISIBLE);
            textView.setVisibility(textView.VISIBLE);
        }

    }
    // This allows for a splash screen
// (and hide elements once the page loads)
    private class CustomWebViewClient extends MyWebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewInitialUse.equals("show")) {
                webview.setVisibility(webview.INVISIBLE);
            }
        }
        @Override
        public void onReceivedError(WebView webview, WebResourceRequest request, WebResourceError error) {
            textView.setText("Error While Loading The App!");
            webview.setVisibility(webview.INVISIBLE);
            imageView.setVisibility(imageView.VISIBLE);
            textView.setVisibility(textView.VISIBLE);
        }

        @Override
        public void onReceivedHttpError(
                WebView webview, WebResourceRequest request, WebResourceResponse errorResponse) {
            textView.setText("HTTP Server Error!");
            webview.setVisibility(webview.INVISIBLE);
            imageView.setVisibility(imageView.VISIBLE);
            textView.setVisibility(textView.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView webview, SslErrorHandler handler,
                                       SslError error) {
            textView.setText("SSL Error!");
            webview.setVisibility(webview.INVISIBLE);
            imageView.setVisibility(imageView.VISIBLE);
            textView.setVisibility(textView.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView webview, String url) {
            ShowOrHideWebViewInitialUse = "hide";

            if (textView.getVisibility() == View.VISIBLE) {
                // visible
            } else{
                webview.setVisibility(mWebView.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
            super.onPageFinished(webview, url);



        }
    }
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
