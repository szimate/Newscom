package com.example.android.newscom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * {@link NewsAdapter} creates NewsViewHolder classes as needed and binds them to their data.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsItems;
    private Context context;

    public NewsAdapter(Context context, List<News> newsItems) {
        this.newsItems = newsItems;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {

        final News newsItem = newsItems.get(position);

        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#5ca4a4a4"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#5BC7C7C7"));
        }

        holder.title.setText(newsItem.getTitle());
        holder.section.setText(newsItem.getSection());
        String kaja = newsItem.getSection();
        for (int i = 0; i < 10; i++) {
            switch (kaja) {
                case "Sport":
                    holder.sectionImage.setImageResource(R.drawable.ic_action_ball);
                    break;
                case "Music":
                    holder.sectionImage.setImageResource(R.drawable.ic_action_music_1);
                    break;
                case "Opinion":
                case "Teacher Network":
                case "Politics":
                case "Society":
                    holder.sectionImage.setImageResource(R.drawable.ic_chat_bubble_outline);
                    break;
                case "Technology":
                case "Science":
                    holder.sectionImage.setImageResource(R.drawable.ic_wb_incandescent);
                    break;
                case "News":
                case "World news":
                    holder.sectionImage.setImageResource(R.drawable.ic_language);
            }
        }
        holder.author.setText(newsItem.getAuthor());
        holder.date.setText(newsItem.getDate());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(newsItem.getUrl());

                // Create a new intent to view the news item URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                context.startActivity(websiteIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView sectionImage;
        ConstraintLayout container;
        TextView title;
        TextView section;
        TextView author;
        TextView date;

        NewsViewHolder(View itemView) {
            super(itemView);

            sectionImage = itemView.findViewById(R.id.sectionImage);
            container = itemView.findViewById(R.id.container);

            title = itemView.findViewById(R.id.title);
            section = itemView.findViewById(R.id.section);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
        }
    }
}
