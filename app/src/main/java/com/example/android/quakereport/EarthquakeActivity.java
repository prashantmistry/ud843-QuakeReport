/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<WordList>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=10";
    private WordAdapter mAdapter;
    private TextView mEmptyTextView;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mAdapter = new WordAdapter(this, new ArrayList<WordList>());
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(mAdapter);

        mEmptyTextView = (TextView) findViewById(R.id.empty_textview);
        earthquakeListView.setEmptyView(mEmptyTextView);

        //Checking is internet is connected
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (!(mNetworkInfo != null && mNetworkInfo.isConnected())){
            mEmptyTextView.setText("No Internet");
        }

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordList earthquake = mAdapter.getItem(position);
                Uri webpage = Uri.parse(earthquake.getmUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });

        LoaderManager.getInstance(this).initLoader(0,null,this);


    }

    @NonNull
    @Override
    public Loader<ArrayList<WordList>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<WordList>> loader, ArrayList<WordList> earthquakes) {
        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
        }

        if (mNetworkInfo != null && mNetworkInfo.isConnected()){
            mEmptyTextView.setText("No Earthquakes Found");
        }

        ProgressBar spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<WordList>> loader) {
        mAdapter.clear();

    }


    public static class EarthquakeLoader extends AsyncTaskLoader<ArrayList<WordList>>{
        public EarthquakeLoader(Context context){
            super(context);
        }

        @Nullable
        @Override
        public ArrayList<WordList> loadInBackground() {
            ArrayList<WordList> earthquakes = QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL);
            return earthquakes;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

    }

}
