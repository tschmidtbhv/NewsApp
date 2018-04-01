package com.example.android.newsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.R;
import com.example.android.newsapp.data.Tag;

import java.util.List;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView section;
        private TextView author;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            section = itemView.findViewById(R.id.section);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
        }
    }

    private Context context;
    private List<Article> articleList;

    public NewsAdapter(Context context, List<Article> articleList){
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newslist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {

        Article article = articleList.get(position);
        holder.title.setText(article.getWebTitle());
        holder.section.setText(article.getSectionName());

        if(article.getTags() != null && article.getTags().size() > 0) {
            Tag tag = article.getTags().get(0); //Get the first Author
            holder.author.setText(tag.getFirstName() + " " + tag.getLastName());
        }
        holder.date.setText(article.getWebPublicationDate());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public void updateData(List<Article> data) {
        if (data != null){
            articleList.clear();
            articleList.addAll(data);
            notifyDataSetChanged();
            Log.v(NewsAdapter.class.getSimpleName(), "updateData " + data.toString());
        }
    }
}
