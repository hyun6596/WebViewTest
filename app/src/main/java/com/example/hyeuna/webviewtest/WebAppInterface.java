package com.example.hyeuna.webviewtest;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;

/**
 * Created by hyeuna on 2017. 8. 25..
 */

public class WebAppInterface {

    Context mContext;
    DownloadManager downloadManager;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void externalNotify(String str) {

        if (str.equals( "scanBarcode" )) {
            Toast.makeText( mContext, str, Toast.LENGTH_SHORT ).show();

            new IntentIntegrator((MainActivity)mContext).initiateScan();

        } else {
            downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
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
