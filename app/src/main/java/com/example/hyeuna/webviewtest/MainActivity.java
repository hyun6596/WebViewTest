package com.example.hyeuna.webviewtest;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.loadUrl("http://www.briefplus.com:8080/BrothersOfWallStreet/index3.jsp");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult( requestCode, resultCode, data);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
        startActivity(intent);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void externalNotify(String str) {

            if (str.equals( "scanBarcode" )) {
                Toast.makeText( mContext, str, Toast.LENGTH_SHORT ).show();

                new IntentIntegrator(MainActivity.this).initiateScan();

            } else {
                downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                downloadURL( str );
            }
        }

        public void downloadURL(String url) {

            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request( uri );

            String fileName = new File( url ).getName();
            request.setTitle(fileName);
            request.setDescription(url);
            long id = downloadManager.enqueue(request);

        }
    }


}
