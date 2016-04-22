package com.campustalk.developer.campustalk;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by khushali on 21/04/2016.
 */
public class DownloadDocument {

    public static void downloadDoc(final Context context,final String url){



        new AsyncTask<Void,Void,Void>() {

            DownloadManager downloadManager;
            DownloadManager.Request request;
            String filename;
            Uri uri;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                uri = Uri.parse(url);
                request = new DownloadManager.Request(uri);

            }

            @Override
            protected Void doInBackground(Void... params) {

                try

                {
                    System.out.println("url :" + url);

                    URL urlobj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urlobj.openConnection();
                    conn.connect();
                    conn.setInstanceFollowRedirects(false);

                    String filePath[] = url.split("\\\\");
                    filename = filePath[filePath.length-1];
                    System.out.println("file name :"+filename);
                }

                catch(Exception e)

                {

                    e.printStackTrace();

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                request.setDescription("CampusTalk Documents");
                request.setTitle(filename);
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename);
                request.setVisibleInDownloadsUi(true);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimeType = fileNameMap.getContentTypeFor(filename);
                /*ContentResolver cR = context.getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = cR.getType(uri);*/
                System.out.println("mime type :"+mimeType);
                request.setMimeType(mimeType);
                downloadManager.enqueue(request);

            }
        }.execute();




    }

}
