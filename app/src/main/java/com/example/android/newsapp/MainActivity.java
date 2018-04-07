package com.example.android.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.newsapp.Loader.DataLoader;
import com.example.android.newsapp.adapter.NewsAdapter;
import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.helper.Config;
import com.example.android.newsapp.helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<?>>, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<Article> articleList;
    private ProgressBar progressBar;
    private TextView infoText;
    private SwipeRefreshLayout refreshLayout;
    private boolean isFirstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupReferences();
        setupRecylerView();
        prepareLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {

            if(Utils.hasSectionsLoaded(this)){
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "No Section loaded yet. Please wait until sections are loaded",Toast.LENGTH_SHORT).show();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Setup for View references
     */
    private void setupReferences() {

        recyclerView = findViewById(R.id.recyclerview);
        infoText = findViewById(R.id.infotext);
        progressBar = findViewById(R.id.progressbar);
        refreshLayout = findViewById(R.id.swipetorefresh);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * Prepare Loading
     * call Start loading
     * if Inet available
     */
    private void prepareLoading() {
        if (Utils.isConnected(this)) {
            progressBar.setVisibility(View.VISIBLE);
            startLoading(isFirstStart);
        } else {
            progressBar.setVisibility(View.GONE);
            setInfo(Config.NOCONNECTION);
        }
    }

    /**
     * Start loading process
     *
     * @param isFirstStart boolean for firstStart
     */
    private void startLoading(boolean isFirstStart) {
        if (isFirstStart) {
            if(!Utils.hasSectionsLoaded(this))getSupportLoaderManager().initLoader(Config.SECTIONLOADERID, null, this);
            getSupportLoaderManager().initLoader(Config.ARTICLELOADERID, null, this);
        } else {
            if(!Utils.hasSectionsLoaded(this))getSupportLoaderManager().initLoader(Config.SECTIONLOADERID, null, this);
            getSupportLoaderManager().restartLoader(Config.ARTICLELOADERID, null, this);
        }
    }

    /**
     * Setup the RecylerView
     */
    private void setupRecylerView() {

        if (articleList == null) articleList = new ArrayList<>();
        newsAdapter = new NewsAdapter(MainActivity.this, articleList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    @NonNull
    @Override
    public Loader<List<?>> onCreateLoader(int id, @Nullable Bundle args) {

        boolean loadSection = false;

        if(id == Config.SECTIONLOADERID) loadSection = true;
        return new DataLoader(MainActivity.this,loadSection);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<?>> loader, List<?> data) {

        List<?> dataList = data;
        if(loader.getId() == Config.SECTIONLOADERID){
            Log.v(MainActivity.class.getSimpleName(), "onLoadFinished: " + data.toString());
            Utils.saveToPreferences(this, data);
        }else {
            setArticles((List<Article>) data);
        }

        Log.v(MainActivity.class.getSimpleName(), "onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<?>> loader) {

    }

    /**
     * Set Articles update the view visibility
     *
     * @param data Article List
     */
    private void setArticles(List<Article> data) {

        refreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);

        if (data != null && data.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.INVISIBLE);
            newsAdapter.updateData(data);
        } else if (data.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            infoText.setVisibility(View.VISIBLE);
            setInfo(Config.NOCONTENT);
        }
    }

    /**
     * Set the Info to the TextView
     *
     * @param problemType Type for
     *                    not connected
     *                    or no content
     */
    private void setInfo(int problemType) {

        String message = "";
        switch (problemType) {
            case Config.NOCONNECTION:
                message = getString(R.string.not_connected);
                break;
            case Config.NOCONTENT:
                message = getString(R.string.no_content);
                break;
        }
        infoText.setVisibility(View.VISIBLE);
        infoText.setText(message);
    }

    /**
     * Refresh data
     */
    @Override
    public void onRefresh() {

        if (Utils.isConnected(this)) {
            startLoading(false);
        } else {
            setInfo(Config.NOCONNECTION);
            recyclerView.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }


}
