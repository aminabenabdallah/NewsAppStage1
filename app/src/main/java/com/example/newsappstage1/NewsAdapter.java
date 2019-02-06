package com.example.newsappstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> NewsList) {
        super(context, 0, NewsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }


        // Bind the Views
        TextView articleTitle = convertView.findViewById(R.id.article_title);
        TextView articleAuthor = convertView.findViewById(R.id.article_author);
        TextView articleDate = convertView.findViewById(R.id.date_textview);

// Get the current article details
        News currentnews=getItem(position);
        // Get and set the associated String values
        articleTitle.setText(currentnews.getArticleTitle());
        articleAuthor.setText(currentnews.getArticleAuthor());
        articleDate.setText(currentnews.getArticleDate());
        return convertView;
    }
}
