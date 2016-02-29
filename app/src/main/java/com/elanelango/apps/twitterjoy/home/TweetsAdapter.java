package com.elanelango.apps.twitterjoy.home;

import android.content.Context;
import android.content.Intent;
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
import com.elanelango.apps.twitterjoy.models.User;

import org.parceler.Parcels;
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

        @Bind(R.id.tvName)
        TextView tvName;

        @Bind(R.id.tvScreenName)
        TextView tvScreenName;

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

        public void setTweet(final Tweet tweet) {
            tvName.setText(tweet.getUser().getName());
            tvText.setText(tweet.getText());
            tvTime.setText(tweet.getRelativeTime());
            tvScreenName.setText("@" + tweet.getUser().getScreenName());
            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .into(ivProfileImage);

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("user", Parcels.wrap(User.class, tweet.getUser()));
                    context.startActivity(i);
                }
            });
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
        int position = tweets.size();
        for (Tweet tweet : newTweets) {
            add(position, tweet);
            position++;
        }
    }

    public Tweet getLastTweet() {
        return get(tweets.size() - 1);
    }

    public Tweet getFirstTweet() {
        return get(0);
    }

    public Tweet get(int position) {
        int size = tweets.size();
        if (size > 0) {
            return tweets.get(position);
        } else {
            return null;
        }
    }

    public void add(int position, Tweet tweet) {
        tweets.add(position, tweet);
        notifyItemInserted(position);
    }

    public void addAllToFront(List<Tweet> newTweets) {
        int position = newTweets.size() - 1;
        for (int i = position; i >= 0; i--) {
            add(0, newTweets.get(position));
        }
    }
}
