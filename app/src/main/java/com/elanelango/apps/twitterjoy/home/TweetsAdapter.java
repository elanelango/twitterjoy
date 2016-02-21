package com.elanelango.apps.twitterjoy.home;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elanelango.apps.twitterjoy.R;
import com.elanelango.apps.twitterjoy.models.Tweet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eelango on 2/20/16.
 */
public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tweet> tweets;

    public enum TweetType {
        TEXT(0);

        private int val;

        TweetType(int v) {
            val = v;
        }
    }

    public static class TextHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivProfileImage)
        ImageView ivProfileImage;

        @Bind(R.id.tvUsername)
        TextView tvUsername;

        @Bind(R.id.tvText)
        TextView tvText;

        @Bind(R.id.tvTime)
        TextView tvTime;

        Context context;

        public TextHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        public void setTweet(Tweet tweet) {
            tvUsername.setText(tweet.getUser().getName());
            tvText.setText(tweet.getText());
            tvTime.setText(tweet.getRelativeTime());
            Glide.with((TimelineActivity) context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .into(ivProfileImage);
        }
    }

    public TweetsAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public int getItemViewType(int position) {
        return TweetType.TEXT.val;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tweetView = layoutInflater.inflate(R.layout.item_tweet, parent, false);
        return new TextHolder(context, tweetView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        TextHolder textHolder = (TextHolder) holder;
        textHolder.setTweet(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void addAll(List<Tweet> newTweets) {
        for (Tweet tweet : newTweets) {
            tweets.add(tweet);
            notifyItemInserted(tweets.size() - 1);
        }
    }
}
