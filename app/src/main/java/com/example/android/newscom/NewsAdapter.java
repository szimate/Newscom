package com.example.android.newscom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * {@link NewsAdapter} creates NewsViewHolder classes as needed and binds them to their data.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsItems;
    private Context context;

    /**
     * Constructs a new {@link NewsAdapter}
     *
     * @param context   of the app
     * @param newsItems is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> newsItems) {
        this.newsItems = newsItems;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {

        final News newsItem = newsItems.get(position);

        holder.title.setText(newsItem.getTitle());
        holder.section.setText(newsItem.getSection());

        holder.author.setText(newsItem.getAuthor());
        holder.date.setText(newsItem.getDate());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PackageManager packageManager = context.getPackageManager();

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(newsItem.getUrl());

                // Create a new intent to view the news item URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                //Check for web browser
                List<ResolveInfo> activities = packageManager.queryIntentActivities(websiteIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    // Send the intent to launch a new activity
                    context.startActivity(websiteIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    /**
     * Clear all data (a list of {@link News} objects)
     */
    public void clearAll() {
        newsItems.clear();
        notifyDataSetChanged();
    }

    /**
     * Add  a list of {@link News}
     *
     * @param newsList is the list of news, which is the data source of the adapter
     */
    public void addAll(List<News> newsList) {
        newsItems.clear();
        newsItems.addAll(newsList);
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView title;
        TextView section;
        TextView author;
        TextView date;

        NewsViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);

            title = itemView.findViewById(R.id.title);
            section = itemView.findViewById(R.id.section);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
        }
    }
}
