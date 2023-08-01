package com.proj.votingclient.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes3.dex */
public class BitmapFromUrl {
    public static Bitmap BitmnapFetcher(String surl) {
        System.out.println("urlfrombitmap:" + surl);
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            System.out.println("Exeption aayi");
            Log.d("urltobitmap", e.toString());
            return null;
        }
    }
}
