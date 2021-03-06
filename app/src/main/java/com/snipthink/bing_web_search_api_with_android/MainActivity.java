package com.snipthink.bing_web_search_api_with_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    public TextView resltTextView;
    public TextView resultTextView;
    private ImageView resultImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultImageView = (ImageView) this.findViewById(R.id.imageView);

        SearchAsyncTask getNewsUpdate = new SearchAsyncTask();
        getNewsUpdate.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class SearchAsyncTask extends AsyncTask<Void, Void, Void> {

        private final String TAG = getClass().getName();

        private String mSearchStr;

        private int mNumOfResults = 0;

        private Callback mCallback;
        private BingSearchResults mBingSearchResults;
        private Error mError;


        @Override
        protected Void doInBackground(Void... params) {
            try {
                String searchStrr = URLEncoder.encode(mSearchStr);
                String searchStr = URLEncoder.encode(mSearchStr);
                String numOfResultsStr = mNumOfResults <= 0 ? "" : "&$top=" + mNumOfResults;
                String bingUrl = "7" + searchStr + "%27" + numOfResultsStr + "&$format=json";
                String accountKey = "xxxxxxxxx";

                byte[] accountKeyBytes;
                accountKeyBytes = Base64.encode((accountKey + ":" + accountKey).getBytes(),Base64.DEFAULT);
                String accountKeyEnc = new String(accountKeyBytes);

                URL url = null;
                url = new URL(bingUrl);

                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
                InputStream response = urlConnection.getInputStream();
                String res = readStream(response);

                Gson gson = (new GsonBuilder()).create();
                mBingSearchResults = gson.fromJson(res, BingSearchResults.class);
                System.out.println(res);
                Log.d(TAG, res);
                //conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                mError = new Error(e.getMessage(), e);
                //Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (mCallback != null) {
                mCallback.onComplete(mBingSearchResults, mError); //
            }

        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString(); //

//
        }

        public interface Callback {
            void onComplete(Object o, Error error);
        }

    }}