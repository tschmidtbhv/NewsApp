package com.example.android.newsapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.Loader.ArticleLoader;
import com.example.android.newsapp.adapter.NewsAdapter;
import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.helper.Config;
import com.example.android.newsapp.helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<Article> articleList;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecylerView();

        if(Utils.isConnected(this)) {
            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);
            refreshLayout = findViewById(R.id.swipetorefresh);
            refreshLayout.setOnRefreshListener(this);
            getSupportLoaderManager().initLoader(Config.LOADERID, null, this);
        }else {
            setInfo(Config.NOCONNECTION);
        }
    }

    /**
     * Setup the RecylerView
     */
    private void setupRecylerView() {

        if(articleList == null) articleList = new ArrayList<>();
        newsAdapter = new NewsAdapter(MainActivity.this, articleList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    @NonNull
    @Override
    public Loader<List<Article>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ArticleLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Article>> loader, List<Article> data) {
        setArticles(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Article>> loader) {
        setArticles(new ArrayList<Article>());
    }

    private void setArticles(List<Article> data) {

        progressBar.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        if(data != null && data.size() > 0) {
            newsAdapter.updateData(data);
            recyclerView.setVisibility(View.VISIBLE);
            Log.v(MainActivity.class.getSimpleName(), "RESULT setArticles: " + data.size());
        }else {
            setInfo(Config.NOCONTENT);
        }
    }

    /**
     * Set the Info to the TextView
     * @param nocontent
     */
    private void setInfo(int nocontent) {

        String message = "";
        switch (nocontent){
            case 0:
                message = getString(R.string.not_connected);
                break;
            case 1:
                message = getString(R.string.no_content);
                break;
        }

        TextView infoText = findViewById(R.id.infotext);
        infoText.setText(message);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(Config.LOADERID,null,this);
    }
}
