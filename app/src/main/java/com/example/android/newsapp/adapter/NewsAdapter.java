package com.example.android.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.R;
import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.data.Tag;
import com.example.android.newsapp.helper.Utils;

import java.util.List;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article article = articleList.get(getLayoutPosition());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(article.getWebUrl()));
                    if (Utils.isAvailable(context, intent)) {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    private Context context;
    private List<Article> articleList;

    public NewsAdapter(Context context, List<Article> articleList) {
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

        if (article.getTags() != null && article.getTags().size() > 0) {
            Tag tag = article.getTags().get(0); //Get the first Author
            holder.author.setText(context.getString(R.string.written_by, tag.getFirstName(), tag.getLastName()));
        } else {
            holder.author.setText("");
        }
        holder.date.setText(article.getFormatedPublicationDate());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    /**
     * Update the RecylerView Data
     *
     * @param data
     */
    public void updateData(List<Article> data) {

        if (data != null) {
            articleList.clear();
            articleList.addAll(data);
            notifyDataSetChanged();
        }
    }
}
